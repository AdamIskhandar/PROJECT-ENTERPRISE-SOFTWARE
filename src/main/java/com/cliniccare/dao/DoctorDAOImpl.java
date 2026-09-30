package com.cliniccare.dao;

import com.cliniccare.model.Doctor;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAOImpl implements DoctorDAO {

    // Converts one database row into a Doctor object
    private Doctor mapRow(ResultSet rs) throws SQLException {

        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setEmail(rs.getString("email"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setConsultationFee(rs.getDouble("consultation_fee"));

        return doctor;
    }

    @Override
    public boolean add(Doctor doctor) {

        String sql = "INSERT INTO doctor "
                + "(full_name, specialization, email, phone, consultation_fee) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getEmail());
            stmt.setString(4, doctor.getPhone());
            stmt.setDouble(5, doctor.getConsultationFee());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Doctor getById(int id) {

        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";

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
    public List<Doctor> getAll() {

        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM doctor ORDER BY full_name";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                doctors.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    @Override
    public boolean update(Doctor doctor) {

        String sql = "UPDATE doctor SET full_name = ?, specialization = ?, "
                + "email = ?, phone = ?, consultation_fee = ? "
                + "WHERE doctor_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, doctor.getFullName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getEmail());
            stmt.setString(4, doctor.getPhone());
            stmt.setDouble(5, doctor.getConsultationFee());
            stmt.setInt(6, doctor.getDoctorId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Returns false if the doctor is still referenced (e.g. by appointments)
    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM doctor WHERE doctor_id = ?";

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
    public List<Doctor> search(String keyword) {

        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM doctor "
                + "WHERE full_name LIKE ? OR specialization LIKE ? "
                + "OR email LIKE ? OR phone LIKE ? "
                + "ORDER BY full_name";

        String pattern = "%" + keyword + "%";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            stmt.setString(4, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    @Override
    public boolean emailExists(String email, int excludeId) {

        String sql = "SELECT COUNT(*) FROM doctor "
                + "WHERE email = ? AND doctor_id <> ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);
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
