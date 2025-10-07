package com.incafit.service;

import com.incafit.Model.Socio;
import java.util.List;
import java.util.Optional;

public interface SocioService {
    List<Socio> obtenerTodosSocios();
    Socio obtenerSocioPorId(Long id);
    Socio guardarSocio(Socio socio);
    void eliminarSocio(Long id);
    void cambiarEstadoSocio(Long id, boolean estado);
    Optional<Socio> obtenerSocioPorEmail(String email); // Método añadido
    boolean existeEmail(String email); // Añade esta línea
    boolean existeDni(String dni); // Añade esta línea para DNI
}