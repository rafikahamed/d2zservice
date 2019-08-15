package com.d2z.d2zservice.daoImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.BrokerRates;
import com.d2z.d2zservice.entity.D2ZRates;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.TransitTime;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.AUWeight;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.ZoneDetails;
import com.d2z.d2zservice.model.ZoneRates;
import com.d2z.d2zservice.model.etower.ETowerTrackingDetails;
import com.d2z.d2zservice.model.etower.TrackEventResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.repository.AUPostResponseRepository;
import com.d2z.d2zservice.repository.BrokerRatesRepository;
import com.d2z.d2zservice.repository.CSTicketsRepository;
import com.d2z.d2zservice.repository.ConsigneeCountRepository;
import com.d2z.d2zservice.repository.D2ZRatesRepository;
import com.d2z.d2zservice.repository.ETowerResponseRepository;
import com.d2z.d2zservice.repository.FFResponseRepository;
import com.d2z.d2zservice.repository.MlidRepository;
import com.d2z.d2zservice.repository.NonD2ZDataRepository;
import com.d2z.d2zservice.repository.ReconcileNDRepository;
import com.d2z.d2zservice.repository.ReconcileRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.Senderdata_InvoicingRepository;
import com.d2z.d2zservice.repository.ServiceTypeListRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.TransitTimeRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class D2ZSuperUserDaoImpl implements ID2ZSuperUserDao {

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SenderDataRepository senderDataRepository;

	@Autowired
	Senderdata_InvoicingRepository senderdata_InvoicingRepository;

	@Autowired
	BrokerRatesRepository brokerRatesRepository;

	@Autowired
	D2ZRatesRepository d2zRatesRepository;

	@Autowired
	UserServiceRepository userServiceRepository;

	@Autowired
	ConsigneeCountRepository consigneeCountRepository;

	@Autowired
	MlidRepository mlidRepository;

	@Autowired
	ServiceTypeListRepository serviceTypeListRepository;

	@Autowired
	ReconcileRepository reconcileRepository;

	@Autowired
	ReconcileNDRepository reconcileNDRepository;

	@Autowired
	NonD2ZDataRepository nonD2ZDataRepository;
	
	@Autowired
	ETowerResponseRepository eTowerResponseRepository;
	
	@Autowired
	AUPostResponseRepository auPostResponseRepository;
	
	@Autowired
	FFResponseRepository ffResponseRepository;
	
	@Autowired
	CSTicketsRepository csticketsRepository;
	
	@Autowired
	TransitTimeRepository transitTimeRepository;

	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for (UploadTrackingFileData fileDataValue : fileData) {
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setArticleID(fileDataValue.getConnoteNo());
			trackingDetails.setTrackEventDetails(fileDataValue.getTrackEventDetails().toUpperCase());
			/*
			 * System.out.println(fileDataValue.getTrackEventDateOccured());
			 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			 * Date parsedDate; Timestamp timestamp = null; try { parsedDate =
			 * dateFormat.parse(fileDataValue.getTrackEventDateOccured()); timestamp = new
			 * java.sql.Timestamp(parsedDate.getTime()); } catch (ParseException e) { throw
			 * new InvalidDateException("Invalid Date"); }
			 */
			// trackingDetails.setTrackEventDateOccured(Timestamp.valueOf(fileDataValue.getTrackEventDateOccured()));
			trackingDetails.setTrackEventDateOccured(fileDataValue.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
			trackingDetails.setIsDeleted("N");
			// trackAndTraceRepository.save(trackingDetails);
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData = (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		return insertedData;
	}

	@Override
	public List<Trackandtrace> uploadArrivalReport(List<ArrivalReportFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for (ArrivalReportFileData fileDataValue : fileData) {
			Trackandtrace trackingDetails = new Trackandtrace();
			trackingDetails.setReference_number(fileDataValue.getReferenceNumber());
			trackingDetails.setArticleID(fileDataValue.getConnoteNo());
			String trackEvent = "Shortage";
			if ("CLEAR".equalsIgnoreCase(fileDataValue.getStatus())) {
				trackEvent = "Received and Clear";
			} else if ("HELD".equalsIgnoreCase(fileDataValue.getStatus())) {
				trackEvent = "Received and Held";
			}
			trackingDetails.setTrackEventDetails(trackEvent.toUpperCase());
			/*
			 * System.out.println(fileDataValue.getScannedDateTime()); SimpleDateFormat
			 * dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); Date parsedDate;
			 * Timestamp timestamp = null; try { parsedDate =
			 * dateFormat.parse(fileDataValue.getScannedDateTime()); timestamp = new
			 * java.sql.Timestamp(parsedDate.getTime());
			 * 
			 * } catch (ParseException e) { throw new InvalidDateException("Invalid Date");
			 * }
			 */
			// trackingDetails.setTrackEventDateOccured(Timestamp.valueOf(fileDataValue.getScannedDateTime()));
			trackingDetails.setTrackEventDateOccured(fileDataValue.getScannedDateTime());
			System.out.println(trackingDetails.getTrackEventDateOccured());
			trackingDetails.setFileName(fileDataValue.getFileName());
			trackingDetails.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
//			System.out.println("Instant : "+Timestamp.from(Instant.now()).toString());
			trackingDetails.setIsDeleted("N");
			trackingDetailsList.add(trackingDetails);
		}
		List<Trackandtrace> insertedData = (List<Trackandtrace>) trackAndTraceRepository.saveAll(trackingDetailsList);
		trackAndTraceRepository.updateTracking();
		trackAndTraceRepository.deleteDuplicates();
		return insertedData;
	}

	@Override
	public List<String> brokerCompanyDetails() {
		List<String> companyDetails = userRepository.fetchBrokerCompanyName();
		return companyDetails;
	}

	@Override
	public User fetchUserDetails(String companyName) {
		User userDetails = userRepository.fetchBrokerbyCompanyName(companyName);
		return userDetails;
	}

	@Override
	public List<String> exportDeteledConsignments(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<String> deletedConsignments = senderDataRepository.fetchDeletedConsignments(fromTime, toTime);
		System.out.println(deletedConsignments.size());

		return deletedConsignments;
	}

	@Override
	public List<SenderdataMaster> exportConsignments(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<SenderdataMaster> exportedConsignments = senderDataRepository.exportConsignments(fromTime, toTime);
		System.out.println(exportedConsignments.size());
		return exportedConsignments;
	}

	@Override
	public List<Object> exportShipment(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<Object> exportedShipment = senderDataRepository.exportShipment(fromTime, toTime);
		System.out.println(exportedShipment.size());
		return exportedShipment;
	}

	@Override
	public List<Object> exportNonShipment(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<Object> exportedShipment = senderDataRepository.exportNonShipment(fromTime, toTime);
		System.out.println(exportedShipment.size());
		return exportedShipment;
	}

	@Override
	public ResponseMessage insertTrackingDetails(TrackingEventResponse trackEventresponse) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		List<TrackEventResponseData> responseData = trackEventresponse.getData();
		ResponseMessage responseMsg = new ResponseMessage();

		if (responseData.isEmpty()) {
			responseMsg.setResponseMessage("No Data from ETower");
		} else {

			for (TrackEventResponseData data : responseData) {

				if (data != null && data.getEvents() != null) {
					for (ETowerTrackingDetails trackingDetails : data.getEvents()) {
						Trackandtrace trackandTrace = new Trackandtrace();
						trackandTrace.setArticleID(trackingDetails.getTrackingNo());
						trackandTrace.setFileName("eTowerAPI");
						// Date date = Date.from(Instant.ofEpochSecond(trackingDetails.getEventTime()));
						// Date date = new Date((long)trackingDetails.getEventTime());

						// DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// String trackEventOccurred = dateFormat.format(date);
						// System.out.println("DAte: "+date);
						// System.out.println("String: "+trackEventOccurred);
						trackandTrace.setTrackEventDateOccured(trackingDetails.getEventTime());
						trackandTrace.setTrackEventCode(trackingDetails.getEventCode());
						/*
						 * if(trackingDetails.getActivity()!=null) {
						 * if(trackingDetails.getActivity().contains("Attempted Delivery") ||
						 * trackingDetails.getActivity().contains("Unable to complete delivery") ) {
						 * trackandTrace.setTrackEventDetails("ATTEMPTED DELIVERY"); }else
						 * if(trackingDetails.getActivity().contains("Collection")) {
						 * trackandTrace.setTrackEventDetails("AWAITING COLLECTION"); }else
						 * if(trackingDetails.getActivity().contains("Delivered") ||
						 * trackingDetails.getActivity().contains("Receiver requested Safe Drop") ) {
						 * trackandTrace.setTrackEventDetails("DELIVERED"); }else
						 * if(trackingDetails.getActivity().contains("Accepted by Driver") ||
						 * trackingDetails.getActivity().
						 * contains("With Australia Post for delivery today") ||
						 * trackingDetails.getActivity().contains("In Transit")) {
						 * trackandTrace.setTrackEventDetails("IN TRANSIT"); }else
						 * if(trackingDetails.getActivity().contains("facility") ||
						 * trackingDetails.getActivity().contains("Transferred") ) {
						 * trackandTrace.setTrackEventDetails("ITEM PROCESSED BY FACILITY"); }else
						 * if(trackingDetails.getActivity().contains("Shipping information") ||
						 * trackingDetails.getActivity().contains("Order Processed") ||
						 * trackingDetails.getActivity().contains("Order Accepted") ||
						 * trackingDetails.getActivity().contains("Lodged") ||
						 * trackingDetails.getActivity().contains("Manifested")) {
						 * trackandTrace.setTrackEventDetails("SHIPPING INFORMATION RECEIVED"); }else {
						 * trackandTrace.setTrackEventDetails(trackingDetails.getActivity()); } }
						 */
						trackandTrace.setTrackEventDetails(trackingDetails.getActivity());
						trackandTrace.setCourierEvents(trackingDetails.getActivity());
						trackandTrace.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
						trackandTrace.setReference_number(trackingDetails.getTrackingNo());
						trackandTrace.setLocation(trackingDetails.getLocation());
						trackandTrace.setIsDeleted("N");
						if ("ARRIVED AT DESTINATION AIRPORT".equalsIgnoreCase(trackandTrace.getTrackEventDetails())
								|| ("COLLECTED FROM AIRPORT TERMINAL"
										.equalsIgnoreCase(trackandTrace.getTrackEventDetails()))
								|| ("PREPARING TO DISPATCH".equalsIgnoreCase(trackandTrace.getTrackEventDetails()))) {
							trackandTrace.setIsDeleted("Y");
						}
						trackAndTraceList.add(trackandTrace);
					}

				}
			}
			trackAndTraceRepository.saveAll(trackAndTraceList);
			trackAndTraceRepository.updateTracking();
			trackAndTraceRepository.deleteDuplicates();
			responseMsg.setResponseMessage("Data uploaded successfully from ETower");
		}
		return responseMsg;
	}

	@Override
	public List<String> fetchTrackingNumbersForETowerCall() {
		List<String> dbResult = trackAndTraceRepository.fetchTrackingNumbersForETowerCall();
		return dbResult;

	}

	@Override
	public List<String> fetchTrackingNumbersForPCACall() {
		List<String> dbResult = trackAndTraceRepository.fetchTrackingNumbersForPCACall();
		return dbResult;

	}

	@Override
	public String uploadBrokerRates(List<BrokerRatesData> brokerRatesData) {
		String message = "";
		List<BrokerRates> brokerRatesList = new ArrayList<BrokerRates>();
		for (BrokerRatesData brokerRateData : brokerRatesData) {

			for (ZoneDetails zoneData : brokerRateData.getZone()) {

				for (ZoneRates zoneRates : zoneData.getRates()) {
					BrokerRates brokerRates_DB = brokerRatesRepository.findByCompositeKey(
							brokerRateData.getBrokerUserName(), brokerRateData.getInjectionType(),
							brokerRateData.getServiceType(), zoneData.getZoneID(), zoneRates.getMinWeight(),
							zoneRates.getMaxWeight());
					if (null != brokerRates_DB) {
						brokerRates_DB.setBackupInd("Y");
						brokerRatesList.add(brokerRates_DB);
					}
					BrokerRates brokerRates = new BrokerRates();
					brokerRates.setBrokerUserName(brokerRateData.getBrokerUserName());
					brokerRates.setInjectionType(brokerRateData.getInjectionType());
					brokerRates.setServiceType(brokerRateData.getServiceType());
					brokerRates.setGST(brokerRateData.getGST());
					brokerRates.setFuelSurcharge(brokerRateData.getFuelSurcharge());
					brokerRates.setZoneID(zoneData.getZoneID());
					brokerRates.setRate(zoneRates.getRate());
					brokerRates.setMaxWeight(zoneRates.getMaxWeight());
					brokerRates.setMinWeight(zoneRates.getMinWeight());
					brokerRates.setBackupInd("N");
					brokerRates.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
					brokerRatesList.add(brokerRates);

				}
			}

		}
		List<BrokerRates> insertedData = (List<BrokerRates>) brokerRatesRepository.saveAll(brokerRatesList);
		if (null != insertedData && insertedData.size() > 0) {
			message = "Data uploaded Successfully";
		} else {
			message = "Failed to upload Data";
		}
		return message;
	}

	@Override
	public String uploadD2ZRates(List<D2ZRatesData> d2zRatesData) {
		String message = "";
		List<D2ZRates> d2zRatesList = new ArrayList<D2ZRates>();
		for (D2ZRatesData d2zRateData : d2zRatesData) {

			for (ZoneDetails zoneData : d2zRateData.getZone()) {

				for (ZoneRates zoneRates : zoneData.getRates()) {
					D2ZRates d2zRates_DB = d2zRatesRepository.findByCompositeKey(d2zRateData.getServiceType(),
							zoneData.getZoneID(), zoneRates.getMinWeight(), zoneRates.getMaxWeight());
					if (null != d2zRates_DB) {
						d2zRates_DB.setBackupInd("Y");
						d2zRatesList.add(d2zRates_DB);
					}
					D2ZRates d2zRates = new D2ZRates();
					d2zRates.setServiceType(d2zRateData.getServiceType());
					d2zRates.setFuelSurcharge(d2zRateData.getFuelSurcharge());
					d2zRates.setZoneID(zoneData.getZoneID());
					d2zRates.setRate(zoneRates.getRate());
					d2zRates.setMaxWeight(zoneRates.getMaxWeight());
					d2zRates.setMinWeight(zoneRates.getMinWeight());
					d2zRates.setBackupInd("N");
					d2zRates.setTimestamp(Timestamp.valueOf(LocalDateTime.now()).toString());
					d2zRatesList.add(d2zRates);

				}
			}

		}
		List<D2ZRates> insertedData = (List<D2ZRates>) d2zRatesRepository.saveAll(d2zRatesList);
		if (null != insertedData && insertedData.size() > 0) {
			message = "Data uploaded Successfully";
		} else {
			message = "Failed to upload Data";
		}
		return message;
	}

	public List<User> brokerList() {
		List<User> brokerList = userRepository.fetchBrokerList();
		return brokerList;
	}

	@Override
	public List<String> fetchMlidList() {
		List<String> mlidList = serviceTypeListRepository.getServiceTypeList();
		return mlidList;
	}

	@Override
	public List<String> brokerShipmentList(int userId) {
		List<String> brokerShipmentList = senderDataRepository.getBrokerShipmentList(userId);
		return brokerShipmentList;
	}

	@Override
	public List<String> brokerShipment() {
		List<String> shipmentData = senderDataRepository.fetchSenderShipmenntData();
		return shipmentData;
	}

	@Override
	public List<Integer> fetchBrokerClientIds() {
		List<Integer> brokerClientIds = userRepository.fetchBrokerClientIds();
		return brokerClientIds;
	}

	@Override
	public List<String> brokerInvoiced() {
		List<String> shipmentInvoicedData = senderDataRepository.brokerInvoiced();
		return shipmentInvoicedData;
	}

	@Override
	public List<String> reconcileData(String articleNo, String refrenceNumber) {
		List<String> reconciledata = senderDataRepository.reconcileData(articleNo, refrenceNumber);
		return reconciledata;
	}

	@Override
	public List<Reconcile> reconcileUpdate(List<Reconcile> reconcileCalculatedList) {
		List<Reconcile> reconcileList = (List<Reconcile>) reconcileRepository.saveAll(reconcileCalculatedList);
		return reconcileList;
	}

	@Override
	public List<Reconcile> fetchReconcileData(List<String> reconcileReferenceNum) {
		List<Reconcile> reconcileFinal = (List<Reconcile>) reconcileRepository
				.fetchReconcileData(reconcileReferenceNum);
		return reconcileFinal;
	}

	@Override
	public UserMessage approvedInvoice(ApprovedInvoice approvedInvoice) {
		senderdata_InvoicingRepository.approvedInvoice(approvedInvoice.getIndicator(), approvedInvoice.getAirwaybill());
		UserMessage userMsg = new UserMessage();
		if (approvedInvoice.getIndicator().equalsIgnoreCase("Invoiced")) {
			userMsg.setMessage("Invoiced Approved Successfully");
		} else if (approvedInvoice.getIndicator().equalsIgnoreCase("Billed")) {
			userMsg.setMessage("Invoiced Billed Successfully");
		}
		return userMsg;
	}

	@Override
	public void reconcilerates(List<String> reconcileReferenceNum) {
		senderDataRepository
				.reconcilerates(reconcileReferenceNum.stream().map(Object::toString).collect(Collectors.joining(",")));
	}

	@Override
	public List<String> fetchNotBilled() {
		List<String> notBilledData = senderDataRepository.fetchNotBilled();
		return notBilledData;
	}

	@Override
	public List<String> downloadInvoice(List<String> broker, List<String> airwayBill, String billed, String invoiced) {
		List<String> downloadInvoice = senderDataRepository.downloadInvoice(broker, airwayBill, billed, invoiced);
		return downloadInvoice;
	}

	@Override
	public UserMessage fetchNonD2zClient(List<NonD2ZData> nonD2zData) {
		nonD2ZDataRepository.saveAll(nonD2zData);
		List<String> articleNbr = nonD2zData.stream().map(obj -> {
			return obj.getArticleId();
		}).collect(Collectors.toList());
		nonD2ZDataRepository.nonD2zRates(articleNbr.stream().map(Object::toString).collect(Collectors.joining(",")));
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Non-D2Z Data Uploaded Successfully into the system");
		return userMsg;
	}

	@Override
	public List<String> fetchAllArticleId() {
		List<String> listOfArticleId = nonD2ZDataRepository.fetchAllArticleId();
		return listOfArticleId;
	}

	@Override
	public List<Reconcile> downloadReconcile() {
		// List<Reconcile> reconcileFinal = (List<Reconcile>)
		// reconcileRepository.downloadReconcile();
		return null;
	}

	@Override
	public List<Reconcile> downloadReconcile(List<String> reconcileNumbers) {
		List<Reconcile> reconcileFinal = (List<Reconcile>) reconcileRepository.downloadReconcile(reconcileNumbers);
		return reconcileFinal;
	}

	@Override
	public List<String> fetchNonD2zBrokerUserName() {
		List<String> listOfNonBroker = userRepository.fetchNonD2zBrokerUserName();
		return listOfNonBroker;
	}

	@Override
	public NonD2ZData reconcileNonD2zData(String articleNo) {
		NonD2ZData nonD2zData = nonD2ZDataRepository.reconcileNonD2zData(articleNo);
		return nonD2zData;
	}

	@Override
	public List<ReconcileND> reconcileNonD2zUpdate(List<ReconcileND> reconcileNoND2zList) {
		List<ReconcileND> reconcileList = (List<ReconcileND>) reconcileNDRepository.saveAll(reconcileNoND2zList);
		return reconcileList;
	}

	@Override
	public void reconcileratesND(List<String> reconcileArticleIdNum) {
		nonD2ZDataRepository.reconcileratesND(
				reconcileArticleIdNum.stream().map(Object::toString).collect(Collectors.joining(",")));
	}

	@Override
	public List<ReconcileND> downloadNonD2zReconcile(List<String> nonD2zReconcileNumbers) {
		List<ReconcileND> reconcileNonD2zFinal = (List<ReconcileND>) reconcileNDRepository
				.downloadNonD2zReconcile(nonD2zReconcileNumbers);
		return reconcileNonD2zFinal;
	}

	@Override
	public List<NonD2ZData> brokerNonD2zShipment() {
		List<NonD2ZData> nonD2ZData = nonD2ZDataRepository.brokerNonD2zShipment();
		return nonD2ZData;
	}

	@Override
	public List<String> downloadNonD2zInvoice(List<String> broker, List<String> airwayBill, String billed,
			String invoiced) {
		List<String> downloadInvoice = nonD2ZDataRepository.downloadNonD2zInvoice(broker, airwayBill, billed, invoiced);
		return downloadInvoice;
	}

	@Override
	public UserMessage approveNdInvoiced(ApprovedInvoice approvedInvoice) {
		nonD2ZDataRepository.approveNdInvoiced(approvedInvoice.getIndicator(), approvedInvoice.getAirwaybill());
		UserMessage userMsg = new UserMessage();
		if (approvedInvoice.getIndicator().equalsIgnoreCase("Invoiced")) {
			userMsg.setMessage("Invoiced Approved Successfully");
		} else if (approvedInvoice.getIndicator().equalsIgnoreCase("Billed")) {
			userMsg.setMessage("Invoiced Billed Successfully");
		}
		return userMsg;
	}

	@Override
	public List<NonD2ZData> brokerNdInvoiced() {
		List<NonD2ZData> nonD2ZApprovedData = nonD2ZDataRepository.brokerNdInvoiced();
		return nonD2ZApprovedData;
	}

	@Override
	public List<String> fetchNonD2zNotBilled() {
		List<String> notBilledData = nonD2ZDataRepository.fetchNonD2zNotBilled();
		return notBilledData;
	}

	@Override
	public NonD2ZData reconcileNonD2zFreipostData(String referenceNumber) {
		NonD2ZData nonD2zData = nonD2ZDataRepository.reconcileNonD2zFreipostData(referenceNumber);
		return nonD2zData;
	}

	@Override
	public List<String> fetchAllReconcileReferenceNumbers() {
		List<String> referenceNumber_DB = reconcileRepository.fetchAllReconcileReferenceNumbers();
		return referenceNumber_DB;
	}

	@Override
	public List<String> fetchAllReconcileArticleIdNumbers() {
		List<String> articleId_DB = reconcileRepository.fetchAllReconcileArticleIdNumbers();
		return articleId_DB;
	}

	@Override
	public List<String> fetchAllReconcileNonD2zReferenceNumbers() {
		List<String> referenceNumberNonD2z_DB = reconcileNDRepository.fetchAllReconcileNonD2zReferenceNumbers();
		return referenceNumberNonD2z_DB;
	}

	@Override
	public List<String> fetchAllReconcileNonD2zArticleIdNumbers() {
		List<String> referenceNumberNonD2z_DB = reconcileNDRepository.fetchAllReconcileNonD2zArticleIdNumbers();
		return referenceNumberNonD2z_DB;
	}

	@Override
	public List<ETowerResponse> fetchEtowerLogResponse(String fromDate, String toDate) {
		List<ETowerResponse> etowerResponseData = eTowerResponseRepository.fetchEtowerLogResponse(fromDate,toDate);
		return etowerResponseData;
	}

	@Override
	public List<AUPostResponse> fetchAUPosLogtResponse(String fromDate, String toDate) {
		List<AUPostResponse> auPostResponseData = auPostResponseRepository.fetchAUPostLogResponse(fromDate,toDate);
		return auPostResponseData;
	}

	@Override
	public List<FFResponse> fetchFdmLogResponse(String fromDate, String toDate) {
		List<FFResponse> fdmResponseData = ffResponseRepository.fetchdmFLogResponse(fromDate,toDate);
		return fdmResponseData;
	}

	@Override
	public List<FFResponse> fetchFreiPostResponseResponse(String fromDate, String toDate) {
		List<FFResponse> freiPostResponseData = ffResponseRepository.fetchFreiPostResponseResponse(fromDate,toDate);
		return freiPostResponseData;
	}

	@Override
	public List<String> trackingLabel(List<String> refBarNumArray) {
		List<String> trackingDetails= senderDataRepository.fetchTrackingLabel(refBarNumArray);
		System.out.println(trackingDetails.size());
		return trackingDetails;
	}
	@Override
	public List<Mlid> downloadMlid(String service) {
		// TODO Auto-generated method stub
		System.out.println("serv:"+service);
		List<Mlid> mlidlist = mlidRepository.downloadMlid(service);
		System.out.println("size:"+mlidlist.size());
		
		return mlidlist;
	}

	@Override
	public UserMessage addMlid(List<Object> MlidData) {
		List<Mlid> mlidlist = new ArrayList<Mlid>();
		System.out.println("size:"+MlidData.size());
		System.out.println("obj:"+MlidData.get(1).toString());
		
		
		   ObjectMapper oMapper = new ObjectMapper();
		
		   Map<String, Object> map1 = oMapper.convertValue(MlidData.get(1), Map.class);
		   String service = map1.get("ServiceType").toString();
			
		System.out.println("service:"+service);
		List<Mlid> mlidsearch = mlidRepository.downloadMlid(service);
		UserMessage userMsg = new UserMessage();
		System.out.println("mildsearchsize:"+mlidsearch.size()+".."+mlidsearch.isEmpty());
		if(mlidsearch.isEmpty()) {
		for(Object mild : MlidData )
		{
			/*JacksonJsonParser jsonParser = new JacksonJsonParser();
			Map<String, Object> responses = jsonParser.parseMap(mild.toString());*/
			 Map<String, Object> map = oMapper.convertValue(mild, Map.class);
			Mlid mldata = new Mlid();
			mldata.setServiceType(map.get("ServiceType").toString());
			mldata.setDestinationzone(map.get("destinationzone").toString());
			mldata.setZoneID(map.get("ZoneID").toString());
			mldata.setMinweight(map.get("Minweight").toString());
			mldata.setMaxweight(map.get("Maxweight").toString());
			mldata.setMlid(map.get("MLID").toString());
			mldata.setInjectionState(map.get("InjectionState").toString());
		
			mlidlist.add(mldata);
			
			
			
		}
		mlidRepository.saveAll(mlidlist);
		userMsg.setMessage("Added Succesfully");
		}
		// TODO Auto-generated method stub
		//System.out.println("size:"+MlidData.size()+"service:"+ MlidData.get(1).getServiceType()+"::"+MlidData.get(1).getMLID());
		//System.out.println("dt"+MlidData.get(1));
	
		/*String service = MlidData.get(1).getServiceType();
		List<Mlid> mlidlist = mlidRepository.downloadMlid(service);
		if(mlidlist.isEmpty()) {
		
		//mlidRepository.saveAll(MlidData);
		userMsg.setMessage("Added Succesfully");
		}*/
		else
		{
			userMsg.setMessage("Service Type already exists");
		}
		
		return userMsg;
		
	}
	@Override
	public UserMessage deleteMlid(String service)
	{
		UserMessage userMsg = new UserMessage();
		System.out.println("servicenew:"+service+"delete");
	int i =	mlidRepository.updateMlidList(service+"delete", service);
	//System.out.println("count"+i);
	userMsg.setMessage("Deleted Succesfully");
		return userMsg;
		
	}

	@Override
	public List<String> fetchMlidDeleteList() {
		
			List<String> mlidList = mlidRepository.getServiceTypeList();
			return mlidList;
		
	}

	@Override
	public List<AUWeight> downloadAUweight(List<Object> ArticleID) {
		// TODO Auto-generated method stub
		 ObjectMapper oMapper = new ObjectMapper();
		
		 System.out.print("inside AUWeight");
		 List<AUWeight> auweightlist = new ArrayList<AUWeight>();
		 List<String> articledata = new ArrayList<String>();
		for(Object articleid : ArticleID )
		{
			 Map<String, Object> map = oMapper.convertValue(articleid, Map.class);
			//AUWeight auwei = new AUWeight();
			
			 String ArticleId = map.get("ArticleID").toString();
			 articledata.add(ArticleId);
			// BigDecimal weight = senderDataRepository.fetchcubicweight(ArticleId);
			
			/* auwei.setArticleID(ArticleId);
			 auwei.setCubicWeight(weight);
			 auweightlist.add(auwei);*/
			
		}
		System.out.print("articleid:"+articledata.size());
		List<BigDecimal> weight = senderDataRepository.fetchcubicweight(articledata);
		System.out.print(weight.size());
		for(int i =0;i<weight.size();i++)
		{AUWeight auwei = new AUWeight();
			auwei.setArticleID(articledata.get(i));
		 auwei.setCubicWeight(weight.get(i));
		 auweightlist.add(auwei);
			
		}
		
				
		return auweightlist;
	}

	@Override
	public String fetchUserById(int userId) {
		return userRepository.fetchUserById( userId);
	}

	@Override
	public List<OpenEnquiryResponse> fetchOpenEnquiryDetails() {
		List<OpenEnquiryResponse> openEnquiryList = new ArrayList<OpenEnquiryResponse>();
		OpenEnquiryResponse openEnquiryResponse = null;
		List<String> openEnquiry = csticketsRepository.fetchOpenEnquiryDetails();
		if(openEnquiry.size() > 0) {
			Iterator itr = openEnquiry.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				String deliveryDate = null;
				openEnquiryResponse = new OpenEnquiryResponse();
				openEnquiryResponse.setUserName(obj[0] != null ? obj[0].toString() : "");
				openEnquiryResponse.setTicketNumber(obj[1] != null ? obj[1].toString() : "");
				openEnquiryResponse.setUserId(Integer.parseInt(obj[2].toString()));
				openEnquiryResponse.setClient_broker_id(Integer.parseInt(obj[3].toString()));
				openEnquiryResponse.setArticleID(obj[4] != null ? obj[4].toString() : "");
				openEnquiryResponse.setTrackingEventDateOccured(obj[5] != null ? obj[5].toString() : "");
				openEnquiryResponse.setConsigneeName(obj[6] != null ? obj[6].toString() : "");
				openEnquiryResponse.setStatus(obj[7] != null ? obj[7].toString() : "");
				openEnquiryResponse.setComments(obj[8] != null ? obj[8].toString() : "");
				openEnquiryResponse.setAttachment(obj[9] != null ? obj[9].toString() : "");
				openEnquiryResponse.setConsigneeaddr1(obj[10] != null ? obj[10].toString() : "");
				openEnquiryResponse.setConsigneeSuburb(obj[11] != null ? obj[11].toString() : "");
				openEnquiryResponse.setConsigneeState(obj[12] != null ? obj[12].toString() : "");
				openEnquiryResponse.setConsigneePostcode(obj[13] != null ? obj[13].toString() : "");
				openEnquiryResponse.setProductDescription(obj[14] != null ? obj[14].toString() : "");
				TransitTime transitTimeResponse = transitTimeRepository.fetchTransitTime(openEnquiryResponse.getConsigneePostcode());
				deliveryDate = D2ZCommonUtil.getIncreasedTime(openEnquiryResponse.getTrackingEventDateOccured(),transitTimeResponse.getTransitTime());
				openEnquiryResponse.setTrackingDeliveryDate(deliveryDate);
				openEnquiryList.add(openEnquiryResponse);
			  }
		}
		return openEnquiryList;
	}

	@Override
	public String updateEnquiryDetails(List<OpenEnquiryResponse> openEnquiryDetails) {
		for(OpenEnquiryResponse enquiryDetails:openEnquiryDetails) {
			if(enquiryDetails.getD2zComments() != null) {
				csticketsRepository.updateTicketInfo(enquiryDetails.getD2zComments(), enquiryDetails.getStatus(), enquiryDetails.getSendUpdate(), enquiryDetails.getArticleID());
			}
		}
		return "Enquiry Updated Successfully";
	}

}
