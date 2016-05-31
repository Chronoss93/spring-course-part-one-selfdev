package ua.epam.spring.hometask.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
@NoArgsConstructor
@AllArgsConstructor()
@EqualsAndHashCode(callSuper = false, exclude = {
        "price", "ticketStatus"
})
public class Ticket extends DomainObject implements Comparable<Ticket> {

    @Getter @Setter
    private User user;

    @Getter @Setter
    private Event event;

    @Getter @Setter
    private LocalDateTime dateTime;

    @Getter @Setter
    private long seat;

    @Getter @Setter
    private TicketStatus ticketStatus;

    @Getter @Setter
    private Double price;

    @Override
    public int compareTo(Ticket other) {
        if (other == null) {
            return 1;
        }
        int result = dateTime.compareTo(other.getDateTime());

        if (result == 0) {
            result = event.getName().compareTo(other.getEvent().getName());
        }
        if (result == 0) {
            result = Long.compare(seat, other.getSeat());
        }
        return result;
    }

}
