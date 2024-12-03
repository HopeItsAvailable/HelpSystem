package helpSystem;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;

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
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.util.*;
import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import helpSystem.DatabaseHelperUser;
import helpSystem.DatabaseHelperArticle;
import helpSystem.DatabaseHelperArticleGroups;

public class helpSystemStart extends Application {

	private static DatabaseHelperUser databaseHelper;
	private static DatabaseHelperArticle databaseHelper1;
	private static DatabaseHelperArticleGroups databaseHelper2;


	

	private Scene loginScene; // To store the initial login scene
	// private LinkedList linkedList; // Declare LinkedList
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
	        oneTimePasswordGeneratorList = new oneTimePasswordGeneratorList(); // Holds the roles for when an invite is created
	        size++;
	    }

	    //creates database class
	    databaseHelper = new DatabaseHelperUser();
	    databaseHelper1 = new DatabaseHelperArticle();
	    databaseHelper2 = new DatabaseHelperArticleGroups();
	    

	    try {
	    	
	    	//connects to all three databases
	        databaseHelper.connectToDatabase();
	        databaseHelper1.connectToDatabase();
	        databaseHelper2.connectToDatabase();

	        primaryStage.setTitle("Help System"); //sets title

	        // Label
	        Label welcome = new Label("Welcome to our Help System");
	        welcome.setFont(new Font("Montserrat", 45));

	        // Dynamically change font size based on window height
	        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
	            double newFontSize = 0.08 * newHeight.doubleValue(); // Convert to double before multiplying
	            welcome.setFont(new Font("Montserrat", newFontSize)); // Apply the new font size
	        });
	        
	        
	        // Line for header
	        Line line = new Line();
	        line.setId("lineDetails");
	        line.setStartY(50); // Set a starting Y position for the line
	        line.setEndY(50);   // Keep the line horizontal

	        // binds size of page to line
	        line.endXProperty().bind(welcome.widthProperty());
	        line.startXProperty().bind(welcome.layoutXProperty());

	        // Logo
	        ImageView logoImageView = new ImageView();
	        logoImageView.setImage(new Image(getClass().getResourceAsStream("img/logo.png")));

	        // Dynamically resize logo based on stage width and height
	        logoImageView.fitWidthProperty().bind(primaryStage.widthProperty().multiply(0.5));
	        logoImageView.fitHeightProperty().bind(primaryStage.heightProperty().multiply(0.6)); 

	        // Create the Start button
	        Button startButton = new Button("Start");
	        startButton.setId("buttonStart");  // Set the ID for styling

	        // Dynamically resize button based on stage width and height
	        startButton.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2)); 
	        startButton.prefHeightProperty().bind(primaryStage.heightProperty().multiply(0.05));

	        // Set the action for when the login button is clicked
	        startButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	                try {
	                    if (databaseHelper.isDatabaseEmpty()) {
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
	        topPane.getChildren().addAll(welcome, line);  // Add the line to the top pane
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
	        starterScreen.setId("startBackground");  // Use CSS for background
	        starterScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	        // Set the location of elements in the BorderPane
	        starterScreen.setTop(topPane);
	        starterScreen.setCenter(middlePane);
	        starterScreen.setBottom(bottomPane);

	        // Store the initial scene
	        Scene loginScene = new Scene(starterScreen, 900, 600);

	        // Set the scene and show the stage
	        primaryStage.setScene(loginScene);

	        // Ensure layout is recalculated after resizing
	        primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
	            starterScreen.requestLayout();
	        });

	        primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
	            starterScreen.requestLayout(); 
	        });

	        primaryStage.show();
	    } catch (SQLException e) {
	        System.err.println("Database error: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	private void createAdmin(Stage primaryStage) {
	    
		// GUI elements
	    Label welcome = new Label("Create an Admin Account");

	    Label invUserName = new Label("Username must be between 6-12 characters");
	    Label invPassword = new Label("Password must be at least 6 characters");
	    Label invConPassword = new Label("Password does not match");

	    Button loginButton = new Button("Create Account");
	    Button quitButton = new Button("Quit");

	    TextField userNameText = new TextField();
	    userNameText.setPromptText("Username");  // Set placeholder for username
	    userNameText.setPrefWidth(300);  // Set the preferred width
	    userNameText.setMaxWidth(300);   
	    
	    PasswordField passwordText = new PasswordField();  // PasswordField for security
	    passwordText.setPromptText("Password"); 
	    passwordText.setPrefWidth(300);
	    passwordText.setMaxWidth(300);   
	    
	    PasswordField confPasswordText = new PasswordField();  // PasswordField for confirmation
	    confPasswordText.setPromptText("Re-enter Password"); 
	    confPasswordText.setPrefWidth(300);
	    confPasswordText.setMaxWidth(300);   
	    
	    // Label design
	    welcome.setFont(new Font("Montserrat", 30));
	    welcome.setStyle("-fx-font-weight: bold;");

	    invUserName.setFont(new Font("Montserrat", 12));
	    invUserName.setStyle("-fx-text-fill: red;");
	    invUserName.setVisible(false);

	    invPassword.setFont(new Font("Montserrat", 12));
	    invPassword.setStyle("-fx-text-fill: red;");
	    invPassword.setVisible(false);

	    invConPassword.setFont(new Font("Montserrat", 12));
	    invConPassword.setStyle("-fx-text-fill: red;");
	    invConPassword.setVisible(false);

	    // Button design
	    loginButton.setId("buttonDesign");  // Set the ID for styling
	    quitButton.setId("buttonDesign");  // Set the ID for styling


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
	            
	            // Check password match
	            if (!passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
	                invConPassword.setVisible(true);
	            } else {
	                invConPassword.setVisible(false);
	            }

	            // Proceed if all conditions are met
	            if (passwordText.getText().trim().length() >= 6 && userNameText.getText().trim().length() >= 6
	                    && userNameText.getText().trim().length() <= 12
	                    && passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
	                String username = userNameText.getText().trim();
	                String password = passwordText.getText().trim();
	                try {
	                    databaseHelper.registerFirstUser(username, password);
	                    databaseHelper2.addArticleGroup("Default");
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
	                e.printStackTrace();
	            }
	        }
	    });

	    // Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, userNameText, invUserName, passwordText, invPassword, confPasswordText, invConPassword);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, loginButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);
	}

	private void login(Stage primaryStage) {

	    // Labels and buttons
	    Label welcome = new Label("Log In");

	    Label invUserName = new Label("Username does not exist");
	    Label invPassword = new Label("Incorrect Password");
	    Label invConPassword = new Label("Passwords do not match");
	    Label checkLogin = new Label("Account not found. Try again.");

	    Button loginButton = new Button("Log-In");
	    Button quitButton = new Button("Quit");
	    Button createAccountButton = new Button("Create Account");
	    Button forgotPassword = new Button("Forgot Password");

	    TextField userNameText = new TextField();
	    userNameText.setPromptText("Username"); // Placeholder text
	    userNameText.setPrefWidth(300);  // Set the preferred width
	    userNameText.setMaxWidth(300);

	    PasswordField passwordText = new PasswordField();
	    passwordText.setPromptText("Password"); // Placeholder text
	    passwordText.setPrefWidth(300);  // Set the preferred width
	    passwordText.setMaxWidth(300);

	    PasswordField confPasswordText = new PasswordField();
	    confPasswordText.setPromptText("Confirm Password"); // Placeholder text
	    confPasswordText.setPrefWidth(300);  // Set the preferred width
	    confPasswordText.setMaxWidth(300);

	    // Label design
	    welcome.setFont(new Font("Montserrat", 36));
	    welcome.setStyle("-fx-font-weight: bold;");

	    invUserName.setFont(new Font("Montserrat", 12));
	    invUserName.setStyle("-fx-text-fill: red;");
	    invUserName.setVisible(false);

	    invPassword.setFont(new Font("Montserrat", 12));
	    invPassword.setStyle("-fx-text-fill: red;");
	    invPassword.setVisible(false);

	    invConPassword.setFont(new Font("Montserrat", 12));
	    invConPassword.setStyle("-fx-text-fill: red;");
	    invConPassword.setVisible(false);

	    checkLogin.setFont(new Font("Arial", 12));
	    checkLogin.setStyle("-fx-text-fill: red;");
	    checkLogin.setVisible(false);

	    // Button design
	    loginButton.setId("buttonDesign");  // Set the ID for styling
	    quitButton.setId("buttonDesign");  // Set the ID for styling
	    createAccountButton.setId("buttonDesign");  // Set the ID for styling
	    forgotPassword.setId("buttonDesign2");  // Set the ID for styling


	    // Forgot Password button action
	    forgotPassword.setOnAction(event -> forgotPassword(primaryStage));

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
				
				// Check password match
	            if (!passwordText.getText().trim().equals(confPasswordText.getText().trim())) {
	                invConPassword.setVisible(true);
	            } else {
	                invConPassword.setVisible(false);
	            }

				// Proceed if all conditions are met
				if (passwordText.getText().trim().length() >= 6 && userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12
						&& passwordText.getText().trim().equals(confPasswordText.getText().trim())) {

					boolean check = databaseHelper.doesUserExist(userNameText.getText().strip());
					boolean[] roles = new boolean[3];
					System.out.println("Logging in with username: " + userNameText.getText().strip());

					if (check) {
						checkLogin.setVisible(false);

						System.out.println("FOUND UOU");

						String username = userNameText.getText().strip();
						try {
							roles = databaseHelper.getUserRoles(username);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (databaseHelper.isEmailEmpty(username) == false) {
							addAccountInfo(primaryStage, username);
						} else {

							if (roles != null) {
								int roleCount = 0;
								String roleToRedirect = "";

								if (roles[0]) {
									roleCount++;
									roleToRedirect = "Admin";
								}
								if (roles[1]) {
									roleCount++;
									roleToRedirect = "Student";
								}
								if (roles[2]) {
									roleCount++;
									roleToRedirect = "Instructor";
								}

								if (roleCount == 1) {
									// Redirect to the role page directly if only one role exists
									if (roleToRedirect.equals("Admin")) {
										// Redirect to admin page
										adminPage(userNameText.getText().strip(),primaryStage);
									} else if (roleToRedirect.equals("Student")) {
										// Redirect to student page
										studentPage(primaryStage,userNameText.getText().strip());
									} else if (roleToRedirect.equals("Instructor")) {
										// Redirect to instructor page
										teacherPage(primaryStage,userNameText.getText().strip());
									}
								} else if (roleCount > 1) {
									// Redirect to a page where user can choose their role
									try {
										try {
											chooseRole(primaryStage, username);
										} catch (SQLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
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
			
		//Layout for page
		HBox topTop = new HBox(quitButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 60, 0));
		
		VBox topPane = new VBox(20,topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

		HBox middleButtons = new HBox(30);
		middleButtons.setAlignment(Pos.CENTER);
		middleButtons.getChildren().addAll(forgotPassword, loginButton);

		VBox middleMiddlePane = new VBox(10, userNameText, invUserName, passwordText, invPassword, confPasswordText, invConPassword, middleButtons);
		middleMiddlePane.setAlignment(Pos.TOP_CENTER);

		HBox bottomPane = new HBox(20, createAccountButton);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(0, 0, 50, 0));

		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middleMiddlePane);
		adminCreateScreen.setBottom(bottomPane);

		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void createUser(Stage primaryStage) {

		// Labels and buttons
	    Label welcome = new Label("Create an Account");

	    Label invUserName = new Label("Username must be between 6-12 characters");
	    Label invPassword = new Label("Password must be at least 6 characters");
	    Label invConPassword = new Label("Password does not match");
	    Label invKey = new Label("Code does not exist");

	    Button loginButton = new Button("Create Account");
	    Button quitButton = new Button("Quit");

	    // TextFields with prompt text (placeholders)
	    TextField userNameText = new TextField();
	    userNameText.setPromptText("Username");  // Set placeholder for username
	    userNameText.setPrefWidth(300);  // Set the preferred width
	    userNameText.setMaxWidth(300);   
	    
	    PasswordField passwordText = new PasswordField();  // PasswordField for security
	    passwordText.setPromptText("Password");  // Set placeholder for password
	    passwordText.setPrefWidth(300);  // Set the preferred width
	    passwordText.setMaxWidth(300);   
	    
	    PasswordField confPasswordText = new PasswordField();  // PasswordField for confirmation
	    confPasswordText.setPromptText("Re-enter Password");  // Set placeholder for confirmation
	    confPasswordText.setPrefWidth(300);  // Set the preferred width
	    confPasswordText.setMaxWidth(300);   

	    PasswordField codeText = new PasswordField();  // PasswordField for confirmation
	    codeText.setPromptText("Code from admin");  // Set placeholder for confirmation
	    codeText.setPrefWidth(300);  // Set the preferred width
	    codeText.setMaxWidth(300);   

	    // Label design
	    welcome.setFont(new Font("Montserrat", 30));
	    welcome.setStyle("-fx-font-weight: bold;");

	    invUserName.setFont(new Font("Montserrat", 12));
	    invUserName.setStyle("-fx-text-fill: red;");
	    invUserName.setVisible(false);

	    invKey.setFont(new Font("Montserrat", 12));
	    invKey.setStyle("-fx-text-fill: red;");
	    invKey.setVisible(false);

	    invPassword.setFont(new Font("Montserrat", 12));
	    invPassword.setStyle("-fx-text-fill: red;");
	    invPassword.setVisible(false);

	    invConPassword.setFont(new Font("Montserrat", 12));
	    invConPassword.setStyle("-fx-text-fill: red;");
	    invConPassword.setVisible(false);

	    // Button design
	    loginButton.setId("buttonDesign");  // Set the ID for styling
	    quitButton.setId("buttonDesign");  // Set the ID for styling


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
				if (passwordText.getText().trim().length() >= 6 && userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12) {

					// ADD USER TO LIST

					String username = userNameText.getText().trim();
					String password = passwordText.getText().trim();
					String userCode = codeText.getText().strip(); // OTP entered by the user

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

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, userNameText, invUserName, passwordText, invPassword, confPasswordText, invConPassword,codeText, invKey);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, loginButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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
		welcome.setFont(new Font("Montserrat", 36));
		email.setFont(new Font("Montserrat", 20));
		firstName.setFont(new Font("Montserrat", 20));
		confFirstName.setFont(new Font("Montserrat", 20));
		middleName.setFont(new Font("Montserrat", 20));
		lastName.setFont(new Font("Montserrat", 20));

		// Button design
		conButton.setId("buttonDesign");  // Set the ID for styling
	    quitButton.setId("buttonDesign");  // Set the ID for styling


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
					
					databaseHelper.updateUserAccountInfo(
			                username,
			                emailText.getText(),
			                firstNameText.getText(),
			                confFirstNameText.getText(),  // Assuming this is the preferred first name
			                middleNameText.getText(),
			                lastNameText.getText()
			            );

					System.out.println(username + "done adding");
					boolean[] roles = new boolean[3];
					
					try {
						roles = databaseHelper.getUserRoles(username);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


					if (roles != null) {
						int roleCount = 0;
						String roleToRedirect = "";

						if (roles[0]) {
							roleCount++;
							roleToRedirect = "Admin";
						}
						if (roles[1]) {
							roleCount++;
							roleToRedirect = "Student";
						}
						if (roles[2]) {
							roleCount++;
							roleToRedirect = "Instructor";
						}

						if (roleCount == 1) {
							// Redirect to the role page directly if only one role exists
							if (roleToRedirect.equals("Admin")) {
								// Redirect to admin page
								adminPage(username, primaryStage);
							} else if (roleToRedirect.equals("Student")) {
								// Redirect to student page
								studentPage(primaryStage, username);
							} else if (roleToRedirect.equals("Instructor")) {
								// Redirect to instructor page
								teacherPage(primaryStage, username);
							}
						} else if (roleCount > 1) {
							// Redirect to a page where user can choose their role
							try {
								chooseRole(primaryStage, username);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

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
		HBox topTop = new HBox(quitButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 40, 0));
		
		VBox topPane = new VBox(20,topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

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
		middlePane.setAlignment(Pos.TOP_CENTER);

		// Bottom pane for login button
		HBox bottomPane = new HBox(conButton);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(0, 0, 50, 0));
		

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void chooseRole(Stage primaryStage, String UserName) throws SQLException {

		// WORK ON THIS CHECK WHICH ROLES USER IS
		
		boolean[] checkRoles = databaseHelper.getUserRoles(UserName);

		boolean isAdmin = checkRoles[0];
		boolean isStudent = checkRoles[1];
		boolean isTeacher = checkRoles[2];

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
					adminPage(UserName, primaryStage);
				}

			}
		});

		// Student button action
		studentButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (isStudent == true) {
					studentPage(primaryStage, UserName);
				}
			}
		});

		// Teacher button action
		teacherButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (isTeacher == true) {
					teacherPage(primaryStage, UserName);
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
		HBox middlePane = new HBox(adminButton, studentButton, teacherButton);
		HBox.setMargin(adminButton, new Insets(80, 80, 0, 130));
		HBox.setMargin(studentButton, new Insets(80, 80, 0, 0));
		HBox.setMargin(teacherButton, new Insets(80, 80, 0, 0));

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

	private void studentPage(Stage primaryStage, String userName) {

		// Labels and buttons
		Label welcome = new Label("Welcome Student");

		// Label design
	    welcome.setFont(new Font("Montserrat", 36));
	    welcome.setStyle("-fx-font-weight: bold;");
	    
		Button quitButton = new Button("Log Out");
		Button viewArt = new Button("View Articles");
		Button sendMessages = new Button("Send Messages");
		Button reviewMessages = new Button("Review Messages");

		// Button design
		quitButton.setId("buttonDesign");
		viewArt.setId("buttonDesign");
		sendMessages.setId("buttonDesign");
		reviewMessages.setId("buttonDesign");
		
		final double BUTTON_WIDTH = 175.0; 

		viewArt.setPrefWidth(BUTTON_WIDTH);
		sendMessages.setPrefWidth(BUTTON_WIDTH);
		reviewMessages.setPrefWidth(BUTTON_WIDTH);

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// Quit button action
		viewArt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					listArticles(primaryStage, userName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// Quit button action
		sendMessages.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				sendMes(primaryStage);
				
			}
		});
		
		// Quit button action
		reviewMessages.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				reviewMes(primaryStage);
				
			}
		});

		HBox topTop = new HBox(quitButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 170, 0));
		
		VBox topPane = new VBox(20,topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

		HBox middlePane = new HBox(30, viewArt, sendMessages, reviewMessages);
		middlePane.setAlignment(Pos.TOP_CENTER);

		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);

		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void teacherPage(Stage primaryStage, String userName) {

		Label welcome = new Label("Welcome Teacher");
		Label articleButtons = new Label("Article Commands");
		Label userButtons = new Label("User/Group Commands");

		Button inviteUserButton = new Button("Invite User");
		Button deleteUserButton = new Button("Delete User");
		Button createGroup = new Button("Create Group");
		Button backupGroup = new Button("Backup Group");
		Button restoreGroup = new Button("Restore Group");
		Button deleteGroup = new Button("Delete Group");
		Button manageGroup = new Button("Manage Groups");


		Button createArticle = new Button("Create Article");
		Button deleteArticle = new Button("Delete Article");
		Button listArticles = new Button("List Articles");
		Button backupArticles = new Button("Back up Articles");
		Button RestoreArticles = new Button("Restore Articles");
		Button updateArt = new Button("Update Article");

		Button logoutButton = new Button("Log Out");

		// Label Design
	    welcome.setFont(new Font("Montserrat", 36));
	    welcome.setStyle("-fx-font-weight: bold;");
	    
	    articleButtons.setFont(new Font("Montserrat", 26));
	    articleButtons.setStyle("-fx-font-weight: bold;");

	    userButtons.setFont(new Font("Montserrat", 26));
	    userButtons.setStyle("-fx-font-weight: bold;");


	    
		// Button Design
		inviteUserButton.setId("buttonDesign");
		manageGroup.setId("buttonDesign");
		deleteUserButton.setId("buttonDesign");
		createGroup.setId("buttonDesign");
		backupGroup.setId("buttonDesign");
		restoreGroup.setId("buttonDesign");
		deleteGroup.setId("buttonDesign");


		createArticle.setId("buttonDesign");
		deleteArticle.setId("buttonDesign");
		listArticles.setId("buttonDesign");
		backupArticles.setId("buttonDesign");
		RestoreArticles.setId("buttonDesign");
		updateArt.setId("buttonDesign");
		
		logoutButton.setId("buttonDesign");
		
		final double BUTTON_WIDTH = 175.0; 

		inviteUserButton.setPrefWidth(BUTTON_WIDTH);
		backupGroup.setPrefWidth(BUTTON_WIDTH);
		manageGroup.setPrefWidth(BUTTON_WIDTH);
		restoreGroup.setPrefWidth(BUTTON_WIDTH);
		deleteUserButton.setPrefWidth(BUTTON_WIDTH);
		createGroup.setPrefWidth(BUTTON_WIDTH);
		deleteGroup.setPrefWidth(BUTTON_WIDTH);

		createArticle.setPrefWidth(BUTTON_WIDTH);
		deleteArticle.setPrefWidth(BUTTON_WIDTH);
		listArticles.setPrefWidth(BUTTON_WIDTH);
		backupArticles.setPrefWidth(BUTTON_WIDTH);
		RestoreArticles.setPrefWidth(BUTTON_WIDTH);
		updateArt.setPrefWidth(BUTTON_WIDTH);



		// Set buttons actions using EventHandler
		createArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				createArticle(primaryStage, userName);
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
					listArticles(primaryStage, userName);
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
		
		// Quit button action
		updateArt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					updateArticle(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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

		deleteUserButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				deleteUser(primaryStage);

			}
		});
		
		createGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				createGroup(primaryStage);
			}
		});
		
		backupGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				backupGroup(primaryStage, userName);
			}
		});
		
		deleteGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				deleteGroup(primaryStage, userName);
			}
		});

		restoreGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				restoreGroup(primaryStage, userName);
			}
		});
		
		manageGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				manageGroup(primaryStage, userName);
			}
		});
		
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				login(primaryStage); // Log out and go back to login
			}
		});
		
		

		// Top Pane
		HBox topTop = new HBox(logoutButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 34, 0));

		VBox topPane = new VBox(20, topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

		// Left Pane
		VBox leftRow1 = new VBox(30, createArticle, deleteArticle, listArticles);
		VBox leftRow2 = new VBox(30, backupArticles, RestoreArticles, updateArt);

		HBox leftRows = new HBox(30, leftRow1, leftRow2);
		leftRows.setAlignment(Pos.TOP_CENTER);

		VBox leftSide = new VBox(40, articleButtons, leftRows);
		leftSide.setAlignment(Pos.TOP_CENTER);

		// Right Pane
		VBox rightRow1 = new VBox(30, inviteUserButton, createGroup, backupGroup,deleteGroup);
		VBox rightRow2 = new VBox(30, deleteUserButton, restoreGroup,manageGroup);

		HBox rightRows = new HBox(30, rightRow1, rightRow2);
		rightRows.setAlignment(Pos.TOP_CENTER);

		VBox rightSide = new VBox(40, userButtons, rightRows);
		rightSide.setAlignment(Pos.TOP_CENTER);

		// Middle Pane
		HBox middlePane = new HBox(40, leftSide, rightSide);
		middlePane.setAlignment(Pos.TOP_CENTER);

		// Main Layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);

		// Scene Setup
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void adminPage(String userName, Stage primaryStage) {
		
		Label welcome = new Label("Welcome Admin");
		Label articleButtons = new Label("Article Commands");
		Label userButtons = new Label("User/Group Commands");

		Button inviteUserButton = new Button("Invite User");
		Button resetUserButton = new Button("Reset User");
		Button deleteUserButton = new Button("Delete User");
		Button listUsersButton = new Button("List Users");
		Button addRoleButton = new Button("Add/Remove Role");
		Button createGroup = new Button("Create Group");
		Button backupGroup = new Button("Backup Group");
		Button restoreGroup = new Button("Restore Group");
		Button deleteGroup = new Button("Delete Group");
		Button manageGroup = new Button("Manage Groups");


		Button createArticle = new Button("Create Article");
		Button deleteArticle = new Button("Delete Article");
		Button listArticles = new Button("List Articles");
		Button backupArticles = new Button("Back up Articles");
		Button RestoreArticles = new Button("Restore Articles");
		Button updateArt = new Button("Update Article");

		Button logoutButton = new Button("Log Out");

		// Label Design
	    welcome.setFont(new Font("Montserrat", 36));
	    welcome.setStyle("-fx-font-weight: bold;");
	    
	    articleButtons.setFont(new Font("Montserrat", 26));
	    articleButtons.setStyle("-fx-font-weight: bold;");

	    userButtons.setFont(new Font("Montserrat", 26));
	    userButtons.setStyle("-fx-font-weight: bold;");


	    
		// Button Design
		inviteUserButton.setId("buttonDesign");
		manageGroup.setId("buttonDesign");
		resetUserButton.setId("buttonDesign");
		deleteUserButton.setId("buttonDesign");
		listUsersButton.setId("buttonDesign");
		addRoleButton.setId("buttonDesign");
		createGroup.setId("buttonDesign");
		backupGroup.setId("buttonDesign");
		restoreGroup.setId("buttonDesign");
		deleteGroup.setId("buttonDesign");


		createArticle.setId("buttonDesign");
		deleteArticle.setId("buttonDesign");
		listArticles.setId("buttonDesign");
		backupArticles.setId("buttonDesign");
		RestoreArticles.setId("buttonDesign");
		updateArt.setId("buttonDesign");
		
		logoutButton.setId("buttonDesign");
		
		final double BUTTON_WIDTH = 175.0; 

		inviteUserButton.setPrefWidth(BUTTON_WIDTH);
		backupGroup.setPrefWidth(BUTTON_WIDTH);
		manageGroup.setPrefWidth(BUTTON_WIDTH);
		restoreGroup.setPrefWidth(BUTTON_WIDTH);
		resetUserButton.setPrefWidth(BUTTON_WIDTH);
		deleteUserButton.setPrefWidth(BUTTON_WIDTH);
		listUsersButton.setPrefWidth(BUTTON_WIDTH);
		addRoleButton.setPrefWidth(BUTTON_WIDTH);
		createGroup.setPrefWidth(BUTTON_WIDTH);
		deleteGroup.setPrefWidth(BUTTON_WIDTH);

		createArticle.setPrefWidth(BUTTON_WIDTH);
		deleteArticle.setPrefWidth(BUTTON_WIDTH);
		listArticles.setPrefWidth(BUTTON_WIDTH);
		backupArticles.setPrefWidth(BUTTON_WIDTH);
		RestoreArticles.setPrefWidth(BUTTON_WIDTH);
		updateArt.setPrefWidth(BUTTON_WIDTH);



		// Set buttons actions using EventHandler
		createArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				createArticle(primaryStage, userName);
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
					listArticles(primaryStage, userName);
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
		
		// Quit button action
		updateArt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					updateArticle(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		
		addRoleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO : ADD NEW PAGE

				changeRoles(primaryStage);

			}
		});
		
		createGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				createGroup(primaryStage);
			}
		});
		
		backupGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				backupGroup(primaryStage, userName);
			}
		});
		
		deleteGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				deleteGroup(primaryStage, userName);
			}
		});

		restoreGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				restoreGroup(primaryStage, userName);
			}
		});
		
		manageGroup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//TODO
				manageGroup(primaryStage, userName);
			}
		});
		
		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				login(primaryStage); // Log out and go back to login
			}
		});
		
		

		// Top Pane
		HBox topTop = new HBox(logoutButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 34, 0));

		VBox topPane = new VBox(20, topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

		// Left Pane
		VBox leftRow1 = new VBox(30, createArticle, deleteArticle, listArticles);
		VBox leftRow2 = new VBox(30, backupArticles, RestoreArticles, updateArt);

		HBox leftRows = new HBox(30, leftRow1, leftRow2);
		leftRows.setAlignment(Pos.TOP_CENTER);

		VBox leftSide = new VBox(40, articleButtons, leftRows);
		leftSide.setAlignment(Pos.TOP_CENTER);

		// Right Pane
		VBox rightRow1 = new VBox(30, inviteUserButton, resetUserButton, deleteUserButton, backupGroup,deleteGroup);
		VBox rightRow2 = new VBox(30, listUsersButton, addRoleButton, createGroup, restoreGroup,manageGroup);

		HBox rightRows = new HBox(30, rightRow1, rightRow2);
		rightRows.setAlignment(Pos.TOP_CENTER);

		VBox rightSide = new VBox(40, userButtons, rightRows);
		rightSide.setAlignment(Pos.TOP_CENTER);

		// Middle Pane
		HBox middlePane = new HBox(40, leftSide, rightSide);
		middlePane.setAlignment(Pos.TOP_CENTER);

		// Main Layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);

		// Scene Setup
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);
	}

	private void sendCode(Stage primaryStage) {
		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Invite User");
		Label noClick = new Label("Please choose which type of account");
	    Label invUserName = new Label("Username must be between 6-12 characters");


		TextField emailText = new TextField();
		emailText.setPromptText("Email");  // Set placeholder for username
		emailText.setPrefWidth(300);  // Set the preferred width
		emailText.setMaxWidth(300);   

		Button sendButton = new Button("Send");
		Button quitButton = new Button("Quit");

		CheckBox adminAccount = new CheckBox("Admin");
		CheckBox studentAccount = new CheckBox("Student");
		CheckBox teacherAccount = new CheckBox("Teacher");

		// Label design
	    welcome.setFont(new Font("Montserrat", 30));
	    welcome.setStyle("-fx-font-weight: bold;");
	    

		noClick.setFont(new Font("Montserrat", 12));
		noClick.setStyle("-fx-text-fill: red;");
		noClick.setVisible(false);
		
		invUserName.setFont(new Font("Montserrat", 12));
	    invUserName.setStyle("-fx-text-fill: red;");
	    invUserName.setVisible(false);

		// CheckBox design
		adminAccount.setFont(new Font("Montserrat", 20));
		studentAccount.setFont(new Font("Montserrat", 20));
		teacherAccount.setFont(new Font("Montserrat", 20));

		// Button design
		sendButton.setId("buttonDesign"); 
	    quitButton.setId("buttonDesign");

		// Send button action
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (emailText.getText().isEmpty()) {
					invUserName.setVisible(true);
				} else {
					invUserName.setVisible(false);
				}

				if (!studentAccount.isSelected() && !teacherAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);

					String[] roles = new String[TOTALNUMBEROFROLES]; // size 3 currently

					if (adminAccount.isSelected()) {
						roles[0] = "admin";
					}
					if (studentAccount.isSelected()) {
						roles[1] = "student";
					}
					if (teacherAccount.isSelected()) {
						roles[2] = "instructor";
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		HBox checkStuff = new HBox(20, adminAccount,studentAccount,teacherAccount);
		checkStuff.setAlignment(Pos.CENTER);
	
		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, emailText, invUserName, checkStuff,noClick);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, sendButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);


	}

	public String generateOneTimeCode() {
		// Length of the code
		int length = 10; // MAY CHANGE LATER

		// all characters, will combine later
		String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallChars = "abcdefghjkmnpqrstuvwxyz";
		String numbers = "123456789";
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
		Label noExist = new Label("Username does not exist");

		TextField usernameText = new TextField();
		usernameText.setPromptText("Username");  // Set placeholder for username
		usernameText.setPrefWidth(300);  // Set the preferred width
		usernameText.setMaxWidth(300);   

		Button sendButton = new Button("Send");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		sendButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");

		// Send button action
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
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

					// Generate the one-time code and expiration time
					String code = generateOneTimeCode();
					long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // Valid for 24 hours from
																								// now
					oneTimePasswordGeneratorList.addPasswordForReset(code, expirationTime); // code added into list

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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	
		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, sendButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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
		welcome.setFont(new Font("Montserrat", 36));
		username.setFont(new Font("Montserrat", 20));

		askDelete.setFont(new Font("Montserrat", 12));
		askDelete.setVisible(false);

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		deletedButton.setId("buttonDesign"); 
		declineDelete.setId("buttonDesign"); 
		confirmDelete.setId("buttonDesign"); 
		quitButton.setId("buttonDesign"); 

		confirmDelete.setVisible(false);
		declineDelete.setVisible(false);

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

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
					login(primaryStage);
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

				deletedButton.setVisible(true);
				declineDelete.setVisible(false);
				confirmDelete.setVisible(false);
				askDelete.setVisible(false);

			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox choicePane = new HBox(20,declineDelete,confirmDelete);
	    HBox bottomTopPane = new HBox(20, deletedButton, quitButton);
	    choicePane.setAlignment(Pos.CENTER);
	    bottomTopPane.setAlignment(Pos.CENTER);
	    
	    VBox bottomPane = new VBox(20, bottomTopPane, choicePane,askDelete);
	    bottomPane.setAlignment(Pos.CENTER);



	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	public void listUsers(Stage primaryStage) throws Exception {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete a User");
		Label printList = new Label(databaseHelper.getAllUsers());

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
		Label welcome = new Label("Remove role");
		Label user = new Label("Username: ");
		Label noExist = new Label("Username does not exist");
		Label noClick = new Label("Please choose roles to add/remove");

		TextField userText = new TextField();

		Button removeButton = new Button("Remove");
		Button addButton = new Button("Add");
		Button quitButton = new Button("Quit");

		CheckBox studentAccount = new CheckBox("Student");
		CheckBox teacherAccount = new CheckBox("Teacher");
		CheckBox adminAccount = new CheckBox("Admin");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		user.setFont(new Font("Montserrat", 20));
		
		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		noClick.setFont(new Font("Montserrat", 12));
		noClick.setStyle("-fx-text-fill: red;");
		noClick.setVisible(false);

		// CheckBox design
		studentAccount.setFont(new Font("Montserrat", 20));
		teacherAccount.setFont(new Font("Montserrat", 20));
		adminAccount.setFont(new Font("Montserrat", 20));

		// Button design
		removeButton.setId("buttonDesign"); 
		addButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");

		// Send button action
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (databaseHelper.doesUserExist(userText.getText())) {
					noExist.setVisible(false);
				} else {
					noExist.setVisible(true);
				}
				String usernameToDeleteFrom = userText.getText();
				boolean[] roles = new boolean[3];
				
				if (!studentAccount.isSelected() && !teacherAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);
					
					try {
			            if (adminAccount.isSelected()) {
			                databaseHelper.removeAdminRole(usernameToDeleteFrom);
			            }
			            if (studentAccount.isSelected()) {
			                databaseHelper.removeStudentRole(usernameToDeleteFrom);
			            }
			            if (teacherAccount.isSelected()) {
			                databaseHelper.removeInstructorRole(usernameToDeleteFrom);
			            }
			            // Optionally provide feedback to the admin, e.g., show a success message
			        } catch (Exception e) {
			            e.printStackTrace(); // Handle exceptions appropriately
			        }

				}
			}
		});
		
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (databaseHelper.doesUserExist(userText.getText())) {
					noExist.setVisible(false);
				} else {
					noExist.setVisible(true);
				}
				String usernameToAddFrom = userText.getText();
				boolean[] roles = new boolean[3];
				
				if (!studentAccount.isSelected() && !teacherAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);
					
					try {
			            if (adminAccount.isSelected()) {
			                databaseHelper.addAdminRole(usernameToAddFrom);
			            }
			            if (studentAccount.isSelected()) {
			                databaseHelper.addStudentRole(usernameToAddFrom);
			            }
			            if (teacherAccount.isSelected()) {
			                databaseHelper.addInstructorRole(usernameToAddFrom);
			            }
			            // Optionally provide feedback to the admin, e.g., show a success message
			        } catch (Exception e) {
			            e.printStackTrace(); // Handle exceptions appropriately
			        }

				}
			}
		});
		
		

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		HBox checkStuff = new HBox(20, adminAccount,studentAccount,teacherAccount);
		checkStuff.setAlignment(Pos.CENTER);
	
		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, userText, noExist, checkStuff,noClick);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, addButton,removeButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

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

				if (validPassword == true) {
					password.setVisible(false); // valid
				} else {
					password.setVisible(true); // OTP is invalid
				}

				if (validPassword && databaseHelper.doesUserExist(userNameText.getText().strip())) {
					System.out.println("here2");
					// ADD USER TO LIST
					resetPasswordPage(primaryStage, userNameText.getText().trim()); // enter only if the OTP is corrent
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

					// databaseHelper.resetPassword();
					// databaseHelper.changeUserPassword();

					// After successful reset, return to the login page
					login(primaryStage);
				} else {
					mismatchPassword.setVisible(true);
				}
			}
		});

		// Layout for the reset password scene
		VBox resetLayout = new VBox(10, welcome, newPassword, newPasswordText, confirmPassword, confirmPasswordText,
				mismatchPassword, resetButton, quitButton);
		resetLayout.setAlignment(Pos.CENTER);

		Scene resetScene = new Scene(resetLayout, 400, 400);
		primaryStage.setScene(resetScene);
		primaryStage.show();
	}

	private void createArticle(Stage primaryStage, String userName) {

		// Labels and buttons
		Label welcome = new Label("Create Article");

		Label email = new Label("Title: ");
		Label firstName = new Label("Author: ");
		Label confFirstName = new Label("Abstract: ");
		Label middleName = new Label("Set of keywords: ");
		Label lastName = new Label("Body of the article: ");
		Label references = new Label("References: ");
		Label level = new Label("Level: ");
		Label Group = new Label("Group: ");

		TextField emailText = new TextField();
		TextField firstNameText = new TextField();
		TextField confFirstNameText = new TextField();
		TextField middleNameText = new TextField();
		TextField lastNameText = new TextField();
		TextField referencesText = new TextField();

		Button conButton = new Button("Confirm");
		Button quitButton = new Button("Quit");

		ChoiceBox<String> getLevel = new ChoiceBox<>();
		
		ChoiceBox<String> getGroup = new ChoiceBox<>();
		
		ArrayList<String> userGroups = null;
		try {
			userGroups = databaseHelper.getUserGroups(userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Get groups for user
        getGroup.getItems().addAll(userGroups); // Populate ChoiceBox with groups

        if (userGroups.isEmpty()) {
            getGroup.setDisable(true); // Disable ChoiceBox if no groups are available
        }
		

		getLevel.getItems().add("Beginner");
		getLevel.getItems().add("Intermediate");
		getLevel.getItems().add("Advanced");
		getLevel.getItems().add("Expert");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		email.setFont(new Font("Montserrat", 20));
		firstName.setFont(new Font("Montserrat", 20));
		confFirstName.setFont(new Font("Montserrat", 20));
		middleName.setFont(new Font("Montserrat", 20));
		lastName.setFont(new Font("Montserrat", 20));
		references.setFont(new Font("Montserrat", 20));
		Group.setFont(new Font("Montserrat", 20));
		level.setFont(new Font("Montserrat", 20));

		// Button design
		conButton.setId("buttonDesign");  // Set the ID for styling
	    quitButton.setId("buttonDesign");  // Set the ID for styling


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

				if (getLevel.getValue() == null) {
					getLevel.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				} else {
					getLevel.setStyle("-fx-border-color: black; -fx-border-width: 2px");
				}
				
				if (getGroup.getValue() == null) {
					getGroup.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				} else {
					getGroup.setStyle("-fx-border-color: black; -fx-border-width: 2px");
				}

				if (!emailText.getText().isEmpty() && !firstNameText.getText().isEmpty()
						&& !confFirstNameText.getText().isEmpty() && !middleNameText.getText().isEmpty()
						&& !lastNameText.getText().isEmpty() && !referencesText.getText().isEmpty()
						&& !databaseHelper.doesUserExist(emailText.getText()) && getLevel.getValue() != null
						&& getGroup.getValue() != null) {

					char[] title = emailText.getText().toCharArray();
					char[] author = firstNameText.getText().toCharArray();
					char[] abstract1 = confFirstNameText.getText().toCharArray();
					char[] keywords = middleNameText.getText().toCharArray();
					char[] body = lastNameText.getText().toCharArray();
					char[] references = referencesText.getText().toCharArray();
					char[] level = getLevel.getValue().toCharArray();
					char[] group = getGroup.getValue().toCharArray();
					try {
						databaseHelper1.register(title, author, abstract1, keywords, body, references, level, group);
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Top pane for welcome label
		HBox topTop = new HBox(quitButton);
		topTop.setAlignment(Pos.CENTER_RIGHT);
		topTop.setPadding(new Insets(20, 20, 0, 0));

		HBox topBottom = new HBox(welcome);
		topBottom.setAlignment(Pos.CENTER);
		topBottom.setPadding(new Insets(0, 0, 30, 0));
				
		VBox topPane = new VBox(20,topTop, topBottom);
		topPane.setAlignment(Pos.CENTER);

		// Middle VBoxs
		VBox middleOne = new VBox(firstName, middleName, email, level);
		VBox.setMargin(firstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleName, new Insets(20, 20, 20, 40));
		VBox.setMargin(email, new Insets(20, 20, 20, 40));
		VBox.setMargin(level, new Insets(20, 20, 20, 40));

		VBox middleTwo = new VBox(firstNameText, middleNameText, emailText, getLevel);
		VBox.setMargin(firstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(emailText, new Insets(20, 20, 20, 40));
		VBox.setMargin(getLevel, new Insets(20, 20, 20, 40));

		VBox middleThree = new VBox(confFirstName, lastName, references, Group);
		VBox.setMargin(confFirstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastName, new Insets(20, 20, 20, 40));
		VBox.setMargin(Group, new Insets(20, 20, 20, 40));
		VBox.setMargin(references, new Insets(20, 20, 20, 40));

		VBox middleFour = new VBox(confFirstNameText, lastNameText, referencesText, getGroup);
		VBox.setMargin(confFirstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(getGroup, new Insets(20, 20, 20, 40));
		VBox.setMargin(referencesText, new Insets(20, 20, 20, 40));

		// Combine VBoxs
		HBox middlePane = new HBox(middleOne, middleTwo, middleThree, middleFour);
		middlePane.setAlignment(Pos.TOP_CENTER);

		// Bottom pane for login button
		HBox bottomPane = new HBox(conButton);
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(0, 0, 50, 0));;

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setId("startBackground");
		adminCreateScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		
		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void listArticles(Stage primaryStage, String userName) throws Exception {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("List Articles");
		
		TextField keyWord = new TextField();
		TextField idArt = new TextField();

		Button quitButton = new Button("Quit");
		Button searchLevel = new Button("Search by Level");
		Button searchGroup = new Button("Search by group");
		Button searchKeyWord = new Button("Search by key word");
		Button openArt = new Button("Open Article (ID)");

		ChoiceBox<String> level = new ChoiceBox<>();
		ChoiceBox<String> getGroup = new ChoiceBox<>();
		
		ArrayList<String> userGroups = null;
		try {
			userGroups = databaseHelper.getUserGroups(userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Get groups for user
        getGroup.getItems().addAll(userGroups); // Populate ChoiceBox with groups

        if (userGroups.isEmpty()) {
            getGroup.setDisable(true); // Disable ChoiceBox if no groups are available
        }

		level.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert", "All");
		
		// Fetch data
        ObservableList<Article> articles = databaseHelper1.displayArticles();

        // Create TableView
        TableView<Article> articleTable = new TableView<>(articles);

        // Define columns
        TableColumn<Article, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Article, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Article, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Article, String> abstractColumn = new TableColumn<>("Abstract");
        abstractColumn.setCellValueFactory(new PropertyValueFactory<>("paperAbstract"));

        // Add columns to the table
        articleTable.getColumns().addAll(idColumn, titleColumn, authorColumn, abstractColumn);
	    
		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		
		//Button Design
		openArt.setId("buttonDesign2");
		quitButton.setId("buttonDesign");
		searchLevel.setId("buttonDesign2");
		searchGroup.setId("buttonDesign2");
		searchKeyWord.setId("buttonDesign2");

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		openArt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(!idArt.getText().trim().isEmpty()) {
					try {
						openArt(primaryStage, userName, idArt.getText().trim());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		searchLevel.setOnAction(event -> {
		    // Get the selected group from the ChoiceBox
		    String selectedLevel = level.getValue();
		    
		    if (selectedLevel != null && !selectedLevel.equalsIgnoreCase("All")) {
		        // Filter articles in-memory (if articles are preloaded in the ObservableList)
		        ObservableList<Article> filteredArticles = articles.filtered(article -> 
		        selectedLevel.equalsIgnoreCase(article.getLevel())
		        );
		        
		        // Update the TableView
		        articleTable.setItems(filteredArticles);
		    } else {
		        // If "All" is selected, display all articles
		        articleTable.setItems(articles);
		    }
		});

		searchGroup.setOnAction(event -> {
		    // Get the selected group from the ChoiceBox
		    String selectedGroup = getGroup.getValue();
		    
		    if (selectedGroup != null && !selectedGroup.equalsIgnoreCase("All")) {
		        // Filter articles in-memory (if articles are preloaded in the ObservableList)
		        ObservableList<Article> filteredArticles = articles.filtered(article -> 
		            selectedGroup.equalsIgnoreCase(article.getArticleGroup())
		        );
		        
		        // Update the TableView
		        articleTable.setItems(filteredArticles);
		    } else {
		        // If "All" is selected, display all articles
		        articleTable.setItems(articles);
		    }
		});
		
		searchKeyWord.setOnAction(event -> {
		    // Get the keyword entered by the user
		    String keyword = keyWord.getText().trim().toLowerCase();
		    
		    if (!keyword.isEmpty()) {
		        // Filter articles containing the keyword in title, author, or abstract
		        ObservableList<Article> filteredArticles = articles.filtered(article -> 
		            (article.getTitle().toLowerCase().contains(keyword) ||
		             article.getAuthor().toLowerCase().contains(keyword) ||
		             article.getPaperAbstract().toLowerCase().contains(keyword))
		        );

		        // Update the TableView
		        articleTable.setItems(filteredArticles);
		    } else {
		        // If no keyword is entered, show all articles
		        articleTable.setItems(articles);
		    }
		});
		
		VBox middle1 = new VBox(30, level, getGroup, keyWord,idArt);
		middle1.setAlignment(Pos.CENTER);

		VBox middle2 = new VBox(30, searchLevel, searchGroup, searchKeyWord,openArt);
		middle2.setAlignment(Pos.CENTER);


		// Center the elements in the VBox
		HBox middleMiddlePane1 = new HBox(40, middle1, middle2);
	    middleMiddlePane1.setAlignment(Pos.CENTER);

		VBox middleMiddlePane = new VBox(5, welcome, middleMiddlePane1);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, quitButton);
	    bottomPane.setPadding(new Insets(20, 0, 0, 0));  // Padding around the VBox
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, articleTable);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(articleTable, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setId("Background");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
	
	public void openArt(Stage primaryStage, String userName, String id) throws Exception {
		
		int idNum = Integer.parseInt(id);
		
		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Read Article");
		Label artString = new Label();
		artString.setText(databaseHelper1.getArticleByIdAndUserName(idNum, userName));
		Label noAccess = new Label("Do not have access to article");
		Label article = new Label("THE ARTICLE GOES HERE");

		Button quitButton = new Button("Quit");
		Button reqButton = new Button("Request Access");


		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		
		artString.setFont(new Font("Montserrat", 20));

		noAccess.setFont(new Font("Montserrat", 12));
		noAccess.setStyle("-fx-text-fill: red;");
		noAccess.setVisible(false);
		
		if(databaseHelper1.checkArticleByIdAndUserName(idNum, userName) == false) {
			noAccess.setVisible(true);
		}

		// Button design
		quitButton.setId("buttonDesign"); 
		reqButton.setId("buttonDesign"); 


		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, artString, noAccess);
		middleMiddlePane.setAlignment(Pos.CENTER);
		middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
		VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

		// Center the buttons in the HBox
		HBox bottomPane = new HBox(20, reqButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);

		// Left side Pane (with background image)
		StackPane leftPane = new StackPane();

		// Background image
		Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setFitWidth(450);
		backgroundImageView.setFitHeight(600);

		// Logo image
		Image logoImage = new Image("/helpSystem/img/logo.png");
		ImageView logoImageView = new ImageView(logoImage);
		logoImageView.setFitWidth(350); // Adjust the width of the logo
		logoImageView.setFitHeight(350); // Adjust the height of the logo
		logoImageView.setPreserveRatio(true);

		// Add both images to the StackPane
		leftPane.getChildren().addAll(backgroundImageView, logoImageView);

		// Position the logo in the center of the left pane
		StackPane.setAlignment(logoImageView, Pos.CENTER);

		// Right side Pane (with white background and login form)
		VBox rightPane = new VBox(40,middleMiddlePane, bottomPane);
		rightPane.setStyle("-fx-background-color: white;");
		rightPane.setPrefWidth(450);  // Set to half of the total width

		// HBox for the left and right sides
		HBox leftRight = new HBox(leftPane, rightPane);
		leftRight.setFillHeight(true);

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(welcomeScene);
		
	}

	public void deleteArticle(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete Article");
		Label noExist = new Label("Article does not exist");

		TextField usernameText = new TextField();
		usernameText.setPromptText("Username");  // Set placeholder for username
		usernameText.setPrefWidth(300);  // Set the preferred width
		usernameText.setMaxWidth(300);   

		Button quitButton = new Button("Quit");
		Button deletedButton = new Button("Delete");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setId("buttonDesign"); 
		deletedButton.setId("buttonDesign");

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	public void backupArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Backup Articles");
		Label username = new Label("File name: ");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("File name");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button deletedButton = new Button("Backup");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		username.setFont(new Font("Montserrat", 20));

		// Button design
		deletedButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");

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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20,middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	public void restoreArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Restore Articles");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("Filename");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button deletedButton = new Button("Restore");
		Button mergeButton = new Button("Merge");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		// Button design
		deletedButton.setId("buttonDesign"); 
		mergeButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");	

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
						databaseHelper1.restore(fileNameText.getText().strip()); // restores articles with old file
						//databaseHelper1.restore(fileNameText.getText().strip());

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		
		// Send button action
		mergeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					try {
						databaseHelper1.mergeData(fileNameText.getText().strip());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Merge both files
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, mergeButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20, middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
	
	public void updateArticle(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Update an Article");
		Label noExist = new Label("Article does not exist");

		TextField usernameText = new TextField();
		usernameText.setPromptText("Article Name");  // Set placeholder for username
		usernameText.setPrefWidth(300);  // Set the preferred width
		usernameText.setMaxWidth(300);   
		
		TextField bodyText = new TextField();
		bodyText.setPromptText("New Body");  // Set placeholder for username
		bodyText.setPrefWidth(300);  // Set the preferred width
		bodyText.setMaxWidth(300);   

		Button updateArticle = new Button("Update Article");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		updateArticle.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");

		// Send button action
		updateArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (databaseHelper1.doesArticleExist(usernameText.getText().strip())) {
					noExist.setVisible(false);
					try {
						databaseHelper1.updateArticleBody(usernameText.getText().strip(), bodyText.getText());
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist, bodyText);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, updateArticle, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20, middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
	
	public void createGroup(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Create Group");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("Group Name");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button deletedButton = new Button("Create Group");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		// Button design
		deletedButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					
					// TODO Create function to create group
					try {
						databaseHelper2.addArticleGroup(fileNameText.getText());
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20,middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	public void deleteGroup(Stage primaryStage, String userName) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete Group");
		Label noExist = new Label("Group does not exist");

		TextField usernameText = new TextField();
		usernameText.setPromptText("Username");  // Set placeholder for username
		usernameText.setPrefWidth(300);  // Set the preferred width
		usernameText.setMaxWidth(300);   

		Button quitButton = new Button("Quit");
		Button deletedButton = new Button("Delete");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setId("buttonDesign"); 
		deletedButton.setId("buttonDesign");

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				//TODO CHECK if GROUP EXIST THEN delete
				boolean found = false;
				try {
					found = databaseHelper2.doesGroupExist(usernameText.getText().strip());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (found) {
					try {
						databaseHelper2.deleteArticleGroup(usernameText.getText().trim().strip());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
		
	public void backupGroup(Stage primaryStage, String userName) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Backup Group");
		Label username = new Label("File name ");
		Label chooseGroup = new Label("Choose Group");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("File name");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button deletedButton = new Button("Backup");
		Button quitButton = new Button("Quit");
		
		ChoiceBox<String> getGroup = new ChoiceBox<>();
		
		//TODO FUnction to add groups to choiceBOX

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		username.setFont(new Font("Montserrat", 20));
		
		chooseGroup.setFont(new Font("Montserrat", 12));
		chooseGroup.setStyle("-fx-text-fill: red;");
		chooseGroup.setVisible(false);

		// Button design
		deletedButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");
		
		ArrayList<String> userGroups = null;
		try {
			userGroups = databaseHelper.getUserGroups(userName);
		} catch (SQLException e) {
			
			e.printStackTrace();
		} // Get groups for user
		getGroup.getItems().addAll(userGroups); // Populate ChoiceBox with groups

		if (userGroups.isEmpty()) {
			getGroup.setDisable(true); // Disable ChoiceBox if no groups are available
		}

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (getGroup.getValue() == null) 
				{
					chooseGroup.setVisible(true);
				} 
				
				else 
				{
					chooseGroup.setVisible(false);
				}
				
				
				if (fileNameText.getText().isEmpty()) 
				{
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} 
				
				else 
				{
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (getGroup.getValue() != null && !fileNameText.getText().isEmpty()) {
					
					//TODO BackUp Group
					try {
						databaseHelper2.backupArticleGroupsToFile(fileNameText.getText().strip(),getGroup.getValue());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText, getGroup, chooseGroup);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20,middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	
	public void restoreGroup(Stage primaryStage, String userName) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Restore Group");
		Label chooseGroup = new Label("Choose Group");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("Filename");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button deletedButton = new Button("Restore");
		Button mergeButton = new Button("Merge");
		Button quitButton = new Button("Quit");
		
		//ChoiceBox<String> getGroup = new ChoiceBox<>();
		
		ArrayList<String> userGroups = null;
		try {
			userGroups = databaseHelper.getUserGroups(userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Get groups for user
        //getGroup.getItems().addAll(userGroups); // Populate ChoiceBox with groups

        if (userGroups.isEmpty()) {
            //getGroup.setDisable(true); // Disable ChoiceBox if no groups are available
        }

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		
		chooseGroup.setFont(new Font("Montserrat", 12));
		chooseGroup.setStyle("-fx-text-fill: red;");
		chooseGroup.setVisible(false);

		// Button design
		deletedButton.setId("buttonDesign"); 
		mergeButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");	

		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				
				
				if (fileNameText.getText().isEmpty()) 
				{
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} 
				
				else 
				{
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (!fileNameText.getText().isEmpty()) {
					
					//TODO Restore Group
					try {
						databaseHelper2.restoreArticleGroupsFromFile(fileNameText.getText());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

			}
		});
		
		// Send button action
		mergeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				
				
				
				if (fileNameText.getText().isEmpty()) 
				{
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} 
				
				else 
				{
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (!fileNameText.getText().isEmpty()) {
					
					//TODO Merge Group
					
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText,chooseGroup);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, deletedButton, mergeButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20, middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
	
	public void manageGroup(Stage primaryStage, String userName) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Manage Group");
		Label chooseGroup = new Label("Choose Group");

		TextField fileNameText = new TextField();
		fileNameText.setPromptText("Instructor Account");  // Set placeholder for username
		fileNameText.setPrefWidth(300);  // Set the preferred width
		fileNameText.setMaxWidth(300);   

		Button addInButton = new Button("Add User");
		Button deleteInButton = new Button("Delete User");
		Button quitButton = new Button("Quit");
		
		ChoiceBox<String> getGroup = new ChoiceBox<>();

		// Label design
		welcome.setFont(new Font("Montserrat", 36));
		
		chooseGroup.setFont(new Font("Montserrat", 12));
		chooseGroup.setStyle("-fx-text-fill: red;");
		chooseGroup.setVisible(false);

		// Button design
		addInButton.setId("buttonDesign"); 
		deleteInButton.setId("buttonDesign"); 
		quitButton.setId("buttonDesign");	
		
		
		ArrayList<String> userGroups = null;
		try {
			userGroups = databaseHelper.getUserGroups(userName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Get groups for user
        getGroup.getItems().addAll(userGroups); // Populate ChoiceBox with groups

        if (userGroups.isEmpty()) {
            getGroup.setDisable(true); // Disable ChoiceBox if no groups are available
        }

		// Send button action
		addInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (getGroup.getValue() == null) 
				{
					chooseGroup.setVisible(true);
				} 
				
				else 
				{
					chooseGroup.setVisible(false);
				}
				
				
				if (fileNameText.getText().isEmpty()) 
				{
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} 
				
				else 
				{
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (getGroup.getValue() != null && !fileNameText.getText().isEmpty()) {
					
					//TODO Add Instruct into Group	
					try {
		                databaseHelper.addUserToGroup(fileNameText.getText(),getGroup.getValue());
		                Alert alert = new Alert(Alert.AlertType.INFORMATION);
		                alert.setTitle("Success");
		                alert.setHeaderText(null);
		                alert.setContentText("User added to the group successfully.");
		                alert.showAndWait();
		            } catch (SQLException e) {
		                e.printStackTrace();
		                Alert alert = new Alert(Alert.AlertType.ERROR);
		                alert.setTitle("Error");
		                alert.setHeaderText(null);
		                alert.setContentText("Failed to add the user to the group.");
		                alert.showAndWait();
		            }
					
				}

			}
		});
		
		// Send button action
		deleteInButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if (getGroup.getValue() == null) 
				{
					chooseGroup.setVisible(true);
				} 
				
				else 
				{
					chooseGroup.setVisible(false);
				}
				
				
				if (fileNameText.getText().isEmpty()) 
				{
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} 
				
				else 
				{
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (getGroup.getValue() != null && !fileNameText.getText().isEmpty()) {
					
					//TODO Delete Instruct from Group
					try {
		                databaseHelper.removeUserFromGroup(fileNameText.getText(),getGroup.getValue());
		                Alert alert = new Alert(Alert.AlertType.INFORMATION);
		                alert.setTitle("Success");
		                alert.setHeaderText(null);
		                alert.setContentText("User removed from the group successfully.");
		                alert.showAndWait();
		            } catch (SQLException e) {
		                e.printStackTrace();
		                Alert alert = new Alert(Alert.AlertType.ERROR);
		                alert.setTitle("Error");
		                alert.setHeaderText(null);
		                alert.setContentText("Failed to remove the user from the group.");
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
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, fileNameText,getGroup,chooseGroup);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, addInButton, deleteInButton, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(20, middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}

	public void sendMes(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Send Message to Admin");
		Label noExist = new Label("No input, please add question");

		TextField usernameText = new TextField();
		usernameText.setPromptText("Message");  // Set placeholder for username
		usernameText.setPrefWidth(300);  // Set the preferred width
		usernameText.setMaxWidth(300);   

		Button quitButton = new Button("Quit");
		Button sendGen = new Button("General Message");
		Button sendSpec = new Button("Specific Message");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));

		noExist.setFont(new Font("Montserrat", 12));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setId("buttonDesign"); 
		sendGen.setId("buttonDesign");
		sendSpec.setId("buttonDesign");

		// Send button action
		sendGen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(usernameText.getText().isEmpty()) {
					noExist.setVisible(true);

				}
				else {
					noExist.setVisible(false);

				}
				
			}
		});
		
		// Send button action
		sendSpec.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(usernameText.getText().isEmpty()) {
					noExist.setVisible(true);

				}
				else {
					noExist.setVisible(false);

				}
				
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, usernameText, noExist);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, sendGen,sendSpec, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}
	
	public void reviewMes(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Review Messages");
		
		Label mess = new Label("ALL MESSAGVES GO HERE");

		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Montserrat", 36));


		// Button design
		quitButton.setId("buttonDesign"); 

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					login(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Center the elements in the VBox
	    VBox middleMiddlePane = new VBox(10, welcome, mess);
	    middleMiddlePane.setAlignment(Pos.CENTER);
	    middleMiddlePane.setPadding(new Insets(50, 20, 20, 20));  // Padding around the VBox
	    VBox.setMargin(welcome, new Insets(70, 0, 50, 0));

	    // Center the buttons in the HBox
	    HBox bottomPane = new HBox(20, quitButton);
	    bottomPane.setAlignment(Pos.CENTER);

	    // Left side Pane (with background image)
	    StackPane leftPane = new StackPane();

	    // Background image
	    Image backgroundImage = new Image("/helpSystem/img/startBackground.png");
	    ImageView backgroundImageView = new ImageView(backgroundImage);
	    backgroundImageView.setFitWidth(450);
	    backgroundImageView.setFitHeight(600);

	    // Logo image
	    Image logoImage = new Image("/helpSystem/img/logo.png");
	    ImageView logoImageView = new ImageView(logoImage);
	    logoImageView.setFitWidth(350); // Adjust the width of the logo
	    logoImageView.setFitHeight(350); // Adjust the height of the logo
	    logoImageView.setPreserveRatio(true);

	    // Add both images to the StackPane
	    leftPane.getChildren().addAll(backgroundImageView, logoImageView);

	    // Position the logo in the center of the left pane
	    StackPane.setAlignment(logoImageView, Pos.CENTER);

	    // Right side Pane (with white background and login form)
	    VBox rightPane = new VBox(middleMiddlePane, bottomPane);
	    rightPane.setStyle("-fx-background-color: white;");
	    rightPane.setPrefWidth(450);  // Set to half of the total width

	    // HBox for the left and right sides
	    HBox leftRight = new HBox(leftPane, rightPane);
	    leftRight.setFillHeight(true);

	    // BorderPane layout
	    BorderPane adminCreateScreen = new BorderPane();
	    adminCreateScreen.setCenter(leftRight); // Center contains both left and right panes

	    // Set the scene
	    Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
	    welcomeScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	    primaryStage.setScene(welcomeScene);

	}


}

