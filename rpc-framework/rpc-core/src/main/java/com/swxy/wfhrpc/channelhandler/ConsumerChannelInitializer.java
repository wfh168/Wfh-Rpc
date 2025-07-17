package com.swxy.wfhrpc.channelhandler;

import com.swxy.wfhrpc.channelhandler.handler.MySimpleChannelInboundHandler;
import com.swxy.wfhrpc.channelhandler.handler.RpcRequestEncoder;
import com.swxy.wfhrpc.channelhandler.handler.RpcResponseDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author it楠老师
 * @createTime 2023-07-02
 */
public class ConsumerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
            // netty自带的日志处理器
            .addLast(new LoggingHandler(LogLevel.DEBUG))
            // 消息编码器
            .addLast(new RpcRequestEncoder())
            // 入栈的解码器
            .addLast(new RpcResponseDecoder())
            // 处理结果
            .addLast(new MySimpleChannelInboundHandler());
        
    }
}
