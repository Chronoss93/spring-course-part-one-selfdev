package ua.epam.spring.hometask.repository.impl;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.repository.EventDAO;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Igor on 12.05.2016.
 */
@Repository
public class EventDAOImpl implements EventDAO {

    private static Set<Event> events = new HashSet<>();

    private static volatile Long idCounter;

    @PostConstruct
    public void init() {
        idCounter = (long) events.size();
    }

    @Override
    public Event find(String name) {
        for (Event event : events) {
            if (event.getName().equals(name))
                return event;
        }
        throw new NoSuchElementException("wrong name");
    }

    @Override
    public Set<Event> getNextEvents(LocalDateTime to) {
        return getForDateRange(LocalDateTime.now(), to);
    }

    @Override
    public Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        Set<Event> result = new TreeSet<>();
        for (Event event : events) {
            for (LocalDateTime airDate : event.getAirDates()) {
                if (airDate.isAfter(from) && airDate.isBefore(to)) {
                    result.add(event);
                    break;
                }
            }
        }
        return result;
    }


    @Override
    public Event save(Event event) {
        event.setId(generateId());
        events.add(event);
        return event;
    }

    @Override
    public void delete(Event event) {
        events.remove(event);

    }

    @Override
    public Event find(Long id) {
        for (Event event : events) {
            if (event.getId().equals(id))
                return event;
        }
        throw new NoSuchElementException("wrong id");
    }

    @Override
    public Collection<Event> findAll() {
        return ImmutableSet.copyOf(events);
    }

    private static synchronized Long generateId() {
        return ++idCounter;
    }

}
