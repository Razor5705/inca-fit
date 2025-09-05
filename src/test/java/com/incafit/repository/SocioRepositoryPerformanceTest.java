package com.incafit.repository;

import com.incafit.Model.Membresia;
import com.incafit.Model.Socio;
import com.incafit.Repository.MembresiaRepository;
import com.incafit.Repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class SocioRepositoryPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(SocioRepositoryPerformanceTest.class);

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private MembresiaRepository membresiaRepository;

    @BeforeEach
    void setUp() {
        Membresia membresia = new Membresia();
        membresia.setNombre("Gold");
        membresiaRepository.save(membresia);

        for (int i = 0; i < 10; i++) {
            String dni = String.format("%08d", i);
            Socio socio = new Socio(dni, "Socio " + i, "socio" + i + "@example.com", "password");
            socio.setMembresia(membresia);
            socioRepository.save(socio);
        }
    }

    @Test
    void testFindAllDefault() {
        logger.info("--- Testing findAllDefault ---");
        long startTime = System.nanoTime();
        List<Socio> socios = socioRepository.findAll();
        long endTime = System.nanoTime();
        logger.info("Execution time: {} ms", (endTime - startTime) / 1_000_000);
        // Accessing membresia to trigger lazy loading
        socios.forEach(s -> s.getMembresia().getNombre());
    }

    @Test
    void testFindAllWithMembresia() {
        logger.info("--- Testing findAllWithMembresia ---");
        long startTime = System.nanoTime();
        List<Socio> socios = socioRepository.findAllWithMembresia();
        long endTime = System.nanoTime();
        logger.info("Execution time: {} ms", (endTime - startTime) / 1_000_000);
        // Accessing membresia (should be already fetched)
        socios.forEach(s -> s.getMembresia().getNombre());
    }
}
