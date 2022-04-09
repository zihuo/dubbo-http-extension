package com.zihuo.dubbo.rpc.http;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.protocol.AbstractInvoker;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.*;

public class SimpleHttpInvoker<T> extends AbstractInvoker<T> {
    private static OkHttpClient client = new OkHttpClient();
    public SimpleHttpInvoker(Class<T> type, URL url) {
        super(type, url);
    }

    protected Result doInvoke(Invocation invocation) throws Throwable {
        RpcInvocation inv = (RpcInvocation)invocation;
        URL url = getUrl();
        inv.getAttachments().put("interface", getInterface().getCanonicalName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), JSONObject.toJSONString(inv));
        Request request = new Request.Builder().url("http://" + url.getAddress() + url.getParameter("context")).post(requestBody).build();
        Call call = client.newCall(request);
        Response res = call.execute();
        ResponseBody body = res.body();
        RpcResult rpcResult = new RpcResult();
        rpcResult.setValue( JSONObject.parseObject(body.string(), RpcUtils.getReturnType(invocation)));
        return rpcResult;
    }
}
