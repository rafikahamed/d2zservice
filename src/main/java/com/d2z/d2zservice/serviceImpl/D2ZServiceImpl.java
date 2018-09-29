package com.d2z.d2zservice.serviceImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.TrackEventDetails;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.validation.D2ZValidator;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import uk.org.okapibarcode.backend.DataMatrix;
import uk.org.okapibarcode.backend.DataMatrix.ForceMode;
import uk.org.okapibarcode.backend.OkapiException;
import uk.org.okapibarcode.backend.Symbol;
import uk.org.okapibarcode.backend.Symbol.DataType;
import uk.org.okapibarcode.output.Java2DRenderer;

@Service
public class D2ZServiceImpl implements ID2ZService{
	
	@Autowired
    private ID2ZDao d2zDao;

	@Autowired
    private D2ZValidator d2zValidator;
	
	@Override
	public UserMessage exportParcel(List<FileUploadData> fileData) {
		List<FileUploadData> fileUploadData= d2zDao.exportParcel(fileData);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("File data upload successfully to the D2Z System");
		return userMsg;
	}
	
	@Override
	public UserMessage consignmentDelete(String refrenceNumlist) {
		String fileUploadData= d2zDao.consignmentDelete(refrenceNumlist);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(fileUploadData);
		return userMsg;
	}

	@Override
	public List<DropDownModel> fileList() {
		List<String> listOfFileNames= d2zDao.fileList();
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		for(String fileName:listOfFileNames) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(fileName);
			dropDownVaL.setValue(fileName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}
	
	@Override
	public List<TrackingDetails> trackingDetails(String fileName) {
		List<TrackingDetails> trackingDetailsList = new ArrayList<TrackingDetails>();
		TrackingDetails trackingDetails = null;
		// TODO Auto-generated method stub
		List<String> trackingService = d2zDao.trackingDetails(fileName);
		Iterator itr = trackingService.iterator();
		 while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 trackingDetails = new TrackingDetails();
			 trackingDetails.setRefrenceNumber(obj[0].toString());
			 trackingDetails.setConsigneeName(obj[1].toString());
			 trackingDetails.setBarCodeLabelNumber(obj[2].toString());
			 trackingDetailsList.add(trackingDetails);
        }
		return trackingDetailsList;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> fileData= d2zDao.consignmentFileData(fileName);
		return fileData;
	}

	@Override
	public byte[] generateLabel(List<SenderData> senderData) {
		
		for(SenderData data : senderData){
			data.setDatamatrixImage(generateDataMatrix(data.getDatamatrix()));
		}
		JRBeanCollectionDataSource beanColDataSource =
		         new JRBeanCollectionDataSource(senderData);
		 Map<String,Object> parameters = new HashMap<>();
		 byte[] bytes = null;
		 //Blob blob = null;
		    JasperReport jasperReport = null;
		    try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
		    	jasperReport  = JasperCompileManager.compileReport(getClass().getResource("/eparcelLabel.jrxml").openStream());
		        JRSaver.saveObject(jasperReport, "label.jasper");
		        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		      // return the PDF in bytes
		      bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			// blob = new javax.sql.rowset.serial.SerialBlob(bytes);
		    }
		    catch (JRException | IOException  e) {
		      e.printStackTrace();
		    }
		    return bytes;
	}

	private BufferedImage generateDataMatrix(String datamatrixInput) {
		BufferedImage datamatrixImage = null;
		 try{
	            // Set up the DataMatrix object
	            DataMatrix dataMatrix = new DataMatrix();
	            // We need a GS1 DataMatrix barcode.
	            dataMatrix.setDataType(DataType.GS1); 
	            // 0 means size will be set automatically according to amount of data (smallest possible).
	            dataMatrix.setPreferredSize(0); 
	            // Don't want no funky rectangle shapes, if we can avoid it.
	            dataMatrix.setForceMode(ForceMode.SQUARE); 
	            dataMatrix.setContent(datamatrixInput);
	            datamatrixImage= getMagnifiedBarcode(dataMatrix);
	            //return getBase64FromByteArrayOutputStream(getMagnifiedBarcode(dataMatrix, MAGNIFICATION));
	        } catch(OkapiException oe){
	        	oe.printStackTrace();
	        }
         return datamatrixImage;

		}

	private  BufferedImage getMagnifiedBarcode(Symbol symbol){
		 final int MAGNIFICATION = 10;
		 final int BORDER_SIZE = 0 * MAGNIFICATION;
        // Make DataMatrix object into bitmap
        BufferedImage image = new BufferedImage((symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
                (symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0,  (symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
                (symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE));
        Java2DRenderer renderer = new Java2DRenderer(g2d, 10, Color.WHITE, Color.BLACK);
        renderer.render(symbol);
        
        return image;
    }

	@Override
	public List<TrackParcel> trackParcel(List<String> referenceNumbers) {
		List<TrackParcel> trackParcelList  = new ArrayList<TrackParcel>();
		for(String refNbr :referenceNumbers ) {
			List<Trackandtrace> trackAndTraceList= d2zDao.trackParcel(refNbr);
			TrackParcel trackParcel = new TrackParcel();
			//List<TrackEventDetails> trackEventDetails = new ArrayList<TrackEventDetails>();
			for (Trackandtrace daoObj : trackAndTraceList) {
				trackParcel.setReferenceNumber(refNbr);
				trackParcel.setBarcodelabelNumber(daoObj.getBarcodelabelNumber());
				
				switch(daoObj.getTrackEventDetails().toUpperCase()) {
					
					case "CONSIGNMENT CREATED":
						trackParcel.setConsignmentCreated(daoObj.getTrackEventDateOccured());
						break;
					case "SHIPMENT ALLOCATED":
						trackParcel.setShipmentCreated(daoObj.getTrackEventDateOccured());
						break;
					case "HELD BY CUSTOMS":
						trackParcel.setHeldByCustoms(daoObj.getTrackEventDateOccured());
						break;
					case "CLEARED CUSTOMS":
						trackParcel.setClearedCustoms(daoObj.getTrackEventDateOccured());
						break;
					case "RECEIVED":
						trackParcel.setReceived(daoObj.getTrackEventDateOccured());
						break;
					case "PROCESSED BY FACILITY":
						trackParcel.setProcessedByFacility(daoObj.getTrackEventDateOccured());
						break;
					case "IN TRANSIT":
						trackParcel.setInTransit(daoObj.getTrackEventDateOccured());
						break;
					case "DELIVERED":
						trackParcel.setDelivered(daoObj.getTrackEventDateOccured());
						break;
				}
				
				/*TrackEventDetails trackEventDetail =  new TrackEventDetails();
				trackEventDetail.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackEventDetail.setTrackEventDetails(daoObj.getTrackEventDetails());
				
				trackEventDetails.add(trackEventDetail);
				trackParcel.setTrackEventDetails(trackEventDetails);*/

			}
			trackParcelList.add(trackParcel);

		}
		
		return trackParcelList;
	}

	@Override
	public List<SenderDataResponse> createConsignments(List<SenderData> orderDetailList) throws ReferenceNumberNotUniqueException {
		
		d2zValidator.isReferenceNumberUnique(orderDetailList);
		d2zValidator.isPostCodeValid(orderDetailList);
		String senderFileID = d2zDao.createConsignments(orderDetailList);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		List<SenderDataResponse> senderDataResponseList = new ArrayList<SenderDataResponse>();
		SenderDataResponse senderDataResponse = null;
		Iterator itr = insertedOrder.iterator();
		 while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 senderDataResponse = new SenderDataResponse();
			 senderDataResponse.setReferenceNumber(obj[0].toString());
			 senderDataResponse.setBarcodeLabelNumber("]d2".concat(obj[1].toString().replaceAll("\\[|\\]", "")));
			 senderDataResponseList.add(senderDataResponse);
       }
		/*List<SenderDataResponse> senderDataResponse = insertedOrder.stream().map(daoObj -> {
			SenderDataResponse senderData = new SenderDataResponse();
			senderData.setReferenceNumber(daoObj.getReference_number());
			String barcodeLabelNumber = "]d2".concat(daoObj.getDatamatrix().replaceAll("\\[|\\]", ""));
			senderData.setBarcodeLabelNumber(barcodeLabelNumber);
			return senderData;
		}).collect(Collectors.toList());*/
		
		return senderDataResponseList;
	}

}
