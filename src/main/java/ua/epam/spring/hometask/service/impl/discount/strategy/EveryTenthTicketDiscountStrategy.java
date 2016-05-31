package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;

/**
 * Created by Igor on 13.05.2016.
 */
@Service
public class EveryTenthTicketDiscountStrategy implements DiscountStrategy {

    private static final byte TENTH_TICKET_PERCENTAGE_DISCOUNT = 50;

    //unit test documentation
    @Override
    public byte getDiscount(User user, LocalDateTime airDateTime, int numberOfTickets) {
        int userPurchasedTickets = user.getTickets().size();
        int ticketsForTheNextDiscount = userPurchasedTickets % 10;
        int amountOfDiscountedTickets = (ticketsForTheNextDiscount + numberOfTickets) / 10;
        double freeTickets = amountOfDiscountedTickets * (100d - TENTH_TICKET_PERCENTAGE_DISCOUNT) / 100;
        double newPriceInPercentsToOldPrice = (numberOfTickets - freeTickets) / numberOfTickets * 100;
        double totalDiscount = 100 - newPriceInPercentsToOldPrice;
        return ((byte) totalDiscount);
    }
}
