package com.swxy.wfhrpc;

import com.swxy.wfhrpc.annotation.RpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author it楠老师
 * @createTime 2023-07-30
 */
@RestController
public class HelloController {
    
    // 需要注入一个代理对象
    @RpcService
    private HelloRpc helloRpc;
    
    @GetMapping("hello")
    public String hello(){
        return helloRpc.sayHi("provider");
    }
    
}
