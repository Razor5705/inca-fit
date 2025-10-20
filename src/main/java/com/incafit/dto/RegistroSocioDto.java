package com.incafit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroSocioDto {


    @NotBlank(message = "El DNI es obligatorio", groups = BasicInfo.class)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio", groups = BasicInfo.class)
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio", groups = BasicInfo.class)
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos", groups = BasicInfo.class)
    private String telefono;

    @NotBlank(message = "El email es obligatorio", groups = BasicInfo.class)
    @Email(message = "Debe ser un email válido", groups = BasicInfo.class)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = BasicInfo.class)
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = BasicInfo.class)
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


    // Campos para el pago simulado
    @NotBlank(message = "El nombre en la tarjeta es obligatorio", groups = PaymentInfo.class)
    private String nombreTarjeta;
    
    @NotBlank(message = "El número de tarjeta es obligatorio", groups = PaymentInfo.class)
    @Pattern(regexp = "\\d{16}", message = "El número de tarjeta debe tener 16 dígitos", groups = PaymentInfo.class)
    private String numeroTarjeta;
    
    @NotBlank(message = "La fecha de caducidad es obligatoria", groups = PaymentInfo.class)
    @Pattern(regexp = "\\d{2}/\\d{2}", message = "La fecha debe tener el formato MM/YY", groups = PaymentInfo.class)
    private String fechaCaducidad;
    
    @NotBlank(message = "El CVV es obligatorio", groups = PaymentInfo.class)
    @Pattern(regexp = "\\d{3}", message = "El CVV debe tener 3 dígitos", groups = PaymentInfo.class)
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