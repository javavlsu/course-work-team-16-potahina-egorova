package ru.vlsu.ispi;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.ispi.beans.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
