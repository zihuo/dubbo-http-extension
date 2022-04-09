package com.zihuo.dubbo.rpc.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.AbstractProtocol;
import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleHttpProtocol extends AbstractProtocol {

    private AtomicBoolean bStart = new AtomicBoolean(false);

    public int getDefaultPort() {
        return 20881;
    }

    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        SimpleHttpExporter<T> simpleHttpExporter = new SimpleHttpExporter<T>(invoker);
        createHttpServer(invoker);
        exporterMap.put(invoker.getInterface().getCanonicalName(), simpleHttpExporter);
        return simpleHttpExporter;
    }

    public <T> Invoker<T> refer(Class<T> aClass, URL url) throws RpcException {
        SimpleHttpInvoker<T> simpleHttpInvoker = new SimpleHttpInvoker<T>(aClass, url);
        invokers.add(simpleHttpInvoker);
        return simpleHttpInvoker;
    }

    private <T> void createHttpServer(Invoker<T> invoker) {
        try {
            if (bStart.compareAndSet(false, true)) {
                HttpServer httpServer = HttpServer.create();
                httpServer.bind(new InetSocketAddress(invoker.getUrl().getPort()), 10);
                httpServer.createContext(invoker.getUrl().getParameter("context"), new HttpHandler() {
                    public void handle(HttpExchange httpExchange) throws IOException {
                        InputStream requestBody = httpExchange.getRequestBody();
                        byte[] bytes = IOUtils.toByteArray(requestBody);
                        String s = new String(bytes, StandardCharsets.UTF_8);
                        RpcInvocation rpcInvocation = JSONObject.parseObject(s, RpcInvocation.class);
                        Exporter<?> exporter = exporterMap.get(rpcInvocation.getAttachment("interface"));
                        Result result = exporter.getInvoker().invoke(rpcInvocation);
                        bytes = JSONObject.toJSONString(result.getValue()).getBytes();
                        httpExchange.sendResponseHeaders(200, bytes.length);
                        OutputStream responseBody = httpExchange.getResponseBody();
                        responseBody.write(bytes);
                        responseBody.close();
                    }
                });
                httpServer.start();
            }
        } catch (IOException e) {
            throw new RpcException("Fail to start server(url: " + invoker.getUrl() + ") " + e.getMessage());
        }
    }
}
