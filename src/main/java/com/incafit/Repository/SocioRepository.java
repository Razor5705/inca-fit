package com.incafit.Repository;

import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    Optional<Socio> findByEmail(String email);

    @Query("SELECT s FROM Socio s")
    List<Socio> findAllSocios();

    @Query("SELECT new map(s.activo as activo, count(s) as total) FROM Socio s GROUP BY s.activo")
    List<Map<String, Object>> countSociosByActivo();

    boolean existsByEmail(String email);
}