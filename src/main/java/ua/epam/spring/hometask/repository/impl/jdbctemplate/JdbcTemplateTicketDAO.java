package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.TicketStatus;
import ua.epam.spring.hometask.repository.TicketDAO;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
public class JdbcTemplateTicketDAO extends GenericJdbcTemplateDAO<Ticket> implements TicketDAO {
    private static final String TICKET_TABLE_NAME = "ticket";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    static RowMapper<Ticket> ticketRowMapper = (rs, rowNum) -> {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getLong("ticket.id"));
        ticket.setDateTime(LocalDateTime.parse(rs.getString("ticket.date")));
        ticket.setSeat(rs.getLong("ticket.seat"));
        ticket.setPrice(rs.getDouble("ticket.price"));
        ticket.setTicketStatus(TicketStatus.valueOf(rs.getString("ticket.status")));
        //todo no set user
        //todo no set event
        return ticket;
    };

    @Autowired
    public JdbcTemplateTicketDAO() {
        super(TICKET_TABLE_NAME, ticketRowMapper);
    }

    @Override
    public Set<Ticket> getTickersForEventAndStatus(Event event, LocalDateTime dateTime, TicketStatus status) {
        String query = "select * from ticket where event_id = ? and date = ? and status = ?";
        List<Ticket> result = jdbcTemplate.query(query, new Object[]{event.getId(), dateTime.toString(), status.name()}, rowMapper);
        return new HashSet<>(result);
    }

    @Override
    public Ticket save(Ticket obj) {
        String query = "insert into ticket (date, seat, price, status) values (?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, obj.getDateTime().toString());
            ps.setLong(2, obj.getSeat());
            ps.setDouble(3, obj.getPrice());
            ps.setString(4, obj.getTicketStatus().name());
            return ps;
        }, holder);
        Long id = holder.getKey().longValue();
        obj.setId(id);
        return obj;
    }
}
