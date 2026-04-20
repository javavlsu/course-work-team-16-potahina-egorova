package ru.vlsu.ispi.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.vlsu.ispi.beans.TaskList;
import ru.vlsu.ispi.services.TaskListService;

import java.util.Optional;

@Component
public class TaskListIdConverter implements Converter<Integer, TaskList> {

    private final TaskListService taskListService;

    @Autowired
    public TaskListIdConverter(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @Override
    public TaskList convert(Integer taskListId) {
        if (taskListId == null) {
            return null;
        }
        Optional<TaskList> taskListOptional = taskListService.getTaskListById(taskListId);
        return taskListOptional.orElse(null);
    }
}
