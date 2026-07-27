import java.util.Scanner;
import java.io.*;

public class Student {
    private String username;
    private String password;
    private String childName;
    public static String lastMykid = null; // remembers last MyKid
    
    static final int WIDTH = 180;

    public Student(String username) {
        this.username = username;
    }

    // ===== UI Helpers =====
    public static void clearScreen() {
        System.out.print("\f");
        System.out.flush();
    }
    public static void blankLines(int n) {
        for (int i = 0; i < n; i++) System.out.println();
    }
    public static void centerPrint(String text) {
        int padding = Math.max(0, (WIDTH - text.length()) / 2);
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.println(text);
    }
    public static void centerInput(String text) {
        for (int i = 0; i < 70; i++) System.out.print(" ");
        System.out.print(text);
    }
    public static void messagePrint(String text) {
        for (int i = 0; i < 70; i++) System.out.print(" ");
        System.out.println(text);
    }
    public static void pause(Scanner sc) {
        System.out.println();
        centerInput("Press [ENTER] to continue...");
        sc.nextLine();
    }

    // ===== STUDENT DETAILS MENU =====
    public void StudentDetails() {
        Scanner sc = new Scanner(System.in);
        int option;

        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║         STUDENT REGISTRATION          ║");
        centerPrint("╠═══════════════════════════════════════╣");
        centerPrint("║  1. Enter Student Details             ║");
        centerPrint("║  2. Back                              ║");
        centerPrint("╚═══════════════════════════════════════╝");
        System.out.println();

        centerInput("Choice: ");
        while (!sc.hasNextInt()) {
            messagePrint("✗ Invalid option! Please enter 1 or 2.");
            sc.nextLine();
            pause(sc);
            clearScreen();
            blankLines(8);
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║         STUDENT REGISTRATION          ║");
            centerPrint("╠═══════════════════════════════════════╣");
            centerPrint("║  1. Enter Student Details             ║");
            centerPrint("║  2. Back                              ║");
            centerPrint("╚═══════════════════════════════════════╝");
            System.out.println();
            centerInput("Choice: ");
        }
        option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {
            clearScreen();
            blankLines(8);
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║          ENTER STUDENT DATA           ║");
            centerPrint("╚═══════════════════════════════════════╝");
            System.out.println();

            String StudentName, dateOfBirth, MykidNum, HomeAddress, gender;

            // Name
            do {
                centerInput("► Student Name  : ");
                StudentName = sc.nextLine().trim();
                if (!StudentName.matches("[a-zA-Z ]+")) {
                    messagePrint("✗ Invalid name! Letters only.");
                    StudentName = "";
                    pause(sc);
                    clearScreen();
                    blankLines(8);
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║          ENTER STUDENT DATA           ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                    System.out.println();
                }
            } while (StudentName.isEmpty());

            // DOB
            do {
                centerInput("► Date of Birth (DD/MM/YYYY): ");
                dateOfBirth = sc.nextLine().trim();
                if (!dateOfBirth.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    messagePrint("✗ Invalid date format! Use DD/MM/YYYY.");
                    dateOfBirth = "";
                    pause(sc);
                    clearScreen();
                    blankLines(8);
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║          ENTER STUDENT DATA           ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                    System.out.println();
                }
            } while (dateOfBirth.isEmpty());

            // MyKid
            do {
                centerInput("► MyKid Number  : ");
                MykidNum = sc.nextLine().trim();
                if (!MykidNum.matches("\\d{12}")) {
                    messagePrint("✗ Invalid MyKid Number! Must be 12 digits.");
                    MykidNum = "";
                    pause(sc);
                    clearScreen();
                    blankLines(8);
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║          ENTER STUDENT DATA           ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                    System.out.println();
                }
            } while (MykidNum.isEmpty());

            // Address
            do {
                centerInput("► Home Address  : ");
                HomeAddress = sc.nextLine().trim();
                if (HomeAddress.isEmpty()) {
                    messagePrint("✗ Invalid address! Cannot be empty.");
                    HomeAddress = "";
                    pause(sc);
                    clearScreen();
                    blankLines(8);
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║          ENTER STUDENT DATA           ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                    System.out.println();
                }
            } while (HomeAddress.isEmpty());

            // Gender
            do {
                centerInput("► Gender (F/M)  : ");
                gender = sc.nextLine().toUpperCase();
                if (!(gender.equals("F") || gender.equals("M"))) {
                    messagePrint("✗ Invalid gender! Please enter F or M only.");
                    gender = "";
                    pause(sc);
                    clearScreen();
                    blankLines(8);
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║          ENTER STUDENT DATA           ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                    System.out.println();
                }
            } while (gender.isEmpty());

            String regDate = java.time.LocalDate.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter("student.txt", true))) {
                bw.write(StudentName + " , " +
                         dateOfBirth + " , " +
                         MykidNum + " , " +
                         HomeAddress + " , " +
                         gender + " , " +
                         regDate + " , " +
                         (username != null ? username : "UNKNOWN"));
                bw.newLine();

                System.out.println();
                centerPrint("╔═══════════════════════════════════════╗");
                centerPrint("║       RECORD SAVED SUCCESSFULLY       ║");
                centerPrint("╚═══════════════════════════════════════╝");

                lastMykid = MykidNum;
            } catch (IOException iox) {
                messagePrint("✗ Error: " + iox.getMessage());
            }

            System.out.println();
            centerInput("Press [ENTER] to continue...");
            sc.nextLine();

        } else if (option == 2) {
            Parent P = new Parent(username,password,childName);
             P.parentsMenu();
        } else {
            messagePrint("✗ Invalid option! Please choose 1 or 2.");
            pause(sc);
        }
    }
}
