package com.bitvault.server.endpoints;

import com.bitvault.server.dto.ErrorDto;
import com.bitvault.util.DateTimeUtils;
import io.netty.handler.codec.http.HttpResponseStatus;

public class EndpointException extends RuntimeException {

    private final HttpResponseStatus status;
    private final String details;

    public static EndpointException createFromException(Exception exception) {
        return new EndpointException(
                "Something wend wrong!",
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage()
        );
    }

    public EndpointException(String message, HttpResponseStatus status, String details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public ErrorDto convertToDto() {
        return new ErrorDto(
                DateTimeUtils.getTimeNow(),
                this.getMessage(),
                this.details
        );
    }
}
