package com.airlane.airlinemanagementsystem.controller.admin;

import com.airlane.airlinemanagementsystem.dao.VueloDAO;
import com.airlane.airlinemanagementsystem.model.Vuelo;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VuelosController {

    @FXML private TableView<Vuelo> tablaVuelos;
    @FXML private TableColumn<Vuelo, Integer> colId;
    @FXML private TableColumn<Vuelo, String> colNumero;
    @FXML private TableColumn<Vuelo, String> colOrigen;
    @FXML private TableColumn<Vuelo, String> colDestino;
    @FXML private TableColumn<Vuelo, String> colFechaSalida;
    @FXML private TableColumn<Vuelo, String> colFechaLlegada;

    @FXML private TextField txtNumero;
    @FXML private TextField txtOrigen;
    @FXML private TextField txtDestino;
    @FXML private DatePicker dpFechaSalida;
    @FXML private DatePicker dpFechaLlegada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtHoraLlegada;
    @FXML private Label lblMensaje;

    private final VueloDAO vueloDAO = new VueloDAO();
    private Vuelo seleccionado;

    private final DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        colOrigen.setCellValueFactory(cellData -> cellData.getValue().origenProperty());
        colDestino.setCellValueFactory(cellData -> cellData.getValue().destinoProperty());
        colFechaSalida.setCellValueFactory(cellData -> cellData.getValue().fechaSalidaProperty());
        colFechaLlegada.setCellValueFactory(cellData -> cellData.getValue().fechaLlegadaProperty());

        tablaVuelos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> seleccionarVuelo());

        cargarVuelos();
    }

    private void seleccionarVuelo() {
        seleccionado = tablaVuelos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            txtNumero.setText(seleccionado.getNumero());
            txtOrigen.setText(seleccionado.getOrigen());
            txtDestino.setText(seleccionado.getDestino());

            try {
                LocalDateTime salida = LocalDateTime.parse(seleccionado.getFechaSalida(), formato);
                LocalDateTime llegada = LocalDateTime.parse(seleccionado.getFechaLlegada(), formato);

                dpFechaSalida.setValue(salida.toLocalDate());
                dpFechaLlegada.setValue(llegada.toLocalDate());
                txtHoraSalida.setText(salida.toLocalTime().toString());
                txtHoraLlegada.setText(llegada.toLocalTime().toString());
            } catch (Exception e) {
                dpFechaSalida.setValue(null);
                dpFechaLlegada.setValue(null);
                txtHoraSalida.clear();
                txtHoraLlegada.clear();
            }
        }
    }

    @FXML
    private void cargarVuelos() {
        ObservableList<Vuelo> lista = vueloDAO.obtenerVuelos();
        tablaVuelos.setItems(lista);
    }

    @FXML
    public void agregarVuelo() {
        if (!validarCampos()) return;

        String numero = txtNumero.getText().trim();
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        String salidaStr = obtenerFechaHoraFormateada(dpFechaSalida, txtHoraSalida);
        String llegadaStr = obtenerFechaHoraFormateada(dpFechaLlegada, txtHoraLlegada);

        Vuelo nuevo = new Vuelo(0, numero, origen, destino, salidaStr, llegadaStr);
        if (vueloDAO.agregarVuelo(nuevo)) {
            cargarVuelos();
            limpiarCampos();
            mostrarMensaje("✔ Vuelo agregado correctamente.", "green");
        } else {
            mostrarMensaje("❌ No se pudo agregar el vuelo.", "red");
        }
    }

    @FXML
    public void modificarVuelo() {
        if (seleccionado == null) {
            mostrarMensaje("⚠ Seleccione un vuelo para modificar.", "orange");
            return;
        }

        if (!validarCampos()) return;

        String numero = txtNumero.getText().trim();
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        String salidaStr = obtenerFechaHoraFormateada(dpFechaSalida, txtHoraSalida);
        String llegadaStr = obtenerFechaHoraFormateada(dpFechaLlegada, txtHoraLlegada);

        seleccionado.setNumero(numero);
        seleccionado.setOrigen(origen);
        seleccionado.setDestino(destino);
        seleccionado.setFechaSalida(salidaStr);
        seleccionado.setFechaLlegada(llegadaStr);

        if (vueloDAO.modificarVuelo(seleccionado)) {
            cargarVuelos();
            limpiarCampos();
            mostrarMensaje("✔ Vuelo modificado correctamente.", "green");
        } else {
            mostrarMensaje("❌ No se pudo modificar el vuelo.", "red");
        }
    }

    @FXML
    public void eliminarVuelo() {
        Vuelo seleccionado = tablaVuelos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Deseas eliminar este vuelo?");
            confirmacion.setContentText("Esta acción también eliminará todos los registros relacionados. ¿Deseas continuar?");

            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    boolean exito = vueloDAO.eliminarVuelo(seleccionado.getId());

                    if (exito) {
                        cargarVuelos();
                        limpiarCampos();
                        mostrarMensaje("✔ Vuelo eliminado correctamente.", "green");
                    } else {
                        mostrarMensaje("❌ No se pudo eliminar el vuelo.", "red");
                    }
                }
            });
        } else {
            mostrarMensaje("⚠ Selecciona un vuelo para eliminar.", "orange");
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNumero.clear();
        txtOrigen.clear();
        txtDestino.clear();
        dpFechaSalida.setValue(null);
        dpFechaLlegada.setValue(null);
        txtHoraSalida.clear();
        txtHoraLlegada.clear();
        seleccionado = null;
        lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje, String color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + color + ";");
    }

    private String obtenerFechaHoraFormateada(DatePicker dp, TextField horaTxt) {
        LocalDate fecha = dp.getValue();
        LocalTime hora = LocalTime.parse(horaTxt.getText().trim());
        return LocalDateTime.of(fecha, hora).format(formato);
    }

    private boolean validarCampos() {
        if (txtNumero.getText().isEmpty() || txtOrigen.getText().isEmpty() || txtDestino.getText().isEmpty()
                || dpFechaSalida.getValue() == null || dpFechaLlegada.getValue() == null
                || txtHoraSalida.getText().isEmpty() || txtHoraLlegada.getText().isEmpty()) {
            mostrarMensaje("⚠ Complete todos los campos.", "orange");
            return false;
        }

        try {
            LocalTime.parse(txtHoraSalida.getText().trim());
            LocalTime.parse(txtHoraLlegada.getText().trim());
        } catch (Exception e) {
            mostrarMensaje("⚠ Formato de hora inválido. Use HH:mm:ss", "orange");
            return false;
        }

        return true;
    }
}

