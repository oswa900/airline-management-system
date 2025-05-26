package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Vuelo;
import com.airlane.airlinemanagementsystem.util.ConexionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                        rs.getDate("fecha_salida").toString(),  // formato yyyy-MM-dd
                        rs.getDate("fecha_llegada").toString()
                );
                lista.add(vuelo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static Vuelo obtenerPorId(int id) {
        String sql = "SELECT * FROM vuelo WHERE id_vuelo = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Vuelo(
                        rs.getInt("id_vuelo"),
                        rs.getString("numero"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getTimestamp("fecha_salida").toLocalDateTime(),
                        rs.getTimestamp("fecha_llegada").toLocalDateTime(),
                        rs.getInt("id_aeronave"),
                        rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Vuelo> obtenerTodos() {
        List<Vuelo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vuelo";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vuelo vuelo = new Vuelo(
                        rs.getInt("id_vuelo"),
                        rs.getString("numero"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getTimestamp("fecha_salida").toLocalDateTime(),
                        rs.getTimestamp("fecha_llegada").toLocalDateTime(),
                        rs.getInt("id_aeronave"),
                        rs.getString("estado")
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

            // Convierte el String a Timestamp
            Timestamp tsSalida = Timestamp.valueOf(vuelo.getFechaSalida());
            Timestamp tsLlegada = Timestamp.valueOf(vuelo.getFechaLlegada());

            stmt.setTimestamp(4, tsSalida);
            stmt.setTimestamp(5, tsLlegada);

            return stmt.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean modificarVuelo(Vuelo vuelo) {
        String sql = "UPDATE vuelo SET numero = ?, origen = ?, destino = ?, fecha_salida = ?, fecha_llegada = ? WHERE id_vuelo = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vuelo.getNumero());
            stmt.setString(2, vuelo.getOrigen());
            stmt.setString(3, vuelo.getDestino());

            // Usa solo Timestamp (igual que en agregarVuelo)
            Timestamp tsSalida = Timestamp.valueOf(vuelo.getFechaSalida());
            Timestamp tsLlegada = Timestamp.valueOf(vuelo.getFechaLlegada());
            stmt.setTimestamp(4, tsSalida);
            stmt.setTimestamp(5, tsLlegada);

            stmt.setInt(6, vuelo.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> obtenerAsientosDisponibles(int idVuelo) {
        List<String> disponibles = new ArrayList<>();
        List<String> ocupados = new ArrayList<>();

        String sqlOcupados = "SELECT asiento FROM reserva WHERE id_vuelo = ?";
        String sqlAeronave = """
        SELECT a.capacidad
        FROM vuelo v
        JOIN aeronave a ON v.id_aeronave = a.id_aeronave
        WHERE v.id_vuelo = ?
    """;

        try (Connection conn = ConexionDB.getConnection()) {

            // 1. Obtener capacidad
            int capacidad = 0;
            try (PreparedStatement stmt = conn.prepareStatement(sqlAeronave)) {
                stmt.setInt(1, idVuelo);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    capacidad = rs.getInt("capacidad");
                }
            }

            // 2. Obtener asientos ocupados
            try (PreparedStatement stmt = conn.prepareStatement(sqlOcupados)) {
                stmt.setInt(1, idVuelo);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    ocupados.add(rs.getString("asiento"));
                }
            }

            // 3. Generar lista de todos los asientos según capacidad
            int filas = (int) Math.ceil(capacidad / 4.0); // 4 asientos por fila
            String[] letras = {"A", "B", "C", "D"};

            for (int i = 1; i <= filas; i++) {
                for (String letra : letras) {
                    String asiento = i + letra;
                    if (disponibles.size() < capacidad && !ocupados.contains(asiento)) {
                        disponibles.add(asiento);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return disponibles;
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
