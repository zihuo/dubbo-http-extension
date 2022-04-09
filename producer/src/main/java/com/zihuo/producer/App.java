package com.zihuo.producer;

import com.alibaba.fastjson.JSONObject;
import com.zihuo.common.api.UserInfo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("dubbo.xml");
        System.in.read();
    }
}
