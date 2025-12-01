package com.example.backend_evaluacion.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHashBCrypt {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("=".repeat(70));
        System.out.println("HASHES BCRYPT PARA USUARIOS DE PRUEBA");
        System.out.println("=".repeat(70));
        System.out.println();

        // Generar hashes
        String hashAdmin = encoder.encode("admin123");
        String hashVendedor = encoder.encode("vendedor123");
        String hashCliente = encoder.encode("cliente123");

        System.out.println("-- Usuario ADMIN (admin / admin123)");
        System.out.println("UPDATE usuarios SET password = '" + hashAdmin + "' WHERE username = 'admin';");
        System.out.println();

        System.out.println("-- Usuario VENDEDOR (vendedor / vendedor123)");
        System.out.println("UPDATE usuarios SET password = '" + hashVendedor + "' WHERE username = 'vendedor';");
        System.out.println();

        System.out.println("-- Usuario CLIENTE (cliente / cliente123)");
        System.out.println("UPDATE usuarios SET password = '" + hashCliente + "' WHERE username = 'cliente';");
        System.out.println();

        System.out.println("=".repeat(70));
        System.out.println("Copia estos comandos y ejecútalos en phpMyAdmin (pestaña SQL)");
        System.out.println("=".repeat(70));
    }
}
