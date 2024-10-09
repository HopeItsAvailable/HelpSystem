package helpSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class helpSystemStart extends Application {
    private Scene loginScene;  // To store the initial login scene
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        System.out.println("This will be our first page");
        primaryStage.setTitle("The Start");
        
        // Create the login button
        Button loginButton = new Button();
        loginButton.setText("Display: 'Log in'");
        
        // Set the action for when the login button is clicked
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Create the welcome scene
                showWelcomeScene(primaryStage);
            }
        });
        
        // Initial layout with the login button
        StackPane loginLayout = new StackPane();
        loginLayout.getChildren().add(loginButton);
        
        // Store the initial scene
        loginScene = new Scene(loginLayout, 300, 250);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    
    private void showWelcomeScene(Stage primaryStage) {
        // Create a label that says "Welcome Admin"
        Label welcomeLabel = new Label("Welcome Admin");
        
        // Create the return button
        Button returnButton = new Button();
        returnButton.setText("Return to Log in");
        
        // Set the action for the return button to go back to the login scene
        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(loginScene);  // Go back to the original login scene
            }
        });
        
        // Create a new layout and add the label and the return button to it
        StackPane welcomeLayout = new StackPane();
        welcomeLayout.getChildren().addAll(welcomeLabel, returnButton);
        
        // Position the button below the label
        StackPane.setMargin(returnButton, new javafx.geometry.Insets(50, 0, 0, 0));  // Add margin to position the button lower
        
        // Create the welcome scene and set it in the primary stage
        Scene welcomeScene = new Scene(welcomeLayout, 300, 250);
        primaryStage.setScene(welcomeScene);
    }
    
    
    
}
