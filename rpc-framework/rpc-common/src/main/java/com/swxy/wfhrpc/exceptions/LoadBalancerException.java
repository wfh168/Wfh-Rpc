package com.swxy.wfhrpc.exceptions;

/**
 * @author wfh168
 * @createTime 2023-07-06
 */
public class LoadBalancerException extends RuntimeException {
    
    public LoadBalancerException(String message) {
        super(message);
    }
    
    public LoadBalancerException() {
    }
}
