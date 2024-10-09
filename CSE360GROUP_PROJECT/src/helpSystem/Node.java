package helpSystem;

public class Node {
    String username;
    String password;
    Node next;

    public Node(String username, String password) {
        this.username = username;
        this.password = password;
        this.next = null;
    }

    @Override
    public String toString() {
        return "Username: " + username + ", Password: " + password;
    }
}
