package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    User findByEmail(String email);
    User findByPhoneNumber(String phoneNumber);
}
