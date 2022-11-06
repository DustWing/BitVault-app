package com.bitvault.server.http;

import com.bitvault.server.ServerListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

import java.util.Set;

class HttpServerInitializer extends ChannelInitializer<SocketChannel> {


    private final Set<ServerListener> serverListeners;

    public HttpServerInitializer(Set<ServerListener> serverListeners) {
        this.serverListeners = serverListeners;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        final ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpHandler(serverListeners));
    }
}
