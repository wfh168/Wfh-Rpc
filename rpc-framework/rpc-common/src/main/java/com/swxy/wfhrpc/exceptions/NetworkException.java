package com.swxy.wfhrpc.exceptions;

/**
 * @author wfh168
 * @createTime 2023-06-29
 */
public class NetworkException extends RuntimeException{
    
    public NetworkException() {
    }
    
    public NetworkException(String message) {
        super(message);
    }
    
    public NetworkException(Throwable cause) {
        super(cause);
    }
}
