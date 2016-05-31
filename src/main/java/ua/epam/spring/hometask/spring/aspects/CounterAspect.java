package ua.epam.spring.hometask.spring.aspects;

import lombok.Getter;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 24.05.2016.
 */
@Aspect
@Component
public class CounterAspect extends AbstractGenericCounter {

    @Getter
    private Map<Event, Integer> eventAccessedByNameMap = new HashMap<>();
    @Getter
    private Map<Event, Integer> priceWasQueriedCounterMap = new HashMap<>();
    @Getter
    private Map<Event, Integer> ticketWasBookedForEventCounterMap = new HashMap<>();


    @AfterReturning(value = "execution(* " +
            "ua.epam.spring.hometask.repository.impl.EventDAOImpl.find(..)) && args(name)", returning = "retVal")
    private void eventWasAccessedByName(Event retVal, String name) {
        incrementCount(eventAccessedByNameMap, retVal);

    }

    @AfterReturning(value = "execution(* " +
            "ua.epam.spring.hometask.service.impl.BookingServiceImpl.getTicketsPrice(..))&& args(event,..)")
    private void eventPriceWasQueried(Event event) {
        incrementCount(priceWasQueriedCounterMap, event);
    }

    @AfterReturning(value = "execution(* " +
            "ua.epam.spring.hometask.service.impl.BookingServiceImpl.bookOneTicket(..))&& args(ticket)")
    private void ticketWasBookedForEvent(Ticket ticket) {
        Event event = ticket.getEvent();
        incrementCount(ticketWasBookedForEventCounterMap, event);
    }


}
