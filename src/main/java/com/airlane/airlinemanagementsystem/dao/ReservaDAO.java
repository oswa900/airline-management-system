package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Reserva;
import com.airlane.airlinemanagementsystem.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    public static boolean insertar(Reserva reserva) {
        // Verifica si el asiento ya está reservado para ese vuelo
        if (asientoOcupado(reserva.getIdVuelo(), reserva.getAsiento())) {
            return false;
        }

        String sql = "INSERT INTO reserva (id_cliente, id_vuelo, estado, fecha_reserva, asiento, total) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdVuelo());
            stmt.setString(3, reserva.getEstado());
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getFechaReserva()));
            stmt.setString(5, reserva.getAsiento());

            // Si tienes el total como propiedad en el modelo Reserva
            stmt.setBigDecimal(6, reserva.getTotal());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizar(Reserva reserva) {
        // Verifica si el asiento está ocupado por otra reserva
        if (asientoOcupadoPorOtro(reserva.getId(), reserva.getIdVuelo(), reserva.getAsiento())) {
            return false;
        }

        String sql = "UPDATE reserva SET id_cliente = ?, id_vuelo = ?, estado = ?, fecha_reserva = ?, asiento = ? WHERE id_reserva = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reserva.getIdCliente());
            stmt.setInt(2, reserva.getIdVuelo());
            stmt.setString(3, reserva.getEstado());
            stmt.setTimestamp(4, Timestamp.valueOf(reserva.getFechaReserva()));
            stmt.setString(5, reserva.getAsiento());
            stmt.setInt(6, reserva.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM reserva WHERE id_reserva = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Reserva> obtenerTodas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = """
        SELECT id_reserva, id_cliente, id_vuelo, estado, fecha_reserva, asiento
        FROM reserva
    """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva r = new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_vuelo"),
                        rs.getString("estado"),
                        rs.getTimestamp("fecha_reserva").toLocalDateTime(),
                        rs.getString("asiento")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public static Reserva obtenerPorId(int id) {
        String sql = "SELECT * FROM reserva WHERE id_reserva = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Reserva(
                            rs.getInt("id_reserva"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_vuelo"),
                            rs.getString("estado"),
                            rs.getTimestamp("fecha_reserva").toLocalDateTime(),
                            rs.getString("asiento")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean asientoOcupado(int idVuelo, String asiento) {
        String sql = "SELECT COUNT(*) FROM reserva WHERE id_vuelo = ? AND asiento = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVuelo);
            stmt.setString(2, asiento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean asientoOcupadoPorOtro(int idReserva, int idVuelo, String asiento) {
        String sql = "SELECT COUNT(*) FROM reserva WHERE id_vuelo = ? AND asiento = ? AND id_reserva <> ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVuelo);
            stmt.setString(2, asiento);
            stmt.setInt(3, idReserva);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
