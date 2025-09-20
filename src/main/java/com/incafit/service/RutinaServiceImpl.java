package com.incafit.service;

import com.incafit.Model.Rutina;
import com.incafit.Repository.RutinaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RutinaServiceImpl implements RutinaService {

    private final RutinaRepository rutinaRepository;

    public RutinaServiceImpl(RutinaRepository rutinaRepository) {
        this.rutinaRepository = rutinaRepository;
    }

    @Override
    public List<Rutina> findAll() {
        return rutinaRepository.findAll();
    }

    @Override
    public Optional<Rutina> findById(Long id) {
        return rutinaRepository.findById(id);
    }

    @Override
    public Rutina save(Rutina rutina) {
        return rutinaRepository.save(rutina);
    }

    @Override
    public void deleteById(Long id) {
        rutinaRepository.deleteById(id);
    }
}
