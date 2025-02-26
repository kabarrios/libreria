package com.ceiba.biblioteca.domain.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "prestamo")
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;

    @Size(max = 10)
    private String isbn;

    @Size(max = 10)
    private String identificacionUsuario;

    private int tipoUsuario;
    private String fechaMaximaDevolucion;
}
