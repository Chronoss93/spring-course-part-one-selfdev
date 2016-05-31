package util;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Created by Igor on 26.05.2016.
 */
public class EntitiesFactory {
    public static User generateUser(LocalDateTime birthday, String email) {
        User user = new User();
        user.setBirthday(birthday);
        user.setEmail(email);
        user.setFirstName("Vladimir");
        user.setLastName("Lenskiy");
        return user;
    }

    public static Event generateEvent(final LocalDateTime airDate, Auditorium auditorium) {
        Event event = new Event();
        event.setAirDates(new TreeSet<LocalDateTime>() {{
            add(airDate);
        }});
        event.setName("Ievgeniy Onegin");
        event.setBasePrice(100d);
        event.setRating(EventRating.HIGH);
        event.addAuditorium(airDate, auditorium);
        return event;
    }

    public static Auditorium generateAuditorium() {
        Auditorium auditorium = new Auditorium();
        auditorium.setName("Shevchenko theater");
        auditorium.setNumberOfSeats(300);
        auditorium.setVipSeats(new HashSet<Long>() {{
            add(1L);
            add(2L);
            add(3L);
            add(4L);
            add(5L);
        }});
        return auditorium;
    }
}
