package ua.epam.spring.hometask.spring.aspects;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.DiscountResultDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 24.05.2016.
 */
@Aspect
@Component
@Slf4j
public class DiscountAspect extends AbstractGenericCounter {

    @Getter
    private Map<Class<?>, Integer> counterForStrategies = new HashMap<>();
    @Getter
    private Map<User, Integer> counterForUsers = new HashMap<>();


    @AfterReturning(value = "execution(* ua.epam.spring.hometask.service.impl.DiscountServiceImpl.getDiscount(..)) " +
            "&& args(user, ..)", returning = "retVal")
    public void countUserDiscountCalls(User user, DiscountResultDto retVal) {
        addRecordForDiscountStrategy(retVal);
        addRecordForDiscountUser(user, retVal);
    }

    private void addRecordForDiscountUser(User user, DiscountResultDto resultDto) {
        if (resultDto.getDiscountAmount() <= 0)
            return;
        incrementCount(counterForUsers, user);
    }

    /**
     * null key contains all not discounted tickets. Не баг а фича
     */
    private void addRecordForDiscountStrategy(DiscountResultDto retVal) {
        Class<?> clazz = retVal.getDiscountStrategy();
        incrementCount(counterForStrategies, clazz);
    }

}
