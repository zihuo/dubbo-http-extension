package com.zihuo.producer.impl;

import com.zihuo.common.api.Hello;
import com.zihuo.common.api.UserInfo;

public class HelloImpl implements Hello {
    public UserInfo sayHello(String str) {
        return new UserInfo(str, 1);
    }
}
