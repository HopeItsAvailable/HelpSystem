//package helpSystem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class linklistTest extends LinkedList {
//    public static void main(String[] args) {
//        // Test cases for LinkedList and Node classes
//        LinkedList list = new LinkedList();
//
//        // Add users
//        list.add("user1", "password1");
//        list.add("user2", "password2");
//        list.add("user3", "password3");
//
//        // Display the list after adding users
//        System.out.println("Display after adding users:"+"\n");
//        System.out.println(list.display());
//        System.out.println("\n");
//
//        // Finalize user's information
//        list.finalizeUserEmail("user1", "user1@example.com");
//        list.finalizeUserFirstName("user1", "John");
//        list.finalizeUserMiddleName("user1", "M");
//        list.finalizeUserLastName("user1", "Doe");
//        list.finalizeUserPreferredFirstName("user1", "Johnny");
//
//        // Add roles to user
//        list.addRoleToUser("user1", "admin");
//        list.addRoleToUser("user2", "student");
//
//        // Display the list after finalizing information and adding roles
//        System.out.println("Display after finalizing user1 and adding roles:");
//        System.out.println(list.display());
//
//        // Check if user is admin
//        System.out.println("Is user1 an admin? " + list.isAdmin("user1"));
//        System.out.println("Is user2 a student? " + list.isStudent("user2"));
//
//        // Test password validation with oneTimePasswordGeneratorList
//        oneTimePasswordGeneratorList otpList = new oneTimePasswordGeneratorList();
//        otpList.addPassword("OTP123", new String[]{"admin"}, System.currentTimeMillis() + 5000); // Expires in 5 seconds
//
//        // Validate OTP before expiration
//        System.out.println("Validating OTP 'OTP123': " + otpList.validatePassword("OTP123"));
//        System.out.println("Roles for OTP 'OTP123': " + String.join(", ", otpList.validatePassword1("OTP123")));
//
//        // Sleep for 6 seconds to let OTP expire
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Validate OTP after expiration
//        System.out.println("Validating expired OTP 'OTP123': " + otpList.validatePassword("OTP123"));
//    }
//}
package unused;

