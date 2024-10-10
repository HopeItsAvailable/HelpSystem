package helpSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.*; 


public class helpSystemStart extends Application {
    private Scene loginScene;  // To store the initial login scene
    private LinkedList linkedList;  // Declare LinkedList
    oneTimePasswordGeneratorList oneTimePasswordGeneratorList ;

    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    //checkAdmind
    
	final public int TOTALNUMBEROFROLES = 3;
    
    //Random String for code
    
    String code = "Test";
    
    //Save Admin details for now
    
    String adminUsername; 
    String adminPassword;
    
    
    @Override
    public void start(Stage primaryStage) {
    	linkedList = new LinkedList();
    	oneTimePasswordGeneratorList = new oneTimePasswordGeneratorList(); //holds the roles for when an invite is created
    	
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
            	if (!linkedList.checkIfAdmin()) { //if there is no admin, make one
            		
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
                    
                    adminUsername = userNameText.getText().trim();
                    adminPassword = passwordText.getText().trim();
                    
                    linkedList.add(adminUsername, adminPassword);
                    linkedList.addRoleToUser(adminUsername, "admin");
                    
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
                	
                			Node userNode = linkedList.searchByUsername(userNameText.getText().trim());
                			
                			String checkUserPassword = userNode.getPassword();
                    
                			if(passwordText.getText().trim().equals(checkUserPassword)&&
                					confPasswordText.getText().trim().equals(checkUserPassword)) {
                				
                				int roleCount = userNode.getNumOfRoles();
                				
                				String emailCheck = userNode.getEmail();
                				
                     					if(emailCheck == null) {
                     						addAccountInfo(primaryStage, userNameText.getText().trim());
                     					}
                				
                     					else {
                     						if(roleCount == 2 || roleCount == 3) {
                     							chooseRole(primaryStage,userNameText.getText().trim());
                     						}
                     						else {
                     							if (userNode.getIsStudent() == true) {
                     								studentPage(primaryStage);
                     							}
                     							else if(userNode.getIsAdmin() == true){
                     								adminPage(primaryStage);

                     							}
                     							else if(userNode.getIsInstructor() == true){
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
    
    private void addAccountInfo(Stage primaryStage, String username) {
    	
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
                	
                	//This uses the set methods to finalize the details of the user that is inputting these
                	//details. At this point, user nodes only have date in their username and password fields.
                	
                	System.out.println(username + "HERE");
                	linkedList.finalizeUserEmail(username, emailText.getText());
                	linkedList.finalizeUserFirstName(username, firstNameText.getText());
                	linkedList.finalizeUserLastName(username, lastNameText.getText());
                	linkedList.finalizeUserMiddleName(username, middleNameText.getText());
                	linkedList.finalizeUserPreferredFirstName(username, confFirstNameText.getText());
                	
                	if(linkedList.getNumOfRoles(username)>1) {
							chooseRole(primaryStage, username);
						}
						else {
							if (linkedList.isStudent(username) == true) {
								studentPage(primaryStage);
							}
							else if(linkedList.isAdmin(username) == true){
								adminPage(primaryStage);

							}
							else if(linkedList.isInstructor(username) == true){
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
    
    private void chooseRole(Stage primaryStage, String UserName) {
    	
    	boolean checkIfStudent = linkedList.isStudent(UserName);
    	boolean checkIfTeacher = linkedList.isStudent(UserName);
    	boolean checkIfAdmin = linkedList.isStudent(UserName);
    	
    	// Labels and buttons
        Label welcome = new Label("Finish Account Setup");

        
        Button adminButton = new Button("Admin");
        Button studentButton = new Button("Student");
        Button teacherButton = new Button("Teacher");
        Button quitButton = new Button("Quit");
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        
        // Button Design
        if(checkIfAdmin == true) {
        	adminButton.setStyle(
                    "-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;" 
                );
        }
        else {
        	adminButton.setStyle(
                    "-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;" +
                    "-fx-opacity: 0.6;"
                );
        }
        
        if(checkIfStudent == true) {
        	studentButton.setStyle(
        			"-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;"
                );
        }
        
        else {
        	studentButton.setStyle(
                    "-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;" +
                    "-fx-opacity: 0.6;"
                );
        }
        
        if(checkIfTeacher == true) {
        	teacherButton.setStyle(
                    "-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;"
                );
        }
        
        else {
        	teacherButton.setStyle(
                    "-fx-background-radius: 50%; " +    
                    "-fx-min-width: 170px; " +          
                    "-fx-min-height: 170px; " +         
                    "-fx-max-width: 170px; " +          
                    "-fx-max-height: 170px; " +
                    "-fx-font-size: 2em;" +
                    "-fx-opacity: 0.6;"
                );
        }
        
        quitButton.setStyle("-fx-font-size: 2em;");
        
        // Admin button action
        adminButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(checkIfAdmin == true) {
            		studentPage(primaryStage);
            	}
            	
            }
        });
        
        // Student button action
        studentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(checkIfStudent == true) {
            		studentPage(primaryStage);
            	}
            }
        });

        
        // Teacher button action
        teacherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	if(checkIfTeacher == true) {
            		studentPage(primaryStage);
            	}            }
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

    private void studentPage(Stage primaryStage) {
    	
    	// Labels and buttons
        Label welcome = new Label("Welcome");
        Label role = new Label("Role: Student");
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        role.setFont(new Font("Arial", 20));
        
        Button quitButton = new Button("Log Out");
        
        //Button design
        quitButton.setStyle("-fx-font-size: 1.5em;");
        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Top pane for welcome label
        HBox topPane = new HBox(role,welcome);
        HBox.setMargin(role, new Insets(20, 0, 0, 20));
        HBox.setMargin(welcome, new Insets(50, 0, 20, 130));
        
        //Bottom pane for exit
        HBox bottomPane = new HBox(quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(quitButton, new Insets(0, 0, 70, 0));
        
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
	
    }
    
    private void teacherPage(Stage primaryStage) {
    	
    	// Labels and buttons
        Label welcome = new Label("Welcome");
        Label role = new Label("Role: Teacher");
        
        Button quitButton = new Button("Log Out");
        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        role.setFont(new Font("Arial", 20));
        
        //Button design
        quitButton.setStyle("-fx-font-size: 1.5em;");
        
        // Quit button action
        quitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                start(primaryStage);
            }
        });
        
        // Top pane for welcome label
        HBox topPane = new HBox(role,welcome);
        HBox.setMargin(role, new Insets(20, 0, 0, 20));
        HBox.setMargin(welcome, new Insets(50, 0, 20, 130));
        
        //Bottom pane for exit
        HBox bottomPane = new HBox(quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(quitButton, new Insets(0, 0, 70, 0));
        
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    	
    }
    
    private void adminPage(Stage primaryStage) {
        Label welcome = new Label("Welcome Admin");
        
        Button inviteUserButton = new Button("Invite User");
        Button resetUserButton = new Button("Reset User Account");
        Button deleteUserButton = new Button("Delete User Account");
        Button listUsersButton = new Button("List User Accounts");
        Button addRoleButton = new Button("Add/Remove Role");
        
        Button logoutButton = new Button("Log Out");
        
        //Label Design
        welcome.setFont(new Font("Arial", 36));
        
        //Button Design
        inviteUserButton.setStyle("-fx-font-size: 1.8em;");
        resetUserButton.setStyle("-fx-font-size: 1.8em;");
        deleteUserButton.setStyle("-fx-font-size: 1.8em;");
        
        listUsersButton.setStyle("-fx-font-size: 1.8em;");
        addRoleButton.setStyle("-fx-font-size: 1.8em;");
        logoutButton.setStyle("-fx-font-size: 1.8em;");

        // Set button actions using EventHandler
        inviteUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	//
            	sendCode(primaryStage);
            }
        });

        resetUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            	resetUser(primaryStage);
            	
            }
        });

        deleteUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	deleteUser(primaryStage);
            	
            }
        });

        listUsersButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
            	listUsers(primaryStage);
            }
        });

        addRoleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO : ADD NEW PAGE
            	
            	changeRoles(primaryStage);
            	
            }
        });

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                login(primaryStage); // Log out and go back to login
            }
        });

        
        //Top Pane
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        //Middle Pane
        HBox middleTopPane = new HBox(inviteUserButton,resetUserButton,deleteUserButton);
        HBox.setMargin(inviteUserButton, new Insets(50, 50, 80, 120));
        HBox.setMargin(resetUserButton, new Insets(50, 50, 80, 0));
        HBox.setMargin(deleteUserButton, new Insets(50, 0, 80, 0));
        
        HBox middleBottomPane = new HBox(listUsersButton,addRoleButton,logoutButton);
        HBox.setMargin(listUsersButton, new Insets(50, 50, 80, 200));
        HBox.setMargin(addRoleButton, new Insets(50, 50, 0, 0));
        HBox.setMargin(logoutButton, new Insets(50, 0, 0, 0));

        VBox middlePane = new VBox(middleTopPane,middleBottomPane);
        
        //Bottom pane for exit
        HBox bottomPane = new HBox(logoutButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(logoutButton, new Insets(0, 0, 70, 0));

        // Create the BorderPane and set the VBox as the center
        BorderPane adminCreateScreen = new BorderPane();
        
        adminCreateScreen.setTop(topPane); 
        adminCreateScreen.setCenter(middlePane); 
        adminCreateScreen.setBottom(bottomPane); 
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");

        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
    }
    
    
    private void sendCode(Stage primaryStage) {
    	// Labels, buttons, textfield, alert, and checkBox
        Label welcome = new Label("Send Code");
        Label email = new Label("Email Address: ");
        Label noClick = new Label("Please choose which type of account");
        
        TextField emailText = new TextField();
        
        Button sendButton = new Button("Send");
        Button quitButton = new Button("Quit");
        
        CheckBox studentAccount = new CheckBox("Student");
        CheckBox teacherAccount = new CheckBox("Teacher");
        CheckBox adminAccount = new CheckBox("Admin");

        
        // Label design
        welcome.setFont(new Font("Arial", 36));
        email.setFont(new Font("Arial", 20));
        
        noClick.setFont(new Font("Arial", 20));
        noClick.setStyle("-fx-text-fill: red;");
        noClick.setVisible(false);
        
        //CheckBox design
        studentAccount.setFont(new Font("Arial", 20));
        teacherAccount.setFont(new Font("Arial", 20));
        adminAccount.setFont(new Font("Arial", 20));
        
        //Button design
        quitButton.setStyle("-fx-font-size: 1.5em;");
        sendButton.setStyle("-fx-font-size: 2em;");

        // Send button action
        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(emailText.getText().isEmpty()) {
                	emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                }
                else {
                	emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
                }
                
                if(!studentAccount.isSelected() && !teacherAccount.isSelected()&& !adminAccount.isSelected()) {
                	noClick.setVisible(true);
                }
                else {
                	noClick.setVisible(false);
                	
                	String[] roles = new String[TOTALNUMBEROFROLES]; //size 3 currently
                	
                    if (studentAccount.isSelected()) {
                        roles[0] = "Student";
                    }
                    if (teacherAccount.isSelected()) {
                    	roles[1] = "Teacher";
                    }
                    if (adminAccount.isSelected()) {
                    	roles[2] = "Admin";

                    }

                    // Generate the one-time code and expiration time
                    String code = generateOneTimeCode();
                    long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Valid for 24 hours from now

					// Add the invitation to the linked list
                    oneTimePasswordGeneratorList.addPassword(code, roles, expirationTime);
                   
                    if(!emailText.getText().isEmpty()) {
                	Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Send Email");
                    alert.setHeaderText("Email Sent");  // Optional: No header text
                    alert.setContentText("Email has been sent with code: " + code);
                    
                    alert.showAndWait();
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
        
        // Top pane for welcome label
        HBox topPane = new HBox(welcome);
        topPane.setAlignment(Pos.CENTER);
        HBox.setMargin(welcome, new Insets(50, 0, 20, 0));
        
        // Middle Pane
        HBox middleTopPane = new HBox(email,emailText);
        HBox.setMargin(email, new Insets(80, 80, 0, 130));
        HBox.setMargin(emailText, new Insets(80, 80, 0, 0));
        
        HBox middleBottomPane = new HBox(studentAccount,teacherAccount,adminAccount,noClick);
        HBox.setMargin(studentAccount, new Insets(80, 40, 0, 60));
        HBox.setMargin(teacherAccount, new Insets(80, 40, 0, 0));
        HBox.setMargin(adminAccount, new Insets(80, 40, 0, 0));
        HBox.setMargin(noClick, new Insets(80, 0, 0, 0));
        
        VBox middlePane = new VBox(middleTopPane,middleBottomPane);
        
        // Bottom pane for login button
        HBox bottomPane = new HBox(sendButton,quitButton);
        bottomPane.setAlignment(Pos.CENTER);
        HBox.setMargin(sendButton, new Insets(0, 220, 80, 280));
        HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));
        
        
        //BorderPane stuff
        BorderPane adminCreateScreen = new BorderPane();
        adminCreateScreen.setTop(topPane);
        adminCreateScreen.setCenter(middlePane);
        adminCreateScreen.setBottom(bottomPane);
        adminCreateScreen.setStyle("-fx-background-color: lightblue;");
        
        // Set the scene
        Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
        primaryStage.setScene(welcomeScene);
        
        
    }
    
    public String generateOneTimeCode() {
    	// Length of the code
        int length = 10; //MAY CHANGE LATER
        
        // all characters, will combine later
        String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String smallChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/.?<>)";

        // combine
        String values = capitalChars + smallChars + numbers + symbols;

        // Using Random to generate the code
        Random rndmMethod = new Random();
        StringBuilder oneTimeCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Generate a random character from the combined string
            oneTimeCode.append(values.charAt(rndmMethod.nextInt(values.length())));
        }

        return oneTimeCode.toString(); // Return the generated code as a string
    }
    
    public void resetUser(Stage primaryStage) {
    	
    }
    
    public void deleteUser(Stage primaryStage) {
    	
    }

    public void listUsers(Stage primaryStage) {
    	
    }

  	public void changeRoles(Stage primaryStage) {
  	
  }

}
