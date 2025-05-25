package com.airlane.airlinemanagementsystem.controller.component;

import com.airlane.airlinemanagementsystem.model.Aeronave;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class TarjetaAeronaveController {

    @FXML private VBox tarjetaRoot;
    @FXML private Label lblModelo;
    @FXML private Label lblCapacidad;
    @FXML private Label lblEstado;
    @FXML private ImageView imagenAvion;

    private Runnable onSeleccionar;

    public void setDatos(Aeronave aeronave) {
        lblModelo.setText(aeronave.getModelo());
        lblCapacidad.setText("Capacidad: " + aeronave.getCapacidad());
        lblEstado.setText("Estado: " + aeronave.getEstado());

        try {
            String ruta = "/images/" + (aeronave.getImagen() != null ? aeronave.getImagen() : "aeronaves/default.png");
            Image imagen = new Image(getClass().getResourceAsStream(ruta));
            imagenAvion.setImage(imagen);
        } catch (Exception e) {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/aeronaves/default.png"));
            imagenAvion.setImage(defaultImage);
        }


        tarjetaRoot.setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        if (onSeleccionar != null) {
            onSeleccionar.run();
        }
    }

    public void setOnSeleccionar(Runnable onSeleccionar) {
        this.onSeleccionar = onSeleccionar;
    }

    public void marcarSeleccionado(boolean seleccionado) {
        if (seleccionado) {
            tarjetaRoot.getStyleClass().add("tarjeta-seleccionada");
        } else {
            tarjetaRoot.getStyleClass().remove("tarjeta-seleccionada");
        }
    }
}
