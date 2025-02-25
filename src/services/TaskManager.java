package services;

import java.util.ArrayList;  
import java.util.List;
import java.time.LocalDate;
import medialab.JsonHandler;
import models.Task;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class TaskManager{
	private static TaskManager instance;
	private ObservableList<Task> tasks;
	
	public TaskManager() {
		tasks = JsonHandler.loadTasks();
		if(tasks == null) {
			tasks = FXCollections.observableArrayList();
		} 
		
	}
	
	public static TaskManager getInstance() {
	        if (instance == null) {
	            instance = new TaskManager();
	        }
	        return instance;
	}
	
	public void addTask(Task task) {
		if (task.getState() == null || task.getState().isEmpty()) {
	        task.setState("Open");
	    }
		tasks.add(task);
		JsonHandler.saveTasks(tasks);
		System.out.println("Task added and saved: " + task.getName());
	}
	
	public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks();

	}
	
	
	public void setTasks(ObservableList<Task> newTasks) {
	    if (newTasks != null) {
	        this.tasks = newTasks;
	        JsonHandler.saveTasks(tasks); 
	    } else {
	        this.tasks = FXCollections.observableArrayList(); 
	    }
	}
	
	// Get a task by name
    public Task getTask(String taskName) {
        for (Task task : tasks) {
            if (task.getName().equalsIgnoreCase(taskName)) {
                return task;
            }
        }
        return null;
    }
	
	public ObservableList<Task> getAllTasks(){
		return tasks;
	}
	
	// Save tasks to JSON
    public void saveTasks() {
        JsonHandler.saveTasks(tasks);
    }

    // Load tasks from JSON
    public void loadTasks() {
        ObservableList<Task> loadedTasks = JsonHandler.loadTasks();
        if (loadedTasks != null) {
            tasks = loadedTasks;
        } else {
            tasks = FXCollections.observableArrayList();
        }
    }

    
    public boolean updateTask(String taskName, Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().equalsIgnoreCase(taskName)) {
                tasks.set(i, updatedTask);
                return true; // Task updated successfully
            }
        }
        return false; // Task not found
    }
    
    public ObservableList<Task> getDelayedTasks() {
        ObservableList<Task> delayedTasks = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        
        for (Task task : tasks) {
            if (task.getDeadline() != null && task.getDeadline().isBefore(today) && !"Completed".equals(task.getState())) {
                delayedTasks.add(task);
            }
        }
        return delayedTasks;
    }

    public int getDelayedTasksCount() {
        int count = 0;
        LocalDate today = LocalDate.now();
        
        for (Task task : tasks) {
            if (task.getDeadline() != null && task.getDeadline().isBefore(today) && !"Completed".equals(task.getState())) {
                count++;
            }
        }
        return count;
    }

    public int getCompletedTasksCount() {
        int count = 0;
        for (Task task : tasks) {
            if ("Completed".equals(task.getState())) {
                count++;
            }
        }
        return count;
    }

    
    public int getUpcomingTasksCount() {
    	LocalDate today = LocalDate.now();
        LocalDate sevenDaysLater = today.plusDays(7);

        return (int) tasks.stream()
                .filter(task -> task.getDeadline() != null) // Ensure the task has a deadline
                .filter(task -> !task.getDeadline().isBefore(today) && task.getDeadline().isBefore(sevenDaysLater))
                .count();
    }
    
}