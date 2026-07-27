import java.util.Scanner;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Parent extends User {
    
    private String childName;
    private String phoneNum;
    private String occupation;
    private String name;
    private String mykadNum;
    private boolean found;
    private String studentName;
    private String status;
    private int mykidNum;
    private String password;
    
    private Scanner input = new Scanner(System.in);
    private String username;
    static final int WIDTH = 180;
    
    public Parent(String username, String password, String childName) {
        this.username = username;
        this.password = password;
    }
    
    public void setUser(String username,String password){
        this.childName = childName;
    }

    public String getChildName() {
        return childName;
    }

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

    // ===== MAIN MENU =====
    public void parentsMenu() {
        int option;
        do {
            clearScreen();
            blankLines(8);
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║            PARENTS DASHBOARD          ║");
            centerPrint("╠═══════════════════════════════════════╣");
            centerPrint("║  1. Provide Details                   ║");
            centerPrint("║  2. Status Application                ║");
            centerPrint("║  3. School Fee                        ║");
            centerPrint("║  4. Back                              ║");
            centerPrint("╚═══════════════════════════════════════╝");
            System.out.println();

            centerInput("► Choose option: ");
            while (!input.hasNextInt()) {
                messagePrint("✗ Invalid input! Please enter a number.");
                input.nextLine();
                centerInput("► Choose option: ");
            }
            option = input.nextInt();
            input.nextLine();
            Fee S = new Fee(200,50,30);
            if (option == 1) ProvideDetails();
            else if (option == 2) StatusApply();
            else if (option == 3) System.out.print(S);
            else if (option == 4) messagePrint("Returning to Main Menu...");
            else messagePrint("✗ Invalid option! Please choose 1–4.");

        } while (option != 4);
    }
    

    // ===== PROVIDE DETAILS =====
    public void ProvideDetails() {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║         SELECT INFORMATION TYPE       ║");
        centerPrint("╠═══════════════════════════════════════╣");
        centerPrint("║  1. Parents Details                   ║");
        centerPrint("║  2. Student Details                   ║");
        centerPrint("║  3. Document Upload                   ║");
        centerPrint("║  4. Back                              ║");
        centerPrint("╚═══════════════════════════════════════╝");
        System.out.println();

        centerInput("► Choice: ");
        while (!input.hasNextInt()) {
            messagePrint("✗ Invalid option! Please enter a number.");
            input.nextLine();
            centerInput("► Choice: ");
        }
        int option = input.nextInt();
        input.nextLine();

        if (option == 1) {
            String choice;
            do {
                clearScreen();
                blankLines(8);
                centerPrint("╔═══════════════════════════════════════╗");
                centerPrint("║           PARENTS DETAILS             ║");
                centerPrint("╚═══════════════════════════════════════╝");
                System.out.println();

                String linkKey = (username != null) ? username : "UNKNOWN";

                // Name validation
                String Name;
                do {
                    centerInput("► Parent Name  : ");
                    Name = input.nextLine().trim();
                    if (!Name.matches("[a-zA-Z ]+")) {
                        messagePrint("✗ Invalid! Name must contain letters only.");
                        Name = "";
                    }
                } while (Name.isEmpty());

                // MyKad validation (12 digits)
                String MykadNum;
                do {
                    centerInput("► Parent's MyKad Number (12 digits): ");
                    MykadNum = input.nextLine().trim();
                    if (!MykadNum.matches("\\d{12}")) {
                        messagePrint("✗ Invalid! MyKad must be exactly 12 digits.");
                        MykadNum = "";
                    }
                } while (MykadNum.isEmpty());

                // Occupation validation
                String Occupation;
                do {
                    centerInput("► Occupation   : ");
                    Occupation = input.nextLine().trim();
                    if (!Occupation.matches("[a-zA-Z ]+")) {
                        messagePrint("✗ Invalid! Occupation must contain letters only.");
                        Occupation = "";
                    }
                } while (Occupation.isEmpty());

                // Phone validation (10 digits)
                String PhoneNum;
                do {
                    centerInput("► Phone Number : ");
                    PhoneNum = input.nextLine().trim();
                    if (!PhoneNum.matches("\\d{10}")) {
                        messagePrint("✗ Invalid! Phone must be 10 digits.");
                        PhoneNum = "";
                    }
                } while (PhoneNum.isEmpty());

                try (BufferedWriter bw = new BufferedWriter(
                        new FileWriter("parents_details.txt", true))) {
                    bw.write(linkKey + "," + Name + "," + MykadNum + "," + Occupation + "," + PhoneNum);
                    bw.newLine();
                    centerPrint("╔═══════════════════════════════════════╗");
                    centerPrint("║      RECORD SAVED SUCCESSFULLY        ║");
                    centerPrint("╚═══════════════════════════════════════╝");
                } catch (IOException e) {
                    messagePrint("✗ Error saving record: " + e.getMessage());
                }

                System.out.println();
                centerInput("Fill another details? (Y/N): ");
                choice = input.nextLine();

            } while (choice.equalsIgnoreCase("Y"));

        } else if (option == 2) {
            clearScreen();
            Student S = new Student(username);
            S.StudentDetails();
        } else if (option == 3) {
            PDFManager manager = new PDFManager(username);
            manager.pdfManagerMenu();
        } else if (option == 4) {
            messagePrint("Returning...");
        } else {
            messagePrint("✗ Invalid option! Please choose 1–4.");
        }
    }

    // ===== STATUS APPLY =====
    public void StatusApply() {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║          STATUS APPLICATION           ║");
        centerPrint("╚═══════════════════════════════════════╝");
        System.out.println();

        centerInput("► Enter your child's MyKid Number: ");
        String mykidInput = input.nextLine();

        boolean found = false;
        String studentName = "";
        String status = "";

        try (BufferedReader br = new BufferedReader(new FileReader("status.txt"))) {
            String line, currentName = "", currentMykid = "", currentStatus = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Student Name:")) currentName = line.substring(13).trim();
                else if (line.startsWith("MyKid:")) currentMykid = line.substring(6).trim();
                else if (line.startsWith("Status:")) {
                    currentStatus = line.substring(7).trim();
                    if (currentMykid.equals(mykidInput)) {
                        found = true;
                        studentName = currentName;
                        status = currentStatus;
                    }
                }
            }
        } catch (IOException e) {
            messagePrint("⚠️ No application found. Please submit registration first.");
            System.out.println();
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        System.out.println();
        if (found) {
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║           APPLICATION RESULT          ║");
            centerPrint("╠═══════════════════════════════════════╣");
            messagePrint("Student Name : " + studentName);
            messagePrint("MyKid Number : " + mykidInput);
            messagePrint("Status       : " + status);
            centerPrint("╚═══════════════════════════════════════╝");

            if (status.equalsIgnoreCase("Approved")) {
                messagePrint("🎉 Congratulations! Your child's registration has been APPROVED!");
            } else if (status.toLowerCase().startsWith("rejected")) {
                messagePrint("❌ Sorry, your child's registration has been REJECTED.");
                messagePrint("Reason: " + status);
            } else {
                messagePrint("⏳ Your application is still PENDING. Please check back later.");
            }
        } else {
            messagePrint("⚠️ No application found for MyKid Number: " + mykidInput);
            messagePrint("Please make sure you entered the correct MyKid Number.");
        }

        System.out.println();
        centerInput("Press [ENTER] to continue...");
        input.nextLine();
    }
}
