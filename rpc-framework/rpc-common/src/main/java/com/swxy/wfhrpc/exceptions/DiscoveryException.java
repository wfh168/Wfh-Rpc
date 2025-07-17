package com.swxy.wfhrpc.exceptions;

/**
 * @author wfh168
 * @createTime 2023-06-29
 */
public class DiscoveryException extends RuntimeException{
    
    public DiscoveryException() {
    }
    
    public DiscoveryException(String message) {
        super(message);
    }
    
    public DiscoveryException(Throwable cause) {
        super(cause);
    }
}
