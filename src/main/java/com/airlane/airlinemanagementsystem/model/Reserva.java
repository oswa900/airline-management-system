package com.airlane.airlinemanagementsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private int idCliente;
    private int idVuelo;
    private String estado;
    private LocalDateTime fechaReserva;
    private String asiento;
    private BigDecimal total;

    // Constructor completo con total
    public Reserva(int id, int idCliente, int idVuelo, String estado, LocalDateTime fechaReserva, String asiento, BigDecimal total) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVuelo = idVuelo;
        this.estado = estado;
        this.fechaReserva = fechaReserva;
        this.asiento = asiento;
        this.total = total;
    }

    // Constructor sin total (opcional, si lo necesitas)
    public Reserva(int id, int idCliente, int idVuelo, String estado, LocalDateTime fechaReserva, String asiento) {
        this(id, idCliente, idVuelo, estado, fechaReserva, asiento, BigDecimal.ZERO);
    }

    public int getId() { return id; }
    public int getIdCliente() { return idCliente; }
    public int getIdVuelo() { return idVuelo; }
    public String getEstado() { return estado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public String getAsiento() { return asiento; }
    public BigDecimal getTotal() { return total; }

    public void setId(int id) { this.id = id; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public void setIdVuelo(int idVuelo) { this.idVuelo = idVuelo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    public void setAsiento(String asiento) { this.asiento = asiento; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
