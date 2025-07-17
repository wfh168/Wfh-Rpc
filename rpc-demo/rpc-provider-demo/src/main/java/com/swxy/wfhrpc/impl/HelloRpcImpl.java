package com.swxy.wfhrpc.impl;


import com.swxy.wfhrpc.HelloRpc;
import com.swxy.wfhrpc.annotation.RpcApi;

/**
 * @author wfh168
 * @createTime 2023-06-27
 */
@RpcApi(group = "primary")
public class HelloRpcImpl implements HelloRpc {
    @Override
    public String sayHi(String msg) {
        return "hi consumer:" + msg;
    }
}
