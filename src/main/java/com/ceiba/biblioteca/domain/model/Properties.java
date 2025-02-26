package com.ceiba.biblioteca.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Properties {
    private int contentType;
    private byte[] correlationId;
    private String replyTo;
}
