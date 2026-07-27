import java.io.*;
import java.nio.file.*;
import java.nio.file.StandardCopyOption;
import java.awt.Desktop;
import java.util.Scanner;

public class PDFManager {

    private static final String STORAGE_FOLDER = "uploaded_pdfs";
    private Scanner input = new Scanner(System.in);
    static final int WIDTH = 180;

    // keep track of the logged-in parent
    private String parentUsername;

    // constructor to receive parent username
    public PDFManager(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    // ===== UI Helpers =====
    public static void clearScreen() { System.out.print("\f"); System.out.flush(); }
    public static void blankLines(int n) { for (int i = 0; i < n; i++) System.out.println(); }
    public static void centerPrint(String text) {
        int padding = Math.max(0, (WIDTH - text.length()) / 2);
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.println(text);
    }
    public static void centerInput(String text) { for (int i = 0; i < 70; i++) System.out.print(" "); System.out.print(text); }
    public static void messagePrint(String text) { for (int i = 0; i < 70; i++) System.out.print(" "); System.out.println(text); }

    // ===== PDF MANAGER MENU =====
    public void pdfManagerMenu() {
        int choice;
        do {
            clearScreen();
            blankLines(8);
            displayPDFTable();

            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║         PDF MANAGER MENU              ║");
            centerPrint("╠═══════════════════════════════════════╣");
            centerPrint("║  [1] Upload PDF                       ║");
            centerPrint("║  [2] Open PDF                         ║");
            centerPrint("║  [3] Delete PDF                       ║");
            centerPrint("║  [4] Back to Previous Menu            ║");
            centerPrint("╚═══════════════════════════════════════╝");
            System.out.println();

            centerInput("► Choose option: ");
            while (!input.hasNextInt()) {
                messagePrint("✗ Invalid input! Please enter a number (1-4).");
                input.nextLine();
                centerInput("► Choose option: ");
            }

            choice = input.nextInt();
            input.nextLine();

            if (choice < 1 || choice > 4) {
                messagePrint("✗ Invalid option! Please choose 1–4.");
                continue;
            }

            if (choice == 1) uploadPDF();
            else if (choice == 2) openPDF();
            else if (choice == 3) deletePDF();
            else if (choice == 4) messagePrint("Returning to previous menu...");

        } while (choice != 4);
    }

    // ===== DISPLAY PDF TABLE =====
    private void displayPDFTable() {
        File storageDir = new File(STORAGE_FOLDER);

        File[] pdfFiles = storageDir.listFiles((dir, name) ->
            name.toLowerCase().endsWith(".pdf") &&
            name.toLowerCase().startsWith(parentUsername.toLowerCase() + "_")
        );

        centerPrint("╔════════════════════════════════════════════════════════════════════════════════════╗");
        centerPrint("║                                 --- PDF FILES TABLE ---                            ║");
        centerPrint("╠════╦════════════════════════════════════════════════════════╦════════════╦═════════╣");
        centerPrint("║ No ║ File Name                                              ║ Size       ║ Action  ║");
        centerPrint("╠════╬════════════════════════════════════════════════════════╬════════════╬═════════╣");

        if (pdfFiles == null || pdfFiles.length == 0) {
            centerPrint("║                           No PDF files found                            ║");
        } else {
            for (int i = 0; i < pdfFiles.length; i++) {
                String fileName = pdfFiles[i].getName();
                String fileSize = formatFileSize(pdfFiles[i].length());
                if (fileName.length() > 55) fileName = fileName.substring(0, 52) + "...";

                String row = String.format("║ %-2d ║ %-54s ║ %10s ║ [%2d]    ║",
                        i + 1, fileName, fileSize, i + 1);
                centerPrint(row);
            }
        }

        centerPrint("╚════╩════════════════════════════════════════════════════════╩════════════╩═════════╝");
    }

    // ===== UPLOAD PDF =====
    public void uploadPDF() {
        clearScreen();
        blankLines(8);
        centerPrint("╔══════════════════════════════════════╗");
        centerPrint("║              UPLOAD PDF              ║");
        centerPrint("╚══════════════════════════════════════╝");
        System.out.println();

        try {
            File selectedFile = openFileChooser();
            if (selectedFile == null) {
                messagePrint("✗ No file selected.");
                centerInput("Press [ENTER] to continue...");
                input.nextLine();
                return;
            }
            if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                messagePrint("✗ Please select a PDF file only.");
                centerInput("Press [ENTER] to continue...");
                input.nextLine();
                return;
            }

            savePDFFile(selectedFile);

        } catch (Exception e) {
            messagePrint("✗ Error: " + e.getMessage());
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
        }
    }

    private File openFileChooser() throws Exception {
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        javax.swing.filechooser.FileNameExtensionFilter filter =
            new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    private void savePDFFile(File sourceFile) {
        try {
            File storageDir = new File(STORAGE_FOLDER);
            if (!storageDir.exists()) storageDir.mkdir();

            // ✅ prefix with parent username
            String taggedName = parentUsername + "_" + sourceFile.getName();
            File destinationFile = new File(STORAGE_FOLDER + File.separator + taggedName);

            if (destinationFile.exists()) {
                messagePrint("⚠️ File already exists, replacing...");
            }

            Files.copy(sourceFile.toPath(), destinationFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("document.txt", true))) {
                bw.write("PDF Document , " + parentUsername + " , " + taggedName);
                bw.newLine();
            }

            centerPrint("╔═══════════════════════════════════════╗");
            centerPrint("║   ✓ PDF UPLOADED SUCCESSFULLY         ║");
            centerPrint("╚═══════════════════════════════════════╝");
            messagePrint("File: " + taggedName);
            messagePrint("Location: " + destinationFile.getAbsolutePath());

            centerInput("Press [ENTER] to continue...");
            input.nextLine();

        } catch (IOException e) {
            messagePrint("✗ Error uploading PDF: " + e.getMessage());
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
        }
    }
    // ===== OPEN PDF =====
    private void openPDF() {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║            OPEN PDF                   ║");
        centerPrint("╚═══════════════════════════════════════╝");
        System.out.println();

        File storageDir = new File(STORAGE_FOLDER);
        File[] pdfFiles = storageDir.listFiles((dir, name) ->
            name.toLowerCase().endsWith(".pdf") &&
            name.toLowerCase().startsWith(parentUsername.toLowerCase() + "_")
        );

        if (pdfFiles == null || pdfFiles.length == 0) {
            messagePrint("✗ No PDF files found for your account.");
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        centerPrint("Select a PDF to open:");
        for (int i = 0; i < pdfFiles.length; i++) {
            messagePrint("[" + (i + 1) + "] " + pdfFiles[i].getName() +
                         " (" + formatFileSize(pdfFiles[i].length()) + ")");
        }

        centerInput("► Enter number: ");
        while (!input.hasNextInt()) {
            messagePrint("✗ Invalid input! Please enter a number.");
            input.nextLine();
            centerInput("► Enter number: ");
        }

        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > pdfFiles.length) {
            messagePrint("✗ Invalid selection!");
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        File chosen = pdfFiles[choice - 1];
        openStoredPDF(chosen.getName());
    }

    private void openStoredPDF(String fileName) {
        try {
            File pdfFile = new File(STORAGE_FOLDER + File.separator + fileName);

            if (!pdfFile.exists()) {
                messagePrint("✗ File not found: " + fileName);
                centerInput("Press [ENTER] to continue...");
                input.nextLine();
                return;
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(pdfFile);
                messagePrint("✓ Opening PDF: " + fileName);
            } else {
                messagePrint("✗ Opening PDFs not supported on this system.");
            }

            centerInput("Press [ENTER] to continue...");
            input.nextLine();

        } catch (IOException e) {
            messagePrint("✗ Error opening PDF: " + e.getMessage());
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
        }
    }

    // ===== DELETE PDF =====
    public void deletePDF() {
        clearScreen();
        blankLines(8);
        centerPrint("╔═══════════════════════════════════════╗");
        centerPrint("║            DELETE PDF                 ║");
        centerPrint("╚═══════════════════════════════════════╝");
        System.out.println();

        File storageDir = new File(STORAGE_FOLDER);
        File[] pdfFiles = storageDir.listFiles((dir, name) ->
            name.toLowerCase().endsWith(".pdf") &&
            name.toLowerCase().startsWith(parentUsername.toLowerCase() + "_")
        );

        if (pdfFiles == null || pdfFiles.length == 0) {
            messagePrint("✗ No PDF files found for your account.");
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        centerPrint("Select a PDF to delete:");
        for (int i = 0; i < pdfFiles.length; i++) {
            messagePrint("[" + (i + 1) + "] " + pdfFiles[i].getName() +
                         " (" + formatFileSize(pdfFiles[i].length()) + ")");
        }

        centerInput("► Enter number: ");
        while (!input.hasNextInt()) {
            messagePrint("✗ Invalid input! Please enter a number.");
            input.nextLine();
            centerInput("► Enter number: ");
        }

        int choice = input.nextInt();
        input.nextLine();

        if (choice < 1 || choice > pdfFiles.length) {
            messagePrint("✗ Invalid selection!");
            centerInput("Press [ENTER] to continue...");
            input.nextLine();
            return;
        }

        File chosen = pdfFiles[choice - 1];
        centerInput("⚠️ Are you sure you want to delete '" + chosen.getName() + "'? (Y/N): ");
        String confirm = input.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            if (chosen.delete()) {
                centerPrint("╔═══════════════════════════════════════╗");
                centerPrint("║   ✓ PDF DELETED SUCCESSFULLY          ║");
                centerPrint("╚═══════════════════════════════════════╝");
                messagePrint("File: " + chosen.getName());
            } else {
                messagePrint("✗ Could not delete PDF: " + chosen.getName());
            }
        } else {
            messagePrint("Deletion cancelled.");
        }

        centerInput("Press [ENTER] to continue...");
        input.nextLine();
    }

    // ===== FILE SIZE FORMATTER =====
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int unit = 1024;
        if (bytes < unit * 1024) return String.format("%.2f KB", bytes / (double) unit);
        unit *= 1024;
        if (bytes < unit * 1024) return String.format("%.2f MB", bytes / (double) unit);
        unit *= 1024;
        return String.format("%.2f GB", bytes / (double) unit);
    }
}
