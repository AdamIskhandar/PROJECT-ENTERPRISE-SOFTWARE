package com.cliniccare.dao;

import com.cliniccare.config.ClinicRateConfig;
import com.cliniccare.model.Doctor;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the doctor table.
 *
 * Doctor details are stored only in doctor:
 * doctor_id, doctor_name, specialization, phone, email.
 *
 * Clinic rates are fixed and are stored in fees for appointment calculation.
 * Admin users do not enter or edit rates from the doctor forms.
 */
public class DoctorDAOImpl implements DoctorDAO {

    private Doctor mapRow(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setDoctorName(rs.getString("doctor_name"));
        doctor.setSpecialization(rs.getString("specialization"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setEmail(rs.getString("email"));
        return doctor;
    }

    @Override
    public boolean add(Doctor doctor) {
        String doctorSql = "INSERT INTO doctor "
                + "(doctor_name, specialization, phone, email) "
                + "VALUES (?, ?, ?, ?)";

        String feeSql = "INSERT INTO fees "
                + "(doctor_id, morning_rate, night_rate) "
                + "VALUES (?, ?, ?)";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int doctorId;

            try (PreparedStatement stmt = conn.prepareStatement(
                    doctorSql,
                    Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, doctor.getDoctorName());
                stmt.setString(2, doctor.getSpecialization());
                stmt.setString(3, doctor.getPhone());
                stmt.setString(4, doctor.getEmail());

                if (stmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return false;
                    }
                    doctorId = keys.getInt(1);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(feeSql)) {
                stmt.setInt(1, doctorId);
                stmt.setBigDecimal(2, ClinicRateConfig.MORNING_RATE);
                stmt.setBigDecimal(3, ClinicRateConfig.NIGHT_RATE);

                if (stmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            doctor.setDoctorId(doctorId);
            return true;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    @Override
    public Doctor getById(int id) {
        String sql = "SELECT doctor_id, doctor_name, specialization, phone, email "
                + "FROM doctor WHERE doctor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        String sql = "SELECT doctor_id, doctor_name, specialization, phone, email "
                + "FROM doctor ORDER BY doctor_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                doctors.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }

    /**
     * Compatibility helper for any existing code that calls getAllDoctors().
     */
    public List<Doctor> getAllDoctors() {
        return getAll();
    }

    @Override
    public boolean update(Doctor doctor) {
        String updateDoctorSql = "UPDATE doctor SET doctor_name = ?, "
                + "specialization = ?, phone = ?, email = ? "
                + "WHERE doctor_id = ?";

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateDoctorSql)) {
                stmt.setString(1, doctor.getDoctorName());
                stmt.setString(2, doctor.getSpecialization());
                stmt.setString(3, doctor.getPhone());
                stmt.setString(4, doctor.getEmail());
                stmt.setInt(5, doctor.getDoctorId());

                if (stmt.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // Rates are fixed. Keep the database fee row in sync even though
            // rates are not shown on the add/edit doctor forms.
            ensureFixedFee(conn, doctor.getDoctorId());

            conn.commit();
            return true;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM doctor WHERE doctor_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Doctor> search(String keyword) {
        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT doctor_id, doctor_name, specialization, phone, email "
                + "FROM doctor "
                + "WHERE doctor_name LIKE ? "
                + "OR specialization LIKE ? "
                + "OR COALESCE(phone, '') LIKE ? "
                + "OR COALESCE(email, '') LIKE ? "
                + "ORDER BY doctor_name";

        String pattern = "%" + (keyword == null ? "" : keyword.trim()) + "%";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, pattern);
            }

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
        if (email == null || email.isBlank()) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM doctor "
                + "WHERE email = ? AND doctor_id <> ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            stmt.setInt(2, excludeId);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void ensureFixedFee(Connection conn, int doctorId) throws SQLException {
        String updateSql = "UPDATE fees SET morning_rate = ?, night_rate = ? "
                + "WHERE doctor_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setBigDecimal(1, ClinicRateConfig.MORNING_RATE);
            stmt.setBigDecimal(2, ClinicRateConfig.NIGHT_RATE);
            stmt.setInt(3, doctorId);

            int updated = stmt.executeUpdate();

            if (updated == 0) {
                String insertSql = "INSERT INTO fees "
                        + "(doctor_id, morning_rate, night_rate) VALUES (?, ?, ?)";

                try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                    insert.setInt(1, doctorId);
                    insert.setBigDecimal(2, ClinicRateConfig.MORNING_RATE);
                    insert.setBigDecimal(3, ClinicRateConfig.NIGHT_RATE);
                    insert.executeUpdate();
                }
            }
        }
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
