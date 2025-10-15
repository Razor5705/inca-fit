package com.incafit.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegistroSocioDto {


    @NotBlank(message = "El DNI es obligatorio")
    private String dni;


    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;


    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;


    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;


    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;


    private String passwordConfirm;


    private Long membresiaId;


    // Getters y Setters


    public String getDni() {
        return dni;
    }


    public void setDni(String dni) {
        this.dni = dni;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getPasswordConfirm() {
        return passwordConfirm;
    }


    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


    public Long getMembresiaId() {
        return membresiaId;
    }


    public void setMembresiaId(Long membresiaId) {
        this.membresiaId = membresiaId;
    }


    // Campos para el pago simulado (sin validaciones @NotBlank)
    private String nombreTarjeta;
    private String numeroTarjeta;
    private String fechaCaducidad;
    private String cvv;




    public String getNombreTarjeta() {
        return nombreTarjeta;
    }


    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }


    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }


    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }


    public String getFechaCaducidad() {
        return fechaCaducidad;
    }


    public void setFechaCaducidad(String fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }


    public String getCvv() {
        return cvv;
    }


    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}