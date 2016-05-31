package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.repository.UserDAO;
import ua.epam.spring.hometask.service.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by Igor on 12.05.2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return userDAO.find(email);
    }

    @Override
    public User save(@Nonnull User object) {
        return userDAO.save(object);
    }

    @Override
    public void remove(@Nonnull User object) {
        userDAO.delete(object);
    }

    @Override
    public User getById(@Nonnull Long id) {
        return userDAO.find(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return userDAO.findAll();
    }

}
