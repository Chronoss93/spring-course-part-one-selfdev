package ua.epam.spring.hometask.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.TicketStatus;
import ua.epam.spring.hometask.repository.TicketDAO;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Igor on 13.05.2016.
 */
@Repository
public class TicketDAOImpl implements TicketDAO {

    private static Set<Ticket> tickets = new HashSet<>();
    private static volatile Long idCounter;

    @PostConstruct
    public void init() {
        idCounter = (long) tickets.size();
    }

    @Override
    public Ticket save(Ticket obj) {
        obj.setId(generateId());
        tickets.add(obj);
        return obj;
    }

    @Override
    public void delete(Ticket obj) {
        tickets.remove(obj);
    }

    @Override
    public Ticket find(Long id) {
        for (Ticket ticket : tickets) {
            if (id.equals(ticket.getId())) {
                return ticket;
            }
        }
        return null;
    }

    @Override
    public Collection<Ticket> findAll() {
        return tickets;
    }

    //TODO: not used 'purchased' parameter
    @Override
    public Set<Ticket> getTickersForEventAndStatus(Event event, LocalDateTime dateTime, TicketStatus purchased) {
        return tickets.stream()
                .filter(ticket -> ticket.getDateTime().equals(dateTime)
                        && ticket.getEvent().equals(event)
                        && ticket.getTicketStatus().equals(TicketStatus.PURCHASED))
                .collect(Collectors.toSet());

    }

    private static synchronized Long generateId() {
        return ++idCounter;
    }

}
