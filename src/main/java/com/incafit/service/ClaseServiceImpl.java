package com.incafit.service;

import com.incafit.Model.Clase;
import com.incafit.Repository.ClaseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseServiceImpl implements ClaseService {

    private final ClaseRepository claseRepository;

    public ClaseServiceImpl(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @Override
    public List<Clase> findAll() {
        return claseRepository.findAll();
    }

    @Override
    public Optional<Clase> findById(Long id) {
        return claseRepository.findById(id);
    }

    @Override
    public Clase save(Clase clase) {
        return claseRepository.save(clase);
    }

    @Override
    public void deleteById(Long id) {
        claseRepository.deleteById(id);
    }
}
