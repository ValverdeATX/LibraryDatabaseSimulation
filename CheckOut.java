package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CheckOut extends Item {
    //class that extends item class.
    //display the checkoutitems.txt
    //see if item is checked out
    //request item
    private final String file_path = "checkedOutItems.txt";
    Scanner input = new Scanner(System.in);

    public void checkOutMenu(String username) {
        boolean flag = false;


        while (flag == false) {
            System.out.println("\n"+"What would you like to do?");
            System.out.println("Please select one of the following options:");
            System.out.println("  1. Show items that can be checked out." + "\n" +
                    "  2. Check out item by item ID number." + "\n" +
                    "  3. Renew item." + "\n" +
                    "  9. Exit.");
            System.out.println("Your choice:");

            String userInput = input.nextLine();
            int userChoice = Integer.parseInt(userInput);

            if(userChoice == 1){
                //display the ID, type, and name
                displayCheckOut();
            }
            else if(userChoice == 2){
                //check age first
                checkItem(username);
            }
            else if(userChoice == 3){
                //check to see if it can be renewed first
                renewItem(username);
            }
            else if(userChoice == 9){
                //sends back to library
                flag = true;
            }
        }
    }

    public void displayCheckOut() {
        displayFile(file_path);
    }

    public void checkItem(String username) {
        //checks age first then how many books are checked out
        //if the user is 12 or under.
        //converts string to int but maybe change getAge function
        //in userdatabase to just send int?
        String age = userDatabase.getAge(username);
        int a = Integer.parseInt(age);
        if(a <= 12)
        {
            String numOfBooks = userDatabase.getNumOfBooksCheckedOut(username);
            int b = Integer.parseInt(numOfBooks);
            if(b >= 5)
            {
                System.out.println("\n" + "Sorry, people 12 and under can only check out up to 5 items at a time.");
                return;
            }
        }

        //check if book is already checked out return true or false
        System.out.println("\n" + "Please enter the ID number of the item you would like to check out.");
        System.out.println("Your choice:");
        String userchoice = input.nextLine();

        boolean is = inStock(userchoice);
        if(is == false)
        {
            System.out.println("\n" + "Sorry, that item is already checked out."
                    + "\n" + "Would you like to place a request for the item?");
            System.out.println("  1. Yes." + "\n" +
                    "  2. No.");
            System.out.println("Your choice:");
            String rchoice = input.nextLine();
            int userRChoice = Integer.parseInt(rchoice);
            if(userRChoice == 1){
                request(userchoice);
                System.out.println("Your request has been logged.");
            }else{
                return;
            }
        }else{
            //check if book is best seller and if audio/video return true or false
            boolean bs = checkBestSeller(userchoice);
            boolean av = checkAudioOrVideo(userchoice);
            //System.out.println(av);

            //calculate due date and change due date in checkedOutItems.txt
            String due = dueDate(bs, av, userchoice, username);

            System.out.println("\n" + "Your due date is: " + due);

            userDatabase.updateAmountCheckedOut(username);

        }


    }

    public void renewItem(String username) {
        System.out.println("\n" + "Please enter the ID number of the item you would like to renew.");
        System.out.println("Your choice:");
        String userchoice = input.nextLine();

        boolean requested = checkIfRequested(userchoice);
        boolean renewed = checkIfRenewed(userchoice);

        if(requested == true)
        {
            System.out.println("\n" + "Sorry, that item has been requested.");
            return;
        }
        else if(renewed == true)
        {
            System.out.println("\n" + "Sorry, items can only be renewed once.");
            return;
        }
        else
        {
            //check if book is best seller and if audio/video return true or false
            boolean bs = checkBestSeller(userchoice);
            boolean av = checkAudioOrVideo(userchoice);
            renew(userchoice);

            //calculate due date and change due date in checkedOutItems.txt
            String due = dueDate(bs, av, userchoice, username);

            System.out.println("\n" + "Your new due date is: " + due);

            userDatabase.updateAmountCheckedOut(username);
        }
    }

    public boolean inStock(String userChoice)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currID = userData[0];
                    if (currID.equals(userChoice)) {
                        String is  = userData[6];
                        if(is.equals("0"))
                            return true;
                        else
                            return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data.");
        }
        return false;
    }

    public boolean checkBestSeller(String userChoice)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currID = userData[0];
                    if (currID.equals(userChoice)) {
                        String bs  = userData[4];
                        if(bs.equals("1"))
                            return true;
                        else
                            return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data.");
        }
        return false;
    }

    public boolean checkAudioOrVideo(String userChoice)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currID = userData[0];
                    if (currID.equals(userChoice)) {
                        String av  = userData[1];
                        if(av.equals("Audio") || av.equals("Video"))
                            return true;
                        else
                            return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data.");
        }
        return false;
    }

    public String dueDate(boolean bs, boolean av, String userChoice, String username)
    {
        // Get today's date
        LocalDate today = LocalDate.now();
        LocalDate due;

        if(bs == true || av == true)
        {
            // Add 2 weeks to today's date
            due = today.plus(2, ChronoUnit.WEEKS);
        }
        else
        {
            // Add 3 weeks to today's date
            due = today.plus(3, ChronoUnit.WEEKS);
        }
        String updatedDue = due.toString();

        //write to txt to change the checked out name and due date
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                lines.add(currLine);
            }
        } catch (IOException e) {
            System.out.println("Unable to read through file.");
        }
        for (int i = 0; i<lines.size(); i++) {
            String[] userData = lines.get(i).split(":");
            if (userData.length == 10 && userData[0].equals(userChoice)) {
                userData[6] = username;
                userData[5] = "1";
                userData[7] = updatedDue;
                lines.set(i, String.join(":", userData));
                break;
            }
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file_path))) {
            for (String currLine:lines) {
                writer.write(currLine + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Unable to write user fee to file.");
        }
        return updatedDue;
    }

    public void request(String userChoice)
    {
        List<String> lines = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                lines.add(currLine);
            }
        } catch (IOException e) {
            System.out.println("Unable to read through file.");
        }
        for (int i = 0; i<lines.size(); i++) {
            String[] userData = lines.get(i).split(":");
            if (userData.length == 10 && userData[0].equals(userChoice)) {
                userData[8] = "1";
                lines.set(i, String.join(":", userData));
                break;
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file_path))) {
            for (String currLine:lines) {
                writer.write(currLine + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Unable to write request to file.");
        }

    }

    public boolean checkIfRequested(String userChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currID = userData[0];
                    if (currID.equals(userChoice)) {
                        String r  = userData[8];
                        if(r.equals("1"))
                            return true;
                        else
                            return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data.");
        }
        return false;
    }

    public boolean checkIfRenewed(String userChoice) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currID = userData[0];
                    if (currID.equals(userChoice)) {
                        String r  = userData[9];
                        if(r.equals("1"))
                            return true;
                        else
                            return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data.");
        }
        return false;
    }

    public void renew(String userChoice)
    {
        List<String> lines = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                lines.add(currLine);
            }
        } catch (IOException e) {
            System.out.println("Unable to read through file.");
        }
        for (int i = 0; i<lines.size(); i++) {
            String[] userData = lines.get(i).split(":");
            if (userData.length == 10 && userData[0].equals(userChoice)) {
                userData[9] = "1";
                lines.set(i, String.join(":", userData));
                break;
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file_path))) {
            for (String currLine:lines) {
                writer.write(currLine + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Unable to write request to file.");
        }
    }
}