package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import services.CategoryManager;
import services.PriorityManager;
import services.TaskManager;
import services.TaskSearcher;
import services.ReminderManager;
import models.*;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file for the main UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainCont.fxml"));
            Parent root = loader.load(); // Load the root object (which is the root layout of your FXML)

            // Get the controller for the FXML
            MainController controller = loader.getController();

            // Initialize managers (services)
            TaskManager taskManager = new TaskManager();
            TaskSearcher taskSearcher = new TaskSearcher();
            CategoryManager categoryManager = new CategoryManager();
            PriorityManager priorityManager = new PriorityManager();
            ReminderManager reminderManager = new ReminderManager();

            // Set the managers into the controller
            controller.setManagers(taskManager, taskSearcher, categoryManager, priorityManager, reminderManager);

            // Set up the scene and the primary stage
            Scene scene = new Scene(root, 600, 800);
            primaryStage.setTitle("Task Manager");
            primaryStage.setScene(scene);

            // Save app state on exit
            primaryStage.setOnCloseRequest(event -> controller.saveAppState());

            // Show the stage
            primaryStage.show();
        } catch (IOException e) {
            // Handle error if FXML loading fails
            e.printStackTrace();
            
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
