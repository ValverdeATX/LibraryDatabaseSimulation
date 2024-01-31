package src;

public class NonCheckOut extends Item {
    //class that extends item class.
    //will simply just display the noncheckoutitems.txt

    public void displayNonCheckOut() {
        final String file_path = "NonCheckOutItems.txt";
        //ask item superclass to display file
        displayFile(file_path);
    }

}