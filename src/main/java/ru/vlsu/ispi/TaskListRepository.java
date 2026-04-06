package ru.vlsu.ispi;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vlsu.ispi.beans.Task;
import ru.vlsu.ispi.beans.TaskList;

public interface TaskListRepository extends JpaRepository<TaskList, Integer> {
}
