package com.cliniccare.dao;

import com.cliniccare.model.Scheduling;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** DAO mapped directly to cliniccare.scheduling. */
public class SchedulingDAOImpl implements SchedulingDAO {

    private Scheduling mapRow(ResultSet rs) throws SQLException {
        Scheduling schedule = new Scheduling();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        schedule.setDoctorId(rs.getInt("doctor_id"));
        schedule.setAvailableDate(rs.getDate("available_date").toLocalDate());
        schedule.setStartTime(rs.getTime("start_time").toLocalTime());
        schedule.setEndTime(rs.getTime("end_time").toLocalTime());
        schedule.setStatus(rs.getString("status"));
        return schedule;
    }

    @Override
    public boolean addSchedule(Scheduling schedule) {
        String sql = "INSERT INTO scheduling "
                + "(doctor_id, available_date, start_time, end_time, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, schedule.getDoctorId());
            stmt.setDate(2, Date.valueOf(schedule.getAvailableDate()));
            stmt.setTime(3, Time.valueOf(schedule.getStartTime()));
            stmt.setTime(4, Time.valueOf(schedule.getEndTime()));
            stmt.setString(5, schedule.getStatus() == null ? "Available" : schedule.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Scheduling> getByDoctor(int doctorId) {
        List<Scheduling> schedules = new ArrayList<>();

        String sql = "SELECT schedule_id, doctor_id, available_date, "
                + "start_time, end_time, status "
                + "FROM scheduling WHERE doctor_id = ? "
                + "ORDER BY available_date DESC, start_time";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    @Override
    public boolean deleteSchedule(int scheduleId) {
        // Do not delete a booked schedule because it may be linked to an appointment.
        String sql = "DELETE FROM scheduling "
                + "WHERE schedule_id = ? AND UPPER(status) <> 'BOOKED'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean hasOverlap(int doctorId,
                              LocalDate availableDate,
                              LocalTime start,
                              LocalTime end) {
        String sql = "SELECT COUNT(*) FROM scheduling "
                + "WHERE doctor_id = ? AND available_date = ? "
                + "AND start_time < ? AND end_time > ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);
            stmt.setDate(2, Date.valueOf(availableDate));
            stmt.setTime(3, Time.valueOf(end));
            stmt.setTime(4, Time.valueOf(start));

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
