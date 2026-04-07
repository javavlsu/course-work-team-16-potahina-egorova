package ru.vlsu.ispi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vlsu.ispi.beans.TaskList;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Integer> {
}
