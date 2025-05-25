package com.airlane.airlinemanagementsystem.controller.admin;

import com.airlane.airlinemanagementsystem.dao.UsuarioDAO;
import com.airlane.airlinemanagementsystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuariosController {

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, Integer> colId;

    @FXML
    private TableColumn<Usuario, String> colUsername;

    @FXML
    private TableColumn<Usuario, String> colRol;

    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();


    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colUsername.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        colRol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRol()));
        cargarUsuarios();
    }

    @FXML
    public void cargarUsuarios() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList(usuarioDAO.obtenerUsuarios());
        tablaUsuarios.setItems(lista);
        lblMensaje.setText("");
    }

    @FXML
    public void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean exito = usuarioDAO.eliminarUsuario(seleccionado.getId());
            if (exito) {
                cargarUsuarios();
                lblMensaje.setText("✔ Usuario eliminado.");
                lblMensaje.setStyle("-fx-text-fill: green;");
            } else {
                lblMensaje.setText("❌ No se pudo eliminar.");
                lblMensaje.setStyle("-fx-text-fill: red;");
            }
        } else {
            lblMensaje.setText("⚠ Seleccione un usuario.");
            lblMensaje.setStyle("-fx-text-fill: orange;");
        }
    }
}
