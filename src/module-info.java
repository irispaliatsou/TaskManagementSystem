module TaskManagementSystem {
	requires javafx.controls;
	requires javafx.fxml;
	requires transitive javafx.graphics; 
	requires java.base;
	requires transitive com.google.gson;
	requires javafx.base;
	//idk why it sometimes needs transitive
	opens models to com.google.gson;
	exports application;
	exports models;
	exports services;
	
	//opens models to com.google.gson;
	opens application to javafx.graphics, javafx.controls, javafx.fxml;
}
