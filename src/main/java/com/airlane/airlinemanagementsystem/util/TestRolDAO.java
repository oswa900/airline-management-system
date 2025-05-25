package com.airlane.airlinemanagementsystem.util;

import com.airlane.airlinemanagementsystem.dao.RolDAO;
import com.airlane.airlinemanagementsystem.model.Rol;

import java.util.List;

public class TestRolDAO {
    public static void main(String[] args) {
        List<Rol> roles = RolDAO.obtenerTodos();
        for (Rol rol : roles) {
            System.out.println(rol.getId() + " - " + rol.getNombre());
        }
    }
}
