package com.airlane.airlinemanagementsystem.controller;

import com.airlane.airlinemanagementsystem.dao.UsuarioDAO;
import com.airlane.airlinemanagementsystem.model.Usuario;
import com.airlane.airlinemanagementsystem.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Label lblError;

    @FXML
    void iniciarSesion(ActionEvent event) {
        String username = txtUsuario.getText().trim();
        String password = txtContrasena.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor, complete ambos campos.");
            return;
        }

        Usuario usuario = UsuarioDAO.verificarLogin(username, password);

        if (usuario != null) {
            Sesion.setUsuarioActual(usuario);
            redirigirDashboard(usuario.getRol());
        } else {
            mostrarError("Credenciales incorrectas.");
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }


    @FXML
    public void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Registro.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtUsuario.getScene().getWindow(); //obtiene la ventana actual
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/estilos.css").toExternalForm()); // aplica CSS
            stage.setScene(scene); // 👉 cambia solo la escena
            stage.setTitle("Registro de Usuario");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void redirigirDashboard(String rol) {
        String rutaFXML;

        switch (rol.toLowerCase()) {
            case "administrador" -> rutaFXML = "/fxml/admin/DashboardAdministrador.fxml";
            case "cliente" -> rutaFXML = "/fxml/cliente/DashboardCliente.fxml";
            case "empleado" -> rutaFXML = "/fxml/empleado/DashboardEmpleado.fxml";
            default -> {
                mostrarError("Rol desconocido: " + rol);
                return;
            }
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFXML));
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistema de Aerolíneas");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo cargar el dashboard.");
        }
    }
}

