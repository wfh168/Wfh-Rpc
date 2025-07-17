package com.swxy.wfhrpc.impl;

import com.swxy.wfhrpc.HelloRpc2;
import com.swxy.wfhrpc.annotation.RpcApi;


/**
 * @author wfh168
 * @createTime 2023-06-27
 */
@RpcApi
public class HelloRpcImpl2 implements HelloRpc2 {
    @Override
    public String sayHi(String msg) {
        return "hi consumer:" + msg;
    }
}
