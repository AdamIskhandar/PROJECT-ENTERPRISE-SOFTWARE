package com.cliniccare.dao;

import com.cliniccare.model.Patient;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Add/Register Patient
    public boolean addPatient(Patient patient) {

        String sql = """
                INSERT INTO patient
                (full_name, email, phone, password, gender, date_of_birth)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, patient.getFullName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhone());
            stmt.setString(4, patient.getPassword());
            stmt.setString(5, patient.getGender());

            if (patient.getDateOfBirth() != null) {
                stmt.setDate(
                    6,
                    Date.valueOf(patient.getDateOfBirth())
                );
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Find Patient by Email
    public Patient getPatientByEmail(String email) {

        String sql = "SELECT * FROM patient WHERE email = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        rs.getInt("patient_id")
                );

                patient.setFullName(
                        rs.getString("full_name")
                );

                patient.setEmail(
                        rs.getString("email")
                );

                patient.setPhone(
                        rs.getString("phone")
                );

                patient.setPassword(
                        rs.getString("password")
                );

                patient.setGender(
                        rs.getString("gender")
                );

                Date dob = rs.getDate("date_of_birth");

                if (dob != null) {
                    patient.setDateOfBirth(
                            dob.toLocalDate()
                    );
                }

                return patient;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Get All Patients
    public List<Patient> getAllPatients() {

        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patient ORDER BY patient_id DESC";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        rs.getInt("patient_id")
                );

                patient.setFullName(
                        rs.getString("full_name")
                );

                patient.setEmail(
                        rs.getString("email")
                );

                patient.setPhone(
                        rs.getString("phone")
                );

                patient.setPassword(
                        rs.getString("password")
                );

                patient.setGender(
                        rs.getString("gender")
                );

                Date dob = rs.getDate("date_of_birth");

                if (dob != null) {
                    patient.setDateOfBirth(
                            dob.toLocalDate()
                    );
                }

                patients.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }


    // Get Patient by ID
    public Patient getPatientById(int patientId) {

        String sql = "SELECT * FROM patient WHERE patient_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, patientId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        rs.getInt("patient_id")
                );

                patient.setFullName(
                        rs.getString("full_name")
                );

                patient.setEmail(
                        rs.getString("email")
                );

                patient.setPhone(
                        rs.getString("phone")
                );

                patient.setPassword(
                        rs.getString("password")
                );

                patient.setGender(
                        rs.getString("gender")
                );

                Date dob = rs.getDate("date_of_birth");

                if (dob != null) {
                    patient.setDateOfBirth(
                            dob.toLocalDate()
                    );
                }

                return patient;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Update Patient
    public boolean updatePatient(Patient patient) {

        String sql = """
                UPDATE patient
                SET full_name = ?,
                    email = ?,
                    phone = ?,
                    gender = ?,
                    date_of_birth = ?
                WHERE patient_id = ?
                """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, patient.getFullName());
            stmt.setString(2, patient.getEmail());
            stmt.setString(3, patient.getPhone());
            stmt.setString(4, patient.getGender());

            if (patient.getDateOfBirth() != null) {
                stmt.setDate(
                    5,
                    Date.valueOf(patient.getDateOfBirth())
                );
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }

            stmt.setInt(6, patient.getPatientId());

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Delete Patient
    public boolean deletePatient(int patientId) {

        String sql = "DELETE FROM patient WHERE patient_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, patientId);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // Search Patient
    public List<Patient> searchPatients(String keyword) {

        List<Patient> patients = new ArrayList<>();

        String sql = """
                SELECT * FROM patient
                WHERE full_name LIKE ?
                   OR email LIKE ?
                   OR phone LIKE ?
                ORDER BY full_name
                """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            String searchValue = "%" + keyword + "%";

            stmt.setString(1, searchValue);
            stmt.setString(2, searchValue);
            stmt.setString(3, searchValue);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Patient patient = new Patient();

                patient.setPatientId(
                        rs.getInt("patient_id")
                );

                patient.setFullName(
                        rs.getString("full_name")
                );

                patient.setEmail(
                        rs.getString("email")
                );

                patient.setPhone(
                        rs.getString("phone")
                );

                patient.setGender(
                        rs.getString("gender")
                );

                Date dob = rs.getDate("date_of_birth");

                if (dob != null) {
                    patient.setDateOfBirth(
                            dob.toLocalDate()
                    );
                }

                patients.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
}