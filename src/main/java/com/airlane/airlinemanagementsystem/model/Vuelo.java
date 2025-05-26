package com.airlane.airlinemanagementsystem.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Vuelo {
    // JavaFX properties para UI binding
    private final IntegerProperty id;
    private final StringProperty numero;
    private final StringProperty origen;
    private final StringProperty destino;
    private final StringProperty fechaSalida; // String visible en tabla
    private final StringProperty fechaLlegada;

    // Campos reales para lógica de backend
    private LocalDateTime fechaSalidaReal;
    private LocalDateTime fechaLlegadaReal;
    private int idAeronave;
    private String estado;

    // Constructor original para uso en interfaz
    public Vuelo(int id, String numero, String origen, String destino, String fechaSalida, String fechaLlegada) {
        this.id = new SimpleIntegerProperty(id);
        this.numero = new SimpleStringProperty(numero);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);
        this.fechaSalida = new SimpleStringProperty(fechaSalida);
        this.fechaLlegada = new SimpleStringProperty(fechaLlegada);
    }

    // Constructor extendido para uso en DAO y lógica
    public Vuelo(int id, String numero, String origen, String destino,
                 LocalDateTime fechaSalidaReal, LocalDateTime fechaLlegadaReal,
                 int idAeronave, String estado) {

        this.id = new SimpleIntegerProperty(id);
        this.numero = new SimpleStringProperty(numero);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);

        this.fechaSalidaReal = fechaSalidaReal;
        this.fechaLlegadaReal = fechaLlegadaReal;

        this.fechaSalida = new SimpleStringProperty(fechaSalidaReal.toString());
        this.fechaLlegada = new SimpleStringProperty(fechaLlegadaReal.toString());

        this.idAeronave = idAeronave;
        this.estado = estado;
    }

    // Getters básicos para JavaFX
    public int getId() { return id.get(); }
    public String getNumero() { return numero.get(); }
    public String getOrigen() { return origen.get(); }
    public String getDestino() { return destino.get(); }
    public String getFechaSalida() { return fechaSalida.get(); }
    public String getFechaLlegada() { return fechaLlegada.get(); }

    // Getters reales
    public LocalDateTime getFechaSalidaReal() { return fechaSalidaReal; }
    public LocalDateTime getFechaLlegadaReal() { return fechaLlegadaReal; }
    public int getIdAeronave() { return idAeronave; }
    public String getEstado() { return estado; }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public void setOrigen(String origen) { this.origen.set(origen); }
    public void setDestino(String destino) { this.destino.set(destino); }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida.set(fechaSalida); }
    public void setFechaLlegada(String fechaLlegada) { this.fechaLlegada.set(fechaLlegada); }
    public void setFechaSalidaReal(LocalDateTime fechaSalidaReal) { this.fechaSalidaReal = fechaSalidaReal; }
    public void setFechaLlegadaReal(LocalDateTime fechaLlegadaReal) { this.fechaLlegadaReal = fechaLlegadaReal; }
    public void setIdAeronave(int idAeronave) { this.idAeronave = idAeronave; }
    public void setEstado(String estado) { this.estado = estado; }

    // Properties para JavaFX
    public IntegerProperty idProperty() { return id; }
    public StringProperty numeroProperty() { return numero; }
    public StringProperty origenProperty() { return origen; }
    public StringProperty destinoProperty() { return destino; }
    public StringProperty fechaSalidaProperty() { return fechaSalida; }
    public StringProperty fechaLlegadaProperty() { return fechaLlegada; }

    @Override
    public String toString() {
        return getNumero() + " " + getOrigen() + " → " + getDestino();
    }


}
