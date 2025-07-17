package com.swxy.wfhrpc;


import com.swxy.wfhrpc.annotation.TryTimes;

/**
 * @author it楠老师
 * @createTime 2023-06-27
 */
public interface HelloRpc {

    /**
     * 通用接口，server和client都需要依赖
     * @param msg 发送的具体的消息
     * @return 返回的结果
     */
    @TryTimes(tryTimes = 3,intervalTime = 3000)
    String sayHi(String msg);

}
