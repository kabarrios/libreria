package com.ceiba.biblioteca.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Header {
    private int deliveryMode;
    private int priority;
}
