package com.airlane.airlinemanagementsystem.util;

import java.sql.Connection;

import com.airlane.airlinemanagementsystem.dao.UsuarioDAO;
import com.airlane.airlinemanagementsystem.model.Usuario;

public class TestLogin {
    public static void main(String[] args) {
        Usuario usuario = UsuarioDAO.verificarLogin("user1", "pass1");

        if (usuario != null) {
            System.out.println("✔ Usuario autenticado: " + usuario.getUsername() + " (" + usuario.getRol() + ")");
        } else {
            System.out.println("❌ Usuario o contraseña incorrectos.");
        }
    }
}
