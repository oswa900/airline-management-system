package com.airlane.airlinemanagementsystem.controller;

import com.airlane.airlinemanagementsystem.dao.ClienteDAO;
import com.airlane.airlinemanagementsystem.dao.UsuarioDAO;
import com.airlane.airlinemanagementsystem.dao.ReservaDAO;
import com.airlane.airlinemanagementsystem.dao.VueloDAO;
import com.airlane.airlinemanagementsystem.model.Cliente;
import com.airlane.airlinemanagementsystem.model.Reserva;
import com.airlane.airlinemanagementsystem.model.Usuario;
import com.airlane.airlinemanagementsystem.model.Vuelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

public class ReservasController {

    @FXML private TextField busquedaField;
    @FXML private TableView<Reserva> tablaReservas;
    @FXML private TableColumn<Reserva, String> colCliente;
    @FXML private TableColumn<Reserva, String> colVuelo;
    @FXML private TableColumn<Reserva, String> colAsiento;
    @FXML private TableColumn<Reserva, String> colEstado;
    @FXML private TableColumn<Reserva, LocalDateTime> colFecha;

    @FXML private ComboBox<Vuelo> comboVuelo;
    @FXML private ComboBox<String> comboAsiento;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<Cliente> comboCliente; // ✅ Tipo correcto


    @FXML private TextField totalField;




    private final ObservableList<Reserva> reservas = FXCollections.observableArrayList();
    private final FilteredList<Reserva> reservasFiltradas = new FilteredList<>(reservas, p -> true);

    @FXML
    public void initialize() {
        configurarColumnas();

        cargarClientes();

        // Cargar ComboBox de vuelos
        cargarVuelos();

        // Cargar estados válidos (respetando el ENUM de la BD)
        comboEstado.setItems(FXCollections.observableArrayList("activa", "cancelada"));

        // Cargar reservas a la tabla
        tablaReservas.setItems(reservasFiltradas);
        cargarReservas();

        // Buscador dinámico
        busquedaField.textProperty().addListener((obs, oldVal, newVal) -> filtrarReservas(newVal));

        // Mostrar reserva seleccionada en el formulario
        tablaReservas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> mostrarEnFormulario(newSel));
    }


    private void configurarColumnas() {
        colCliente.setCellValueFactory(cell -> {
            Usuario u = UsuarioDAO.obtenerPorId(cell.getValue().getIdCliente());
            String nombreCompleto = (u != null) ? u.getNombre() + " " + u.getApellido() : "Desconocido";
            return new SimpleStringProperty(nombreCompleto);
        });

        colVuelo.setCellValueFactory(cell -> {
            Vuelo v = VueloDAO.obtenerPorId(cell.getValue().getIdVuelo());
            String infoVuelo = (v != null) ? v.getNumero() + " " + v.getOrigen() + " ➝ " + v.getDestino() : "N/A";
            return new SimpleStringProperty(infoVuelo);
        });

        colAsiento.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getAsiento()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));
        colFecha.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFechaReserva()));
    }

    private void cargarClientes() {
        List<Cliente> lista = ClienteDAO.obtenerTodos();
        comboCliente.setItems(FXCollections.observableArrayList(lista));
    }


    private void cargarVuelos() {
        List<Vuelo> lista = VueloDAO.obtenerTodos();
        comboVuelo.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void cargarAsientosDisponibles() {
        Vuelo vuelo = comboVuelo.getValue();
        if (vuelo != null) {
            List<String> disponibles = VueloDAO.obtenerAsientosDisponibles(vuelo.getId());
            comboAsiento.setItems(FXCollections.observableArrayList(disponibles));
        }
    }

    private void cargarReservas() {
        reservas.setAll(ReservaDAO.obtenerTodas());
    }

    private void filtrarReservas(String filtro) {
        reservasFiltradas.setPredicate(reserva -> {
            if (filtro == null || filtro.isEmpty()) return true;

            Usuario u = UsuarioDAO.obtenerPorId(reserva.getIdCliente());
            Vuelo v = VueloDAO.obtenerPorId(reserva.getIdVuelo());
            String f = filtro.toLowerCase();

            return (u != null && (u.getNombre() + " " + u.getApellido()).toLowerCase().contains(f)) ||
                    (v != null && (v.getNumero() + v.getOrigen() + v.getDestino()).toLowerCase().contains(f)) ||
                    reserva.getAsiento().toLowerCase().contains(f);
        });
    }

    private void mostrarEnFormulario(Reserva reserva) {
        if (reserva != null) {
            comboCliente.setValue(ClienteDAO.obtenerPorId(reserva.getIdCliente()));
            comboVuelo.setValue(VueloDAO.obtenerPorId(reserva.getIdVuelo()));
            cargarAsientosDisponibles();
            comboAsiento.setValue(reserva.getAsiento());
            comboEstado.setValue(reserva.getEstado());
        }
    }

    @FXML
    private void agregarReserva() {
        Cliente cliente = comboCliente.getValue();
        Vuelo vuelo = comboVuelo.getValue();
        String asiento = comboAsiento.getValue();
        String estado = comboEstado.getValue();

        if (cliente == null || vuelo == null || asiento == null || estado == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        Reserva nueva = new Reserva(0, cliente.getId(), vuelo.getId(), estado, LocalDateTime.now(), asiento);
        if (ReservaDAO.insertar(nueva)) {
            cargarReservas();
            limpiarFormulario();
        } else {
            mostrarAlerta("No se pudo agregar la reserva. El asiento puede estar ocupado.");
        }
    }

    @FXML
    private void editarReserva() {
        Reserva seleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una reserva para editar.");
            return;
        }

        Cliente cliente = comboCliente.getValue();
        Vuelo vuelo = comboVuelo.getValue();
        String asiento = comboAsiento.getValue();
        String estado = comboEstado.getValue();

        if (cliente == null || vuelo == null || asiento == null || estado == null) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return;
        }

        // Validación del total
        BigDecimal total;
        try {
            String totalStr = totalField.getText().trim();
            if (totalStr.isEmpty()) {
                mostrarAlerta("Ingrese el total de la reserva.");
                return;
            }

            total = new BigDecimal(totalStr);

            if (total.compareTo(BigDecimal.ZERO) < 0) {
                mostrarAlerta("El total no puede ser negativo.");
                return;
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("El total debe ser un número válido (ej. 123.45).");
            return;
        }

        Reserva actualizada = new Reserva(
                seleccionada.getId(),
                cliente.getId(),
                vuelo.getId(),
                estado,
                LocalDateTime.now(),
                asiento,
                total
        );

        if (ReservaDAO.actualizar(actualizada)) {
            cargarReservas();
            limpiarFormulario();
            mostrarSnackbar("✏️ Reserva actualizada correctamente.");
        } else {
            mostrarAlerta("No se pudo actualizar la reserva. El asiento puede estar ocupado.");
        }
    }

    private void mostrarSnackbar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }



    @FXML
    private void eliminarReserva() {
        Reserva seleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una reserva para eliminar.");
            return;
        }

        if (ReservaDAO.eliminar(seleccionada.getId())) {
            cargarReservas();
            limpiarFormulario();
        } else {
            mostrarAlerta("No se pudo eliminar la reserva.");
        }
    }

    @FXML
    private void limpiarFormulario() {
        comboCliente.setValue(null);
        comboVuelo.setValue(null);
        comboAsiento.setItems(FXCollections.observableArrayList());
        comboEstado.setValue(null);
        tablaReservas.getSelectionModel().clearSelection();
    }

    @FXML
    private void limpiarBusqueda() {
        busquedaField.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

