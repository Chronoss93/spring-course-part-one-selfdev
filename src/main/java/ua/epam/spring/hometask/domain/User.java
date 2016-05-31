package ua.epam.spring.hometask.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Yuriy_Tkach
 */
@EqualsAndHashCode(exclude = "tickets", callSuper = false)
@ToString(exclude = "tickets")
public class User extends DomainObject {
    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private LocalDateTime birthday;

    private Set<Ticket> tickets = new TreeSet<>();

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }


}
