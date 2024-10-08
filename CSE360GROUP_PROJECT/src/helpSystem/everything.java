package helpSystem;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.*;

public class everything extends Application {
    
    private static Map<String, User> users = new HashMap<>(); // Stores user accounts
    private static User currentUser; // The currently logged-in user
    private static User firstAdmin = null; // Tracks the first user for admin

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        if (users.isEmpty()) {
            // If no users exist, prompt for admin account creation
            showAdminCreationPage(primaryStage);
        } else {
            // Otherwise, go to login page
            showLoginPage(primaryStage);
        }
    }

    // Display the login page
    private void showLoginPage(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button btnLogin = new Button("Login");
        grid.add(btnLogin, 1, 3);

        Label statusLabel = new Label();
        grid.add(statusLabel, 1, 4);

        btnLogin.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                String username = userTextField.getText();
                String password = passwordField.getText();
                if (users.containsKey(username) && users.get(username).checkPassword(password)) {
                    currentUser = users.get(username);
                    statusLabel.setText("Login successful");

                    if (!currentUser.isSetupComplete()) {
                        showAccountSetupPage(primaryStage); // Direct user to finish setup
                    } else {
                        // If setup complete, move to role selection or home page
                        showRoleSelectionPage(primaryStage);
                    }
                } else {
                    statusLabel.setText("Invalid credentials");
                }
            }
        });

        Scene scene = new Scene(grid, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Admin creation page for the first user
    private void showAdminCreationPage(Stage primaryStage) {
        primaryStage.setTitle("Admin Account Creation");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Label confirmPasswordLabel = new Label("Confirm Password:");
        grid.add(confirmPasswordLabel, 0, 3);

        PasswordField confirmPasswordField = new PasswordField();
        grid.add(confirmPasswordField, 1, 3);

        Button btnCreate = new Button("Create Admin Account");
        grid.add(btnCreate, 1, 4);

        Label statusLabel = new Label();
        grid.add(statusLabel, 1, 5);

        btnCreate.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                String username = userTextField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                if (!password.equals(confirmPassword)) {
                    statusLabel.setText("Passwords do not match");
                } else if (users.containsKey(username)) {
                    statusLabel.setText("Username already taken");
                } else {
                    // Create the first admin user
                    User admin = new User(username, password, "Admin");
                    users.put(username, admin);
                    firstAdmin = admin;
                    currentUser = admin;
                    statusLabel.setText("Admin account created. Returning to login page.");

                    showLoginPage(primaryStage);
                }
            }
        });

        Scene scene = new Scene(grid, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Account setup page for users who need to finish setting up their account
    private void showAccountSetupPage(Stage primaryStage) {
        primaryStage.setTitle("Finish Setting Up Your Account");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 1);

        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 1);

        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 2);

        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 2);

        Label middleNameLabel = new Label("Middle Name:");
        grid.add(middleNameLabel, 0, 3);

        TextField middleNameTextField = new TextField();
        grid.add(middleNameTextField, 1, 3);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 4);

        TextField lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 4);

        Label preferredFirstNameLabel = new Label("Preferred First Name (Optional):");
        grid.add(preferredFirstNameLabel, 0, 5);

        TextField preferredFirstNameTextField = new TextField();
        grid.add(preferredFirstNameTextField, 1, 5);

        Button btnSubmit = new Button("Submit");
        grid.add(btnSubmit, 1, 6);

        btnSubmit.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                currentUser.setEmail(emailTextField.getText());
                currentUser.setFirstName(firstNameTextField.getText());
                currentUser.setMiddleName(middleNameTextField.getText());
                currentUser.setLastName(lastNameTextField.getText());
                currentUser.setPreferredFirstName(preferredFirstNameTextField.getText());

                currentUser.setSetupComplete(true);
                showRoleSelectionPage(primaryStage);
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Role selection page for users with multiple roles
    private void showRoleSelectionPage(Stage primaryStage) {
        primaryStage.setTitle("Select Role");

        List<String> roles = currentUser.getRoles();

        if (roles.size() == 1) {
            // If the user has only one role, go directly to the home page for that role
            showHomePage(primaryStage, roles.get(0));
        } else {
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);

            Label roleLabel = new Label("Select your role:");
            grid.add(roleLabel, 0, 1);

            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.getItems().addAll(roles);
            grid.add(roleComboBox, 1, 1);

            Button btnSelect = new Button("Select");
            grid.add(btnSelect, 1, 2);

            btnSelect.setOnAction(new EventHandler<>() {
                @Override
                public void handle(ActionEvent event) {
                    String selectedRole = roleComboBox.getValue();
                    showHomePage(primaryStage, selectedRole);
                }
            });

            Scene scene = new Scene(grid, 300, 250);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    // Home page for different roles
    private void showHomePage(Stage primaryStage, String role) {
        primaryStage.setTitle(role + " Home Page");

        StackPane root = new StackPane();
        Button btnLogout = new Button("Log out");

        btnLogout.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                showLoginPage(primaryStage); // Redirect to login page
            }
        });

        root.getChildren().add(btnLogout);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    // User class to manage accounts
    private static class User {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String middleName;
        private String lastName;
        private String preferredFirstName;
        private List<String> roles;
        private boolean setupComplete;

        public User(String username, String password, String... roles) {
            this.username = username;
            this.password = password;
            this.roles = new ArrayList<>(Arrays.asList(roles));
            this.setupComplete = false;
        }

        public boolean checkPassword(String password) {
            return this.password.equals(password);
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setPreferredFirstName(String preferredFirstName) {
            this.preferredFirstName = preferredFirstName;
        }

        public void setSetupComplete(boolean setupComplete) {
            this.setupComplete = setupComplete;
        }

        public boolean isSetupComplete() {
            return setupComplete;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void addRole(String role) {
            if (!roles.contains(role)) {
                roles.add(role);
            }
        }

        public void removeRole(String role) {
            roles.remove(role);
        }
    }
}
