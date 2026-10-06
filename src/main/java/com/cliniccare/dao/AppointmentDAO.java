package com.cliniccare.dao;

import com.cliniccare.model.Appointment;
import com.cliniccare.model.ScheduleOption;
import com.cliniccare.service.AppointmentChargeCalculator;
import com.cliniccare.service.AppointmentChargeCalculator.ChargeBreakdown;
import com.cliniccare.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO implements AppointmentDAOInterface {

    // =========================================================
    // APPOINTMENT SELECT
    // =========================================================

    private static final String BASE_SELECT = """
            SELECT
                a.appointment_id,
                a.patient_id,
                p.full_name AS patient_name,
                a.schedule_id,
                s.doctor_id,
                d.doctor_name,
                d.specialization,
                a.appointment_date,
                a.start_time,
                a.end_time,
                a.rate_type,
                a.rate_used,
                a.duration_hours,
                a.total_charge,
                a.reason,
                a.status
            FROM appointment a
            JOIN patient p
                ON p.patient_id = a.patient_id
            JOIN scheduling s
                ON s.schedule_id = a.schedule_id
            JOIN doctor d
                ON d.doctor_id = s.doctor_id
            """;


    // =========================================================
    // SCHEDULE SELECT
    // =========================================================

    private static final String SCHEDULE_SELECT = """
            SELECT
                s.schedule_id,
                s.doctor_id,
                d.doctor_name,
                d.specialization,
                s.available_date,
                s.start_time,
                s.end_time,
                s.status AS schedule_status,
                f.morning_rate,
                f.night_rate
            FROM scheduling s
            JOIN doctor d
                ON d.doctor_id = s.doctor_id
            JOIN fees f
                ON f.fee_id = (
                    SELECT MAX(f2.fee_id)
                    FROM fees f2
                    WHERE f2.doctor_id = s.doctor_id
                )
            """;


    // =========================================================
    // ADD APPOINTMENT
    // =========================================================

    @Override
    public boolean addAppointment(
            Appointment appointment) {

        String insertSql = """
                INSERT INTO appointment
                (
                    patient_id,
                    schedule_id,
                    appointment_date,
                    start_time,
                    end_time,
                    rate_type,
                    rate_used,
                    duration_hours,
                    total_charge,
                    reason,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;


        Connection conn = null;


        try {

            conn =
                    DBConnection.getConnection();


            conn.setAutoCommit(false);


            // Check whether schedule
            // is still available
            if (!scheduleCanBeBooked(
                    conn,
                    appointment.getScheduleId(),
                    null)) {

                conn.rollback();

                return false;
            }


            // Insert appointment
            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    insertSql
                            )
            ) {

                setAppointmentValues(
                        stmt,
                        appointment,
                        false
                );


                if (stmt.executeUpdate() == 0) {

                    conn.rollback();

                    return false;
                }
            }


            // Change schedule status
            // to Booked
            setScheduleStatus(
                    conn,
                    appointment.getScheduleId(),
                    "Booked"
            );


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


    // =========================================================
    // GET APPOINTMENT BY ID
    // =========================================================

    @Override
    public Appointment getAppointmentById(
            int appointmentId) {

        String sql =
                BASE_SELECT
                + " WHERE a.appointment_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    appointmentId
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    return mapAppointment(rs);
                }
            }


        } catch (SQLException e) {

            e.printStackTrace();
        }


        return null;
    }


    // =========================================================
    // FIND APPOINTMENTS
    // =========================================================

    @Override
    public List<Appointment> findAppointments(
            String keyword,
            String status,
            Integer patientId) {

        List<Appointment> appointments =
                new ArrayList<>();


        List<Object> parameters =
                new ArrayList<>();


        StringBuilder sql =
                new StringBuilder(
                        BASE_SELECT
                );


        sql.append(
                " WHERE 1 = 1 "
        );


        // =====================================
        // PATIENT FILTER
        // =====================================

        if (patientId != null) {

            sql.append(
                    " AND a.patient_id = ? "
            );

            parameters.add(
                    patientId
            );
        }


        // =====================================
        // STATUS FILTER
        // =====================================

        if (status != null
                && !status.isBlank()) {

            sql.append(
                    " AND UPPER(a.status) = UPPER(?) "
            );

            parameters.add(
                    status.trim()
            );
        }


        // =====================================
        // SEARCH
        // =====================================

        if (keyword != null
                && !keyword.isBlank()) {

            sql.append("""
                    AND (
                        CAST(a.appointment_id AS CHAR) LIKE ?
                        OR p.full_name LIKE ?
                        OR d.doctor_name LIKE ?
                        OR d.specialization LIKE ?
                        OR COALESCE(a.reason, '') LIKE ?
                        OR CAST(a.appointment_date AS CHAR) LIKE ?
                    )
                    """);


            String value =
                    "%"
                    + keyword.trim()
                    + "%";


            for (int i = 0;
                 i < 6;
                 i++) {

                parameters.add(
                        value
                );
            }
        }


        // =====================================
        // ORDER
        // =====================================

        sql.append(
                " ORDER BY "
                + "a.appointment_date DESC, "
                + "a.start_time DESC"
        );


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(
                                sql.toString()
                        )
        ) {


            for (int i = 0;
                 i < parameters.size();
                 i++) {

                stmt.setObject(
                        i + 1,
                        parameters.get(i)
                );
            }


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    appointments.add(
                            mapAppointment(rs)
                    );
                }
            }


        } catch (SQLException e) {

            e.printStackTrace();
        }


        return appointments;
    }


    // =========================================================
    // UPDATE APPOINTMENT
    // =========================================================

    @Override
    public boolean updateAppointment(
            Appointment appointment) {

        String updateSql = """
                UPDATE appointment
                SET
                    patient_id = ?,
                    schedule_id = ?,
                    appointment_date = ?,
                    start_time = ?,
                    end_time = ?,
                    rate_type = ?,
                    rate_used = ?,
                    duration_hours = ?,
                    total_charge = ?,
                    reason = ?,
                    status = ?
                WHERE appointment_id = ?
                """;


        Connection conn = null;


        try {

            conn =
                    DBConnection.getConnection();


            conn.setAutoCommit(false);


            AppointmentState existing =
                    getAppointmentState(
                            conn,
                            appointment.getAppointmentId()
                    );


            if (existing == null) {

                conn.rollback();

                return false;
            }


            boolean changedSchedule =
                    existing.scheduleId()
                    != appointment.getScheduleId();


            // =====================================
            // CHECK NEW SCHEDULE
            // =====================================

            if (changedSchedule
                    &&
                    !scheduleCanBeBooked(
                            conn,
                            appointment.getScheduleId(),
                            appointment.getAppointmentId()
                    )) {

                conn.rollback();

                return false;
            }


            // =====================================
            // UPDATE APPOINTMENT
            // =====================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    updateSql
                            )
            ) {

                setAppointmentValues(
                        stmt,
                        appointment,
                        true
                );


                if (stmt.executeUpdate() == 0) {

                    conn.rollback();

                    return false;
                }
            }


            // =====================================
            // UPDATE SCHEDULE
            // =====================================

            if (changedSchedule) {

                releaseScheduleIfUnused(
                        conn,
                        existing.scheduleId()
                );


                setScheduleStatus(
                        conn,
                        appointment.getScheduleId(),
                        "Booked"
                );
            }


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


    // =========================================================
    // CANCEL APPOINTMENT
    // =========================================================

    @Override
    public boolean cancelAppointment(
            int appointmentId) {

        return updateAppointmentStatus(
                appointmentId,
                "Cancelled"
        );
    }


    // =========================================================
    // UPDATE APPOINTMENT STATUS
    // =========================================================

    @Override
    public boolean updateAppointmentStatus(
            int appointmentId,
            String status) {

        String normalizedStatus =
                normalizeStatus(status);


        if (normalizedStatus == null) {

            return false;
        }


        String updateSql =
                """
                UPDATE appointment
                SET status = ?
                WHERE appointment_id = ?
                """;


        Connection conn = null;


        try {

            conn =
                    DBConnection.getConnection();


            conn.setAutoCommit(false);


            AppointmentState existing =
                    getAppointmentState(
                            conn,
                            appointmentId
                    );


            if (existing == null) {

                conn.rollback();

                return false;
            }


            boolean wasCancelled =
                    "Cancelled".equalsIgnoreCase(
                            existing.status()
                    );


            boolean willBeCancelled =
                    "Cancelled".equalsIgnoreCase(
                            normalizedStatus
                    );


            // =====================================
            // REOPEN CANCELLED APPOINTMENT
            // =====================================

            if (wasCancelled
                    && !willBeCancelled) {

                if (!scheduleCanBeBooked(
                        conn,
                        existing.scheduleId(),
                        appointmentId)) {

                    conn.rollback();

                    return false;
                }


                setScheduleStatus(
                        conn,
                        existing.scheduleId(),
                        "Booked"
                );
            }


            // =====================================
            // UPDATE STATUS
            // =====================================

            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    updateSql
                            )
            ) {

                stmt.setString(
                        1,
                        normalizedStatus
                );


                stmt.setInt(
                        2,
                        appointmentId
                );


                if (stmt.executeUpdate() == 0) {

                    conn.rollback();

                    return false;
                }
            }


            // =====================================
            // RELEASE SCHEDULE IF CANCELLED
            // =====================================

            if (willBeCancelled) {

                releaseScheduleIfUnused(
                        conn,
                        existing.scheduleId()
                );
            }


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


    // =========================================================
    // DELETE APPOINTMENT
    // =========================================================

    @Override
    public boolean deleteAppointment(
            int appointmentId) {

        String deleteSql =
                """
                DELETE FROM appointment
                WHERE appointment_id = ?
                """;


        Connection conn = null;


        try {

            conn =
                    DBConnection.getConnection();


            conn.setAutoCommit(false);


            AppointmentState existing =
                    getAppointmentState(
                            conn,
                            appointmentId
                    );


            if (existing == null) {

                conn.rollback();

                return false;
            }


            try (
                    PreparedStatement stmt =
                            conn.prepareStatement(
                                    deleteSql
                            )
            ) {

                stmt.setInt(
                        1,
                        appointmentId
                );


                if (stmt.executeUpdate() == 0) {

                    conn.rollback();

                    return false;
                }
            }


            releaseScheduleIfUnused(
                    conn,
                    existing.scheduleId()
            );


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


    // =========================================================
    // CHECK SCHEDULE AVAILABILITY
    // =========================================================

    @Override
    public boolean isScheduleAvailableForBooking(
            int scheduleId,
            Integer excludeAppointmentId) {

        try (
                Connection conn =
                        DBConnection.getConnection()
        ) {

            return scheduleCanBeBooked(
                    conn,
                    scheduleId,
                    excludeAppointmentId
            );


        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET AVAILABLE SCHEDULES
    // =========================================================

    @Override
    public List<ScheduleOption> getAvailableSchedules() {

        String sql =
                SCHEDULE_SELECT
                + """
                WHERE UPPER(s.status) = 'AVAILABLE'

                  AND (
                      s.available_date > CURDATE()

                      OR (
                          s.available_date = CURDATE()
                          AND s.start_time > CURTIME()
                      )
                  )

                  AND NOT EXISTS (

                      SELECT 1

                      FROM appointment a

                      WHERE a.schedule_id = s.schedule_id

                      AND UPPER(a.status) <> 'CANCELLED'
                  )

                ORDER BY
                    s.available_date,
                    s.start_time,
                    d.doctor_name
                """;


        return querySchedules(
                sql,
                null
        );
    }


    // =========================================================
    // GET SCHEDULES FOR EDIT
    // =========================================================

    @Override
    public List<ScheduleOption> getSchedulesForEdit(
            int currentScheduleId) {

        String sql =
                SCHEDULE_SELECT
                + """
                WHERE (

                    s.schedule_id = ?

                    OR (

                        UPPER(s.status) = 'AVAILABLE'

                        AND (

                            s.available_date > CURDATE()

                            OR (

                                s.available_date = CURDATE()

                                AND s.start_time > CURTIME()
                            )
                        )

                        AND NOT EXISTS (

                            SELECT 1

                            FROM appointment a

                            WHERE a.schedule_id = s.schedule_id

                            AND UPPER(a.status) <> 'CANCELLED'
                        )
                    )
                )

                ORDER BY
                    s.available_date,
                    s.start_time,
                    d.doctor_name
                """;


        return querySchedules(
                sql,
                currentScheduleId
        );
    }


    // =========================================================
    // GET SCHEDULE BY ID
    // =========================================================

    @Override
    public ScheduleOption getScheduleById(
            int scheduleId) {

        String sql =
                SCHEDULE_SELECT
                + " WHERE s.schedule_id = ?";


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    scheduleId
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    return mapScheduleOption(
                            rs
                    );
                }
            }


        } catch (
                SQLException |
                IllegalArgumentException e
        ) {

            e.printStackTrace();
        }


        return null;
    }


    // =========================================================
    // QUERY SCHEDULES
    // =========================================================

    private List<ScheduleOption> querySchedules(
            String sql,
            Integer scheduleId) {

        List<ScheduleOption> schedules =
                new ArrayList<>();


        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {


            if (scheduleId != null) {

                stmt.setInt(
                        1,
                        scheduleId
                );
            }


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    schedules.add(
                            mapScheduleOption(rs)
                    );
                }
            }


        } catch (
                SQLException |
                IllegalArgumentException e
        ) {

            e.printStackTrace();
        }


        return schedules;
    }


    // =========================================================
    // INTERNAL SCHEDULE CHECK
    // =========================================================

    private boolean scheduleCanBeBooked(
            Connection conn,
            int scheduleId,
            Integer excludeAppointmentId)
            throws SQLException {

        String sql = """
                SELECT
                    s.status,

                    (
                        SELECT COUNT(*)

                        FROM appointment a

                        WHERE a.schedule_id = s.schedule_id

                        AND UPPER(a.status) <> 'CANCELLED'

                        AND (
                            ? IS NULL
                            OR a.appointment_id <> ?
                        )
                    )
                    AS active_appointments,

                    (
                        SELECT COUNT(*)

                        FROM appointment current_a

                        WHERE current_a.appointment_id = ?

                        AND current_a.schedule_id =
                            s.schedule_id
                    )
                    AS is_current_schedule

                FROM scheduling s

                WHERE s.schedule_id = ?

                FOR UPDATE
                """;


        try (
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {


            if (excludeAppointmentId == null) {

                stmt.setNull(
                        1,
                        java.sql.Types.INTEGER
                );

                stmt.setNull(
                        2,
                        java.sql.Types.INTEGER
                );

                stmt.setNull(
                        3,
                        java.sql.Types.INTEGER
                );

            } else {

                stmt.setInt(
                        1,
                        excludeAppointmentId
                );

                stmt.setInt(
                        2,
                        excludeAppointmentId
                );

                stmt.setInt(
                        3,
                        excludeAppointmentId
                );
            }


            stmt.setInt(
                    4,
                    scheduleId
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {


                if (!rs.next()) {

                    return false;
                }


                String scheduleStatus =
                        rs.getString(
                                "status"
                        );


                int activeAppointments =
                        rs.getInt(
                                "active_appointments"
                        );


                boolean currentSchedule =
                        rs.getInt(
                                "is_current_schedule"
                        ) > 0;


                boolean statusAllowsBooking =

                        "Available".equalsIgnoreCase(
                                scheduleStatus
                        )

                        ||

                        (
                            currentSchedule
                            &&
                            "Booked".equalsIgnoreCase(
                                    scheduleStatus
                            )
                        );


                return statusAllowsBooking
                        &&
                        activeAppointments == 0;
            }
        }
    }


    // =========================================================
    // GET CURRENT APPOINTMENT STATE
    // =========================================================

    private AppointmentState getAppointmentState(
            Connection conn,
            int appointmentId)
            throws SQLException {

        String sql = """
                SELECT
                    appointment_id,
                    schedule_id,
                    status

                FROM appointment

                WHERE appointment_id = ?

                FOR UPDATE
                """;


        try (
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(
                    1,
                    appointmentId
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    return new AppointmentState(

                            rs.getInt(
                                    "schedule_id"
                            ),

                            rs.getString(
                                    "status"
                            )
                    );
                }
            }
        }


        return null;
    }


    // =========================================================
    // UPDATE SCHEDULE STATUS
    // =========================================================

    private void setScheduleStatus(
            Connection conn,
            int scheduleId,
            String status)
            throws SQLException {

        String sql =
                """
                UPDATE scheduling
                SET status = ?
                WHERE schedule_id = ?
                """;


        try (
                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    status
            );


            stmt.setInt(
                    2,
                    scheduleId
            );


            stmt.executeUpdate();
        }
    }


    // =========================================================
    // RELEASE SCHEDULE
    // =========================================================

    private void releaseScheduleIfUnused(
            Connection conn,
            int scheduleId)
            throws SQLException {

        String countSql = """
                SELECT COUNT(*)

                FROM appointment

                WHERE schedule_id = ?

                AND UPPER(status) <> 'CANCELLED'
                """;


        try (
                PreparedStatement stmt =
                        conn.prepareStatement(
                                countSql
                        )
        ) {

            stmt.setInt(
                    1,
                    scheduleId
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()
                        &&
                        rs.getInt(1) == 0) {

                    setScheduleStatus(
                            conn,
                            scheduleId,
                            "Available"
                    );
                }
            }
        }
    }


    // =========================================================
    // SET APPOINTMENT VALUES
    // =========================================================

    private void setAppointmentValues(
            PreparedStatement stmt,
            Appointment appointment,
            boolean includeId)
            throws SQLException {


        stmt.setInt(
                1,
                appointment.getPatientId()
        );


        stmt.setInt(
                2,
                appointment.getScheduleId()
        );


        stmt.setDate(
                3,
                Date.valueOf(
                        appointment.getAppointmentDate()
                )
        );


        stmt.setTime(
                4,
                Time.valueOf(
                        appointment.getStartTime()
                )
        );


        stmt.setTime(
                5,
                Time.valueOf(
                        appointment.getEndTime()
                )
        );


        stmt.setString(
                6,
                appointment.getRateType()
        );


        stmt.setBigDecimal(
                7,
                appointment.getRateUsed()
        );


        stmt.setBigDecimal(
                8,
                appointment.getDurationHours()
        );


        stmt.setBigDecimal(
                9,
                appointment.getTotalCharge()
        );


        stmt.setString(
                10,
                appointment.getReason()
        );


        stmt.setString(
                11,
                normalizeStatus(
                        appointment.getStatus()
                )
        );


        if (includeId) {

            stmt.setInt(
                    12,
                    appointment.getAppointmentId()
            );
        }
    }


    // =========================================================
    // MAP APPOINTMENT
    // =========================================================
    //
    // IMPORTANT:
    // Do NOT recalculate rate here.
    //
    // rate_type, rate_used,
    // duration_hours and total_charge
    // were already calculated when booking
    // and saved inside appointment table.
    //
    // =========================================================

    private Appointment mapAppointment(
            ResultSet rs)
            throws SQLException {

        Appointment appointment =
                new Appointment();


        // Appointment ID
        appointment.setAppointmentId(
                rs.getInt(
                        "appointment_id"
                )
        );


        // Patient ID
        appointment.setPatientId(
                rs.getInt(
                        "patient_id"
                )
        );


        // Patient Name
        appointment.setPatientName(
                rs.getString(
                        "patient_name"
                )
        );


        // Schedule
        appointment.setScheduleId(
                rs.getInt(
                        "schedule_id"
                )
        );


        // Doctor
        appointment.setDoctorId(
                rs.getInt(
                        "doctor_id"
                )
        );


        appointment.setDoctorName(
                rs.getString(
                        "doctor_name"
                )
        );


        appointment.setSpecialization(
                rs.getString(
                        "specialization"
                )
        );


        // =====================================
        // DATE
        // =====================================

        Date appointmentDate =
                rs.getDate(
                        "appointment_date"
                );


        if (appointmentDate != null) {

            appointment.setAppointmentDate(
                    appointmentDate.toLocalDate()
            );
        }


        // =====================================
        // START TIME
        // =====================================

        Time startTime =
                rs.getTime(
                        "start_time"
                );


        if (startTime != null) {

            appointment.setStartTime(
                    startTime.toLocalTime()
            );
        }


        // =====================================
        // END TIME
        // =====================================

        Time endTime =
                rs.getTime(
                        "end_time"
                );


        if (endTime != null) {

            appointment.setEndTime(
                    endTime.toLocalTime()
            );
        }


        // =====================================
        // READ SAVED CALCULATION
        // FROM APPOINTMENT TABLE
        // =====================================

        appointment.setRateType(
                rs.getString(
                        "rate_type"
                )
        );


        appointment.setRateUsed(
                rs.getBigDecimal(
                        "rate_used"
                )
        );


        appointment.setDurationHours(
                rs.getBigDecimal(
                        "duration_hours"
                )
        );


        appointment.setTotalCharge(
                rs.getBigDecimal(
                        "total_charge"
                )
        );


        // =====================================
        // REASON
        // =====================================

        appointment.setReason(
                rs.getString(
                        "reason"
                )
        );


        // =====================================
        // STATUS
        // =====================================

        appointment.setStatus(
                normalizeStatus(
                        rs.getString(
                                "status"
                        )
                )
        );


        return appointment;
    }


    // =========================================================
    // MAP SCHEDULE
    // =========================================================
    //
    // This is where the 7 PM rate
    // calculation is performed.
    //
    // =========================================================

    private ScheduleOption mapScheduleOption(
            ResultSet rs)
            throws SQLException {

        ScheduleOption schedule =
                new ScheduleOption();


        schedule.setScheduleId(
                rs.getInt(
                        "schedule_id"
                )
        );


        schedule.setDoctorId(
                rs.getInt(
                        "doctor_id"
                )
        );


        schedule.setDoctorName(
                rs.getString(
                        "doctor_name"
                )
        );


        schedule.setSpecialization(
                rs.getString(
                        "specialization"
                )
        );


        schedule.setAvailableDate(
                rs.getDate(
                        "available_date"
                ).toLocalDate()
        );


        schedule.setStartTime(
                rs.getTime(
                        "start_time"
                ).toLocalTime()
        );


        schedule.setEndTime(
                rs.getTime(
                        "end_time"
                ).toLocalTime()
        );


        schedule.setScheduleStatus(
                rs.getString(
                        "schedule_status"
                )
        );


        schedule.setMorningRate(
                rs.getBigDecimal(
                        "morning_rate"
                )
        );


        schedule.setNightRate(
                rs.getBigDecimal(
                        "night_rate"
                )
        );


        // =====================================
        // 7 PM RATE CALCULATION
        // =====================================

        ChargeBreakdown charge =
                AppointmentChargeCalculator
                        .calculate(

                                schedule.getStartTime(),

                                schedule.getEndTime(),

                                schedule.getMorningRate(),

                                schedule.getNightRate()
                        );


        schedule.setRateType(
                charge.rateType()
        );


        schedule.setRateUsed(
                charge.rateUsed()
        );


        schedule.setDurationHours(
                charge.durationHours()
        );


        schedule.setEstimatedCharge(
                charge.totalCharge()
        );


        schedule.setMorningHours(
                charge.morningHours()
        );


        schedule.setNightHours(
                charge.nightHours()
        );


        schedule.setMorningCharge(
                charge.morningCharge()
        );


        schedule.setNightCharge(
                charge.nightCharge()
        );


        return schedule;
    }


    // =========================================================
    // NORMALIZE STATUS
    // =========================================================

    private String normalizeStatus(
            String status) {

        if (status == null) {

            return "Pending";
        }


        return switch (
                status.trim().toUpperCase()
        ) {

            case "PENDING" ->
                    "Pending";

            case "CONFIRMED" ->
                    "Confirmed";

            case "COMPLETED" ->
                    "Completed";

            case "CANCELLED",
                 "CANCELED" ->
                    "Cancelled";

            default ->
                    null;
        };
    }


    // =========================================================
    // ROLLBACK
    // =========================================================

    private void rollbackQuietly(
            Connection conn) {

        if (conn != null) {

            try {

                conn.rollback();

            } catch (SQLException ignored) {

            }
        }
    }


    // =========================================================
    // CLOSE CONNECTION
    // =========================================================

    private void closeQuietly(
            Connection conn) {

        if (conn != null) {

            try {

                conn.setAutoCommit(true);

            } catch (SQLException ignored) {

            }


            try {

                conn.close();

            } catch (SQLException ignored) {

            }
        }
    }


    // =========================================================
    // APPOINTMENT STATE
    // =========================================================

    private record AppointmentState(
            int scheduleId,
            String status) {
    }
}