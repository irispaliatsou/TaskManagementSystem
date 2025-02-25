package services;

import java.time.LocalDate; 
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import models.Reminder;
import models.Task;
import medialab.JsonHandler;

public class ReminderManager {
	private ObservableList<Reminder> reminders;

    public ReminderManager() {
    	reminders = FXCollections.observableArrayList();
    }

    // Add a reminder
    public void addReminder(Task task, String type, LocalDate customDate) {
        if (task.getState().equals("COMPLETED")) {
            System.out.println("Cannot add reminders for completed tasks.");
            return;
        }

        LocalDate reminderDate;
        switch (type) {
            case "ONE_DAY_BEFORE":
                reminderDate = task.getDeadline().minusDays(1);
                break;
            case "ONE_WEEK_BEFORE":
                reminderDate = task.getDeadline().minusWeeks(1);
                break;
            case "ONE_MONTH_BEFORE":
                reminderDate = task.getDeadline().minusMonths(1);
                break;
            case "CUSTOM_DATE":
                reminderDate = customDate;
                break;
            default:
                System.out.println("Invalid reminder type.");
                return;
        }

        if (reminderDate.isAfter(task.getDeadline())) {
            System.out.println("Reminder date cannot be after the task deadline.");
            return;
        }

        reminders.add(new Reminder(reminderDate, task));
    }

    // Edit a reminder
    public void editReminder(Reminder reminder, String newType, LocalDate newDate) {
        addReminder(reminder.getTask(), newType, newDate);
        reminders.remove(reminder); // Remove the old reminder
    }

    // Delete a reminder
    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
    }

    // Delete all reminders for a task
//    public void deleteRemindersForTask(Task task) {
//        reminders.removeIf(reminder -> reminder.getTask().equals(task));
//    }

    // Get all reminders
    public ObservableList<Reminder> getReminders() {
        return reminders;
    }

	public void saveReminders() {
		//List<Reminder> reminderList = new ArrayList<>(reminders);
		JsonHandler.saveReminders(reminders);
		
	}

	public void loadReminders(String filepath) {
        ObservableList<Reminder> loadedReminders = JsonHandler.loadReminders("medialab/reminders.json");
        if (loadedReminders != null) {
            reminders = loadedReminders;
        } else {
            reminders = FXCollections.observableArrayList();
        }
		
	}
}
