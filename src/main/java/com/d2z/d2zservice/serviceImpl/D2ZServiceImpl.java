package com.d2z.d2zservice.serviceImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.validation.Valid;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.boot.json.JacksonJsonParser;

import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.excelWriter.ShipmentDetailsWriter;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.APIRatesRequest;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DeleteConsignmentRequest;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.Ebay_Shipment;
import com.d2z.d2zservice.model.Ebay_ShipmentDetails;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.FDMManifestDetails;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.PostCodeWeight;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.TrackingEvents;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.auspost.CreateShippingRequest;
import com.d2z.d2zservice.model.auspost.FromAddress;
import com.d2z.d2zservice.model.auspost.Items;
import com.d2z.d2zservice.model.auspost.ShipmentRequest;
import com.d2z.d2zservice.model.auspost.ToAddress;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.fdm.ArrayOfConsignment;
import com.d2z.d2zservice.model.fdm.ArrayofDetail;
import com.d2z.d2zservice.model.fdm.Consignment;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.d2z.d2zservice.model.fdm.Line;
import com.d2z.d2zservice.proxy.AusPostProxy;
import com.d2z.d2zservice.proxy.EbayProxy;
import com.d2z.d2zservice.proxy.FDMProxy;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.util.EmailUtil;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.FreipostWrapper;
import com.d2z.singleton.D2ZSingleton;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
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
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ShipmentDetailsWriter shipmentWriter;
	
	@Autowired
    private ID2ZBrokerDao d2zBrokerDao;

	@Autowired
	private EbayProxy proxy;
	
	@Autowired
	EmailUtil emailUtil; 
	
	@Autowired
	FreipostWrapper freipostWrapper; 
	
	@Autowired
	AusPostProxy ausPostProxy;
	
	@Autowired
	FDMProxy fdmProxy;
	
	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Override
	public List<SenderDataResponse> exportParcel(List<SenderData> orderDetailList) throws ReferenceNumberNotUniqueException{
		d2zValidator.isReferenceNumberUniqueUI(orderDetailList);
		d2zValidator.isServiceValidUI(orderDetailList);
		d2zValidator.isPostCodeValidUI(orderDetailList);
		String senderFileID  = d2zDao.exportParcel(orderDetailList);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		List<SenderDataResponse> senderDataResponseList = new ArrayList<SenderDataResponse>();
		SenderDataResponse senderDataResponse = null;
		Iterator itr = insertedOrder.iterator();
		 while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 senderDataResponse = new SenderDataResponse();
			 senderDataResponse.setReferenceNumber(obj[0].toString());
			 if(obj[1] != null)
				 senderDataResponse.setBarcodeLabelNumber("]d2".concat(obj[1].toString().replaceAll("\\[|\\]", "")));
			 senderDataResponseList.add(senderDataResponse);
        }
		return senderDataResponseList;
	}
	
	@Override
	public UserMessage consignmentDelete(String refrenceNumlist) {
		String fileUploadData= d2zDao.consignmentDelete(refrenceNumlist);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(fileUploadData);
		return userMsg;
	}

	@SuppressWarnings("rawtypes")
	public List<DropDownModel> fileList(Integer userId) {
		List<String> listOfFileNames= d2zDao.fileList(userId);
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		Iterator itr = listOfFileNames.iterator();
		while(itr.hasNext()) {   
			Object[] obj = (Object[]) itr.next();
			DropDownModel dropDownVal = new DropDownModel();
			dropDownVal.setName(obj[0].toString());
			dropDownVal.setValue(obj[0].toString());
			dropDownList.add(dropDownVal);
		}
		return dropDownList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<DropDownModel> labelFileList(Integer userId) {
		List<String> listOfFileNames= d2zDao.labelFileList(userId);
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		Iterator itr = listOfFileNames.iterator();
		while(itr.hasNext()) {   
			Object[] obj = (Object[]) itr.next();
			DropDownModel dropDownVal = new DropDownModel();
			dropDownVal.setName(obj[0].toString());
			dropDownVal.setValue(obj[0].toString());
			dropDownList.add(dropDownVal);
		}
		return dropDownList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TrackingDetails> trackingDetails(String fileName) {
		List<TrackingDetails> trackingDetailsList = new ArrayList<TrackingDetails>();
		TrackingDetails trackingDetails = null;
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

	/*@Override
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
*/
	
	@Override
	public byte[] generateLabel(List<SenderData> senderData) {
		
		List<SenderData> eParcelData = new ArrayList<SenderData>();
		
		List<SenderData> expressData =  new ArrayList<SenderData>();
		
		
		for(SenderData data : senderData){
			if(data.getCarrier().equalsIgnoreCase("eParcel")) {
				eParcelData.add(data);
			}
			else if(data.getCarrier().equalsIgnoreCase("Express")) {
				expressData.add(data);
			}
			data.setDatamatrixImage(generateDataMatrix(data.getDatamatrix()));
		}
		
		 Map<String,Object> parameters = new HashMap<>();
		 byte[] bytes = null;
		 JRBeanCollectionDataSource eParcelDataSource;
		 JRBeanCollectionDataSource expressDataSource;
		    JasperReport eParcelLabel= null;
		    JasperReport expressLabel = null;  
		    try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
		    	List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		    	if(!eParcelData.isEmpty()) {
		    		System.out.println("Generating eParcel..."+eParcelData.size());
		    		eParcelDataSource = new JRBeanCollectionDataSource(eParcelData);
		    		eParcelLabel  = JasperCompileManager.compileReport(getClass().getResource("/eparcelLabel.jrxml").openStream());
		    		JRSaver.saveObject(eParcelLabel, "label.jasper");
		    		jasperPrintList.add(JasperFillManager.fillReport(eParcelLabel, parameters, eParcelDataSource));

		    	}
		    	if(!expressData.isEmpty()) {
		    		System.out.println("Generating Express..."+expressData.size());
		    		expressDataSource = new JRBeanCollectionDataSource(expressData);
		    		expressLabel  = JasperCompileManager.compileReport(getClass().getResource("/ExpressLabel.jrxml").openStream());
			        JRSaver.saveObject(expressLabel, "express.jasper");
		    		jasperPrintList.add(JasperFillManager.fillReport(expressLabel, parameters, expressDataSource));
			    	}
		    	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		    	SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
		    		      outputStream);
		    	JRPdfExporter exporter = new JRPdfExporter();
		    	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)); 
		    	exporter.setExporterOutput(exporterOutput);
		    	exporter.exportReport();
		    		      // return the PDF in bytes
		    	bytes = outputStream.toByteArray();
		      //bytes = JasperExportManager.exportReportToPdf(jasperPrint);
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
	public  byte[] trackingLabel(List<String> refBarNum) {
		List<SenderData> trackingLabelList = new ArrayList<SenderData>();
		List<String> trackingLabelData = d2zDao.trackingLabel(refBarNum);
		Iterator itr = trackingLabelData.iterator();
		while(itr.hasNext()) {   
			SenderData trackingLabel = new SenderData();
			Object[] trackingArray = (Object[]) itr.next();
			if(trackingArray[0] != null)
				trackingLabel.setReferenceNumber(trackingArray[0].toString());
			if(trackingArray[1] != null)
				trackingLabel.setConsigneeName(trackingArray[1].toString());
			if(trackingArray[2] != null)
				trackingLabel.setConsigneeAddr1(trackingArray[2].toString());
			if(trackingArray[3] != null)
				trackingLabel.setConsigneeSuburb(trackingArray[3].toString());
			if(trackingArray[4] != null)
				trackingLabel.setConsigneeState(trackingArray[4].toString());
			if(trackingArray[5] != null)
				trackingLabel.setConsigneePostcode(trackingArray[5].toString());
			if(trackingArray[6] != null)
				trackingLabel.setConsigneePhone(trackingArray[6].toString());
			if(trackingArray[7] != null)
				trackingLabel.setWeight(trackingArray[7].toString());
			if(trackingArray[8] != null)
				trackingLabel.setShipperName(trackingArray[8].toString());
			if(trackingArray[9] != null)
				trackingLabel.setShipperAddr1(trackingArray[9].toString());
			if(trackingArray[10] != null)
				trackingLabel.setShipperCity(trackingArray[10].toString());
			if(trackingArray[11] != null)
				trackingLabel.setShipperState(trackingArray[11].toString());
			if(trackingArray[12] != null)
				trackingLabel.setShipperCountry(trackingArray[12].toString());
			if(trackingArray[13] != null)
				trackingLabel.setShipperPostcode(trackingArray[13].toString());
			if(trackingArray[14] != null)
				trackingLabel.setBarcodeLabelNumber(trackingArray[14].toString());
			if(trackingArray[15] != null)
				trackingLabel.setDatamatrix(trackingArray[15].toString());
			if(trackingArray[16] != null)
				trackingLabel.setInjectionState(trackingArray[16].toString());
			if(trackingArray[17] != null)
				trackingLabel.setSku(trackingArray[17].toString());
			if(trackingArray[18] != null)
				trackingLabel.setLabelSenderName(trackingArray[18].toString());
			if(trackingArray[19] != null)
				trackingLabel.setDeliveryInstructions(trackingArray[19].toString());
			if(trackingArray[20] != null)
				trackingLabel.setConsigneeCompany(trackingArray[20].toString());
			if(trackingArray[21] != null)
				trackingLabel.setCarrier(trackingArray[21].toString());
			if(trackingArray[22] != null)
				trackingLabel.setConsigneeAddr2(trackingArray[22].toString());
			if(trackingArray[23] != null)
				trackingLabel.setReturnAddress1(trackingArray[23].toString());
			if(trackingArray[24] != null)
				trackingLabel.setReturnAddress2(trackingArray[24].toString());
			
			trackingLabel.setDatamatrixImage(generateDataMatrix(trackingLabel.getDatamatrix()));
			trackingLabelList.add(trackingLabel);
		 }
		
		/*JRBeanCollectionDataSource beanColDataSource =
		         new JRBeanCollectionDataSource(trackingLabelList);
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
		    return bytes;*/
		List<SenderData> eParcelData = new ArrayList<SenderData>();
		
		List<SenderData> expressData =  new ArrayList<SenderData>();
		
		for(SenderData data : trackingLabelList) {
			if(data.getCarrier().equalsIgnoreCase("eParcel")) {
				eParcelData.add(data);
			}
			else if(data.getCarrier().equalsIgnoreCase("Express")) {
				expressData.add(data);
			}
		}
	
		 Map<String,Object> parameters = new HashMap<>();
		 byte[] bytes = null;
		 //Blob blob = null;
		 JRBeanCollectionDataSource eParcelDataSource;
		 JRBeanCollectionDataSource expressDataSource;
		    JasperReport eParcelLabel= null;
		    JasperReport expressLabel = null;  
		    try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
		    	List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
		    	if(!eParcelData.isEmpty()) {
		    		System.out.println("Generating eParcel..."+eParcelData.size());
		    		eParcelDataSource = new JRBeanCollectionDataSource(eParcelData);
		    		eParcelLabel  = JasperCompileManager.compileReport(getClass().getResource("/eparcelLabel.jrxml").openStream());
		    		JRSaver.saveObject(eParcelLabel, "label.jasper");
		    		jasperPrintList.add(JasperFillManager.fillReport(eParcelLabel, parameters, eParcelDataSource));

		    	}
		    	if(!expressData.isEmpty()) {
		    		System.out.println("Generating Express..."+expressData.size());
		    		expressDataSource = new JRBeanCollectionDataSource(expressData);
		    		expressLabel  = JasperCompileManager.compileReport(getClass().getResource("/ExpressLabel.jrxml").openStream());
			        JRSaver.saveObject(expressLabel, "express.jasper");
		    		jasperPrintList.add(JasperFillManager.fillReport(expressLabel, parameters, expressDataSource));
			    	}
		    	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
		    	SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(
		    		      outputStream);
		    	JRPdfExporter exporter = new JRPdfExporter();
		    	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList)); 
		    	exporter.setExporterOutput(exporterOutput);
		    	exporter.exportReport();
		    		      // return the PDF in bytes
		    	bytes = outputStream.toByteArray();
		    	/*try(OutputStream out = new FileOutputStream("Label.pdf")){
		    		out.write(bytes);
		    	}*/
		      //bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			// blob = new javax.sql.rowset.serial.SerialBlob(bytes);
		    }
		    catch (JRException | IOException  e) {
		      e.printStackTrace();
		    }
		    return bytes;
	}

	@Override
	public UserMessage manifestCreation(String manifestNumber, String referenceNumber) {
		String fileUploadData= d2zDao.manifestCreation(manifestNumber, referenceNumber);
		String [] refNbrs = referenceNumber.split(",");
		int userId = d2zDao.fetchUserIdByReferenceNumber(refNbrs[0]);
		String autoShipment = userRepository.fetchAutoShipmentIndicator(userId);

		if("Y".equalsIgnoreCase(autoShipment)) {
			try {
				allocateShipment(referenceNumber, manifestNumber.concat("AutoShip"));
			} catch (ReferenceNumberNotUniqueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(fileUploadData);
		return userMsg;
	}
	
/*	public List<TrackParcel> trackParcel(List<String> referenceNumbers,List<String> articleIDs) {
		List<TrackParcel> trackParcelList  = new ArrayList<TrackParcel>();
		if(!referenceNumbers.isEmpty()) {
			trackParcelList = trackParcelByRefNbr(referenceNumbers);
		}
		else if(!articleIDs.isEmpty()) {
			trackParcelList = trackParcelByArticleID(articleIDs);
		}
		return trackParcelList;
	}*/

	public List<TrackParcel> trackParcelByArticleID(List<String> articleIDs ) {
		List<TrackParcel> trackParcelList  = new ArrayList<TrackParcel>();

		for(String articleID :articleIDs ) {
			List<Trackandtrace> trackAndTraceList= d2zDao.trackParcelByArticleID(articleID);
			TrackParcel trackParcel = new TrackParcel();
			//List<TrackEventDetails> trackEventDetails = new ArrayList<TrackEventDetails>();
			List<TrackingEvents> trackingEventList = new ArrayList<TrackingEvents>();
			for (Trackandtrace daoObj : trackAndTraceList) {
				TrackingEvents trackingEvents = new TrackingEvents();
				trackParcel.setReferenceNumber(daoObj.getReference_number());
				trackParcel.setBarcodelabelNumber(daoObj.getArticleID());
				trackingEvents.setEventDetails(daoObj.getTrackEventDetails());
				trackingEvents.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackingEventList.add(trackingEvents);
			/*	switch(daoObj.getTrackEventDetails().toUpperCase()) {
					
					case "CONSIGNMENT CREATED":
						trackParcel.setConsignmentCreated(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "SHIPMENT ALLOCATED":
						trackParcel.setShipmentCreated(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "HELD BY CUSTOMS":
						trackParcel.setHeldByCustoms(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "CLEARED CUSTOMS":
						trackParcel.setClearedCustoms(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "RECEIVED":
						trackParcel.setReceived(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "PROCESSED BY FACILITY":
						trackParcel.setProcessedByFacility(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "IN TRANSIT":
						trackParcel.setInTransit(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "DELIVERED":
						trackParcel.setDelivered(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
				}
				*/
				/*TrackEventDetails trackEventDetail =  new TrackEventDetails();
				trackEventDetail.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackEventDetail.setTrackEventDetails(daoObj.getTrackEventDetails());
				
				trackEventDetails.add(trackEventDetail);
				trackParcel.setTrackEventDetails(trackEventDetails);*/

			}
			trackParcel.setTrackingEvents(trackingEventList);

			trackParcelList.add(trackParcel);
		}
		return trackParcelList;
	}

	public List<TrackParcel> trackParcelByRefNbr(List<String> referenceNumbers) {
		List<TrackParcel> trackParcelList  = new ArrayList<TrackParcel>();
		
		for(String refNbr :referenceNumbers ) {
			List<Trackandtrace> trackAndTraceList= d2zDao.trackParcel(refNbr);
			//List<TrackEventDetails> trackEventDetails = new ArrayList<TrackEventDetails>();
			TrackParcel trackParcel = new TrackParcel();
			List<TrackingEvents> trackingEventList = new ArrayList<TrackingEvents>();
			for (Trackandtrace daoObj : trackAndTraceList) {
				System.out.println(daoObj);
				TrackingEvents trackingEvents = new TrackingEvents();
				trackParcel.setReferenceNumber(daoObj.getReference_number());
				trackParcel.setBarcodelabelNumber(daoObj.getArticleID());
				trackingEvents.setEventDetails(daoObj.getTrackEventDetails());
				trackingEvents.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackingEventList.add(trackingEvents);
				/*switch(daoObj.getTrackEventDetails().toUpperCase()) {
					
					case "CONSIGNMENT CREATED":
						trackParcel.setConsignmentCreated(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "SHIPMENT ALLOCATED":
						trackParcel.setShipmentCreated(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "HELD BY CUSTOMS":
						trackParcel.setHeldByCustoms(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "CLEARED CUSTOMS":
						trackParcel.setClearedCustoms(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "RECEIVED AND CLEAR":
						trackParcel.setReceived(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "PROCESSED BY FACILITY":
						trackParcel.setProcessedByFacility(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "IN TRANSIT":
						trackParcel.setInTransit(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
					case "DELIVERED":
						trackParcel.setDelivered(String.valueOf(daoObj.getTrackEventDateOccured()));
						break;
				}*/
			/*	TrackEventDetails trackEventDetail =  new TrackEventDetails();
				trackEventDetail.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackEventDetail.setTrackEventDetails(daoObj.getTrackEventDetails());
				trackEventDetails.add(trackEventDetail);
				trackParcel.setTrackEventDetails(trackEventDetails);*/
				
			}
			trackParcel.setTrackingEvents(trackingEventList);
			trackParcelList.add(trackParcel);
		}
		return trackParcelList;
	}

	@Override
	public List<SenderDataResponse> createConsignments(CreateConsignmentRequest orderDetail) throws ReferenceNumberNotUniqueException, EtowerFailureResponseException {
		Integer userId = userRepository.fetchUserIdbyUserName(orderDetail.getUserName());
		if(userId == null) {
			throw new InvalidUserException("User does not exist",orderDetail.getUserName());
		}
		if(orderDetail.getConsignmentData().size() > 300) {
			throw new MaxSizeCountException("We are allowing max 300 records, Your Request contains - "+orderDetail.getConsignmentData().size()+" Records");
		}
		d2zValidator.isReferenceNumberUnique(orderDetail.getConsignmentData());
		d2zValidator.isServiceValid(orderDetail);
		d2zValidator.isPostCodeValid(orderDetail.getConsignmentData());
	
		List<SenderDataResponse> senderDataResponseList = new ArrayList<SenderDataResponse>();
		SenderDataResponse senderDataResponse = null;
	    String serviceType = orderDetail.getConsignmentData().get(0).getServiceType();
		if("1PS2".equalsIgnoreCase(serviceType) || "1PM3E".equalsIgnoreCase(serviceType)){
			makeCreateShippingOrderEtowerCall(orderDetail,senderDataResponseList);
			 return senderDataResponseList;
		}
		
		String senderFileID = d2zDao.createConsignments(orderDetail.getConsignmentData(),userId,orderDetail.getUserName(),null);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		
		Iterator itr = insertedOrder.iterator();
		 while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 senderDataResponse = new SenderDataResponse();
			 senderDataResponse.setReferenceNumber(obj[0].toString());
			 String barcode = obj[1].toString();
			 //String formattedBarcode = barcode.substring(0,barcode.length()-6).concat("120000");
			 senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
			 senderDataResponseList.add(senderDataResponse);
       }
		
		return senderDataResponseList;
	}

	private void makeCreateShippingOrderEtowerCall(CreateConsignmentRequest data,List<SenderDataResponse> senderDataResponseList) throws EtowerFailureResponseException {
		List<com.d2z.d2zservice.model.etower.CreateShippingRequest> eTowerRequest = new ArrayList<com.d2z.d2zservice.model.etower.CreateShippingRequest>();

		for(SenderDataApi orderDetail : data.getConsignmentData()) {
			com.d2z.d2zservice.model.etower.CreateShippingRequest request = new com.d2z.d2zservice.model.etower.CreateShippingRequest();
			
			request.setReferenceNo(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() >34 ? orderDetail.getConsigneeName().substring(0, 34) : orderDetail.getConsigneeName(); 
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			if("1PM3E".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("MEL3");
				orderDetail.setCarrier("Express");
			}else if("1PS2".equalsIgnoreCase(orderDetail.getServiceType())) {
				request.setFacility("SYD2");
			}
			if(("Express").equalsIgnoreCase(orderDetail.getCarrier())){
			request.setServiceOption("Express-Post");
			}
			else {
				request.setServiceOption("E-Parcel");

			}
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			request.setInvoiceValue(orderDetail.getValue());
			request.getOrderItems().get(0).setUnitValue(orderDetail.getValue());
			eTowerRequest.add(request);
		}
		d2zDao.createShippingOrderEtower(data,eTowerRequest,senderDataResponseList);
		
	}

	@Override
	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList) {
		return d2zDao.editConsignments(requestList);
	}

	@Override
	public ResponseMessage allocateShipment(String referenceNumbers, String shipmentNumber) throws ReferenceNumberNotUniqueException {
		ResponseMessage userMsg = new ResponseMessage();
		String[] refNbrs = referenceNumbers.split(",");
		List<SenderdataMaster> incorrectRefNbr = d2zDao.findRefNbrByShipmentNbr(refNbrs);
		List<String> invalidData = incorrectRefNbr.stream()
	               .map(a -> {
	            	   StringBuffer msg = new StringBuffer(a.getReference_number());
	            	 
	            	  if(null != a.getAirwayBill()) {
	            		  msg.append(" : Shippment is already allocated");
	            	  }
	            	  if(a.getIsDeleted().equalsIgnoreCase("Y")) {
	            		  msg = new StringBuffer(a.getReference_number());
	            		  msg.append(" : Consignment already deleted");
	            	  }
	            	  return msg.toString();
	               })
	               .collect(Collectors.toList());
		if(!invalidData.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Request failed",invalidData);
		}
		

		String msg =  d2zDao.allocateShipment(referenceNumbers,shipmentNumber);

		/*List<String> refNumberList = new ArrayList<String>(Arrays.asList(refNbrs)); 
		List<List<String>> referNumPartList = ListUtils.partition(refNumberList, 300);
		int count = 1;
		String msg = null;
		for(List<String> referenceNum : referNumPartList) {
			System.out.println(count + ":::" + referenceNum.size());
			count++;
			String refNumbers = StringUtils.join(referenceNum, ",");
			msg =  d2zDao.allocateShipment(refNumbers,shipmentNumber);
		}*/

	/*	List<SenderdataMaster> senderData =  d2zDao.fetchDataForAusPost(refNbrs);

		
		List<SenderdataMaster> senderData =  d2zDao.fetchDataForAusPost(refNbrs);

		if(null != senderData && !senderData.isEmpty()) {
			Runnable r = new Runnable( ) {			
		        public void run() {
		        	 makeCalltoAusPost(senderData);
		        }
		     };
		    new Thread(r).start();
		}*/
		userMsg.setResponseMessage(msg);
		return userMsg;
	}
	
	//@Scheduled(cron = "0 0 0/2 * * ?")
	//@Scheduled(cron = "0 0/10 * * * ?")
	public void makeCalltoAusPost() {
		List<String> referenceNumbers = d2zDao.fetchDataForAUPost();
		System.out.println("Track and trace:"+referenceNumbers.size());
		if(referenceNumbers.size()<10) {
			System.out.println("Less than 10 records for AUPost call");
			return;
		}
		List<SenderdataMaster> senderMasterData = d2zDao.fetchDataForAusPost(referenceNumbers);
		System.out.println("Sender Data:"+senderMasterData.size());
		
		List<List<SenderdataMaster>> senderDataList = ListUtils.partition(senderMasterData, 2000);
		for(List<SenderdataMaster> senderData : senderDataList) {
		CreateShippingRequest request =  new CreateShippingRequest();
		
		Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmm");
        String orderRef = ft.format(dNow);
        
        request.setOrder_reference(orderRef);
        request.setPayment_method("CHARGE_TO_ACCOUNT");
        
        List<ShipmentRequest> shipments = new ArrayList<ShipmentRequest>();
        for(SenderdataMaster data : senderData)
        {
        	String email = null;
        if(data.getConsignee_Email()!=null&&!data.getConsignee_Email().trim().isEmpty()&&data.getConsignee_Email().contains("@")) {
        	email = data.getConsignee_Email();
        }
        ShipmentRequest shipmentRequest = new ShipmentRequest();
        shipmentRequest.setSender_references(data.getReference_number());
        shipmentRequest.setEmail_tracking_enabled(email!=null);
       
        FromAddress from = new FromAddress();
        
        shipmentRequest.setFrom(from);
        
        ToAddress to = new ToAddress();
        to.setName(data.getConsignee_name());
        to.setPostcode(data.getConsignee_Postcode());
        to.setState(data.getConsignee_State());
        to.setSuburb(data.getConsignee_Suburb());
        to.getLines().add(data.getConsignee_addr1());
        String regex = "^[0-9]{1,20}$";
        String phone = "";
        if(null!=data.getConsignee_Phone() && data.getConsignee_Phone().matches(regex)) {
        	phone = data.getConsignee_Phone();
        }
        to.setPhone(phone);
        to.setEmail(email);
        to.setDelivery_instructions(data.getDeliveryInstructions());
        shipmentRequest.setTo(to);
        
        List<Items> items = new ArrayList<Items>();
        Items item = new Items();
      //  item.setHeight(data.getDimensions_Height() == null ? "" : data.getDimensions_Height().toString());
      //  item.setLength(data.getDimensions_Length() == null ? "" : data.getDimensions_Length().toString());
      //  item.setWidth(data.getDimensions_Width() == null ? "" : data.getDimensions_Width().toString());
        item.setItem_description(data.getProduct_Description());
        item.setWeight(data.getCubic_Weight()==null ? "": data.getCubic_Weight().toString());
       
        com.d2z.d2zservice.model.auspost.TrackingDetails trackingDetail = new com.d2z.d2zservice.model.auspost.TrackingDetails();
        trackingDetail.setArticle_id(data.getArticleId());
        StringBuffer barcode = new StringBuffer(data.getDatamatrix().replaceAll("\\[|\\]", ""));
        barcode.insert(41, '|');
        barcode.insert(49, '|');
        trackingDetail.setBarcode_id(barcode.toString());
        trackingDetail.setConsignment_id(data.getArticleId().substring(0, 20));
        item.setTracking_details(trackingDetail);

        items.add(item);
        shipmentRequest.setItems(items);

        shipments.add(shipmentRequest);
        }
        
        request.setShipments(shipments);       
        
        String response = ausPostProxy.createOrderIncludingShipments(request);
		List <AUPostResponse> AUPostResponseList =  new ArrayList<AUPostResponse>();
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		Map<String, Object> responses= jsonParser.parseMap(response);
		if(responses.containsKey("order"))
		{
		LinkedHashMap<String, Object> order  = (LinkedHashMap<String, Object>) responses.get("order");

		String orderid = (String) order.get("order_id");
		String orderreference = (String) order.get("order_reference");
		String ordercreationdate = (String) order.get("order_creation_date");
		List<Map<String, Object>> shipmentlist = (List<Map<String, Object>>)order.get("shipments");
		for (Map<String, Object> shipment: shipmentlist){
			AUPostResponse auresponse = new AUPostResponse();
			auresponse.setApiname("AU post");
		auresponse.setOrderid(orderid);
		auresponse.setOrderreference(orderreference);
		auresponse.setOrderCreationDate(ordercreationdate.substring(0, 19));
		//auresponse.setOrderCreationDate(Timestamp.valueOf(ordercreationdate));
		auresponse.setShipmentId((String) shipment.get("shipment_id"));
			Map<String, Object> shipment_summary = (Map<String, Object>) shipment.get("shipment_summary");
			auresponse.setCost(""+shipment_summary.get("total_cost"));
			auresponse.setFuelSurcharge(""+shipment_summary.get("fuel_surcharge"));
			auresponse.setGST(""+shipment_summary.get("total_gst"));
			
			
			List<Map<String, Object>> itemlist = (List<Map<String, Object>>)shipment.get("items");
			for (Map<String, Object> item: itemlist){
				auresponse.setItemId((String)(item.get("item_id")));
				Map<String, Object> tracking_summary = (Map<String, Object>) item.get("tracking_details");
				auresponse.setArticleId((String)(tracking_summary.get("article_id")));
				auresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				AUPostResponseList.add(auresponse);
			}

		}
		}
		else {
			List<Map<String, Object>> errorlist = (List<Map<String, Object>>)responses.get("errors");
			
			for (Map<String, Object> error: errorlist){
				AUPostResponse auresponse = new AUPostResponse();
				auresponse.setApiname("AU post");
				auresponse.setErrorCode((String)error.get("code"));
				auresponse.setName((String)error.get("name"));
				auresponse.setMessage((String)error.get("message"));
				auresponse.setField((String)error.get("field"));
			
				/*String Field = error.get("context").toString().split("=")[1];
				auresponse.setField(Field.substring(0, (Field.length()-1)));*/
			
				auresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				AUPostResponseList.add(auresponse);
		
			}
			
		
		}
		//AUResponseRepository.saveAll(AUPostResponseList);
		d2zDao.logAUPostResponse(AUPostResponseList);

		
		}

		}
	
	@Override
	public UserMessage addUser(UserDetails userData) {
		UserMessage userMsg = new UserMessage();
		User existingUser = userRepository.fetchUserbyCompanyName(userData.getCompanyName(), userData.getRole_Id());
		if(existingUser == null) {
			List<String> existingUserNames = userRepository.fetchAllUserName();
			if(existingUserNames.contains(userData.getUserName())){
				userMsg.setMessage("UserName already exist");
				userMsg.setUserName(userData.getUserName());
				return userMsg;
			}
 			User savedUser = d2zDao.addUser(userData);
			if(savedUser.getUser_Id()!= 0) {
				List<UserService> savedUserService = d2zDao.addUserService(savedUser,userData.getServiceType());
				if(savedUserService.size()!=0) {
					userMsg.setMessage("User Added Successfully");
					userMsg.setUserName(userData.getUserName());
				}
			}
			else {
				userMsg.setMessage("Unable to Add User");
				userMsg.setUserName(userData.getUserName());
			}
		}else {
			userMsg.setMessage("Company Name already exist");
			userMsg.setCompanyName(userData.getCompanyName());
		}
		return userMsg;
	}

	@Override
	public UserMessage updateUser(UserDetails userDetails) {
		UserMessage userMsg = new UserMessage();
		User existingUser = userRepository.fetchUserbyCompanyName(userDetails.getCompanyName(), userDetails.getRole_Id());
		if(existingUser == null) {
			userMsg.setMessage("Company Name does not exist");
			userMsg.setCompanyName(userDetails.getCompanyName());
			return userMsg;
		}
		else {
			 if(!existingUser.getUsername().equals(userDetails.getUserName())) {
				List<String> existingUserNames = userRepository.fetchAllUserName();
				if(existingUserNames.contains(userDetails.getUserName())) {
					userMsg.setMessage("UserName already exist");
					userMsg.setUserName(userDetails.getUserName());
					return userMsg;
				}
				existingUser.setUsername(userDetails.getUserName());
			 }
				existingUser.setAddress(userDetails.getAddress());
				existingUser.setState(userDetails.getState());
				existingUser.setSuburb(userDetails.getSuburb());
				existingUser.setPostcode(userDetails.getPostCode());
				existingUser.setCountry(userDetails.getCountry());
				existingUser.setEmail(userDetails.getEmailAddress());
				existingUser.setPassword(D2ZCommonUtil.hashPassword(userDetails.getPassword()));
				existingUser.setPassword_value(userDetails.getPassword());
				existingUser.setEBayToken(userDetails.geteBayToken());
				existingUser.setModifiedTimestamp(Timestamp.valueOf(LocalDateTime.now()));
				User updatedUser = d2zDao.updateUser(existingUser);
				d2zDao.updateUserService(updatedUser,userDetails);
				userMsg.setMessage("Updated Successfully");
		}
		return userMsg;
	}

	@Override
	public UserMessage deleteUser(String companyName, String roleId) {
		UserMessage userMsg = new UserMessage();
		userMsg.setCompanyName(companyName);
		String msg = d2zDao.deleteUser(companyName, roleId);
		userMsg.setMessage(msg);
		return userMsg;
	}


	public List<SenderdataMaster> fetchManifestData(String fileName) {
		List<SenderdataMaster> fileData= d2zDao.fetchManifestData(fileName);
		return fileData;
	}

	@Override
	public UserDetails login(String userName, String passWord) {
		User userData = d2zDao.login(userName, passWord);
		UserDetails userDetails = new UserDetails();
		userDetails.setAddress(userData.getAddress());
		userDetails.setCompanyName(userData.getCompanyName());
		userDetails.setContactName(userData.getName());
		userDetails.setContactPhoneNumber(userData.getPhoneNumber());
		userDetails.setCountry(userData.getCountry());
		userDetails.setEmailAddress(userData.getEmail());
		userDetails.setPassword(userData.getPassword_value());
		userDetails.setPostCode(userData.getPostcode());
		userDetails.setState(userData.getState());
		userDetails.setSuburb(userData.getSuburb());
		userDetails.setUserName(userData.getUsername());
		userDetails.setRole_Id(userData.getRole_Id());
		userDetails.setUser_id(userData.getUser_Id());
		List<String> serviceTypeList = d2zDao.fetchServiceType(userDetails.getUser_id());
		userDetails.setServiceType(serviceTypeList);
		return userDetails;
	}

	@Override
	//public byte[] downloadShipmentData(String shipmentNumber) {
	public List<ShipmentDetails>  downloadShipmentData(String shipmentNumber, Integer userId) {
		List<Integer> listOfClientId = d2zBrokerDao.getClientId(userId);
		List<SenderdataMaster> senderDataList  = d2zDao.fetchShipmentData(shipmentNumber,listOfClientId);
		System.out.println(senderDataList.size()+" records");
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for(SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			shipmentData.setReferenceNumber(senderData.getReference_number());
			shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19,30));
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			shipmentData.setWeight(senderData.getWeight());
			shipmentData.setConsigneePhone(senderData.getConsignee_Phone());
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			shipmentData.setDestination("AUSTRALIA");
			shipmentData.setQuantity(senderData.getShippedQuantity());
			shipmentData.setCommodity(senderData.getProduct_Description());
			shipmentData.setValue(senderData.getValue());
			shipmentData.setShipperName(senderData.getShipper_Name());
			shipmentData.setShipperAddress(senderData.getShipper_Addr1());
			shipmentData.setShipperCity(senderData.getShipper_City());
			shipmentData.setShipperState(senderData.getShipper_State());
			shipmentData.setShipperPostcode(senderData.getShipper_Postcode());
			shipmentData.setShipperCountry(senderData.getShipper_Country());
			shipmentData.setShipperContact(senderData.getAirwayBill());
			shipmentDetails.add(shipmentData);
		}
//		byte[] bytes = shipmentWriter.generateShipmentxls(shipmentDetails);
//		return bytes;
		return shipmentDetails;
	}


	@Override
	public List<ParcelStatus> getStatusByRefNbr(List<String> referenceNumbers) {
		List<ParcelStatus> trackParcelList  = new ArrayList<ParcelStatus>();

		for(String referenceNumber :referenceNumbers) {
			Trackandtrace trackAndTrace= d2zDao.getLatestStatusByReferenceNumber(referenceNumber);
			if(trackAndTrace!=null) {
			ParcelStatus trackParcel = new ParcelStatus();
			trackParcel.setReferenceNumber(trackAndTrace.getReference_number());
			trackParcel.setArticleID(trackAndTrace.getArticleID());
			trackParcel.setTrackEventDateOccured(String.valueOf(trackAndTrace.getTrackEventDateOccured()));
			trackParcel.setTrackEventDetails(trackAndTrace.getTrackEventDetails());
			trackParcelList.add(trackParcel);
			}
		}
		
		return trackParcelList;
	}

	@Override
	public List<ParcelStatus> getStatusByArticleID(List<String> articleIDs) {

		List<ParcelStatus> trackParcelList  = new ArrayList<ParcelStatus>();

		for(String articleID :articleIDs ) {
			Trackandtrace trackAndTrace= d2zDao.getLatestStatusByArticleID(articleID);
			if(trackAndTrace!=null) {
			ParcelStatus trackParcel = new ParcelStatus();
			trackParcel.setReferenceNumber(trackAndTrace.getReference_number());
			trackParcel.setArticleID(trackAndTrace.getArticleID());
			trackParcel.setTrackEventDateOccured(String.valueOf(trackAndTrace.getTrackEventDateOccured()));
			trackParcel.setTrackEventDetails(trackAndTrace.getTrackEventDetails());
			trackParcelList.add(trackParcel);
			}
		}
		
		return trackParcelList;
	
	}

	@Override
	public UserMessage uploadShipmentDetailsToEbay(Ebay_ShipmentDetails shipmentDetails) {
		UserMessage userMsg = new UserMessage();
		int successCount=0;
		for(Ebay_Shipment shipment : shipmentDetails.getShipment()) {
		CompleteSaleResponseType response = proxy.makeCalltoEbay_CompleteSale(shipment,shipmentDetails.getClientEbayToken());
		d2zDao.logEbayResponse(response);
		if(response!=null && "SUCCESS".equalsIgnoreCase(response.getAck().toString())) {
			successCount++;
		}
		if(successCount == shipmentDetails.getShipment().size()) {
			userMsg.setMessage("Data sucessfully uploaded to EBay Server");
		}
		else if(successCount==0) {
			userMsg.setMessage("Failed to upload data to EBay Server");
		}
		else {
			userMsg.setMessage("Data Partially uploaded to Ebay Server");
		}
		}
		return userMsg;
	}


	@Override
	public ClientDashbaord clientDahbaord(Integer userId) {
		ClientDashbaord clientDashbaord = d2zDao.clientDahbaord(userId);
		return clientDashbaord;
	}

	@Override
	public UserMessage deleteConsignments(@Valid DeleteConsignmentRequest request) throws ReferenceNumberNotUniqueException {
		UserMessage userMsg = new UserMessage();
		User user = d2zDao.login(request.getUserName(), request.getPassword());
		if(null==user) {
			userMsg.setMessage("Invalid UserName or Password");
			return userMsg;
		}
		List<String> referenceNumbers_DB = d2zDao.fetchReferenceNumberByUserId(user.getUser_Id());
		List<String> incomingRefNbr = request.getReferenceNumbers();
		
		List<String> invalidRefNbr = incomingRefNbr.stream()
	               .filter(a -> !referenceNumbers_DB.contains(a))
	               .collect(Collectors.toList());
		if(!invalidRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Invalid Reference Number",invalidRefNbr);
		}
		List<SenderdataMaster> shipment_Manifest = d2zDao.fetchConsignmentsManifestShippment(incomingRefNbr);

		List<String> invalidData = shipment_Manifest.stream()
	               .map(a -> {
	            	   StringBuffer msg = new StringBuffer(a.getReference_number());
	            	  if(null != a.getManifest_number()) {
	            		  msg.append(" : Manifest is already created");
	            	  }
	            	  if(null != a.getAirwayBill()) {
	            		  msg = new StringBuffer(a.getReference_number());
	            		  msg.append(" : Shippment is already allocated");
	            	  }
	            	  return msg.toString();
	               })
	               .collect(Collectors.toList());
		if(!invalidData.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Consignment cannot be deleted",invalidData);
		}
		String referenceNumbers = String.join(",", incomingRefNbr);
		
		d2zDao.deleteConsignment(referenceNumbers);
		userMsg.setMessage("Deleted Successfully");
		return userMsg;
	}

	@Override
	public List<PostCodeWeight> getRates(@Valid APIRatesRequest request) {
		 User user = d2zDao.login(request.getUserName(), request.getPassword());

		if(null==user) {
			throw new InvalidUserException("Invalid Username or Password", null);
		}
		
		Map<String, Double> postcodeWeightMap = D2ZSingleton.getInstance().getPostCodeWeightMap();
		request.getConsignmentDetails().forEach(obj -> {
		double weight = obj.getWeight();
		double maxWeight = 0;
		double minWeight = 0;
		if(weight >= 0 && weight <= 0.5) {
			maxWeight = 0.50;
		}else if(weight > 0.5 && weight <= 1) {
			maxWeight = 1;
		}else if(weight > 1 && weight <= 2) {
			maxWeight = 2;
		}else if(weight > 2 && weight <= 3) {
			maxWeight = 3;
		}else if(weight > 3 && weight <= 4) {
			
			maxWeight = 4;
		}else if(weight > 4 && weight <= 5) {
			
			maxWeight = 5;
		}else if(weight > 5 && weight <= 7) {
			
			maxWeight = 7;
		}else if(weight > 7 && weight <= 10) {
			
			maxWeight = 10;
		}else if(weight > 10 && weight <= 15) {
			
			maxWeight = 15;
		}else if(weight > 15 && weight <= 22) {
			
			maxWeight = 22;
		}
		double rate = postcodeWeightMap.get(obj.getPostcode()+maxWeight+user.getUser_Id());
		obj.setRate(rate);
		});
		return request.getConsignmentDetails();
	}
	
	@Override
	public UserMessage contactUs(String email, String messageData, String name, String subject) {
		UserMessage userMsg = new UserMessage();
		System.out.print("email:"+email+"message:"+messageData);
		
		final String fromEmail = "cs@d2z.com.au";
	
		final String password ="rydjwqzrxhvcwhrb";
		
		Properties props = new Properties();
		

		props.put("mail.smtp.host", "smtp.office365.com");
	
		props.put("mail.smtp.port", "587"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		Session session = Session.getInstance(props, auth);
		emailUtil.sendEmail(session, email, fromEmail, name, messageData, subject);
		emailUtil.senderEmail(session, email, fromEmail, name, subject);
		userMsg.setMessage("Thanks for contacting us,D2Z team will reach you soon");
		return userMsg;
		
	}

	@Override
	public void triggerFreipost() {
		freipostWrapper.trackingEventService("124538");
	}
	
	@Override
	public void triggerFDM() {
		
		List<String> referenceNumbers = d2zDao.fetchArticleIDForFDMCall();
		System.out.println("Track and trace:"+referenceNumbers.size());

		List<SenderdataMaster> senderData = d2zDao.fetchDataForAusPost(referenceNumbers);
		System.out.println("Sender Data:"+senderData.size());
		List<SenderdataMaster> testData =  new ArrayList<SenderdataMaster>();
		testData.add(senderData.get(0));
		testData.add(senderData.get(1));
		testData.add(senderData.get(2));
		testData.add(senderData.get(3));
		testData.add(senderData.get(4));
		if(!senderData.isEmpty()) {
		FDMManifestRequest request = new FDMManifestRequest();
		Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmm");
        String orderRef = ft.format(dNow);
        
        FDMManifestDetails fdmDetails = new FDMManifestDetails();
        fdmDetails.setMessage_no(orderRef);
        fdmDetails.setCustomer_id("D2Z");
        
        ArrayOfConsignment consignmentsArray =  new ArrayOfConsignment();
        List<Consignment> consignments = new ArrayList<Consignment>();
        
		for(SenderdataMaster data : senderData) {
			Consignment consignment = new Consignment();
			consignment.setConnote_no(data.getArticleId());
			consignment.setTracking_connote(data.getReference_number());
			String date = data.getTimestamp();
			try {
				Date dateFormat =  new SimpleDateFormat("YYMMDDHHMMSS").parse(date);
				consignment.setConnote_date(dateFormat.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			consignment.setCustname(data.getConsignee_name());
			consignment.setCust_street1(data.getConsignee_addr1());
			consignment.setCust_street2(data.getConsignee_addr2());
			consignment.setCust_suburb(data.getConsignee_Suburb());
			consignment.setCust_pcode(data.getConsignee_Postcode());
			consignment.setCust_state(data.getConsignee_State());
			consignment.setCust_country("AU");
			consignment.setCust_ph(data.getConsignee_Phone());
			consignment.setCust_email(data.getConsignee_Email());
			consignment.setInstruction(data.getDeliveryInstructions());
			consignment.setCustomer_code("D2Z");
			consignment.setCarrier("AUSPOST");
			consignment.setVendor_name("D2Z");
			consignment.setVendor_street1("Ground Floor, Suite 3, 410 Church Street");
			consignment.setVendor_suburb("North Parramatta");
			consignment.setVendor_pcode("2151");
			consignment.setVendor_state("NSW");
			consignment.setVendor_country("AU");
			consignment.setTotal_weight(String.valueOf(data.getCubic_Weight()));
			ArrayofDetail details = new ArrayofDetail();
			List<Line> itemList = new ArrayList<Line>();
			Line lineItem = new Line();
			lineItem.setBarcode(data.getBarcodelabelNumber());
			lineItem.setArticle_no(data.getArticleId());
			lineItem.setDescription(data.getProduct_Description());
			lineItem.setWeight(String.valueOf(data.getCubic_Weight()));
			lineItem.setDim_height(data.getDimensions_Height() == null ? "" : data.getDimensions_Height().toString());
			lineItem.setDim_length(data.getDimensions_Length() == null ? "" : data.getDimensions_Length().toString());
		    lineItem.setDim_width(data.getDimensions_Width() == null ? "" : data.getDimensions_Width().toString());
			itemList.add(lineItem);
			details.setLine(itemList);
			consignment.setDetails(details);
			consignments.add(consignment);
		
		}
		consignmentsArray.setConsignment(consignments);
		fdmDetails.setConsignments(consignmentsArray);
		request.setManifest(fdmDetails);
		fdmProxy.makeCallToFDMManifestMapping(request);
		}
	}

	@Override
	public ResponseMessage auTrackingEvent() {
		ResponseMessage respMsg = null;
		List<String> articleIdData = trackAndTraceRepository.getArticleId();
		List<List<String>> articleIdList = ListUtils.partition(articleIdData, 10);
		int count = 1;
		for(List<String> articleIdNumbers : articleIdList) {
			System.out.println(count + ":::" + articleIdNumbers.size());
			count++;
			String articleIds = StringUtils.join(articleIdNumbers, ", ");
			TrackingResponse auTrackingDetails = ausPostProxy.trackingEvent(articleIds);
			System.out.println("AU Track Response");
			System.out.println(auTrackingDetails.toString());
			respMsg = d2zDao.insertAUTrackingDetails(auTrackingDetails);
	 	}
		return respMsg;
	}
	
}
