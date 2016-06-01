package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.repository.EventDAO;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
public class JdbcTemplateEventDAO extends GenericJdbcTemplateDAO<Event> implements EventDAO {


    private static final String EVENT_TABLE_NAME = "event";
    static RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setBasePrice(rs.getDouble("base_price"));
        event.setRating(EventRating.valueOf(rs.getString("rating")));
        return event;
    };

    public JdbcTemplateEventDAO() {
        super(EVENT_TABLE_NAME, eventRowMapper);
    }

    @Override
    public Event find(String name) {
        String query = "select * from event where name = ?";
        Event event = jdbcTemplate.queryForObject(query, new Object[]{name}, eventRowMapper);
        return event;
    }

    @Override
    public Set<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        //todo because problems with LocalDateTime and Derby. I know that everything below is awful
        //todo jodatime timestamp
        Collection<Event> collection = findAll();
        Set<Event> result = collection.stream().filter(event -> {
            for (LocalDateTime airDate : event.getAirDates())
                if (airDate.isAfter(from) && airDate.isBefore(to))
                    return true;
            return false;
        }).collect(Collectors.toSet());
        return result;
    }

    @Override
    public Set<Event> getNextEvents(LocalDateTime to) {
        Collection<Event> collection = findAll();
        Set<Event> result = collection.stream().filter(event -> {
            for (LocalDateTime airDate : event.getAirDates())
                if (airDate.isAfter(to))
                    return true;
            return false;
        }).collect(Collectors.toSet());
        return result;
    }

    @Override
    public Event save(Event obj) {
        String query = "insert into event (name, base_price, rating) values (?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, obj.getName());
            ps.setDouble(2, obj.getBasePrice());
            ps.setString(3, obj.getRating().name());
            return ps;
        }, holder);
        Long id = holder.getKey().longValue();
        obj.setId(id);
        return obj;
    }
}
