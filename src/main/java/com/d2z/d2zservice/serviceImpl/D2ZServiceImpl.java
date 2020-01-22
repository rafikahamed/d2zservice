package com.d2z.d2zservice.serviceImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.ParseException;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.validation.Valid;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.async.AsyncService;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.excelWriter.ShipmentDetailsWriter;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.exception.PCAlabelException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.APIRatesRequest;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.DeleteConsignmentRequest;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.Ebay_Shipment;
import com.d2z.d2zservice.model.Ebay_ShipmentDetails;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.FDMManifestDetails;
import com.d2z.d2zservice.model.PFLSenderDataFileRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.PFLTrackEvent;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.ParcelStatus;
import com.d2z.d2zservice.model.PflCreateShippingOrderInfo;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.PostCodeWeight;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.model.ShipmentDetails;
import com.d2z.d2zservice.model.TrackParcel;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.TrackingDetails;
import com.d2z.d2zservice.model.TrackingEvents;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.auspost.CreateShippingRequest;
import com.d2z.d2zservice.model.auspost.FromAddress;
import com.d2z.d2zservice.model.auspost.Items;
import com.d2z.d2zservice.model.auspost.ShipmentRequest;
import com.d2z.d2zservice.model.auspost.ToAddress;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
import com.d2z.d2zservice.model.etower.ETowerTrackingDetails;
import com.d2z.d2zservice.model.etower.TrackEventResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.model.fdm.ArrayOfConsignment;
import com.d2z.d2zservice.model.fdm.ArrayofDetail;
import com.d2z.d2zservice.model.fdm.Consignment;
import com.d2z.d2zservice.model.fdm.FDMManifestRequest;
import com.d2z.d2zservice.model.fdm.Line;
import com.d2z.d2zservice.proxy.AusPostProxy;
import com.d2z.d2zservice.proxy.EbayProxy;
import com.d2z.d2zservice.proxy.FDMProxy;
import com.d2z.d2zservice.proxy.PcaProxy;
import com.d2z.d2zservice.repository.FFResponseRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.service.ID2ZService;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.util.EmailUtil;
import com.d2z.d2zservice.util.FTPUploader;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.ETowerWrapper;
import com.d2z.d2zservice.wrapper.FreipostWrapper;
import com.d2z.d2zservice.wrapper.PCAWrapper;
import com.d2z.d2zservice.wrapper.PFLWrapper;
import com.d2z.singleton.D2ZSingleton;
import com.d2z.singleton.SingletonCounter;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;
import com.google.gson.Gson;

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
public class D2ZServiceImpl implements ID2ZService {

	@Autowired
	private ID2ZDao d2zDao;

	@Autowired
	private D2ZValidator d2zValidator;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FFResponseRepository ffresponseRepository;

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
	ETowerWrapper eTowerWrapper; 
	
	@Autowired
	AusPostProxy ausPostProxy;

	@Autowired
	FDMProxy fdmProxy;

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;

	@Autowired
	private FTPUploader ftpUploader;
	
	@Autowired
	PFLWrapper pflWrapper;
	
	@Autowired
	PCAWrapper pcaWrapper;
	
	@Autowired
	PcaProxy pcaProxy;
	
	@Autowired
	AsyncService aysncService;

	@Override
	public List<SenderDataResponse> exportParcel(List<SenderData> orderDetailList) 
					throws ReferenceNumberNotUniqueException, FailureResponseException{
		List<String> incomingRefNbr = orderDetailList.stream().map(obj -> {
			return obj.getReferenceNumber(); })
				.collect(Collectors.toList());
		boolean isPostcodeValidationReq = true;
		if(("N").equals(userRepository.fetchPostcodeValidationIndicator(orderDetailList.get(0).getUserName()))){
			isPostcodeValidationReq = false;
		}

		d2zValidator.isReferenceNumberUniqueUI(incomingRefNbr);
		d2zValidator.isServiceValidUI(orderDetailList);
		
		d2zValidator.isAddressValidUI(orderDetailList);

		List<SenderDataResponse> senderDataResponseList = new ArrayList<SenderDataResponse>();
		SenderDataResponse senderDataResponse = null;
		String serviceType = orderDetailList.get(0).getServiceType();
		if("1PM3E".equalsIgnoreCase(serviceType) 
				|| "1PS3".equalsIgnoreCase(serviceType) 
				|| "1PM5".equalsIgnoreCase(serviceType) || "TST1".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
				d2zValidator.isPostCodeValidUI(orderDetailList);
				}
			eTowerWrapper.makeCreateShippingOrderEtowerCallForFileData(orderDetailList,senderDataResponseList);
			return senderDataResponseList;
		}else if ("FWM".equalsIgnoreCase(serviceType) || "FW".equalsIgnoreCase(serviceType) || "1PS4".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
				if("1PS4".equalsIgnoreCase(serviceType)) {
						d2zValidator.isPostCodeValidUI(orderDetailList);
						
				}else {
					d2zValidator.isFWPostCodeUIValid(orderDetailList);
				}
			}
			makeCreateShippingOrderFilePFLCall(orderDetailList,senderDataResponseList,null,serviceType);
			return senderDataResponseList;
		}else if("FWS".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
			d2zValidator.isFWPostCodeUIValid(orderDetailList);
			}
			pcaWrapper.makeCreateShippingOrderFilePCACall(orderDetailList,senderDataResponseList,null,serviceType);
			return senderDataResponseList;
		}else if("MCM".equalsIgnoreCase(serviceType) || "MCM1".equalsIgnoreCase(serviceType) || "MCM2".equalsIgnoreCase(serviceType) 
				|| "MCM3".equalsIgnoreCase(serviceType) || "MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)){
			PFLSenderDataFileRequest consignmentData = d2zValidator.isFWSubPostCodeUIValid(orderDetailList);
			if(consignmentData.getPflSenderDataApi().size() > 0) {
				if("MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)) {
					pcaWrapper.makeCreateShippingOrderFilePCACall(consignmentData.getPflSenderDataApi(),senderDataResponseList,null,serviceType);
				}else {
					makeCreateShippingOrderFilePFLCall(consignmentData.getPflSenderDataApi(),senderDataResponseList,null, serviceType);
				}
			}
			
			if("STS".equalsIgnoreCase(serviceType) && consignmentData.getNonPflSenderDataApi().size() > 0) {
				pcaWrapper.makeCreateShippingOrderFilePCACall(consignmentData.getNonPflSenderDataApi(),senderDataResponseList,null,"STS-Sub");
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && !"STS".equalsIgnoreCase(serviceType)) {
				if(isPostcodeValidationReq) {
					d2zValidator.isPostCodeValidUI(orderDetailList);
					}
				String senderFileID  = d2zDao.exportParcel(consignmentData.getNonPflSenderDataApi(),null);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					senderDataResponse = new SenderDataResponse();
					senderDataResponse.setReferenceNumber(obj[0].toString());
					senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataResponse.setCarrier(obj[4].toString());
					senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
					senderDataResponseList.add(senderDataResponse);
				}
			}
			return senderDataResponseList;
		}
		if(isPostcodeValidationReq) {
			d2zValidator.isPostCodeValidUI(orderDetailList);
			}
		String senderFileID  = d2zDao.exportParcel(orderDetailList,null);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		Iterator itr = insertedOrder.iterator();
		while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 senderDataResponse = new SenderDataResponse();
			 senderDataResponse.setReferenceNumber(obj[0].toString());
			 senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
			 senderDataResponse.setCarrier(obj[4].toString());
			 senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
			 senderDataResponseList.add(senderDataResponse);
        }
		return senderDataResponseList;
	}

	@Override
	public UserMessage consignmentDelete(String refrenceNumlist) {
		String fileUploadData = d2zDao.consignmentDelete(refrenceNumlist);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(fileUploadData);
		
		 List<String> incomingRefNbr = Arrays.asList(refrenceNumlist.split("\\s*,\\s*"));
				 
				 Runnable r = new Runnable( ) {			
		        public void run() {
		        System.out.println("in Thread for Delete pfl etower");
		        List<String> inc = incomingRefNbr.stream().map(obj ->obj+"Delete").collect(Collectors.toList());
		        System.out.println("in Thread for Delete pfl etower");
		        	deleteEtowerPflPca(inc);
		        	
		    		
		        }
		     };
		    new Thread(r).start();
		return userMsg;
	}

	public List<DropDownModel> fileList(Integer userId) {
		List<String> listOfFileNames = d2zDao.fileList(userId);
		List<DropDownModel> dropDownList = new ArrayList<DropDownModel>();
		Iterator itr = listOfFileNames.iterator();
		while (itr.hasNext()) {
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
		List<String> listOfFileNames = d2zDao.labelFileList(userId);
		List<DropDownModel> dropDownList = new ArrayList<DropDownModel>();
		Iterator itr = listOfFileNames.iterator();
		while (itr.hasNext()) {
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
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			trackingDetails = new TrackingDetails();
			trackingDetails.setRefrenceNumber(obj[0].toString());
			trackingDetails.setConsigneeName(obj[1].toString());
			if((obj[3].toString().equalsIgnoreCase("FastwayM")  )||(obj[3].toString().equalsIgnoreCase("FastwayS")  )) {
				trackingDetails.setBarCodeLabelNumber(obj[2].toString());
			}else {
				trackingDetails.setBarCodeLabelNumber(obj[2].toString().substring(18));
			}
			trackingDetailsList.add(trackingDetails);
		}
		return trackingDetailsList;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> fileData = d2zDao.consignmentFileData(fileName);
		return fileData;
	}

	/*
	 * @Override public byte[] generateLabel(List<SenderData> senderData) {
	 * 
	 * for(SenderData data : senderData){
	 * data.setDatamatrixImage(generateDataMatrix(data.getDatamatrix())); }
	 * JRBeanCollectionDataSource beanColDataSource = new
	 * JRBeanCollectionDataSource(senderData); Map<String,Object> parameters = new
	 * HashMap<>(); byte[] bytes = null; //Blob blob = null; JasperReport
	 * jasperReport = null; try (ByteArrayOutputStream byteArray = new
	 * ByteArrayOutputStream()) { jasperReport =
	 * JasperCompileManager.compileReport(getClass().getResource(
	 * "/eparcelLabel.jrxml").openStream()); JRSaver.saveObject(jasperReport,
	 * "label.jasper"); JasperPrint jasperPrint =
	 * JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource); //
	 * return the PDF in bytes bytes =
	 * JasperExportManager.exportReportToPdf(jasperPrint); // blob = new
	 * javax.sql.rowset.serial.SerialBlob(bytes); } catch (JRException | IOException
	 * e) { e.printStackTrace(); } return bytes; }
	 */

	@Override
	public byte[] generateLabel(List<SenderData> senderData) {

		List<SenderData> eParcelData = new ArrayList<SenderData>();

		List<SenderData> expressData = new ArrayList<SenderData>();
		List<SenderData> fastwayData = new ArrayList<SenderData>();
		List<SenderData> fastway_S_Data = new ArrayList<SenderData>();
		List<SenderData> whiteLabelData = new ArrayList<SenderData>();
		List<SenderData> eParcelNewData = new ArrayList<SenderData>();
		List<SenderData> expressNewData = new ArrayList<SenderData>();
		List<SenderData> parcelPostData = new ArrayList<SenderData>();
		List<SenderData> fwData = new ArrayList<SenderData>();

		
		boolean setGS1Type=false;
		for (SenderData data : senderData) {
			if ("MCM1".equalsIgnoreCase(data.getServiceType()) && data.getCarrier().equalsIgnoreCase("FastwayM")) {
				whiteLabelData.add(data);
			}
			else if ("MCM2".equalsIgnoreCase(data.getServiceType()) && (data.getCarrier().equalsIgnoreCase("Express") || data.getCarrier().equalsIgnoreCase("eParcel")) ) {
				whiteLabelData.add(data);
			}
			else if("MCM3".equalsIgnoreCase(data.getServiceType())) {
				whiteLabelData.add(data);
			}else if("1PM".equalsIgnoreCase(data.getServiceType())) {
				eParcelNewData.add(data);
			}else if("1PME".equalsIgnoreCase(data.getServiceType())) {
				expressNewData.add(data);
			}else if("HKG".equalsIgnoreCase(data.getServiceType())) {
				parcelPostData.add(data);
			}else  if(data.getCarrier().equalsIgnoreCase("eParcel")) {
				setGS1Type= true;
				eParcelData.add(data);
			} else if (data.getCarrier().equalsIgnoreCase("Express")) {
				setGS1Type = true;
				expressData.add(data);
			}else if ("FW".equalsIgnoreCase(data.getServiceType()) && data.getCarrier().equalsIgnoreCase("FastwayM")) {
				fwData.add(data);
			}else if(data.getCarrier().equalsIgnoreCase("FastwayM")) {
				fastwayData.add(data);
			}else if(data.getCarrier().equalsIgnoreCase("FastwayS")) {
				fastway_S_Data.add(data);
			}
		
			if("VELC".equalsIgnoreCase(data.getUserName())) {
				data.setLabelSenderName("SW4");
			}
			data.setDatamatrixImage(generateDataMatrix(data.getDatamatrix(),setGS1Type));
		}

		Map<String, Object> parameters = new HashMap<>();
		byte[] bytes = null;
		JRBeanCollectionDataSource eParcelDataSource;
		JRBeanCollectionDataSource expressDataSource;
		JasperReport eParcelLabel = null;
		JasperReport expressLabel = null;
		JRBeanCollectionDataSource fastwayDataSource;
		JasperReport fastwayLabel = null;
		JRBeanCollectionDataSource fastway_S_DataSource;
		JasperReport fastway_S_Label = null;
		JRBeanCollectionDataSource whiteLabelDataSource;
		JasperReport whiteLabel = null;
		JRBeanCollectionDataSource eParcelNewDataSource;
		JasperReport eParcelNew = null;
		JRBeanCollectionDataSource expressNewDataSource;
		JasperReport expressNew = null;
		JRBeanCollectionDataSource fwDataSource;
		JasperReport fwLabel = null;
		JRBeanCollectionDataSource parcelPostDataSource;
		JasperReport parcelPost = null;
		
		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			if (!eParcelData.isEmpty()) {
				System.out.println("Generating eParcel..." + eParcelData.size());
				eParcelDataSource = new JRBeanCollectionDataSource(eParcelData);
				eParcelLabel = JasperCompileManager
						.compileReport(getClass().getResource("/eparcelLabel.jrxml").openStream());
				JRSaver.saveObject(eParcelLabel, "label.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(eParcelLabel, parameters, eParcelDataSource));

			}
			if (!expressData.isEmpty()) {
				System.out.println("Generating Express..." + expressData.size());
				expressDataSource = new JRBeanCollectionDataSource(expressData);
				expressLabel = JasperCompileManager
						.compileReport(getClass().getResource("/ExpressLabel.jrxml").openStream());
				JRSaver.saveObject(expressLabel, "express.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(expressLabel, parameters, expressDataSource));
			}
			
			if (!expressNewData.isEmpty()) {
				System.out.println("Generating Express new..." + expressNewData.size());
				expressNewDataSource = new JRBeanCollectionDataSource(expressNewData);
				expressNew = JasperCompileManager
						.compileReport(getClass().getResource("/ExpressNew.jrxml").openStream());
				JRSaver.saveObject(expressNew, "expressNew.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(expressNew, parameters, expressNewDataSource));
			}
			if (!parcelPostData.isEmpty()) {
				System.out.println("Generating Parcel Post..." + parcelPostData.size());
				parcelPostDataSource = new JRBeanCollectionDataSource(parcelPostData);
				parcelPost = JasperCompileManager
						.compileReport(getClass().getResource("/ParcelPost.jrxml").openStream());
				JRSaver.saveObject(parcelPost, "parcelPost.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(parcelPost, parameters, parcelPostDataSource));
			}
			if (!fastwayData.isEmpty()) {
				System.out.println("Generating Fastway..." + fastwayData.size());
				fastwayDataSource = new JRBeanCollectionDataSource(fastwayData);
				fastwayLabel = JasperCompileManager
						.compileReport(getClass().getResource("/FastWayLabel.jrxml").openStream());
				JRSaver.saveObject(fastwayLabel, "FastWayLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fastwayLabel, parameters, fastwayDataSource));
			}
			if (!fastway_S_Data.isEmpty()) {
				System.out.println("Generating FastwayS..." + fastway_S_Data.size());
				fastway_S_DataSource = new JRBeanCollectionDataSource(fastway_S_Data);
				fastway_S_Label = JasperCompileManager
						.compileReport(getClass().getResource("/FastwayPCA.jrxml").openStream());
				JRSaver.saveObject(fastway_S_Label, "FastwayPCA.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fastway_S_Label, parameters, fastway_S_DataSource));
			}
			if (!whiteLabelData.isEmpty()) {
				System.out.println("Generating WhiteLabel..." + whiteLabelData.size());
				whiteLabelDataSource = new JRBeanCollectionDataSource(whiteLabelData);
				whiteLabel = JasperCompileManager
						.compileReport(getClass().getResource("/WhiteLabel.jrxml").openStream());
				JRSaver.saveObject(whiteLabel, "whiteLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(whiteLabel, parameters, whiteLabelDataSource));
			}
			if (!eParcelNewData.isEmpty()) {
				System.out.println("Generating EParcel new..." + eParcelNewData.size());
				eParcelNewDataSource = new JRBeanCollectionDataSource(eParcelNewData);
				eParcelNew = JasperCompileManager
						.compileReport(getClass().getResource("/eParcelNew.jrxml").openStream());
				JRSaver.saveObject(eParcelNew, "eParcelNew.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(eParcelNew, parameters, eParcelNewDataSource));
			}
			if (!fwData.isEmpty()) {
				System.out.println("Generating Fastway FW..." + fwData.size());
				fwDataSource = new JRBeanCollectionDataSource(fwData);
				fwLabel = JasperCompileManager
						.compileReport(getClass().getResource("/FWLabel.jrxml").openStream());
				JRSaver.saveObject(fastwayLabel, "FWLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fwLabel, parameters, fwDataSource));
			}
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			exporter.setExporterOutput(exporterOutput);
			exporter.exportReport();
			// return the PDF in bytes
			bytes = outputStream.toByteArray();
			// bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			// blob = new javax.sql.rowset.serial.SerialBlob(bytes);
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	private BufferedImage generateDataMatrix(String datamatrixInput, boolean setGS1DataType) {
		BufferedImage datamatrixImage = null;
		try {
			// Set up the DataMatrix object
			DataMatrix dataMatrix = new DataMatrix();
			// We need a GS1 DataMatrix barcode.
			if(setGS1DataType)
			dataMatrix.setDataType(DataType.GS1);
			
			// 0 means size will be set automatically according to amount of data (smallest
			// possible).
			dataMatrix.setPreferredSize(0);
			// Don't want no funky rectangle shapes, if we can avoid it.
			dataMatrix.setForceMode(ForceMode.SQUARE);
			dataMatrix.setContent(datamatrixInput);
			datamatrixImage = getMagnifiedBarcode(dataMatrix);
			// return getBase64FromByteArrayOutputStream(getMagnifiedBarcode(dataMatrix,
			// MAGNIFICATION));
		} catch (OkapiException oe) {
			oe.printStackTrace();
		}
		return datamatrixImage;

	}

	private BufferedImage getMagnifiedBarcode(Symbol symbol) {
		final int MAGNIFICATION = 10;
		final int BORDER_SIZE = 0 * MAGNIFICATION;
		// Make DataMatrix object into bitmap
		BufferedImage image = new BufferedImage((symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
				(symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, (symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
				(symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE));
		Java2DRenderer renderer = new Java2DRenderer(g2d, 10, Color.WHITE, Color.BLACK);
		renderer.render(symbol);

		return image;
	}

	@Override
	public byte[] trackingLabel(List<String> refBarNum) throws PCAlabelException {
		List<SenderData> trackingLabelList = new ArrayList<SenderData>();
		System.out.println("size:"+refBarNum.size());
		List<String> trackingLabelData = d2zDao.trackingLabel(refBarNum);
		boolean pcalabel = false;
		Iterator itr = trackingLabelData.iterator();
		while (itr.hasNext()) {
			SenderData trackingLabel = new SenderData();
			Object[] trackingArray = (Object[]) itr.next();
			if (trackingArray[0] != null)
				trackingLabel.setReferenceNumber(trackingArray[0].toString());
			if (trackingArray[1] != null)
				trackingLabel.setConsigneeName(trackingArray[1].toString());
			if (trackingArray[2] != null)
				trackingLabel.setConsigneeAddr1(trackingArray[2].toString());
			if (trackingArray[3] != null)
				trackingLabel.setConsigneeSuburb(trackingArray[3].toString());
			if (trackingArray[4] != null)
				trackingLabel.setConsigneeState(trackingArray[4].toString());
			if (trackingArray[5] != null)
				trackingLabel.setConsigneePostcode(trackingArray[5].toString());
			if (trackingArray[6] != null)
				trackingLabel.setConsigneePhone(trackingArray[6].toString());
			if (trackingArray[7] != null)
				trackingLabel.setWeight(trackingArray[7].toString());
			if (trackingArray[8] != null)
				trackingLabel.setShipperName(trackingArray[8].toString());
			if (trackingArray[9] != null)
				trackingLabel.setShipperAddr1(trackingArray[9].toString());
			if (trackingArray[10] != null)
				trackingLabel.setShipperCity(trackingArray[10].toString());
			if (trackingArray[11] != null)
				trackingLabel.setShipperState(trackingArray[11].toString());
			if (trackingArray[12] != null)
				trackingLabel.setShipperCountry(trackingArray[12].toString());
			if (trackingArray[13] != null)
				trackingLabel.setShipperPostcode(trackingArray[13].toString());
			if (trackingArray[14] != null)
				trackingLabel.setBarcodeLabelNumber(trackingArray[14].toString());
			if (trackingArray[15] != null)
				trackingLabel.setDatamatrix(trackingArray[15].toString());
			if (trackingArray[16] != null)
				trackingLabel.setInjectionState(trackingArray[16].toString());
			if (trackingArray[17] != null)
				trackingLabel.setSku(trackingArray[17].toString());
			if (trackingArray[18] != null)
				trackingLabel.setLabelSenderName(trackingArray[18].toString());
			if (trackingArray[19] != null)
				trackingLabel.setDeliveryInstructions(trackingArray[19].toString());
			if (trackingArray[20] != null)
				trackingLabel.setConsigneeCompany(trackingArray[20].toString());
			if (trackingArray[21] != null)
				trackingLabel.setCarrier(trackingArray[21].toString());
			if (trackingArray[22] != null)
				trackingLabel.setConsigneeAddr2(trackingArray[22].toString());
			if (trackingArray[23] != null)
				trackingLabel.setReturnAddress1(trackingArray[23].toString());
			if (trackingArray[24] != null)
				trackingLabel.setReturnAddress2(trackingArray[24].toString());
			if (trackingArray[25] != null)
				trackingLabel.setProductDescription(trackingArray[25].toString());
			if (trackingArray[26] != null)
				trackingLabel.setServiceType(trackingArray[26].toString());
			boolean setGS1DataType = false;
			if(trackingLabel.getCarrier().equalsIgnoreCase("Express") || trackingLabel.getCarrier().equalsIgnoreCase("eParcel")) {
				setGS1DataType = true;
			}
			if(trackingLabel.getCarrier().equalsIgnoreCase("StarTrack"))
			{if(trackingLabelData.size() > 1)
			{
				throw new PCAlabelException("Starttradk Label request can contain only one reference number", refBarNum);
			}
			else
			{
				pcalabel = true;
			}
				
			}
			trackingLabel.setDatamatrixImage(generateDataMatrix(trackingLabel.getDatamatrix(),setGS1DataType));
			
			String user = d2zDao.fetchUserById(Integer.parseInt(trackingArray[27].toString()));
			if("VELC".equalsIgnoreCase(user)) {
				trackingLabel.setLabelSenderName("SW4");
			}
			trackingLabelList.add(trackingLabel);
		}
		byte[] bytes = null;
if(!pcalabel)
{
		List<SenderData> eParcelData = new ArrayList<SenderData>();

		List<SenderData> expressData = new ArrayList<SenderData>();
		List<SenderData> fastwayData = new ArrayList<SenderData>();
		List<SenderData> fastway_S_Data = new ArrayList<SenderData>();
		List<SenderData> whiteLabelData = new ArrayList<SenderData>();
		List<SenderData> eParcelNewData = new ArrayList<SenderData>();
		List<SenderData> expressNewData = new ArrayList<SenderData>();
		List<SenderData> parcelPostData = new ArrayList<SenderData>();
		List<SenderData> fwData = new ArrayList<SenderData>();

		for (SenderData data : trackingLabelList) {
			if ("MCM1".equalsIgnoreCase(data.getServiceType()) && data.getCarrier().equalsIgnoreCase("FastwayM")) {
				whiteLabelData.add(data);
			}
			else if ("MCM2".equalsIgnoreCase(data.getServiceType()) && (data.getCarrier().equalsIgnoreCase("Express") || data.getCarrier().equalsIgnoreCase("eParcel")) ) {
				whiteLabelData.add(data);
			}
			else if("MCM3".equalsIgnoreCase(data.getServiceType())) {
				whiteLabelData.add(data);
			}else if("1PM".equalsIgnoreCase(data.getServiceType())) {
				eParcelNewData.add(data);
			}else if("1PME".equalsIgnoreCase(data.getServiceType())) {
				expressNewData.add(data);
			}else if("HKG".equalsIgnoreCase(data.getServiceType())) {
				parcelPostData.add(data);
			}else  if(data.getCarrier().equalsIgnoreCase("eParcel")) {
				eParcelData.add(data);
			} else if (data.getCarrier().equalsIgnoreCase("Express")) {
				expressData.add(data);
			}else if ("FW".equalsIgnoreCase(data.getServiceType()) && data.getCarrier().equalsIgnoreCase("FastwayM")) {
				fwData.add(data);
			}else if(data.getCarrier().equalsIgnoreCase("FastwayM")) {
				fastwayData.add(data);
			}else if(data.getCarrier().equalsIgnoreCase("FastwayS")) {
				fastway_S_Data.add(data);
			}
		}

		Map<String, Object> parameters = new HashMap<>();
		
		// Blob blob = null;
		JRBeanCollectionDataSource eParcelDataSource;
		JRBeanCollectionDataSource expressDataSource;
		JasperReport eParcelLabel = null;
		JasperReport expressLabel = null;
		JRBeanCollectionDataSource fastwayDataSource;
		JasperReport fastwayLabel = null;
		JRBeanCollectionDataSource fastway_S_DataSource;
		JasperReport fastway_S_Label = null;
		JRBeanCollectionDataSource whiteLabelDataSource;
		JasperReport whiteLabel = null;
		JRBeanCollectionDataSource eParcelNewDataSource;
		JasperReport eParcelNew = null;
		JRBeanCollectionDataSource expressNewDataSource;
		JasperReport expressNew = null;
		JRBeanCollectionDataSource parcelPostDataSource;
		JasperReport parcelPost = null;
		JRBeanCollectionDataSource fwDataSource;
		JasperReport fwLabel = null;
		try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			if (!eParcelData.isEmpty()) {
				System.out.println("Generating eParcel..." + eParcelData.size());
				eParcelDataSource = new JRBeanCollectionDataSource(eParcelData);
				eParcelLabel = JasperCompileManager
						.compileReport(getClass().getResource("/eparcelLabel.jrxml").openStream());
				JRSaver.saveObject(eParcelLabel, "label.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(eParcelLabel, parameters, eParcelDataSource));
			}
			if (!expressData.isEmpty()) {
				System.out.println("Generating Express..." + expressData.size());
				expressDataSource = new JRBeanCollectionDataSource(expressData);
				expressLabel = JasperCompileManager
						.compileReport(getClass().getResource("/ExpressLabel.jrxml").openStream());
				JRSaver.saveObject(expressLabel, "express.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(expressLabel, parameters, expressDataSource));
			}
			if (!expressNewData.isEmpty()) {
				System.out.println("Generating Express new..." + expressNewData.size());
				expressNewDataSource = new JRBeanCollectionDataSource(expressNewData);
				expressNew = JasperCompileManager
						.compileReport(getClass().getResource("/ExpressNew.jrxml").openStream());
				JRSaver.saveObject(expressNew, "expressNew.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(expressNew, parameters, expressNewDataSource));
			}
			if (!parcelPostData.isEmpty()) {
				System.out.println("Generating Parcel Post..." + parcelPostData.size());
				parcelPostDataSource = new JRBeanCollectionDataSource(parcelPostData);
				parcelPost = JasperCompileManager
						.compileReport(getClass().getResource("/ParcelPost.jrxml").openStream());
				JRSaver.saveObject(parcelPost, "parcelPost.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(parcelPost, parameters, parcelPostDataSource));
			}
			if (!fastwayData.isEmpty()) {
				System.out.println("Generating Fastway..." + fastwayData.size());
				fastwayDataSource = new JRBeanCollectionDataSource(fastwayData);
				fastwayLabel = JasperCompileManager
						.compileReport(getClass().getResource("/FastWayLabel.jrxml").openStream());
				JRSaver.saveObject(fastwayLabel, "FastWayLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fastwayLabel, parameters, fastwayDataSource));
			}
			if (!fwData.isEmpty()) {
				System.out.println("Generating Fastway FW..." + fwData.size());
				fwDataSource = new JRBeanCollectionDataSource(fwData);
				fwLabel = JasperCompileManager
						.compileReport(getClass().getResource("/FWLabel.jrxml").openStream());
				JRSaver.saveObject(fastwayLabel, "FWLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fwLabel, parameters, fwDataSource));
			}
			if (!fastway_S_Data.isEmpty()) {
				System.out.println("Generating FastwayS..." + fastway_S_Data.size());
				fastway_S_DataSource = new JRBeanCollectionDataSource(fastway_S_Data);
				fastway_S_Label = JasperCompileManager
						.compileReport(getClass().getResource("/FastwayPCA.jrxml").openStream());
				JRSaver.saveObject(fastway_S_Label, "FastwayPCA.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(fastway_S_Label, parameters, fastway_S_DataSource));
			}
			if (!whiteLabelData.isEmpty()) {
				System.out.println("Generating WhiteLabel..." + whiteLabelData.size());
				whiteLabelDataSource = new JRBeanCollectionDataSource(whiteLabelData);
				whiteLabel = JasperCompileManager
						.compileReport(getClass().getResource("/WhiteLabel.jrxml").openStream());
				JRSaver.saveObject(whiteLabel, "whiteLabel.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(whiteLabel, parameters, whiteLabelDataSource));
			}
			if (!eParcelNewData.isEmpty()) {
				System.out.println("Generating EParcel new..." + eParcelNewData.size());
				eParcelNewDataSource = new JRBeanCollectionDataSource(eParcelNewData);
				eParcelNew = JasperCompileManager
						.compileReport(getClass().getResource("/eParcelNew.jrxml").openStream());
				JRSaver.saveObject(eParcelNew, "eParcelNew.jasper");
				jasperPrintList.add(JasperFillManager.fillReport(eParcelNew, parameters, eParcelNewDataSource));
			}
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			exporter.setExporterOutput(exporterOutput);
			exporter.exportReport();
			// return the PDF in bytes
			bytes = outputStream.toByteArray();
			
		} catch (JRException | IOException e) {
			e.printStackTrace();
		}
}
else
{
	bytes = pcaWrapper.pcalabel(trackingLabelList.get(0).getReferenceNumber());
}
		return bytes;
	}

	@Override
	public UserMessage manifestCreation(String manifestNumber, String referenceNumber) {
		String[] refNbrs = referenceNumber.split(",");
		String fileUploadData = d2zDao.manifestCreation(manifestNumber, refNbrs);
		int userId = d2zDao.fetchUserIdByReferenceNumber(refNbrs[0]);
		String autoShipment = userRepository.fetchAutoShipmentIndicator(userId);

		if ("Y".equalsIgnoreCase(autoShipment)) {
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMdd");
    			String shipmentNumber = userId+simpleDateFormat.format(new Date());
				allocateShipment(referenceNumber, manifestNumber.concat("AS").concat(shipmentNumber));
			} catch (ReferenceNumberNotUniqueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(fileUploadData);
		return userMsg;
	}

	/*
	 * public List<TrackParcel> trackParcel(List<String>
	 * referenceNumbers,List<String> articleIDs) { List<TrackParcel> trackParcelList
	 * = new ArrayList<TrackParcel>(); if(!referenceNumbers.isEmpty()) {
	 * trackParcelList = trackParcelByRefNbr(referenceNumbers); } else
	 * if(!articleIDs.isEmpty()) { trackParcelList =
	 * trackParcelByArticleID(articleIDs); } return trackParcelList; }
	 */

	public List<TrackParcel> trackParcelByArticleID(List<String> articleIDs) {
		List<TrackParcel> trackParcelList = new ArrayList<TrackParcel>();

		for (String articleID : articleIDs) {
			List<Trackandtrace> trackAndTraceList = d2zDao.trackParcelByArticleID(articleID);
			TrackParcel trackParcel = new TrackParcel();
			// List<TrackEventDetails> trackEventDetails = new
			// ArrayList<TrackEventDetails>();
			List<TrackingEvents> trackingEventList = new ArrayList<TrackingEvents>();
			for (Trackandtrace daoObj : trackAndTraceList) {
				TrackingEvents trackingEvents = new TrackingEvents();
				trackParcel.setReferenceNumber(daoObj.getReference_number());
				trackParcel.setBarcodelabelNumber(daoObj.getArticleID());
				trackingEvents.setEventDetails(daoObj.getTrackEventDetails());
				trackingEvents.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				trackingEventList.add(trackingEvents);
				/*
				 * switch(daoObj.getTrackEventDetails().toUpperCase()) {
				 * 
				 * case "CONSIGNMENT CREATED":
				 * trackParcel.setConsignmentCreated(String.valueOf(daoObj.
				 * getTrackEventDateOccured())); break; case "SHIPMENT ALLOCATED":
				 * trackParcel.setShipmentCreated(String.valueOf(daoObj.getTrackEventDateOccured
				 * ())); break; case "HELD BY CUSTOMS":
				 * trackParcel.setHeldByCustoms(String.valueOf(daoObj.getTrackEventDateOccured()
				 * )); break; case "CLEARED CUSTOMS":
				 * trackParcel.setClearedCustoms(String.valueOf(daoObj.getTrackEventDateOccured(
				 * ))); break; case "RECEIVED":
				 * trackParcel.setReceived(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; case "PROCESSED BY FACILITY":
				 * trackParcel.setProcessedByFacility(String.valueOf(daoObj.
				 * getTrackEventDateOccured())); break; case "IN TRANSIT":
				 * trackParcel.setInTransit(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; case "DELIVERED":
				 * trackParcel.setDelivered(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; }
				 */
				/*
				 * TrackEventDetails trackEventDetail = new TrackEventDetails();
				 * trackEventDetail.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				 * trackEventDetail.setTrackEventDetails(daoObj.getTrackEventDetails());
				 * 
				 * trackEventDetails.add(trackEventDetail);
				 * trackParcel.setTrackEventDetails(trackEventDetails);
				 */

			}
			trackParcel.setTrackingEvents(trackingEventList);

			trackParcelList.add(trackParcel);
		}
		return trackParcelList;
	}

	public List<TrackParcel> trackParcelByRefNbr(List<String> referenceNumbers) {
		List<TrackParcel> trackParcelList = new ArrayList<TrackParcel>();

		for (String refNbr : referenceNumbers) {
			List<Trackandtrace> trackAndTraceList = d2zDao.trackParcel(refNbr);
			// List<TrackEventDetails> trackEventDetails = new
			// ArrayList<TrackEventDetails>();
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
				/*
				 * switch(daoObj.getTrackEventDetails().toUpperCase()) {
				 * 
				 * case "CONSIGNMENT CREATED":
				 * trackParcel.setConsignmentCreated(String.valueOf(daoObj.
				 * getTrackEventDateOccured())); break; case "SHIPMENT ALLOCATED":
				 * trackParcel.setShipmentCreated(String.valueOf(daoObj.getTrackEventDateOccured
				 * ())); break; case "HELD BY CUSTOMS":
				 * trackParcel.setHeldByCustoms(String.valueOf(daoObj.getTrackEventDateOccured()
				 * )); break; case "CLEARED CUSTOMS":
				 * trackParcel.setClearedCustoms(String.valueOf(daoObj.getTrackEventDateOccured(
				 * ))); break; case "RECEIVED AND CLEAR":
				 * trackParcel.setReceived(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; case "PROCESSED BY FACILITY":
				 * trackParcel.setProcessedByFacility(String.valueOf(daoObj.
				 * getTrackEventDateOccured())); break; case "IN TRANSIT":
				 * trackParcel.setInTransit(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; case "DELIVERED":
				 * trackParcel.setDelivered(String.valueOf(daoObj.getTrackEventDateOccured()));
				 * break; }
				 */
				/*
				 * TrackEventDetails trackEventDetail = new TrackEventDetails();
				 * trackEventDetail.setTrackEventDateOccured(daoObj.getTrackEventDateOccured());
				 * trackEventDetail.setTrackEventDetails(daoObj.getTrackEventDetails());
				 * trackEventDetails.add(trackEventDetail);
				 * trackParcel.setTrackEventDetails(trackEventDetails);
				 */

			}
			trackParcel.setTrackingEvents(trackingEventList);
			trackParcelList.add(trackParcel);
		}
		return trackParcelList;
	}

	@Override
	public List<SenderDataResponse> createConsignments(CreateConsignmentRequest orderDetail,List<String> autoShipRefNbrs) throws 
		ReferenceNumberNotUniqueException, FailureResponseException {
		Integer userId = userRepository.fetchUserIdbyUserName(orderDetail.getUserName());
		if (userId == null) {
			throw new InvalidUserException("User does not exist", orderDetail.getUserName());
		}
		if (orderDetail.getConsignmentData().size() > 300) {
			throw new MaxSizeCountException("We are allowing max 300 records, Your Request contains - "
					+ orderDetail.getConsignmentData().size() + " Records");
		}
		boolean isPostcodeValidationReq =true;
		if(("N").equals(userRepository.fetchPostcodeValidationIndicator(orderDetail.getUserName()))){
			isPostcodeValidationReq=false;
		}
		List<String> incomingRefNbr = orderDetail.getConsignmentData().stream().map(obj -> {
			return obj.getReferenceNumber(); })
				.collect(Collectors.toList());
		d2zValidator.isReferenceNumberUnique(incomingRefNbr);
		d2zValidator.isServiceValid(orderDetail);
		
		List<SenderDataResponse> senderDataResponseList = new ArrayList<SenderDataResponse>();
		SenderDataResponse senderDataResponse = null;
		boolean autoShipment = false;
		String barcodeLabelNumber = orderDetail.getConsignmentData().get(0).getBarcodeLabelNumber();
		String datamatrix = orderDetail.getConsignmentData().get(0).getDatamatrix();
	    if(null==barcodeLabelNumber || barcodeLabelNumber.trim().isEmpty() || null==datamatrix || datamatrix.trim().isEmpty()) {
	    String serviceType = orderDetail.getConsignmentData().get(0).getServiceType();
	    System.out.print("servicetype:"+serviceType);
	    if( "1PM3E".equalsIgnoreCase(serviceType) 
				|| "1PS3".equalsIgnoreCase(serviceType) 
				|| "1PM5".equalsIgnoreCase(serviceType) || "TST1".equalsIgnoreCase(serviceType)) {
	    	if(isPostcodeValidationReq) {
	    		d2zValidator.isPostCodeValid(orderDetail.getConsignmentData());
	    		}
			 System.out.print("servicetype:"+serviceType);
			eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(orderDetail,senderDataResponseList);
			return senderDataResponseList;
		}else if ("FWM".equalsIgnoreCase(serviceType) || "FW".equalsIgnoreCase(serviceType) || "1PS4".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
				if("1PS4".equalsIgnoreCase(serviceType)) {
						d2zValidator.isPostCodeValid(orderDetail.getConsignmentData());
						
				}else {
					d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData());
				}
			}
			makeCreateShippingOrderPFLCall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(),serviceType);
			return senderDataResponseList;
		}else if("FWS".equalsIgnoreCase(serviceType)) {
			if(isPostcodeValidationReq) {
			d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData());
			}
			pcaWrapper.makeCreateShippingOrderPFACall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(),serviceType);
			return senderDataResponseList;
		}else if("MCM".equalsIgnoreCase(serviceType) || "MCM1".equalsIgnoreCase(serviceType) || "MCM2".equalsIgnoreCase(serviceType) 
					|| "MCM3".equalsIgnoreCase(serviceType) || "MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)){
			PFLSenderDataRequest consignmentData = d2zValidator.isFWSubPostCodeValid(orderDetail);
			System.out.println("service type:"+serviceType+":"+consignmentData.getPflSenderDataApi().size());
			if(consignmentData.getPflSenderDataApi().size() > 0) {
				if("MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)){
					System.out.println("inside MCS");
					pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),serviceType);
				}
				else
					makeCreateShippingOrderPFLCall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(), serviceType);
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && "STS".equalsIgnoreCase(serviceType)) {
				pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getNonPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),"STS-Sub");
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && !"STS".equalsIgnoreCase(serviceType)) {
				if(isPostcodeValidationReq) {
		    		d2zValidator.isPostCodeValid(orderDetail.getConsignmentData());
		    		}
				String senderFileID = d2zDao.createConsignments(consignmentData.getNonPflSenderDataApi(), userId, orderDetail.getUserName(), null);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					senderDataResponse = new SenderDataResponse();
					senderDataResponse.setReferenceNumber(obj[0].toString());
					senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
					if("MCS".equalsIgnoreCase(serviceType))
							{
						String barcode = obj[1].toString();
						senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
						
						if(senderDataResponse.getInjectionPort().equals("SYD") ||senderDataResponse.getInjectionPort().equals("MEL")||senderDataResponse.getInjectionPort().equals("BNE")||senderDataResponse.getInjectionPort().equals("ADL") ||senderDataResponse.getInjectionPort().equals("PER"))
						{
							senderDataResponse.setSortcode(senderDataResponse.getInjectionPort());
						}
						else
						{
							senderDataResponse.setSortcode("OTH");
						}
							}
					else
					{
					senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					}
					senderDataResponse.setCarrier(obj[4].toString());
					
					senderDataResponseList.add(senderDataResponse);
				}
			}
			return senderDataResponseList;
		}
		
		}else if (barcodeLabelNumber!=null && !barcodeLabelNumber.trim().isEmpty()
				&& datamatrix!=null && !datamatrix.trim().isEmpty()){
		autoShipment =("Y").equals(userRepository.fetchAutoShipmentIndicator(userId));
		}
	    if(isPostcodeValidationReq) {
    		d2zValidator.isPostCodeValid(orderDetail.getConsignmentData());
    		}
		String senderFileID = d2zDao.createConsignments(orderDetail.getConsignmentData(), userId, orderDetail.getUserName(), null);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		

		Iterator itr = insertedOrder.iterator();
		while (itr.hasNext()) {
			
			Object[] obj = (Object[]) itr.next();
			senderDataResponse = new SenderDataResponse();
			senderDataResponse.setReferenceNumber(obj[0].toString());
			String barcode = obj[1].toString();
			
			senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
			senderDataResponse.setCarrier(obj[4].toString());
			senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
			senderDataResponseList.add(senderDataResponse);
			if(autoShipment)
				autoShipRefNbrs.add(senderDataResponse.getReferenceNumber());
		}
		
		
		return senderDataResponseList;
	}

	private void makeCreateShippingOrderPFLCall (List<SenderDataApi> data,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws FailureResponseException {
		PflCreateShippingRequest pflRequest = new PflCreateShippingRequest();
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		List<PflCreateShippingOrderInfo> pflOrderInfoRequest = new ArrayList<PflCreateShippingOrderInfo>();
		for (SenderDataApi orderDetail : data) {
			PflCreateShippingOrderInfo request = new PflCreateShippingOrderInfo();
			 //Random rnd = new Random();
			 int uniqueNumber = SingletonCounter.getInstance().getPFLCount();
    		 String sysRefNbr = "RTFGA"+uniqueNumber;
    		 request.setCustom_ref(sysRefNbr);
  			systemRefNbrMap.put(request.getCustom_ref(), orderDetail.getReferenceNumber());

			//request.setCustom_ref(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			//request.setEmail(orderDetail.getConsigneeEmail());
			request.setPhone(orderDetail.getConsigneePhone());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setCountry("AU");
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			pflOrderInfoRequest.add(request);
		}
		pflRequest.setOrderinfo(pflOrderInfoRequest);
		pflWrapper.createShippingOrderPFL(data, pflRequest, userName, senderDataResponseList, serviceType,systemRefNbrMap);
	}
	
	private void makeCreateShippingOrderFilePFLCall (List<SenderData> data,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws FailureResponseException {
		PflCreateShippingRequest pflRequest = new PflCreateShippingRequest();
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();
		List<PflCreateShippingOrderInfo> pflOrderInfoRequest = new ArrayList<PflCreateShippingOrderInfo>();
		for (SenderData orderDetail : data) {
			PflCreateShippingOrderInfo request = new PflCreateShippingOrderInfo();
			 //Random rnd = new Random();
			 int uniqueNumber = SingletonCounter.getInstance().getPFLCount();
    		 String sysRefNbr = "RTFGA"+uniqueNumber;
    		 request.setCustom_ref(sysRefNbr);
  			systemRefNbrMap.put(request.getCustom_ref(), orderDetail.getReferenceNumber());
		//	request.setCustom_ref(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			//request.setEmail(orderDetail.getConsigneeEmail());
			request.setPhone(orderDetail.getConsigneePhone());
			request.setCity(orderDetail.getConsigneeSuburb());
			request.setState(orderDetail.getConsigneeState());
			request.setPostcode(orderDetail.getConsigneePostcode());
			request.setCountry("AU");
			request.setWeight(Double.valueOf(orderDetail.getWeight()));
			pflOrderInfoRequest.add(request);
		}
		pflRequest.setOrderinfo(pflOrderInfoRequest);
		pflWrapper.createShippingOrderPFLUI(data, pflRequest, userName, senderDataResponseList, serviceType,systemRefNbrMap);
	}

	@Override
	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList) {
		return d2zDao.editConsignments(requestList);
	}

	@Override
	public ResponseMessage allocateShipment(String referenceNumbers, String shipmentNumber)
			throws ReferenceNumberNotUniqueException {
	
		ResponseMessage userMsg = new ResponseMessage();
		String[] refNbrs = referenceNumbers.split(",");
		List<String> refNumbers = Arrays.asList(refNbrs);
	
		List<SenderdataMaster> consignments = d2zDao.fetchConsignmentsByRefNbr(refNumbers);
		if(consignments.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Failure - Invalid Reference Numbers", refNumbers);
		}
		List<String> invalidData = consignments.stream().filter(obj -> null!=obj.getAirwayBill())
				.map(a -> {
			
				String msg = a.getReference_number()+" : Shippment is already allocated";
			
				return msg;
		}).collect(Collectors.toList());
		if (!invalidData.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Request failed", invalidData);
		}
      
		String msg = d2zDao.allocateShipment(referenceNumbers, shipmentNumber);
		userMsg.setResponseMessage(msg);
		  if(consignments.size() < refNumbers.size()) {
	        	List<String> refNbr_DB = consignments.stream().map(obj->{
	        		return obj.getReference_number();
	        	}).collect(Collectors.toList());
	        	List<String> invalidRefNbrs = new ArrayList<String>(refNumbers);
	        	invalidRefNbrs.removeAll(refNbr_DB);
	        	userMsg.setResponseMessage("Partial Success - Invalid Reference Numbers : "+String.join(",",invalidRefNbrs));
	        }
		Runnable freipost = new Runnable( ) {			
	        public void run() {
	        	String[] refNbrArray = referenceNumbers.split(",");

	        	 List<String> articleIDS = d2zDao.fetchDataForEtowerForeCastCall(refNbrs);
	        	 if(!articleIDS.isEmpty()) {
	        		 eTowerWrapper.makeEtowerForecastCall(articleIDS);
	        	 }
	        	
	        	List<SenderdataMaster> senderMasterData = d2zDao.fetchDataBasedonSupplier(Arrays.asList(refNbrArray),"Freipost");
	        	if(!senderMasterData.isEmpty()) {
	        		freipostWrapper.uploadManifestService(senderMasterData);
	        	}
	        	
	        	List<SenderdataMaster> pcaData = d2zDao.fetchDataBasedonSupplier(Arrays.asList(refNbrArray),"PCA");
	        	if(!pcaData.isEmpty()) {
	        		try {
						pcaWrapper.makeCreateShippingOrderPCACall(pcaData);
					} catch (FailureResponseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        
	        	List<String> fastwayOrderId = d2zDao.fetchDataforPFLSubmitOrder(refNbrs);
	        	String serviceType = d2zDao.fetchServiceTypeByRefNbr(refNbrs[0]);
	        	 if(!fastwayOrderId.isEmpty()) {
	        		 try {
						pflWrapper.createSubmitOrderPFL(fastwayOrderId,serviceType);
					} catch (FailureResponseException e) {
						e.printStackTrace();
					}
	        	 }
	        
	        }};
	        new Thread(freipost).start();
	        
		/* Runnable pfl = new Runnable( ) {			
		        public void run() {
		        }
		    };
			new Thread(pfl).start();
		
		 Runnable r = new Runnable( ) {			
	        public void run() {
	        	
	        }
	     };
		 new Thread(r).start();*/
		 
		
		return userMsg;
	 }

	// @Scheduled(cron = "0 0 0/2 * * ?")
	// @Scheduled(cron = "0 0/10 * * * ?")
	public void makeCalltoAusPost() {
		List<String> referenceNumbers = d2zDao.fetchDataForAUPost();
		System.out.println("Track and trace:" + referenceNumbers.size());
		if (referenceNumbers.size() < 10) {
			System.out.println("Less than 10 records for AUPost call");
			return;
		}
		makeCalltoAusPost(referenceNumbers);
	}
	
	public void makeCalltoAusPost(List<String> referenceNumbers) {
	System.out.println(referenceNumbers.get(0));
	System.out.println(referenceNumbers.get(1));
		List<SenderdataMaster> senderMasterData = d2zDao.fetchDataForAusPost(referenceNumbers);
		System.out.println("Sender Data:" + senderMasterData.size());

		List<List<SenderdataMaster>> senderDataList = ListUtils.partition(senderMasterData, 2000);
		for (List<SenderdataMaster> senderData : senderDataList) {
			CreateShippingRequest request = new CreateShippingRequest();

			Date dNow = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmm");
			String orderRef = ft.format(dNow);

			request.setOrder_reference(orderRef);
			request.setPayment_method("CHARGE_TO_ACCOUNT");

			List<ShipmentRequest> shipments = new ArrayList<ShipmentRequest>();
			for (SenderdataMaster data : senderData) {
				String email = null;
				if (data.getConsignee_Email() != null && !data.getConsignee_Email().trim().isEmpty()
						&& data.getConsignee_Email().contains("@")) {
					email = data.getConsignee_Email().trim();
				}
				ShipmentRequest shipmentRequest = new ShipmentRequest();
				 //Random rnd = new Random();
				 int uniqueNumber = SingletonCounter.getInstance().getAuPostCount();
	    		 String sysRefNbr = "DA"+uniqueNumber;
				//shipmentRequest.setSender_references(data.getReference_number());
	    		 shipmentRequest.setSender_references(sysRefNbr);
				shipmentRequest.setEmail_tracking_enabled(email != null);

				FromAddress from = new FromAddress();

				shipmentRequest.setFrom(from);

				ToAddress to = new ToAddress();
				to.setName(data.getConsignee_name().length() > 39	
				        ? data.getConsignee_name().substring(0, 39)
						:data.getConsignee_name());
				
				to.setPostcode(data.getConsignee_Postcode());
				to.setState(data.getConsignee_State());
				to.setSuburb(data.getConsignee_Suburb());
				to.getLines().add(data.getConsignee_addr1().length() > 39	
						        ? data.getConsignee_addr1().substring(0, 39)
								:data.getConsignee_addr1());
				String regex = "^[0-9]{1,20}$";
				String phone = "";
				if (null != data.getConsignee_Phone() && data.getConsignee_Phone().matches(regex)) {
					phone = data.getConsignee_Phone();
				}
				to.setPhone(phone);
				to.setEmail(email);
				to.setDelivery_instructions(data.getDeliveryInstructions());
				shipmentRequest.setTo(to);

				List<Items> items = new ArrayList<Items>();
				Items item = new Items();
				// item.setHeight(data.getDimensions_Height() == null ? "" :
				// data.getDimensions_Height().toString());
				// item.setLength(data.getDimensions_Length() == null ? "" :
				// data.getDimensions_Length().toString());
				// item.setWidth(data.getDimensions_Width() == null ? "" :
				// data.getDimensions_Width().toString());
				item.setItem_description(data.getProduct_Description().length() > 50
						? data.getProduct_Description().substring(0, 50)
								:data.getProduct_Description());
				item.setWeight(data.getCubic_Weight() == null ? "" : data.getCubic_Weight().toString());

				com.d2z.d2zservice.model.auspost.TrackingDetails trackingDetail = new com.d2z.d2zservice.model.auspost.TrackingDetails();
				trackingDetail.setArticle_id(data.getArticleId());
				StringBuffer barcode = new StringBuffer(data.getDatamatrix().replaceAll("\\[|\\]", ""));
				barcode.insert(41, '|');
				barcode.insert(49, '|');
				trackingDetail.setBarcode_id(barcode.toString());
				
				trackingDetail.setConsignment_id(data.getArticleId().substring(0, 12));
				item.setTracking_details(trackingDetail);

				items.add(item);
				shipmentRequest.setItems(items);

				shipments.add(shipmentRequest);
			}

			request.setShipments(shipments);

			String response = ausPostProxy.createOrderIncludingShipments(request);
			List<AUPostResponse> AUPostResponseList = new ArrayList<AUPostResponse>();
			JacksonJsonParser jsonParser = new JacksonJsonParser();
			Map<String, Object> responses = jsonParser.parseMap(response);
			if (responses.containsKey("order")) {
				LinkedHashMap<String, Object> order = (LinkedHashMap<String, Object>) responses.get("order");

				String orderid = (String) order.get("order_id");
				String orderreference = (String) order.get("order_reference");
				String ordercreationdate = (String) order.get("order_creation_date");
				List<Map<String, Object>> shipmentlist = (List<Map<String, Object>>) order.get("shipments");
				for (Map<String, Object> shipment : shipmentlist) {
					AUPostResponse auresponse = new AUPostResponse();
					auresponse.setApiname("AU post");
					auresponse.setOrderid(orderid);
					auresponse.setOrderreference(orderreference);
					auresponse.setOrderCreationDate(ordercreationdate.substring(0, 19));
					// auresponse.setOrderCreationDate(Timestamp.valueOf(ordercreationdate));
					auresponse.setShipmentId((String) shipment.get("shipment_id"));
					Map<String, Object> shipment_summary = (Map<String, Object>) shipment.get("shipment_summary");
					auresponse.setCost("" + shipment_summary.get("total_cost"));
					auresponse.setFuelSurcharge("" + shipment_summary.get("fuel_surcharge"));
					auresponse.setGST("" + shipment_summary.get("total_gst"));

					List<Map<String, Object>> itemlist = (List<Map<String, Object>>) shipment.get("items");
					for (Map<String, Object> item : itemlist) {
						auresponse.setItemId((String) (item.get("item_id")));
						Map<String, Object> tracking_summary = (Map<String, Object>) item.get("tracking_details");
						auresponse.setArticleId((String) (tracking_summary.get("article_id")));
						auresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
						AUPostResponseList.add(auresponse);
					}

				}
			} else {
				List<Map<String, Object>> errorlist = (List<Map<String, Object>>) responses.get("errors");

				for (Map<String, Object> error : errorlist) {
					AUPostResponse auresponse = new AUPostResponse();
					auresponse.setApiname("AU post");
					auresponse.setErrorCode((String) error.get("code"));
					auresponse.setName((String) error.get("name"));
					auresponse.setMessage((String) error.get("message"));
					auresponse.setField((String) error.get("field"));
					auresponse.setArticleId(UUID.randomUUID().toString());

					/*
					 * String Field = error.get("context").toString().split("=")[1];
					 * auresponse.setField(Field.substring(0, (Field.length()-1)));
					 */

					auresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
					AUPostResponseList.add(auresponse);

				}

			}
			// AUResponseRepository.saveAll(AUPostResponseList);
			d2zDao.logAUPostResponse(AUPostResponseList);

		}

	}

	@Override
	public UserMessage addUser(UserDetails userData) {
		UserMessage userMsg = new UserMessage();
		User existingUser = userRepository.fetchUserbyCompanyName(userData.getCompanyName(), userData.getRole_Id());
		if (existingUser == null) {
			List<String> existingUserNames = userRepository.fetchAllUserName();
			if (existingUserNames.contains(userData.getUserName())) {
				userMsg.setMessage("UserName already exist");
				userMsg.setUserName(userData.getUserName());
				return userMsg;
			}
			User savedUser = d2zDao.addUser(userData);
			if (savedUser.getUser_Id() != 0) {
				List<UserService> savedUserService = d2zDao.addUserService(savedUser, userData.getServiceType());
				if (savedUserService.size() != 0) {
					userMsg.setMessage("User Added Successfully");
					userMsg.setUserName(userData.getUserName());
				}
			} else {
				userMsg.setMessage("Unable to Add User");
				userMsg.setUserName(userData.getUserName());
			}
		} else {
			userMsg.setMessage("Company Name already exist");
			userMsg.setCompanyName(userData.getCompanyName());
		}
		return userMsg;
	}

	@Override
	public UserMessage updateUser(UserDetails userDetails) {
		UserMessage userMsg = new UserMessage();
		User existingUser = userRepository.fetchUserbyCompanyName(userDetails.getCompanyName(),
				userDetails.getRole_Id());
		if (existingUser == null) {
			userMsg.setMessage("Company Name does not exist");
			userMsg.setCompanyName(userDetails.getCompanyName());
			return userMsg;
		} else {
			if (!existingUser.getUsername().equals(userDetails.getUserName())) {
				List<String> existingUserNames = userRepository.fetchAllUserName();
				if (existingUserNames.contains(userDetails.getUserName())) {
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
			d2zDao.updateUserService(updatedUser, userDetails);
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
		List<SenderdataMaster> fileData = d2zDao.fetchManifestData(fileName);
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
	// public byte[] downloadShipmentData(String shipmentNumber) {
	public List<ShipmentDetails> downloadShipmentData(String shipmentNumber, Integer userId) {
		List<Integer> listOfClientId = d2zBrokerDao.getClientId(userId);
		List<SenderdataMaster> senderDataList = d2zDao.fetchShipmentData(shipmentNumber, listOfClientId);
		System.out.println(senderDataList.size() + " records");
		Double audcurrency = d2zDao.getAudcurrency("USD");
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for (SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			shipmentData.setReferenceNumber(senderData.getReference_number());
			//shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19, 30));
			String connoteNo = "";
			if(null!=senderData.getBarcodelabelNumber()) {
				if(senderData.getBarcodelabelNumber().length()==12) {
					connoteNo = senderData.getBarcodelabelNumber();
				}
				else if(senderData.getBarcodelabelNumber().length()==39) {
					connoteNo = senderData.getBarcodelabelNumber().substring(18, 28);
				}
				else if(senderData.getBarcodelabelNumber().length()==41) {
					connoteNo = senderData.getBarcodelabelNumber().substring(18,30);
				}
			}
			shipmentData.setCon_no(connoteNo);
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			shipmentData.setWeight(senderData.getWeight());
			shipmentData.setConsigneePhone(senderData.getConsignee_Phone());
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			shipmentData.setDestination("AUSTRALIA");
			shipmentData.setQuantity(senderData.getShippedQuantity());
			String commodity = senderData.getProduct_Description();
			if(null!=commodity) {
			if(commodity.length()>50) {
				commodity = "Car Accessory";
			}
			if(commodity.equalsIgnoreCase("Education Board")) {
				commodity = "Education Accessory";
			}
			}
			shipmentData.setCommodity(commodity);
			Double uscurrency = senderData.getValue()*audcurrency;
			shipmentData.setValue(Math.round(uscurrency*100.0)/100.0);
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
		List<ParcelStatus> trackParcelList = new ArrayList<ParcelStatus>();

		for (String referenceNumber : referenceNumbers) {
			Trackandtrace trackAndTrace = d2zDao.getLatestStatusByReferenceNumber(referenceNumber);
			if (trackAndTrace != null) {
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

		List<ParcelStatus> trackParcelList = new ArrayList<ParcelStatus>();

		for (String articleID : articleIDs) {
			Trackandtrace trackAndTrace = d2zDao.getLatestStatusByArticleID(articleID);
			if (trackAndTrace != null) {
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
		int successCount = 0;
		for (Ebay_Shipment shipment : shipmentDetails.getShipment()) {
			CompleteSaleResponseType response = proxy.makeCalltoEbay_CompleteSale(shipment,
					shipmentDetails.getClientEbayToken());
			d2zDao.logEbayResponse(response);
			if (response != null && "SUCCESS".equalsIgnoreCase(response.getAck().toString())) {
				successCount++;
			}
			if (successCount == shipmentDetails.getShipment().size()) {
				userMsg.setMessage("Data sucessfully uploaded to EBay Server");
			} else if (successCount == 0) {
				userMsg.setMessage("Failed to upload data to EBay Server");
			} else {
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
	public UserMessage deleteConsignments(@Valid DeleteConsignmentRequest request)
			throws ReferenceNumberNotUniqueException {
		UserMessage userMsg = new UserMessage();
		User user = d2zDao.login(request.getUserName(), request.getPassword());
		if (null == user) {
			userMsg.setMessage("Invalid UserName or Password");
			return userMsg;
		}
		List<String> referenceNumbers_DB = d2zDao.fetchReferenceNumberByUserId(user.getUser_Id());
		List<String> incomingRefNbr = request.getReferenceNumbers();

		List<String> invalidRefNbr = incomingRefNbr.stream().filter(a -> !referenceNumbers_DB.contains(a))
				.collect(Collectors.toList());
		if (!invalidRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Invalid Reference Number", invalidRefNbr);
		}
		List<SenderdataMaster> shipment_Manifest = d2zDao.fetchConsignmentsManifestShippment(incomingRefNbr);

		List<String> invalidData = shipment_Manifest.stream().map(a -> {
			StringBuffer msg = new StringBuffer(a.getReference_number());
			if (null != a.getManifest_number()) {
				msg.append(" : Manifest is already created");
			}
			if (null != a.getAirwayBill()) {
				msg = new StringBuffer(a.getReference_number());
				msg.append(" : Shippment is already allocated");
			}
			return msg.toString();
		}).collect(Collectors.toList());
		if (!invalidData.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Consignment cannot be deleted", invalidData);
		}
		String referenceNumbers = String.join(",", incomingRefNbr);

		d2zDao.deleteConsignment(referenceNumbers);
		Runnable r = new Runnable( ) {			
		        public void run() {
		        	List<String> inc = incomingRefNbr.stream().map(obj ->obj+"Delete").collect(Collectors.toList());
		        System.out.println("in Thread for Delete pfl etower");
		        	deleteEtowerPflPca(inc);
		        	
		        }
		     };
		    new Thread(r).start();
		userMsg.setMessage("Deleted Successfully");
		return userMsg;
	}

	@Override
	public List<PostCodeWeight> getRates(@Valid APIRatesRequest request) {
		System.out.println("name:"+request.getUserName());
		User user = d2zDao.login(request.getUserName(), request.getPassword());

		if (null == user) {
			throw new InvalidUserException("Invalid Username or Password", null);
		}
//String ServiceType = request.getServicetype();
		Map<String, Double> postcodeWeightMap = D2ZSingleton.getInstance().getPostCodeWeightMap();
		request.getConsignmentDetails().forEach(obj -> {
			double weight = obj.getWeight();
			double maxWeight = 0;
			double minWeight = 0;
			if (weight >= 0 && weight <= 0.5) {
				maxWeight = 0.50;
			} else if (weight > 0.5 && weight <= 1) {
				maxWeight = 1;
			} else if (weight > 1 && weight <= 2) {
				maxWeight = 2;
			} else if (weight > 2 && weight <= 3) {
				maxWeight = 3;
			} else if (weight > 3 && weight <= 4) {

				maxWeight = 4;
			} else if (weight > 4 && weight <= 5) {

				maxWeight = 5;
			} else if (weight > 5 && weight <= 7) {

				maxWeight = 7;
			} else if (weight > 7 && weight <= 10) {

				maxWeight = 10;
			} else if (weight > 10 && weight <= 15) {

				maxWeight = 15;
			} else if (weight > 15 && weight <= 22) {

				maxWeight = 22;
			}
			//double rate = postcodeWeightMap.get(obj.getPostcode() + maxWeight + user.getUser_Id()+ServiceType);
			double rate = postcodeWeightMap.get(obj.getPostcode() + maxWeight + user.getUser_Id());
			obj.setRate(rate);
		});
		return request.getConsignmentDetails();
	}

	@Override
	public UserMessage contactUs(String email, String messageData, String name, String subject) {
		UserMessage userMsg = new UserMessage();
		System.out.print("email:" + email + "message:" + messageData);

		final String fromEmail = "cs@d2z.com.au";

		final String password = "rydjwqzrxhvcwhrb";

		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.office365.com");

		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

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
	public void triggerFreipost(String referenceNumbers) {
		//d2zDao.makeFriePostUpdataManifestCall(referenceNumbers);
		String[] refNbrArray = referenceNumbers.split(",");
    	List<SenderdataMaster> senderMasterData = d2zDao.fetchDataBasedonSupplier(Arrays.asList(refNbrArray),"Freipost");
    	if(!senderMasterData.isEmpty()) {
    		freipostWrapper.uploadManifestService(senderMasterData);
    	}
		//freipostWrapper.trackingEventService("124538");
	}

	@Override
	public void triggerFDM() {
		List<String> refNumbers = d2zDao.fetchArticleIDForFDMCall();
		System.out.println("Track and trace:" + refNumbers.size());
		List<List<String>> refNbrList = ListUtils.partition(refNumbers, 2000);
		for (List<String> referenceNumbers : refNbrList) {
			List<SenderdataMaster> senderData = d2zDao.fetchDataForAusPost(referenceNumbers);
			System.out.println("Sender Data:" + senderData.size());
				if (!senderData.isEmpty()) {
					FDMManifestRequest request = new FDMManifestRequest();
					Date dNow = new Date();
					SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmm");
					String orderRef = ft.format(dNow);
					FDMManifestDetails fdmDetails = new FDMManifestDetails();
					fdmDetails.setMessage_no(orderRef);
					fdmDetails.setCustomer_id("D2Z");
					List<FFResponse> FFResponseList = new ArrayList<FFResponse>();
	
					ArrayOfConsignment consignmentsArray = new ArrayOfConsignment();
					List<Consignment> consignments = new ArrayList<Consignment>();
						for (SenderdataMaster data : senderData) {
							Consignment consignment = new Consignment();
							FFResponse ffresponse = new FFResponse();
							ffresponse.setMessage(orderRef);
							ffresponse.setBarcodelabelnumber(data.getBarcodelabelNumber());
							ffresponse.setWeight(String.valueOf(data.getCubic_Weight()));
							ffresponse.setArticleid(data.getArticleId());
							ffresponse.setReferencenumber(data.getReference_number());
							// ffresponse.setSupplier(data.getsu);
							ffresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
							ffresponse.setSupplier("FDM");
							ffresponse.setResponse("Pending");
							consignment.setConnote_no(data.getArticleId());
							consignment.setTracking_connote(data.getReference_number());
		
							String date = data.getTimestamp();
							try {
								Date dateFormat = new SimpleDateFormat("YYMMDDHHMMSS").parse(date);
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
							lineItem.setDim_height(
									data.getDimensions_Height() == null ? "" : data.getDimensions_Height().toString());
							lineItem.setDim_length(
									data.getDimensions_Length() == null ? "" : data.getDimensions_Length().toString());
							lineItem.setDim_width(
									data.getDimensions_Width() == null ? "" : data.getDimensions_Width().toString());
							itemList.add(lineItem);
							details.setLine(itemList);
							consignment.setDetails(details);
							consignments.add(consignment);
							FFResponseList.add(ffresponse);
						}
					 consignmentsArray.setConsignment(consignments);
					 fdmDetails.setConsignments(consignmentsArray);
					 request.setManifest(fdmDetails);

					 Gson gson = new Gson();
					 String jsonStr = gson.toJson(request);
					 JSONObject json = new JSONObject(jsonStr);
					 String requestXml = XML.toString(json);
					 byte[] contentInBytes = requestXml.getBytes();
					 InputStream targetStream = new ByteArrayInputStream(contentInBytes);
					 System.out.println("in:"+targetStream+"request:"+request);
					 //ftpUploader.fdmFileCreation(request);
					 System.out.println("FDM Request ---->");
					 System.out.println(request);
					 //ftpUploader.ftpUpload(targetStream);
					 //ffresponseRepository.saveAll(FFResponseList);
					 ftpUploader.fdmFileCreation(request);
					// ffresponseRepository.saveAll(FFResponseList);
					// String response = fdmProxy.makeCallToFDMManifestMapping(request);
					/* List <FFResponse> FFresponsequery =
					 ffresponseRepository.findByMessageNoIs(orderRef);
					 List <FFResponse> FFResponseUpdaList = new ArrayList<FFResponse>();
					 for (FFResponse temp : FFresponsequery) {
					 temp.setResponse(response);
					 FFResponseUpdaList .add(temp);
					 }
					 ffresponseRepository.saveAll(FFResponseUpdaList);*/
				}
		}
	}

	@Override
	public ResponseMessage auTrackingEvent() {
		trackAndTraceRepository.updateAUPostTrack();
		ResponseMessage respMsg = null;
		List<String> articleIdData = trackAndTraceRepository.getArticleId();
		List<String> articleIds = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		Iterator itr = articleIdData.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			articleIds.add(obj[0].toString());
			map.put(obj[0].toString(),obj[1].toString());
			
		}
		List<List<String>> articleIdList = ListUtils.partition(articleIds, 10);
		int count = 1;
		for (List<String> articleIdNumbers : articleIdList) {
			System.out.println(count + ":::" + articleIdNumbers.size());
			count++;
			String articleId = StringUtils.join(articleIdNumbers, ", ");
			TrackingResponse auTrackingDetails = ausPostProxy.trackingEvent(articleId);
			System.out.println("AU Track Response");
			System.out.println(auTrackingDetails.toString());
			respMsg = d2zDao.insertAUTrackingDetails(auTrackingDetails,map);
			System.out.print("timestamp:"+LocalDateTime.now());
			try {
				if(count > 10)
				{
				Thread.sleep(60000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print("After thread timestamp:"+LocalDateTime.now());
		}
		return respMsg;
	}


	@Override
	public void updateRates() {
		d2zDao.updateRates();
	}
	
	@Override
	public void updateCubicWeight() {
		d2zDao.updateCubicWeight();
	}
	@Override
	public void makeCallToEtowerBasedonSupplierUI(List<String> incomingRefNbr) {
		List<SenderdataMaster> eTowerOrders = d2zDao.fetchDataBasedonSupplier(incomingRefNbr,"eTower");
		eTowerWrapper.makeCalltoEtower(eTowerOrders);
	}
	
	@Override
	public void freipostTrackingEvent() {
		List<String> articleIds = d2zDao.getArticleIDForFreiPostTracking();
		for(String articleId : articleIds) {
			freipostWrapper.trackingEventService(articleId);
		}
	}

	@Override
	public EnquiryResponse createEnquiry(Enquiry createEnquiry) throws ReferenceNumberNotUniqueException {
		EnquiryResponse enquiryInfo = d2zDao.createEnquiry(createEnquiry);
		return enquiryInfo;
	}

	@Override
	public List<CSTickets> fetchEnquiry(String status, String fromDate, String toDate, String userId) {
		List<CSTickets> enquiryDetails = d2zDao.fetchEnquiry(status,fromDate,toDate,userId);
		return enquiryDetails;
	}

	@Override
	public List<CSTickets> fetchCompletedEnquiry(String userId) {
		List<CSTickets> enquiryDetails = d2zDao.fetchCompletedEnquiry(userId);
		return enquiryDetails;
	}

	@Override
	public List<Integer> fetchUserId(String userId) {
		List<Integer> userIds = d2zDao.fetchUserId(userId);
		return userIds;
	}

	@Override
	public ResponseMessage allocateShipmentArticleid(String ArticleId, String shipmentNumber)
			throws ReferenceNumberNotUniqueException {
		// TODO Auto-generated method stub
		String[] articleNbrs = ArticleId.split(",");
		
		
		List<String>refnbrs = d2zDao.fetchReferencenumberByArticleid(Arrays.asList(articleNbrs));
		String referenceNumbers =refnbrs.stream()
                .collect(Collectors.joining(","));
		ResponseMessage userMsg= allocateShipment(referenceNumbers, shipmentNumber);
		return userMsg;
	}

	@Override
	public UserMessage addUserService(String username,String serviceType) {
		UserMessage userMsg = new UserMessage();
		User usr = userRepository.findByUsername(username);
		if(usr!=null){
		 List<String> serviceTypeList = Arrays.asList(serviceType.split("\\s*,\\s*"));
		 List<UserService> savedUserService = d2zDao.addUserService(usr, serviceTypeList);
		
		 if (savedUserService.size() != 0) {
					userMsg.setMessage("User Service Added Successfully");
					userMsg.setUserName(username);
			}else {
				userMsg.setMessage("Unable to Add User Service");
				userMsg.setUserName(username);
			}
		}
		else{
			userMsg.setMessage("UserName doesnot Exist");
			userMsg.setUserName(username);
		}
		return userMsg;
	}

	@Override
	public void currencyRate() {
		d2zDao.logcurrencyRate();
	}
	
	public void deleteEtowerPflPca(List<String> refnbr){
		
		List<String> pflfwarticleid = new ArrayList<String>();
		List<String> pflarticleid = new ArrayList<String>();
		List<String> pcaArticleid = new ArrayList<String>();
		System.out.println(refnbr);
		List<SenderdataMaster> senderdata = d2zDao.fetchDataBasedonrefnbr(refnbr);
		System.out.println(senderdata.size());
		List<SenderdataMaster> eTowerOrders = d2zDao.fetchDataBasedonSupplier(refnbr,"eTower");
		System.out.println(eTowerOrders.size());
		List<String> etowerreference = eTowerOrders.stream().map(obj -> {
			String s = obj.getReference_number().replace("Delete", ""); 
			return s;})
				.collect(Collectors.toList());
		for(SenderdataMaster data : senderdata){
			if(data.getCarrier().equals("FastwayM") ){
				if(data.getServicetype().equalsIgnoreCase("FW"))
				{
					pflfwarticleid.add(data.getArticleId());
				}
				else
				{
				pflarticleid.add(data.getArticleId());
				}
			}
			if(data.getCarrier().equals("FastwayS"))
				pcaArticleid.add(data.getArticleId());
		}
		
		try {
			System.out.println("etower size:"+etowerreference.size()+":::"+"Pfl Size:"+pflarticleid.size()+":::"+"Pfl Size:"+pcaArticleid.size());
			if(etowerreference.size() > 0){
				eTowerWrapper.DeleteShipingResponse(etowerreference);
			}
			if(pflarticleid.size() > 0){
				pflWrapper.DeleteOrderPFL(pflarticleid,"");
			}
			if(pflfwarticleid.size() > 0){
				pflWrapper.DeleteOrderPFL(pflfwarticleid,"FW");
			}
			if(pcaArticleid.size() > 0) {
				pcaWrapper.deletePcaOrder(pcaArticleid);
			}
		} catch (FailureResponseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Returns> returnsOutstanding(String fromDate, String toDate, String userId) {
		List<Returns> outstandingData =  d2zDao.returnsOutstanding(fromDate,toDate,userId);
		return outstandingData;
	}


	@Override
	public List<ShipmentDetails> downloadShipmentDatabyType(List<String> number, Integer userId, String type) {
		// TODO Auto-generated method stub
		
		List<Integer> listOfClientId = d2zBrokerDao.getClientId(userId);
		List<SenderdataMaster> senderDataList = d2zDao.fetchShipmentDatabyType(number, listOfClientId,type);
		System.out.println(senderDataList.size() + " records");
		Double audcurrency = d2zDao.getAudcurrency("USD");
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for (SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			shipmentData.setReferenceNumber(senderData.getReference_number());
			//shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19, 30));
			String connoteNo = "";
			if(null!=senderData.getBarcodelabelNumber()) {
				if(senderData.getBarcodelabelNumber().length()==12) {
					connoteNo = senderData.getBarcodelabelNumber();
				}
				else if(senderData.getBarcodelabelNumber().length()==39) {
					connoteNo = senderData.getBarcodelabelNumber().substring(18,28);
				}
				else if(senderData.getBarcodelabelNumber().length()==41) {
					connoteNo = senderData.getBarcodelabelNumber().substring(18,30);
				}
			}
			 
			shipmentData.setCon_no(connoteNo);
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			shipmentData.setWeight(senderData.getWeight());
			shipmentData.setConsigneePhone(senderData.getConsignee_Phone());
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			shipmentData.setDestination("AUSTRALIA");
			shipmentData.setQuantity(senderData.getShippedQuantity());
			String commodity = senderData.getProduct_Description();
			if(null!=commodity) {
			if(commodity.length()>50) {
				commodity = "Car Accessory";
			}
			if(commodity.equalsIgnoreCase("Education Board")) {
				commodity = "Education Accessory";
			}
			}
			shipmentData.setCommodity(commodity);
			
			Double uscurrency = senderData.getValue()*audcurrency;
			shipmentData.setValue(Math.round(uscurrency*100.0)/100.0);
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
	public List<ShipmentDetails> downloadShipmentDataTemplate(String shipmentNumber, Integer userId) {
		// TODO Auto-generated method stub
		List<Integer> listOfClientId = d2zBrokerDao.getClientId(userId);
		List<SenderdataMaster> senderDataList = d2zDao.fetchShipmentData(shipmentNumber, listOfClientId);
		System.out.println(senderDataList.size() + " records");
		
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for (SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			if(senderData.getReference_number()!=null && senderData.getReference_number().length() > 21)
			{
				shipmentData.setReferenceNumber(senderData.getReference_number().substring(0, 21));
			}
			else
			{
				shipmentData.setReferenceNumber(senderData.getReference_number());
			}
			
			//shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19, 30));
			
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			if(senderData.getConsignee_addr1()!=null && senderData.getConsignee_addr1().length() > 49)
			{
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1().substring(0, 49));
			}
			else
			{
				shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			}
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			if(senderData.getConsignee_Postcode()!=null && senderData.getConsignee_Postcode().length() > 4)
			{
				shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode().substring(0, 4));
			}
			else
			{
				shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			}
			
			shipmentData.setDestination("AU");
			shipmentData.setCommodity(senderData.getProduct_Description());
			shipmentData.setCount("1");
			shipmentData.setLanding("1");
			shipmentData.setWeight(senderData.getWeight());
			
			shipmentData.setShipperName(senderData.getShipper_Name());
			shipmentData.setShipperAddress(senderData.getShipper_Addr1());
			shipmentData.setShipperCity(senderData.getShipper_City());
			shipmentData.setShipperState(senderData.getShipper_State());
			shipmentData.setShipperPostcode(senderData.getShipper_Postcode());
			shipmentData.setShipperCountry(senderData.getShipper_Country());
			shipmentData.setOrgin("HKG");
			shipmentData.setDest("SYD");
			shipmentData.setValue(senderData.getValue());
			shipmentData.setGoods("AUD");
			
			if( senderData.getValue() > 1000)
			{
				shipmentData.setSac("N");
			}
			else
			{
				shipmentData.setSac("Y");
			}
			shipmentData.setShipperContact(senderData.getAirwayBill());
			shipmentDetails.add(shipmentData);
		}
//		byte[] bytes = shipmentWriter.generateShipmentxls(shipmentDetails);
//		return bytes;
		return shipmentDetails;

	}

	@Override
	public List<ShipmentDetails> downloadShipmentDataTemplatebyType(List<String> number, Integer userId, String type) {
		// TODO Auto-generated method stub
		List<Integer> listOfClientId = d2zBrokerDao.getClientId(userId);
		List<SenderdataMaster> senderDataList = d2zDao.fetchShipmentDatabyType(number, listOfClientId,type);
		System.out.println(senderDataList.size() + " records");
		
		
		List<ShipmentDetails> shipmentDetails = new ArrayList<ShipmentDetails>();
		for (SenderdataMaster senderData : senderDataList) {
			ShipmentDetails shipmentData = new ShipmentDetails();
			if(senderData.getReference_number()!=null && senderData.getReference_number().length() > 21)
			{
				shipmentData.setReferenceNumber(senderData.getReference_number().substring(0, 21));
			}
			else
			{
				shipmentData.setReferenceNumber(senderData.getReference_number());
			}
			
			//shipmentData.setCon_no(senderData.getBarcodelabelNumber().substring(19, 30));
			
			shipmentData.setConsigneeName(senderData.getConsignee_name());
			if(senderData.getConsignee_addr1()!=null && senderData.getConsignee_addr1().length() > 49)
			{
			shipmentData.setConsigneeAddress(senderData.getConsignee_addr1().substring(0, 49));
			}
			else
			{
				shipmentData.setConsigneeAddress(senderData.getConsignee_addr1());
			}
			shipmentData.setConsigneeSuburb(senderData.getConsignee_Suburb());
			
			shipmentData.setConsigneeState(senderData.getConsignee_State());
			if(senderData.getConsignee_Postcode()!=null && senderData.getConsignee_Postcode().length() > 4)
			{
				shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode().substring(0, 4));
			}
			else
			{
				shipmentData.setConsigneePostcode(senderData.getConsignee_Postcode());
			}
			
			shipmentData.setDestination("AU");
			shipmentData.setCommodity(senderData.getProduct_Description());
			shipmentData.setCount("1");
			shipmentData.setLanding("1");
			shipmentData.setWeight(senderData.getWeight());
			
			shipmentData.setShipperName(senderData.getShipper_Name());
			shipmentData.setShipperAddress(senderData.getShipper_Addr1());
			shipmentData.setShipperCity(senderData.getShipper_City());
			shipmentData.setShipperState(senderData.getShipper_State());
			shipmentData.setShipperPostcode(senderData.getShipper_Postcode());
			shipmentData.setShipperCountry(senderData.getShipper_Country());
			shipmentData.setOrgin("HKG");
			shipmentData.setDest("SYD");
			shipmentData.setValue(senderData.getValue());
			shipmentData.setGoods("AUD");
			
			if( senderData.getValue() > 1000)
			{
				shipmentData.setSac("N");
			}
			else
			{
				shipmentData.setSac("Y");
			}
			shipmentData.setShipperContact(senderData.getAirwayBill());
			shipmentDetails.add(shipmentData);
		}
//		byte[] bytes = shipmentWriter.generateShipmentxls(shipmentDetails);
//		return bytes;
		return shipmentDetails;
	}

	@Override
	public UserMessage returnAction(List<ReturnsAction> returnsAction) {
		UserMessage usrMsg =  d2zDao.returnAction(returnsAction);
		return usrMsg;
	}

	@Override
	public List<TrackParcelResponse> trackParcels(List<String> articleIds) throws InterruptedException, ExecutionException {
		List<String> eTowerArticleIds = new ArrayList<String>();
		List<String> auPostArticleIds = new ArrayList<String>();
		List<String> pcaArticleIds = new ArrayList<String>();
		List<String> pflArticleIds = new ArrayList<String>();
		List<String> eParcelMlids = d2zDao.fetchMlidsBasedOnSupplier("eTower");
		List<String> auPostMlids =  d2zDao.fetchMlidsBasedOnSupplier("FDM");
	    List<String> pcaMlids = d2zDao.fetchMlidsBasedOnSupplier("PCA");
		CompletableFuture<TrackingEventResponse> eTowerResponse = new CompletableFuture<TrackingEventResponse>();
		CompletableFuture<TrackingResponse> auPostResponse = new CompletableFuture<TrackingResponse>();
		CompletableFuture<String> pcaResponse = new CompletableFuture<String>(); 
		CompletableFuture<List<PFLTrackingResponseDetails>> pflResponse  = new CompletableFuture<List<PFLTrackingResponseDetails>>();
		
		for(String articleId : articleIds) {
			if(articleId.length()==21 || articleId.length() == 23) {
				String mlid = articleId.length() == 23 ? articleId.substring(0,5) : articleId.substring(0,3);
				boolean isEParcel = eParcelMlids.stream().anyMatch(mlid::equalsIgnoreCase);
				boolean isAuPost = auPostMlids.stream().anyMatch(mlid::equalsIgnoreCase);
				boolean isPCA = pcaMlids.stream().anyMatch(mlid::equalsIgnoreCase);
				if(isEParcel) {
					eTowerArticleIds.add(articleId);
				}else if(isAuPost) {
					auPostArticleIds.add(articleId);
				}else if(isPCA) {
					pcaArticleIds.add(articleId);
				}
			}else if(articleId.startsWith("BN")) {
				pflArticleIds.add(articleId);
			}
			else {
				pcaArticleIds.add(articleId);
			}
		}
		
		if(eTowerArticleIds.size() > 0) {
			eTowerResponse = aysncService.makeCalltoEtower(eTowerArticleIds);
		}else {
			eTowerResponse.complete(null);
		}
		if(auPostArticleIds.size() > 0) {
			auPostResponse = aysncService.makeCalltoAuPost(auPostArticleIds);
		}else {
			auPostResponse.complete(null);
		}
		if(pcaArticleIds.size() > 0) {
			pcaResponse = aysncService.makeCalltoPCA(pcaArticleIds);
		}
		else {
			pcaResponse.complete(null);
		}
		if(pflArticleIds.size() > 0) {
				pflResponse = aysncService.makeCalltoPFL(pflArticleIds);
			}
		else {
			pflResponse.complete(null);
		}
		CompletableFuture.allOf(eTowerResponse, auPostResponse, pcaResponse,pflResponse).join();
		
		List<TrackParcelResponse> trackPracelsResponse = aggreateTrackParcelResponse(eTowerResponse.get(),auPostResponse.get(),pcaResponse.get(),pflResponse.get());
		return trackPracelsResponse;
	}

	private List<TrackParcelResponse> aggreateTrackParcelResponse(TrackingEventResponse eTowerResponse,
			TrackingResponse auPostResponse, String pcaResponse, List<PFLTrackingResponseDetails> pflResponse) {
			
		List<TrackParcelResponse> trackParcelResponse = new ArrayList<TrackParcelResponse>();
		if(null != eTowerResponse) {
			parseEtowerTrackingResponse(trackParcelResponse,eTowerResponse);	
		}
		if(null!=auPostResponse) {
			parseAuPostTrackingResponse(trackParcelResponse,auPostResponse);
		}
		if(null != pcaResponse) {
			parsePCAResponse(trackParcelResponse,pcaResponse);
		}
		if(null!=pflResponse) {
			parsePFLResponse(trackParcelResponse,pflResponse);
		}
		return trackParcelResponse;	
	}

	private void parsePFLResponse(List<TrackParcelResponse> trackParcelResponse,
			List<PFLTrackingResponseDetails> pflResponse) {
			if(!pflResponse.isEmpty()) {
				for(PFLTrackingResponseDetails response : pflResponse) {
					TrackParcelResponse parcelStatus = new TrackParcelResponse();
					parcelStatus.setArticleId(response.getBarcodeLabel());
					List<TrackingEvents> events = new ArrayList<TrackingEvents>();
					for(PFLTrackEvent trackEvent:response.getTrackEvent()) {
						TrackingEvents  event= new TrackingEvents();
						event.setTrackEventDateOccured(trackEvent.getDate());
						event.setEventDetails(trackEvent.getStatus());
						events.add(event);
					}
					
					parcelStatus.setTrackingEvents(events);
					trackParcelResponse.add(parcelStatus);

				}
			}
	}

	private void parsePCAResponse(List<TrackParcelResponse> trackParcelResponse, String pcaResponse) {
		// TODO Auto-generated method stub
		
	}

	private void parseAuPostTrackingResponse(List<TrackParcelResponse> trackParcelResponse,
			TrackingResponse auPostResponse) {
	    List<TrackingResults> trackingData = auPostResponse.getTracking_results();
		if(!trackingData.isEmpty()) {
			
			for(TrackingResults data : trackingData ) {
				if(data!=null && data.getTrackable_items()!=null) {
					for(TrackableItems trackingLabel : data.getTrackable_items()) {
						TrackParcelResponse parcelStatus = new TrackParcelResponse();
						List<TrackingEvents> events = new ArrayList<TrackingEvents>();

						parcelStatus.setArticleId(trackingLabel.getArticle_id());
						if(trackingLabel != null && trackingLabel.getEvents() != null) {
							for(com.d2z.d2zservice.model.auspost.TrackingEvents trackingEvents: trackingLabel.getEvents()) {
								TrackingEvents event = new TrackingEvents();
								event.setTrackEventDateOccured(trackingEvents.getDate());
								event.setEventDetails(trackingEvents.getDescription());
								events.add(event);
								}
							parcelStatus.setTrackingEvents(events);
							}
						trackParcelResponse.add(parcelStatus);
						}
				}
			}
		}
	}

	private void parseEtowerTrackingResponse(List<TrackParcelResponse> trackParcelResponse,
			TrackingEventResponse eTowerResponse) {
		List<TrackEventResponseData> responseData = eTowerResponse.getData();

		if (!responseData.isEmpty()) {

			for (TrackEventResponseData data : responseData) {

				if (data != null && data.getEvents() != null) {
					
					List<TrackingEvents> trackingEvents = new ArrayList<TrackingEvents>();
					TrackParcelResponse parcelStatus = new TrackParcelResponse();
					
					for (ETowerTrackingDetails trackingDetails : data.getEvents()) {
					
						parcelStatus.setArticleId(trackingDetails.getTrackingNo());
						
						TrackingEvents event = new TrackingEvents();
						event.setTrackEventDateOccured(trackingDetails.getEventTime());
						event.setEventDetails(trackingDetails.getActivity());
						trackingEvents.add(event);
						}
					
					parcelStatus.setTrackingEvents(trackingEvents);
					trackParcelResponse.add(parcelStatus);
					}
				}
	}
	}

	@Override
	public UserMessage enquiryFileUpload(Blob blob, String ticketNumber, String comments, String d2zComments, String sendUpdate,
			String status, String fileName) {
		UserMessage usrMsg = d2zDao.enquiryFileUpload(blob,ticketNumber,comments,d2zComments,sendUpdate,status,fileName);
		return usrMsg;
	}

}
