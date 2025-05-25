package com.airlane.airlinemanagementsystem.controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;


public class DashboardAdministradorController {

    @FXML
    private Button btnCerrarSesion; // Se enlaza con fx:id del FXML

    @FXML
    private StackPane panelCentro;



    @FXML
    public void cerrarSesion() {
        try {
            // Cargar la vista de Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            // Crear nueva ventana para Login
            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Inicio de Sesión");
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.setMaximized(true); // Se muestra maximizada, no pantalla completa forzada
            nuevaVentana.show();

            // Cerrar la ventana actual (el dashboard)
            Stage ventanaActual = (Stage) btnCerrarSesion.getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirUsuarios() {
        System.out.println("➡ Se presionó el botón 'Usuarios'.");
    }

    @FXML
    private void abrirAeronaves() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/Aeronaves.fxml"));
            Pane vista = loader.load();
            panelCentro.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirVuelos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/Vuelos.fxml"));
            Parent vistaVuelos = loader.load();
            panelCentro.getChildren().setAll(vistaVuelos); // panelCentro es el StackPane del centro
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void abrirReportes() {
        System.out.println("➡ Se presionó el botón 'Reportes'.");
    }
}
