package com.incafit.Repository;

import com.incafit.Model.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {
    Optional<Socio> findByEmail(String email);

    @Query("SELECT s FROM Socio s")
    List<Socio> findAllSocios();

    // Asegúrate de tener este método para guardar
    @Override
    <S extends Socio> S save(S entity);

    boolean existsByEmail(String email);

}