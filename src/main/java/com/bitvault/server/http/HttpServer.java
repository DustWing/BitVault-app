package com.bitvault.server.http;

import com.bitvault.server.endpoints.EndpointResolver;
import com.bitvault.util.Result;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.HashSet;
import java.util.Set;

public class HttpServer {
    private final int mPort;
    private final EventLoopGroup mBossGroup;
    private final EventLoopGroup mWorkerGroup;
    private final ChannelFuture mChannelFuture;
    private final Set<ServerListener> serverListeners;


    public static HttpServer create(int port, EndpointResolver endpointResolver) {
        Set<ServerListener> serverListeners = new HashSet<>();
        return new HttpServer(port, serverListeners, endpointResolver);
    }

    private HttpServer(int port, Set<ServerListener> serverListeners, EndpointResolver endpointResolver) {
        this.mPort = port;
        this.serverListeners = serverListeners;


        // Configure the server.
        mBossGroup = new NioEventLoopGroup(1);
        mWorkerGroup = new NioEventLoopGroup();
        try {
            final ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);

            serverBootstrap.group(mBossGroup, mWorkerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer(serverListeners, endpointResolver));

            final Channel ch = serverBootstrap.bind(this.mPort).sync().channel();

//            System.err.println("Open your web browser and navigate to " +
//                    "http" + "://127.0.0.1:" + port + '/');

            mChannelFuture = ch.closeFuture();

        } catch (InterruptedException e) {
            mBossGroup.shutdownGracefully();
            mWorkerGroup.shutdownGracefully();
            throw new RuntimeException(e);
        }
    }

    public void addListener(ServerListener serverListener) {
        serverListeners.add(serverListener);
    }

    public void removeListener(ServerListener serverListener) {
        serverListeners.add(serverListener);
    }

    public void start() {
        try {
            mChannelFuture.sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            mBossGroup.shutdownGracefully();
            mWorkerGroup.shutdownGracefully();
        }

    }


    public void stop() {
        onMsg("SHUTTING DOWN - fu");
        mBossGroup.shutdownGracefully();
        mWorkerGroup.shutdownGracefully();
    }

    private void onMsg(String msg) {
        serverListeners.forEach(e -> e.onMessage(Result.ok(msg)));
    }

    public int getPort() {
        return mPort;
    }
}
