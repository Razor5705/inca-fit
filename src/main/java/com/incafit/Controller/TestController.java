package com.incafit.Controller;

import com.incafit.Repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/test-db")
    public String testDatabase() {
        try {
            long count = socioRepository.count();
            return "Conexión exitosa. Socios en BD: " + count;
        } catch (Exception e) {
            return "Error en BD: " + e.getMessage();
        }
    }
}
