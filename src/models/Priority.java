package models;

import javafx.collections.ObservableList;

public class Priority{
	private String name;
    private static final String DEFAULT_PRIORITY = "Default";  // Name of the default priority

	
	public Priority(String name) {
		if (name == null || name.isEmpty()) {
	        throw new IllegalArgumentException("Priority name cannot be null or empty.");
	    }
	    
	    // Only throw an exception if the name is "Default" and you're trying to change it
	    if (name.equals("Default")) {
	        this.name = "Default"; // Ensure it's treated as "Default"
	    } else {
	        this.name = name;
	    }
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
	    return name; 
	}
	
	// setter for name
	public void setName(String name) {
		if ("Default".equals(this.name)) {
            throw new IllegalStateException("Cannot rename the default priority.");
        }
        this.name = name;
	}
	
	public boolean isDefault() {
        return this.name.equals(DEFAULT_PRIORITY);
    }
	
	// Static method to delete a priority and update tasks
	public static void deletePriority(String priorityName, ObservableList<Task> tasks) {
		for(Task task : tasks) {
			if(task.getPriority().getName().equals(priorityName)) {
				task.setPriority(new Priority("Default"));
			}
		}
		
	}
}