package helpSystem;

import javafx.application.Application;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.*;
import javafx.scene.image.Image;
import java.sql.SQLException;
import java.util.Scanner;

import helpSystem.DatabaseHelperUser;
import helpSystem.DatabaseHelperArticle;


public class helpSystemStart extends Application {
	
	private static DatabaseHelperUser databaseHelper;
	private static DatabaseHelperArticle databaseHelper1;
	
	private Scene loginScene; // To store the initial login scene
	//private LinkedList linkedList; // Declare LinkedList
	oneTimePasswordGeneratorList oneTimePasswordGeneratorList;
	int size = 0;
	
	// checkAdmind

	final public int TOTALNUMBEROFROLES = 3;

	// Save Admin details for now


	public static void main(String[] args) {
		
		
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Initialize the linked list if empty
	    if (size == 0) {
	        //linkedList = new LinkedList();
	        oneTimePasswordGeneratorList = new oneTimePasswordGeneratorList(); // Holds the roles for when an invite is created
	        size++;
	    }
	    
	    databaseHelper = new DatabaseHelperUser();
		databaseHelper1 = new DatabaseHelperArticle();
		
		
	    try {
	    	databaseHelper.connectToDatabase();
			databaseHelper1.connectToDatabase();
			
			primaryStage.setTitle("Help System");

			// Label
			Label welcome = new Label("Welcome to our Help System");
			welcome.setFont(new Font("Montserrat", 45));
			
			//Line for header
			Line line = new Line(0, 0, 600, 0);
		    line.setId("lineDetails");
		    
		    //Logo
		    ImageView logoImageView = new ImageView();
		    logoImageView.setImage(new Image(getClass().getResourceAsStream("img/logo.png")));
		    logoImageView.setFitWidth(200);
		    logoImageView.setFitHeight(200);

			// Create the login button
			Button startButton = new Button("Start");
			startButton.setId("buttonStart");

			// Set the action for when the login button is clicked
			startButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Create the welcome scene
					try {
						if (databaseHelper.isDatabaseEmpty()) { // if there is no admin, make one

							createAdmin(primaryStage);

						} else {

							login(primaryStage);

						}
					} catch (SQLException e) {

						System.err.println("Database error: " + e.getMessage());
						e.printStackTrace();

					}
				}
			});
			
			// Top Pane (VBox for the welcome label and line)
		    VBox topPane = new VBox();
		    topPane.getChildren().addAll(welcome, line);
		    topPane.setAlignment(Pos.CENTER);
		    VBox.setMargin(welcome, new Insets(50, 0, 5, 0));

			// Center Pane (HBox for the logo)
		    HBox middlePane = new HBox();
		    middlePane.getChildren().addAll(logoImageView);
		    middlePane.setAlignment(Pos.CENTER);

		    // Bottom Pane (HBox for the Start button)
		    HBox bottomPane = new HBox(startButton);
		    bottomPane.setAlignment(Pos.CENTER);
		    HBox.setMargin(startButton, new Insets(0, 0, 80, 0));

		    // Main layout with BorderPane
		    BorderPane starterScreen = new BorderPane();
		    starterScreen.setId("startBackground");
		    starterScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		    // Set the location of elements in the BorderPane
		    starterScreen.setTop(topPane);
		    starterScreen.setCenter(middlePane);
		    starterScreen.setBottom(bottomPane);

		    // Store the initial scene
		    Scene loginScene = new Scene(starterScreen, 900, 600);

		    // Set the scene and show the stage
		    primaryStage.setScene(loginScene);
		    primaryStage.show();
	    }
	    
	    catch (SQLException e) {

			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}
	    
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

				// Proceed if all conditions are met
				if (passwordText.getText().trim().length() >= 6						
						&& userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12) {

					String username = userNameText.getText().trim();
					String password = passwordText.getText().trim();
					try {
						databaseHelper.registerFirstUser(username, password);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					login(primaryStage);
					
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// Layout setup

		// Middle VBoxs
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
		HBox bottomPane = new HBox(loginButton, quitButton);
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

	private void login(Stage primaryStage) {

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
		Button forgotPassword = new Button("Forgot Password");

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
		forgotPassword.setStyle("-fx-font-size: 1em;");
		
		// Create Account button action
		forgotPassword.setOnAction(new EventHandler<ActionEvent>() {
			@Override
				public void handle(ActionEvent event) {
					forgotPassword(primaryStage);
				}
			});

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

				// Proceed if all conditions are met
				if (passwordText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12){
					
					boolean check = databaseHelper.doesUserExist(userNameText.getText().strip());
					boolean [] roles = new boolean[3];
					
					if(check) {
						checkLogin.setVisible(false);
						
						System.out.println("FOUND UOU");
						
						String username = userNameText.getText().strip();
					    try {
							roles = databaseHelper.getUserRoles(username);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    
					    
					    if (roles != null) {
					        int roleCount = 0;
					        String roleToRedirect = "";

					        if (roles[0]) { roleCount++; roleToRedirect = "Admin"; }
					        if (roles[1]) { roleCount++; roleToRedirect = "Student"; }
					        if (roles[2]) { roleCount++; roleToRedirect = "Instructor"; }

					        if (roleCount == 1) {
					            // Redirect to the role page directly if only one role exists
					            if (roleToRedirect.equals("Admin")) {
					                // Redirect to admin page
					                adminPage(primaryStage);
					            } else if (roleToRedirect.equals("Student")) {
					                // Redirect to student page
					            	studentPage(primaryStage);
					            } else if (roleToRedirect.equals("Instructor")) {
					                // Redirect to instructor page
					                teacherPage(primaryStage);
					            }
					        } else if (roleCount > 1) {
					            // Redirect to a page where user can choose their role
					            chooseRole(primaryStage, username);
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
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password, confPassword,forgotPassword);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));
		VBox.setMargin(confPassword, new Insets(20, 20, 20, 40));
		VBox.setMargin(forgotPassword, new Insets(20, 20, 20, 40));

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
		HBox bottomPane = new HBox(createAccountButton, loginButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);

		HBox.setMargin(createAccountButton, new Insets(0, 100, 0, 30));
		HBox.setMargin(loginButton, new Insets(0, 150, 0, 100));
		HBox.setMargin(quitButton, new Insets(0, 80, 0, 70));

		HBox textBottom = new HBox(newUser, checkLogin);
		HBox.setMargin(newUser, new Insets(10, 50, 50, 30));
		HBox.setMargin(checkLogin, new Insets(10, 50, 50, 95));

		VBox bottomText = new VBox(bottomPane, textBottom);

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
				
				
				// Proceed if all conditions are met
				System.out.println("here1");
				if (passwordText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() >= 6 && userNameText.getText().trim().length() <= 12)
						{

					// ADD USER TO LIST

					String username = userNameText.getText().trim();
					String password = passwordText.getText().trim();
				    String userCode = userCodeText.getText().strip(); // OTP entered by the user
				    
				    String[] roles = oneTimePasswordGeneratorList.getRolesFromOTP(userCode);

				    if (roles == null || roles.length == 0) {
				        System.out.println("Invalid or expired invite code");
				        return; // Exit if the OTP is invalid
				    }

				    try {
				        // Register user with email and password
				        databaseHelper.registerWithInviteCode(username, password, roles);
				    } catch (Exception e) {
				        e.printStackTrace();
				    }

					login(primaryStage);

				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password, confPassword, userCode);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));
		VBox.setMargin(confPassword, new Insets(20, 20, 20, 40));
		VBox.setMargin(userCode, new Insets(20, 20, 20, 40));

		VBox middleMiddlePane = new VBox(userNameText, passwordText, confPasswordText, userCodeText);
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
		HBox bottomPane = new HBox(loginButton, quitButton);
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
				if (emailText.getText().isEmpty()) {
					emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (firstNameText.getText().isEmpty()) {
					firstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					firstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (confFirstNameText.getText().isEmpty()) {
					confFirstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					confFirstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (middleNameText.getText().isEmpty()) {
					middleNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					middleNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (lastNameText.getText().isEmpty()) {
					lastNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					lastNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (!emailText.getText().isEmpty() && !firstNameText.getText().isEmpty()
						&& !confFirstNameText.getText().isEmpty() && !middleNameText.getText().isEmpty()
						&& !lastNameText.getText().isEmpty()) {

					// This uses the set methods to finalize the details of the user that is
					// inputting these
					// details. At this point, user nodes only have date in their username and
					// password fields.

					System.out.println(username + "HERE");
					
					// ADD account info to user and then go to right page
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle VBoxs
		VBox middleOne = new VBox(firstName, middleName, email);
		VBox.setMargin(firstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleName, new Insets(20, 20, 20, 40));
		VBox.setMargin(email, new Insets(20, 20, 20, 40));

		VBox middleTwo = new VBox(firstNameText, middleNameText, emailText);
		VBox.setMargin(firstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(emailText, new Insets(20, 20, 20, 40));

		VBox middleThree = new VBox(confFirstName, lastName);
		VBox.setMargin(confFirstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastName, new Insets(20, 20, 20, 40));

		VBox middleFour = new VBox(confFirstNameText, lastNameText);
		VBox.setMargin(confFirstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastNameText, new Insets(20, 20, 20, 40));

		// Combine VBoxs
		HBox middlePane = new HBox(middleOne, middleTwo, middleThree, middleFour);

		// Bottom pane for login button
		HBox bottomPane = new HBox(conButton, quitButton);
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

		//WORK ON THIS CHECK WHICH ROLES USER IS
		
		boolean isStudent = true;
		boolean isTeacher = true;
		boolean isAdmin = true;

		// Labels and buttons
		Label welcome = new Label("Finish Account Setup");

		Button adminButton = new Button("Admin");
		Button studentButton = new Button("Student");
		Button teacherButton = new Button("Teacher");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));

		// Button Design
		if (isAdmin) {
			adminButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;");
		} else {
			adminButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;"
					+ "-fx-opacity: 0.6;");
		}

		if (isStudent) {
			studentButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;");
		}

		else {
			studentButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;"
					+ "-fx-opacity: 0.6;");
		}

		if (isTeacher) {
			teacherButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;");
		}

		else {
			teacherButton.setStyle("-fx-background-radius: 50%; " + "-fx-min-width: 170px; " + "-fx-min-height: 170px; "
					+ "-fx-max-width: 170px; " + "-fx-max-height: 170px; " + "-fx-font-size: 2em;"
					+ "-fx-opacity: 0.6;");
		}

		quitButton.setStyle("-fx-font-size: 2em;");

		// Admin button action
		adminButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (isAdmin) {
					adminPage(primaryStage);
				}

			}
		});

		// Student button action
		studentButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isStudent == true) {
					studentPage(primaryStage);
				}
			}
		});

		// Teacher button action
		teacherButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (isTeacher == true) {
					teacherPage(primaryStage);
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(studentButton, teacherButton, adminButton);
		HBox.setMargin(studentButton, new Insets(80, 80, 0, 130));
		HBox.setMargin(teacherButton, new Insets(80, 80, 0, 0));
		HBox.setMargin(adminButton, new Insets(80, 80, 0, 0));

		// Bottom pane for exit
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

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(role, welcome);
		HBox.setMargin(role, new Insets(20, 0, 0, 20));
		HBox.setMargin(welcome, new Insets(50, 0, 20, 130));

		// Bottom pane for exit
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

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(role, welcome);
		HBox.setMargin(role, new Insets(20, 0, 0, 20));
		HBox.setMargin(welcome, new Insets(50, 0, 20, 130));

		// Bottom pane for exit
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
		
		Button createArticle = new Button("Create Article");
		Button deleteArticle = new Button("Delete Aritcle");
		Button listArticles = new Button("List Articles");
		Button backupArticles = new Button("Back up Articles");
		Button RestoreArticles = new Button("Restore Articles");

		Button logoutButton = new Button("Log Out");

		// Label Design
		welcome.setFont(new Font("Arial", 36));

		// Button Design
		inviteUserButton.setStyle("-fx-font-size: 1.8em;");
		resetUserButton.setStyle("-fx-font-size: 1.8em;");
		deleteUserButton.setStyle("-fx-font-size: 1.8em;");

		listUsersButton.setStyle("-fx-font-size: 1.8em;");
		addRoleButton.setStyle("-fx-font-size: 1.8em;");
		logoutButton.setStyle("-fx-font-size: 1.8em;");
		
		createArticle.setStyle("-fx-font-size: 1.8em;");
		deleteArticle.setStyle("-fx-font-size: 1.8em;");
		listArticles.setStyle("-fx-font-size: 1.8em;");

		backupArticles.setStyle("-fx-font-size: 1.8em;");
		RestoreArticles.setStyle("-fx-font-size: 1.8em;");
		logoutButton.setStyle("-fx-font-size: 1.8em;");

		// Set buttons actions using EventHandler
		createArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				createArticle(primaryStage);
			}
		});

		deleteArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				deleteArticle(primaryStage);

			}
		});

		listArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					listArticles(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		backupArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				backupArticles(primaryStage);
			}
		});

		RestoreArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO : ADD NEW PAGE

				restoreArticles(primaryStage);

			}
		});

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

				try {
					listUsers(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		addRoleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO : ADD NEW PAGE

				changeRoles(primaryStage);

			}
		});

		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				login(primaryStage); // Log out and go back to login
			}
		});

		// Top Pane
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		
		HBox middleTopTop = new HBox(createArticle,deleteArticle,listArticles,backupArticles,RestoreArticles);
		
		HBox middleTopPane = new HBox(inviteUserButton, resetUserButton, deleteUserButton);
		HBox.setMargin(inviteUserButton, new Insets(50, 50, 80, 120));
		HBox.setMargin(resetUserButton, new Insets(50, 50, 80, 0));
		HBox.setMargin(deleteUserButton, new Insets(50, 0, 80, 0));

		HBox middleBottomPane = new HBox(listUsersButton, addRoleButton, logoutButton);
		HBox.setMargin(listUsersButton, new Insets(50, 50, 80, 200));
		HBox.setMargin(addRoleButton, new Insets(50, 50, 0, 0));
		HBox.setMargin(logoutButton, new Insets(50, 0, 0, 0));

		VBox middlePane = new VBox(middleTopTop, middleTopPane, middleBottomPane);

		// Bottom pane for exit
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

		// CheckBox design
		studentAccount.setFont(new Font("Arial", 20));
		teacherAccount.setFont(new Font("Arial", 20));
		adminAccount.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		sendButton.setStyle("-fx-font-size: 2em;");

		// Send button action
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (emailText.getText().isEmpty()) {
					emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (!studentAccount.isSelected() && !teacherAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);

					String[] roles = new String[TOTALNUMBEROFROLES]; // size 3 currently

					if (studentAccount.isSelected()) {
						roles[0] = "Student";
					}
					if (teacherAccount.isSelected()) {
						roles[1] = "instructor";
					}
					if (adminAccount.isSelected()) {
						roles[2] = "Admin";

					}

					// Generate the one-time code and expiration time
					String code = generateOneTimeCode();
					long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Valid for 24 hours from
																								// now

					// Add the invitation to the linked list
					oneTimePasswordGeneratorList.addPassword(code, roles, expirationTime);

					if (!emailText.getText().isEmpty()) {
						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Send Email");
						alert.setHeaderText("Email Sent"); // Optional: No header text
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
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middleTopPane = new HBox(email, emailText);
		HBox.setMargin(email, new Insets(80, 80, 0, 130));
		HBox.setMargin(emailText, new Insets(80, 80, 0, 0));

		HBox middleBottomPane = new HBox(studentAccount, teacherAccount, adminAccount, noClick);
		HBox.setMargin(studentAccount, new Insets(80, 40, 0, 60));
		HBox.setMargin(teacherAccount, new Insets(80, 40, 0, 0));
		HBox.setMargin(adminAccount, new Insets(80, 40, 0, 0));
		HBox.setMargin(noClick, new Insets(80, 0, 0, 0));

		VBox middlePane = new VBox(middleTopPane, middleBottomPane);

		// Bottom pane for login button
		HBox bottomPane = new HBox(sendButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(sendButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
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
		int length = 10; // MAY CHANGE LATER

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

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Reset User");
		Label username = new Label("Username: ");
		Label noExist = new Label("Username does not exist");

		TextField usernameText = new TextField();

		Button sendButton = new Button("Send");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		noExist.setFont(new Font("Arial", 20));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		sendButton.setStyle("-fx-font-size: 2em;");

		// Send button action
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (usernameText.getText().isEmpty()) {
					usernameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					usernameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (databaseHelper.doesUserExist(usernameText.getText().trim())) {
					noExist.setVisible(true);
				} else {
					noExist.setVisible(false);

					// Generate the one-time code and expiration time
					String code = generateOneTimeCode();
					long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Valid for 24 hours from
																							  // now
					oneTimePasswordGeneratorList.addPasswordForReset(code, expirationTime); //code added into list

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Sent Code");
					alert.setHeaderText("Code Sent"); // Optional: No header text
					alert.setContentText("Following code has been sent: " + code);
					alert.showAndWait();
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, usernameText, noExist);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(usernameText, new Insets(80, 80, 0, 0));
		HBox.setMargin(noExist, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(sendButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(sendButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void deleteUser(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete a User");
		Label username = new Label("Username: ");
		Label askDelete = new Label("Are you sure?");
		Label noExist = new Label("Username does not exist");

		TextField usernameText = new TextField();

		Button deletedButton = new Button("Delete");
		Button confirmDelete = new Button("Yes");
		Button declineDelete = new Button("No");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		askDelete.setFont(new Font("Arial", 20));
		askDelete.setVisible(false);

		noExist.setFont(new Font("Arial", 20));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");

		confirmDelete.setStyle("-fx-font-size: 2em;");
		confirmDelete.setVisible(false);

		declineDelete.setStyle("-fx-font-size: 2em;");
		declineDelete.setVisible(false);

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (usernameText.getText().isEmpty()) {
					usernameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					usernameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (!databaseHelper.doesUserExist(usernameText.getText().trim())) {
					noExist.setVisible(true);
				} else {
					noExist.setVisible(false);

					deletedButton.setVisible(false);
					declineDelete.setVisible(true);
					confirmDelete.setVisible(true);
					askDelete.setVisible(true);

				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		//
		confirmDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				deletedButton.setVisible(true);
				declineDelete.setVisible(false);
				confirmDelete.setVisible(false);
				askDelete.setVisible(false);

				try {
					databaseHelper.deleteUser(usernameText.getText().trim());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		declineDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				deletedButton.setVisible(false);
				declineDelete.setVisible(true);
				confirmDelete.setVisible(true);
				askDelete.setVisible(true);

			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, usernameText, noExist);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(usernameText, new Insets(80, 80, 0, 0));
		HBox.setMargin(noExist, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomTopPane = new HBox(deletedButton);
		bottomTopPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 0, 30, 0));

		HBox bottomMiddlePane = new HBox(askDelete);
		bottomMiddlePane.setAlignment(Pos.CENTER);
		HBox.setMargin(askDelete, new Insets(0, 0, 30, 0));

		HBox bottomBottomPane = new HBox(confirmDelete, declineDelete, quitButton);
		bottomBottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(confirmDelete, new Insets(0, 0, 30, 260));
		HBox.setMargin(declineDelete, new Insets(0, 0, 30, 40));
		HBox.setMargin(quitButton, new Insets(0, 0, 30, 180));

		VBox bottomPane = new VBox(bottomTopPane, bottomMiddlePane, bottomBottomPane);

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void listUsers(Stage primaryStage) throws Exception {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete a User");
		Label printList = new Label(databaseHelper.displayUsersByUser());

		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		printList.setFont(new Font("Arial", 15));

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Button design
		quitButton.setStyle("-fx-font-size: 2em;");

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox middlePane = new HBox(printList);
		middlePane.setAlignment(Pos.CENTER);
		HBox.setMargin(printList, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox bottomPane = new HBox(quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(quitButton, new Insets(50, 0, 20, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void changeRoles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Remove");
		Label user = new Label("Email Address: ");
		Label noClick = new Label("Please choose roles to remove:");

		TextField userText = new TextField();

		Button removeButton = new Button("Remove");
		Button quitButton = new Button("Quit");

		CheckBox studentAccount = new CheckBox("Student");
		CheckBox teacherAccount = new CheckBox("Teacher");
		CheckBox adminAccount = new CheckBox("Admin");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		user.setFont(new Font("Arial", 20));

		noClick.setFont(new Font("Arial", 20));
		noClick.setStyle("-fx-text-fill: red;");
		noClick.setVisible(false);

		// CheckBox design
		studentAccount.setFont(new Font("Arial", 20));
		teacherAccount.setFont(new Font("Arial", 20));
		adminAccount.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		removeButton.setStyle("-fx-font-size: 2em;");

		// Send button action
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (userText.getText().isEmpty()) {
					userText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					userText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
		        String usernameToDeleteFrom = userText.getText();

				if (!studentAccount.isSelected() && !teacherAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);
					
					databaseHelper.doesUserExist(userText.getText().strip());
					
					 if (user != null) {
			                
			                
			               

						Alert alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Roles Changed");
						alert.setHeaderText("Success"); // Optional: No header text
						alert.setContentText("Roles have changed for User: " + userText.getText());

						alert.showAndWait();
					}
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middleTopPane = new HBox(user, userText);
		HBox.setMargin(user, new Insets(80, 80, 0, 130));
		HBox.setMargin(userText, new Insets(80, 80, 0, 0));

		HBox middleBottomPane = new HBox(studentAccount, teacherAccount, adminAccount, noClick);
		HBox.setMargin(studentAccount, new Insets(80, 40, 0, 60));
		HBox.setMargin(teacherAccount, new Insets(80, 40, 0, 0));
		HBox.setMargin(adminAccount, new Insets(80, 40, 0, 0));
		HBox.setMargin(noClick, new Insets(80, 0, 0, 0));

		VBox middlePane = new VBox(middleTopPane, middleBottomPane);

		// Bottom pane for login button
		HBox bottomPane = new HBox(removeButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(removeButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}
	
	public void forgotPassword(Stage primaryStage) {
		
		// Labels and buttons
		Label welcome = new Label("Reset Password");
		Label userName = new Label("Enter Username: ");
		Label password = new Label("Enter Code: ");

		Label invUserName = new Label("Username not found");
		Label invPassword = new Label("Code incorrect");

		Button loginButton = new Button("Create Account");
		Button quitButton = new Button("Quit");

		TextField userNameText = new TextField();
		TextField passwordText = new TextField();

		// Label design
		welcome.setFont(new Font("Arial", 36));
		userName.setFont(new Font("Arial", 20));
		password.setFont(new Font("Arial", 20));

		invUserName.setFont(new Font("Arial", 20));
		invUserName.setStyle("-fx-text-fill: red;");
		invUserName.setVisible(false);

		invPassword.setFont(new Font("Arial", 20));
		invPassword.setStyle("-fx-text-fill: red;");
		invPassword.setVisible(false);

		// Button design
		loginButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");


		// Login button action
				loginButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
												
						// Check username length
						if (databaseHelper.doesUserExist(userNameText.getText().strip())) {
							invUserName.setVisible(true);
						} else {
							invUserName.setVisible(false);
						}

						// holds whether the password is valid
						String otpCode = passwordText.getText().trim();
						boolean validPassword = oneTimePasswordGeneratorList.validatePassword(otpCode);
						System.out.println(validPassword);

						if (validPassword==true) {
							password.setVisible(false); // valid
						} else {
							password.setVisible(true); // OTP is invalid
						}
						
						
						if (validPassword&& databaseHelper.doesUserExist(userNameText.getText().strip())) {
							System.out.println("here2");
							// ADD USER TO LIST
							resetPasswordPage(primaryStage, userNameText.getText().trim()); //enter only if the OTP is corrent
						}
						
					}
				});
		
		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));

		VBox middleMiddlePane = new VBox(userNameText, passwordText);
		VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
		VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));

		VBox middleRightPane = new VBox(invUserName, invPassword);
		VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
		VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));

		// Combine the middle VBoxs
		HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
		middlePane.setAlignment(Pos.CENTER_LEFT);

		// Bottom pane for login button
		HBox bottomPane = new HBox(loginButton, quitButton);
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
	
	
	public void resetPasswordPage(Stage primaryStage, String username) {
		// Labels and buttons
				Label welcome = new Label("Create a New Password");
				Label newPassword = new Label("New Password: ");
				Label confirmPassword = new Label("Confirm Password: ");
				Label mismatchPassword = new Label("Passwords do not match!");

				TextField newPasswordText = new TextField();
				TextField confirmPasswordText = new TextField();

				Button resetButton = new Button("Reset Password");
				Button quitButton = new Button("Quit");

				// Label design
				welcome.setFont(new Font("Arial", 36));
				newPassword.setFont(new Font("Arial", 20));
				confirmPassword.setFont(new Font("Arial", 20));

				mismatchPassword.setFont(new Font("Arial", 20));
				mismatchPassword.setStyle("-fx-text-fill: red;");
				mismatchPassword.setVisible(false);

				// Button design
				resetButton.setStyle("-fx-font-size: 2em;");
				quitButton.setStyle("-fx-font-size: 1.5em;");

				// Reset button action
				resetButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// Check if both passwords match
						if (newPasswordText.getText().equals(confirmPasswordText.getText())) {
							mismatchPassword.setVisible(false);
							// Save the new password in the system for the user
							
							//databaseHelper.resetPassword();
							//databaseHelper.changeUserPassword();
							
							// After successful reset, return to the login page
							login(primaryStage);
						} else {
							mismatchPassword.setVisible(true);
						}
					}
				});

				// Layout for the reset password scene
				VBox resetLayout = new VBox(10, welcome, newPassword, newPasswordText, confirmPassword, confirmPasswordText, mismatchPassword, resetButton, quitButton);
				resetLayout.setAlignment(Pos.CENTER);
				
				Scene resetScene = new Scene(resetLayout, 400, 400);
				primaryStage.setScene(resetScene);
				primaryStage.show();
	}
	
	private void createArticle(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Create Article");
		
		Label email = new Label("Title: ");
		Label firstName = new Label("Author: ");
		Label confFirstName = new Label("Abstract: ");
		Label middleName = new Label("Set of keywords: ");
		Label lastName = new Label("Body of the article: ");
		Label references = new Label("References: ");
		Label level = new Label("Level");

		TextField emailText = new TextField();
		TextField firstNameText = new TextField();
		TextField confFirstNameText = new TextField();
		TextField middleNameText = new TextField();
		TextField lastNameText = new TextField();
		TextField referencesText = new TextField();

		Button conButton = new Button("Confirm");
		Button quitButton = new Button("Quit");
		
		ChoiceBox<String> getLevel = new ChoiceBox<>();

		getLevel.getItems().add("Beginner");
		getLevel.getItems().add("Intermediate");
		getLevel.getItems().add("Advanced");
		getLevel.getItems().add("Expert");


		// Label design
		welcome.setFont(new Font("Arial", 36));
		email.setFont(new Font("Arial", 20));
		firstName.setFont(new Font("Arial", 20));
		confFirstName.setFont(new Font("Arial", 20));
		middleName.setFont(new Font("Arial", 20));
		lastName.setFont(new Font("Arial", 20));
		references.setFont(new Font("Arial", 20));
		level.setFont(new Font("Arial", 20));

		// Button design
		conButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Con button action
		conButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (emailText.getText().isEmpty()) {
					emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (firstNameText.getText().isEmpty()) {
					firstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					firstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (confFirstNameText.getText().isEmpty()) {
					confFirstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					confFirstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (middleNameText.getText().isEmpty()) {
					middleNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					middleNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (lastNameText.getText().isEmpty()) {
					lastNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					lastNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (referencesText.getText().isEmpty()) {
					referencesText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					referencesText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (!emailText.getText().isEmpty() && !firstNameText.getText().isEmpty()
						&& !confFirstNameText.getText().isEmpty() && !middleNameText.getText().isEmpty()
						&& !lastNameText.getText().isEmpty() && !referencesText.getText().isEmpty()
						&& !databaseHelper.doesUserExist(emailText.getText())) {
					
					char[] title = emailText.getText().toCharArray();
					char[] author = firstNameText.getText().toCharArray();
					char[] abstract1 = confFirstNameText.getText().toCharArray();
					char[] keywords = middleNameText.getText().toCharArray();
					char[] body = lastNameText.getText().toCharArray();
					char[] references = referencesText.getText().toCharArray();
					char[] level = referencesText.getText().toCharArray();
					
					try {
						databaseHelper1.register(title, author, abstract1, keywords, body, references,level);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle VBoxs
		VBox middleOne = new VBox(firstName, middleName, email, level);
		VBox.setMargin(firstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleName, new Insets(20, 20, 20, 40));
		VBox.setMargin(email, new Insets(20, 20, 20, 40));
		VBox.setMargin(level, new Insets(20, 20, 20, 40));


		VBox middleTwo = new VBox(firstNameText, middleNameText, emailText,getLevel);
		VBox.setMargin(firstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(emailText, new Insets(20, 20, 20, 40));
		VBox.setMargin(getLevel, new Insets(20, 20, 20, 40));

		VBox middleThree = new VBox(confFirstName, lastName,references);
		VBox.setMargin(confFirstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastName, new Insets(20, 20, 20, 40));
		VBox.setMargin(references, new Insets(20, 20, 20, 40));

		VBox middleFour = new VBox(confFirstNameText, lastNameText, referencesText);
		VBox.setMargin(confFirstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(referencesText, new Insets(20, 20, 20, 40));

		// Combine VBoxs
		HBox middlePane = new HBox(middleOne, middleTwo, middleThree, middleFour);

		// Bottom pane for login button
		HBox bottomPane = new HBox(conButton, quitButton);
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
	
	public void listArticles(Stage primaryStage) throws Exception {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("List Articles");
		Label printList = new Label(databaseHelper1.displayArticles());

		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		printList.setFont(new Font("Arial", 15));

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Button design
		quitButton.setStyle("-fx-font-size: 2em;");

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox middlePane = new HBox(printList);
		middlePane.setAlignment(Pos.CENTER);
		HBox.setMargin(printList, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox bottomPane = new HBox(quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(quitButton, new Insets(50, 0, 20, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void deleteArticle(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete an Article");
		Label username = new Label("Username: ");
		Label noExist = new Label("Username does not exist");

		TextField usernameText = new TextField();

		Button deletedButton = new Button("Delete");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		noExist.setFont(new Font("Arial", 20));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (usernameText.getText().isEmpty()) {
					usernameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					usernameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (databaseHelper1.doesArticleExist(usernameText.getText().strip())) {
					noExist.setVisible(false);
					try {
						databaseHelper1.deleteArticle(usernameText.getText().strip());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					noExist.setVisible(true);

				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, usernameText, noExist);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(usernameText, new Insets(80, 80, 0, 0));
		HBox.setMargin(noExist, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void backupArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Backup Articles");
		Label username = new Label("File name: ");

		TextField fileNameText = new TextField();

		Button deletedButton = new Button("Backup");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					try {
						databaseHelper1.backup(fileNameText.getText().strip());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, fileNameText);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(fileNameText, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void restoreArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Restore Articles");
		Label username = new Label("File name: ");

		TextField fileNameText = new TextField();

		Button deletedButton = new Button("Restore");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					try {
						databaseHelper1.restore(fileNameText.getText().strip()); //restores articles with old file
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, fileNameText);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(fileNameText, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}
	
}
