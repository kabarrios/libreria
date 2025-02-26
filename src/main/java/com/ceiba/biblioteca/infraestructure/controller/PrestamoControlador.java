package com.ceiba.biblioteca.infraestructure.controller;


import com.ceiba.biblioteca.domain.model.gateway.IPrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("prestamo")
public class PrestamoControlador {
    @Autowired
    private IPrestamoService prestamoService;


    @GetMapping(path = "/{id-prestamo}")
    public Mono<String> pruebaServicio(@PathVariable("id-prestamo") Long id) throws Exception {
        return prestamoService.pruebaServicio(id);
    }

}

