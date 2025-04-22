
package book.store;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStore {

    public static void main(String[] args) {
        Main_Program();
    }

    // Main Function of the program to decide either current role is admin or user.
    public static void Main_Program() {
        // Create Scanner Object here 
        Scanner scanner = new Scanner(System.in);

        // Infinite loop for correct entering of role 
        while (true) {
            System.out.print("1: For Admin");
            System.out.println();
            System.out.print("2: For User");
            System.out.println();
            System.out.println();
            // Taking input from user to select role and redirect to the admin application or user application 
            int userRole = scanner.nextInt();
            switch (userRole) {
                case 1 ->
                    Admin_Application(scanner);
                case 2 ->
                    User_Application(scanner);
                default ->
                    Main_Program();
            }

        }
    }

    // Admin Function 
    public static void Admin_Application(Scanner scanner) {
        // Admin File Path define admin_data is file name and .dat is extension 
        String adminFileName = "admin_data.dat";
        // Generic function for reading user data
        String[][] adminList = Read_File(adminFileName);
        int counter = 0;

        Admin_Login(scanner, adminList, counter);
    }

    public static void Admin_Login(Scanner scanner, String[][] adminList, int counter) {

        System.out.print("Enter your username or email address : ");
        String emailORUsername = scanner.next();

        System.out.print("Enter your password : ");
        String password = scanner.next();

        String bookListFilePath = "book_data.dat";

        if (counter > 2) {
            System.err.println("Authentication Failure... Try After sometime");
            //Program Terminate 
            System.exit(0);
        } else {
            String[] getAdmin = Is_Valid_User_Credients(adminList, emailORUsername, password);
            if (getAdmin == null) {
                Admin_Login(scanner, adminList, ++counter);
            } else {
                System.out.println("***********************************************************************");
                System.out.println("*                  Welcome to Online Book Store                       *");
                System.out.println("***********************************************************************");

                Admin_Functionality(scanner, bookListFilePath);
            }
        }
    }

    public static void User_Application(Scanner scanner) {

        String userFileName = "user_data.dat";
        // Read existing user data if the file is not empty
        String[][] userList = Read_File(userFileName);
        System.out.print("For Already Existing User Press 1 : ");
        System.out.println();
        System.out.print("For New User Press 2 : ");
        System.out.println();
        boolean isAlreadyExisting = scanner.nextInt() == 1;
        if (isAlreadyExisting) {
            User_Login(scanner, userList, userFileName);
        } else {
            User_SignUp(scanner, userList, userFileName);
        }

    }

    public static void User_Login(Scanner scanner, String[][] userList, String userFileName) {

        System.out.print("Enter your username or email : ");
        String usernameOREmail = scanner.next();
        System.out.print("Enter your password : ");
        String password = scanner.next();

        String[] getUser = Is_Valid_User_Credients(userList, usernameOREmail, password);

        if (getUser == null) {
            System.out.println("Wrong User Credients....");
            System.out.println("Enter 0 to terminate the application");
            System.out.println("Enter 1 to Creating Account");
            System.out.println("Enter any key to try again....");
            int option = scanner.nextInt();
            switch (option) {
                case 0:
                    System.exit(0);
                case 1:
                    User_SignUp(scanner, userList, userFileName);
                    break;
                default:
                    User_Login(scanner, userList, userFileName);
                    break;
            }
        } else {
            String bookListFilePath = "book_data.dat";
            User_Functionailty(scanner, bookListFilePath, getUser);
        }

    }

    public static String[] Is_Valid_User_Credients(String[][] Array, String userNameOrEmail, String password) {

        int userNameIndex = 2;
        int emailAddressIndex = 3;
        for (String[] Array1 : Array) {
            if (Array1[userNameIndex].equals(userNameOrEmail) || Array1[emailAddressIndex].equals(userNameOrEmail)) {
                if (Array1[5].equals(password)) {
                    return Array1;
                }
            }
        }
        return null;
    }

    public static void User_Functionailty(Scanner scanner, String bookListFilePath, String[] UserData) {
        String[][] bookList = Read_File(bookListFilePath);
        System.out.println();
        System.out.println("1: For books view                        ");
        System.out.println("2: For books view & apply filter         ");
        System.out.println("3: For books view & Search by Boook name ");
        System.out.println("4: For books buying                      ");
        System.out.println("5: For logout...                         ");
        System.out.println();
        int option = scanner.nextInt();

        switch (option) {
            case 1 -> {
                Print_Book_List(bookList);
                User_Functionailty(scanner, bookListFilePath, UserData);
            }
            case 2 -> {
                View_Book_By_Filters(scanner, bookList);
                User_Functionailty(scanner, bookListFilePath, UserData);
            }
            case 3 -> {
                System.out.println("Enter the title of Book : ");
                String bookTitle = scanner.nextLine();
                String[] foundBook = View_Book_By_Search_Name(bookTitle, bookList);
                if (foundBook == null) {
                    System.out.println("Book doesn't exist !");
                } else {
                    System.out.println("Title: " + foundBook[0] + ", Author: " + foundBook[1]
                            + ", Quantities: " + foundBook[2] + ", Price: " + foundBook[3] + ", Discount: " + foundBook[4] + " %");
                }
                User_Functionailty(scanner, bookListFilePath, UserData);
            }
            case 4 -> {
                Buying_Books(scanner, bookListFilePath, UserData);
                User_Functionailty(scanner, bookListFilePath, UserData);
            }
            default ->
                System.exit(0);

        }

    }

    public static void Buying_Books(Scanner scanner, String bookListFilePath, String[] userData) {
        String[][] bookList = Read_File(bookListFilePath);

        String transcationRecord = "transcation_Record.dat";

        System.out.println();

        String[][] userBuyingList = new String[0][];
        int isBuyingMore = 1;

        while (isBuyingMore != 0) {
            System.out.println("Enter Book Name To Buy :   ");
            String bookName = scanner.nextLine();
            bookName = scanner.nextLine();
            System.out.println("Enter Number Of Quantities :");
            int numberOfBooks = scanner.nextInt();

            if (userBuyingList == null) {
                userBuyingList = new String[0][];
            }

            userBuyingList = Add_To_User_Buy_List(bookList, userData,
                    userBuyingList, bookName,
                    numberOfBooks
            );

            System.out.println();
            System.out.println("Enter Zero To Checkout : ");
            isBuyingMore = scanner.nextInt();
        }
        Print_User_Buying_List(true, userBuyingList);
        Write_File(bookList, bookListFilePath);
        Read_And_Write_Transaction(userBuyingList, transcationRecord);

    }

    public static void Read_And_Write_Transaction(String[][] newData, String filePath) {

        String[][] data = Read_File(filePath);

        if (data == null) {
            data = new String[0][0];
        }

        String[][] updatedData = mergeArrays(data, newData);

        Write_File(updatedData, filePath);
    }

    // Function for merging 2d arrays
    public static String[][] mergeArrays(String[][] array1, String[][] array2) {
        int length1 = array1.length;
        int length2 = array2.length;
        String[][] mergedArray = new String[length1 + length2][];

        // Copy data from array1 to mergedArray
        for (int i = 0; i < length1; i++) {
            mergedArray[i] = new String[array1[i].length];
            for (int j = 0; j < array1[i].length; j++) {
                mergedArray[i][j] = array1[i][j];
            }
        }

        // Copy data from array2 to mergedArray
        for (int i = 0; i < length2; i++) {
            mergedArray[length1 + i] = new String[array2[i].length];
            for (int j = 0; j < array2[i].length; j++) {
                mergedArray[length1 + i][j] = array2[i][j];
            }
        }

        return mergedArray;
    }

    public static void Print_User_Buying_List(boolean isUser, String[][] userBuyingList) {
        if (userBuyingList == null || userBuyingList.length == 0 || userBuyingList[0] == null || userBuyingList[0].length == 0) {
            System.out.println("User Buying List is empty.");
            return;
        }
        System.out.println();
        System.out.println("User Buying List:");
        System.out.println();
        System.out.printf(isUser ? " %-25s %-25s %-25s %-25s %-25s\n"
                : "%-25s %-25s %-25s %-25s %-25s %-25s %-25s\n",
                "Book Title", "Author", "Quantity", "Book Price", "Total Price",
                "Username", "Phone Number");

        for (String[] userBuyInfo : userBuyingList) {
            System.out.printf(isUser ? " %-25s %-25s %-25s %-25s %-25s\n"
                    : " %-25s %-25s %-25s %-25s %-25s %-25s %-25s\n",
                    userBuyInfo[3], userBuyInfo[4],
                    userBuyInfo[5], userBuyInfo[6], userBuyInfo[7],
                    userBuyInfo[0], userBuyInfo[2]);
        }

        System.out.println();

        double totalBillPrice = 0;
        for (String[] books : userBuyingList) {
            totalBillPrice += Double.parseDouble(books[7]);
        }

        String lineOfSpaces = String.format("%100s", " ");
        System.out.printf("%s Total Bill: %-25.2f%n", lineOfSpaces, totalBillPrice);
        System.out.println();
    }

    public static String[][] Add_To_User_Buy_List(String[][] bookList, String[] userData, String[][] userBuyingList, String bookName, int numberOfBooks) {
        String[] foundBook = View_Book_By_Search_Name(bookName, bookList);
        int indexOfFoundBook = Found_Book_Index(bookName, bookList);
        if (foundBook == null) {
            System.out.println("Book doesn't exist!");
            return userBuyingList;
        }
        if (Integer.parseInt(foundBook[2]) < numberOfBooks) {
            System.out.println("Please Enter Less Quantities.");
            return userBuyingList;
        }

        userBuyingList = Add_Books_To_Buyer_Buying(foundBook, userBuyingList, numberOfBooks, userData); // Add the book to the user's buying list
        Decrease_Quanity_Of_Book(indexOfFoundBook, numberOfBooks, bookList); // Decrease the quantity from the store's inventory

        return userBuyingList;
    }

    public static String[][] Add_Books_To_Buyer_Buying(String[] foundBook, String[][] userBuyingList, int numberOfBooks, String[] userData) {
        boolean bookExists = false;
        if (userBuyingList.length > 1) {
            for (int i = 0; i < userBuyingList.length; i++) {
                if (userBuyingList[i][3].equals(foundBook[0])) {

                    int updatedQuantity = Integer.parseInt(userBuyingList[i][5]) + numberOfBooks;
                    userBuyingList[i][5] = String.valueOf(updatedQuantity);

                    double bookPrice = Double.parseDouble(foundBook[3]);
                    double totalPrice = updatedQuantity * bookPrice;
                    userBuyingList[i][7] = String.valueOf(totalPrice);

                    bookExists = true;
                    break;
                }
            }
        }

        if (!bookExists) {
            String[] newBookEntry = new String[9];
            newBookEntry[0] = userData[2];
            newBookEntry[1] = userData[3];
            newBookEntry[2] = userData[4];
            newBookEntry[3] = foundBook[0];
            newBookEntry[4] = foundBook[1];
            newBookEntry[5] = String.valueOf(numberOfBooks);

            double bookPrice = Double.parseDouble(foundBook[3]);

            newBookEntry[6] = foundBook[3];
            newBookEntry[6] = foundBook[3];

            double totalPrice = numberOfBooks * bookPrice;

            newBookEntry[7] = String.valueOf(totalPrice);
            newBookEntry[8] = getCurrentDateTime();

            String[][] newUserBuyingList = new String[userBuyingList.length + 1][];

            for (int i = 0; i < userBuyingList.length; i++) {
                newUserBuyingList[i] = userBuyingList[i];
            }

            newUserBuyingList[newUserBuyingList.length - 1] = newBookEntry;

            userBuyingList = newUserBuyingList;

            return userBuyingList;
        }
        return userBuyingList;
    }

    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    public static void Decrease_Quanity_Of_Book(int index, int numberOfBooks, String[][] bookList) {

        int currentQuantity = Integer.parseInt(bookList[index][2]);
        int updatedQuantity = currentQuantity - numberOfBooks;

        if (updatedQuantity <= 0) {

            removeBookAtIndex(bookList, index);
        } else {
            bookList[index][2] = String.valueOf(updatedQuantity);
        }

    }

    public static String[][] removeBookAtIndex(String[][] bookList, int index) {
        if (index < 0 || index >= bookList.length) {
            return bookList;
        }

        String[][] newBookList = new String[bookList.length - 1][];
        System.arraycopy(bookList, 0, newBookList, 0, index);
        System.arraycopy(bookList, index + 1, newBookList, index, bookList.length - index - 1);

        return newBookList;
    }

    public static String[] View_Book_By_Search_Name(String bookTitle, String[][] bookList) {

        String[] foundBook = null;

        for (int index = 0; index < bookList.length; index++) {
            if (bookTitle.equalsIgnoreCase(bookList[index][0])) {
                foundBook = bookList[index];
                return foundBook;
            }
        }
        return foundBook;
    }

    public static int Found_Book_Index(String bookTitle, String[][] bookList) {

        for (int index = 0; index < bookList.length; index++) {
            if (bookTitle.equalsIgnoreCase(bookList[index][0])) {
                return index;
            }
        }
        return -1;
    }

    public static void View_Book_By_Filters(Scanner scanner, String[][] bookList) {
        System.out.println("Enter lower price for applying filter : ");
        double lowerRange = scanner.nextDouble();
        System.out.println("Enter upper price for applying filter : ");
        double upperRange = scanner.nextDouble();

        System.out.println("Book List:");
        System.out.println();

        for (String[] book : bookList) {
            try {
                double bookPrice = Double.parseDouble(book[3]);
                if (lowerRange < bookPrice && upperRange > bookPrice) {
                    System.out.println("Title: " + book[0] + ", Author: " + book[1]
                            + ", Quantities: " + book[2] + ", Price: " + book[3] + ", Discount: " + book[4]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid price format for book: " + book[0]);
            }
        }
    }

    public static void User_SignUp(Scanner scanner, String[][] userList, String userFileName) {

        FileInputStream fileInputStream = null;
        try {

            // Regex for Validation 
            String phoneRegex = "^\\+92[0-9]{10}$";
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            String passwordRegex = "^(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

            int userNameIndex = 2;
            int emailAddressIndex = 3;
            int phoneNumberIndex = 4;

            System.out.print("Enter your first name: ");
            String firstName = scanner.next();
            System.out.print("Enter your last name: ");
            String lastName = scanner.next();

            String username = Get_User_Input(scanner, "Enter your username: ", userList, userNameIndex);
            String emailAddress = Get_User_Input(scanner, "Enter your email Address: ", userList, emailAddressIndex, emailRegex);
            String phoneNumber = Get_User_Input(scanner, "Enter your phone Number e.g (+923366651111) : ", userList, phoneNumberIndex, phoneRegex);

            System.out.print("Enter your password (must have 8 characters with at least one digit and one special character): ");
            String currentPassword = scanner.next();
            while (!Validation_Function(currentPassword, passwordRegex)) {
                System.out.print("Enter your password (must have 8 characters with at least one digit and one special character): ");
                currentPassword = scanner.next();

            }
            System.out.print("Enter your confirm password : ");
            String confirmPassword = scanner.next();
            while (!currentPassword.equals(confirmPassword)) {
                System.out.print("Enter your confirm password same as current password : ");
                confirmPassword = scanner.next();
            }

            // Checking does this username or phonenumber or email already exist if any one of them exist then prompt user 
            // to enter again
            String[][] newUser = {{firstName, lastName, username, emailAddress, phoneNumber, currentPassword, confirmPassword}};

            if (userList != null) {
                String[][] newUserList = new String[userList.length + 1][];

                for (int i = 0; i < userList.length; i++) {
                    newUserList[i] = userList[i];
                }

                newUserList[newUserList.length - 1] = newUser[0];
                userList = newUserList;
            } else {
                userList = newUser;
            }

            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(userFileName))) {
                objectOutputStream.writeObject(userList);
                String[] newUserData = {firstName, lastName, username, emailAddress, phoneNumber, currentPassword, confirmPassword
                };
                User_Functionailty(scanner, "book_data.dat", newUserData);

            }

        } catch (IOException ex) {
            System.out.println("IO Exception occured here !");

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();

                } catch (IOException ex) {
                    System.out.println("IO Exception occured here !");
                }
            }
        }

    }

    public static String Get_User_Input(Scanner scanner, String prompt, String[][] existingData, int index) {
        String userInput;
        do {
            System.out.print(prompt);
            userInput = scanner.next();
        } while (Is_Found(existingData, userInput, index));
        return userInput;
    }

    public static String Get_User_Input(Scanner scanner, String prompt, String[][] existingData, int index, String regex) {
        String userInput;
        do {
            userInput = Get_User_Input(scanner, prompt, existingData, index);
        } while (!Validation_Function(userInput, regex));
        return userInput;
    }

    public static void Add_Book(Scanner scanner, String[][] bookList, String bookListFilePath) {
        System.out.print("Enter the book title: ");
        String title = scanner.nextLine();
        title = scanner.nextLine();

        System.out.print("Enter the book author: ");
        String author = scanner.nextLine();

        System.out.print("Enter the book quantities: ");
        int quantities = scanner.nextInt();

        System.out.print("Enter the price per book: ");
        double price = scanner.nextDouble();

        System.out.print("Enter the discount on the book: ");
        double discount = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline character after reading double

        String[] newBook = {title, author, Integer.toString(quantities), Double.toString(price), Double.toString(discount)};

        if (bookList != null) {
            String[][] newBookList = new String[bookList.length + 1][];

            for (int i = 0; i < bookList.length; i++) {
                newBookList[i] = bookList[i];
            }

            newBookList[newBookList.length - 1] = newBook;
            bookList = newBookList;
        } else {
            bookList = new String[][]{newBook};
        }

        Write_File(bookList, bookListFilePath);
    }

    public static void Print_Book_List(String[][] bookList) {
        // Print the book list
        System.out.println("Book List:");
        System.out.println();
        for (String[] book : bookList) {
            System.out.println("Title: " + book[0] + ", Author: " + book[1]
                    + ", Quantities: " + book[2] + ", Price: " + book[3] + ", Discount: " + book[4] + " %");
        }
    }

    public static void Remove_Book(Scanner scanner, String[][] bookList, String bookListFilePath) {
        // Assuming you want to remove a book by title
        System.out.print("Enter the title of the book to remove: ");
        String titleToRemove = scanner.nextLine();
        titleToRemove = scanner.nextLine();

        int foundIndex = -1;

        for (int i = 0; i < bookList.length; i++) {
            if (bookList[i] != null && bookList[i][0].equalsIgnoreCase(titleToRemove)) {

                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1) {

            String[][] updatedBookList = new String[bookList.length - 1][];

            for (int i = 0; i < foundIndex; i++) {
                updatedBookList[i] = bookList[i];
            }

            for (int i = foundIndex + 1; i < bookList.length; i++) {
                updatedBookList[i - 1] = bookList[i];
            }

            System.out.println("Book removed successfully!");

            Write_File(updatedBookList, bookListFilePath);
        } else {
            System.out.println("Book not found in the list.");
        }
    }

    public static void Edit_Book(Scanner scanner, String[][] bookList, String bookListFilePath) {

        System.out.print("Enter the title of the book to edit: ");
        String titleToEdit = scanner.nextLine();
        titleToEdit = scanner.nextLine();

        boolean found = false;

        for (String[] bookList1 : bookList) {
            if (bookList1 != null && bookList1[0].equalsIgnoreCase(titleToEdit)) {

                System.out.println("Enter the new book quantities: ");
                int newQuantities = scanner.nextInt();
                System.out.println("Enter the new price per book: ");
                double newPrice = scanner.nextDouble();
                System.out.println("Enter the new discount on the book: ");
                double newDiscount = scanner.nextDouble();
                scanner.nextLine();

                bookList1[2] = Integer.toString(newQuantities);
                bookList1[3] = Double.toString(newPrice);
                bookList1[4] = Double.toString(newDiscount);
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Book edited successfully!");

            Write_File(bookList, bookListFilePath);
        } else {
            System.out.println("Book not found in the list.");
        }
    }

    // Admin functions of Application 
    public static void Admin_Functionality(Scanner scanner, String bookListFilePath) {
        String[][] bookList = Read_File(bookListFilePath);
        System.out.println();
        System.out.println("1: For add book into Book Store    ");
        System.out.println("2: For remove book into Book Store ");
        System.out.println("3: For edit book into Book Store   ");
        System.out.println("4: For view all book list          ");

        System.out.println("6: For Apply general discount      ");

        System.out.println("8: For close application           ");

        int option = scanner.nextInt();

        switch (option) {
            case 1 -> {
                Add_Book(scanner, bookList, bookListFilePath);
                Admin_Functionality(scanner, bookListFilePath);
            }
            case 2 -> {
                Remove_Book(scanner, bookList, bookListFilePath);
                Admin_Functionality(scanner, bookListFilePath);
            }
            case 3 -> {
                Edit_Book(scanner, bookList, bookListFilePath);
                Admin_Functionality(scanner, bookListFilePath);
            }
            case 4 -> {
                Print_Book_List(bookList);
                Admin_Functionality(scanner, bookListFilePath);
            }

            case 6 -> {
                Apply_Gerenal_Discount(scanner, bookList, bookListFilePath);
                Admin_Functionality(scanner, bookListFilePath);
            }

            
            default ->
                System.exit(0);
        }
    }




    public static void Apply_Gerenal_Discount(Scanner scanner, String[][] bookList, String bookListFilePath) {

        System.out.print("Enter the discount percentage : ");
        double discountPercentage = scanner.nextDouble();

        for (String[] bookList1 : bookList) {
            bookList1[4] = Double.toString(discountPercentage);
        }

        Write_File(bookList, bookListFilePath);
    }

    // User functions of Application 
    // Generic functions of Application  
    // Function for read file 
    public static String[][] Read_File(String fileName) {
        Path path = Paths.get(fileName);
        boolean isExist = Files.exists(path);
        if (!isExist) {
            try {
                Files.createFile(path);
                System.out.println("File created successfully.");

            } catch (IOException ex) {
                System.out.println("IOException Occure : " + ex);
            }
        }

        // Creating outinputstream object here 
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            // If the size of the file is not zero then read file else print empty file message 
            if (Files.size(Paths.get(fileName)) > 0) {
                return (String[][]) ois.readObject();
            } else {
                System.out.println("File is empty.");
            }
        } catch (Exception eofEx) {

            System.out.println("End of file reached unexpectedly.");
        }
        return null;
    }

    public static void Write_File(String[][] dataToWrite, String filePath) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dataToWrite);
        } catch (IOException ex) {
            System.out.println("IOException handling : " + ex);
        }
    }

    public static boolean Is_Found(String[][] Array, String checkingValue, int checkingIndex) {
        for (String[] Array1 : Array) {
            if (Array1[checkingIndex].equals(checkingValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean Validation_Function(String value, String regex) {

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }

}
