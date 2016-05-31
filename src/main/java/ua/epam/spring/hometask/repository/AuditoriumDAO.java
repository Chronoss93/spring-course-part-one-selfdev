package ua.epam.spring.hometask.repository;

import ua.epam.spring.hometask.domain.Auditorium;

/**
 * Created by Igor on 12.05.2016.
 */
public interface AuditoriumDAO extends CrudRepository<Auditorium> {
    Auditorium find(String name);
}
