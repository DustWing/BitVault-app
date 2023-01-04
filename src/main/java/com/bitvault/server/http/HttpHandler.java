package com.bitvault.server.http;/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import com.bitvault.server.endpoints.SecureItemController;
import com.bitvault.server.model.KeyDto;
import com.bitvault.server.model.ResultRsDto;
import com.bitvault.util.Json;
import com.bitvault.util.Result;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

class HttpHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest mRequest;

    private final Set<ServerListener> serverListeners;
    private final SecureItemController secureItemController;

    public HttpHandler(Set<ServerListener> serverListeners, SecureItemController secureItemController) {
        this.serverListeners = serverListeners;
        this.secureItemController = secureItemController;
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {


        final StringBuilder buf = new StringBuilder();

        if (msg instanceof HttpRequest request) {
            mRequest = request;
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
        }


        if (msg instanceof HttpContent httpContent) {

            ByteBuf content = httpContent.content();


            if (HttpMethod.GET.equals(mRequest.method())) {
                Result<KeyDto> get = secureItemController.get();
                KeyDto resultRsDto = get.get();
                Result<String> serialize = Json.serialize(resultRsDto);
                buf.append(serialize.get());

            }
            if (HttpMethod.POST.equals(mRequest.method())) {
                String body = content.toString(CharsetUtil.UTF_8);
                onMsg("BODY: %s".formatted(body));
                Result<ResultRsDto> post = secureItemController.post(body);

                if (post.isFail()) {
                    buf.append(post.getError().getMessage());
                }else {
                    ResultRsDto resultRsDto = post.get();
                    Result<String> serialize = Json.serialize(resultRsDto);
                    buf.append(serialize.get());
                }

            }


            if (msg instanceof LastHttpContent trailer) {
                onMsg("RESPONSE: %s".formatted(buf.toString()));
                writeResponse(mRequest, ctx, trailer, buf.toString());
            }
        }

    }

    private void writeResponse(
            HttpRequest request,
            ChannelHandlerContext ctx,
            LastHttpContent trailer,
            String responseData
    ) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1,
                trailer.decoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(responseData, CharsetUtil.UTF_8));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");

        if (keepAlive) {
            httpResponse.headers()
                    .setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            httpResponse.headers()
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(httpResponse);

        if (!keepAlive) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }


    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE, Unpooled.EMPTY_BUFFER);
        ctx.write(response);
    }

    private Map<String, List<String>> getQueryParams(HttpRequest request) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        return queryStringDecoder.parameters();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
        onError(cause);
    }

    private void onMsg(String msg) {
        serverListeners.forEach(e -> e.onMessage(msg));
    }

    private void onError(Throwable throwable) {
        serverListeners.forEach(e -> e.onError(throwable));
    }
}