package com.cliniccare.dao;

import com.cliniccare.model.Appointment;
import com.cliniccare.model.ScheduleOption;

import java.util.List;

public interface AppointmentDAOInterface {

    boolean addAppointment(Appointment appointment);

    Appointment getAppointmentById(int appointmentId);

    List<Appointment> findAppointments(String keyword,
                                       String status,
                                       Integer patientId);

    boolean updateAppointment(Appointment appointment);

    boolean cancelAppointment(int appointmentId);

    boolean updateAppointmentStatus(int appointmentId, String status);

    boolean deleteAppointment(int appointmentId);

    boolean isScheduleAvailableForBooking(int scheduleId,
                                          Integer excludeAppointmentId);

    List<ScheduleOption> getAvailableSchedules();

    List<ScheduleOption> getSchedulesForEdit(int currentScheduleId);

    ScheduleOption getScheduleById(int scheduleId);
}
