package com.cliniccare.dao;

import com.cliniccare.model.DoctorAvailability;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorAvailabilityDAOImpl implements DoctorAvailabilityDAO {

    private DoctorAvailability mapRow(ResultSet rs) throws SQLException {

        DoctorAvailability slot = new DoctorAvailability();
        slot.setAvailabilityId(rs.getInt("availability_id"));
        slot.setDoctorId(rs.getInt("doctor_id"));
        slot.setDayOfWeek(rs.getString("day_of_week"));
        slot.setStartTime(rs.getTime("start_time").toLocalTime());
        slot.setEndTime(rs.getTime("end_time").toLocalTime());

        return slot;
    }

    @Override
    public boolean addSlot(DoctorAvailability slot) {

        String sql = "INSERT INTO doctor_availability "
                + "(doctor_id, day_of_week, start_time, end_time) "
                + "VALUES (?, ?, ?, ?)";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, slot.getDoctorId());
            stmt.setString(2, slot.getDayOfWeek());
            stmt.setTime(3, Time.valueOf(slot.getStartTime()));
            stmt.setTime(4, Time.valueOf(slot.getEndTime()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<DoctorAvailability> getByDoctor(int doctorId) {

        List<DoctorAvailability> slots = new ArrayList<>();

        String sql = "SELECT * FROM doctor_availability WHERE doctor_id = ? "
                + "ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', "
                + "'Thursday', 'Friday', 'Saturday', 'Sunday'), start_time";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, doctorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    slots.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return slots;
    }

    @Override
    public boolean deleteSlot(int availabilityId) {

        String sql = "DELETE FROM doctor_availability WHERE availability_id = ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, availabilityId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Two ranges overlap when: newStart < existingEnd AND newEnd > existingStart
    @Override
    public boolean hasOverlap(int doctorId, String dayOfWeek,
                              LocalTime start, LocalTime end) {

        String sql = "SELECT COUNT(*) FROM doctor_availability "
                + "WHERE doctor_id = ? AND day_of_week = ? "
                + "AND start_time < ? AND end_time > ?";

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, doctorId);
            stmt.setString(2, dayOfWeek);
            stmt.setTime(3, Time.valueOf(end));
            stmt.setTime(4, Time.valueOf(start));

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
