package ru.vlsu.ispi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.core.convert.converter.Converter;
import ru.vlsu.ispi.services.UserService;

import java.util.Optional;

@Component
public class UserIdConverter implements Converter<Integer, User> {

    private final UserService userService;

    @Autowired
    public UserIdConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User convert(Integer userId) {
        if (userId == null) {
            return null;
        }

        Optional<User> userOptional = userService.getUserById(userId);
        return userOptional.orElse(null);
    }
}
