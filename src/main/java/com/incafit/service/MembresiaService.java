package com.incafit.service;

import com.incafit.Model.Membresia;
import java.util.List;
import java.util.Optional;

public interface MembresiaService {
    List<Membresia> findAll();
    Optional<Membresia> findById(Long id);
    Membresia save(Membresia membresia);
    void deleteById(Long id);
}