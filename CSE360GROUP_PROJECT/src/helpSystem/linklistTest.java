package helpSystem;

public class linklistTest extends LinkedList {

    public static void main(String[] args) {
        linklistTest userList = new linklistTest();

        // Adding users
        userList.add("user1", "password1");
        userList.add("user2", "password2");
        userList.add("user3", "password3");

        // Display the linked list
        System.out.println("Displaying the user list:");
        userList.display();

        // Searching for a user by username
        String searchUsername = "user2";
        Node result = userList.searchByUsername(searchUsername);
        
        if (result != null) {
            System.out.println("\nFound user:");
            System.out.println(result);
        } else {
            System.out.println("\nUser " + searchUsername + " not found.");
        }
    }
}
