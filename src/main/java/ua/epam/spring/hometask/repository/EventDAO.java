package ua.epam.spring.hometask.repository;

import ua.epam.spring.hometask.domain.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Igor on 12.05.2016.
 */
public interface EventDAO extends CrudRepository<Event> {
    Event find(String name);

    Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to);

    Set<Event> getNextEvents(LocalDateTime to);
}
