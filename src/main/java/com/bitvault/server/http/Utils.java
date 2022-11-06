package com.bitvault.server.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;

import java.nio.charset.StandardCharsets;

class Utils {

    public static String formatBody(HttpContent httpContent) {
        StringBuilder responseData = new StringBuilder();
        ByteBuf content = httpContent.content();
        if (content.isReadable()) {
            responseData.append(content.toString(StandardCharsets.UTF_8).toUpperCase())
                    .append("\r\n");
        }
        return responseData.toString();
    }

}
