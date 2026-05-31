import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * PDFManager - Upload, store, and open PDF files properly without showing binary data
 */
public class PDFManager {
    
    private static final String STORAGE_FOLDER = "uploaded_pdfs";
    
    /**
     * Opens a file chooser dialog to select and upload a PDF
     */
    public static void uploadPDF() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            savePDFFile(selectedFile);
        }
    }
    
    /**
     * Saves the PDF file to the storage folder (preserves the binary format)
     */
    private static void savePDFFile(File sourceFile) {
        try {
            // Create storage folder if it doesn't exist
            File storageDir = new File(STORAGE_FOLDER);
            if (!storageDir.exists()) {
                storageDir.mkdir();
            }
            
            // Create destination file path
            File destinationFile = new File(STORAGE_FOLDER + File.separator + sourceFile.getName());
            
            // Copy the PDF file (preserves binary format - IMPORTANT!)
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), 
                      StandardCopyOption.REPLACE_EXISTING);
            
            showInfo("PDF uploaded successfully!\nFile: " + destinationFile.getName() + 
                    "\nLocation: " + destinationFile.getAbsolutePath());
            
            // Ask if user wants to open the PDF immediately
            int openNow = JOptionPane.showConfirmDialog(null, 
                "Do you want to open this PDF now?", 
                "Open PDF", JOptionPane.YES_NO_OPTION);
            
            if (openNow == JOptionPane.YES_OPTION) {
                openStoredPDF(destinationFile.getName());
            }
            
        } catch (IOException e) {
            showError("Error uploading PDF: " + e.getMessage());
        }
    }
    
    /**
     * Opens a stored PDF file with the system's default PDF viewer
     * This reads the binary file correctly and opens it properly
     */
    public static void openStoredPDF(String fileName) {
        try {
            File pdfFile = new File(STORAGE_FOLDER + File.separator + fileName);
            
            if (!pdfFile.exists()) {
                showError("File not found: " + fileName);
                return;
            }
            
            // Open with system's default PDF viewer
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
                showInfo("Opening PDF: " + fileName);
            } else {
                showError("Desktop operations not supported");
            }
            
        } catch (IOException e) {
            showError("Error opening PDF: " + e.getMessage());
        }
    }
    
    /**
     * Lists all uploaded PDFs in storage
     */
    public static void listUploadedPDFs() {
        File storageDir = new File(STORAGE_FOLDER);
        
        if (!storageDir.exists() || !storageDir.isDirectory()) {
            showInfo("No uploaded PDFs yet");
            return;
        }
        
        File[] pdfFiles = storageDir.listFiles((dir, name) -> name.endsWith(".pdf"));
        
        if (pdfFiles == null || pdfFiles.length == 0) {
            showInfo("No PDF files found in storage");
            return;
        }
        
        StringBuilder fileList = new StringBuilder("Uploaded PDFs:\n\n");
        for (int i = 0; i < pdfFiles.length; i++) {
            fileList.append((i + 1)).append(". ").append(pdfFiles[i].getName())
                    .append(" (").append(formatFileSize(pdfFiles[i].length())).append(")\n");
        }
        
        showInfo(fileList.toString());
    }
    
    /**
     * Deletes a stored PDF file
     */
    public static void deletePDF(String fileName) {
        File pdfFile = new File(STORAGE_FOLDER + File.separator + fileName);
        
        if (pdfFile.exists() && pdfFile.delete()) {
            showInfo("PDF deleted: " + fileName);
        } else {
            showError("Could not delete PDF: " + fileName);
        }
    }
    
    /**
     * Formats file size for display
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int unit = 1024;
        if (bytes < unit * 1024) return String.format("%.2f KB", bytes / (double) unit);
        unit *= 1024;
        if (bytes < unit * 1024) return String.format("%.2f MB", bytes / (double) unit);
        unit *= 1024;
        return String.format("%.2f GB", bytes / (double) unit);
    }
    
    /**
     * Shows success message
     */
    private static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "PDF Manager", 
                                      JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows error message
     */
    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "PDF Manager Error", 
                                      JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Main menu for testing
     */
    public static void main(String[] args) {
        while (true) {
            String[] options = {"Upload PDF", "List PDFs", "Delete PDF", "Exit"};
            int choice = JOptionPane.showOptionDialog(null, 
                "PDF Manager - What would you like to do?", 
                "PDF Manager Menu",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, options, options[0]);
            
            switch (choice) {
                case 0:
                    uploadPDF();
                    break;
                case 1:
                    listUploadedPDFs();
                    break;
                case 2:
                    String fileToDelete = JOptionPane.showInputDialog("Enter PDF filename to delete:");
                    if (fileToDelete != null && !fileToDelete.isEmpty()) {
                        deletePDF(fileToDelete);
                    }
                    break;
                case 3:
                    System.exit(0);
                default:
                    break;
            }
        }
    }
}
