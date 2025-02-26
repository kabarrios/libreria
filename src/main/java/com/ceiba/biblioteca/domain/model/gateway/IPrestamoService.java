package com.ceiba.biblioteca.domain.model.gateway;

import reactor.core.publisher.Mono;

public interface IPrestamoService {
    Mono<String> pruebaServicio(Long id) throws Exception;

}
