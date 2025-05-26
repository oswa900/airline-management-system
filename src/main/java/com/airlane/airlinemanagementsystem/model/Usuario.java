package com.airlane.airlinemanagementsystem.model;

import java.time.LocalDateTime;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private int idRol;
    private LocalDateTime createdAt;
    private String rol; // nombre del rol (opcional, para mostrar)
    private String nombre;
    private String apellido;
    private String email;

    // Constructor completo (cuando se obtiene desde BD con todos los datos)
    public Usuario(int id, String username, String password, int idRol, LocalDateTime createdAt, String nombre, String apellido, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.idRol = idRol;
        this.createdAt = createdAt;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    // Constructor reducido (cuando solo se usa para login y se tiene el nombre del rol)
    public Usuario(int id, String username, String password, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }


    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getIdRol() { return idRol; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getRol() { return rol; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setIdRol(int idRol) { this.idRol = idRol; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setRol(String rol) { this.rol = rol; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return id + " - " + nombre + " " + apellido;
    }
}
