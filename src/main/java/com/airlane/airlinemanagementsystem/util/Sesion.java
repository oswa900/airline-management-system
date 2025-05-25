package com.airlane.airlinemanagementsystem.util;

import com.airlane.airlinemanagementsystem.model.Usuario;

public class Sesion {

    private static Usuario usuarioActual;

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean estaAutenticado() {
        return usuarioActual != null;
    }

    public static String getRolUsuario() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }

    public static String getUsername() {
        return usuarioActual != null ? usuarioActual.getUsername() : null;
    }
}

