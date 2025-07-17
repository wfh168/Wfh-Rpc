package com.swxy.wfhrpc.loadbanlancer;


import java.net.InetSocketAddress;

/**
 * @author wfh168
 * @createTime 2023-07-06
 */
public interface Selector {
    
    /**
     * 根据服务列表执行一种算法获取一个服务节点
     * @return 具体的服务节点
     */
    InetSocketAddress getNext();
    
}
