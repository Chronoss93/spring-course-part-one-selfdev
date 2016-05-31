package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.TicketStatus;
import ua.epam.spring.hometask.repository.TicketDAO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
public class JdbcTemplateTicketDAO implements TicketDAO {
    @Override
    public Set<Ticket> getTickersForEventAndStatus(Event event, LocalDateTime dateTime, TicketStatus purchased) {
        return null;
    }

    @Override
    public Ticket save(Ticket obj) {
        return null;
    }

    @Override
    public void delete(Ticket obj) {

    }

    @Override
    public Ticket find(Long id) {
        return null;
    }

    @Override
    public Collection<Ticket> findAll() {
        return null;
    }
}
