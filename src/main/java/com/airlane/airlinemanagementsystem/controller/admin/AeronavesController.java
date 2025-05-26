package com.airlane.airlinemanagementsystem.controller.admin;

import com.airlane.airlinemanagementsystem.dao.AeronaveDAO;
import com.airlane.airlinemanagementsystem.model.Aeronave;
import com.airlane.airlinemanagementsystem.controller.component.TarjetaAeronaveController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


public class AeronavesController {

    @FXML private FlowPane contenedorTarjetas;
    @FXML private ScrollPane scrollPane;

    @FXML private TextField txtModelo;
    @FXML private TextField txtCapacidad;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private Label lblMensaje;

    private final AeronaveDAO aeronaveDAO = new AeronaveDAO();
    private Aeronave seleccionada;
    private Node tarjetaSeleccionada = null;
    private TarjetaAeronaveController controllerSeleccionado = null;
    private File imagenSeleccionada; // imagen elegida desde el sistema

    @FXML
    public void initialize() {
        cmbEstado.getItems().addAll("activa", "inactiva", "mantenimiento");
        cargarTarjetas();
    }

    private void cargarTarjetas() {
        contenedorTarjetas.getChildren().clear();
        ObservableList<Aeronave> lista = aeronaveDAO.obtenerAeronaves();

        for (Aeronave aeronave : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/TarjetaAeronave.fxml"));
                Node tarjeta = loader.load();

                TarjetaAeronaveController controller = loader.getController();
                controller.setDatos(aeronave);
                controller.setOnSeleccionar(() -> seleccionarAeronaveDesdeTarjeta(aeronave, tarjeta, controller));

                contenedorTarjetas.getChildren().add(tarjeta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarAeronaveDesdeTarjeta(Aeronave aeronave, Node tarjeta, TarjetaAeronaveController controller) {
        if (tarjetaSeleccionada != null && controllerSeleccionado != null) {
            controllerSeleccionado.marcarSeleccionado(false);
        }

        this.seleccionada = aeronave;
        this.tarjetaSeleccionada = tarjeta;
        this.controllerSeleccionado = controller;

        controller.marcarSeleccionado(true);

        txtModelo.setText(aeronave.getModelo());
        txtCapacidad.setText(String.valueOf(aeronave.getCapacidad()));
        cmbEstado.setValue(aeronave.getEstado());
    }

    @FXML
    public void agregarAeronave() {
        String modelo = txtModelo.getText().trim();
        String capacidadStr = txtCapacidad.getText().trim();
        String estado = cmbEstado.getValue();

        if (modelo.isEmpty() || capacidadStr.isEmpty() || estado == null) {
            mostrarMensaje("⚠ Complete todos los campos", "orange");
            return;
        }

        try {
            int capacidad = Integer.parseInt(capacidadStr);

            String rutaImagen = (imagenSeleccionada != null)
                    ? copiarImagenAlProyecto(imagenSeleccionada, modelo)
                    : "images/default.png";


            Aeronave nueva = new Aeronave(0, modelo, capacidad, estado, rutaImagen, "AUTO");

            if (aeronaveDAO.agregarAeronave(nueva)) {
                cargarTarjetas();
                limpiarCampos();
                mostrarMensaje("✔ Aeronave agregada", "green");
            } else {
                mostrarMensaje("❌ No se pudo agregar", "red");
            }

        } catch (NumberFormatException e) {
            mostrarMensaje("⚠ Capacidad inválida", "orange");
        }
    }



    @FXML
    public void seleccionarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de aeronave");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(null);
        if (archivo != null) {
            imagenSeleccionada = archivo;
            mostrarMensaje("✔ Imagen seleccionada: " + archivo.getName(), "green");
        }
    }

    private String copiarImagenAlProyecto(File archivoOriginal, String modelo) {
        // Limpiar el nombre del modelo para crear un nombre de archivo seguro
        String nombreLimpio = modelo.toLowerCase().replaceAll("[^a-z0-9]", "_");

        // Obtener extensión del archivo original
        String extension = archivoOriginal.getName().substring(archivoOriginal.getName().lastIndexOf("."));

        // Crear nombre único del archivo con timestamp
        String nombreArchivo = nombreLimpio + "_" + System.currentTimeMillis() + extension;

        // Definir ruta de destino dentro del proyecto
        File destino = new File("src/main/resources/images/aeronaves/" + nombreArchivo);

        try (FileInputStream in = new FileInputStream(archivoOriginal);
             FileOutputStream out = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            // Devolver ruta relativa para guardar en la base de datos
            return "images/aeronaves/" + nombreArchivo;

        } catch (IOException e) {
            e.printStackTrace();
            return "images/default.png";
        }
    }



    @FXML
    public void modificarAeronave() {
        if (seleccionada != null) {
            String modelo = txtModelo.getText().trim();
            String capacidadStr = txtCapacidad.getText().trim();
            String estado = cmbEstado.getValue();

            if (modelo.isEmpty() || capacidadStr.isEmpty() || estado == null) {
                mostrarMensaje("⚠ Complete todos los campos", "orange");
                return;
            }

            try {
                int capacidad = Integer.parseInt(capacidadStr);
                seleccionada.setModelo(modelo);
                seleccionada.setCapacidad(capacidad);
                seleccionada.setEstado(estado);

                // Si el usuario seleccionó una nueva imagen, se copia y se actualiza
                if (imagenSeleccionada != null) {
                    String rutaImagen = copiarImagenAlProyecto(imagenSeleccionada, modelo);
                    seleccionada.setImagen(rutaImagen);
                }

                if (aeronaveDAO.modificarAeronave(seleccionada)) {
                    cargarTarjetas();
                    limpiarCampos(); // <-- Aquí se limpia imagenSeleccionada también
                    mostrarMensaje("✔ Aeronave modificada", "green");
                } else {
                    mostrarMensaje("❌ No se pudo modificar", "red");
                }

            } catch (NumberFormatException e) {
                mostrarMensaje("⚠ Capacidad inválida", "orange");
            }

        } else {
            mostrarMensaje("⚠ Seleccione una aeronave", "orange");
        }
    }



    @FXML
    public void eliminarAeronave() {
        if (seleccionada != null) {
            if (aeronaveDAO.tieneVuelosAsociados(seleccionada.getId())) {
                mostrarMensaje("⚠ No se puede eliminar: tiene vuelos asociados", "orange");
                return;
            }

            if (aeronaveDAO.eliminarAeronave(seleccionada.getId())) {
                cargarTarjetas();
                limpiarCampos();
                mostrarMensaje("✔ Aeronave eliminada", "green");
            } else {
                mostrarMensaje("❌ No se pudo eliminar", "red");
            }
        } else {
            mostrarMensaje("⚠ Seleccione una aeronave", "orange");
        }
    }

    @FXML
    private void limpiarCampos() {
        txtModelo.clear();
        txtCapacidad.clear();
        cmbEstado.setValue(null);
        seleccionada = null;
        lblMensaje.setText("");

        if (controllerSeleccionado != null) {
            controllerSeleccionado.marcarSeleccionado(false);
        }

        tarjetaSeleccionada = null;
        controllerSeleccionado = null;
        imagenSeleccionada = null;

    }

    private void mostrarMensaje(String mensaje, String color) {
        lblMensaje.setText(mensaje);
        lblMensaje.setStyle("-fx-text-fill: " + color + ";");
    }
}
