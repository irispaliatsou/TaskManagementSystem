package medialab;

import java.io.*; 
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.*;
import services.LocalDateAdapter;
import java.time.LocalDate;
import java.lang.reflect.Type;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class JsonHandler {

    private static final String TASKS_FILE = "medialab/tasks.json";
    private static final String CATEGORIES_FILE = "medialab/categories.json";
    private static final String PRIORITIES_FILE = "medialab/priorities.json";
    private static final String REMINDERS_FILE = "medialab/reminders.json";
    
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static Gson getGson() {
        return gson;
    }

    // Save tasks to JSON
    public static void saveTasks(ObservableList<Task> tasks) {
        try (Writer writer = new FileWriter(TASKS_FILE)) {
            gson.toJson(tasks, writer);
            System.out.println("Tasks saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving tasks to JSON: " + e.getMessage());
        }
    }

    // Load tasks from JSON
    public static ObservableList<Task> loadTasks() {
        File file = new File(TASKS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.err.println("Tasks file is empty or does not exist. Returning an empty task list.");
            return FXCollections.observableArrayList();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<Task>>() {}.getType();
            List<Task> taskList = gson.fromJson(reader, listType);

           
            if (taskList != null) {
                for (Task task : taskList) {
                    
                    if (task.getReminders() == null) {
                        task.setReminders(FXCollections.observableArrayList());
                    } else {
                        task.setReminders(FXCollections.observableArrayList(task.getReminders())); // Convert from ArrayList
                    }
                }
                System.out.println("Tasks loaded successfully!");
                return FXCollections.observableArrayList(taskList);
            } else {
                System.err.println("No tasks found in the file. Returning an empty task list.");
                return FXCollections.observableArrayList();
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks from JSON: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }





    // Save and load categories
    public static void saveCategories(ObservableList<Category> categories) {
        try (Writer writer = new FileWriter(CATEGORIES_FILE)) {
            gson.toJson(categories, writer);
        } catch (IOException e) {
            System.err.println("Error saving categories to JSON: " + e.getMessage());
        }
    }

    public static ObservableList<Category> loadCategories() {
    	try (Reader reader = new FileReader(CATEGORIES_FILE)) {
            
            Type listType = new TypeToken<List<Category>>() {}.getType();
            List<Category> categories = gson.fromJson(reader, listType);

            // Convert the List<Category> to an ObservableList<Category>
            if (categories != null) {
                System.out.println("Categories loaded successfully!");
                return FXCollections.observableArrayList(categories);
            } else {
                System.err.println("No categories found in the file. Returning an empty category list.");
                return FXCollections.observableArrayList();
            }
        } catch (IOException e) {
            System.err.println("Error loading categories from JSON: " + e.getMessage());
            return FXCollections.observableArrayList(); // Return an empty ObservableList
        }
    }

    // Save and load priorities
    public static void savePriorities(ObservableList<Priority> priorities) {
        try (Writer writer = new FileWriter(PRIORITIES_FILE)) {
            gson.toJson(priorities, writer);
        } catch (IOException e) {
            System.err.println("Error saving priorities to JSON: " + e.getMessage());
        }
    }

    public static ObservableList<Priority> loadPriorities() {
        try (Reader reader = new FileReader(PRIORITIES_FILE)) {
            Type listType = new TypeToken<List<Priority>>() {}.getType();
            List<Priority> priorities = gson.fromJson(reader, listType);

            return priorities != null ? FXCollections.observableArrayList(priorities) : FXCollections.observableArrayList();
        } catch (IOException e) {
            System.err.println("Error loading priorities from JSON: " + e.getMessage());
            return FXCollections.observableArrayList();
        }
    }

    public static void saveReminders(ObservableList<Reminder> reminders) {
        try (Writer writer = new FileWriter(REMINDERS_FILE)) {
            gson.toJson(reminders, writer);
            System.out.println("Reminders saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving reminders to JSON: " + e.getMessage());
        }
    }

	public static ObservableList<Reminder> loadReminders(String string) {
		try (Reader reader = new FileReader(REMINDERS_FILE)) {
	       
	        Type listType = new TypeToken<List<Reminder>>() {}.getType();
	        List<Reminder> reminders = gson.fromJson(reader, listType);

	        // Convert the List<Reminder> to an ObservableList<Reminder>
	        if (reminders != null) {
	            System.out.println("Reminders loaded successfully!");
	            return FXCollections.observableArrayList(reminders);
	        } else {
	            System.err.println("No reminders found in the file. Returning an empty reminder list.");
	            return FXCollections.observableArrayList();
	        }
	    } catch (IOException e) {
	        System.err.println("Error loading reminders from JSON: " + e.getMessage());
	        return FXCollections.observableArrayList(); // Return an empty ObservableList
	    }
	}
}
