package com.airlane.airlinemanagementsystem.controller.admin;

import com.airlane.airlinemanagementsystem.dao.RolDAO;
import com.airlane.airlinemanagementsystem.dao.UsuarioDAO;
import com.airlane.airlinemanagementsystem.model.Rol;
import com.airlane.airlinemanagementsystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colUsername;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Rol> comboRol;

    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Usuario usuarioSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colUsername.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsername()));
        colRol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getRol()));

        comboRol.setItems(FXCollections.observableArrayList(RolDAO.obtenerTodos()));

        cargarUsuarios();

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                usuarioSeleccionado = newSel;
                usernameField.setText(newSel.getUsername());
                passwordField.setText(newSel.getPassword());
                nombreField.setText(newSel.getNombre());
                apellidoField.setText(newSel.getApellido());
                emailField.setText(newSel.getEmail());
                Rol rol = RolDAO.buscarPorNombre(newSel.getRol());
                comboRol.getSelectionModel().select(rol);
            }
        });
    }

    @FXML
    public void cargarUsuarios() {
        ObservableList<Usuario> lista = FXCollections.observableArrayList(usuarioDAO.obtenerUsuarios());
        tablaUsuarios.setItems(lista);
        lblMensaje.setText("");
    }

    @FXML
    public void agregarUsuario() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String email = emailField.getText().trim();
        Rol rolSeleccionado = comboRol.getValue();

        if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || rolSeleccionado == null) {
            mostrarMensaje("⚠ Todos los campos son obligatorios.", "orange");
            return;
        }

        int idRol = rolSeleccionado.getId();
        Usuario nuevo = new Usuario(0, username, password, idRol, LocalDateTime.now(), nombre, apellido, email);
        boolean exito = UsuarioDAO.insertar(nuevo);

        if (exito) {
            cargarUsuarios();
            limpiarFormulario();
            mostrarMensaje("✔ Usuario agregado.", "green");
        } else {
            mostrarMensaje("❌ Error al agregar usuario.", "red");
        }
    }

    @FXML
    public void editarUsuario() {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("⚠ Seleccione un usuario para editar.", "orange");
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String email = emailField.getText().trim();
        Rol rolSeleccionado = comboRol.getValue();

        if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || rolSeleccionado == null) {
            mostrarMensaje("⚠ Todos los campos son obligatorios.", "orange");
            return;
        }

        int idRol = rolSeleccionado.getId();

        Usuario actualizado = new Usuario(usuarioSeleccionado.getId(), username, password, idRol, usuarioSeleccionado.getCreatedAt(), nombre, apellido, email);
        boolean exito = UsuarioDAO.actualizarUsuarioYPersona(actualizado);


        if (exito) {
            cargarUsuarios();
            limpiarFormulario();
            mostrarMensaje("✔ Usuario editado.", "green");
        } else {
            mostrarMensaje("❌ Error al editar usuario.", "red");
        }
    }

    @FXML
    public void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean exito = usuarioDAO.eliminarUsuarioGeneral(seleccionado.getId());

            if (exito) {
                cargarUsuarios();
                limpiarFormulario();
                mostrarMensaje("✔ Usuario eliminado.", "green");
            } else {
                mostrarMensaje("❌ No se pudo eliminar.", "red");
            }
        } else {
            mostrarMensaje("⚠ Seleccione un usuario.", "orange");
        }
    }


    @FXML
    public void limpiarFormulario() {
        usernameField.clear();
        passwordField.clear();
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();
        comboRol.getSelectionModel().clearSelection();
        usuarioSeleccionado = null;
        tablaUsuarios.getSelectionModel().clearSelection();
        lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje, String color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + color + ";");
    }
}


