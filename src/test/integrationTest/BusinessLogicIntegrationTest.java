import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.repository.EventDAO;
import ua.epam.spring.hometask.repository.TicketDAO;
import ua.epam.spring.hometask.repository.UserDAO;
import ua.epam.spring.hometask.repository.impl.UserDAOImpl;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.BookingService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;
import ua.epam.spring.hometask.spring.configuration.SpringConfiguration;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static util.EntitiesFactory.*;

/**
 * Created by Igor on 13.05.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class BusinessLogicIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    EventService eventService;
    @Autowired
    BookingService bookingService;
    @Autowired
    UserDAO userDAO;
    @Autowired
    EventDAO eventDAO;
    @Autowired
    TicketDAO ticketDao;
    @Autowired
    AuditoriumService auditoriumService;

    @Test
//    @Ignore
    public void healthcheck() {
        assertNotNull(userService);
        assertNotNull(eventService);
        assertNotNull(bookingService);
        int size = userService.getAll().size();
        assertEquals(0, size);
//        long idCounter = userDAO.getIdCounter();
//        assertEquals(0, idCounter);
    }

    @Test
    public void shouldInjectAuditoriums() {
        assertEquals(2, auditoriumService.getAll().size());
    }

    @Test
    public void shouldBookTicketsAndPersistInDb() {
        //given
        Auditorium auditorium = generateAuditorium();
        auditoriumService.addAuditorium(auditorium);
        LocalDateTime airDate = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        Event event = generateEvent(airDate, auditorium);
        eventDAO.save(event);
        String userEmail = "Vladimir_Lenskiy@poet.com";
        User unregisteredUser = generateUser(airDate.minus(1, ChronoUnit.DAYS), userEmail);
        userDAO.save(unregisteredUser);

        Ticket ticket1 = new Ticket(unregisteredUser, event, airDate, 10, TicketStatus.NEW, 1d);
        Ticket ticket2 = new Ticket(unregisteredUser, event, airDate, 11, TicketStatus.NEW, 1d);
        Ticket ticket3 = new Ticket(unregisteredUser, event, airDate, 12, TicketStatus.NEW, 1d);
        Set<Ticket> ticketSet = new HashSet<Ticket>() {
            {
                add(ticket1);
                add(ticket2);
                add(ticket3);
            }
        };
        //when
        bookingService.bookTickets(ticketSet);
        //then
        //todo for some reason db did not cleared after another test case.
        User registeredUser = userDAO.find(unregisteredUser.getId());
        //todo not working due to
        assertEquals(3, registeredUser.getTickets().size());
        assertEquals(TicketStatus.BOOKED, registeredUser.getTickets().iterator().next().getTicketStatus());
    }

    @Test
    public void shouldRegisterUser() {
        LocalDateTime birthday = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        String userEmail = "Vladimir_Lenskiy@poet.com";
        User unregisteredUser = generateUser(birthday, userEmail);
        //when
        User user = userDAO.save(unregisteredUser);
        //then
        User registeredUser = userDAO.find(userEmail);
        assertNotNull(registeredUser);
        assertEquals(birthday, registeredUser.getBirthday());
    }

    @Test
    public void shouldSearchPurchasedTicketsForParticularEvent() {
        LocalDateTime searchedAirTime = LocalDateTime.of(1999, Month.APRIL, 13, 18, 45);
        LocalDateTime wrongAirTime = LocalDateTime.of(1999, Month.APRIL, 19, 18, 45);
        Event event = generateEvent(searchedAirTime, generateAuditorium());
        event.addAirDateTime(wrongAirTime);
        Ticket goodTicket = new Ticket(null, event, searchedAirTime, 10, TicketStatus.NEW, 1d);
        goodTicket.setId(1L);
        goodTicket.setTicketStatus(TicketStatus.PURCHASED);
        Ticket wrongTicket1 = new Ticket(null, event, wrongAirTime, 10, TicketStatus.NEW, 1d);
        wrongTicket1.setId(2L);
        wrongTicket1.setTicketStatus(TicketStatus.PURCHASED);
        Ticket wrongTicket2 = new Ticket(null, event, searchedAirTime, 11, TicketStatus.NEW, 1d);
        wrongTicket2.setId(3L);
        wrongTicket2.setTicketStatus(TicketStatus.BOOKED);
        ticketDao.save(goodTicket);
        ticketDao.save(wrongTicket1);
        ticketDao.save(wrongTicket2);
        //when
        Set<Ticket> result = bookingService.getPurchasedTicketsForEvent(event, searchedAirTime);
        //then
        assertEquals(1, result.size());
        Long ticketId = result.iterator().next().getId();
        assertEquals(Long.valueOf(1L), ticketId);
    }

    @Test
    public void shouldCalculatePriceForTicketsWithDiscountAndElse() {
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
        double result = bookingService.getTicketsPrice(event, dateTime, user, vipSeats);
        //then
        //100*2(2 seats)*1.2(HighPriorityShow)*2(VIP seats)*0.8(BirthdayDiscount)
        assertEquals(384, result, 0.4);
    }


}
