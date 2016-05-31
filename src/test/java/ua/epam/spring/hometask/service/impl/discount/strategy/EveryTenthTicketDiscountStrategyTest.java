package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.junit.Test;
import org.mockito.Mockito;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Igor on 13.05.2016.
 */
public class EveryTenthTicketDiscountStrategyTest {

    DiscountStrategy discountStrategy = new EveryTenthTicketDiscountStrategy();

    @Test
    public void shouldCalculateDiscountForOneTicket() {
        //given
        int ticketsPurchased = 2;
        User user = new User();
        Set<Ticket> tickets = Mockito.mock(TreeSet.class);
        when(tickets.size()).thenReturn(9);
        user.setTickets(tickets);
        //when
        byte result = discountStrategy.getDiscount(user, null, ticketsPurchased);
        //then
        assertEquals(25, result);
    }

    @Test
    public void shouldCalculateDiscountForTwoTickets(){
        int ticketsPurchased = 12;
        User user = new User();
        Set<Ticket> tickets = Mockito.mock(TreeSet.class);
        when(tickets.size()).thenReturn(9);
        user.setTickets(tickets);

        byte result = discountStrategy.getDiscount(user, null, ticketsPurchased);
        //with 50% discount price should be 11/12 - approx 8% discount
        assertEquals(8, result);
    }

}