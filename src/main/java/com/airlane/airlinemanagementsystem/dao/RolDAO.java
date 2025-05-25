package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Rol;
import com.airlane.airlinemanagementsystem.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {

    public static List<Rol> obtenerTodos() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM rol";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre"));
                roles.add(rol);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public static Rol buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM rol WHERE nombre = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Rol(rs.getInt("id_rol"), rs.getString("nombre"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
