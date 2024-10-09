package helpSystem;

public class LinkedList {
    protected Node head;
    private int size;

    public LinkedList() {
        this.head = null;
    }

    // Method to add a node at the end of the list
    public void add(String username, String password) {
        Node newNode = new Node(username, password);

        if (head == null) {
            head = newNode;
            size++;
        } 
        
        else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            
            current.next = newNode;
            size++;
        }
        
    }

    // Method to display the linked list
    public void display() {
        if (head == null) {
            System.out.println("The list is empty.");
            return;
        }

        Node current = head;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
        System.out.println(size);
    }

    // Method to search for a node by username
    public Node searchByUsername(String username) {
        Node current = head;
        while (current != null) {
            if (current.username.equals(username)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }
}