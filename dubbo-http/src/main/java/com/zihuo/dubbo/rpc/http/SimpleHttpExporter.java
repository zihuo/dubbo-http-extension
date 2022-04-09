package com.zihuo.dubbo.rpc.http;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.protocol.AbstractExporter;

public class SimpleHttpExporter<T> extends AbstractExporter<T> {
    public SimpleHttpExporter(Invoker<T> invoker) {
        super(invoker);
    }
}
