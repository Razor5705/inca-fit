package com.incafit;

import com.incafit.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class IncaFitApplicationTests {


	@Mock
	private EmailService emailService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Mock
	private JavaMailSender javaMailSender;

	@Test
	void contextLoads() {
	}
}