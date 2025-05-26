package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Cliente;
import com.airlane.airlinemanagementsystem.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();

        String sql = """
            SELECT c.id_cliente, p.nombre, p.apellido
            FROM cliente c
            JOIN persona p ON c.id_persona = p.id_persona
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                );
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public static Cliente obtenerPorId(int idCliente) {
        String sql = """
            SELECT c.id_cliente, p.nombre, p.apellido
            FROM cliente c
            JOIN persona p ON c.id_persona = p.id_persona
            WHERE c.id_cliente = ?
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
