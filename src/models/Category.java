package models;

import javafx.collections.ObservableList;

public class Category{
	private String name;
	
	@Override
	public String toString() {
	    return name;  
	}
	
	public Category(String name) {
		this.name = name;
	}
	
	public void setCategory(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return name;
	}
	
	public static void deleteCategory(Category category, ObservableList<Task> tasks, ObservableList<Reminder> reminders) {
		// Remove tasks associated with the category
		tasks.removeIf(task -> {
			if(task.getCategory().equals(category)) {
				//remove associated reminders for deleted tasks
				Reminder.deleteRemindersForTask(task, reminders);
				return true;
			}
			return false;
		});
		// remove associated reminders here
	}
	//
}