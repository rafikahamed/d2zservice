package com.d2z.d2zservice.serviceImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.IncomingJobsLogic;
import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.excelWriter.ShipmentDetailsWriter;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.AUWeight;
import com.d2z.d2zservice.model.AddShipmentModel;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerList;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.BrokerShipmentList;
import com.d2z.d2zservice.model.CreateEnquiryRequest;
import com.d2z.d2zservice.model.CreateJobRequest;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DownloadInvice;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.InvoiceShipment;
import com.d2z.d2zservice.model.NotBilled;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.PCADim;
import com.d2z.d2zservice.model.PFLTrackingResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.PflTrackEventRequest;
import com.d2z.d2zservice.model.ReconcileData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.ReturnsClientResponse;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.WeightUpload;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingEvents;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
import com.d2z.d2zservice.model.ExportDelete;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.proxy.AusPostProxy;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.d2zservice.proxy.PFLProxy;
import com.d2z.d2zservice.proxy.PcaProxy;
import com.d2z.d2zservice.repository.CSTicketsRepository;
import com.d2z.d2zservice.service.ISuperUserD2ZService;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.ETowerWrapper;
import com.d2z.d2zservice.wrapper.FreipostWrapper;
import com.d2z.d2zservice.wrapper.PCAWrapper;
import com.d2z.d2zservice.wrapper.PFLWrapper;

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
import uk.org.okapibarcode.backend.OkapiException;
import uk.org.okapibarcode.backend.Symbol;
import uk.org.okapibarcode.backend.DataMatrix.ForceMode;
import uk.org.okapibarcode.backend.Symbol.DataType;
import uk.org.okapibarcode.output.Java2DRenderer;
import com.d2z.d2zservice.model.ExportShipment;
import com.d2z.d2zservice.model.IncomingJobResponse;

@Service
public class SuperUserD2ZServiceImpl implements ISuperUserD2ZService {

	@Autowired
	private ID2ZSuperUserDao d2zDao;

	@Autowired
	ShipmentDetailsWriter shipmentWriter;

	@Autowired
	private ETowerProxy proxy;

	@Autowired
	private PcaProxy pcaproxy;
	
	@Autowired
	private PFLProxy pflProxy;

	@Autowired
	private D2ZValidator d2zValidator;
	
	@Autowired
	AusPostProxy ausPostProxy;

	@Autowired
	PFLWrapper pflWrapper;
	
	@Autowired
	PCAWrapper pcaWrapper;
	
	@Autowired
	FreipostWrapper freipostWrapper;

	@Autowired
	ETowerWrapper eTowerWrapper; 
	
	@Override
	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		UserMessage userMsg = new UserMessage();
		List<Trackandtrace> insertedData = d2zDao.uploadTrackingFile(fileData);
		if (insertedData.isEmpty()) {
			userMsg.setMessage("Failed to upload data");
			return userMsg;
		}
		userMsg.setMessage("Data uploaded successfully");
		return userMsg;
	}

	@Override
	public UserMessage uploadArrivalReport(List<ArrivalReportFileData> fileData) {
		UserMessage userMsg = new UserMessage();
		List<Trackandtrace> insertedData = d2zDao.uploadArrivalReport(fileData);
		if (insertedData.isEmpty()) {
			userMsg.setMessage("Failed to upload data");
			return userMsg;
		}
		userMsg.setMessage("Data uploaded successfully");
		return userMsg;
	}

	@Override
	public List<DropDownModel> brokerCompanyDetails() {
		List<String> listOfCompany = d2zDao.brokerCompanyDetails();
		List<DropDownModel> dropDownList = new ArrayList<DropDownModel>();
		for (String companyName : listOfCompany) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(companyName);
			dropDownVaL.setValue(companyName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}

	@Override
	public UserDetails fetchUserDetails(String companyName) {
		User user = d2zDao.fetchUserDetails(companyName);
		UserDetails userDetails = new UserDetails();
		userDetails.setAddress(user.getAddress());
		userDetails.setCompanyName(user.getCompanyName());
		userDetails.setContactName(user.getName());
		userDetails.setContactPhoneNumber(user.getPhoneNumber());
		userDetails.setCountry(user.getCountry());
		userDetails.setEmailAddress(user.getEmail());
		userDetails.setPassword(user.getPassword_value());
		userDetails.setPostCode(user.getPostcode());
		userDetails.setState(user.getState());
		userDetails.setSuburb(user.getSuburb());
		userDetails.setUserName(user.getUsername());
	/*
		Set<UserService> userServiceList = user.getUserService();
		List<String> serviceType = userServiceList.stream().map(obj -> {
			return obj.getServiceType();
		}).collect(Collectors.toList());*/
		
		List<String> serviceType = d2zDao.fetchServiceTypeByUserName(user.getUsername());
		userDetails.setServiceType(serviceType);
		return userDetails;
	}

	@Override
	public List<ExportDelete> exportDeteledConsignments(String fromDate, String toDate) {
		/* return d2zDao.exportDeteledConsignments(fromDate,toDate); */
		List<ExportDelete> exportdeletelist = new ArrayList<ExportDelete>();
		List<String> ExportDeleteList = d2zDao.exportDeteledConsignments(fromDate, toDate);
		Iterator itr = ExportDeleteList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			// now you have one array of Object for each row
			ExportDelete exportval = new ExportDelete();
			exportval.setBrokername(String.valueOf(obj[0]));
			exportval.setReference_number(String.valueOf(obj[1]));
			exportval.setBarcodelabelNumber(String.valueOf(obj[2]));
			exportval.setDat(String.valueOf(obj[3]).substring(0, 10));
			exportdeletelist.add(exportval);
		}
		// ExportDeleteList.forEach(System.out::println);
		return exportdeletelist;
		// byte[] bytes =
		// shipmentWriter.generateDeleteConsignmentsxls(deletedConsignments);
		// return bytes;
	}

	@Override
	public List<SenderdataMaster> exportConsignmentData(String fromDate, String toDate) {
		List<SenderdataMaster> output = d2zDao.exportConsignments(fromDate, toDate);
		output.forEach(senderdata -> senderdata.setTimestamp(senderdata.getTimestamp().substring(0,10)));
		return output;
	}
	
	@Override
	public List<ExportShipment> exportShipmentData(String fromDate, String toDate) {
		List<ExportShipment> exportshipmentlist = new ArrayList<ExportShipment>();
		List<Object> ExportDeleteList = d2zDao.exportShipment(fromDate, toDate);
		Iterator itr = ExportDeleteList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			// System.out.println(obj.length);
			// now you have one array of Object for each row
			ExportShipment exportval = new ExportShipment();
			exportval.setBroker_name(String.valueOf(obj[0]));
			exportval.setReference_number(String.valueOf(obj[1]));
			exportval.setValue(Double.valueOf("" + obj[2]));
			exportval.setShippedQuantity(Integer.valueOf("" + obj[3]));
			exportval.setConsignee_name(String.valueOf(obj[4]));
			exportval.setConsignee_addr1(String.valueOf(obj[5]));
			exportval.setConsignee_Suburb(String.valueOf(obj[6]));
			exportval.setConsignee_State(String.valueOf(obj[7]));
			exportval.setConsignee_Postcode(String.valueOf(obj[8]));
			exportval.setConsignee_Phone(String.valueOf(obj[9]));
			exportval.setProduct_Description(String.valueOf(obj[10]));
			exportval.setShipper_Country(String.valueOf(obj[11]));
			exportval.setWeight(Double.valueOf("" + obj[12]));
			exportval.setBarcodelabelNumber(String.valueOf(obj[13]));
			exportval.setServicetype(String.valueOf(obj[14]));
			exportval.setCurrency(String.valueOf(obj[15]));
			exportval.setArticleID(String.valueOf(obj[16]));
			exportval.setDat(String.valueOf(obj[17]).substring(0, 10));
			exportval.setManifest(String.valueOf(obj[18]));
			exportshipmentlist.add(exportval);
		}
		// ExportDeleteList.forEach(System.out::println);
		return exportshipmentlist;
	}

	@Override
	public List<ExportShipment> exportNonShipmentData(String fromDate, String toDate) {
		List<ExportShipment> exportshipmentlist = new ArrayList<ExportShipment>();
		List<Object> ExportDeleteList = d2zDao.exportNonShipment(fromDate, toDate);
		Iterator itr = ExportDeleteList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			// now you have one array of Object for each row
			ExportShipment exportval = new ExportShipment();
			exportval.setBroker_name(String.valueOf(obj[0]));
			exportval.setReference_number(String.valueOf(obj[1]));
			exportval.setValue(Double.valueOf("" + obj[2]));
			exportval.setShippedQuantity(Integer.valueOf("" + obj[3]));
			exportval.setConsignee_name(String.valueOf(obj[4]));
			exportval.setConsignee_addr1(String.valueOf(obj[5]));
			exportval.setConsignee_Suburb(String.valueOf(obj[6]));
			exportval.setConsignee_State(String.valueOf(obj[7]));
			exportval.setConsignee_Postcode(String.valueOf(obj[8]));
			exportval.setConsignee_Phone(String.valueOf(obj[9]));
			exportval.setProduct_Description(String.valueOf(obj[10]));
			exportval.setShipper_Country(String.valueOf(obj[11]));
			exportval.setWeight(Double.valueOf("" + obj[12]));
			exportval.setBarcodelabelNumber(String.valueOf(obj[13]));
			exportval.setServicetype(String.valueOf(obj[14]));
			exportval.setCurrency(String.valueOf(obj[15]));
			exportval.setDat(String.valueOf(obj[16]).substring(0, 10));
			exportshipmentlist.add(exportval);
		}
		// ExportDeleteList.forEach(System.out::println);
		return exportshipmentlist;
	}

	@Override
	public ResponseMessage trackingEvent(List<String> trackingNbrs,Map<String,String> map) {
		ResponseMessage respMsg = null;
		List<List<String>> trackingNbrList = ListUtils.partition(trackingNbrs, 300);
		int count = 1;
		for (List<String> trackingNumbers : trackingNbrList) {
			System.out.println(count + ":::" + trackingNumbers.size());
			count++;
			TrackingEventResponse response = proxy.makeCallForTrackingEvents(trackingNumbers);
			respMsg = d2zDao.insertTrackingDetails(response,map);
		}
		// List<List<ETowerTrackingDetails>> response = proxy.stubETower();
		return respMsg;
	}

	// @Scheduled(cron = "0 0 0/2 * * ?")
//	@Scheduled(cron = "0 0/10 * * * ?")
	@Override
	public void scheduledTrackingEvent() {
		Map<String,String> map = d2zDao.fetchTrackingNumbersForETowerCall();
		List<String> trackingNumbers = new ArrayList<>(map.keySet());
		if (trackingNumbers.isEmpty()) {
			System.out.println("ETower call not required");
		} else {
			trackingEvent(trackingNumbers,map);
		}
	}

	@Override
	public void scheduledPCATrackingEvent() {
		List<String> trackingNumbers = d2zDao.fetchTrackingNumbersForPCACall();
		if (trackingNumbers.isEmpty()) {
			System.out.println("PCA call not required");
		} else {
			// trackingEvent(trackingNumbers);
			pcaproxy.trackingEvent(trackingNumbers);
		}
	}

	@Override
	public UserMessage uploadBrokerRates(List<BrokerRatesData> brokerRatesData) {
		String uploadInvoiceData = d2zDao.uploadBrokerRates(brokerRatesData);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(uploadInvoiceData);
		return userMsg;
	}

	@Override
	public UserMessage uploadD2ZRates(List<D2ZRatesData> d2zRatesData) {
		String uploadedD2ZRates = d2zDao.uploadD2ZRates(d2zRatesData);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage(uploadedD2ZRates);
		return userMsg;
	}

	@Override
	public List<BrokerList> brokerList() {
		List<User> brokerList = d2zDao.brokerList();
		List<BrokerList> brokerListDetails = new ArrayList<BrokerList>();
		brokerList.forEach(users -> {
			BrokerList broker = new BrokerList();
			DropDownModel dropDownBroker = new DropDownModel();
			dropDownBroker.setName(users.getUsername());
			dropDownBroker.setValue(users.getUsername());
			broker.setBrokerUserName(dropDownBroker);
			broker.setUserId(users.getUser_Id());
			Set<DropDownModel> dropDownServiceList = new HashSet<DropDownModel>();
			Set<DropDownModel> dropDownTypeList = new HashSet<DropDownModel>();
			users.getUserService().forEach(service -> {
				if (service.getServiceType() != null) {
					DropDownModel dropDownService = new DropDownModel();
					dropDownService.setName(service.getServiceType());
					dropDownService.setValue(service.getServiceType());
					dropDownServiceList.add(dropDownService);
					broker.setServiceType(dropDownServiceList);
				}
				if (service.getInjectionType() != null) {
					DropDownModel dropDownType = new DropDownModel();
					dropDownType.setName(service.getInjectionType());
					dropDownType.setValue(service.getInjectionType());
					dropDownTypeList.add(dropDownType);
					broker.setInjectionType(dropDownTypeList);
				}
			});
			brokerListDetails.add(broker);
		});

		return brokerListDetails;
	}

	@Override
	public List<DropDownModel> fetchMlidList() {
		List<String> listOfMlid = d2zDao.fetchMlidList();
		List<DropDownModel> mlidList = new ArrayList<DropDownModel>();
		for (String mlid : listOfMlid) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(mlid);
			dropDownVaL.setValue(mlid);
			mlidList.add(dropDownVaL);
		}
		return mlidList;
	}

	@Override
	public List<BrokerShipmentList> brokerShipmentList() {
		List<User> brokerList = d2zDao.brokerList();
		List<BrokerShipmentList> brokerShipmentData = new ArrayList<BrokerShipmentList>();
		brokerList.forEach(broker -> {
			BrokerShipmentList brokerShipment = new BrokerShipmentList();
			DropDownModel dropDownBroker = new DropDownModel();
			dropDownBroker.setName(broker.getUsername());
			dropDownBroker.setValue(broker.getUsername());
			List<String> brokerShipmentList = d2zDao.brokerShipmentList(broker.getUser_Id());
			Set<DropDownModel> shipmentList = new HashSet<DropDownModel>();
			brokerShipmentList.forEach(shipment -> {
				if (null != shipment) {
					DropDownModel dropDownVaL = new DropDownModel();
					dropDownVaL.setName(shipment);
					dropDownVaL.setValue(shipment);
					shipmentList.add(dropDownVaL);
				}
			});
			brokerShipment.setBrokerUserName(dropDownBroker);
			brokerShipment.setUserId(broker.getUser_Id());
			brokerShipment.setShipmentNumber(shipmentList);
			brokerShipmentData.add(brokerShipment);
		});
		return brokerShipmentData;
	}

	@Override
	public List<InvoiceShipment> brokerShipment() {
		List<String> brokerShipmentList = d2zDao.brokerShipment();
		List<InvoiceShipment> invoiceShipmentList = new ArrayList<InvoiceShipment>();
		Iterator itr = brokerShipmentList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			InvoiceShipment invoiceShipment = new InvoiceShipment();
			if (obj[0] != null)
				invoiceShipment.setBrokerName(obj[0].toString());
			if (obj[1] != null)
				invoiceShipment.setShipmentNumber(obj[1].toString());
			invoiceShipmentList.add(invoiceShipment);
		}
		return invoiceShipmentList;
	}

	@Override
	public List<InvoiceShipment> brokerInvoiced() {
		List<String> brokerShipmentList = d2zDao.brokerInvoiced();
		List<InvoiceShipment> invoiceShipmentList = new ArrayList<InvoiceShipment>();
		Iterator itr = brokerShipmentList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			InvoiceShipment invoiceShipment = new InvoiceShipment();
			if (obj[0] != null)
				invoiceShipment.setBrokerName(obj[0].toString());
			if (obj[1] != null)
				invoiceShipment.setShipmentNumber(obj[1].toString());
			invoiceShipmentList.add(invoiceShipment);
		}
		return invoiceShipmentList;
	}

	@Override
	public UserMessage fetchReconcile(List<ReconcileData> reconcileData) throws ReferenceNumberNotUniqueException {
		List<Reconcile> reconcileCalculatedList = new ArrayList<Reconcile>();
		List<String> reconcileReferenceNum = new ArrayList<String>();
		// Check for duplicates
		if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("PFL")) {
			d2zValidator.isArticleIdUniqueReconcile(reconcileData, "D2Z");
		} else {
			d2zValidator.isArticleIdUniqueReconcile(reconcileData, "D2Z");
		}
		reconcileData.forEach(reconcile -> {
			List<String> reconcileList = d2zDao.reconcileData(reconcile.getArticleNo(), reconcile.getRefrenceNumber());
			Reconcile reconcileObj = new Reconcile();
			Iterator reconcileItr = reconcileList.iterator();
			while (reconcileItr.hasNext()) {
				Object[] obj = (Object[]) reconcileItr.next();
				MathContext mc = new MathContext(2);
				if (obj[0] != null)
					reconcileObj.setBrokerUserName(obj[0].toString());
				if (obj[1] != null)
					reconcileObj.setAirwaybill(obj[1].toString());
				if (obj[2] != null)
					reconcileObj.setArticleId(obj[2].toString());
				if (obj[3] != null)
					reconcileObj.setReference_number(obj[3].toString());
				if (obj[4] != null)
					reconcileObj.setD2ZCost(new BigDecimal(obj[4].toString()));
				if (obj[5] != null)
					reconcileObj.setD2ZWeight(Double.parseDouble(obj[5].toString()));
				if (obj[6] != null)
					reconcileObj.setInvoicedAmount(new BigDecimal(obj[6].toString()));
				if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("UBI")) {
					reconcileObj.setSupplierCharge(BigDecimal.valueOf(reconcile.getNormalRateParcel()));
					reconcileObj.setSupplierWeight(reconcile.getArticleActualWeight());
					reconcileObj.setWeightDifference((reconcileObj.getSupplierWeight()) - (reconcileObj.getD2ZWeight()));
				} else if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("freipost")) {
					reconcileObj.setSupplierCharge(BigDecimal.valueOf(reconcile.getCost()));
					reconcileObj.setSupplierWeight(reconcile.getChargedWeight());
					reconcileObj.setWeightDifference((reconcileObj.getSupplierWeight()) - (reconcileObj.getD2ZWeight()));
				} else if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("PFL")) {
					reconcileObj.setSupplierCharge(BigDecimal.valueOf(reconcile.getCost()));
					reconcileObj.setSupplierWeight(reconcile.getChargedWeight());
					reconcileObj.setWeightDifference((reconcileObj.getSupplierWeight()) - (reconcileObj.getD2ZWeight()));
				}
				if (reconcileObj.getSupplierCharge() != null && reconcileObj.getD2ZCost() != null) {
					reconcileObj.setCostDifference(reconcileObj.getSupplierCharge().subtract(reconcileObj.getD2ZCost(), mc));
					reconcileObj.setSupplierType(reconcileData.get(0).getSupplierType());
				}
					
			}
			reconcileCalculatedList.add(reconcileObj);
			if (reconcileObj.getBrokerUserName() != null) {
				reconcileReferenceNum.add(reconcileObj.getReference_number());
			} else {
				if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("UBI")) {
					reconcileObj.setArticleId(reconcile.getArticleNo());
				} else if (reconcileData.get(0).getSupplierType().equalsIgnoreCase("PFL")) {
					reconcileObj.setReference_number(reconcile.getRefrenceNumber());
				}
			}
			reconcileCalculatedList.add(reconcileObj);
		});
		d2zDao.reconcileUpdate(reconcileCalculatedList);
		if (!reconcileReferenceNum.isEmpty())
			d2zDao.reconcilerates(reconcileReferenceNum);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Reconcile data uploaded successfully, Please click download button to explore the data");
		return userMsg;
	}

	@Override
	public UserMessage approvedInvoice(com.d2z.d2zservice.model.ApprovedInvoice approvedInvoice) {
		// Calling Delete Store Procedure
		UserMessage approvedInvoiceMsg = d2zDao.approvedInvoice(approvedInvoice);
		return approvedInvoiceMsg;
	}

	@Override
	public List<NotBilled> fetchNotBilled() {
		List<String> notBilledData = d2zDao.fetchNotBilled();
		List<NotBilled> notBilledList = new ArrayList<NotBilled>();
		Iterator itr = notBilledData.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			NotBilled notBilled = new NotBilled();
			if (obj[0] != null)
				notBilled.setUserName(obj[0].toString());
			if (obj[1] != null)
				notBilled.setAirwayBill(obj[1].toString());
			if (obj[2] != null)
				notBilled.setArticleId(obj[2].toString());
			if (obj[3] != null)
				notBilled.setReferenceNumber(obj[3].toString());
			if (obj[4] != null)
				notBilled.setD2zRate(Double.parseDouble(obj[4].toString()));
			notBilledList.add(notBilled);
		}
		return notBilledList;
	}

	@Override
	public List<DownloadInvice> downloadInvoice(List<String> broker, List<String> airwayBill, String billed,String invoiced) {
		List<String> downloadInvoiceData = d2zDao.downloadInvoice(broker, airwayBill, billed, invoiced);
		List<DownloadInvice> downloadInvoiceList = new ArrayList<DownloadInvice>();
		Iterator itr = downloadInvoiceData.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			DownloadInvice downloadInvoice = new DownloadInvice();
			if (obj[0] != null)
				downloadInvoice.setTrackingNumber(obj[0].toString());
			if (obj[1] != null)
				downloadInvoice.setReferenceNuber(obj[1].toString());
			if (obj[2] != null)
				downloadInvoice.setPostcode(obj[2].toString());
			if (obj[3] != null)
				downloadInvoice.setWeight(obj[3].toString());
			if (obj[4] != null)
				downloadInvoice.setPostage(obj[4].toString());
			if (obj[5] != null)
				downloadInvoice.setFuelsurcharge(obj[5].toString());
			if (obj[6] != null)
				downloadInvoice.setTotal(obj[6].toString());
			if (obj[7] != null)
				downloadInvoice.setServiceType(obj[7].toString());
			if (obj[8] != null)
				downloadInvoice.setAirwaybill(obj[8].toString());
			downloadInvoiceList.add(downloadInvoice);
		}
		return downloadInvoiceList;
	}

	@Override
	public UserMessage fetchNonD2zClient(List<NonD2ZData> nonD2zData) throws ReferenceNumberNotUniqueException {
		d2zValidator.isArticleIdUniqueUI(nonD2zData);
		UserMessage uploadNonD2zClient = d2zDao.fetchNonD2zClient(nonD2zData);
		return uploadNonD2zClient;
	}

	@Override
	public List<Reconcile> downloadReconcile(List<String> reconcileNumbers) {
		List<Reconcile> reconcileData = d2zDao.downloadReconcile(reconcileNumbers);
		return reconcileData;
	}

	@Override
	public List<DropDownModel> fetchNonD2zBrokerUserName() {
		List<String> listOffBrokerName = d2zDao.fetchNonD2zBrokerUserName();
		List<DropDownModel> dropDownList = new ArrayList<DropDownModel>();
		for (String brokerName : listOffBrokerName) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(brokerName);
			dropDownVaL.setValue(brokerName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}

	@Override
	public UserMessage uploadReconcileNonD2z(List<ReconcileData> reconcileNonD2zData)
			throws ReferenceNumberNotUniqueException {
		List<ReconcileND> reconcileNoND2zList = new ArrayList<ReconcileND>();
		List<String> reconcileArticleIdNum = new ArrayList<String>();
		if (reconcileNonD2zData.get(0).getSupplierType().equalsIgnoreCase("freipost")) {
			d2zValidator.isReferenceNumberUniqueReconcile(reconcileNonD2zData, "ND2Z");
		} else {
			d2zValidator.isArticleIdUniqueReconcile(reconcileNonD2zData, "ND2Z");
		}
		reconcileNonD2zData.forEach(reconcileNonD2z -> {
			NonD2ZData reconcileObj = null;
			if (reconcileNonD2z.getSupplierType().equalsIgnoreCase("freipost")) {
				reconcileObj = d2zDao.reconcileNonD2zFreipostData(reconcileNonD2z.getRefrenceNumber());
			} else {
				reconcileObj = d2zDao.reconcileNonD2zData(reconcileNonD2z.getArticleNo());
			}
			ReconcileND reconcileNonD2Obj = new ReconcileND();
			MathContext mc = new MathContext(2);
			if (reconcileObj != null) {
				reconcileNonD2Obj.setBrokerUserName(reconcileObj.getBrokerName());
				reconcileNonD2Obj.setAirwaybill(reconcileObj.getShipmentNumber());
				reconcileNonD2Obj.setArticleId(reconcileObj.getArticleId());
				reconcileNonD2Obj.setReference_number(reconcileObj.getReference_number());
				reconcileNonD2Obj.setSupplierCharge(BigDecimal.valueOf(reconcileNonD2z.getCost()));
				if (reconcileObj.getD2Zrate() != null)
					reconcileNonD2Obj.setD2ZCost(new BigDecimal(reconcileObj.getD2Zrate()));
				if (reconcileNonD2Obj.getSupplierCharge() != null && reconcileNonD2Obj.getD2ZCost() != null)
					reconcileNonD2Obj.setCostDifference(
							reconcileNonD2Obj.getSupplierCharge().subtract(reconcileNonD2Obj.getD2ZCost(), mc));
				reconcileNonD2Obj.setSupplierWeight(reconcileNonD2z.getChargedWeight());
				reconcileNonD2Obj.setD2ZWeight(reconcileObj.getWeight());
				reconcileNonD2Obj.setWeightDifference(
						(reconcileNonD2Obj.getSupplierWeight()) - (reconcileNonD2Obj.getD2ZWeight()));
				if (reconcileObj.getBrokerRate() != null)
					reconcileNonD2Obj.setInvoicedAmount(new BigDecimal(reconcileObj.getBrokerRate()));
				reconcileNonD2Obj.setSupplierType(reconcileNonD2z.getSupplierType());
				reconcileArticleIdNum.add(reconcileObj.getArticleId());
			} else {
				if (reconcileNonD2z.getSupplierType().equalsIgnoreCase("freipost")) {
					reconcileNonD2Obj.setReference_number(reconcileNonD2z.getRefrenceNumber());
				} else {
					reconcileNonD2Obj.setArticleId(reconcileNonD2z.getArticleNo());
				}
			}
			reconcileNoND2zList.add(reconcileNonD2Obj);
		});
		d2zDao.reconcileNonD2zUpdate(reconcileNoND2zList);
		if (!reconcileArticleIdNum.isEmpty())
			d2zDao.reconcileratesND(reconcileArticleIdNum);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Reconcile data uploaded successfully, Please click download button to explore the data");
		return userMsg;
	}

	@Override
	public List<ReconcileND> downloadNonD2zReconcile(List<String> nonD2zReconcileNumbers) {
		List<ReconcileND> nonD2zReconcileData = d2zDao.downloadNonD2zReconcile(nonD2zReconcileNumbers);
		return nonD2zReconcileData;
	}

	@Override
	public List<InvoiceShipment> brokerNonD2zShipment() {
		List<NonD2ZData> pendingNonD2zInvoice = d2zDao.brokerNonD2zShipment();
		List<InvoiceShipment> invoiceShipmentList = new ArrayList<InvoiceShipment>();
		Iterator itr = pendingNonD2zInvoice.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			InvoiceShipment invoiceShipment = new InvoiceShipment();
			if (obj[0] != null)
				invoiceShipment.setBrokerName(obj[0].toString());
			if (obj[1] != null)
				invoiceShipment.setShipmentNumber(obj[1].toString());
			invoiceShipmentList.add(invoiceShipment);
		}
		return invoiceShipmentList;
	}

	@Override
	public List<DownloadInvice> downloadNonD2zInvoice(List<String> broker, List<String> airwayBill, String billed,
			String invoiced) {
		List<String> downloadNonD2zInvoiceData = d2zDao.downloadNonD2zInvoice(broker, airwayBill, billed, invoiced);
		List<DownloadInvice> downloadNonD2zInvoiceList = new ArrayList<DownloadInvice>();
		Iterator itr = downloadNonD2zInvoiceData.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			DownloadInvice downloadInvoice = new DownloadInvice();
			if (obj[0] != null)
				downloadInvoice.setTrackingNumber(obj[0].toString());
			if (obj[1] != null)
				downloadInvoice.setReferenceNuber(obj[1].toString());
			if (obj[2] != null)
				downloadInvoice.setPostcode(obj[2].toString());
			if (obj[3] != null)
				downloadInvoice.setWeight(obj[3].toString());
			if (obj[4] != null)
				downloadInvoice.setPostage(obj[4].toString());
			if (obj[5] != null)
				downloadInvoice.setFuelsurcharge(obj[5].toString());
			if (obj[6] != null)
				downloadInvoice.setTotal(obj[6].toString());
			if (obj[7] != null)
				downloadInvoice.setServiceType(obj[7].toString());
			downloadNonD2zInvoiceList.add(downloadInvoice);
		}
		return downloadNonD2zInvoiceList;
	}

	@Override
	public UserMessage approveNdInvoiced(ApprovedInvoice approvedInvoice) {
		// Calling Delete Store Procedure
		UserMessage approvedInvoiceMsg = d2zDao.approveNdInvoiced(approvedInvoice);
		return approvedInvoiceMsg;
	}

	@Override
	public List<InvoiceShipment> brokerNdInvoiced() {
		List<NonD2ZData> approvedNonD2zInvoice = d2zDao.brokerNdInvoiced();
		List<InvoiceShipment> approvedShipmentList = new ArrayList<InvoiceShipment>();
		Iterator itr = approvedNonD2zInvoice.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			InvoiceShipment invoiceShipment = new InvoiceShipment();
			if (obj[0] != null)
				invoiceShipment.setBrokerName(obj[0].toString());
			if (obj[1] != null)
				invoiceShipment.setShipmentNumber(obj[1].toString());
			approvedShipmentList.add(invoiceShipment);
		}
		return approvedShipmentList;
	}

	@Override
	public List<NotBilled> fetchNonD2zNotBilled() {
		List<String> notBilledData = d2zDao.fetchNonD2zNotBilled();
		List<NotBilled> notBilledList = new ArrayList<NotBilled>();
		Iterator itr = notBilledData.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			NotBilled notBilled = new NotBilled();
			if (obj[0] != null)
				notBilled.setUserName(obj[0].toString());
			if (obj[1] != null)
				notBilled.setAirwayBill(obj[1].toString());
			if (obj[2] != null)
				notBilled.setArticleId(obj[2].toString());
			if (obj[3] != null)
				notBilled.setReferenceNumber(obj[3].toString());
			if (obj[4] != null)
				notBilled.setD2zRate(Double.parseDouble(obj[4].toString()));
			notBilledList.add(notBilled);
		}
		return notBilledList;
	}

	@Override
	public List<?> fetchApiLogs(String client, String fromDate, String toDate) {
		List<String> apiname = new ArrayList<String>();
		if(client.equalsIgnoreCase("etower")) {
			apiname.add("Create Shipping Order");
			apiname.add("Forecast");
			List<ETowerResponse> etowerResponse = d2zDao.fetchEtowerLogResponseApi(apiname,fromDate,toDate);
			return etowerResponse;
		}else if(client.equalsIgnoreCase("auPost")) {
			List<AUPostResponse> auPostResponse = d2zDao.fetchAUPosLogtResponse(fromDate,toDate);
			return auPostResponse;
		}else if(client.equalsIgnoreCase("pfl")) {
			apiname.add("PFL - Create order");
			apiname.add("PFL - Submit order");
			List<ETowerResponse> etowerResponse = d2zDao.fetchEtowerLogResponseApi(apiname,fromDate,toDate);
			return etowerResponse;
		}else if(client.equalsIgnoreCase("nex")) {
			System.out.println("in nex");
			apiname.add("NEX - Create order");
			
			List<ETowerResponse> etowerResponse = d2zDao.fetchEtowerLogResponseApi(apiname,fromDate,toDate);
			return etowerResponse;
		}
		return null;
	}

	@Override
	public byte[] trackingLabel(List<String> refBarNumArray) {

		List<SenderData> trackingLabelList = new ArrayList<SenderData>();
		List<String> trackingLabelData = d2zDao.trackingLabel(refBarNumArray);
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
			boolean setGS1DataType = false;
			if(trackingLabel.getCarrier().equalsIgnoreCase("Express") || trackingLabel.getCarrier().equalsIgnoreCase("eParcel")) {
				setGS1DataType = true;
			}
					trackingLabel.setDatamatrixImage(generateDataMatrix(trackingLabel.getDatamatrix(),setGS1DataType));
					String user = d2zDao.fetchUserById(Integer.parseInt(trackingArray[27].toString()));
					if("VELC".equalsIgnoreCase(user)) {
						trackingLabel.setLabelSenderName("SW4");
					}
			trackingLabelList.add(trackingLabel);
		}

		/*
		 * JRBeanCollectionDataSource beanColDataSource = new
		 * JRBeanCollectionDataSource(trackingLabelList); Map<String,Object> parameters
		 * = new HashMap<>(); byte[] bytes = null; //Blob blob = null; JasperReport
		 * jasperReport = null; try (ByteArrayOutputStream byteArray = new
		 * ByteArrayOutputStream()) { jasperReport =
		 * JasperCompileManager.compileReport(getClass().getResource(
		 * "/eparcelLabel.jrxml").openStream()); JRSaver.saveObject(jasperReport,
		 * "label.jasper"); JasperPrint jasperPrint =
		 * JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource); //
		 * return the PDF in bytes bytes =
		 * JasperExportManager.exportReportToPdf(jasperPrint); // blob = new
		 * javax.sql.rowset.serial.SerialBlob(bytes); } catch (JRException | IOException
		 * e) { e.printStackTrace(); } return bytes;
		 */
		List<SenderData> eParcelData = new ArrayList<SenderData>();

		List<SenderData> expressData = new ArrayList<SenderData>();
		List<SenderData> fastwayData = new ArrayList<SenderData>();
		List<SenderData> fastway_S_Data = new ArrayList<SenderData>();

		for (SenderData data : trackingLabelList) {
			if (data.getCarrier().equalsIgnoreCase("eParcel")) {
				eParcelData.add(data);
			} else if (data.getCarrier().equalsIgnoreCase("Express")) {
				expressData.add(data);
			}else if(data.getCarrier().equalsIgnoreCase("FastwayM")) {
				fastwayData.add(data);
			}
			else if(data.getCarrier().equalsIgnoreCase("FastwayS")) {
				fastway_S_Data.add(data);
			}
		}

		Map<String, Object> parameters = new HashMap<>();
		byte[] bytes = null;
		// Blob blob = null;
		JRBeanCollectionDataSource eParcelDataSource;
		JRBeanCollectionDataSource expressDataSource;
		JasperReport eParcelLabel = null;
		JasperReport expressLabel = null;
		JRBeanCollectionDataSource fastwayDataSource;
		JasperReport fastwayLabel = null;
		JRBeanCollectionDataSource fastway_S_DataSource;
		JasperReport fastway_S_Label = null;
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
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			exporter.setExporterOutput(exporterOutput);
			exporter.exportReport();
			// return the PDF in bytes
			bytes = outputStream.toByteArray();
			/*
			 * try(OutputStream out = new FileOutputStream("Label.pdf")){ out.write(bytes);
			 * }
			 */
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
	public List<Mlid> downloadMlid(String service) {
		return d2zDao.downloadMlid(service);
	}

	@Override
	public UserMessage uploadMlid(List<Object> MlidData) {
		return d2zDao.addMlid(MlidData);
	}

	@Override
	public UserMessage deleteMLID(String service){
		UserMessage approvedInvoiceMsg = d2zDao.deleteMlid(service);
		return approvedInvoiceMsg;
	}
	
	@Override
	public List<DropDownModel> fetchMlidDeleteList() {
		List<String> listOfMlid = d2zDao.fetchMlidDeleteList();
		List<DropDownModel> mlidList = new ArrayList<DropDownModel>();
		for (String mlid : listOfMlid) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(mlid);
			dropDownVaL.setValue(mlid);
			mlidList.add(dropDownVaL);
		}
		return mlidList;
	}
	
	@Override
	public List<AUWeight> downloadAUWeight(List<Object> AUWeight) {
		return d2zDao.downloadAUweight(AUWeight);
	}
	
	@Override
	public List<OpenEnquiryResponse> fetchOpenEnquiryDetails() {
		return d2zDao.fetchOpenEnquiryDetails();
	}

	@Override
	public UserMessage updateEnquiryDetails(List<OpenEnquiryResponse> openEnquiryDetails) {
		UserMessage usrMsg = new UserMessage();
		String message = d2zDao.updateEnquiryDetails(openEnquiryDetails);
		usrMsg.setMessage("Enquiry Updated Successfully");
		return usrMsg;
	}

	@Override
	public List<OpenEnquiryResponse> completedEnquiryDetails() {
		List<OpenEnquiryResponse> enquiryListDetails = new ArrayList<OpenEnquiryResponse>();
		List<CSTickets> csTickets = d2zDao.completedEnquiryDetails();
		for(CSTickets csTicketDetails:csTickets) {
			OpenEnquiryResponse enquiryData = new OpenEnquiryResponse();
			enquiryData.setTicketNumber(csTicketDetails.getTicketID());
			enquiryData.setArticleID(csTicketDetails.getArticleID());
			enquiryData.setReferenceNumber(csTicketDetails.getReferenceNumber());
			enquiryData.setDeliveryEnquiry(csTicketDetails.getDeliveryEnquiry());
			enquiryData.setPod(csTicketDetails.getPod());
			enquiryData.setComments(csTicketDetails.getComments());
			enquiryData.setConsigneeName(csTicketDetails.getConsigneeName());
			enquiryData.setD2zComments(csTicketDetails.getD2zComments());
			enquiryData.setTrackingStatus(csTicketDetails.getTrackingStatus());
			enquiryData.setTrackingEvent(csTicketDetails.getTrackingEvent());
			enquiryData.setTrackingEventDateOccured(csTicketDetails.getTrackingEventDateOccured().toString());
			enquiryListDetails.add(enquiryData);
		}
		return enquiryListDetails;
	}

	@Override

	public List<AddShipmentModel> incomingjobList() {
		// TODO Auto-generated method stub
		List<IncomingJobsLogic> job = d2zDao.getBrokerMlidDetails();
		
		List<AddShipmentModel> incomingjob = new ArrayList<AddShipmentModel>();
	 	Set<String> BrokerName = job.stream().map(obj -> {
			return obj.getBroker(); })
				.collect(Collectors.toSet());
	 	Set<String> consignee ;
		for(String s:BrokerName)
		{AddShipmentModel data1 = new AddShipmentModel();
			DropDownModel DropDownBroker = new DropDownModel();
			DropDownBroker.setName(s);
			DropDownBroker.setValue(s);
			List<DropDownModel> incomingmlid = new ArrayList<DropDownModel>();
			List<DropDownModel> incomingconsign = new ArrayList<DropDownModel>();
			for(IncomingJobsLogic j :job)
			{
				if(j.getBroker().equals(s))
				{
				DropDownModel DropDownMlid = new DropDownModel();
				DropDownMlid.setName(j.getMLID());
				DropDownMlid.setValue(j.getMLID());
				DropDownModel DropDownCon = new DropDownModel();
				DropDownCon.setName(j.getConsignee());
				DropDownCon.setValue(j.getConsignee());
				incomingmlid.add(DropDownMlid);
				incomingconsign.add(DropDownCon);
				}
				
			}
			data1.setBrokerName(DropDownBroker);
			data1.setConsignee(incomingconsign);
			data1.setMlid(incomingmlid);
			incomingjob.add(data1);
		}
		
		
				return incomingjob;
	}

	@Override
	public UserMessage createJob(List<CreateJobRequest> createJob) {
		
			String jobInfo = d2zDao.createEnquiry(createJob);
			UserMessage usrMsg = new UserMessage();
			usrMsg.setMessage(jobInfo);
			return usrMsg;
		}

	@Override
	public List<IncomingJobResponse> getJobList() {
		// TODO Auto-generated method stub
	return d2zDao.getJobList();
	}
	

	@Override
	public List<IncomingJobResponse> getcloseJobList() {
		// TODO Auto-generated method stub
		return d2zDao.getClosedJobList();
	}

	
	@Override
	public ReturnsClientResponse fetchClientDetails(String referenceNumber, String barcodeLabel, String articleId) {
		List<String> clientDetails = d2zDao.fetchClientDetails(referenceNumber,barcodeLabel,articleId);
		ReturnsClientResponse clientResponse = new ReturnsClientResponse();
		if( null != clientDetails ) {
			Iterator itr = clientDetails.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				clientResponse.setClientName(obj[0].toString());
				clientResponse.setBrokerName(obj[1].toString());
				clientResponse.setConsigneeName(obj[2].toString());
				clientResponse.setRoleId(Integer.parseInt(obj[3].toString()));
				clientResponse.setUserId(Integer.parseInt(obj[4].toString()));
				clientResponse.setClientBrokerId(Integer.parseInt(obj[5].toString()));
				clientResponse.setCarrier(obj[6].toString());
				clientResponse.setReferenceNumber(obj[7].toString());
				clientResponse.setBarcodelabelNumber(obj[8].toString());
				clientResponse.setArticleId(obj[9].toString());
			}
		}
		return clientResponse;
	}

	@Override
	public UserMessage createReturns(List<Returns> returns) {
		List<Returns> returnsList = new ArrayList<Returns>();
		for(Returns returnVal: returns) {
			Returns returnData = new Returns();
			returnData.setScanType(returnVal.getScanType());
			returnData.setArticleId(returnVal.getArticleId());
			returnData.setBarcodelabelNumber(returnVal.getBarcodelabelNumber());
			returnData.setReferenceNumber(returnVal.getReferenceNumber());
			returnData.setReturnReason(returnVal.getReturnReason());
			returnData.setBrokerName(returnVal.getBrokerName());
			returnData.setClientName(returnVal.getClientName());
			returnData.setConsigneeName(returnVal.getConsigneeName());
			returnData.setRate(12.0);
			returnData.setReturnsCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			returnData.setUserId(returnVal.getUserId());
			returnData.setClientBrokerId(returnVal.getClientBrokerId());
			returnData.setCarrier(returnVal.getCarrier());
			returnsList.add(returnData);
		}
		String clientDetails = d2zDao.createReturns(returnsList);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage(clientDetails);
		return usrMsg;
	}

	@Override
	public UserMessage updateJob(List<IncomingJobResponse> job) {
		// TODO Auto-generated method stub
		
		String jobInfo = d2zDao.updateJob(job);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage(jobInfo);
		return usrMsg;

	}

	@Override
	public List<DropDownModel> fetchReturnsBroker() {
		List<String> returnsBroker = d2zDao.fetchReturnsBroker();
		List<DropDownModel> brokerList = new ArrayList<DropDownModel>();
		for (String mlid : returnsBroker) {
			if(null != mlid) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(mlid);
				dropDownVaL.setValue(mlid);
				brokerList.add(dropDownVaL);
			}
		}
		return brokerList;
	}

	@Override
	public List<Returns> returnsOutstanding(String fromDate, String toDate, String brokerName) {
		return  d2zDao.returnsOutstanding(fromDate,toDate,brokerName);
	}

	@Override
	public UserMessage deleteJob(List<IncomingJobResponse> job) {
		// TODO Auto-generated method stub
		String jobInfo = d2zDao.deleteJob(job);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage(jobInfo);
		return usrMsg;

	}

	@Override
	public List<SenderdataMaster> exportConsignmentDatafile(String type, List<String> Data) {
		// TODO Auto-generated method stub
		List<SenderdataMaster> output = d2zDao.exportConsignmentsfile(type, Data);
		output.forEach(senderdata -> senderdata.setTimestamp(senderdata.getTimestamp().substring(0,10)));
		return output;
	
	}

	@Override
	public List<ExportShipment> exportShipmentDatafile(String type, List<String> Data) {
		// TODO Auto-generated method stub
		List<ExportShipment> exportshipmentlist = new ArrayList<ExportShipment>();
		List<Object> ExportDeleteList = d2zDao.exportShipmentfile(type, Data);
		Iterator itr = ExportDeleteList.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			// System.out.println(obj.length);
			// now you have one array of Object for each row
			ExportShipment exportval = new ExportShipment();
			exportval.setBroker_name(String.valueOf(obj[0]));
			exportval.setReference_number(String.valueOf(obj[1]));
			exportval.setValue(Double.valueOf("" + obj[2]));
			exportval.setShippedQuantity(Integer.valueOf("" + obj[3]));
			exportval.setConsignee_name(String.valueOf(obj[4]));
			exportval.setConsignee_addr1(String.valueOf(obj[5]));
			exportval.setConsignee_Suburb(String.valueOf(obj[6]));
			exportval.setConsignee_State(String.valueOf(obj[7]));
			exportval.setConsignee_Postcode(String.valueOf(obj[8]));
			exportval.setConsignee_Phone(String.valueOf(obj[9]));
			exportval.setProduct_Description(String.valueOf(obj[10]));
			exportval.setShipper_Country(String.valueOf(obj[11]));
			exportval.setWeight(Double.valueOf("" + obj[12]));
			exportval.setBarcodelabelNumber(String.valueOf(obj[13]));
			exportval.setServicetype(String.valueOf(obj[14]));
			exportval.setCurrency(String.valueOf(obj[15]));
			exportval.setArticleID(String.valueOf(obj[16]));
			exportval.setDat(String.valueOf(obj[17]).substring(0, 10));
			exportval.setManifest(String.valueOf(obj[18]));
			exportshipmentlist.add(exportval);
		}
		// ExportDeleteList.forEach(System.out::println);
		return exportshipmentlist;
	}

	@Override
	public void triggerSC() {
		List<CSTickets> csTickets = d2zDao.fetchCSTickets();
		List<String> pcaList = new ArrayList<String>();
		List<String> pflList = new ArrayList<String>();
		List<String> etowerList = new ArrayList<String>();
		List<String> auPostList = new ArrayList<String>();
		if(csTickets != null) {
			for(CSTickets csTicketDetails:csTickets) {
				if(csTicketDetails.getCarrier().equalsIgnoreCase("FastwayS") || csTicketDetails.getCarrier().equalsIgnoreCase("StarTrack")) {
					if(csTicketDetails.getArticleID() != null && csTicketDetails.getBarcodelabelNumber() != null) {
						//pcaList.add(d2zDao.fetchBarcodeLabel(csTicketDetails.getArticleID()));
						pcaList.add(csTicketDetails.getBarcodelabelNumber());
					}
				}else if(csTicketDetails.getCarrier().equalsIgnoreCase("FastwayM")) {
					if(csTicketDetails.getArticleID() != null && csTicketDetails.getBarcodelabelNumber() != null) {
						//pflList.add(d2zDao.fetchBarcodeLabel(csTicketDetails.getArticleID()));
						pflList.add(csTicketDetails.getBarcodelabelNumber());
					}
				}else if(csTicketDetails.getCarrier().equalsIgnoreCase("Express")) {
					if(csTicketDetails.getArticleID() != null)
						etowerList.add(csTicketDetails.getArticleID());
				}else if(csTicketDetails.getCarrier().equalsIgnoreCase("eParcel")) {
					if("33G7K".contains(csTicketDetails.getArticleID().substring(0,5))|| "33G7L".contains(csTicketDetails.getArticleID().substring(0,5)) ||
						"33G7M".contains(csTicketDetails.getArticleID().substring(0,5)) || "33G7N".contains(csTicketDetails.getArticleID().substring(0,5)) ||
						"33G7P".contains(csTicketDetails.getArticleID().substring(0,5)) || "SJU".contains(csTicketDetails.getArticleID().substring(0,3)) ||
						"ZK6".contains(csTicketDetails.getArticleID().substring(0,3))){
						if(csTicketDetails.getArticleID() != null)
							etowerList.add(csTicketDetails.getArticleID());
					}
					if("33PE9".contains(csTicketDetails.getArticleID().substring(0,5)) || "33PET".contains(csTicketDetails.getArticleID().substring(0,5)) ||
							"33PEN".contains(csTicketDetails.getArticleID().substring(0,5)) || "33PEH".contains(csTicketDetails.getArticleID().substring(0,5))) {
						if(csTicketDetails.getArticleID() != null)
							auPostList.add(csTicketDetails.getArticleID());
					}
					if("AMQ".contains(csTicketDetails.getArticleID().substring(0,3))) {
						if(csTicketDetails.getArticleID() != null)
							pcaList.add(csTicketDetails.getArticleID());
					}
				}
			}
		}
		if(pcaList.size() > 0 ) {
			System.out.println("PCA List-->");
			System.out.println(pcaList.toString());	
			pcaproxy.trackingEvent(pcaList);
		}
		if(pflList.size() > 0 ) {
			System.out.println("PFL List-->");
			System.out.println(pflList.toString());
			List<PFLTrackingResponseDetails> pflTrackingDetails = new ArrayList<PFLTrackingResponseDetails>();
			for(String pflValue:pflList) {
				PflTrackEventRequest pflTrackEvent = new PflTrackEventRequest();
				PFLTrackingResponseDetails pflResp = new PFLTrackingResponseDetails();
				pflTrackEvent.setTracking_number(pflValue);
				PFLTrackingResponse pflTrackResp = pflProxy.trackingEvent(pflTrackEvent);
				if(pflTrackResp.getResult() != null) {
					pflResp.setBarcodeLabel(pflTrackEvent.getTracking_number());
					pflResp.setStatus(pflTrackResp.getResult().get(0).getStatus());
					pflResp.setStatus_code(pflTrackResp.getResult().get(0).getStatus_code());
					pflResp.setDate(pflTrackResp.getResult().get(0).getDate());
					pflTrackingDetails.add(pflResp);
				}
			}
			System.out.println(pflTrackingDetails);
			ResponseMessage respMsg = null;
			if(pflTrackingDetails.size() > 0) 
				respMsg = d2zDao.updatePFLTrackingDetails(pflTrackingDetails);
			else 
				System.out.println("No Response from PFL for the giventracking number");
		}
		if(etowerList.size() > 0 ) {
			System.out.println("Etower List");
			System.out.println(etowerList.toString());
			eTowerTrackingEvent(etowerList);
		}
		if(auPostList.size() > 0 ) {
			System.out.println("AUPost List");
			System.out.println(auPostList.toString());
			auTrackingEvent(auPostList);
		}
	}
	
	public ResponseMessage auTrackingEvent(List<String> auPostList) {
		ResponseMessage respMsg = null;
		List<List<String>> articleIdList = ListUtils.partition(auPostList, 10);
		int count = 1;
		for (List<String> articleIdNumbers : articleIdList) {
			System.out.println(count + ":::" + articleIdNumbers.size());
			count++;
			String articleIds = StringUtils.join(articleIdNumbers, ", ");
			TrackingResponse auTrackingDetails = ausPostProxy.trackingEvent(articleIds);
			System.out.println("AU Track Response");
			System.out.println(auTrackingDetails.toString());
			respMsg = d2zDao.updateAUCSTrackingDetails(auTrackingDetails);
			System.out.print("timestamp:"+LocalDateTime.now());
			try {
				if(count > 10){
					Thread.sleep(60000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.print("After thread timestamp:"+LocalDateTime.now());
		}
		return respMsg;
	}
	
	
	public ResponseMessage eTowerTrackingEvent(List<String> trackingNbrs) {
		ResponseMessage respMsg = null;
		List<List<String>> trackingNbrList = ListUtils.partition(trackingNbrs, 300);
		int count = 1;
		for (List<String> trackingNumbers : trackingNbrList) {
			System.out.println(count + ":::" + trackingNumbers.size());
			count++;
			TrackingEventResponse response = proxy.makeCallForTrackingEvents(trackingNumbers);
			respMsg = d2zDao.updateAUEtowerTrackingDetails(response);
		}
		return respMsg;
	}
	
	public UserMessage submitJob(List<IncomingJobResponse> job) {
		// TODO Auto-generated method stub
		
		String jobInfo = d2zDao.submitJob(job);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage(jobInfo);
		return usrMsg;
		
	}

	@Override
	public List<Returns> returnsOutstanding() {
		return d2zDao.returnsOutstanding();
	}

	@Override
	public UserMessage updateAction(List<ReturnsAction> returnsAction) {
		return  d2zDao.updateAction(returnsAction);
	}

	@Override
	public UserMessage uploadWeight(List<WeightUpload> weight) {
		// TODO Auto-generated method stub
		return d2zDao.uploadweight(weight);
	}

	@Override
	public ResponseMessage allocateShipment(String articleid, String shipmentNumber) {
		// TODO Auto-generated method stub
String[] articleNbrs = articleid.split(",");
		
		
		
		ResponseMessage userMsg = new ResponseMessage();
		/* String[] refNbrs = referenceNumbers.split(",");
		List<String> refNumbers = Arrays.asList(refNbrs);*/
		List<String> refNumbers = d2zDao.fetchRefnobyArticle(Arrays.asList(articleNbrs));
	System.out.println("refno"+refNumbers.size());
		List<SenderdataMaster> consignments = d2zDao.fetchConsignmentsByRefNbr(refNumbers);
		if(consignments.isEmpty()) {
			userMsg.setResponseMessage("Invalid Article id");
		}
		String referenceNumbers =refNumbers.stream()
                .collect(Collectors.joining(","));
		
		List<String> allocateddata = consignments.stream().filter(obj -> null!=obj.getAirwayBill())
				.map(a -> {
			
				String msg = a.getReference_number();
			
			/*if (a.getIsDeleted().equalsIgnoreCase("Y")) {
				msg = new StringBuffer(a.getReference_number());
				msg.append(" : Consignment already deleted");
			}*/
			return msg;
		}).collect(Collectors.toList());
		/*if (!invalidData.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Request failed", invalidData);
		}*/
		List<String> toAllocate = refNumbers;
		toAllocate.removeAll(allocateddata);
		
		String[] refNbrs = toAllocate.stream().toArray(String[] ::new);
		
		
      
		  d2zDao.allocateShipment(referenceNumbers, shipmentNumber);
		
		  String msg  = d2zDao.updateinvoicing(articleid,shipmentNumber);
		userMsg.setResponseMessage(msg);
		 
		Runnable freipost = new Runnable( ) {			
	        public void run() {
	        	String[] refNbrArray = referenceNumbers.split(",");
	        	List<SenderdataMaster> senderMasterData = d2zDao.fetchDataBasedonSupplier(Arrays.asList(refNbrArray),"Freipost");
	        	if(!senderMasterData.isEmpty()) {
	        		freipostWrapper.uploadManifestService(senderMasterData);
	        	}
	        	
	        	List<SenderdataMaster> pcaData = d2zDao.fetchDataBasedonSupplier(Arrays.asList(refNbrArray),"PCA");
	        	if(!pcaData.isEmpty()) {
	        		try {
						pcaWrapper.makeCreateShippingOrderPCACall(pcaData);
					} catch (EtowerFailureResponseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	
	        	List<String> fastwayOrderId = d2zDao.fetchDataforPFLSubmitOrder(refNbrs);
	        	 if(!fastwayOrderId.isEmpty()) {
	        		 try {
						pflWrapper.createSubmitOrderPFL(fastwayOrderId);
					} catch (EtowerFailureResponseException e) {
						e.printStackTrace();
					}
	        	 }
	        
	        	 List<String> articleIDS = d2zDao.fetchDataForEtowerForeCastCall(refNbrs);
	        	 if(!articleIDS.isEmpty()) {
	        		 eTowerWrapper.makeEtowerForecastCall(articleIDS);
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


}
