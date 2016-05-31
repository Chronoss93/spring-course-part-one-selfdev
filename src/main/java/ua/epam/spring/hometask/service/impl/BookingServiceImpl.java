package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.repository.TicketDAO;
import ua.epam.spring.hometask.repository.UserDAO;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.DiscountService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Igor on 12.05.2016.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private static final int NORMAL_SEAT_PRICE = 1;
    private static final int VIP_SEAT_MULTIPLIER = 2;

    @Autowired
    private DiscountService discountService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDAO;

    //todo dunno, maybe that is bad practice, to do such 'adapter' overloading
    @Override
    public double getTicketsPrice(Ticket ticket) {
        Event event = checkNotNull(ticket.getEvent());
        LocalDateTime dateTime = checkNotNull(ticket.getDateTime());
        User user = ticket.getUser();
        long seat = checkNotNull(ticket.getSeat());
        return getTicketsPrice(event, dateTime, user, Collections.singleton(seat));
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user, @Nonnull Set<Long> seats) {
        double seatsPrice = calculateSeatsPrice(event, dateTime, seats);
        seatsPrice *= calculateEventPriceMultiplier(event);
        seatsPrice *= calculateDiscountMultiplier(user, event, dateTime, seats);
        return seatsPrice;
    }


    private double calculateSeatsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nonnull Set<Long> seats) {
        double basePrice = checkNotNull(event.getBasePrice());
        Auditorium auditorium = checkNotNull(event.getAuditoriums().get(dateTime));
        double seatsPrice = 0;
        for (Long seat : seats) {
            double multiplier = calculateVipMultiplier(auditorium, seat);
            seatsPrice += multiplier * basePrice;
        }
        return seatsPrice;
    }

    private double calculateVipMultiplier(Auditorium auditorium, Long seat) {
        return auditorium.isVipSeat(seat) ? VIP_SEAT_MULTIPLIER : NORMAL_SEAT_PRICE;
    }

    private double calculateEventPriceMultiplier(@Nonnull Event event) {
        return event.getRating().getPriceMultiplier();
    }

    private double calculateDiscountMultiplier(User user, Event event, LocalDateTime dateTime, Set<Long> seats) {
        DiscountResultDto discountResultDto = discountService.getDiscount(user, event, dateTime, seats.size());
        byte discountAmount = discountResultDto.getDiscountAmount();
        return (100 - discountAmount) / 100d;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            bookOneTicket(ticket);
        }
    }

    @Override
    public void bookOneTicket(Ticket ticket) {
        double ticketsPrice = getTicketsPrice(ticket);
        ticket.setPrice(ticketsPrice);
        User user = ticket.getUser();

        user.addTicket(ticket);
        ticket.setTicketStatus(TicketStatus.BOOKED);
        ticketDAO.save(ticket);
        userDAO.save(user);
    }


    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        return ticketDAO.getTickersForEventAndStatus(event, dateTime, TicketStatus.PURCHASED);
    }


}
