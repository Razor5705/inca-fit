package com.incafit.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "instructores")
public class Instructor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreCompleto;


    private String especialidad;


    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    private String telefono;
    private String experiencia;


    // Constructores, getters y setters


    public Instructor() {
    }


    public Instructor(String nombreCompleto, String especialidad, String email) {
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.email = email;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getNombreCompleto() {
        return nombreCompleto;
    }


    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }


    public String getEspecialidad() {
        return especialidad;
    }


    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }
}