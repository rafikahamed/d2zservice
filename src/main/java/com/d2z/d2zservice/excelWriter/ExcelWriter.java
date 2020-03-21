package com.d2z.d2zservice.excelWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.model.PerformanceReportData;
import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.ShipmentDetails;
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
      sheet.autoSizeColumn(0);
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
	public byte[] generateShipmentReport(IncomingJobs incomingJobs) {
		try {

			   Workbook workbook = new XSSFWorkbook();
			   Sheet sheet = workbook.createSheet("MYSheet");


			   InputStream inputStream = new FileInputStream("src/main/resources/D2Z.jpg");

			   byte[] imageBytes = IOUtils.toByteArray(inputStream);

			   int pictureureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

			   inputStream.close();

			   CreationHelper helper = workbook.getCreationHelper();

			   Drawing drawing = sheet.createDrawingPatriarch();

			   ClientAnchor anchor = helper.createClientAnchor();

			   anchor.setCol1(2);
			   anchor.setRow1(7);

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
			   fillShipmentSummaryData("SURPLUS/SHORTAGE",incomingJobs.getSurplusShortage(),16,sheet,workbook);
			   fillShipmentSummaryData("DAMAGE/OTHER",incomingJobs.getDamageNotes(),17,sheet,workbook);
			   
			   
			   Row row10 = sheet.createRow(19);
			   row10.createCell(0).setCellValue("ARTICLE ID");
			   row10.createCell(1).setCellValue("SURPLUS/SHORTAGE");
			   
			   for(int i = 0; i < 4; i++) { sheet.autoSizeColumn(i); }
			   File fileNm = new File("src/main/resources/ShipmentReport.xlsx");
			   FileOutputStream fileOut =  new FileOutputStream(fileNm);
			   workbook.write(fileOut);
			   fileOut.close();
			}catch (Exception e) {
			   System.out.println(e);
			}
    return null;
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
}
