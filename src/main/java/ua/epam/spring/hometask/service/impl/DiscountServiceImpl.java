package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Igor on 13.05.2016.
 */
@Component
public class DiscountServiceImpl implements DiscountService {

    @Autowired
    private List<DiscountStrategy> strategyList;

    @Override
    public DiscountResultDto getDiscount(@Nullable User user, @Nonnull Event event, @Nonnull LocalDateTime airDateTime, int numberOfTickets) {
        //todo use chain of responsibility?
        byte maxDiscount = 0;
        Class<? extends DiscountStrategy> discountStrategy = null;
        for (DiscountStrategy strategy : strategyList) {
            byte discount = strategy.getDiscount(user, airDateTime, numberOfTickets);
            if (discount > maxDiscount) {
                discountStrategy = strategy.getClass();
                maxDiscount = discount;
            }
        }
        return new DiscountResultDto(discountStrategy, maxDiscount);
    }

}
