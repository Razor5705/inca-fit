package com.incafit.dto;

import com.incafit.validation.DatosPersonales;
import com.incafit.validation.PagoInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroSocioDto {

    @NotBlank(message = "El DNI es obligatorio", groups = DatosPersonales.class)
    @Size(min = 8, max = 10, message = "El DNI debe tener entre 8 y 10 caracteres", groups = DatosPersonales.class)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio", groups = DatosPersonales.class)
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio", groups = DatosPersonales.class)
    private String telefono;

    @NotBlank(message = "El email es obligatorio", groups = DatosPersonales.class)
    @Email(message = "Debe ser un email válido", groups = DatosPersonales.class)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria", groups = DatosPersonales.class)
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres", groups = DatosPersonales.class)
    private String password;

    private String passwordConfirm;

    private Long membresiaId;

    // Campos para el pago simulado
    @NotBlank(message = "El nombre en la tarjeta es obligatorio", groups = PagoInfo.class)
    private String nombreTarjeta;

    @NotBlank(message = "El número de tarjeta es obligatorio", groups = PagoInfo.class)
    @Pattern(regexp = "^\\d{16}$", message = "El número de tarjeta debe tener 16 dígitos", groups = PagoInfo.class)
    private String numeroTarjeta;

    @NotBlank(message = "La fecha de caducidad es obligatoria", groups = PagoInfo.class)
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "El formato debe ser MM/AA", groups = PagoInfo.class)
    private String fechaCaducidad;

    @NotBlank(message = "El CVV es obligatorio", groups = PagoInfo.class)
    @Pattern(regexp = "^\\d{3,4}$", message = "El CVV debe tener 3 o 4 dígitos", groups = PagoInfo.class)
    private String cvv;

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