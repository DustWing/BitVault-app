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

import com.bitvault.server.dto.ErrorDto;
import com.bitvault.server.endpoints.EndpointException;
import com.bitvault.server.endpoints.EndpointResolver;
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
    private final EndpointResolver endpointProvider;

    public HttpHandler(Set<ServerListener> serverListeners, EndpointResolver endpointProvider) {
        this.serverListeners = serverListeners;
        this.endpointProvider = endpointProvider;
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {


        if (msg instanceof HttpRequest request) {
            mRequest = request;
            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
        }


        if (msg instanceof HttpContent httpContent) {

            ByteBuf content = httpContent.content();

            Response response;

            if (HttpMethod.GET.equals(mRequest.method())) {

                Result<?> result = endpointProvider.get(mRequest.uri());
                response = createResponseBody(result);

            } else if (HttpMethod.POST.equals(mRequest.method())) {
                String body = content.toString(CharsetUtil.UTF_8);
                onMsg("REQUEST BODY: %s".formatted(body));
                Result<?> result = endpointProvider.post(mRequest.uri(), body);
                response = createResponseBody(result);

            } else {
                response = Response.createServerError("Could not handle HttpMethod - Use GET or POST");
            }

            if (msg instanceof LastHttpContent) {
                onMsg("RESPONSE: %s".formatted(response.body));
                writeResponse(mRequest, ctx, response);
            }
        }

    }

    private Response createResponseBody(Result<?> result) {
        if (result.hasError()) {

            Exception error = result.getError();

            if (error instanceof EndpointException endpointException) {
                return Response.fromEndpointException(endpointException);
            }

            EndpointException fromException = EndpointException.createFromException(error);
            return Response.fromEndpointException(fromException);
        }

        Result<String> serializeResult = Json.serialize(result.get());
        if (serializeResult.hasError()) {
            return Response.createServerError(serializeResult.getError().getMessage());
        }
        String body = serializeResult.get();
        return Response.createOk(body);

    }


    private void writeResponse(
            HttpRequest request,
            ChannelHandlerContext ctx,
            Response response
    ) {
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(
                HTTP_1_1,
                response.status,
                Unpooled.copiedBuffer(response.body, CharsetUtil.UTF_8)
        );

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            httpResponse.headers()
                    .setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            // Add keep alive header as per:
            // - https://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            httpResponse.headers()
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Write the response.
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
        serverListeners.forEach(e -> e.onMessage(Result.ok(msg)));
    }

    private void onError(Throwable throwable) {
        serverListeners.forEach(e -> e.onMessage(Result.error(new Exception(throwable))));
    }

    private record Response(String body, HttpResponseStatus status) {
        public static Response createOk(String body) {
            return new Response(body, OK);
        }

        public static Response createServerError(String body) {
            return new Response(body, INTERNAL_SERVER_ERROR);
        }

        public static Response fromEndpointException(EndpointException endpointException) {

            ErrorDto errorDto = endpointException.convertToDto();

            Result<String> serializeResult = Json.serialize(errorDto);
            if (serializeResult.hasError()) {
                return Response.createServerError(serializeResult.getError().getMessage());
            }

            return new Response(serializeResult.get(), endpointException.getStatus());
        }
    }
}