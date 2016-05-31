package ua.epam.spring.hometask.repository.impl;

import com.google.common.collect.ImmutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.repository.UserDAO;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by Igor on 12.05.2016.
 */
@Repository
public class UserDAOImpl implements UserDAO {

    private static Set<User> users = new HashSet<>();

    private static volatile Long idCounter;

    @PostConstruct
    public void init() {
        idCounter = (long) users.size();
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User save(User obj) {
        String query = "insert into user1 (first_name, second_name, email, birthday) values (?,?,?,?)";
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
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public User find(Long id) {
        for (User user : users) {
            if (user.getId().equals(id))
                return user;
        }
        throw new NoSuchElementException("wrong id");
    }

    @Override
    public User find(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    @Override
    public boolean userRegistered(User user) {
        return users.contains(user);
    }

    @Override
    public Set<User> findAll() {
        return ImmutableSet.copyOf(users);
    }

    private static synchronized Long generateId() {
        return ++idCounter;
    }

    public Long getIdCounter() {
        return idCounter;
    }


}
