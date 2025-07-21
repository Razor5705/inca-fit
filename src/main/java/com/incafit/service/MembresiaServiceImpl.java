package com.incafit.service;

import com.incafit.Model.Membresia;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MembresiaServiceImpl implements MembresiaService {

    private final MembresiaRepository membresiaRepository;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    public List<Membresia> obtenerTodasMembresias() {
        return membresiaRepository.findAll();
    }

    @Override
    public Membresia guardarMembresia(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    @Override
    public Membresia obtenerMembresiaPorId(Long id) {
        return membresiaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarMembresia(Long id) {
        membresiaRepository.deleteById(id);
    }
}