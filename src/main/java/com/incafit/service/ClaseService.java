package com.incafit.service;

import com.incafit.Model.Clase;
import java.util.List;
import java.util.Optional;

public interface ClaseService {
    List<Clase> findAll();
    Optional<Clase> findById(Long id);
    Clase save(Clase clase);
    void deleteById(Long id);
}
