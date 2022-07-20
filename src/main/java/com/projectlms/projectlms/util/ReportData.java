package com.projectlms.projectlms.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.projectlms.projectlms.domain.dao.EnrolledCourse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportData {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD); 
    private static Font detFont = new Font(Font.FontFamily.TIMES_ROMAN, 12); 

    public static ByteArrayInputStream courseReportData(EnrolledCourse enrolledCourses) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            log.info("Add title and detail");
            Paragraph paragraph = new Paragraph();

            paragraph = new Paragraph("Data Report " +  enrolledCourses.getCourse().getCourseName(), catFont);
            paragraph.setAlignment(Element.ALIGN_CENTER);

            paragraph.setIndentationLeft(65);
            paragraph.setIndentationRight(65);
            paragraph.add(new Paragraph(" "));
            paragraph.add(new Paragraph(" "));

            document.add(paragraph);

            paragraph = new Paragraph("Name               :     " + enrolledCourses.getUser().getFirstname().toUpperCase() + " " + enrolledCourses.getUser().getLastname().toUpperCase(), detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);

            document.add(paragraph);

            paragraph = new Paragraph("Course             :     " + enrolledCourses.getCourse().getCourseName(), detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);

            document.add(paragraph);

            paragraph = new Paragraph("Specialization  :     " + enrolledCourses.getCourse().getCategory().getCategoryName(), detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            
            document.add(paragraph);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = enrolledCourses.getCreatedAt().format(formatter);

            paragraph = new Paragraph("Start Enrolled  :     " + formatDateTime, detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);

            document.add(paragraph);

            paragraph = new Paragraph("Progress           :     " + enrolledCourses.getProgress() + " %", detFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.add(new Paragraph(" "));
            
            document.add(paragraph);


            log.info("Add table");

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 2, 4, 3});

            Font headFont = FontFactory.getFont(FontFactory.TIMES_BOLD);
            
            PdfPCell hcell;

            hcell = new PdfPCell(new Phrase("No.", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Type", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Material", headFont));
            hcell.setPadding(5);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Score - Status", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            log.info("Add table cells");

            PdfPCell cell;
            Boolean check = true;

            for(int i = 0; i<enrolledCourses.getReports().size(); i++) {
                cell = new PdfPCell(new Phrase(String.valueOf(i+1)));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(
                    new Phrase(enrolledCourses.getReports().get(i).getMaterial().getMaterialType().toUpperCase()));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                cell = new PdfPCell(
                    new Phrase(enrolledCourses.getReports().get(i).getMaterial().getMaterialName()));
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);

                if (check.equals(enrolledCourses.getReports().get(i).getIsCompleted())) {
                    cell = new PdfPCell(
                        new Phrase(" 100  -    Completed   "));
                    cell.setPadding(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(
                        new Phrase("    0   -  Uncompleted "));
                    cell.setPadding(5);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
            }

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            log.error("Create report data error failed");
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    
}
