package ru.vlsu.ispi;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.User;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
