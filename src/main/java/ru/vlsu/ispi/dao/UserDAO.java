package ru.vlsu.ispi.dao;

import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface UserDAO {
    void save(User user);
    void update(User user);
    void delete(User user);
    User findById(Integer id);
    List<User> findAll();
}