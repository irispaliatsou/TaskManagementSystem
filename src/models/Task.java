package models;

import java.time.LocalDate; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Task{
	private String name;
	private String description;
	private Category category;
	private Priority priority;
	private LocalDate deadline;
	private String state;
	private ObservableList<Reminder> reminders= FXCollections.observableArrayList();
	
	
	
	// Constructor
	public Task(String name, String description, Category category, Priority priority, LocalDate deadline, String state){
		this.name = name;
		this.description = description;
		this.category = category;
		this.priority = priority;
		this.deadline = deadline;
		this.state = (state == null || state.isEmpty()) ? "Open" : state; 
		this.reminders = FXCollections.observableArrayList();
	
	}
	
	@Override
	public String toString() {
	    return name; 
	}
	
	// Update the state on startup
	public void updateStateOnStartup() {
		if (!state.equals("Completed") && deadline.isBefore(LocalDate.now())) {
			this.state = "Delayed";
		}
		
	}
	
	// add reminder to task
	public void addReminder(Reminder reminder) {
		if (this.state.equals("Completed")) {
			throw new IllegalStateException("Cannot set reminders for a completed task!");
		}
		if (reminder.getReminderDate().isAfter(this.deadline)) {
			throw new IllegalArgumentException("Reminder date cannot be after task deadline!");
		}
		this.reminders.add(reminder);
	}
	
	// delete all reminders associated with this task
	public void deleteAllReminders() {
		this.reminders.clear();
	}
	

	// Set task state and automatically delete reminders if completed
	public void setState(String state) {
		this.state = state;
		if (state.equals("Completed")) {
			deleteAllReminders(); //automatic deletion of reminders
		}
	}
	
	// Update category name for tasks
	public void updateCategoryName(String oldCategoryName, String newCategoryName) {
	    if (this.category.getCategory().equals(oldCategoryName)) {
	        this.category.setCategory(newCategoryName);
	    }
	}

	
	//setters and getters
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getCategory() {
		return category;
	}
	
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	
	
	public String getState() {
		return state;
	}
	
	public 
	enum TaskState{
		OPEN, IN_PROGRESS, POSTPONED, COMPLETED, DELAYED;
	} //not used now
	
	public ObservableList<Reminder> getReminders(){
		return reminders;
	}
	
	public void setReminders(ObservableList<Reminder> reminder ) {
		this.reminders = FXCollections.observableArrayList(reminders);
	}
}