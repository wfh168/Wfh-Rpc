package com.swxy.wfhrpc.impl;

import com.swxy.wfhrpc.HelloRpc2;
import com.swxy.wfhrpc.annotation.RpcApi;

/**
 * @author it楠老师
 * @createTime 2023-06-27
 */
@RpcApi
public class HelloYrpcImpl2 implements HelloRpc2 {
    @Override
    public String sayHi(String msg) {
        return "hi consumer:" + msg;
    }
}
