package application;

import models.*; 

import services.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

import medialab.JsonHandler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


import java.time.LocalDate;



public class MainController implements Initializable{
	private TaskManager taskManager;
	private TaskSearcher taskSearcher;
    private CategoryManager categoryManager;
    private PriorityManager priorityManager;
    private ReminderManager reminderManager;
    
    private ObservableList<Category> categories;
    private ObservableList<Task> tasks;

    @FXML private VBox root;
    @FXML private VBox topSection, bottomSection;
    @FXML private Label totalTasksLabel, completedTasksLabel, delayedTasksLabel, dueIn7DaysLabel;
    @FXML private TextField taskNameField, searchTitleField, newCategoryField, newPriorityField, editPriorityField;
    @FXML private TextArea taskDescriptionField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField categoryNameTextField;
    @FXML private Button editCategoryButton;
    //@FXML private Button editPriorityButton;
    @FXML private ComboBox<Priority> priorityComboBox;
    @FXML private DatePicker deadlineDatePicker, reminderDatePicker, searchDeadlineDatePicker;
    @FXML private ComboBox<String> stateComboBox;
    @FXML private Button addCategoryButton, deleteCategoryButton, addReminder, addPriorityButton;
    @FXML private ComboBox<Reminder> reminderComboBox;
    @FXML private Button searchButton;
    @FXML private ListView<Task> taskListView;
    @FXML private TableView<Task> taskTable, searchResultsTable;
    @FXML private TableColumn<Task, String> taskTitleColumn, taskCategoryColumn, taskPriorityColumn, taskDeadlineColumn, taskStateColumn;
    @FXML private ListView<Category> categoryListView;
    @FXML private ListView<Priority> priorityListView;
    @FXML private ListView<Reminder> reminderListView;
    @FXML private ComboBox<String> searchCategoryComboBox, searchPriorityComboBox;
    
    
    @FXML
    private TableColumn<Task, String> searchTitleColumn;
    @FXML
    private TableColumn<Task, String> searchCategoryColumn;
    @FXML
    private TableColumn<Task, String> searchPriorityColumn;
    @FXML
    private TableColumn<Task, LocalDate> searchDeadlineColumn;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.taskManager = new TaskManager();
        this.categoryManager = new CategoryManager();
        this.priorityManager = new PriorityManager();

        // Ensure categories are loaded from storage
        categories = FXCollections.observableArrayList(categoryManager.getCategories());
        categoryListView.setItems(categories); 
        categoryComboBox.setItems(categories); 
        priorityComboBox.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
        priorityListView.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));

        stateComboBox.getItems().setAll("Open", "In Progress", "Postponed", "Completed", "Delayed");

        // Set the default state (if needed)
        stateComboBox.setValue("Open");
        
        tasks = FXCollections.observableArrayList(taskManager.getAllTasks());  // Ensure this is initialized
        taskListView.setItems(tasks);
        
        
        
        searchTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        searchCategoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory().getCategory()));
        searchPriorityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPriority().getName()));
        searchDeadlineColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadline()));

        
        populateSearchFilters();

        loadPriorities();
        updateTaskSummary();

        Platform.runLater(() -> {
            loadAppState();
            updateTaskListView();
            updateTaskSummary();
            showDelayedTasksPopup();
        });
    }

   
   private void updateTaskListView() {
   	if (taskListView != null) { // Check for null before using it
           taskListView.setItems(FXCollections.observableArrayList(taskManager.getAllTasks()));
       } else {
           System.err.println("taskListView is null! Check FXML binding.");
       }    }
   

   
   private void loadCategories() {
	   categoryComboBox.getItems().clear();
	   categoryListView.getItems().clear();
	   
       ObservableList<Category> categories = categoryManager.getCategories(); 
       if (categories != null) {
    	   
           categoryComboBox.setItems(categories);
           categoryListView.setItems(categories);
       } else {
           System.err.println("No categories loaded.");
       }
   }

   private void loadPriorities() {
       priorityComboBox.getItems().clear();
       priorityListView.getItems().clear();

       ObservableList<Priority> priorities = priorityManager.getPriorities();
       if (priorities != null) {
           priorityComboBox.setItems(priorities);
           priorityListView.setItems(priorities);
       }
   }
	
   private void updateTaskSummary() {
	    totalTasksLabel.setText(String.valueOf(taskManager.getAllTasks().size()));
	    completedTasksLabel.setText(String.valueOf(taskManager.getCompletedTasksCount()));
	    delayedTasksLabel.setText(String.valueOf(taskManager.getDelayedTasksCount()));
	    dueIn7DaysLabel.setText(String.valueOf(taskManager.getUpcomingTasksCount()));
	}

    public void setManagers(TaskManager taskManager, TaskSearcher taskSearcher, CategoryManager categoryManager, PriorityManager priorityManager, ReminderManager reminderManager) {
        this.taskManager = taskManager;
        this.taskSearcher = taskSearcher;
        this.categoryManager = categoryManager;
        this.priorityManager = priorityManager;
        this.reminderManager = reminderManager;
    }
    
    public void saveAppState() {
        try {
            taskManager.saveTasks();
            categoryManager.saveCategories();
            priorityManager.savePriorities();
            reminderManager.saveReminders();
            updateTaskSummary();
        } catch (Exception e) {
        	Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving application state: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();        }
    }

    private void loadAppState() {
        try {
            taskManager.loadTasks();
            categoryManager.loadCategories("medialab/categories.json");
            priorityManager.loadPriorities("medialab/priorities.json");
            reminderManager.loadReminders("medialab/reminders.json");
            updateTaskSummary();
        } catch (Exception e) {
            showError("Error loading application state: " + e.getMessage());
        }
    }


    @FXML
    private void handleAddTask() {
        String name = taskNameField.getText();
        String description = taskDescriptionField.getText();
        Category category = categoryComboBox.getValue();
        Priority priority = priorityComboBox.getValue();
        LocalDate deadline = deadlineDatePicker.getValue();
        String state  = (stateComboBox.getValue() != null) ? stateComboBox.getValue() : "Open";

        if (name.isEmpty() || description.isEmpty() || category == null || deadline == null ) {
            showError("Please fill out all fields.");
            return;
        }

        if (priority == null) {
            priority = priorityManager.getPriorities().stream()
                .filter(p -> p.getName().equalsIgnoreCase("DEFAULT"))
                .findFirst()
                .orElse(new Priority("DEFAULT"));
        }

        Task newTask = new Task(name, description, category, priority, deadline, state);
        taskManager.addTask(newTask);
        updateTaskListView();
        updateTaskSummary();
        clearTaskFields();
    }

    private void loadTasks() {
        ObservableList<Task> loadedTasks = JsonHandler.loadTasks(); 
        if (loadedTasks != null) {
            taskManager.setTasks(loadedTasks); 
            tasks.setAll(taskManager.getAllTasks());
        }
    }
    
    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask == null) {
            showError("Please select a task to delete.");
        } else {
            taskManager.removeTask(selectedTask);
            updateTaskListView();
            updateTaskSummary();
        }
    }

    

    
    private void showDelayedTasksPopup() {
        int delayedCount = taskManager.getDelayedTasksCount();
        if (delayedCount > 0) {
            showInfo("You have " + delayedCount + " delayed tasks.");
        }
    }

 
    @FXML
    private void handleSearchTask() {
        String name = searchTitleField.getText().toLowerCase().trim();
        String category = searchCategoryComboBox.getValue();
        String priority = searchPriorityComboBox.getValue();
        LocalDate deadline = searchDeadlineDatePicker.getValue();

        ObservableList<Task> allTasks = taskManager.getAllTasks();

        // Filter tasks based on criteria
        ObservableList<Task> filteredTasks = allTasks.filtered(task -> {
            boolean matchesTitle = name.isEmpty() || task.getName().toLowerCase().contains(name);
            boolean matchesCategory = (category == null || category.isEmpty()) || task.getCategory().getCategory().equals(category);
            boolean matchesPriority = (priority == null || priority.isEmpty()) || task.getPriority().getName().equals(priority);
            boolean matchesDeadline = (deadline == null) || task.getDeadline().equals(deadline);

            return matchesTitle && matchesCategory && matchesPriority && matchesDeadline;
        });

        // Update table
        searchResultsTable.setItems(filteredTasks);
    }
    
    private void populateSearchFilters() {
        // Populate categories
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        for (Category category : categoryManager.getCategories()) {
            categoryNames.add(category.getCategory());
        }
        searchCategoryComboBox.setItems(categoryNames);

        // Populate priorities
        ObservableList<String> priorityNames = FXCollections.observableArrayList();
        for (Priority priority : priorityManager.getPriorities()) {
            priorityNames.add(priority.getName());
        }
        searchPriorityComboBox.setItems(priorityNames);
    }

    @FXML
    private void handleResetSearch() {
        searchTitleField.clear();
        searchCategoryComboBox.setValue(null);
        searchPriorityComboBox.setValue(null);
        searchDeadlineDatePicker.setValue(null);

        // Reload all tasks
        searchResultsTable.setItems(FXCollections.observableArrayList(TaskManager.getInstance().getAllTasks()));
    }


    @FXML
    private void handleAddCategory() {
        String categoryName = newCategoryField.getText();
        if (categoryName.isEmpty()) {
            showError("Category name cannot be empty.");
            return;
        } else {
            categoryManager.addCategory(categoryName);
            categoryComboBox.getItems().setAll(categoryManager.getCategories());
            categoryListView.setItems(categoryManager.getCategories());
            
        }
    }
    
    

    @FXML
    private void handleEditCategory() {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
        
        if (selectedCategory == null) {
            showError("Error, Please select a category to edit.");
            return;
        }
        
        String newName = categoryNameTextField.getText().trim();

        if (newName.isEmpty()) {
            showError("Error, Category name cannot be empty.");
            return;
        }
        
        selectedCategory.setCategory(newName);

        // Update all tasks using this category
        for (Task task : taskManager.getAllTasks()) {
            if (task.getCategory().equals(selectedCategory)) {
                task.getCategory().setCategory(newName); // Update category name in the task
            }
        }

        
            //selectedCategory.setCategory(newName);       
        	categoryManager.updateTasksCategory(selectedCategory.getCategory(), newName);

        	categories.setAll(categoryManager.getCategories());
            categoryComboBox.setItems(categories);
            categoryListView.setItems(categories);
            categoryListView.refresh();
            
            taskListView.setItems(FXCollections.observableArrayList(taskManager.getAllTasks()));
            taskListView.refresh();
            JsonHandler.saveCategories(categories);
            JsonHandler.saveTasks(taskManager.getAllTasks());
            
    }
    
    
    
    @FXML
    private void handleDeleteCategory() {
        Category selectedCategory = categoryComboBox.getValue();
        if (selectedCategory != null) {
            categoryManager.deleteCategory(selectedCategory);
            categoryComboBox.getItems().setAll(categoryManager.getCategories());
        } else {
            showError("Please select a category to delete.");
        }
    }

    
    @FXML
    private void handleAddReminder() {
        Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        LocalDate reminderDate = reminderDatePicker.getValue();
        if (selectedTask == null) {
            showError("Please select a task to add a reminder.");
        } else if (reminderDate == null) {
            showError("Please select a reminder date.");
        } else {
            reminderManager.addReminder(selectedTask, "CUSTOM_DATE", reminderDate);
            showInfo("Reminder added successfully.");
            reminderDatePicker.setValue(null);
        }
    }

    @FXML
    private void handleAddPriority() {
        String priorityName = newPriorityField.getText();
        if (priorityName.isEmpty()) {
            showError("Priority name cannot be empty.");
        } else {
            Priority newPriority = new Priority(priorityName);
            priorityManager.addPriority(newPriority);
            priorityComboBox.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
            priorityListView.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
        
        }
    }
    
    @FXML
    private void handleEditPriority() {
        Priority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();
        if (selectedPriority == null) {
            showError("Please select a priority to edit.");
            return;
        }

        String newPriorityName = editPriorityField.getText();
        if (newPriorityName.isEmpty()) {
            showError("Priority name cannot be empty.");
        } else {
            try {
                
                selectedPriority.setName(newPriorityName);
                priorityManager.editPriority(selectedPriority, newPriorityName);
                priorityComboBox.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
                priorityListView.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
            
            } catch (IllegalStateException e) {
                showError(e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeletePriority() {
        Priority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();
        if (selectedPriority == null) {
            showError("Please select a priority to delete.");
            return;
        }

        // Check if it's the default priority
        if (selectedPriority.isDefault()) {
            showError("Cannot delete the default priority.");
            return;
        }

        // Delete the priority
        try {
            priorityManager.deletePriority(selectedPriority);
            priorityComboBox.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
            priorityListView.setItems(FXCollections.observableArrayList(priorityManager.getPriorities()));
            } catch (IllegalStateException e) {
            showError(e.getMessage());
        }
    }


    @FXML
    private void handleListAllTasks() {
        updateTaskListView();
    }

    
    private void clearTaskFields() {
        taskNameField.clear();
        taskDescriptionField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        priorityComboBox.getSelectionModel().clearSelection();
        deadlineDatePicker.setValue(null);
        stateComboBox.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}