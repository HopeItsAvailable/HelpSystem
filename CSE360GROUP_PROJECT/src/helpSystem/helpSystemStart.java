package helpSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    Boolean adminInfo = false;
    Boolean user = false;
    Boolean admin = false;
    Boolean teacher = false;
    Boolean multRoles = true;
    
    //Random String for code
    
    String code = "Test";
    
    //Save Admin details for now
    
    String adminUsername; 
    String adminPassword;
    
    
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
        
        VBox.setMargin(startButton, new Insets(50, 0, 20, 0)); // Adds 20px margin at the top of the button

           
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
    	
    	// Labels and buttons
        Label welcome = new Label("Create an Admin Account");
        Label userName = new Label("Enter Username: ");
        Label password = new Label("Enter Password: ");
        Label confPassword = new Label("Re-enter Password: ");
        
        Label invUserName = new Label("Username must be between 6-12 characters");
        Label invPassword = new Label("Password must be at least 6 characters");
        Label invConPassword = new Label("Password does not match");
        
        Button loginButton = new Button("Create Account");
        Button quitButton = new Button("Quit");
        
        TextField userNameText = new TextField();
        TextField passwordText = new TextField();
        TextField confPasswordText = new TextField();
        
        // Label design
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
        
        // Button design
        loginButton.setStyle("-fx-font-size: 2em;");
        quitButton.setStyle("-fx-font-size: 1.5em;");
        
        // Login button action
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                // Check username length
                if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
                    invUserName.setVisible(true);
                } else {
                    invUserName.setVisible(false);
                }
                
                // Check password length
                if (passwordText.getText().trim().length() < 6) {
                    invPassword.setVisible(true);
                } else {
                    invPassword.setVisible(false);
                }
                
                // Check if passwords match
                if (!passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
                    invConPassword.setVisible(true);
                } else {
                    invConPassword.setVisible(false);
                }
                
                // Proceed if all conditions are met
                if (passwordText.getText().trim().length() >= 6 && 
                    passwordText.getText().trim().equals(confPasswordText.getText().trim()) &&
                    userNameText.getText().trim().length() >= 6 && 
                    userNameText.getText().trim().length() <= 12) {
                    
                    adminMade = true;
                    adminUsername = userNameText.getText().trim();
                    adminPassword = passwordText.getText().trim();
                    login(primaryStage);
                }
            }
        });
        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Layout setup
        
        //Middle VBoxs
        VBox middleLeftPane = new VBox(userName, password, confPassword);
        VBox.setMargin(userName, new Insets(50, 20, 20, 40));
        VBox.setMargin(password, new Insets(20, 20, 20, 40));
        VBox.setMargin(confPassword, new Insets(20, 20, 20, 40));
        
        VBox middleMiddlePane = new VBox(userNameText, passwordText, confPasswordText);
        VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
        VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));
        VBox.setMargin(confPasswordText, new Insets(20, 20, 20, 20));
        
        VBox middleRightPane = new VBox(invUserName, invPassword, invConPassword);
        VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
        VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));
        VBox.setMargin(invConPassword, new Insets(20, 20, 20, 20));
        
        // Combine the middle VBoxs
        HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
        middlePane.setAlignment(Pos.CENTER_LEFT);
        
        // Bottom pane for login button
        HBox bottomPane = new HBox(loginButton,quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(loginButton, new Insets(0, 220, 80, 280));
        HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));
        
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        
        // BorderPane layout
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    }
    

    private void login(Stage primaryStage){
    	
    	// Labels and buttons
        Label welcome = new Label("Log In");
        Label newUser = new Label("Click here to make new account");
        Label userName = new Label("Enter Username: ");
        Label password = new Label("Enter Password: ");
        Label confPassword = new Label("Re-enter Password: ");
        
        Label invUserName = new Label("Username does not exist");
        Label invPassword = new Label("Incorrect Password");
        Label invConPassword = new Label("Password does not match");
        Label checkLogin = new Label("Account not found try again");
        
        Button loginButton = new Button("Log-In");
        Button quitButton = new Button("Quit");
        Button createAccountButton = new Button("Create Account");
        
        TextField userNameText = new TextField();
        TextField passwordText = new TextField();
        TextField confPasswordText = new TextField();
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        userName.setFont(new Font("Arial", 20));
        password.setFont(new Font("Arial", 20));
        confPassword.setFont(new Font("Arial", 20));
        newUser.setFont(new Font("Arial", 14));
                
        invUserName.setFont(new Font("Arial", 20));
        invUserName.setStyle("-fx-text-fill: red;");
        invUserName.setVisible(false);
        
        invPassword.setFont(new Font("Arial", 20));
        invPassword.setStyle("-fx-text-fill: red;");
        invPassword.setVisible(false);
        
        checkLogin.setFont(new Font("Arial", 14));
        checkLogin.setStyle("-fx-text-fill: red;");
        checkLogin.setVisible(false);
        
        invConPassword.setFont(new Font("Arial", 20));
        invConPassword.setStyle("-fx-text-fill: red;");
        invConPassword.setVisible(false);
        
        // Button design
        loginButton.setStyle("-fx-font-size: 2em;");
        quitButton.setStyle("-fx-font-size: 1.5em;");
        createAccountButton.setStyle("-fx-font-size: 1.5em;");
        
        // Login button action
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	// Check username length
                if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
                    invUserName.setVisible(true);
                } else {
                    invUserName.setVisible(false);
                }
                
                // Check password length
                if (passwordText.getText().trim().length() < 6) {
                    invPassword.setVisible(true);
                } else {
                    invPassword.setVisible(false);
                }
                
                // Check if passwords match
                if (!passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
                    invConPassword.setVisible(true);
                } else {
                    invConPassword.setVisible(false);
                }
                
                
                // Proceed if all conditions are met
                if (passwordText.getText().trim().length() >= 6 && 
                		passwordText.getText().trim().equals(confPasswordText.getText().trim()) &&
                		userNameText.getText().trim().length() >= 6 && 
                		userNameText.getText().trim().length() <= 12){
                    
                			if(userNameText.getText().trim().equals(adminUsername) &&
                					passwordText.getText().trim().equals(adminPassword)&&
                					confPasswordText.getText().trim().equals(adminPassword)) {
                				
                     					if(adminInfo == false) {
                     						addAccountInfo(primaryStage);
                     					}
                				
                     					else {
                     						if(multRoles == true) {
                     							chooseRole(primaryStage);
                     						}
                     						else {
                     							if (user == true) {
                     								userPage(primaryStage);
                     							}
                     							else if(admin == true){
                     								adminPage(primaryStage);

                     							}
                     							else if(teacher == true){
                     								teacherPage(primaryStage);
                     							}
                     						}
                     					}
                				
                     		}
                			
                			else {
                					checkLogin.setVisible(true);
                			}
                	}
                
            	}
        	});
        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	start(primaryStage);
            }
        });
        
        // Create Account button action
        createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createUser(primaryStage);
            }
        });
        
        // Layout setup
        
        //Middle VBoxs
        VBox middleLeftPane = new VBox(userName, password, confPassword);
        VBox.setMargin(userName, new Insets(50, 20, 20, 40));
        VBox.setMargin(password, new Insets(20, 20, 20, 40));
        VBox.setMargin(confPassword, new Insets(20, 20, 20, 40));
        
        VBox middleMiddlePane = new VBox(userNameText, passwordText, confPasswordText);
        VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
        VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));
        VBox.setMargin(confPasswordText, new Insets(20, 20, 20, 20));
        
        VBox middleRightPane = new VBox(invUserName, invPassword,invConPassword);
        VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
        VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));
        VBox.setMargin(invConPassword, new Insets(20, 20, 20, 20));
        
        // Combine the middle VBoxs
        HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
        middlePane.setAlignment(Pos.CENTER_LEFT);
        
        // Bottom pane for login button
        HBox bottomPane = new HBox(createAccountButton,loginButton,quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        
        HBox.setMargin(createAccountButton, new Insets(0, 100, 0, 30));
        HBox.setMargin(loginButton, new Insets(0, 150, 0, 100));
        HBox.setMargin(quitButton, new Insets(0, 80, 0, 70));
        
        HBox textBottom = new HBox(newUser,checkLogin);
        HBox.setMargin(newUser, new Insets(10, 50, 50, 30));
        HBox.setMargin(checkLogin, new Insets(10, 50, 50, 95));

        
        VBox bottomText = new VBox(bottomPane,textBottom);
                
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        // BorderPane layout
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomText);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
        
    }
    
    private void createUser(Stage primaryStage) {
    	
    	// Labels and buttons
        Label welcome = new Label("Create a User");
        Label userName = new Label("Enter Username: ");
        Label password = new Label("Enter Password: ");
        Label confPassword = new Label("Re-enter Password: ");
        Label userCode = new Label("Enter User Code: ");

        
        Label invUserName = new Label("Username must be between 6-12 characters");
        Label invPassword = new Label("Password must be at least 6 characters");
        Label invConPassword = new Label("Password does not match");
        Label invUserCode = new Label("Usercode incorrect");
        Label invChooseType = new Label("Please choose");

        
        Button loginButton = new Button("Create Account");
        Button quitButton = new Button("Quit");
        
        TextField userNameText = new TextField();
        TextField passwordText = new TextField();
        TextField confPasswordText = new TextField();
        TextField userCodeText = new TextField();
        

        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        userName.setFont(new Font("Arial", 20));
        password.setFont(new Font("Arial", 20));
        confPassword.setFont(new Font("Arial", 20));
        userCode.setFont(new Font("Arial", 20));
        
        invUserName.setFont(new Font("Arial", 20));
        invUserName.setStyle("-fx-text-fill: red;");
        invUserName.setVisible(false);
        
        invPassword.setFont(new Font("Arial", 20));
        invPassword.setStyle("-fx-text-fill: red;");
        invPassword.setVisible(false);
        
        invConPassword.setFont(new Font("Arial", 20));
        invConPassword.setStyle("-fx-text-fill: red;");
        invConPassword.setVisible(false);
        
        invUserCode.setFont(new Font("Arial", 20));
        invUserCode.setStyle("-fx-text-fill: red;");
        invUserCode.setVisible(false);
        
        // Button design
        loginButton.setStyle("-fx-font-size: 2em;");
        quitButton.setStyle("-fx-font-size: 1.5em;");
        
        // Login button action
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                // Check username length
                if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
                    invUserName.setVisible(true);
                } else {
                    invUserName.setVisible(false);
                }
                
                // Check password length
                if (passwordText.getText().trim().length() < 6) {
                    invPassword.setVisible(true);
                } else {
                    invPassword.setVisible(false);
                }
                
                // Check if passwords match
                if (!passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
                    invConPassword.setVisible(true);
                } else {
                    invConPassword.setVisible(false);
                }
                
                if (!userCodeText.getText().trim().equals(code)) {
                	invUserCode.setVisible(true);
                } else {
                	invUserCode.setVisible(false);
                }
                
                // Proceed if all conditions are met
                if (passwordText.getText().trim().length() >= 6 && 
                    passwordText.getText().trim().equals(confPasswordText.getText().trim()) &&
                    userNameText.getText().trim().length() >= 6 && 
                    userNameText.getText().trim().length() <= 12 &&
                	userCodeText.getText().trim().equals(code)){
                    
                    //ADD USER TO LIST
                    login(primaryStage);
                }
            }
        });
        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Layout setup
        
        //Middle VBoxs
        VBox middleLeftPane = new VBox(userName, password, confPassword,userCode);
        VBox.setMargin(userName, new Insets(50, 20, 20, 40));
        VBox.setMargin(password, new Insets(20, 20, 20, 40));
        VBox.setMargin(confPassword, new Insets(20, 20, 20, 40));
        VBox.setMargin(userCode, new Insets(20, 20, 20, 40));
        
        VBox middleMiddlePane = new VBox(userNameText, passwordText, confPasswordText,userCodeText);
        VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
        VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));
        VBox.setMargin(confPasswordText, new Insets(15, 20, 20, 20));
        VBox.setMargin(userCodeText, new Insets(15, 20, 20, 20));
        
        VBox middleRightPane = new VBox(invUserName, invPassword, invConPassword, invUserCode);
        VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
        VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));
        VBox.setMargin(invConPassword, new Insets(20, 20, 20, 20));
        VBox.setMargin(invUserCode, new Insets(20, 20, 20, 20));
        
        // Combine the middle VBoxs
        HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
        middlePane.setAlignment(Pos.CENTER_LEFT);
        
        // Bottom pane for login button
        HBox bottomPane = new HBox(loginButton,quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(loginButton, new Insets(0, 220, 80, 280));
        HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));
        
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        
        // BorderPane layout
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    }
    
    private void addAccountInfo(Stage primaryStage) {
    	
    	// Labels and buttons
        Label welcome = new Label("Finish Account Setup");
        Label email = new Label("Email Address: ");
        Label firstName = new Label("First Name: ");
        Label confFirstName = new Label("Perferred First Name: ");
        Label middleName = new Label("Middle Name: ");
        Label lastName = new Label("Last Name: ");
        
        TextField emailText = new TextField();
        TextField firstNameText = new TextField();
        TextField confFirstNameText = new TextField();
        TextField middleNameText = new TextField();
        TextField lastNameText = new TextField();
        
        Button conButton = new Button("Confirm");
        Button quitButton = new Button("Quit");
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        email.setFont(new Font("Arial", 20));
        firstName.setFont(new Font("Arial", 20));
        confFirstName.setFont(new Font("Arial", 20));
        middleName.setFont(new Font("Arial", 20));
        lastName.setFont(new Font("Arial", 20));
        
        // Button design
        conButton.setStyle("-fx-font-size: 2em;");
        quitButton.setStyle("-fx-font-size: 1.5em;");
        
        // Con button action
        conButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(emailText.getText().isEmpty()) {
                	emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(firstNameText.getText().isEmpty()) {
                	firstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	firstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(confFirstNameText.getText().isEmpty()) {
                	confFirstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	confFirstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(middleNameText.getText().isEmpty()) {
                	middleNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	middleNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(lastNameText.getText().isEmpty()) {
                	lastNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	lastNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(!emailText.getText().isEmpty() &&
                		!firstNameText.getText().isEmpty() &&
                		!confFirstNameText.getText().isEmpty() &&
                		!middleNameText.getText().isEmpty() &&
                		!lastNameText.getText().isEmpty()) {
                	if(multRoles == true) {
							chooseRole(primaryStage);
						}
						else {
							if (user == true) {
								userPage(primaryStage);
							}
							else if(admin == true){
								adminPage(primaryStage);

							}
							else if(teacher == true){
								teacherPage(primaryStage);
							}
						}
                }
            }
        });
        

        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Layout setup
        
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        
       //Middle VBoxs
        VBox middleOne = new VBox(firstName, middleName, email);
        VBox.setMargin(firstName, new Insets(50, 20, 20, 40));
        VBox.setMargin(middleName, new Insets(20, 20, 20, 40));
        VBox.setMargin(email, new Insets(20, 20, 20, 40));
        
        VBox middleTwo = new VBox(firstNameText,middleNameText,emailText);
        VBox.setMargin(firstNameText, new Insets(50, 20, 20, 40));
        VBox.setMargin(middleNameText, new Insets(20, 20, 20, 40));
        VBox.setMargin(emailText, new Insets(20, 20, 20, 40));
        
        VBox middleThree = new VBox(confFirstName, lastName);
        VBox.setMargin(confFirstName, new Insets(50, 20, 20, 40));
        VBox.setMargin(lastName, new Insets(20, 20, 20, 40));
        
        VBox middleFour = new VBox(confFirstNameText, lastNameText);
        VBox.setMargin(confFirstNameText, new Insets(50, 20, 20, 40));
        VBox.setMargin(lastNameText, new Insets(20, 20, 20, 40));
        
        //Combine VBoxs
        HBox middlePane = new HBox(middleOne,middleTwo,middleThree,middleFour);
        
        // Bottom pane for login button
        HBox bottomPane = new HBox(conButton,quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(conButton, new Insets(0, 220, 80, 280));
        HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));
        
        // BorderPane layout
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
        
    }
    
    private void chooseRole(Stage primaryStage) {
    	
    	// Labels and buttons
        Label welcome = new Label("Finish Account Setup");

        
        Button adminButton = new Button("Admin");
        Button studentButton = new Button("Student");
        Button teacherButton = new Button("Teacher");
        Button quitButton = new Button("Quit");
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        
        // Button Design
        adminButton.setStyle(
            "-fx-background-radius: 50%; " +    
            "-fx-min-width: 170px; " +          
            "-fx-min-height: 170px; " +         
            "-fx-max-width: 170px; " +          
            "-fx-max-height: 170px; " +
            "-fx-font-size: 2em;"
        );
        
        // Button Design
        studentButton.setStyle(
            "-fx-background-radius: 50%; " +    
            "-fx-min-width: 170px; " +          
            "-fx-min-height: 170px; " +         
            "-fx-max-width: 170px; " +          
            "-fx-max-height: 170px; " +
            "-fx-font-size: 2em;"
        );
        
        // Button Design
        teacherButton.setStyle(
            "-fx-background-radius: 50%; " +    
            "-fx-min-width: 170px; " +          
            "-fx-min-height: 170px; " +         
            "-fx-max-width: 170px; " +          
            "-fx-max-height: 170px; " +
            "-fx-font-size: 2em;"
        );
        
        quitButton.setStyle("-fx-font-size: 2em;");
        
        // Quit button action
        adminButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
        
        // Quit button action
        studentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        
        // Quit button action
        teacherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });

        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        // Middle Pane
        HBox middlePane = new HBox(studentButton,teacherButton,adminButton);
        HBox.setMargin(studentButton, new Insets(80, 80, 0, 130));
        HBox.setMargin(teacherButton, new Insets(80, 80, 0, 0));
        HBox.setMargin(adminButton, new Insets(80, 80, 0, 0));
        
        //Bottom pane for exit
        HBox bottomPane = new HBox(quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(quitButton, new Insets(0, 0, 70, 0));
        
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    	
    }

    private void userPage(Stage primaryStage) {
	
    }
    
    private void teacherPage(Stage primaryStage) {
    	
    }
    
    private void adminPage(Stage primaryStage) {
    	
    }

}
