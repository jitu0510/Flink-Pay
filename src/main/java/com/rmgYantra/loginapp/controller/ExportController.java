package com.rmgYantra.loginapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.rmgYantra.loginapp.model.Project;
import com.rmgYantra.loginapp.repo.ExportRepo;

@RestController
@CrossOrigin(origins = "*")
public class ExportController {

		@Autowired
		private ExportRepo repo;
		
		private final int ROWS_PER_PAGE = 15;
	
	 	@GetMapping("/export_xlsx")
	    public void exportToExcel(HttpServletResponse response) throws IOException {	
	 		List<Project> projects  = repo.findAll();
	        // Create workbook and sheet
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet("Projects Info");
	        HSSFRow row   = sheet.createRow(0);
	        row.createCell(0).setCellValue("ProjectId");
	        row.createCell(1).setCellValue("ProjectName");
	        row.createCell(2).setCellValue("No Of Emp");
	        row.createCell(3).setCellValue("Project Manager");
	        row.createCell(4).setCellValue("Status");
	        row.createCell(5).setCellValue("Created On");
	        
	        int dataRowIndex = 1;
	        
	        for(Project prj : projects) {
	        	HSSFRow dataRow = sheet.createRow(dataRowIndex);
	        	dataRow.createCell(0).setCellValue(prj.getProjectId());
	        	dataRow.createCell(1).setCellValue(prj.getProjectName());
	        	dataRow.createCell(2).setCellValue(prj.getTeamSize());
	        	dataRow.createCell(3).setCellValue(prj.getCreatedBy());
	        	dataRow.createCell(4).setCellValue(prj.getStatus());
	        	dataRow.createCell(5).setCellValue(prj.getCreatedOn());
	        	dataRowIndex++;
	        }
	        
	        // Create a temporary file to save the workbook
	        File tempFile = File.createTempFile("table_data", ".xls");

	        // Write workbook data to the file
	        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
	            workbook.write(fileOutputStream);
	        }

	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=RMG_Projects.xlsx");

	        // Stream the file to the response
	        try (FileInputStream fileInputStream = new FileInputStream(tempFile)) {
	            IOUtils.copy(fileInputStream, response.getOutputStream());
	        }

	        // Delete the temporary file
	        tempFile.delete();
	        
	        // Close workbook
	        workbook.close();
	    }
	
	
	 	@GetMapping("/export_csv")
	 	public void exportToCsv(HttpServletResponse response) throws IOException {
	        // Fetch data from the database
	        List<Project> data = repo.findAll();

	        // Set response headers
	        response.setContentType("text/csv");
	        response.setHeader("Content-Disposition", "attachment; filename=RMG_Projects.csv");

	        // Write CSV content to the response
	        try (PrintWriter writer = response.getWriter()) {
	            // Write CSV header
	            writer.println("ProjectId,Project Name,No Of Emp,Project Manager,Status,Created On"); // Replace with your actual column names

	            // Write data rows
	            for (Project entity : data) {
	                writer.println(
	                        entity.getProjectId() + "," +
	                        entity.getProjectName() + "," +
	                        entity.getTeamSize() + "," +
	                        entity.getCreatedBy() + "," +
	                        entity.getStatus() + "," +
	                        entity.getCreatedOn()
	                        // Add more columns as needed
	                );
	            }
	        }
	    }
	 	
	 	
	 	@GetMapping("/export_doc") 
	    public void exportToWord(HttpServletResponse response) throws IOException {
	        // Fetch data from the database
	        List<Project> data = repo.findAll();

	        // Set response headers
	        response.setContentType("application/msword");
	        response.setHeader("Content-Disposition", "attachment; filename=RMG_Projects.docx");

	        // Create Word document
	        try (XWPFDocument document = new XWPFDocument()) {
	            XWPFParagraph paragraph = document.createParagraph();

	            // Write header
	            XWPFRun run = paragraph.createRun();
	            run.setText("ProjectId\tProject Name\tNo Of Emp\tProject Manager\tStatus\tCreated On");
	            run.addCarriageReturn();

	            // Write data rows
	            for (Project entity : data) {
	                run.setText(entity.getProjectId() + "\t" +
	                        entity.getProjectName() + "\t" +
	                        entity.getTeamSize() + "\t" +
	                        entity.getCreatedBy() + "\t" +
	                        entity.getStatus() + "\t" +
	                        entity.getCreatedOn());
	                run.addCarriageReturn();
	            }

	            // Save Word document to response stream
	            document.write(response.getOutputStream());
	        }
	    }

	 	@GetMapping("/export_pdf")
	 	 public void exportToPdf(HttpServletResponse response) throws IOException {
	         // Fetch data from the database
	         List<Project> data = repo.findAll();
	         // Set response headers
	         response.setContentType("application/pdf");
	         response.setHeader("Content-Disposition", "attachment; filename=data.pdf");

	         // Create PDF document
	         try (PDDocument document = new PDDocument()) {
	             addPages(document, data);

	             // Save PDF to response stream
	             document.save(response.getOutputStream());
	         }
	     }

	     private void addPages(PDDocument document, List<Project> data) throws IOException {
	         for (int i = 0; i < data.size(); i += ROWS_PER_PAGE) {
	             List<Project> pageData = data.subList(i, Math.min(i + ROWS_PER_PAGE, data.size()));
	             addPage(document, pageData);
	         }
	     }

	     private void addPage(PDDocument document, List<Project> data) throws IOException {
	         PDPage page = new PDPage();
	         document.addPage(page);

	         // Create content stream
	         try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	             contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
	             contentStream.beginText();
	             contentStream.newLineAtOffset(20, 700);
	             contentStream.setLeading(20F);
	             // Write header
	             contentStream.showText("ProjectId   Project Name   No Of Emp   Project Manager   Status   Created On");
	             contentStream.newLine();

	             // Write data rows
	            for (Project entity : data) {
	                 contentStream.showText(
	                         entity.getProjectId() + "   " +
	                         entity.getProjectName() + "   " +
	                         entity.getTeamSize() + "   " +
	                         entity.getCreatedBy() + "   " +
	                         entity.getStatus() + "   " +
	                         entity.getCreatedOn()
	                 );
	                 contentStream.newLine();
	             }
	             contentStream.endText();
	         }
	     }
	         @GetMapping("/export_png")
	         public ResponseEntity<byte[]> downloadImage() throws IOException {
	             // Load the image from the classpath
	             ClassPathResource resource = new ClassPathResource("static/images/ACOE3.png");

	             // Copy the image to a temporary file
	             Path tempFile = Files.createTempFile("download", ".png");
	             Files.copy(resource.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

	             // Set headers for the response
	             HttpHeaders headers = new HttpHeaders();
	             headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	             headers.setContentDispositionFormData("attachment", "sample.png");

	             // Read the content of the temporary file into a byte array
	             byte[] fileContent = Files.readAllBytes(tempFile);

	             // Return the response entity with the image content and headers
	             return ResponseEntity.ok()
	                     .headers(headers)
	                     .body(fileContent);
	         }

}
