package src;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class userDatabase {
    //A class meant to simulate a database that stores
    //user information on a text file for log in purposes.

    private static final String file_path = "userDatabase.txt";

    public static void generateDatabase() {
        try {
            FileWriter fileWriter = new FileWriter(file_path, true);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to create the user database text file.");
        }
    }

    public static boolean userLogin(String username, String password) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                //Order for writing to file is:
                //ID, username, password, address, phoneNumber, age, numOfBooksCheckedOut, overdueFee
                if (userData.length == 8) {
                    String currUsername = userData[1];
                    String currPassword = userData[2];
                    if (currUsername.equals(username) && currPassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to locate the user on the text file.");
        }
        return false;
    }

    public static void userRegister(String username, String password, String address, String phoneNumber, String age) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file_path, true))) {
            int currID = getNextID();
            int noOfBooksCheckedOut = 0;
            double overdueFees = 0.0;
            String signingUpUser = currID + ":" + username + ":" + password + ":" +  address + ":" + phoneNumber + ":" + age + ":" +
                    noOfBooksCheckedOut + ":" + overdueFees + System.lineSeparator();
            writer.write(signingUpUser);
        } catch (IOException e) {
            System.out.println("Unable to register the user on the text file.");
        }
    }

    public static String getNumOfBooksCheckedOut(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 8) {
                    String currUsername = userData[1];
                    if (currUsername.equals(username)) {
                        String numOfBooks  = userData[6];
                        return numOfBooks;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data (value).");
        }
        return null;
    }

    public static String getAge(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while ((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if (userData.length == 8) {
                    String currUsername = userData[1];
                    if (currUsername.equals(username)) {
                        String age  = userData[5];
                        return age;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to view file data (value).");
        }
        return null;
    }

    public static void updateAmountCheckedOut(String username) {
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
            if (userData.length == 8 && userData[1].equals(username)) {
                int j = Integer.parseInt(userData[6]);
                j = j+1;
                userData[6] = Integer.toString(j);
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

    }

    private static int getNextID() {
        int nextID = 1;

        try(BufferedReader reader = new BufferedReader(new FileReader(file_path))) {
            String currLine;
            while((currLine = reader.readLine()) != null) {
                String[] userData = currLine.split(":");
                if(userData.length > 0) {
                    int ID = Integer.parseInt(userData[0]);
                    nextID = Math.max(nextID, ID + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to generate an ID for the current user.");
        }
        return nextID;
    }


}