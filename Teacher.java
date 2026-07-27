import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.awt.Desktop;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Teacher extends User {
    

    public Teacher(String username, String password) {
        setUser(username, password);
    }

    private Scanner sc;
    static final int WIDTH = 180; // same width as Parents

    public Teacher(Scanner sc) {
        this.sc = sc;
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

    public void pause() {
        centerInput("Press [ENTER] to continue...");
        sc.nextLine();
    }

    // ==================== VALIDATED INPUT HELPERS ====================
    // Returns "yes", "no", or "PAUSE" (teacher chose to stop and resume later)
    private String askYesNoOrPause(String prompt) {
        while (true) {
            centerInput(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no")) {
                return input.toLowerCase();
            }

            messagePrint("✗ Invalid input! Please type 'yes' or 'no'.");
            centerInput("► Continue processing this application now? (yes) or pause and return to Teacher Menu (no): ");
            String cont = sc.nextLine().trim();

            if (cont.equalsIgnoreCase("no")) {
                return "PAUSE";
            }
            // anything else (including "yes") loops back to re-ask the original prompt
        }
    }

    // Returns "approve", "reject", or "PAUSE"
    private String askApproveRejectOrPause(String prompt) {
        while (true) {
            centerInput(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("approve") || input.equalsIgnoreCase("reject")) {
                return input.toLowerCase();
            }

            messagePrint("✗ Invalid input! Please type 'approve' or 'reject'.");
            centerInput("► Continue processing this application now? (yes) or pause and return to Teacher Menu (no): ");
            String cont = sc.nextLine().trim();

            if (cont.equalsIgnoreCase("no")) {
                return "PAUSE";
            }
        }
    }

    // ==================== TEACHER MENU ====================
    public void TeacherMenu() {
        int choice;
        do {
            clearScreen();
            blankLines(8);
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║              TEACHER MENU             ║");
            centerPrint("╠═══════════════════════════════════════╣");
            centerPrint("║  1. Process Student Registration      ║");
            centerPrint("║  2. Review Student Application List   ║");
            centerPrint("║  3. Back                              ║");
            centerPrint("╚═══════════════════════════════════════╝");
            System.out.println();

            centerInput("► Choose option: ");
            while (!sc.hasNextInt()) {
                messagePrint("✗ Invalid input! Please enter a number.");
                sc.nextLine();
                centerInput("► Choose option: ");
            }

            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) processRegistration();
            else if (choice == 2) reviewApplicationList();
            else if (choice == 3) messagePrint("Returning to Main Menu...");
            else messagePrint("✗ Invalid choice. Try again.");

        } while (choice != 3);
    }

    // ==================== DISPLAY STUDENT DETAILS ====================
    private void displayStudentDetails(String name, String dob, String mykid, String address,
                                        String gender, String status) {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║          REGISTRATION DETAILS         ║");
        centerPrint("╠═══════════════════════════════════════╣");
        messagePrint("Student Name   : " + name);
        messagePrint("Date of Birth  : " + dob);
        messagePrint("MyKid Number   : " + mykid);
        messagePrint("Home Address   : " + address);
        messagePrint("Gender         : " + gender);
        messagePrint("Current Status : " + status);
        centerPrint("╚═══════════════════════════════════════╝");
    }
    // ==================== APPLICATION RECORD ====================
    private static class Application {
        int no;
        String name = "N/A";
        String mykid = "N/A";
        String regDate = "N/A";
        String status = "N/A";
        String dob = "N/A";
        String address = "N/A";
        String gender = "N/A";
        String username = "N/A";
        String parentName = "N/A";
        String parentMykad = "N/A";
        String parentOccupation = "N/A";
        String parentPhone = "N/A";
        boolean hasParentInfo = false;
    }

    // ==================== REVIEW APPLICATION LIST ====================
    public void reviewApplicationList() {
        Map<String, Application> byMykid = new LinkedHashMap<>();

        // 1. Read student.txt
        try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length == 5 || parts.length == 6 || parts.length == 7) {
                    Application a = new Application();
                    a.name = parts[0];
                    a.dob = parts[1];
                    a.mykid = parts[2];
                    a.address = parts[3];
                    a.gender = parts[4];
                    a.regDate = (parts.length >= 6) ? parts[5] : "N/A";
                    a.username = (parts.length == 7) ? parts[6] : "N/A";
                    a.status = "Pending";
                    byMykid.put(a.mykid, a);
                }
            }
        } catch (IOException e) {
            clearScreen();
            messagePrint("⚠️ Error: student.txt not found. No applications submitted yet.");
            pause();
            TeacherMenu();
            return;
        }

        // 2. Overlay status.txt
        try (BufferedReader reader = new BufferedReader(new FileReader("status.txt"))) {
            String line;
            String curName = "N/A", curMykid = "N/A", curStatus = "N/A";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Student Name:")) {
                    curName = line.replace("Student Name:", "").trim();
                } else if (line.startsWith("MyKid:")) {
                    curMykid = line.replace("MyKid:", "").trim();
                } else if (line.startsWith("Status:")) {
                    curStatus = line.replace("Status:", "").trim();
                } else if (line.equals("----------")) {
                    Application existing = byMykid.get(curMykid);
                    if (existing != null) {
                        existing.status = curStatus;
                    } else if (!curName.equals("N/A")) {
                        Application orphan = new Application();
                        orphan.name = curName;
                        orphan.mykid = curMykid;
                        orphan.status = curStatus;
                        byMykid.put(curMykid, orphan);
                    }
                    curName = "N/A";
                    curMykid = "N/A";
                    curStatus = "N/A";
                }
            }
        } catch (IOException e) {
            // status.txt missing is fine
        }

        // 3. Overlay parent info from parents_details.txt
        try (BufferedReader pr = new BufferedReader(new FileReader("parents_details.txt"))) {
            String line;
            while ((line = pr.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length != 5) continue;

                String pUsername   = parts[0].trim();
                String pName       = parts[1].trim();
                String pMykad      = parts[2].trim();
                String pOccupation = parts[3].trim();
                String pPhone      = parts[4].trim();

                for (Application app: byMykid.values()){
                    if (app.username.trim().equalsIgnoreCase(pUsername)) {
                        app.parentName = pName;
                        app.parentMykad = pMykad;
                        app.parentOccupation = pOccupation;
                        app.parentPhone = pPhone;
                        app.hasParentInfo = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            // parents_details.txt missing is fine
        }

        List<Application> applications = new ArrayList<>(byMykid.values());

        // 4. Assign numbering
        int n = 1;
        for (Application a : applications) {
            a.no = n++;
        }

        // 5. Render table
        clearScreen();
        printApplicationTable(applications);

        // 6. Interactive loop
        while (true) {
            centerInput("► Enter No. to view full details (0 to go back): ");
            while (!sc.hasNextInt()) {
                messagePrint("✗ Invalid input! Please enter a number.");
                sc.nextLine();
                centerInput("► Enter No. to view full details (0 to go back): ");
            }
            int selection = sc.nextInt();
            sc.nextLine();

            if (selection == 0) {
                TeacherMenu();
                return;
            }

            Application selected = null;
            for (Application a : applications) {
                if (a.no == selection) {
                    selected = a;
                    break;
                }
            }

            if (selected == null) {
                messagePrint("✗ Invalid No. Please try again.");
                continue;
            }

            showApplicationDetailsPage(selected);
            clearScreen();
            printApplicationTable(applications);
        }
    }

    // ==================== PRINT APPLICATION TABLE ====================
        private void printApplicationTable(List<Application> applications) {
        centerPrint("╔═════╦══════════════════════╦══════════════════╦══════════════════════════╦═════════╗");
        centerPrint("║ No. ║ Student Name         ║ Reg. Date        ║ Status                   ║ Action  ║");
        centerPrint("╠═════╬══════════════════════╬══════════════════╬══════════════════════════╬═════════╣");

        if (applications.isEmpty()) {
        messagePrint("No applications have been reviewed yet.");
        } else {
        for (Application a : applications) {
            String row = String.format("║ %-3d ║ %-20s ║ %-16s ║ %-24s ║ [%3d]   ║",
                    a.no,
                    truncate(a.name, 20),
                    truncate(a.regDate, 16),
                    truncate(a.status, 24),
                    a.no);
            centerPrint(row); // ✅ Center the entire row
        }
    }

    centerPrint("╚═════╩══════════════════════╩══════════════════╩══════════════════════════╩═════════╝");
    messagePrint("Total applications: " + applications.size());
    }


    private String truncate(String s, int max) {
        if (s == null) return "N/A";
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
    // ==================== APPLICATION DETAILS PAGE ====================
    private void showApplicationDetailsPage(Application a) {
        boolean viewingDetails = true;

        while (viewingDetails) {
            clearScreen();
            blankLines(8);
            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║          APPLICATION DETAILS          ║");
            centerPrint("╠═══════════════════════════════════════╣");
            messagePrint("Student Name     : " + a.name);
            messagePrint("Date of Birth    : " + a.dob);
            messagePrint("MyKid Number     : " + a.mykid);
            messagePrint("Home Address     : " + a.address);
            messagePrint("Gender           : " + a.gender);
            messagePrint("Registration Date: " + a.regDate);
            messagePrint("Status           : " + a.status);
            centerPrint("╠═══════════════════════════════════════╣");
            centerPrint("║  [1] View Submitted PDF Document      ║");
            centerPrint("║  [2] View Parent Information          ║");
            centerPrint("║  [3] Back to Application List         ║");
            centerPrint("╚═══════════════════════════════════════╝");
            centerInput("► Choose option: ");

            while (!sc.hasNextInt()) {
                messagePrint("✗ Invalid input! Please enter a number.");
                sc.nextLine();
                centerInput("► Choose option: ");
            }
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                viewSubmittedDocument(a);
            } else if (choice == 2) {
                showParentInfoPage(a);
            } else if (choice == 3) {
                viewingDetails = false;
            } else {
                messagePrint("✗ Invalid option. Please choose 1, 2 or 3.");
                pause();
            }
        }
    }

    // ==================== PARENT INFORMATION PAGE ====================
    private void showParentInfoPage(Application a) {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║          PARENT INFORMATION           ║");
        centerPrint("╠═══════════════════════════════════════╣");

        if (!a.hasParentInfo) {
            messagePrint("No parent details have been submitted for this student yet.");
        } else {
            messagePrint("Parent Name      : " + a.parentName);
            messagePrint("Parent MyKad No. : " + a.parentMykad);
            messagePrint("Occupation       : " + a.parentOccupation);
            messagePrint("Phone Number     : " + a.parentPhone);
        }

        centerPrint("╚═══════════════════════════════════════╝");
        pause();
    }

    // ==================== VIEW SUBMITTED PDF DOCUMENT ====================
    private static final String PDF_STORAGE_FOLDER = "uploaded_pdfs";

    // PDFManager saves files as "<parentUsername>_<originalFileName>.pdf".
    // So the reliable way to find a student's PDF is to match by the parent's
    // username prefix. We also keep a mykid-contains() check as a fallback for
    // any older files that might not follow the username-prefix convention.
    private List<File> findMatchingPDFs(String mykidNum, String username) {
        List<File> matches = new ArrayList<>();
        File storageDir = new File(PDF_STORAGE_FOLDER);

        if (!storageDir.exists() || !storageDir.isDirectory()) {
            return matches;
        }

        File[] pdfFiles = storageDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        if (pdfFiles == null) {
            return matches;
        }

        for (File f : pdfFiles) {
            String fname = f.getName().toLowerCase();

            boolean matchByUsername = username != null && !username.equals("N/A")
                    && fname.startsWith(username.toLowerCase() + "_");

            boolean matchByMykid = mykidNum != null && !mykidNum.equals("N/A")
                    && fname.contains(mykidNum.toLowerCase());

            if (matchByUsername || matchByMykid) {
                matches.add(f);
            }
        }

        return matches;
    }

    private void viewSubmittedDocument(Application a) {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║         SUBMITTED PDF DOCUMENT        ║");
        centerPrint("╚═══════════════════════════════════════╝");

        List<File> matches = findMatchingPDFs(a.mykid, a.username);

        if (matches.isEmpty()) {
            messagePrint("✗ No PDF document has been submitted for this student.");
            pause();
            return;
        }

        File toOpen = (matches.size() == 1)
                ? matches.get(0)
                : chooseFromList(matches, "Multiple documents found for this student:");

        if (toOpen != null) {
            openPDF(toOpen);
        }
    }

    private File chooseFromList(List<File> files, String header) {
        messagePrint(header);
        for (int i = 0; i < files.size(); i++) {
            messagePrint("[" + (i + 1) + "] " + files.get(i).getName());
        }
        centerInput("► Enter number to open (0 to cancel): ");

        while (!sc.hasNextInt()) {
            messagePrint("✗ Invalid input! Please enter a number.");
            sc.nextLine();
            centerInput("► Enter number to open (0 to cancel): ");
        }
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 0) return null;
        if (choice < 1 || choice > files.size()) {
            messagePrint("✗ Invalid selection.");
            pause();
            return null;
        }
        return files.get(choice - 1);
    }

    private void openPDF(File pdfFile) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
                messagePrint("✓ Opening: " + pdfFile.getName());
            } else {
                messagePrint("✗ Desktop operations not supported on this system.");
            }
        } catch (IOException e) {
            messagePrint("✗ Error opening PDF: " + e.getMessage());
        }
        pause();
    }

    // ==================== CHECK PDF BEFORE DECISION ====================
    private void checkSubmittedDocumentBeforeDecision(String mykidNum, String username) {
        List<File> matches = findMatchingPDFs(mykidNum, username);

        if (matches.isEmpty()) {
            messagePrint("⚠️ No PDF document attached for this student.");
            return;
        }

        if (matches.size() == 1) {
            messagePrint("📄 Submitted document found: " + matches.get(0).getName());
        } else {
            messagePrint("📄 " + matches.size() + " submitted documents found for this student.");
        }

        String view = askYesNoOrPause("► View submitted document before deciding? (yes/no): ");

        if (view.equals("yes")) {
            File toOpen = (matches.size() == 1)
                    ? matches.get(0)
                    : chooseFromList(matches, "Select a document to open:");
            if (toOpen != null) {
                openPDF(toOpen);
            }
        }
        // Note: if the teacher chooses PAUSE here, we simply don't open a document
        // and let processRegistration's own prompts handle the pause on the next question.
    }
    // ==================== PROCESS REGISTRATION ====================
    public void processRegistration() {
        List<String[]> students = new ArrayList<>();

        // Read from student.txt — format: Name , DOB , MykidNum , Address , Gender [, RegDate [, Username]]
        try (BufferedReader studentReader = new BufferedReader(new FileReader("student.txt"))) {
            String line;
            while ((line = studentReader.readLine()) != null) {
                String[] parts = line.split("\\s*,\\s*");
                if (parts.length == 5 || parts.length == 6 || parts.length == 7) {
                    students.add(parts);
                }
            }
        } catch (IOException e) {
            messagePrint("⚠️ Error: student.txt not found.");
            pause();
            TeacherMenu();
            return;
        }

        // Skip students already processed (their MyKid appears in status.txt)
        Set<String> processedMykids = new HashSet<>();
        try (BufferedReader statusReader = new BufferedReader(new FileReader("status.txt"))) {
            String line;
            while ((line = statusReader.readLine()) != null) {
                if (line.startsWith("MyKid:")) {
                    processedMykids.add(line.replace("MyKid:", "").trim());
                }
            }
        } catch (IOException e) {
            // status.txt missing just means nothing has been processed yet
        }

        List<String[]> pendingStudents = new ArrayList<>();
        for (String[] s : students) {
            if (!processedMykids.contains(s[2])) {
                pendingStudents.add(s);
            }
        }

        if (pendingStudents.isEmpty()) {
            clearScreen();
            blankLines(8);
            centerPrint("✅ No pending applications to process.");
            messagePrint("Every submitted student has already been reviewed.");
            pause();
            TeacherMenu();
            return;
        }

        // Loop through each student that hasn't been processed yet
        for (String[] s : pendingStudents) {
            String studentName    = s[0];
            String studentDOB     = s[1];
            String mykidNum       = s[2];
            String studentAddress = s[3];
            String studentGender  = s[4];
            String studentUsername = (s.length == 7) ? s[6] : "N/A";
            String status         = "Pending";

            // Show initial details with Pending status
            displayStudentDetails(studentName, studentDOB, mykidNum, studentAddress, studentGender, status);

            // Automatically check whether the parent uploaded a PDF
            checkSubmittedDocumentBeforeDecision(mykidNum, studentUsername);

            // Document verification
            String docVerified = askYesNoOrPause("► Is the document valid? (yes/no): ");
            if (docVerified.equals("PAUSE")) {
                messagePrint("⏸ Processing paused. This student remains Pending — you can continue anytime.");
                pause();
                TeacherMenu();
                return;
            }

            if (docVerified.equals("yes")) {
                String studentVerified = askYesNoOrPause("► Are student details correct? (yes/no): ");
                if (studentVerified.equals("PAUSE")) {
                    messagePrint("⏸ Processing paused. This student remains Pending — you can continue anytime.");
                    pause();
                    TeacherMenu();
                    return;
                }

                if (studentVerified.equals("yes")) {
                    String decision = askApproveRejectOrPause("► Decision - approve or reject: ");
                    if (decision.equals("PAUSE")) {
                        messagePrint("⏸ Processing paused. This student remains Pending — you can continue anytime.");
                        pause();
                        TeacherMenu();
                        return;
                    }
                    status = decision.equalsIgnoreCase("approve") ? "Approved" : "Rejected";
                } else {
                    status = "Rejected - Invalid Student Details";
                }
            } else {
                status = "Rejected - Invalid Document";
            }

            // Re-display with updated status
            displayStudentDetails(studentName, studentDOB, mykidNum, studentAddress, studentGender, status);
            messagePrint("✅ Registration status updated: " + status);

            // Save to status.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("status.txt", true))) {
                writer.write("Student Name: " + studentName);
                writer.newLine();
                writer.write("MyKid: " + mykidNum);
                writer.newLine();
                writer.write("Status: " + status);
                writer.newLine();
                writer.write("----------");
                writer.newLine();
            } catch (IOException e) {
                messagePrint("⚠️ Error saving status: " + e.getMessage());
            }

            // If approved, ask teacher to continue or stop
            if (status.equals("Approved")) {
                String continueCheck = askYesNoOrPause("► Check next student applicant? (yes/no): ");
                if (continueCheck.equals("PAUSE") || continueCheck.equals("no")) {
                    messagePrint("Returning to Teacher Menu...");
                    pause();
                    TeacherMenu();
                    return;
                }
            } else {
                pause();
            }
        }

        messagePrint("✅ All student applications have been reviewed.");
        pause();
        TeacherMenu();
    }
}
