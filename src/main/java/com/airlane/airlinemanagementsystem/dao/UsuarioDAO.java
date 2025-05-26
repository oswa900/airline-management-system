package com.airlane.airlinemanagementsystem.dao;

import com.airlane.airlinemanagementsystem.model.Usuario;
import com.airlane.airlinemanagementsystem.util.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public static Usuario verificarLogin(String username, String password) {
        String sql = """
            SELECT u.id_usuario, u.username, u.password, r.nombre AS rol
            FROM usuario u
            JOIN rol r ON u.id_rol = r.id_rol
            WHERE u.username = ? AND u.password = ?
        """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.password, u.id_rol, u.created_at, r.nombre AS rol, p.nombre, p.apellido, p.email FROM usuario u LEFT JOIN cliente c ON u.id_usuario = c.id_usuario LEFT JOIN persona p ON c.id_persona = p.id_persona JOIN rol r ON u.id_rol = r.id_rol";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_rol"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email")
                );
                usuario.setRol(rs.getString("rol"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminarUsuarioCompleto(int idUsuario) {
        String sqlCliente = "SELECT id_persona FROM cliente WHERE id_usuario = ?";
        String deleteCliente = "DELETE FROM cliente WHERE id_usuario = ?";
        String deletePersona = "DELETE FROM persona WHERE id_persona = ?";
        String deleteUsuario = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int idPersona = -1;
            try (PreparedStatement stmt = conn.prepareStatement(sqlCliente)) {
                stmt.setInt(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idPersona = rs.getInt("id_persona");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteCliente)) {
                stmt.setInt(1, idUsuario);
                stmt.executeUpdate();
            }

            if (idPersona != -1) {
                try (PreparedStatement stmt = conn.prepareStatement(deletePersona)) {
                    stmt.setInt(1, idPersona);
                    stmt.executeUpdate();
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteUsuario)) {
                stmt.setInt(1, idUsuario);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static Usuario obtenerPorId(int id) {
        String sql = """
        SELECT u.id_usuario, u.username, u.password, u.id_rol, u.created_at,
               p.nombre, p.apellido, p.email
        FROM usuario u
        JOIN cliente c ON u.id_usuario = c.id_usuario
        JOIN persona p ON c.id_persona = p.id_persona
        WHERE u.id_usuario = ?
    """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_rol"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Usuario> obtenerClientes() {
        List<Usuario> clientes = new ArrayList<>();
        String sql = """
        SELECT u.id_usuario, u.username, u.password, u.id_rol, u.created_at,
               p.nombre, p.apellido, p.email
        FROM usuario u
        JOIN cliente c ON u.id_usuario = c.id_usuario
        JOIN persona p ON c.id_persona = p.id_persona
        WHERE u.id_rol = 3
    """;

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("id_rol"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email")
                );
                clientes.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public static boolean insertar(Usuario usuario) {
        String insertUsuario = "INSERT INTO usuario (username, password, id_rol, created_at) VALUES (?, ?, ?, ?)";
        String insertPersona = "INSERT INTO persona (nombre, apellido, email) VALUES (?, ?, ?)";
        String insertCliente = "INSERT INTO cliente (id_usuario, id_persona) VALUES (?, ?)";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int idUsuario = -1;
            try (PreparedStatement stmt = conn.prepareStatement(insertUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.getUsername());
                stmt.setString(2, usuario.getPassword());
                stmt.setInt(3, usuario.getIdRol());
                stmt.setTimestamp(4, Timestamp.valueOf(usuario.getCreatedAt()));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idUsuario = rs.getInt(1);
                }
            }

            int idPersona = -1;
            try (PreparedStatement stmt = conn.prepareStatement(insertPersona, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.getNombre());
                stmt.setString(2, usuario.getApellido());
                stmt.setString(3, usuario.getEmail());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idPersona = rs.getInt(1);
                }
            }

            if (usuario.getIdRol() == 3) {
                try (PreparedStatement stmt = conn.prepareStatement(insertCliente)) {
                    stmt.setInt(1, idUsuario);
                    stmt.setInt(2, idPersona);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean actualizarUsuarioYPersona(Usuario usuario) {
        String selectPersona = "SELECT id_persona FROM cliente WHERE id_usuario = ?";
        String updateUsuario = "UPDATE usuario SET username = ?, password = ?, id_rol = ? WHERE id_usuario = ?";
        String updatePersona = "UPDATE persona SET nombre = ?, apellido = ?, email = ? WHERE id_persona = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int idPersona = -1;
            try (PreparedStatement stmt = conn.prepareStatement(selectPersona)) {
                stmt.setInt(1, usuario.getId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idPersona = rs.getInt("id_persona");
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateUsuario)) {
                stmt.setString(1, usuario.getUsername());
                stmt.setString(2, usuario.getPassword());
                stmt.setInt(3, usuario.getIdRol());
                stmt.setInt(4, usuario.getId());
                stmt.executeUpdate();
            }

            if (idPersona != -1) {
                try (PreparedStatement stmt = conn.prepareStatement(updatePersona)) {
                    stmt.setString(1, usuario.getNombre());
                    stmt.setString(2, usuario.getApellido());
                    stmt.setString(3, usuario.getEmail());
                    stmt.setInt(4, idPersona);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean eliminarUsuarioGeneral(int idUsuario) {
        String selectCliente = "SELECT id_persona FROM cliente WHERE id_usuario = ?";
        String selectEmpleado = "SELECT id_persona FROM empleado WHERE id_usuario = ?";
        String deleteCliente = "DELETE FROM cliente WHERE id_usuario = ?";
        String deleteEmpleado = "DELETE FROM empleado WHERE id_usuario = ?";
        String deletePersona = "DELETE FROM persona WHERE id_persona = ?";
        String deleteUsuario = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection()) {
            conn.setAutoCommit(false);

            int idPersona = -1;

            // Buscar y eliminar en cliente
            try (PreparedStatement stmt = conn.prepareStatement(selectCliente)) {
                stmt.setInt(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idPersona = rs.getInt("id_persona");
                    try (PreparedStatement d = conn.prepareStatement(deleteCliente)) {
                        d.setInt(1, idUsuario);
                        d.executeUpdate();
                    }
                }
            }

            // Buscar y eliminar en empleado
            try (PreparedStatement stmt = conn.prepareStatement(selectEmpleado)) {
                stmt.setInt(1, idUsuario);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idPersona = rs.getInt("id_persona");
                    try (PreparedStatement d = conn.prepareStatement(deleteEmpleado)) {
                        d.setInt(1, idUsuario);
                        d.executeUpdate();
                    }
                }
            }

            // Eliminar en persona si se encontró
            if (idPersona != -1) {
                try (PreparedStatement stmt = conn.prepareStatement(deletePersona)) {
                    stmt.setInt(1, idPersona);
                    stmt.executeUpdate();
                }
            }

            // Finalmente, eliminar en usuario
            try (PreparedStatement stmt = conn.prepareStatement(deleteUsuario)) {
                stmt.setInt(1, idUsuario);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


}
