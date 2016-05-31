package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.repository.EventDAO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
public class JdbcTemplateEventDAO implements EventDAO {
    @Override
    public Event find(String name) {
        return null;
    }

    @Override
    public Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return null;
    }

    @Override
    public Set<Event> getNextEvents(LocalDateTime to) {
        return null;
    }

    @Override
    public Event save(Event obj) {
        return null;
    }

    @Override
    public void delete(Event obj) {

    }

    @Override
    public Event find(Long id) {
        return null;
    }

    @Override
    public Collection<Event> findAll() {
        return null;
    }
}
