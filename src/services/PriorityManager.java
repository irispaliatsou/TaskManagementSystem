package services;

import java.util.ArrayList; 
import java.util.List;

import medialab.JsonHandler;
import models.Priority;
import models.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriorityManager{
	private ObservableList<Priority> priorities;
	
	public PriorityManager() {
		this.priorities = FXCollections.observableArrayList();
        this.priorities.add(new Priority("Default"));
	}
	
	// add a new priority
	public void addPriority(Priority priority) {
		if ("Default".equals(priority.getName())) {
            throw new IllegalArgumentException("Cannot add a priority with the name 'Default'");
        }
		priorities.add(priority);
		savePriorities();
	}
	
	// edit an existing priority name (except for default)
	public void editPriority(Priority priority, String newName) {
		if (priority.isDefault()) {
            throw new IllegalStateException("Cannot edit the default priority.");
        }
        priority.setName(newName);
	}
	
	// delete a priority and set all tasks with this priority to default
	public void deletePriority(Priority priority) {
		if (!"Default".equals(priority.getName())) {
            priorities.remove(priority);
        }
	}
	
	// get all priorities
	public ObservableList<Priority> getPriorities(){
		return priorities;
	}

	public Priority getDefaultPriority() {
        for (Priority p : priorities) {
            if (p.isDefault()) {
                return p;
            }
        }
        return null; // Default should always exist
    }
	
	public void savePriorities() {
		JsonHandler.savePriorities(priorities);
		
	}

	public void loadPriorities(String string) {
		ObservableList<Priority> loadedPriorities = JsonHandler.loadPriorities();
        if (loadedPriorities != null) {
            priorities = loadedPriorities;
        } else {
            priorities.add(new Priority("DEFAULT"));
        }
		
	}
}