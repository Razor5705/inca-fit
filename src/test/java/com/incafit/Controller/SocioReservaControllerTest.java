package com.incafit.Controller;

import com.incafit.Model.Socio;
import com.incafit.service.FacturaService;
import com.incafit.service.ReservaService;
import com.incafit.service.SocioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SocioReservaController.class)
public class SocioReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SocioService socioService;

    @Mock
    private ReservaService reservaService;

    @Mock
    private FacturaService facturaService;

    @BeforeEach
    void setUp() {
        Socio mockSocio = new Socio();
        mockSocio.setId(1L);
        when(socioService.obtenerSocioPorEmail("user@example.com")).thenReturn(Optional.of(mockSocio));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testListarReservas() throws Exception {
        mockMvc.perform(get("/socio/reservas"))
                .andExpect(status().isOk())
                .andExpect(view().name("socio/reservas/lista"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testGuardarReserva() throws Exception {
        mockMvc.perform(post("/socio/reservas/guardar")
                        .with(csrf())
                        .param("clase", "Yoga")
                        .param("fechaHora", "2025-10-10T10:00:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/socio/reservas"));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void testCancelarReserva() throws Exception {
        mockMvc.perform(post("/socio/reservas/1/cancelar")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/socio/reservas"));
    }
}