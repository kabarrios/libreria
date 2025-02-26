package com.ceiba.biblioteca.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ProtocoloAMPQ {
    private Header header;
    private Properties properties;
    private Body body;

}
