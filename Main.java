package src;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //This generates a .txt file containing the 'database'.
        //The text file name: userDatabase.txt
        //The format of the data on the txt file is:
        //username:password:userID:address:phoneNumber:userAge:noOfBooksCheckedOut:overdueFee
        userDatabase.generateDatabase();
        Library lib = new Library();

        //The following segment generates a menu for the player to choose from.
        System.out.println("Hello and welcome to the Texas State library.");
        System.out.println("Please select one of the following options:");
        System.out.println("  1. Log-in with your account." + "\n" +
                "  2. Register a new account." + "\n" +
                "  9. Exit.");

        System.out.println("Your choice:");
        try (Scanner input = new Scanner(System.in)) {
            String userInput = input.nextLine();

            try {
                int userChoice = Integer.parseInt(userInput);
                if(userChoice == 1) {
                    //Redirect to next set of choices, where they log in.
                    System.out.println("Please insert your username:");
                    String username = input.nextLine();
                    System.out.println("Please insert your password:");
                    String password = input.nextLine();

                    if(userDatabase.userLogin(username, password)) {
                        System.out.println("Welcome, " + username + "!");
                        //call library to choose a book/movie then check out?
                        lib.enterLibrary(username);
                    }
                    else {
                        System.out.println("Wrong username or password. Please try again.");
                    }
                }
                else if(userChoice == 2) {
                    //Redirect to next set of choices, where they make an account.
                    System.out.println("Please enter your new username:");
                    String username = input.nextLine();
                    System.out.println("Please enter your new password:");
                    String password = input.nextLine();
                    System.out.println("Please enter your address:");
                    String address = input.nextLine();
                    System.out.println("Please enter your phone number:");
                    String phoneNumber = input.nextLine();
                    System.out.println("Please enter your age:");
                    String age = input.nextLine();
                    userDatabase.userRegister(username, password, address, phoneNumber, age);

                    System.out.println("Thanks for signing up!");
                    System.out.println("Would you like to look through our library, or is that all for you today?");
                    System.out.println("  1. Look through library." + "\n" + "  2. Exit.");
                    //Option 1 = Move to library
                    //Option 2 = Exit program
                    System.out.println("Your choice:");
                    String registerInput = input.nextLine();
                    try {
                        int registerChoice = Integer.parseInt(registerInput);
                        if(registerChoice == 1) {
                            //call library to choose a book/movie then check out?
                            System.out.println("Moving you to our library...");
                            lib.enterLibrary(username);
                        }
                        else {
                            System.out.println("Your account has been created. We look forward to seeing you again!");
                            System.exit(0);
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Your account has been created. We look forward to seeing you again!");
                    }
                }
                else if(userChoice == 9) {
                    //Exit the application.
                    System.out.println("Happy reading!");
                    System.exit(0);
                }
                else {
                    System.out.println("Sorry, that option is currently not available. Please try again.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Sorry, that was not a valid option. Please try again.");
            }
        }
    }
}