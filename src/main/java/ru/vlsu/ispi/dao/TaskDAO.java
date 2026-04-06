package ru.vlsu.ispi.dao;

import ru.vlsu.ispi.beans.Task;

public interface TaskDAO {
	void save(Task task);
	void update(Task task);
	void delete(Task task);
	Task findById(Integer id);
}