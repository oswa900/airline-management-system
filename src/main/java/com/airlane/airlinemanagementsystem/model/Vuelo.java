package com.airlane.airlinemanagementsystem.model;

import javafx.beans.property.*;

public class Vuelo {
    private final IntegerProperty id;
    private final StringProperty numero;
    private final StringProperty origen;
    private final StringProperty destino;
    private final StringProperty fechaSalida;
    private final StringProperty fechaLlegada;

    public Vuelo(int id, String numero, String origen, String destino, String fechaSalida, String fechaLlegada) {
        this.id = new SimpleIntegerProperty(id);
        this.numero = new SimpleStringProperty(numero);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);
        this.fechaSalida = new SimpleStringProperty(fechaSalida);
        this.fechaLlegada = new SimpleStringProperty(fechaLlegada);
    }

    public int getId() { return id.get(); }
    public String getNumero() { return numero.get(); }
    public String getOrigen() { return origen.get(); }
    public String getDestino() { return destino.get(); }
    public String getFechaSalida() { return fechaSalida.get(); }
    public String getFechaLlegada() { return fechaLlegada.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public void setOrigen(String origen) { this.origen.set(origen); }
    public void setDestino(String destino) { this.destino.set(destino); }
    public void setFechaSalida(String fechaSalida) { this.fechaSalida.set(fechaSalida); }
    public void setFechaLlegada(String fechaLlegada) { this.fechaLlegada.set(fechaLlegada); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty numeroProperty() { return numero; }
    public StringProperty origenProperty() { return origen; }
    public StringProperty destinoProperty() { return destino; }
    public StringProperty fechaSalidaProperty() { return fechaSalida; }
    public StringProperty fechaLlegadaProperty() { return fechaLlegada; }
}