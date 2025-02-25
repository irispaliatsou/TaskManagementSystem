package models;

import java.time.LocalDate; 


import javafx.collections.ObservableList;

public class Reminder{
	private LocalDate reminderDate;
	private Task task;
	private String reminderType; 
	
	public Reminder() {
    }
	// Constructor
	public Reminder(LocalDate reminderDate, Task task) {
		if(reminderDate.isAfter(task.getDeadline())) {
			throw new IllegalArgumentException("Reminder cannot be set after the task deadline!");
		}
		this.reminderDate = reminderDate;
		this.task = task;
		
	}
	
	//getters and setters
	public LocalDate getReminderDate() {
		return reminderDate;
	}
	
	public void setReminderDate(LocalDate reminderDate) {
		if(reminderDate.isAfter(task.getDeadline())) {
			throw new IllegalArgumentException("Reminder cannot be set after the task deadline!");
		}
		this.reminderDate = reminderDate;
	}
	
	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
        this.task = task;
    }
	
	// Static method to delete reminders for a specific task
	public static void deleteRemindersForTask(Task task, ObservableList<Reminder> reminders) {
		reminders.removeIf(reminder -> reminder.getTask().equals(task));
	}
	
    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }
}