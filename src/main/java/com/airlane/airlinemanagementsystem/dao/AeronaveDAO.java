package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Aeronave;
import com.airlane.airlinemanagementsystem.util.ConexionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AeronaveDAO {

    public ObservableList<Aeronave> obtenerAeronaves() {
        ObservableList<Aeronave> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM aeronave";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aeronave a = new Aeronave(
                        rs.getInt("id_aeronave"),
                        rs.getString("modelo"),
                        rs.getInt("capacidad"),
                        rs.getString("estado"),
                        rs.getString("imagen"),
                        rs.getString("matricula") // Agregado
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean agregarAeronave(Aeronave aeronave) {
        String sql = "INSERT INTO aeronave (modelo, capacidad, estado, imagen, matricula) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aeronave.getModelo());
            stmt.setInt(2, aeronave.getCapacidad());
            stmt.setString(3, aeronave.getEstado());
            stmt.setString(4, aeronave.getImagen());
            stmt.setString(5, aeronave.getMatricula()); // Agregado

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tieneVuelosAsociados(int idAeronave) {
        String sql = "SELECT COUNT(*) FROM vuelo WHERE id_aeronave = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAeronave);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0; // Si hay vuelos asociados, retorna true
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean modificarAeronave(Aeronave aeronave) {
        String sql = "UPDATE aeronave SET modelo = ?, capacidad = ?, estado = ?, imagen = ?, matricula = ? WHERE id_aeronave = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aeronave.getModelo());
            stmt.setInt(2, aeronave.getCapacidad());
            stmt.setString(3, aeronave.getEstado());
            stmt.setString(4, aeronave.getImagen());
            stmt.setString(5, aeronave.getMatricula()); // Agregado
            stmt.setInt(6, aeronave.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAeronave(int idAeronave) {
        String sql = "DELETE FROM aeronave WHERE id_aeronave = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAeronave);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
