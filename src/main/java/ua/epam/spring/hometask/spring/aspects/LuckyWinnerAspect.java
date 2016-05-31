package ua.epam.spring.hometask.spring.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.domain.Ticket;

/**
 * Created by Igor on 24.05.2016.
 */
@Aspect
@Component
public class LuckyWinnerAspect {

    @Around(value = "execution(* " +
            "ua.epam.spring.hometask.service.impl.BookingServiceImpl.getTicketsPrice(..)) && args(ticket)")
    public Object luckyEventChecker(ProceedingJoinPoint jp, Ticket ticket) throws Throwable {
        if (!freeTicket()) {
            return jp.proceed();
        }
        return 0d;
    }

    private boolean freeTicket() {
//        return Math.random()>100000000;
        return true;
    }
}
