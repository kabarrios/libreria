package com.ceiba.biblioteca.infraestructure.repository;

import com.ceiba.biblioteca.domain.model.Prestamo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IPrestamoRepository extends CrudRepository<Prestamo, Long> {
    List<Prestamo> findByidentificacionUsuario(String idUsuario);
}
