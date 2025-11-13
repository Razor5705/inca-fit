package com.incafit.Config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashRunner {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: PasswordHashRunner <password_plano>");
            System.exit(0);
        }

        String raw = args[0];
        String hash = new BCryptPasswordEncoder().encode(raw);
        System.out.println("Hash BCrypt para \"" + raw + "\":");
        System.out.println(hash);
    }
}
