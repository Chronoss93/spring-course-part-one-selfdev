package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.repository.TicketDAO;
import ua.epam.spring.hometask.repository.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import static ua.epam.spring.hometask.repository.impl.jdbctemplate.JdbcTemplateTicketDAO.ticketRowMapper;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
@Repository
public class JdbcTemplateUserDAO extends GenericJdbcTemplateDAO<User> implements UserDAO {
    private static final String USER_TABLE_NAME = "user1";
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //todo cool but evil
    private OneToManyResultSetExtractor<User, Ticket, Long> oneToManyResultSetExtractor =
            new OneToManyResultSetExtractor<User, Ticket, Long>(userRowMapper, ticketRowMapper) {
                @Override
                protected Long mapPrimaryKey(ResultSet resultSet) throws SQLException {
                    return resultSet.getLong("user1.id");
                }

                @Override
                protected Long mapForeignKey(ResultSet resultSet) throws SQLException {
                    return resultSet.getLong("ticket.user_id");
                }

                @Override
                protected void addChild(User user, Ticket ticket) {
                    user.getTickets().add(ticket);
                }
            };

    static RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User usr = new User();
        usr.setId(rs.getLong("id"));
        usr.setFirstName(rs.getString("first_name"));
        usr.setLastName(rs.getString("second_name"));
        usr.setEmail(rs.getString("email"));
        usr.setBirthday(LocalDateTime.parse(rs.getString("birthday")));
        return usr;
    };

    public JdbcTemplateUserDAO() {
        super(USER_TABLE_NAME, userRowMapper);
    }


    @Override
    public User find(String email) {
        String query = "select * from user1 where user1.email = ?";
//        String query1 = "select user1.id, user1.first_name, user1.second_name, user1.email, user1.birthday from user1 as user1
// left join ticket on user1.id = ticket.user_id where user1.email = ?";
//        jdbcTemplate.query(query);
        User user = jdbcTemplate.queryForObject(query, new Object[]{email}, userRowMapper);
        return user;
    }

    @Override
    public boolean userRegistered(User ticketUser) {
        String query = "select count(*) from user1 where email = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{ticketUser.getEmail()}, Integer.class);
        return count > 0;
    }


    @Override
    public User save(User obj) {
        String query = "insert into user1 (first_name, second_name, email, birthday) values (?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();

        //todo try SimpleJdbcInsert with BaseObjectMapper<T>.
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, obj.getFirstName());
            ps.setString(2, obj.getLastName());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getBirthday().toString());
            return ps;
        }, holder);
        Long userId = holder.getKey().longValue();
        obj.setId(userId);
        //todo JDBC is pain. You never saw next 3lines xD
        for (Ticket ticket : obj.getTickets()) {
            ticketDAO.save(ticket);
        }
        return obj;
    }
}
