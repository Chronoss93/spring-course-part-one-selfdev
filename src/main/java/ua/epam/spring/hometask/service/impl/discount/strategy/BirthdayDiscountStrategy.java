package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Igor on 13.05.2016.
 */
@Service
public class BirthdayDiscountStrategy implements DiscountStrategy {
    private static final byte BIRTHDAY_PERCENTAGE_DISCOUNT = 20;
    private static final int DAYS_FROM_BIRTHDAY_FOR_DISCOUNT = 5;
    private static final int NO_DISCOUNT = 0;

    @Override
    public byte getDiscount(User user, LocalDateTime airDateTime, int numberOfTickets) {
        LocalDateTime userBirthday = user.getBirthday();
        if (userBirthday == null)
            return NO_DISCOUNT;
        return userBirthday.isAfter(airDateTime.minus(DAYS_FROM_BIRTHDAY_FOR_DISCOUNT, ChronoUnit.DAYS))
                && userBirthday.isBefore(airDateTime.plus(5, ChronoUnit.DAYS)) ?
                BIRTHDAY_PERCENTAGE_DISCOUNT : NO_DISCOUNT;
    }
}
