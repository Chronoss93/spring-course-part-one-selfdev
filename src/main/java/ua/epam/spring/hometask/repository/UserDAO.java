package ua.epam.spring.hometask.repository;

import ua.epam.spring.hometask.domain.User;

import java.util.Set;

/**
 * Created by Igor on 12.05.2016.
 */
public interface UserDAO extends CrudRepository<User> {

    User find(String email);

    boolean userRegistered(User ticketUser);
}
