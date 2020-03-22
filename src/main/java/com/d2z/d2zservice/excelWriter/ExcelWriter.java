package com.d2z.d2zservice.excelWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.model.EmailEnquiryDetails;
import com.d2z.d2zservice.model.PerformanceReportData;

@Service
public class ExcelWriter {

	public byte[] generatePerformance(List<PerformanceReportData> performanceReportData) {
	      String[] columns = {"ArticleID","Consignee Name","Consignee Address1","Consignee Adress2","City","State","Postcode","Arrive Date",
	    		  "Clearance Date","Lodgement Date","Latest Trackin Status","Latest Tracking Date/Time"};
	      
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Performance Report");
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();//Create font
	    font.setBold(true);//Make font bold
	    style.setFont(font);
		Row headerRow = sheet.createRow(0);
		for(int i = 0; i < columns.length; i++) {
          Cell cell = headerRow.createCell(i);
          cell.setCellValue(columns[i]);
          cell.setCellStyle(style);
		}
		int rowNum = 1;
      for(PerformanceReportData data : performanceReportData) {
          Row row = sheet.createRow(rowNum++);
          row.createCell(0).setCellValue(data.getArticleId());
          row.createCell(1).setCellValue(data.getConsigneeName());
          row.createCell(2).setCellValue(data.getConsigneeAddr1());
          row.createCell(3).setCellValue(data.getConsigneeAddr2());
          row.createCell(4).setCellValue(data.getCity());
          row.createCell(5).setCellValue(data.getState());
          row.createCell(6).setCellValue(data.getPostcode());
          row.createCell(7).setCellValue(data.getArriveDate());
          row.createCell(8).setCellValue(data.getClearanceDate());
          row.createCell(9).setCellValue(data.getLodgementDate());
          row.createCell(10).setCellValue(data.getLatestTrackingStatus());
          row.createCell(11).setCellValue(data.getLatestTrackingTimestamp());
       }
      for(int i = 0; i < columns.length; i++) {
          sheet.autoSizeColumn(i);
      }
		/*
		 * try { File fileNm = new
		 * File("src/main/resources/PerformanceReport/PerformanceReport.xlsx");
		 * FileOutputStream file = new FileOutputStream(fileNm); if (!fileNm.exists()) {
		 * fileNm.createNewFile(); } workbook.write(file); file.close();
		 * workbook.close(); } catch (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */
      byte[] xls = null;
      try {
      	ByteArrayOutputStream baos = new ByteArrayOutputStream();
      	workbook.write(baos);
      	 xls = baos.toByteArray();
      	workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return xls;
	}

	
	public byte[] generateEnquiryReport(List<EmailEnquiryDetails> enquiryDetails) {
		//Header for Enquiry Reports
		String[] columns = {"Ticket ID","Article ID","Reference Number","Enquiry","POD","Comments","D2Z Comments","Consignee Name",
	    		  "Tracking Event","Tracking Date"};
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Outstanding Enquiry Report");
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();//Create font
	    font.setBold(true);//Make font bold
	    style.setFont(font);
		Row headerRow = sheet.createRow(0);
		for(int i = 0; i < columns.length; i++) {
         Cell cell = headerRow.createCell(i);
         cell.setCellValue(columns[i]);
         cell.setCellStyle(style);
		}
		int rowNum = 1;
     for(EmailEnquiryDetails enquiry : enquiryDetails) {
         Row row = sheet.createRow(rowNum++);
         row.createCell(0).setCellValue(enquiry.getTicketId());
         row.createCell(1).setCellValue(enquiry.getArticleId());
         row.createCell(2).setCellValue(enquiry.getReferenceNumber());
         row.createCell(3).setCellValue(enquiry.getDeliveryEnquiry());
         row.createCell(4).setCellValue(enquiry.getPod());
         row.createCell(5).setCellValue(enquiry.getComments());
         row.createCell(6).setCellValue(enquiry.getD2zComments());
         row.createCell(7).setCellValue(enquiry.getConsignee_Name());
         row.createCell(8).setCellValue(enquiry.getTrackingEvent());
         row.createCell(9).setCellValue(enquiry.getTrackingEventDateOccured());
      	}
     
	     for(int i = 0; i < columns.length; i++) {
	         sheet.autoSizeColumn(i);
	     }
	
	     byte[] xls = null;
	     try {
	     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     	workbook.write(baos);
	     	 xls = baos.toByteArray();
	     	workbook.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     return xls;
	}
	
}
