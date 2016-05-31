package ua.epam.spring.hometask.repository;

import java.util.Collection;

/**
 * Created by Igor on 12.05.2016.
 */
public interface CrudRepository<T> {

    T save(T obj);

    void delete(T obj);

    T find(Long id);

    Collection<T> findAll();

}
