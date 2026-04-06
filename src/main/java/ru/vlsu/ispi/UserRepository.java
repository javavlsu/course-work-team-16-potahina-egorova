package ru.vlsu.ispi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
