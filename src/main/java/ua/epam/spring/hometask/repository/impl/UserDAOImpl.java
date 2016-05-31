package ua.epam.spring.hometask.repository.impl;

import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Repository;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.repository.UserDAO;

import javax.annotation.PostConstruct;
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

    @Override
    public User save(User user) {
        user.setId(generateId());
        users.add(user);
        return user;
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
