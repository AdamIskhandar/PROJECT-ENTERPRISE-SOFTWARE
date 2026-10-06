package com.cliniccare.dao;

import com.cliniccare.model.Scheduling;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SchedulingDAO {

    boolean addSchedule(Scheduling schedule);

    List<Scheduling> getByDoctor(int doctorId);

    boolean deleteSchedule(int scheduleId);

    boolean hasOverlap(int doctorId,
                       LocalDate availableDate,
                       LocalTime start,
                       LocalTime end);
}
