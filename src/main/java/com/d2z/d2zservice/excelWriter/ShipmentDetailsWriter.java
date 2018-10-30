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

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.ShipmentDetails;
@Service
public class ShipmentDetailsWriter {

	public byte[] generateShipmentxls(List<ShipmentDetails> shipmentDetails) {
	      String[] columns = {"CUSTOMER_REF", "CONNOTE NO.", "WEIGHT", "CNEE","CNEE COMPANY","TEL","ADDRESS","SUBURB","STATE","P/C","DESTINATION",
	    		  "PCS","COMMODITY","INNER ITEMS","UNIT VALUE","TTL VALUE","CMETER","SHIPPER","SHIPPER ADD","SHIPPER CITY","SHIPPER STATE","SHIPPER PC",
	    		  "SHIPPER COUNTRY CODE","SHIPPER CONTACT","INSURANCE","RECEIVER","RECEIVER TEL","RECEIVER ADDRESS","RECEIVER SUBURB","RECEIVER STATE","RECEIVER P/C",
	    		  "CLEAR","FBA PO","FBA SHIPMENT ID","INVOICE_REF","IMPORTER_ABN","VENDOR_ID","CONSIGNOR_TIN"};
	      
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Shipment Details");
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
		System.out.println(shipmentDetails.size());
        for(ShipmentDetails shipmentDetail : shipmentDetails) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(shipmentDetail.getReferenceNumber());
            row.createCell(1).setCellValue(shipmentDetail.getCon_no());
            row.createCell(2).setCellValue(shipmentDetail.getWeight());
            row.createCell(3).setCellValue(shipmentDetail.getConsigneeName());
            row.createCell(4).setCellValue(shipmentDetail.getConsigneeCompany());
            row.createCell(5).setCellValue(shipmentDetail.getConsigneePhone());
            row.createCell(6).setCellValue(shipmentDetail.getConsigneeAddress());
            row.createCell(7).setCellValue(shipmentDetail.getConsigneeSuburb());
            row.createCell(8).setCellValue(shipmentDetail.getConsigneeState());
            row.createCell(9).setCellValue(shipmentDetail.getConsigneePostcode());
            row.createCell(10).setCellValue(shipmentDetail.getDestination());
            row.createCell(11).setCellValue(shipmentDetail.getQuantity());
            row.createCell(12).setCellValue(shipmentDetail.getCommodity());
            row.createCell(13).setCellValue(shipmentDetail.getQuantity());
            row.createCell(14).setCellValue(shipmentDetail.getValue());
            row.createCell(15).setCellValue(shipmentDetail.getValue());
            row.createCell(16).setCellValue(shipmentDetail.getCmeter());
            row.createCell(17).setCellValue(shipmentDetail.getShipperName());
            row.createCell(18).setCellValue(shipmentDetail.getShipperAddress());
            row.createCell(19).setCellValue(shipmentDetail.getShipperCity());
            row.createCell(20).setCellValue(shipmentDetail.getShipperState());
            row.createCell(21).setCellValue(shipmentDetail.getShipperPostcode());
            row.createCell(22).setCellValue(shipmentDetail.getShipperCountry());
            row.createCell(23).setCellValue(shipmentDetail.getShipperContact());
            row.createCell(24).setCellValue(shipmentDetail.getInsurance());
            row.createCell(25).setCellValue(shipmentDetail.getConsigneeName());
            row.createCell(26).setCellValue(shipmentDetail.getConsigneePhone());
            row.createCell(27).setCellValue(shipmentDetail.getConsigneeAddress());
            row.createCell(28).setCellValue(shipmentDetail.getConsigneeSuburb());
            row.createCell(29).setCellValue(shipmentDetail.getConsigneeState());
            row.createCell(30).setCellValue(shipmentDetail.getConsigneePostcode());
            row.createCell(31).setCellValue(shipmentDetail.getClear());
            row.createCell(32).setCellValue(shipmentDetail.getFBAPO());
            row.createCell(33).setCellValue(shipmentDetail.getFBAShipmentID());
            row.createCell(34).setCellValue(shipmentDetail.getInvoiceRef());
            row.createCell(35).setCellValue(shipmentDetail.getImporterAbn());
            row.createCell(36).setCellValue(shipmentDetail.getVendorId());
            row.createCell(36).setCellValue(shipmentDetail.getConsignorTin());

            
        }
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
      /*  try {
			FileOutputStream file = new FileOutputStream("shipment.xlsx");
			workbook.write(file);
			file.close();
			workbook.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
        
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

	public byte[] generateDeleteConsignmentsxls(List<SenderdataMaster> deletedConsignments) {
		 String[] columns = {"REF NO.", "ARTICLE ID"};
	      
	 
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Deleted Consignments");
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
       for(SenderdataMaster data : deletedConsignments) {
           Row row = sheet.createRow(rowNum++);
           row.createCell(0).setCellValue(data.getReference_number());
           row.createCell(1).setCellValue(data.getBarcodelabelNumber().substring(18));
       }
       for(int i = 0; i < columns.length; i++) {
           sheet.autoSizeColumn(i);
       }
     /*  try {
			FileOutputStream file = new FileOutputStream("shipment.xlsx");
			workbook.write(file);
			file.close();
			workbook.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
       
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
