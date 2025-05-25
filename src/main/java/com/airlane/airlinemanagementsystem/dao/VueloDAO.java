package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Vuelo;
import com.airlane.airlinemanagementsystem.util.ConexionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class VueloDAO {

    public ObservableList<Vuelo> obtenerVuelos() {
        ObservableList<Vuelo> lista = FXCollections.observableArrayList();
        String sql = "SELECT id_vuelo, numero, origen, destino, fecha_salida, fecha_llegada FROM vuelo";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vuelo vuelo = new Vuelo(
                        rs.getInt("id_vuelo"),
                        rs.getString("numero"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getString("fecha_salida"),
                        rs.getString("fecha_llegada")
                );
                lista.add(vuelo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public boolean agregarVuelo(Vuelo vuelo) {
        String sql = "INSERT INTO vuelo (numero, origen, destino, fecha_salida, fecha_llegada) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vuelo.getNumero());
            stmt.setString(2, vuelo.getOrigen());
            stmt.setString(3, vuelo.getDestino());
            stmt.setString(4, vuelo.getFechaSalida());
            stmt.setString(5, vuelo.getFechaLlegada());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean eliminarVuelo(int idVuelo) {
        String sql = "DELETE FROM vuelo WHERE id_vuelo = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVuelo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
