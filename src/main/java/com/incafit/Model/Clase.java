package com.incafit.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;


@Entity
@Table(name = "clases")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la clase no puede estar vacio")
    private String nombre;

    private String descripcion;

    @Min(value = 1, message = "La capacidad maxima debe ser al menos 1")
    private int capacidadMaxima;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    private LocalTime hora;
    private int duracionMinutos;

    @Convert(converter = DiaSemanaConverter.class)
    @Column(name = "dias_semana")
    private Set<DayOfWeek> diasSemana = EnumSet.noneOf(DayOfWeek.class);
    
    private boolean activo = true; // Por defecto las clases estan activas
    
    // Campos nuevos para clases con duracion limitada
    private LocalDate fechaInicio; // Fecha de inicio de la clase (para clases limitadas)
    private LocalDate fechaFin; // Fecha de fin de la clase (para clases limitadas)
    
    // Campo para clases que generen coste adicional
    private BigDecimal precioAdicional; // Precio adicional por reserva (null si no tiene coste adicional)

    // Constructores, getters y setters

    public Clase() {
    }

    public Clase(String nombre, String descripcion, Instructor instructor, LocalTime hora, int duracionMinutos, int capacidadMaxima) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instructor = instructor;
        this.hora = hora;
        this.duracionMinutos = duracionMinutos;
        this.capacidadMaxima = capacidadMaxima;
        this.activo = true; // Por defecto activa
    }
    
    public Clase(String nombre, String descripcion, Instructor instructor, LocalTime hora, int duracionMinutos, int capacidadMaxima, boolean activo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instructor = instructor;
        this.hora = hora;
        this.duracionMinutos = duracionMinutos;
        this.capacidadMaxima = capacidadMaxima;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Set<DayOfWeek> getDiasSemana() {
        if (diasSemana == null) {
            diasSemana = EnumSet.noneOf(DayOfWeek.class);
        }
        return diasSemana;
    }

    public void setDiasSemana(Set<DayOfWeek> diasSemana) {
        if (diasSemana == null || diasSemana.isEmpty()) {
            this.diasSemana = EnumSet.noneOf(DayOfWeek.class);
        } else {
            this.diasSemana = EnumSet.copyOf(diasSemana);
        }
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public BigDecimal getPrecioAdicional() {
        return precioAdicional;
    }

    public void setPrecioAdicional(BigDecimal precioAdicional) {
        this.precioAdicional = precioAdicional;
    }
    
    // Metodos de utilidad
    public boolean isVigente() {
        if (fechaInicio == null || fechaFin == null) {
            return true; // Si no tiene fechas, es permanente
        }
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }
    
    public boolean tienePrecioAdicional() {
        return precioAdicional != null && precioAdicional.compareTo(BigDecimal.ZERO) > 0;
    }
}
