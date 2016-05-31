import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.repository.EventDAO;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.impl.discount.strategy.BirthdayDiscountStrategy;
import ua.epam.spring.hometask.spring.aspects.CounterAspect;
import ua.epam.spring.hometask.spring.aspects.DiscountAspect;
import ua.epam.spring.hometask.spring.configuration.SpringConfiguration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static util.EntitiesFactory.generateAuditorium;
import static util.EntitiesFactory.generateEvent;

/**
 * Created by Igor on 26.05.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class AspectIntegrationTest {

    @Autowired
    BookingService bookingService;
    @Autowired
    DiscountAspect discountAspect;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    CounterAspect counterAspect;

    @Test
    public void shouldCollectOneCallOfNullStrategyBucketAndZeroUserDiscounts() {
        LocalDateTime dateTime = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        Auditorium auditorium = generateAuditorium();
        Set<Long> vipSeats = new HashSet<Long>() {{
            add(10L);
            add(11L);
        }};
        auditorium.setVipSeats(vipSeats);

        Event event = generateEvent(dateTime, auditorium);
        event.setBasePrice(100);
        event.setRating(EventRating.HIGH);

        User user = new User();

        //when
        bookingService.getTicketsPrice(event, dateTime, user, vipSeats);
        //then
        int noStrategiesCalledAmount = discountAspect.getCounterForStrategies().get(null);
        assertEquals(1, noStrategiesCalledAmount);
        int usersDiscountCounter = discountAspect.getCounterForUsers().size();
        assertEquals(0, usersDiscountCounter);
    }

    @Test
    public void shouldCollectOneCallOfBirthdayDiscountAndOneUserDiscounts() {
        LocalDateTime dateTime = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        Auditorium auditorium = generateAuditorium();
        Set<Long> vipSeats = new HashSet<Long>() {{
            add(10L);
            add(11L);
        }};
        auditorium.setVipSeats(vipSeats);

        Event event = generateEvent(dateTime, auditorium);
        event.setBasePrice(100);
        event.setRating(EventRating.HIGH);

        User user = new User();
        user.setBirthday(dateTime);

        //when
        bookingService.getTicketsPrice(event, dateTime, user, vipSeats);
        //then
        int birthdayStrategyCounter = discountAspect.getCounterForStrategies().get(BirthdayDiscountStrategy.class);
        assertEquals(1, birthdayStrategyCounter);
        int usersDiscountCounter = discountAspect.getCounterForUsers().get(user);
        assertEquals(1, usersDiscountCounter);
    }

    @Test
    public void shouldCountEventSearchAspect() {
        Auditorium auditorium = generateAuditorium();
        LocalDateTime dateTime = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);

        Event event = generateEvent(dateTime, auditorium);
        eventDAO.save(event);
        //when
        eventDAO.find("Ievgeniy Onegin");
        eventDAO.find("Ievgeniy Onegin");
        eventDAO.find("Ievgeniy Onegin");
        //then
        int eventWasSearchedByNameCounter = counterAspect.getEventAccessedByNameMap().get(event);
        assertEquals(3, eventWasSearchedByNameCounter);

    }

    @Test
    public void shouldCollectPriceCallsForEvents() {
        Auditorium auditorium = generateAuditorium();
        LocalDateTime dateTime1 = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        LocalDateTime dateTime2 = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        Set<Long> vipSeats = new HashSet<Long>() {{
            add(10L);
            add(11L);
        }};
        auditorium.setVipSeats(vipSeats);

        Event event1 = generateEvent(dateTime1, auditorium);
        Event event2 = generateEvent(dateTime2, auditorium);
        event2.setName("anotherName");

        User user = new User();

        eventDAO.save(event1);
        eventDAO.save(event2);
        //when
        bookingService.getTicketsPrice(event1, dateTime1, user, vipSeats);
        bookingService.getTicketsPrice(event1, dateTime1, user, vipSeats);
        bookingService.getTicketsPrice(event2, dateTime2, user, vipSeats);
        bookingService.getTicketsPrice(event2, dateTime2, user, vipSeats);
        bookingService.getTicketsPrice(event2, dateTime2, user, vipSeats);
        //then
        int priceWasQueriedFromEvent1 = counterAspect.getPriceWasQueriedCounterMap().get(event1);
        assertEquals(2, priceWasQueriedFromEvent1);
        int priceWasQueriedFromEvent2 = counterAspect.getPriceWasQueriedCounterMap().get(event2);
        assertEquals(3, priceWasQueriedFromEvent2);

    }

    @Test
    public void testFreeTicket() {
        LocalDateTime dateTime = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        Auditorium auditorium = generateAuditorium();
        Set<Long> vipSeats = new HashSet<Long>() {{
            add(10L);
            add(11L);
        }};
        auditorium.setVipSeats(vipSeats);

        Event event = generateEvent(dateTime, auditorium);
        event.setBasePrice(100);
        event.setRating(EventRating.HIGH);

        User user = new User();
        user.setBirthday(dateTime);
        Ticket ticket = new Ticket(user, event, dateTime, 10L, TicketStatus.NEW, 100d);
        //when
        double result = bookingService.getTicketsPrice(ticket);
        //then
        assertEquals(0d, result, 0.1d);
    }
}
