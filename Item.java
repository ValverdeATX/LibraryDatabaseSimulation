package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Item {
//superclass for checkout and non checkout items.
//will have a display function for both objects.

    public void displayFile(String file) {
        // loops through the given file to display it
        try {
            File myObj = new File(file);
            Scanner myReader = new Scanner(myObj);

            System.out.println("\n");

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int firstColonIndex = data.indexOf(':');
                if (firstColonIndex != -1) {
                    int secondColonIndex = data.indexOf(':', firstColonIndex + 1);
                    int thirdColonIndex = data.indexOf(':', secondColonIndex + 1);
                    if (secondColonIndex != -1 && thirdColonIndex != -1) {
                        String partUpToThirdColon = data.substring(0, thirdColonIndex + 1);
                        System.out.println(partUpToThirdColon);
                    }
                }
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }
}