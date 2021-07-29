package com.d2z.d2zservice.excelWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.model.EmailEnquiryDetails;
import com.d2z.d2zservice.model.EmailReturnDetails;
import com.d2z.d2zservice.model.HeldParcelDetails;
import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.model.PerformanceReportData;
import com.d2z.d2zservice.model.PerformanceReportData;
import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.SurplusData;

@Service
public class ExcelWriter {

	public byte[] generatePerformance(List<PerformanceReportData> performanceReportData) {
	      String[] columns = {"ArticleID","Consignee Name","Consignee Address1","Consignee Adress2","City","State","Postcode","Arrive Date","MAWB",
	    		  "Clearance Date","Lodgement Date","Latest Trackin Status","Latest Tracking Date/Time"};
	      
	 
		Workbook workbook = new SXSSFWorkbook();
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
          row.createCell(8).setCellValue(data.getMawb());
          row.createCell(9).setCellValue(data.getClearanceDate());
          row.createCell(10).setCellValue(data.getLodgementDate());
          row.createCell(11).setCellValue(data.getLatestTrackingStatus());
          row.createCell(12).setCellValue(data.getLatestTrackingTimestamp());
       }
     // sheet.autoSizeColumn(0);
		/*
		 * for(int i = 0; i < columns.length; i++) { sheet.autoSizeColumn(i); }
		 */
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
		Sheet sheet = workbook.createSheet("Enquiry Report");
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
	
	public byte[] generateShipmentReport(IncomingJobResponse incomingJobs, List<SurplusData> surplusData) {
		byte[] xls = null;
		try {
			   Workbook workbook = new XSSFWorkbook();
			   Sheet sheet = workbook.createSheet("ShipmentSummary");
			   InputStream inputStream = getClass().getClassLoader().getResourceAsStream("D2Z.jpg");
               System.out.println(inputStream);
			   byte[] imageBytes = IOUtils.toByteArray(inputStream);
			   int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_JPEG);
			   inputStream.close();

			   CreationHelper helper = workbook.getCreationHelper();
			   Drawing drawing = sheet.createDrawingPatriarch();
			   ClientAnchor anchor = helper.createClientAnchor();
			   anchor.setCol1(0);
			   anchor.setRow1(1);
			   anchor.setCol2(2);
			   anchor.setRow2(7);
			   drawing.createPicture(anchor, pictureureIdx);
			   
			   Row row = sheet.createRow(8);
			   Cell header = row.createCell(0);
			   Cell header1 = row.createCell(0);
			   header.setCellValue("SHIPMENT SUMMARY");
			   CellStyle style = workbook.createCellStyle();
			   style.setAlignment(HorizontalAlignment.CENTER);
			   header.setCellStyle(style);
			   header1.setCellStyle(style);
			   CellRangeAddress range = new CellRangeAddress(8,8,0,1);
			   sheet.addMergedRegion(range);
			  
			   RegionUtil.setBorderBottom(BorderStyle.THIN, range, sheet);
			   RegionUtil.setBorderTop(BorderStyle.THIN, range, sheet);
			   RegionUtil.setBorderRight(BorderStyle.THIN, range, sheet);
			   RegionUtil.setBorderLeft(BorderStyle.THIN, range, sheet);

			   fillShipmentSummaryData("MAWB",incomingJobs.getMawb(),9,sheet,workbook);
			   fillShipmentSummaryData("DESTINATION",incomingJobs.getDestination(),10,sheet,workbook);
			   fillShipmentSummaryData("ATA",null!=incomingJobs.getAta()?incomingJobs.getAta().toString():"",11,sheet,workbook);
			   fillShipmentSummaryData("CLEARANCE DATE",null!=incomingJobs.getClearanceDate()?incomingJobs.getClearanceDate().toString():"",12,sheet,workbook);
			   fillShipmentSummaryData("INJECTION DATE",null!=incomingJobs.getInjectionDate()?incomingJobs.getInjectionDate().toString():"",13,sheet,workbook);
			   fillShipmentSummaryData("CLEAR",incomingJobs.getClear(),14,sheet,workbook);
			   fillShipmentSummaryData("HELD",incomingJobs.getHeld(),15,sheet,workbook);
//			   fillShipmentSummaryData("SURPLUS/SHORTAGE",incomingJobs.getSurplus(),16,sheet,workbook);
//			   fillShipmentSummaryData("DAMAGE/OTHER",incomingJobs.getDamage(),17,sheet,workbook);
			   
			   fillShipmentSummaryData("ARTICLE ID","STATUS","COMMENTS",17,sheet,workbook);
			   int rowNum = 18;
			      for(SurplusData data : surplusData) {
			          Row surplusrow = sheet.createRow(rowNum++);
			          surplusrow.createCell(0).setCellValue(data.getArticleId());
			          surplusrow.createCell(1).setCellValue(data.getStatus());
			          surplusrow.createCell(2).setCellValue(data.getNote());
			      }
			   for(int i = 0; i < 4; i++){ 
				   sheet.autoSizeColumn(i); 
			   }
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
		       workbook.write(baos);
		       xls = baos.toByteArray();
		       workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	     return xls;
	}

	private void fillShipmentSummaryData(String label, String value, int rowNo, Sheet sheet, Workbook workbook) {
		   Row row = sheet.createRow(rowNo);
		   Cell labelCell = row.createCell(0);
		   Cell valueCell = row.createCell(1);
		   labelCell.setCellValue(label);
		   valueCell.setCellValue(value);	
		   
		   CellStyle style = workbook.createCellStyle();
		   style.setBorderBottom(BorderStyle.THIN);
		   style.setBorderLeft(BorderStyle.THIN);
		   style.setBorderRight(BorderStyle.THIN);
		   style.setBorderTop(BorderStyle.THIN);
		   
		   labelCell.setCellStyle(style);
		   valueCell.setCellStyle(style);
	}

	private void fillShipmentSummaryData(String label1, String label2, String label3, int rowNo, Sheet sheet, Workbook workbook) {
		   Row row = sheet.createRow(rowNo);
		   Cell labelCell1 = row.createCell(0);
		   Cell labelCell2 = row.createCell(1);
		   Cell labelCell3 = row.createCell(2);
		   labelCell1.setCellValue(label1);
		   labelCell2.setCellValue(label2);	
		   labelCell3.setCellValue(label3);	
		   
		   CellStyle style = workbook.createCellStyle();
		   style.setBorderBottom(BorderStyle.THIN);
		   style.setBorderLeft(BorderStyle.THIN);
		   style.setBorderRight(BorderStyle.THIN);
		   style.setBorderTop(BorderStyle.THIN);
		   
		   labelCell1.setCellStyle(style);
		   labelCell2.setCellStyle(style);
		   labelCell3.setCellStyle(style);
	}

	public byte[] generateReturnsReport(List<EmailReturnDetails> enquiryVal) {
		
		//Header for Enquiry Reports
		String[] columns = {"Article ID","Reference Number","Consignee Name","Client Name","Return Reason","Shipment Number","Scanned Date"};
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Returns Report");
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
		
		for(EmailReturnDetails returns : enquiryVal) {
	         Row row = sheet.createRow(rowNum++);
	         row.createCell(0).setCellValue(returns.getArticleId());
	         row.createCell(1).setCellValue(returns.getReferenceNumber());
	         row.createCell(2).setCellValue(returns.getConsigneeName());
	         row.createCell(3).setCellValue(returns.getClientName());
	         row.createCell(4).setCellValue(returns.getReturnReason());
	         row.createCell(5).setCellValue(returns.getAirwaybill());
	         row.createCell(6).setCellValue(returns.getReturnsCreatedDate());
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


	public byte[] generateParcelReport(List<HeldParcelDetails> parcelDetails) {

		//Header for Held Reports
		String[] columns = {"MAWB","HAWB","NOTE","CLIENT","POD","STATUS"};
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Held Report");
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
		
		for(HeldParcelDetails enquiry : parcelDetails) {
	         Row row = sheet.createRow(rowNum++);
	         row.createCell(0).setCellValue(enquiry.getMawb());
	         row.createCell(1).setCellValue(enquiry.getHawb());
	         row.createCell(2).setCellValue(enquiry.getNote());
	         row.createCell(3).setCellValue(enquiry.getClient());
	         row.createCell(4).setCellValue(enquiry.getPod());
	         row.createCell(5).setCellValue(enquiry.getStatus());
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



	public byte[] generateMonitoringReport(Map<String, List<String>> monitoringMap) {
		Workbook workbook = new XSSFWorkbook();

		monitoringMap.forEach((key,value) -> {
			//Header for Enquiry Reports
			String[] columns = {"Article ID"};
		 
			Sheet sheet = workbook.createSheet(key);
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
			
			for(String articleID : value) {
				String[] arr = articleID.split(",");
				System.out.println(arr);
		         Row row = sheet.createRow(rowNum++);
		         row.createCell(0).setCellValue(articleID);
	      	}
	     
		     for(int i = 0; i < columns.length; i++) {
		         sheet.autoSizeColumn(i);
		     }
		});
		
	
	
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
