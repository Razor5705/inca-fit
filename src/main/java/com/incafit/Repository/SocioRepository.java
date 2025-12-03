package com.incafit.Repository;

import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    Optional<Socio> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    // Método para verificar existencia por ID
    boolean existsById(Long id);
    // Método adicional para debugging
    @Query("SELECT COUNT(s) FROM Socio s")
    long countSocios();
}
