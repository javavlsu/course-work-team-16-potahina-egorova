package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.beans.User;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Integer> {
    List<TaskList> findByUser(User user);
}
