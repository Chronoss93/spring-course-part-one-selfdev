package ua.epam.spring.hometask.repository;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.TicketStatus;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Igor on 13.05.2016.
 */
public interface TicketDAO extends CrudRepository<Ticket> {
    Set<Ticket> getTickersForEventAndStatus(Event event, LocalDateTime dateTime, TicketStatus purchased);
}
