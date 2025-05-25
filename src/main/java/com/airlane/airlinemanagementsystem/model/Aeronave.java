package com.airlane.airlinemanagementsystem.model;

import javafx.beans.property.*;

public class Aeronave {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty modelo = new SimpleStringProperty();
    private final IntegerProperty capacidad = new SimpleIntegerProperty();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty imagen = new SimpleStringProperty();
    private final StringProperty matricula = new SimpleStringProperty();

    public String getMatricula() { return matricula.get(); }
    public void setMatricula(String matricula) { this.matricula.set(matricula); }
    public StringProperty matriculaProperty() { return matricula; }

    public Aeronave(int id, String modelo, int capacidad, String estado, String imagen, String matricula) {
        this.id.set(id);
        this.modelo.set(modelo);
        this.capacidad.set(capacidad);
        this.estado.set(estado);
        this.imagen.set(imagen);
        this.matricula.set(matricula);
    }

    public IntegerProperty idProperty() { return id; }
    public StringProperty modeloProperty() { return modelo; }
    public IntegerProperty capacidadProperty() { return capacidad; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty imagenProperty() { return imagen; }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getModelo() { return modelo.get(); }
    public void setModelo(String modelo) { this.modelo.set(modelo); }

    public int getCapacidad() { return capacidad.get(); }
    public void setCapacidad(int capacidad) { this.capacidad.set(capacidad); }

    public String getEstado() { return estado.get(); }
    public void setEstado(String estado) { this.estado.set(estado); }

    public String getImagen() { return imagen.get(); }
    public void setImagen(String imagen) { this.imagen.set(imagen); }
}
