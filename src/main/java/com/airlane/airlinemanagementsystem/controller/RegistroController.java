package com.airlane.airlinemanagementsystem.controller;

import com.airlane.airlinemanagementsystem.util.ConexionDB;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroController {

    @FXML
    private TextField txtNombre, txtEmail, txtTelefono, txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblMensaje;

    @FXML
    public void initialize() {

    }

    @FXML
    public void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String username = txtUsuario.getText().trim();
        String password = txtContrasena.getText().trim();
        String rolSeleccionado = "cliente";

        if (nombre.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
            lblMensaje.setText("⚠ Todos los campos son obligatorios.");
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setVisible(true);
            return;
        }

        try (Connection conn = ConexionDB.getConnection()) {
            // 1. Insertar en persona
            String[] nombres = nombre.split(" ", 2);
            String nombre1 = nombres.length > 0 ? nombres[0] : "";
            String apellido = nombres.length > 1 ? nombres[1] : "";

            String sqlPersona = "INSERT INTO persona (nombre, apellido, email, telefono) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtPersona.setString(1, nombre1);
            stmtPersona.setString(2, apellido);
            stmtPersona.setString(3, email);
            stmtPersona.setString(4, telefono);
            stmtPersona.executeUpdate();

            ResultSet rsPersona = stmtPersona.getGeneratedKeys();
            int idPersona = rsPersona.next() ? rsPersona.getInt(1) : 0;

            // 2. Obtener id del rol
            String sqlRol = "SELECT id_rol FROM rol WHERE nombre = ?";
            PreparedStatement stmtRol = conn.prepareStatement(sqlRol);
            stmtRol.setString(1, rolSeleccionado);
            ResultSet rsRol = stmtRol.executeQuery();
            int idRol = rsRol.next() ? rsRol.getInt("id_rol") : 0;

            // 3. Insertar en usuario
            String sqlUsuario = "INSERT INTO usuario (username, password, id_rol) VALUES (?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtUsuario.setString(1, username);
            stmtUsuario.setString(2, password);
            stmtUsuario.setInt(3, idRol);
            stmtUsuario.executeUpdate();

            ResultSet rsUsuario = stmtUsuario.getGeneratedKeys();
            int idUsuario = rsUsuario.next() ? rsUsuario.getInt(1) : 0;

            // 4. Insertar en cliente o empleado
            if ("cliente".equalsIgnoreCase(rolSeleccionado)) {
                String sqlCliente = "INSERT INTO cliente (id_usuario, id_persona, pasaporte, programa_viajero) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente);
                stmtCliente.setInt(1, idUsuario);
                stmtCliente.setInt(2, idPersona);
                stmtCliente.setString(3, "P" + idUsuario + "X"); // pasaporte ficticio
                stmtCliente.setString(4, "Básico");
                stmtCliente.executeUpdate();

            } else if ("empleado".equalsIgnoreCase(rolSeleccionado)) {
                String sqlEmpleado = "INSERT INTO empleado (id_usuario, id_persona, puesto, horario) VALUES (?, ?, ?, ?)";
                PreparedStatement stmtEmpleado = conn.prepareStatement(sqlEmpleado);
                stmtEmpleado.setInt(1, idUsuario);
                stmtEmpleado.setInt(2, idPersona);
                stmtEmpleado.setString(3, "Sin asignar");
                stmtEmpleado.setString(4, "L-V 9:00-18:00");
                stmtEmpleado.executeUpdate();
            }

            lblMensaje.setText("✔ Usuario registrado con éxito.");
            lblMensaje.setStyle("-fx-text-fill: green;");
            lblMensaje.setVisible(true);

        }

        catch (SQLException e) {
            lblMensaje.setText("❌ Error al registrar: " + e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    public void volverAlLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNombre.getScene().getWindow(); // usa la misma ventana
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm()); // aplica CSS
            stage.setScene(scene); // cambia solo la escena, no crea ventana nueva
            stage.setTitle("Sistema de Gestión de Aerolíneas - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
