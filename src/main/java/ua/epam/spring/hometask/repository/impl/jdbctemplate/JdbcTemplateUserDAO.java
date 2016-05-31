package ua.epam.spring.hometask.repository.impl.jdbctemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.repository.UserDAO;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

/**
 * Created by Igor_Kravchenko on 5/31/16.
 */
public class JdbcTemplateUserDAO implements UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User find(String email) {
        return null;
    }

    @Override
    public boolean userRegistered(User ticketUser) {
        return false;
    }

    @Override
    public User save(User obj) {
        String query = "insert into user1 (first_name, second_name, email, birthday) values (?,?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
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
        return obj;
    }

    @Override
    public void delete(User obj) {

    }

    @Override
    public User find(Long id) {
        return null;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }
}
