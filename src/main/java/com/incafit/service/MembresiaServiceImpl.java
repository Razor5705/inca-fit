package com.incafit.service;

import com.incafit.Model.Membresia;
import com.incafit.Repository.MembresiaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MembresiaServiceImpl implements MembresiaService {

    private final MembresiaRepository membresiaRepository;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepository) {
        this.membresiaRepository = membresiaRepository;
    }

    @Override
    public List<Membresia> findAll() {
        return membresiaRepository.findAll();
    }

    @Override
    public Optional<Membresia> findById(Long id) {
        return membresiaRepository.findById(id);
    }

    @Override
    public Membresia save(Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    @Override
    public void deleteById(Long id) {
        membresiaRepository.deleteById(id);
    }
}