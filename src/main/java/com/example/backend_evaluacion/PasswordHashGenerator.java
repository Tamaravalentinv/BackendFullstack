package com.example.backend_evaluacion;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para generar hashes BCrypt de contraseñas
 * Ejecuta este programa para obtener los hashes necesarios para usuarios de
 * prueba
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=".repeat(60));
        System.out.println("GENERADOR DE HASHES BCRYPT PARA USUARIOS DE PRUEBA");
        System.out.println("=".repeat(60));
        System.out.println();

        // Contraseñas de prueba
        String[] usuarios = {
                "admin", "admin123",
                "vendedor", "vendedor123",
                "cliente", "cliente123"
        };

        for (int i = 0; i < usuarios.length; i += 2) {
            String username = usuarios[i];
            String password = usuarios[i + 1];
            String hash = encoder.encode(password);

            System.out.println("Usuario: " + username);
            System.out.println("Password: " + password);
            System.out.println("BCrypt Hash:");
            System.out.println(hash);
            System.out.println();
            System.out.println("-".repeat(60));
            System.out.println();
        }

        System.out.println("SCRIPT SQL GENERADO:");
        System.out.println("=".repeat(60));
        System.out.println();

        // Generar nuevo script SQL
        System.out.println("-- Hash para admin / admin123");
        System.out.println(
                "UPDATE usuarios SET password = '" + encoder.encode("admin123") + "' WHERE username = 'admin';");
        System.out.println();

        System.out.println("-- Hash para vendedor / vendedor123");
        System.out.println(
                "UPDATE usuarios SET password = '" + encoder.encode("vendedor123") + "' WHERE username = 'vendedor';");
        System.out.println();

        System.out.println("-- Hash para cliente / cliente123");
        System.out.println(
                "UPDATE usuarios SET password = '" + encoder.encode("cliente123") + "' WHERE username = 'cliente';");
        System.out.println();
    }
}
