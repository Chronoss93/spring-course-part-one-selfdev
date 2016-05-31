package ua.epam.spring.hometask.service.impl.discount.strategy;

import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;

/**
 * Created by Igor on 13.05.2016.
 */
public interface DiscountStrategy {

    byte getDiscount(User user, LocalDateTime airDateTime, int numberOfTickets);
}
