package com.swxy.wfhrpc.exceptions;

/**
 * @author wfh168
 * @createTime 2023-06-29
 */
public class CompressException extends RuntimeException{
    
    public CompressException() {
    }
    
    public CompressException(String message) {
        super(message);
    }
    
    public CompressException(Throwable cause) {
        super(cause);
    }
}
