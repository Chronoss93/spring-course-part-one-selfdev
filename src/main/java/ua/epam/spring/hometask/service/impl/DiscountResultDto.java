package ua.epam.spring.hometask.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;

/**
 * Created by Igor on 26.05.2016.
 */
@AllArgsConstructor
public class DiscountResultDto {
    @Getter
    private Class<? extends DiscountStrategy> discountStrategy;
    @Getter
    private byte discountAmount;
}
