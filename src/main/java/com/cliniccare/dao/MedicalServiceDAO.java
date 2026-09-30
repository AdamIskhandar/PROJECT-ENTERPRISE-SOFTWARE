package com.cliniccare.dao;

import com.cliniccare.model.MedicalService;

public interface MedicalServiceDAO extends CrudDAO<MedicalService> {

    /** True if another service (id different from excludeId) already has this name. */
    boolean nameExists(String name, int excludeId);
}
