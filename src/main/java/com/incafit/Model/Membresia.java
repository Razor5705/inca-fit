package com.incafit.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "membresias")
public class Membresia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoCobro tipoCobro;

    private BigDecimal precioBase;
    private Integer clasesIncluidas;
    private BigDecimal precioClaseExtra;

    public Membresia() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoCobro getTipoCobro() {
        return tipoCobro;
    }

    public void setTipoCobro(TipoCobro tipoCobro) {
        this.tipoCobro = tipoCobro;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }

    public Integer getClasesIncluidas() {
        return clasesIncluidas;
    }

    public void setClasesIncluidas(Integer clasesIncluidas) {
        this.clasesIncluidas = clasesIncluidas;
    }

    public BigDecimal getPrecioClaseExtra() {
        return precioClaseExtra;
    }

    public void setPrecioClaseExtra(BigDecimal precioClaseExtra) {
        this.precioClaseExtra = precioClaseExtra;
    }
}