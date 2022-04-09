package com.zihuo.consumer;

import com.zihuo.common.api.Hello;
import com.zihuo.common.api.Hi;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class app {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("dubbo.xml");
        Hi hi = classPathXmlApplicationContext.getBean("hi", Hi.class);
        System.out.println(hi.sayHi("123"));
        Hello hello = classPathXmlApplicationContext.getBean("hello", Hello.class);
        System.out.println(hello.sayHello("456"));

    }
}
