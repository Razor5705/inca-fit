package com.incafit.Repository;

import com.incafit.Model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {
    
    // Buscar todas las clases activas
    List<Clase> findByActivoTrue();
    
    // Buscar todas las clases inactivas
    List<Clase> findByActivoFalse();
    
    // Buscar clases activas por instructor
    List<Clase> findByActivoTrueAndInstructorId(Long instructorId);

    Optional<Clase> findFirstByInstructorIdAndHora(Long instructorId, LocalTime hora);

}
