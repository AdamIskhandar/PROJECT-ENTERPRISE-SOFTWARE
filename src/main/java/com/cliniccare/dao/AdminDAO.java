package com.cliniccare.dao;

import com.cliniccare.model.Admin;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public Admin getAdminByEmail(String email) {

        String sql = "SELECT * FROM admin WHERE email = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Admin admin = new Admin();

                admin.setAdminId(
                        rs.getInt("admin_id")
                );

                admin.setFullName(
                        rs.getString("full_name")
                );

                admin.setEmail(
                        rs.getString("email")
                );

                admin.setPassword(
                        rs.getString("password")
                );

                return admin;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}