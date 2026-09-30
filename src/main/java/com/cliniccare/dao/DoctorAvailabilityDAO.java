package com.cliniccare.dao;

import com.cliniccare.model.DoctorAvailability;

import java.time.LocalTime;
import java.util.List;

public interface DoctorAvailabilityDAO {

    boolean addSlot(DoctorAvailability slot);

    List<DoctorAvailability> getByDoctor(int doctorId);

    boolean deleteSlot(int availabilityId);

    /** True if the time range overlaps an existing slot for the same doctor and day. */
    boolean hasOverlap(int doctorId, String dayOfWeek,
                       LocalTime start, LocalTime end);
}
