package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class userFeeCalculator {
    private static final String file_path1 = "userDatabase.txt";
    private static final String file_path2 = "checkedOutItems.txt";

    //Accepts the user's username in order to search through
    //checkedOutItems.txt with it and return the book they have
    //rented. Also reads in the itemValue and itemDueDate parameters
    //from checkedOutItems.txt, and then uses those numbers to
    //calculate the user fee.
    public static double calculateDifferenceFee(String username) {
        LocalDate bookDate = getDateOnFile(username);
        String itemValue = getValueOnFile(username);
        double itemPrice = Double.parseDouble(itemValue);

        if(bookDate != null) {
            LocalDate currDate = LocalDate.now();
            int daysDifference = (int) (currDate.toEpochDay() - bookDate.toEpochDay());
            double result = 0.10 * daysDifference;
            //If 'result' is greater than ItemPrice, then it result defaults to ItemPrice value
            if(result <= 0) {
                result = 0;
            }
            else if(result >= itemPrice) {
                result = itemPrice;
            }
            double newResult = feeConverter(result);
            return newResult;
        }
        return -1;
    }

    //Fixes feeValue decimal space so that we don't end up writing
    //values like 1.20000000004909 on the file.
    public static double feeConverter(double feeValue) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String valueFormat = decimalFormat.format(feeValue);
        double newValue = Double.parseDouble(valueFormat);
        return newValue;
    }

    //Gets the value of the book from checkedOutItems.txt
    //In order to verify later on if the fee exceeds the item value
    private static String getValueOnFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path2))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currUsername = userData[6];
                    if (currUsername.equals(username)) {
                        String bookValue = userData[3];
                        //Print below is just to test the correct value has been returned from file.
                        //System.out.println(bookValue);
                        return bookValue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data (value).");
        }
        return null;
    }

    //Grabs the item's due date on the checkedOutItems.txt file
    //And returns it to calculateDifferenceFee
    public static LocalDate getDateOnFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path2))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currUsername = userData[6];
                    if (currUsername.equals(username)) {
                        String rentDate = userData[7];
                        //String bookName = userData[2];
                        //System.out.println(bookName);
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        return LocalDate.parse(rentDate, dateFormat);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data (date).");
        }
        return null;
    }


    public static String getNameOnFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path2))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 10) {
                    String currUsername = userData[6];
                    if (currUsername.equals(username)) {
                        String bookName = userData[2];
                        return bookName;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data (date).");
        }
        return null;
    }

    //Writes the user's fee to their respective user line on the
    //userDatabase.txt file
    public static boolean writeUserFee(String username, double feeValue) {
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(file_path1))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                lines.add(currLine);
            }
        } catch (IOException e) {
            System.out.println("Unable to read through file.");
        }
        boolean updateFee = false;
        for (int i = 0; i<lines.size(); i++) {
            String[] userData = lines.get(i).split(":");
            if (userData.length == 8 && userData[1].equals(username)) {
                userData[7] = Double.toString(feeValue);
                lines.set(i, String.join(":", userData));
                updateFee = true;
                break;
            }
        }

        if(updateFee != true) {
            return false;
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file_path1))) {
            for (String currLine:lines) {
                writer.write(currLine + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Unable to write user fee to file.");
            return false;
        }
        return true;
    }
}