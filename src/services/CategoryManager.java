package services;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import medialab.JsonHandler;
import models.Category;
import models.Task;
import models.Reminder;

public class CategoryManager {
	private ObservableList<Category> categories;
	//private ObservableList<Task> tasks;
	//private ObservableList<Reminder> reminders;
	
	public CategoryManager() {
		this.categories = FXCollections.observableArrayList();
		//this.tasks = FXCollections.observableArrayList();
		//this.reminders = FXCollections.observableArrayList();
		
		loadCategories("medialab/categories.json");
	}
	
	// add a new category
	public void addCategory(String name) {
		categories.add(new Category(name));
		JsonHandler.saveCategories(categories);
	}
	
	// edit an existing category name and associated tasks
	public void editCategory(Category category, String newName) {
		String oldName = category.getCategory();
		category.setCategory(newName);
		updateTasksCategory(oldName, newName);
        saveCategories();
        JsonHandler.saveCategories(categories); 
	}
	
	// delete category, associated tasks, update reminders
	public void deleteCategory(Category category) {
		
		categories.remove(category);
        JsonHandler.saveCategories(categories);
	}
	
	
	// Update all tasks with the old category name to the new name
    public void updateTasksCategory(String oldName, String newName) {
        
    	for (Category category : categories) {
            if (category.getCategory().equals(oldName)) {
                category.setCategory(newName);  // Assuming setCategory is available in the Category class
                break;
            }
        }

    	JsonHandler.saveCategories(categories);
//        JsonHandler.saveTasks(tasks);
    }
	
    

    
	//Get all categories
	public ObservableList<Category> getCategories(){
		if (categories == null) { 
	        categories = FXCollections.observableArrayList();
	    }
		return categories;
	}

	public void loadCategories(String string) {
		ObservableList<Category> loadedCategories = JsonHandler.loadCategories();
        if (loadedCategories != null) {
            categories = loadedCategories;
        } else {
            categories = FXCollections.observableArrayList();
        }
		
	}

	public void saveCategories() {
		JsonHandler.saveCategories(categories);
		
	}
	
}