package com.incafit.service;

import com.incafit.Model.Membresia;
import java.util.List;

public interface MembresiaService {
    List<Membresia> obtenerTodasMembresias();
    Membresia guardarMembresia(Membresia membresia);
    Membresia obtenerMembresiaPorId(Long id);
    void eliminarMembresia(Long id);
}