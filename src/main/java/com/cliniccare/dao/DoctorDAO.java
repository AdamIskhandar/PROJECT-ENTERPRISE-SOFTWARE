package com.cliniccare.dao;

import com.cliniccare.model.Doctor;

public interface DoctorDAO extends CrudDAO<Doctor> {

    /** True if another doctor (id different from excludeId) already uses this email. */
    boolean emailExists(String email, int excludeId);
}
