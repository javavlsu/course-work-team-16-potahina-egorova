package ru.vlsu.ispi.dao;

import ru.vlsu.ispi.beans.TaskList;

public interface TaskListDAO {
	void save(TaskList taskList);
	void update(TaskList taskList);
	void delete(TaskList taskList);
	TaskList findById(Integer id);
}