package com.swxy.wfhrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wfh168
 * @createTime 2023-07-30
 */
@SpringBootApplication
@RestController
public class ApplicationConsumer {
    
    public static void main(String[] args) {
        SpringApplication.run(ApplicationConsumer.class,args);
    }
    
    @GetMapping("test")
    public String Hello(){
        return "hello provider";
    }
    
}
