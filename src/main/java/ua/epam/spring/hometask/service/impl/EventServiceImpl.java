package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.repository.EventDAO;
import ua.epam.spring.hometask.service.EventService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Igor on 12.05.2016.
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDAO eventDao;

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return eventDao.find(name);
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull LocalDateTime from, @Nonnull LocalDateTime to) {
        return eventDao.getForDateRange(from, to);
    }

    @Nonnull
    @Override
    public Set<Event> getNextEvents(@Nonnull LocalDateTime to) {
        return eventDao.getNextEvents(to);
    }

    @Override
    public Event save(@Nonnull Event object) {
        return eventDao.save(object);
    }

    @Override
    public void remove(@Nonnull Event object) {
        eventDao.delete(object);
    }

    @Override
    public Event getById(@Nonnull Long id) {
        return eventDao.find(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        return eventDao.findAll();
    }

}
