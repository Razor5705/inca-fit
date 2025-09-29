package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.Repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DebugController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/debug/db")
    public String debugDatabase() {
        try {
            long count = socioRepository.count();
            List<Socio> socios = socioRepository.findAll();

            StringBuilder result = new StringBuilder();
            result.append("✅ Conexión a BD exitosa\n");
            result.append("📊 Total de socios: ").append(count).append("\n");
            result.append("👥 Socios en BD:\n");

            for (Socio socio : socios) {
                result.append("   - ID: ").append(socio.getId())
                        .append(", Email: ").append(socio.getEmail())
                        .append(", DNI: ").append(socio.getDni())
                        .append(", Activo: ").append(socio.isActivo())
                        .append("\n");
            }

            return result.toString();
        } catch (Exception e) {
            return "❌ Error en BD: " + e.getMessage();
        }
    }
}