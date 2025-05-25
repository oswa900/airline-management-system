package com.airlane.airlinemanagementsystem.model;

import javafx.beans.property.*;

public class Vuelo {
    private IntegerProperty id;
    private StringProperty numero;
    private StringProperty origen;
    private StringProperty destino;
    private StringProperty fecha;

    public Vuelo(int id, String numero, String origen, String destino, String fecha) {
        this.id = new SimpleIntegerProperty(id);
        this.numero = new SimpleStringProperty(numero);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);
        this.fecha = new SimpleStringProperty(fecha);
    }

    public int getId() { return id.get(); }
    public String getNumero() { return numero.get(); }
    public String getOrigen() { return origen.get(); }
    public String getDestino() { return destino.get(); }
    public String getFecha() { return fecha.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setNumero(String numero) { this.numero.set(numero); }
    public void setOrigen(String origen) { this.origen.set(origen); }
    public void setDestino(String destino) { this.destino.set(destino); }
    public void setFecha(String fecha) { this.fecha.set(fecha); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty numeroProperty() { return numero; }
    public StringProperty origenProperty() { return origen; }
    public StringProperty destinoProperty() { return destino; }
    public StringProperty fechaProperty() { return fecha; }
}
