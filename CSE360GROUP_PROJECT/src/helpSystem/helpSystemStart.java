package helpSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class helpSystemStart extends Application {
    private Scene loginScene;  // To store the initial login scene
    
    public static void main(String[] args) {
        launch(args);
    }
    
    //checkAdmind
    Boolean adminMade = false;
    
    @Override
    public void start(Stage primaryStage) {
    	
    	
        primaryStage.setTitle("Help System");
        
        //Labels
        
        Label welcome = new Label("Welcome to our Help System");
        Label clickStart = new Label("Click the button to begin!");
        Label logo = new Label("?");
        
        //label designs
        
        welcome.setFont(new Font("Arial", 36));
        logo.setFont(new Font("Arial", 200));
        clickStart.setFont(new Font("Arial", 18));
        
        
        // Create the login button
        Button startButton = new Button();
        startButton.setText("Start");
        
        startButton.setStyle("-fx-font-size: 2em; ");
        
        // Set the action for when the login button is clicked
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Create the welcome scene
            	if (adminMade == false) {
            		
            		createAdmin(primaryStage);
            		
            	}
            	else {
            		
            		login(primaryStage);
            		
            	}
            }
        });
        
        //Pane for Center BorderPane
        VBox middlePane = new VBox(logo,startButton,clickStart);
        middlePane.setAlignment(Pos.CENTER);
        
        VBox.setMargin(startButton, new Insets(60, 0, 20, 0)); // Adds 20px margin at the top of the button

           
        //Pane For TOP of BorderPane
        
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER); // Centers the label in the HBox
        
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0)); // Adds 20px margin at the top of the button
        
        // Initial layout with the login button
        BorderPane starterScreen = new BorderPane();
        
        
        //Set location of elements
        
        starterScreen.setTop(topPane);
        starterScreen.setCenter(middlePane);
        
        //Background
        starterScreen.setStyle("-fx-background-color: lightblue;");
        
        
        
        
        // Store the initial scene
        loginScene = new Scene(starterScreen, 900, 600);
        
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
    }
    
    private void createAdmin(Stage primaryStage) {
    	
        // needed buttons,labels etc.
        Label welcome = new Label("Create an Admin Account");
        Label userName = new Label("Enter Username: ");
        Label password = new Label("Enter Password: ");
        Label confPassword = new Label("Re-enter Password: ");
        
        Label invUserName = new Label("Username must be between 6-12 charcters");
        Label invPassword = new Label("Password must be at least 6 charcters");
        Label invConPassword = new Label("Password does not match");
        
        Button loginButton = new Button();
        Button quitButton = new Button();
        
        TextField userNameText = new TextField();
        TextField passwordText = new TextField();
        TextField confPasswordText = new TextField();
        
        
        
        //label designs
        
        welcome.setFont(new Font("Arial", 36));
        userName.setFont(new Font("Arial", 20));
        password.setFont(new Font("Arial", 20));
        confPassword.setFont(new Font("Arial", 20));
        
        invUserName.setFont(new Font("Arial", 20));
        invUserName.setStyle("-fx-text-fill: red;");
        invUserName.setVisible(false);
        
        invPassword.setFont(new Font("Arial", 20));
        invPassword.setStyle("-fx-text-fill: red;");
        invPassword.setVisible(false);
        
        invConPassword.setFont(new Font("Arial", 20));
        invConPassword.setStyle("-fx-text-fill: red;");
        invConPassword.setVisible(false);

        // Create the return button
        loginButton.setText("Create Account");
        loginButton.setStyle("-fx-font-size: 2em; ");

        
        // Set the action for the return button to go back to the login scene
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
            		invUserName.setVisible(true);
            	}
            	else {
            		invUserName.setVisible(false);
            	}
            	
            	if (passwordText.getText().trim().length() < 6) {
            		invPassword.setVisible(true);
            	}
            	else {
            		invPassword.setVisible(false);
            	}
            	
            	if (passwordText.getText().trim().equals(confPasswordText.getText().trim()) == false) {
            		invConPassword.setVisible(true);
            	}
            	else {
            		invConPassword.setVisible(false);
            	}
            	
            	if (passwordText.getText().trim().length() > 6 && 
            			passwordText.getText().trim().equals(confPasswordText.getText().trim()) == true &&
            			userNameText.getText().trim().length() >= 6 && userNameText.getText().trim().length() <= 12) {
            		adminMade = true;
            		login(primaryStage);
            	}
            }
        });
        
        //quit button
        
        quitButton.setText("Quit");
        quitButton.setStyle("-fx-font-size: 1.5em; ");

        
        // Set the action for the return button to go back to the login scene
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
            public void handle(ActionEvent event) {
        		start(primaryStage);
        	}
        });
        
       //Pane for Center BorderPane
        
        //3 Vbox
        VBox middleleftPane = new VBox(userName,password,confPassword);
        VBox.setMargin(userName, new Insets(100, 20, 20, 40)); 
        VBox.setMargin(password, new Insets(20, 20, 20, 40)); 
        VBox.setMargin(confPassword, new Insets(20, 20, 20, 40)); 
        
        VBox middleMiddlePane = new VBox(userNameText,passwordText,confPasswordText);
        VBox.setMargin(userNameText, new Insets(100, 20, 20, 20)); 
        VBox.setMargin(passwordText, new Insets(20, 20, 20, 20)); 
        VBox.setMargin(confPasswordText, new Insets(20, 20, 20, 20)); 
        
        VBox middlerightPane = new VBox(invUserName,invPassword,invConPassword);
        VBox.setMargin(invUserName, new Insets(100, 20, 20, 20)); 
        VBox.setMargin(invPassword, new Insets(20, 20, 20, 20)); 
        VBox.setMargin(invConPassword, new Insets(20, 20, 20, 20)); 

        //Combine with HBox
        
        HBox middlePane = new HBox(middleleftPane,middleMiddlePane,middlerightPane);
        middlePane.setAlignment(Pos.CENTER_LEFT);
        
        //Bottom Pane
        
        HBox bottomPane = new HBox(loginButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(loginButton, new Insets(0, 0, 80, 0)); // Adds 20px margin at the top of the button
           
        //Pane For TOP of BorderPane
        
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER); // Centers the label in the HBox
        
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0)); // Adds 20px margin at the top of the button
        
        // Initial layout with the login button
        BorderPane admindCreateScreen = new BorderPane();
        
        //Pane for right
        
        VBox rightPane = new VBox(quitButton);
        rightPane.setAlignment(Pos.BASELINE_RIGHT);
        
        VBox.setMargin(quitButton, new Insets(320, 40, 40, 0)); // Adds
        
        
        //Set location of elements
        
        admindCreateScreen.setTop(topPane);
        admindCreateScreen.setCenter(middlePane);
        admindCreateScreen.setBottom(bottomPane);
        admindCreateScreen.setRight(rightPane);
        
        //Background
        admindCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Create the welcome scene and set it in the primary stage
        Scene welcomeScene = new Scene(admindCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    }
    
    private void login(Stage primaryStage){
    	
    }
}
