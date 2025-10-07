package com.incafit.service;


import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class ClaseServiceImpl implements ClaseService {


    private final ClaseRepository claseRepository;


    public ClaseServiceImpl(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }


    @Override
    public List<Clase> obtenerTodasLasClases() {
        return claseRepository.findAll();
    }


    @Override
    public Clase obtenerClasePorId(Long id) {
        return claseRepository.findById(id).orElse(null);
    }
}