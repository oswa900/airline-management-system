package com.airlane.airlinemanagementsystem.controller.admin;

import com.airlane.airlinemanagementsystem.dao.VueloDAO;
import com.airlane.airlinemanagementsystem.model.Vuelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class VuelosController {

    @FXML
    private TableView<Vuelo> tablaVuelos;

    @FXML
    private TableColumn<Vuelo, Integer> colId;

    @FXML
    private TableColumn<Vuelo, String> colNumero;

    @FXML
    private TableColumn<Vuelo, String> colOrigen;

    @FXML
    private TableColumn<Vuelo, String> colDestino;

    @FXML
    private TableColumn<Vuelo, String> colFechaSalida;

    @FXML
    private TableColumn<Vuelo, String> colFechaLlegada;


    @FXML
    private Label lblMensaje;

    private final VueloDAO vueloDAO = new VueloDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNumero.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        colOrigen.setCellValueFactory(cellData -> cellData.getValue().origenProperty());
        colDestino.setCellValueFactory(cellData -> cellData.getValue().destinoProperty());
        colFechaSalida.setCellValueFactory(cellData -> cellData.getValue().fechaSalidaProperty());
        colFechaLlegada.setCellValueFactory(cellData -> cellData.getValue().fechaLlegadaProperty());


        cargarVuelos();
    }


    @FXML
    private void cargarVuelos() {
        ObservableList<Vuelo> lista = vueloDAO.obtenerVuelos();
        tablaVuelos.setItems(lista);
    }

    @FXML
    public void eliminarVuelo() {
        Vuelo seleccionado = tablaVuelos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Deseas eliminar este vuelo?");
            confirmacion.setContentText(
                    "Esta acción también eliminará todos los registros relacionados (como asignaciones de empleados). ¿Deseas continuar?");

            // Mostrar el cuadro de diálogo y esperar respuesta
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    boolean exito = vueloDAO.eliminarVuelo(seleccionado.getId());

                    if (exito) {
                        cargarVuelos();
                        lblMensaje.setText("✔ Vuelo eliminado correctamente.");
                        lblMensaje.setStyle("-fx-text-fill: green;");
                    } else {
                        lblMensaje.setText("❌ No se pudo eliminar el vuelo.");
                        lblMensaje.setStyle("-fx-text-fill: red;");
                    }
                }
            });
        } else {
            lblMensaje.setText("⚠ Selecciona un vuelo para eliminar.");
            lblMensaje.setStyle("-fx-text-fill: orange;");
        }
    }

}
