package com.cliniccare.dao;

import com.cliniccare.model.MedicalService;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalServiceDAOImpl implements MedicalServiceDAO {

    private MedicalService mapRow(ResultSet rs) throws SQLException {

        MedicalService service = new MedicalService();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getDouble("price"));

        return service;
    }

    @Override
    public boolean add(MedicalService service) {

        String sql = "INSERT INTO medical_service "
                + "(service_name, description, price) VALUES (?, ?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, service.getServiceName());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public MedicalService getById(int id) {

        String sql = "SELECT * FROM medical_service WHERE service_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<MedicalService> getAll() {

        List<MedicalService> services = new ArrayList<>();

        String sql = "SELECT * FROM medical_service ORDER BY service_name";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                services.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    @Override
    public boolean update(MedicalService service) {

        String sql = "UPDATE medical_service SET service_name = ?, "
                + "description = ?, price = ? WHERE service_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, service.getServiceName());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());
            stmt.setInt(4, service.getServiceId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Returns false if the service is still referenced (e.g. by appointments)
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM medical_service WHERE service_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<MedicalService> search(String keyword) {

        List<MedicalService> services = new ArrayList<>();

        String sql = "SELECT * FROM medical_service "
                + "WHERE service_name LIKE ? OR description LIKE ? "
                + "ORDER BY service_name";

        String pattern = "%" + keyword + "%";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    @Override
    public boolean nameExists(String name, int excludeId) {

        String sql = "SELECT COUNT(*) FROM medical_service "
                + "WHERE service_name = ? AND service_id <> ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, name);
            stmt.setInt(2, excludeId);

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
