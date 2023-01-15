package com.bitvault.server.http;

import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.server.endpoints.SecureItemController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

import java.util.Set;

class HttpServerInitializer extends ChannelInitializer<SocketChannel> {


    private final Set<ServerListener> serverListeners;
    private final EndpointResolver endpointResolver;


    public HttpServerInitializer(Set<ServerListener> serverListeners, EndpointResolver endpointResolver) {
        this.serverListeners = serverListeners;
        this.endpointResolver = endpointResolver;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        final ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpHandler(serverListeners, endpointResolver));
    }
}
