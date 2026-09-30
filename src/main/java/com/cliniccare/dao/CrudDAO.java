package com.cliniccare.dao;

import java.util.List;

/**
 * Generic DAO interface. Every DAO in the Doctor & Service module
 * implements these six operations, so servlets depend on the
 * interface and not on the database code.
 */
public interface CrudDAO<T> {

    boolean add(T item);

    T getById(int id);

    List<T> getAll();

    boolean update(T item);

    boolean delete(int id);

    List<T> search(String keyword);
}
