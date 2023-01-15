package com.bitvault.server.dto;

public record ErrorDto(
        String dateTime,
        String message,
        String details

) {
}
