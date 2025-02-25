package services;

import java.util.stream.Collectors; 

import models.Category;
import models.Priority;
import models.Task;

import java.util.List;

public class TaskSearcher{
	public List<Task> searchTasks(List<Task> tasks, String name, Category category, Priority priority){
		return tasks.stream()
				.filter(task -> (name == null || task.getName().toLowerCase().contains(name.toLowerCase())))
				.filter(task -> (category == null || task.getCategory().equals(category)))
				.filter(task -> (priority == null || task.getPriority() == priority))
				.collect(Collectors.toList());
	}
}
