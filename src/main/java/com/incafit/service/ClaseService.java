package com.incafit.service;

import com.incafit.Model.Clase;
import java.util.List;

public interface ClaseService {
    List<Clase> obtenerTodasLasClases();
    Clase obtenerClasePorId(Long id);
}