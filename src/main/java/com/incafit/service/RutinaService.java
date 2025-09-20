package com.incafit.service;

import com.incafit.Model.Rutina;
import java.util.List;
import java.util.Optional;

public interface RutinaService {
    List<Rutina> findAll();
    Optional<Rutina> findById(Long id);
    Rutina save(Rutina rutina);
    void deleteById(Long id);
}
