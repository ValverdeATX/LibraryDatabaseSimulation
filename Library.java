package src;
import java.time.LocalDate;
import java.util.Scanner;

public class Library {
    //A class that is intended to control library aspects
    //the items will be held in a txt file with their type and info

    public void enterLibrary(String username) {
        try (Scanner input = new Scanner(System.in)) {
            boolean flag = false;
            NonCheckOut nonCheck = new NonCheckOut();
            CheckOut check = new CheckOut();

            while(flag == false) {
                //The following segment generates a menu for the player to choose from.
                //put in while loop
                System.out.println("\n"+"What would you like to do?");
                System.out.println("Please select one of the following options:");
                System.out.println("  1. Look at items to check out." + "\n" +
                        "  2. Look at items that can not be check out." + "\n" +
                        "  3. Calculate items checked out." + "\n" +
                        "  4. See due dates." + "\n" +
                        "  5. See current fines." + "\n" +
                        "  9. Exit.");
                System.out.println("Your choice:");


                String userInput = input.nextLine();

                try {
                    int userChoice = Integer.parseInt(userInput);
                    if(userChoice == 1){
                        //send to check out items (books, audio/video)
                        //The format of the CheckOutItem file is:
                        //ItemID:ItemType(book/audio/video):ItemName:ItemValue:ItemBestSeller(0/1):CheckedOut(0/1):CurrentReader:DueDate:requested(0/1):renewed(0/1)?
                        check.checkOutMenu(username);
                    }
                    else if(userChoice == 2){
                        //Scroll through non check out items (refrence books, magazines)
                        //The format of the CheckOutItem file is:
                        //ID:type:title:
                        nonCheck.displayNonCheckOut();
                    }
                    else if(userChoice == 3){
                        //see how many items are checked out by the user.
                        //either direct connection to user database to send back how
                        //many items are checked out or done through checkOut class?
                        //System.out.println("Option 3");

                        //Currently, the value will be whatever is edited manually on file,
                        //until something is added to CheckOut (or similar) to increment the
                        //counter on userDatabase.txt everytime the user checks out an item.

                        String numOfBooks = userDatabase.getNumOfBooksCheckedOut(username);
                        System.out.println("The amount of items you currently have checked " +
                                "out is: " + numOfBooks);
                    }
                    else if(userChoice == 4){
                        //see the current due dates for checked out items by the user.
                        //either direct connection to user database to send back due
                        //dates or done through checkOut class?
                        //System.out.println("Option 4");
                        LocalDate bookDue = userFeeCalculator.getDateOnFile(username);
                        String itemName = userFeeCalculator.getNameOnFile(username);
                        System.out.println("The due date for " + itemName + " is " + bookDue);
                    }
                    else if(userChoice == 5){
                        double bookFeeTest = userFeeCalculator.calculateDifferenceFee(username);
                        String itemName = userFeeCalculator.getNameOnFile(username);
                        userFeeCalculator.writeUserFee(username, bookFeeTest);
                        System.out.format("Your overdue fee for " + itemName + " is: $%.2f", bookFeeTest);
                        System.out.println();
                    }
                    else if(userChoice == 9){
                        //exit program
                        flag = true;
                        System.out.println("Happy reading!");
                    }
                } catch (Exception e) {
                    System.out.println("Sorry, that option is currently not available. Please try again.");
                }

            }
        }


    }

}