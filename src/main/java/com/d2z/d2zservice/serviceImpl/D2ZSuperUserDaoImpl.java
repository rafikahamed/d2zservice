package com.d2z.d2zservice.serviceImpl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.BrokerRates;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.D2ZRates;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.IncomingJobs;
import com.d2z.d2zservice.entity.IncomingJobsLogic;
import com.d2z.d2zservice.entity.Mlid;
import com.d2z.d2zservice.entity.NonD2ZData;
import com.d2z.d2zservice.entity.Parcels;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.Reconcile;
import com.d2z.d2zservice.entity.ReconcileND;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Senderdata_Invoicing;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.TransitTime;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.model.AUWeight;
import com.d2z.d2zservice.model.ApprovedInvoice;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.CategoryResponse;
import com.d2z.d2zservice.model.CreateJobRequest;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.HeldParcel;
import com.d2z.d2zservice.model.IncomingJobResponse;
import com.d2z.d2zservice.model.OpenEnquiryResponse;
import com.d2z.d2zservice.model.PFLTrackingResponseDetails;
import com.d2z.d2zservice.model.ParcelResponse;
import com.d2z.d2zservice.model.ProfitLossReport;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.ShipmentApproval;
import com.d2z.d2zservice.model.ShipmentCharges;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.WeightUpload;
import com.d2z.d2zservice.model.Zone;
import com.d2z.d2zservice.model.ZoneDetails;
import com.d2z.d2zservice.model.ZoneRates;
import com.d2z.d2zservice.model.ZoneReport;
import com.d2z.d2zservice.model.ZoneRequest;
import com.d2z.d2zservice.model.ZoneResponse;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
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
import com.d2z.d2zservice.repository.IncomingJobsLogicRepository;
import com.d2z.d2zservice.repository.IncomingJobsRepository;
import com.d2z.d2zservice.repository.MlidRepository;
import com.d2z.d2zservice.repository.NonD2ZDataRepository;
import com.d2z.d2zservice.repository.ParcelRepository;
import com.d2z.d2zservice.repository.ReconcileNDRepository;
import com.d2z.d2zservice.repository.ReconcileRepository;
import com.d2z.d2zservice.repository.ReturnsRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.Senderdata_InvoicingRepository;
import com.d2z.d2zservice.repository.ServiceTypeListRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.TransitTimeRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

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
	private ID2ZBrokerDao d2zBrokerDao;

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
	
	@Autowired
	IncomingJobsLogicRepository incomingJobsLogicRepository;
	
	@Autowired
	IncomingJobsRepository incomingRepository;

	@Autowired
	ReturnsRepository returnsRepository; 
	
	@Autowired
	ParcelRepository parcelRepository;

	@Override
	public List<Trackandtrace> uploadTrackingFile(List<UploadTrackingFileData> fileData) {
		List<Trackandtrace> trackingDetailsList = new ArrayList<Trackandtrace>();
		for (UploadTrackingFileData fileDataValue : fileData) {
			Trackandtrace trackingDetails = new Trackandtrace();
			//trackingDetails.setRowId(D2ZCommonUtil.generateTrackID());
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
			//trackingDetails.setRowId(D2ZCommonUtil.generateTrackID());
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
	public List<String> fetchServiceTypeByUserName(String userName) {
		List<String> serviceTypeList = userServiceRepository.fetchAllServiceTypeByUserName(userName);
		return serviceTypeList;
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
	public List<Object> exportConsignments(String fromDate, String toDate) {
		String fromTime = fromDate.concat(" ").concat("00:00:00");
		String toTime = toDate.concat(" ").concat("23:59:59");
		List<Object> exportedConsignments = senderDataRepository.exportConsignments(fromTime, toTime);
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
	public ResponseMessage insertTrackingDetails(TrackingEventResponse trackEventresponse,Map<String,String> map) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		List<TrackEventResponseData> responseData = trackEventresponse.getData();
		ResponseMessage responseMsg = new ResponseMessage();

		if (responseData.isEmpty()) {
			responseMsg.setResponseMessage("No Data from ETower");
		} else {

			for (TrackEventResponseData data : responseData) {

				if (data != null && data.getEvents() != null) {
					for (ETowerTrackingDetails trackingDetails : data.getEvents()) {
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
						SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						
					    Date eventTime = null;
					    Date latestTime = null;
						try {
							eventTime = inputFormat.parse(trackingDetails.getEventTime());
							latestTime = output.parse(map.get(trackingDetails.getTrackingNo()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(eventTime.after(latestTime)) {
						Trackandtrace trackandTrace = new Trackandtrace();
						//trackandTrace.setRowId(D2ZCommonUtil.generateTrackID());
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
			}
			trackAndTraceRepository.saveAll(trackAndTraceList);
			trackAndTraceRepository.updateTracking();
			//trackAndTraceRepository.deleteDuplicates();
			responseMsg.setResponseMessage("Data uploaded successfully from ETower");
		}
		return responseMsg;
	}

	@Override
	public Map<String,String> fetchTrackingNumbersForETowerCall() {
	List<String> dbResult = trackAndTraceRepository.fetchTrackingNumbersForETowerCall();
		Map<String,String> map = new HashMap<String,String>();
		Iterator itr = dbResult.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			map.put(obj[0].toString(),obj[1].toString());
			
		}
		
		return map;

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
		List<String> downloadInvoice = senderdata_InvoicingRepository.downloadInvoice(broker, airwayBill, billed, invoiced);
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
	public List<ETowerResponse> fetchEtowerLogResponseApi(List<String> api ,String fromDate, String toDate) {
		List<ETowerResponse> etowerResponseData = eTowerResponseRepository.fetchEtowerLogResponseApi(fromDate,toDate,api);
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
		for(Object mild : MlidData ){
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
		else{
			userMsg.setMessage("Service Type already exists");
		}
		return userMsg;
	}
	
	@Override
	public UserMessage deleteMlid(String service){
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
		for(Object articleid : ArticleID ){
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
		for(int i =0;i<weight.size();i++){
		 AUWeight auwei = new AUWeight();
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
				openEnquiryResponse.setTrackingEventDateOccured(obj[5] != null ? obj[5].toString(): null);
				openEnquiryResponse.setConsigneeName(obj[6] != null ? obj[6].toString() : "");
				openEnquiryResponse.setStatus(obj[7] != null ? obj[7].toString() : "");
				openEnquiryResponse.setComments(obj[8] != null ? obj[8].toString() : "");
				openEnquiryResponse.setD2zComments(obj[9] != null ? obj[9].toString() : "");
				openEnquiryResponse.setConsigneeaddr1(obj[10] != null ? obj[10].toString() : "");
				openEnquiryResponse.setConsigneeSuburb(obj[11] != null ? obj[11].toString() : "");
				openEnquiryResponse.setConsigneeState(obj[12] != null ? obj[12].toString() : "");
				openEnquiryResponse.setConsigneePostcode(obj[13] != null ? obj[13].toString() : "");
				openEnquiryResponse.setProductDescription(obj[14] != null ? obj[14].toString() : "");
				openEnquiryResponse.setTrackingEvent(obj[15] != null ? obj[15].toString() : "");
				openEnquiryResponse.setSendUpdate(obj[17] != null ? obj[17].toString() : "");
				openEnquiryResponse.setFileName(obj[18] != null ? obj[18].toString() : "");
				TransitTime transitTimeResponse = transitTimeRepository.fetchTransitTime(openEnquiryResponse.getConsigneePostcode());
				if( null != transitTimeResponse && null != transitTimeResponse.getTransitTime() && null !=openEnquiryResponse.getTrackingEventDateOccured()) {
					deliveryDate = D2ZCommonUtil.getIncreasedTime(openEnquiryResponse.getTrackingEventDateOccured(),transitTimeResponse.getTransitTime());
				}else {
					
				}
				openEnquiryResponse.setTrackingDeliveryDate(deliveryDate);
				openEnquiryList.add(openEnquiryResponse);
			  }
		}
		return openEnquiryList;
	}

	@Override
	public String updateEnquiryDetails(List<OpenEnquiryResponse> openEnquiryDetails) {
		for(OpenEnquiryResponse enquiryDetails:openEnquiryDetails) {
				csticketsRepository.updateTicketInfo(enquiryDetails.getD2zComments(), enquiryDetails.getStatus(), enquiryDetails.getSendUpdate(), enquiryDetails.getArticleID());
		}
		return "Enquiry Updated Successfully";
	}

	@Override
	public List<CSTickets> completedEnquiryDetails() {
		List<CSTickets> completedTicketDetails = csticketsRepository.completedEnquiryDetails();
		return completedTicketDetails;
	}

	@Override
	public List<IncomingJobsLogic> getBrokerMlidDetails() {
		List<IncomingJobsLogic> jobs = (List<IncomingJobsLogic>) incomingJobsLogicRepository.findAll();
		return jobs;
	}

	@Override
	public String createEnquiry(List<CreateJobRequest> createJob) {
		List<IncomingJobs> joblist = new ArrayList<IncomingJobs>();
		for(CreateJobRequest jobRequest :createJob){
			String mlid = "";
			for(DropDownModel model : jobRequest.getMlid()){
				 mlid = mlid + model.getName()+",";
			}
			mlid = mlid.substring(0, mlid.length() - 1);
				IncomingJobs jobs = new IncomingJobs();
				jobs.setBroker(jobRequest.getType());
				jobs.setMLID(mlid);
				jobs.setConsignee(jobRequest.getConsignee());
				jobs.setDestination(jobRequest.getDest());
				jobs.setHawb(jobRequest.getHawb());
				jobs.setMawb(jobRequest.getMawb());
				jobs.setFlight(jobRequest.getFlight());
				jobs.setPiece(jobRequest.getPrice());
				jobs.setWeight(jobRequest.getWeight());
				jobs.setIsDeleted("N");
				System.out.println("Date:"+":"+jobRequest.getEta().length());
				if(jobRequest.getEta()!=null && jobRequest.getEta().length() > 0){
				LocalDate date1  = LocalDate.parse(jobRequest.getEta());
				System.out.println("Date:"+":"+date1);
				jobs.setEta(date1);
				}
				joblist.add(jobs);
		}
		incomingRepository.saveAll(joblist);
	return "Job created Successfully";
	}

	@Override
	public List<IncomingJobResponse> getJobList() {
		// TODO Auto-generated method stub
		List<IncomingJobs> js = (List<IncomingJobs>)incomingRepository.fetchincomingJobs();
		List<IncomingJobResponse> joblist = new ArrayList<IncomingJobResponse>();
		for(IncomingJobs job :js){
			IncomingJobResponse jobs = new IncomingJobResponse();
			System.out.println("ATA:"+job.getAta()+"ETA:"+job.getEta());
			jobs.setJobid(job.getID());
			
			if(job.getEta()!=null){
			jobs.setEta(job.getEta().toString());
			}
			if(job.getAta()!=null){
			jobs.setAta(job.getAta().toString());
			}
			jobs.setBroker(job.getBroker());
			jobs.setClear(job.getClear());
			jobs.setConsignee(job.getConsignee());
			jobs.setDestination(job.getDestination());
			jobs.setFlight(job.getFlight());
			jobs.setHawb(job.getHawb());
			jobs.setHeld(job.getHeld());
			jobs.setMawb(job.getMawb());
			jobs.setMLID(job.getMLID());
			jobs.setNote(job.getNote());
			jobs.setOutturn(job.getOutturn());
			jobs.setPiece(job.getPiece());
			jobs.setWeight(job.getWeight());
			joblist.add(jobs);
			
		}
		return  joblist;
	}
	

	@Override
	public List<String> fetchClientDetails(String referenceNumber, String barcodeLabel, String articleId) {
		String dataMatrix = null;
		if(!barcodeLabel.equalsIgnoreCase("null")) {
			dataMatrix = barcodeLabel.substring(1,39);
		}
		List<String> clientDetails = returnsRepository.fetchClientDetails(referenceNumber,barcodeLabel,articleId,dataMatrix);
		return clientDetails;
	}

	@Override
	public String createReturns(List<Returns> returnsList) {
		returnsRepository.saveAll(returnsList);
		returnsRepository.updateReturnsClientDetails();
		return "Returns Updated Successfully";
	}

	@Override
	public String updateJob(List<IncomingJobResponse> js) {
		List<IncomingJobs> joblist =  new ArrayList<IncomingJobs>();
		for(IncomingJobResponse job :js){
			IncomingJobs jobs = new IncomingJobs();
			jobs.setBroker(job.getBroker());
			jobs.setClear(job.getClear());
			jobs.setConsignee(job.getConsignee());
			jobs.setDestination(job.getDestination());
			jobs.setFlight(job.getFlight());
			jobs.setHawb(job.getHawb());
			jobs.setHeld(job.getHeld());
			jobs.setMawb(job.getMawb());
			jobs.setMLID(job.getMLID());
			jobs.setNote(job.getNote());
			if(job.getEta()!=null && job.getEta().length() > 0){
				LocalDate date1  = LocalDate.parse(job.getEta());
				jobs.setEta(date1);
			}
			if(job.getAta()!=null &&  job.getAta().length() > 0){
				System.out.print("Ata;"+job.getAta());
			
			if(job.getAta().contains("T04:")){
				 LocalDate date2 = LocalDate.parse(job.getAta(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
				 jobs.setAta(date2);
			}
			else{
				LocalDate date2  = LocalDate.parse(job.getAta());
				jobs.setAta(date2);
				}
			}
			jobs.setPiece(job.getPiece());
			jobs.setWeight(job.getWeight());
			jobs.setID(job.getJobid());
		
			if(job.getOutturn()!=null && (job.getOutturn().equalsIgnoreCase("True") || job.getOutturn().equalsIgnoreCase("Y"))){
				System.out.print("Outturn;"+job.getOutturn());	
				jobs.setOutturn("Y");
			}
			jobs.setHeld(job.getHeld());
			jobs.setIsDeleted("N");
			joblist.add(jobs);
		}
		incomingRepository.saveAll(joblist);
		return "Job Updated Succesfully";
	}

	@Override
	public List<String> fetchReturnsBroker() {
		List<String> brokerReturns= returnsRepository.fetchReturnsBroker();
		return brokerReturns;
	}

	@Override
	public List<Returns> returnsOutstanding(String fromDate, String toDate, String brokerName) {
		List<Returns> returnDetails = null;
		if(Strings.isNullOrEmpty(fromDate) && Strings.isNullOrEmpty(toDate)) {
			returnDetails = returnsRepository.returnsOutstandingDetails(fromDate,toDate,brokerName);
		}else {
			returnDetails = returnsRepository.returnsOutstandingDetailsBroker(brokerName);
		}
		return returnDetails;
	}

	@Override
	public List<IncomingJobResponse> getClosedJobList() {
			List<IncomingJobs> js = (List<IncomingJobs>)incomingRepository.fetchJobs();
			List<IncomingJobResponse> joblist = new ArrayList<IncomingJobResponse>();
			for(IncomingJobs job :js){
				IncomingJobResponse jobs = new IncomingJobResponse();
				System.out.println("ATA:"+job.getAta());
				jobs.setJobid(job.getID());
				if(job.getEta()!=null){
					jobs.setEta(job.getEta().toString());
				}
				if(job.getAta()!=null){
					jobs.setAta(job.getAta().toString());
				}
				jobs.setBroker(job.getBroker());
				jobs.setClear(job.getClear());
				jobs.setConsignee(job.getConsignee());
				jobs.setDestination(job.getDestination());
				jobs.setFlight(job.getFlight());
				jobs.setHawb(job.getHawb());
				jobs.setHeld(job.getHeld());
				jobs.setMawb(job.getMawb());
				jobs.setMLID(job.getMLID());
				jobs.setNote(job.getNote());
				jobs.setOutturn(job.getOutturn());
				jobs.setPiece(job.getPiece());
				jobs.setWeight(job.getWeight());
				joblist.add(jobs);
			}
			return  joblist;
		}

	@Override
	public String deleteJob(List<IncomingJobResponse> js) {
		List<IncomingJobs> joblist =  new ArrayList<IncomingJobs>();
		for(IncomingJobResponse job :js){
			IncomingJobs jobs = new IncomingJobs();
			System.out.println("jkk"+job.getBroker()+"::"+job.getEta()+job.getClear());
			jobs.setBroker(job.getBroker());
			jobs.setClear(job.getClear());
			jobs.setConsignee(job.getConsignee());
			jobs.setDestination(job.getDestination());
			jobs.setFlight(job.getFlight());
			jobs.setHawb(job.getHawb());
			jobs.setHeld(job.getHeld());
			jobs.setMawb(job.getMawb());
			jobs.setMLID(job.getMLID());
			jobs.setNote(job.getNote());
			if(job.getEta()!=null && job.getEta().length() > 0)
			{
						LocalDate date1  = LocalDate.parse(job.getEta());
						jobs.setEta(date1);
						
			}
			if(job.getAta()!=null &&  job.getAta().length() > 0)
			{
						
						if(job.getAta().contains("T04:"))
						{
						 LocalDate date2 = LocalDate.parse(job.getAta(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
						 jobs.setAta(date2);
						}
						else
						{LocalDate date2  = LocalDate.parse(job.getAta());
						jobs.setAta(date2);
						}
			}
			jobs.setPiece(job.getPiece());
			jobs.setWeight(job.getWeight());
			jobs.setID(job.getJobid());
		
			if(job.getOutturn()!=null && (job.getOutturn().equalsIgnoreCase("True") || job.getOutturn().equalsIgnoreCase("Y"))){
				System.out.print("Outturn;"+job.getOutturn());	
				jobs.setOutturn("Y");
			}
		jobs.setHeld(job.getHeld());
		jobs.setIsDeleted("Y");
		joblist.add(jobs);
		}
		incomingRepository.saveAll(joblist);
		return "Job Deleted Succesfully";
	}

	@Override
	public List<Object> exportConsignmentsfile(String Type, List<String> Data) {
		List<Object> exportedConsignments;
		if(Type.equals("articleid")){
			exportedConsignments = senderDataRepository.exportConsignmentsArticleid(Data);
		}
		else if (Type.equals("barcodelabel")){
			Data.forEach(System.out::print);
			exportedConsignments = senderDataRepository.exportConsignmentsBarcode(Data);
		}else{
			System.out.print("in else");
			exportedConsignments = senderDataRepository.exportConsignmentsRef(Data);
		}
		
		
		System.out.println(exportedConsignments.size());
		return exportedConsignments;
	}

	@Override
	public List<Object> exportShipmentfile(String Type, List<String> Data) {
		// TODO Auto-generated method stub
		
		List<Object> exportedShipment ;
		
		if(Type.equals("articleid"))
		{
			exportedShipment = senderDataRepository.exportShipmentArticleid(Data);
		}
		else if (Type.equals("barcodelabel"))
				{
			exportedShipment = senderDataRepository.exportShipmentBarcode(Data);
				}
			
			
		else
		{
			System.out.print("in else");
			exportedShipment = senderDataRepository.exportShipmentRef(Data);
		}
				
		System.out.println(exportedShipment.size());
		return exportedShipment;
		
	}


	@Override
	public String submitJob(List<IncomingJobResponse> Job) {
		List<IncomingJobs> joblist =  new ArrayList<IncomingJobs>();
		for(IncomingJobResponse job :Job){
			IncomingJobs jobs = new IncomingJobs();
			System.out.println("jkk"+job.getBroker()+"::"+job.getEta()+job.getClear());
			jobs.setBroker(job.getBroker());
			jobs.setClear(job.getClear());
			jobs.setConsignee(job.getConsignee());
			jobs.setDestination(job.getDestination());
			jobs.setFlight(job.getFlight());
			jobs.setHawb(job.getHawb());
			jobs.setHeld(job.getHeld());
			jobs.setMawb(job.getMawb());
			jobs.setMLID(job.getMLID());
			jobs.setNote(job.getNote());
			if(job.getEta()!=null && job.getEta().length() > 0){
						LocalDate date1  = LocalDate.parse(job.getEta());
						jobs.setEta(date1);
			}
			if(job.getAta()!=null &&  job.getAta().length() > 0){
						if(job.getAta().contains("T04:")){
						 LocalDate date2 = LocalDate.parse(job.getAta(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
						 jobs.setAta(date2);
						}
						else{
						LocalDate date2  = LocalDate.parse(job.getAta());
						jobs.setAta(date2);
						}
			}
			jobs.setPiece(job.getPiece());
			jobs.setWeight(job.getWeight());
			jobs.setID(job.getJobid());
			if(job.getOutturn()!=null && (job.getOutturn().equalsIgnoreCase("True") || job.getOutturn().equalsIgnoreCase("Y"))){
				System.out.print("Outturn;"+job.getOutturn());	
			jobs.setOutturn("Y");
			}
		jobs.setHeld(job.getHeld());
		jobs.setIsSubmitted("Y");
		jobs.setIsDeleted("N");
		joblist.add(jobs);
		}
		incomingRepository.saveAll(joblist);
		return "Job Submitted Succesfully";
	}
		
	@Override
	public List<CSTickets> fetchCSTickets() {
		return csticketsRepository.fetchCSTicketDetails();
	}

	@Override
	public ResponseMessage updateAUCSTrackingDetails(TrackingResponse auTrackingDetails) {
		List<TrackingResults> trackingData = auTrackingDetails.getTracking_results();
		ResponseMessage responseMsg =  new ResponseMessage();
		if(trackingData == null) {
			responseMsg.setResponseMessage("No Tracking Info from AuPost");
		}else {
			for(TrackingResults data : trackingData ) {
				if(data!=null && data.getTrackable_items()!=null) {
					for(TrackableItems trackingLabel : data.getTrackable_items()) {
						if(trackingLabel != null && trackingLabel.getEvents() != null && !trackingLabel.getEvents().isEmpty()) {
							DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
							DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
							Date date = null;
							try {
								date = inputFormat.parse(trackingLabel.getEvents().get(0).getDate());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							String eventDateOccuredAuPost = outputFormat.format(date);
							System.out.println("AU Post Response Event Date--->"+eventDateOccuredAuPost);
							csticketsRepository.updateAUCSTrackingDetails(trackingLabel.getArticle_id(), trackingLabel.getEvents().get(0).getDescription(), trackingLabel.getStatus(), Timestamp.valueOf(eventDateOccuredAuPost));
						}
					}
				}
			}
			responseMsg.setResponseMessage("Data uploaded successfully from AU Post");
		}
		return responseMsg;
	 }

	@Override
	public ResponseMessage updateAUEtowerTrackingDetails(TrackingEventResponse trackEventresponse) {
		List<TrackEventResponseData> responseData = trackEventresponse.getData();
		ResponseMessage responseMsg = new ResponseMessage();
		if (responseData == null) {
			responseMsg.setResponseMessage("No Data from ETower");
		} else {
			for (TrackEventResponseData data : responseData) {
				if (data != null && data.getEvents() != null && !data.getEvents().isEmpty()) {
					DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
				    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
				    Date date = null;
					try {
						date = inputFormat.parse(data.getEvents().get(0).getEventTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				    String eventDateOccured = outputFormat.format(date);
				    System.out.println("Etower Response Event Date--->"+eventDateOccured);
					csticketsRepository.updateAUCSTrackingDetails(data.getEvents().get(0).getTrackingNo(), data.getEvents().get(0).getActivity(), data.getStatus(), Timestamp.valueOf(eventDateOccured));
				}
			}
			responseMsg.setResponseMessage("Data uploaded successfully from ETower");
		}
		return responseMsg;
	}

	@Override
	public String fetchBarcodeLabel(String articleID) {
		return senderDataRepository.fetchBarcodeDetails(articleID);
	}

	@Override
	public ResponseMessage updatePFLTrackingDetails(List<PFLTrackingResponseDetails> pflTrackingDetails) {
		ResponseMessage responseMsg = new ResponseMessage();
		if(pflTrackingDetails.isEmpty()) {
			responseMsg.setResponseMessage("No Data from PFL Response");
		}else {
			for(PFLTrackingResponseDetails data:pflTrackingDetails) {
				csticketsRepository.updatePFLCSTrackingDetails(data.getBarcodeLabel(), data.getStatus(), data.getStatus_code(), Timestamp.valueOf(data.getDate()));
			}
			responseMsg.setResponseMessage("Data uploaded successfully from PFL");
		}
		return responseMsg;
	}

	@Override
	public List<Returns> returnsOutstanding() {
		return returnsRepository.returnsOutstanding();
	}

	@Override
	public UserMessage updateAction(List<ReturnsAction> returnsAction) {
		for(ReturnsAction actionRequest:returnsAction) {
			returnsRepository.updateReturnStatus(actionRequest.getArticleId());
		}
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Return Action Updated Successfully");
		return usrMsg;
	}

	@Override
	public UserMessage uploadweight(List<WeightUpload> weight) {
		// TODO Auto-generated method stub
		System.out.println("before for:"+System.currentTimeMillis());
		for(WeightUpload wei : weight)
		{
			Double weig = (Double.parseDouble(wei.getWeight()));
			senderDataRepository.updateweight(weig, wei.getArticleid());
			senderdata_InvoicingRepository.updateinvoicingweight(weig, wei.getArticleid());
		}
		System.out.println("after for:"+System.currentTimeMillis());
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Weight Updated Successfully");
		return usrMsg;
	}

	@Override
	public List<SenderdataMaster> fetchConsignmentsByRefNbr(List<String> refNbrs) {
		return senderDataRepository.fetchConsignmentsByRefNbr(refNbrs);
	}

	@Override
	public List<String> fetchRefnobyArticle(List<String> articleid) {
		// TODO Auto-generated method stub
		List<String> refnbrs = senderDataRepository.fetchreferencenumberforArticleid(articleid);
		return refnbrs;
	}

	@Override
	public String allocateShipment(String referenceNumbers, String shipmentNumber) {
		// TODO Auto-generated method stub
		senderDataRepository.updateAirwayBill(referenceNumbers.split(","), shipmentNumber,D2ZCommonUtil.getAETCurrentTimestamp());
		senderDataRepository.allocateShipment(referenceNumbers, shipmentNumber);
		return "Shipment Allocated Successfully";
	}

	@Override
	public List<SenderdataMaster> fetchDataBasedonSupplier(List<String> incomingRefNbr, String supplier) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchDataBasedonSupplier(incomingRefNbr, supplier);
	}

	@Override
	public List<String> fetchDataforPFLSubmitOrder(String[] refNbrs) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchDataforPFLSubmitOrder(refNbrs);
	}

	@Override
	public List<String> fetchDataForEtowerForeCastCall(String[] refNbrs) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchDataForEtowerForeCastCall(refNbrs);
	}

	@Override
	public String updateinvoicing(String toAllocate, String shipmentNumber) {
		// TODO Auto-generated method stub
		System.out.println("airway"+shipmentNumber);
		int count = senderdata_InvoicingRepository.updateinvoicingairway(shipmentNumber,toAllocate.split(","));
		System.out.println("updated"+count);
		//senderdata_InvoicingRepository.selectinvoicing(toAllocate.split(","));
		return "Updated Succesfully";
	}


	@Override
	public String createParcel(List<HeldParcel> createJob) {
		List<Parcels> parcelist = new ArrayList<Parcels>();
		for(HeldParcel jobRequest :createJob){
			Parcels p = new Parcels();
			p.setHawb(jobRequest.getHawb());
			p.setMawb(jobRequest.getMawb());
			p.setNote(jobRequest.getNote());
			p.setStatus(jobRequest.getStat());
			p.setOutput("C");
			p.setClient(jobRequest.getClient());
			p.setPod(jobRequest.getPod());
			parcelist.add(p);
		}
		System.out.println("size:"+parcelist.size());
		parcelRepository.saveAll(parcelist);
		return "Parcel created Successfully";
	}

	@Override
	public List<ParcelResponse> getParcelList(String client) {
		List<Parcels> js = (List<Parcels>)parcelRepository.fetchheldparcel(client);
		List<ParcelResponse> parcellist = new ArrayList<ParcelResponse>();
		for(Parcels par : js){
			ParcelResponse p = new ParcelResponse();
			p.setParcelid(par.getID());
			p.setHawb(par.getHawb());
			p.setMawb(par.getMawb());
			p.setNote(par.getNote());
			DropDownModel model = new DropDownModel();
			model.setName(par.getStatus());
			model.setValue(par.getStatus());
			p.setStat(model);
			DropDownModel model1 = new DropDownModel();
			model1.setName(par.getClient());
			model1.setValue(par.getClient());
			p.setClient(model1);
			DropDownModel model2 = new DropDownModel();
			model2.setName(par.getPod());
			model2.setValue(par.getPod());
			p.setPod(model2);
			
			parcellist.add(p);
		}
		// TODO Auto-generated method stub
		return parcellist;
	}

	@Override
	public String updateParcel(List<ParcelResponse> parcel) {
		List<Parcels> js = new ArrayList<Parcels>();
		String msg = "";
	for(ParcelResponse p : parcel){
		Parcels par = parcelRepository.findByHAWB(p.getHawb());
		if(null!=par) {
		par.setHawb(p.getHawb());
		par.setMawb(p.getMawb());
		par.setNote(p.getNote());
		par.setOutput(p.getOutput());
		DropDownModel model = p.getStat();
		par.setStatus(model.getName());
		DropDownModel model1 = p.getClient();
		DropDownModel model2 = p.getPod();
		par.setClient(model1.getName());
		par.setPod(model2.getName());
		//par.setID(p.getParcelid());
		js.add(par);
		}
	}
	parcelRepository.saveAll(js);
	if(parcel.get(0).getOutput().equalsIgnoreCase("C")){
		msg = "Updated Successfully";
	}
	else if(parcel.get(0).getOutput().equalsIgnoreCase("D")){
		msg = "Deleted Successfully";
	}
	else{ msg = "Exported Successfully";
		
	}
		return msg;
	}

	@Override
	public List<ParcelResponse> getParcelReleaseList(String client) {
		// TODO Auto-generated method stub
		List<Parcels> js = (List<Parcels>)parcelRepository.fetchreleaseparcel(client);
		List<ParcelResponse> parcellist = new ArrayList<ParcelResponse>();
		for(Parcels par : js){
			ParcelResponse p = new ParcelResponse();
			p.setParcelid(par.getID());
			p.setHawb(par.getHawb());
			p.setMawb(par.getMawb());
			p.setNote(par.getNote());
			DropDownModel model = new DropDownModel();
			model.setName(par.getStatus());
			model.setValue(par.getStatus());
			p.setStat(model);
			DropDownModel model1 = new DropDownModel();
			model1.setName(par.getClient());
			model1.setValue(par.getClient());
			p.setClient(model1);
			DropDownModel model2 = new DropDownModel();
			model2.setName(par.getPod());
			model2.setValue(par.getPod());
			p.setPod(model2);
			parcellist.add(p);
		}
		return parcellist;
	}
	
	public void updateReturnInvoice(Returns returnVal) {
		senderdata_InvoicingRepository.updateReturnInvoice("Return-"+D2ZCommonUtil.getday(),"RES", returnVal.getArticleId());
	}

	@Override
	public List<ShipmentCharges> shipmentCharges() {
		List<IncomingJobs> incomingJob = incomingRepository.shipmentCharges();
		List<ShipmentCharges> shipmentChargesList = new ArrayList<ShipmentCharges>();
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		for(IncomingJobs incomingJobDetails:incomingJob){
			ShipmentCharges shipmemntCharge = new ShipmentCharges();
			shipmemntCharge.setBroker(incomingJobDetails.getBroker());
			shipmemntCharge.setConsignee(incomingJobDetails.getConsignee());
			shipmemntCharge.setMawb(incomingJobDetails.getMawb());
			shipmemntCharge.setPod(incomingJobDetails.getDestination());
			shipmemntCharge.setPcs(incomingJobDetails.getPiece());
			shipmemntCharge.setWeight(incomingJobDetails.getWeight());
			shipmemntCharge.setHawb(incomingJobDetails.getHawb());
			
//			if(incomingJobDetails.getBroker().equalsIgnoreCase("NEXB") && incomingJobDetails.getConsignee().equalsIgnoreCase("BLUE")){
//				Double processNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.22);
//				shipmemntCharge.setProcess(Double.valueOf(twoDForm.format(processNexb)));
//				Double pickUpCharge = (Double.valueOf(incomingJobDetails.getWeight())*(0.175)) > 90.75 ? (Double.valueOf(incomingJobDetails.getWeight())*(0.175)) : 90.75;
//				shipmemntCharge.setPickUp(Double.valueOf(twoDForm.format(pickUpCharge)));
//				shipmemntCharge.setDocs(57.75);
//				Double airportChargeNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.5775);
//				shipmemntCharge.setAirport(Double.valueOf(twoDForm.format(airportChargeNexb)));
//				Double totalNexb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
//				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalNexb)));
//			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("VELB") && incomingJobDetails.getConsignee().equalsIgnoreCase("BLUE")){
//				shipmemntCharge.setProcess((double) 0);
//				Double pickUpChargeVelb = (Double.valueOf(incomingJobDetails.getWeight())*(0.15)) > 70 ? (Double.valueOf(incomingJobDetails.getWeight())*(0.15)) : 70;
//				shipmemntCharge.setPickUp(Double.valueOf(twoDForm.format(pickUpChargeVelb)));
//				shipmemntCharge.setDocs(60.00);
//				Double airportChargeVelb = Double.valueOf(incomingJobDetails.getWeight())*(0.60);
//				shipmemntCharge.setAirport(Double.valueOf(twoDForm.format(airportChargeVelb)));
//				Double totalVelb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
//				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalVelb)));
//			}
//			if(incomingJobDetails.getBroker().equalsIgnoreCase("VELB") && incomingJobDetails.getConsignee().equalsIgnoreCase("PCA")) {
//				Double processNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.22);
//				shipmemntCharge.setProcess(Double.valueOf(twoDForm.format(processNexb)));
//				Double pickUpCharge = (Double.valueOf(incomingJobDetails.getWeight())*(0.175)) > 90.75 ? (Double.valueOf(incomingJobDetails.getWeight())*(0.175)) : 90.75;
//				shipmemntCharge.setPickUp(Double.valueOf(twoDForm.format(pickUpCharge)));
//				shipmemntCharge.setDocs(57.75);
//				Double airportChargeNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.5775);
//				shipmemntCharge.setAirport(Double.valueOf(twoDForm.format(airportChargeNexb)));
//				Double totalNexb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
//				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalNexb)));
//			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("VELB") && incomingJobDetails.getConsignee().equalsIgnoreCase("AMI")) {
//				shipmemntCharge.setProcess((double) 0);
//				shipmemntCharge.setPickUp((double) 160);
//				shipmemntCharge.setDocs((double) 60);
//				shipmemntCharge.setAirport((double) 60);
//				Double totalRmfb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
//				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalRmfb)));
//			}
			
			if(incomingJobDetails.getBroker().equalsIgnoreCase("VELB") && incomingJobDetails.getConsignee().equalsIgnoreCase("PCA") ) {
				shipmemntCharge.setProcess((double) 0);
				shipmemntCharge.setPickUp((double)70);
				shipmemntCharge.setDocs((double)60);
				Double airportChargeNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.6);
				shipmemntCharge.setAirport(Double.valueOf(twoDForm.format(airportChargeNexb)));
				Double totalNexb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalNexb)));
			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("VELB") && incomingJobDetails.getConsignee().equalsIgnoreCase("AMI")) {
				shipmemntCharge.setProcess((double) 0);
				shipmemntCharge.setPickUp((double) 160);
				shipmemntCharge.setDocs((double) 0);
				shipmemntCharge.setAirport((double) 60);
				Double totalRmfb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalRmfb)));
			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("RMFB") && incomingJobDetails.getConsignee().equalsIgnoreCase("D2Z")) {
				shipmemntCharge.setProcess((double)25);
				shipmemntCharge.setPickUp((double) 0);
				shipmemntCharge.setDocs((double) 0);
				shipmemntCharge.setAirport((double) 0);
				Double totalRmfb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalRmfb)));
			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("GPXB") && incomingJobDetails.getConsignee().equalsIgnoreCase("PCA")) {
				Double processNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.88);
				shipmemntCharge.setProcess(Double.valueOf(twoDForm.format(processNexb)));
				shipmemntCharge.setPickUp((double) 0);
				shipmemntCharge.setDocs((double) 60);
				shipmemntCharge.setAirport((double) 0);
				Double totalNexb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalNexb)));
			}else if(incomingJobDetails.getBroker().equalsIgnoreCase("5ULB") && incomingJobDetails.getConsignee().equalsIgnoreCase("PFL")) {
				Double processNexb = Double.valueOf(incomingJobDetails.getWeight())*(0.85);
				shipmemntCharge.setProcess(Double.valueOf(twoDForm.format(processNexb)));
				shipmemntCharge.setPickUp((double) 0);
				shipmemntCharge.setDocs((double) 60);
				shipmemntCharge.setAirport((double) 0);
				Double totalNexb = shipmemntCharge.getProcess() + shipmemntCharge.getPickUp() + shipmemntCharge.getDocs() + shipmemntCharge.getAirport();
				shipmemntCharge.setTotal(Double.valueOf(twoDForm.format(totalNexb)));
			}
			shipmentChargesList.add(shipmemntCharge);
		}
		return shipmentChargesList;
	}

	@Override
	public List<User> broker() {
		return userRepository.broker();
	}

	@Override
	public Zone zoneReport(List<ZoneRequest> zoneRequest) {
		List<ZoneResponse> zoneResponseList = new ArrayList<ZoneResponse>();
		Zone zoneFinal = new Zone();
		for(ZoneRequest zoneObj:zoneRequest) {
			List<Integer> listOfClientId = d2zBrokerDao.getClientId(zoneObj.getUserId());
			Map<String, ZoneReport> zoneMap = new HashMap<String, ZoneReport>();
			Map<String, ZoneReport> categoryMap = new HashMap<String, ZoneReport>();
			if(listOfClientId.size() > 0) {
				List<String> zoneData = senderdata_InvoicingRepository.zoneReport(listOfClientId, zoneObj.getFromDate(), zoneObj.getToDate());
				if(zoneData.size() > 0) {
					Iterator itr = zoneData.iterator();
					while (itr.hasNext()) {
						Object[] obj = (Object[]) itr.next();
						ZoneReport zone = new ZoneReport();
						zone.setZone(obj[0].toString());
						zone.setCategory(obj[1].toString());
						zone.setCategoryVal((int) obj[2]);
						zone.setZoneSumVal( (int) obj[3]);
						zone.setTotal( (int) obj[4]);
						zone.setZonePerc(new Double((double) obj[5]).floatValue());
						zone.setCatSumVal( (int) obj[6]);
						zone.setCatPerc(new Double((double) obj[7]).floatValue());
						zoneMap.put(obj[0].toString()+"-"+obj[1].toString(), zone);
						categoryMap.put(obj[1].toString(), zone);
					}
				}
			}
			
			if(zoneMap.size() > 0) {
				zoneMap.forEach((k,v)->{
					boolean zoneCheck = zoneResponseList.stream().
									filter(o -> o.getZone().equals(k.substring(0, k.indexOf("-")))).findFirst().isPresent();
					if(zoneCheck) {
						Optional<ZoneResponse> matchingObject = zoneResponseList.stream().
									filter(p -> p.getZone().equals(k.substring(0, k.indexOf("-")))).findFirst();
						ZoneResponse zone = matchingObject.get();
						if(v.getCategory().contains("category1"))
							zone.setCategory1(v.getCategoryVal());
						else if(v.getCategory().contains("category2"))
							zone.setCategory2(v.getCategoryVal());
						else if(v.getCategory().contains("category3"))
							zone.setCategory3(v.getCategoryVal());
						else if(v.getCategory().contains("category4"))
							zone.setCategory4(v.getCategoryVal());
						else if(v.getCategory().contains("category5"))
							zone.setCategory5(v.getCategoryVal());
						else if(v.getCategory().contains("category6"))
							zone.setCategory6(v.getCategoryVal());
						else if(v.getCategory().contains("category7"))
							zone.setCategory7(v.getCategoryVal());
						else if(v.getCategory().contains("category8"))
							zone.setCategory8(v.getCategoryVal());
						else if(v.getCategory().contains("category9"))
							zone.setCategory9(v.getCategoryVal());
						else if(v.getCategory().contains("category10"))
							zone.setCategory10(v.getCategoryVal());
					}else {
						ZoneResponse zoneResponse = new ZoneResponse();
						zoneResponse.setZone(k.substring(0, k.indexOf("-")));
						if(v.getCategory().contains("category1"))
							zoneResponse.setCategory1(v.getCategoryVal());
						else if(v.getCategory().contains("category2"))
							zoneResponse.setCategory2(v.getCategoryVal());
						else if(v.getCategory().contains("category3"))
							zoneResponse.setCategory3(v.getCategoryVal());
						else if(v.getCategory().contains("category4"))
							zoneResponse.setCategory4(v.getCategoryVal());
						else if(v.getCategory().contains("category5"))
							zoneResponse.setCategory5(v.getCategoryVal());
						else if(v.getCategory().contains("category6"))
							zoneResponse.setCategory6(v.getCategoryVal());
						else if(v.getCategory().contains("category7"))
							zoneResponse.setCategory7(v.getCategoryVal());
						else if(v.getCategory().contains("category8"))
							zoneResponse.setCategory8(v.getCategoryVal());
						else if(v.getCategory().contains("category9"))
							zoneResponse.setCategory9(v.getCategoryVal());
						else if(v.getCategory().contains("category10"))
							zoneResponse.setCategory10(v.getCategoryVal());
						zoneResponse.setTotalCnt(v.getZoneSumVal());
						zoneResponse.setZonePerctange(v.getZonePerc());
						zoneResponse.setTotal(v.getTotal());
						zoneResponseList.add(zoneResponse);
					}
				}
			  );
			}
		 
		if(categoryMap.size() > 0) {
			 ZoneResponse categoryResponse = new ZoneResponse();
			  categoryMap.forEach((key,val)->{
				  categoryResponse.setZone("Total");
				  if(key.contains("category1"))
					  categoryResponse.setCategory1(val.getCatSumVal());
				  else if(key.contains("category2"))
					  categoryResponse.setCategory2(val.getCatSumVal());
				  else if(key.contains("category3"))
					  categoryResponse.setCategory3(val.getCatSumVal());
				  else if(key.contains("category4"))
					  categoryResponse.setCategory4(val.getCatSumVal());
				  else if(key.contains("category5"))
					  categoryResponse.setCategory5(val.getCatSumVal());
				  else if(key.contains("category6"))
					  categoryResponse.setCategory6(val.getCatSumVal());
				  else if(key.contains("category7"))
					  categoryResponse.setCategory7(val.getCatSumVal());
				  else if(key.contains("category8"))
					  categoryResponse.setCategory8(val.getCatSumVal());
				  else if(key.contains("category9"))
					  categoryResponse.setCategory9(val.getCatSumVal());
				  else if(key.contains("category10"))
					  categoryResponse.setCategory10(val.getCatSumVal());
				  categoryResponse.setTotal(val.getTotal());
			  });
			  zoneResponseList.add(categoryResponse);
			  zoneFinal.setZoneResponse(zoneResponseList);
			  CategoryResponse categoryPercentage = new CategoryResponse();
			  categoryMap.forEach((key,val)->{
				  categoryPercentage.setZone("%");
				  if(key.contains("category1"))
					  categoryPercentage.setCategory1(val.getCatPerc());
				  else if(key.contains("category2"))
					  categoryPercentage.setCategory2(val.getCatPerc());
				  else if(key.contains("category3"))
					  categoryPercentage.setCategory3(val.getCatPerc());
				  else if(key.contains("category4"))
					  categoryPercentage.setCategory4(val.getCatPerc());
				  else if(key.contains("category5"))
					  categoryPercentage.setCategory5(val.getCatPerc());
				  else if(key.contains("category6"))
					  categoryPercentage.setCategory6(val.getCatPerc());
				  else if(key.contains("category7"))
					  categoryPercentage.setCategory7(val.getCatPerc());
				  else if(key.contains("category8"))
					  categoryPercentage.setCategory8(val.getCatPerc());
				  else if(key.contains("category9"))
					  categoryPercentage.setCategory9(val.getCatPerc());
				  else if(key.contains("category10"))
					  categoryPercentage.setCategory10(val.getCatPerc());
			  });
			  zoneFinal.setCategoryResponse(categoryPercentage);
			}
		}
	  return zoneFinal;
	}

	@Override
	public void updateAirwayBill(String referenceNumbers, String shipmentNumber) {
		 senderDataRepository.updateAirwayBill(referenceNumbers.split(","), shipmentNumber,D2ZCommonUtil.getAETCurrentTimestamp());
		
	}

	public UserMessage approveShiment(List<ShipmentApproval> shipmentApproval) {
		for (ShipmentApproval shipment : shipmentApproval) {
			incomingRepository.approveShiment(shipment.getMawb());
		}
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Shipment Charges updated Successfully");
		return userMsg;
	}
	
	@Override
	public String fetchServiceTypeByRefNbr(String refNbr) {
		return senderDataRepository.fetchServiceTypeByRefNbr(refNbr);
	}

	@Override

	public String fetchServiceTypeByMlid(String mlid) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchServiceTypeByMlid(mlid);
	}
	public List<ProfitLossReport> profitLossReport(String fromDate,String toDate) {
		//List<String> brokerList = incomingJobsLogicRepository.getIncomeBrokerList();
		List<String> brokerProfit = senderdata_InvoicingRepository.getBrokerProfitDetails(fromDate,toDate);
		List<ProfitLossReport> profitLossReport = new ArrayList<ProfitLossReport>();
		if(brokerProfit.size() > 0) {
			Iterator itr = brokerProfit.iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				ProfitLossReport profit = new ProfitLossReport();
				profit.setBroker(obj[0].toString());
				profit.setParcel( (int) obj[1]);
				profit.setRevenue((BigDecimal) obj[2]);
				profit.setBrokerRate((BigDecimal) obj[2]);
				profit.setD2zRate((BigDecimal) obj[3]);
				profit.setProfit(profit.getBrokerRate().subtract(profit.getD2zRate()));
				profit.setProfitPerParcel(profit.getProfit().divide(new BigDecimal(profit.getParcel()), 4));
				profitLossReport.add(profit);
			}
		}
		
		/*Supplier Profit and loss Report*/
		List<Senderdata_Invoicing> supplierDetila = senderdata_InvoicingRepository.getSupplierDetails(fromDate,toDate);
		List<ProfitLossReport> pflProfitList = new ArrayList<ProfitLossReport>();
		List<ProfitLossReport> pcaProfitList = new ArrayList<ProfitLossReport>();
		List<ProfitLossReport> ubiProfitList = new ArrayList<ProfitLossReport>();
		List<ProfitLossReport> fdmProfitList = new ArrayList<ProfitLossReport>();
		List<String> etowerMlid = consigneeCountRepository.getMlidBasedonSupplier("etower");
		List<String> fdmMlid = consigneeCountRepository.getMlidBasedonSupplier("FDM");
		supplierDetila.forEach(obj -> {
			if((obj.getArticleId().length() == 12) && (obj.getServicetype().equalsIgnoreCase("FWM") || obj.getServicetype().equalsIgnoreCase("MCM") ||
					obj.getServicetype().equalsIgnoreCase("FW"))) {
				ProfitLossReport pflProfit = new ProfitLossReport();
				pflProfit.setArticleId(obj.getArticleId());
				pflProfit.setBrokerRate(obj.getBrokerRate());
				pflProfit.setD2zRate(obj.getD2zRate());
				pflProfitList.add(pflProfit);
			}else if((obj.getArticleId().length() == 12) && (obj.getServicetype().equalsIgnoreCase("FWS") || obj.getServicetype().equalsIgnoreCase("MCS") )) {
				ProfitLossReport pcaProfit = new ProfitLossReport();
				pcaProfit.setArticleId(obj.getArticleId());
				pcaProfit.setBrokerRate(obj.getBrokerRate());
				pcaProfit.setD2zRate(obj.getD2zRate());
				pcaProfitList.add(pcaProfit);
			}else if((obj.getArticleId().length() == 21) && (etowerMlid.contains(obj.getArticleId().substring(0, 3)))) {
				ProfitLossReport ubiProfit = new ProfitLossReport();
				ubiProfit.setArticleId(obj.getArticleId());
				ubiProfit.setBrokerRate(obj.getBrokerRate());
				ubiProfit.setD2zRate(obj.getD2zRate());
				ubiProfitList.add(ubiProfit);
			}else if((obj.getArticleId().length() == 23) && (etowerMlid.contains(obj.getArticleId().substring(0, 5)))) {
				ProfitLossReport ubiProfit = new ProfitLossReport();
				ubiProfit.setArticleId(obj.getArticleId());
				ubiProfit.setBrokerRate(obj.getBrokerRate());
				ubiProfit.setD2zRate(obj.getD2zRate());
				ubiProfitList.add(ubiProfit);
			}else if((obj.getArticleId().length() == 23) && (fdmMlid.contains(obj.getArticleId().substring(0, 5)))) {
				ProfitLossReport fdmProfit = new ProfitLossReport();
				fdmProfit.setArticleId(obj.getArticleId());
				fdmProfit.setBrokerRate(obj.getBrokerRate());
				fdmProfit.setD2zRate(obj.getD2zRate());
				fdmProfitList.add(fdmProfit);
			}
		});
		if(pflProfitList.size() > 0) {
			List<BigDecimal> pflBrkRateList = new ArrayList<BigDecimal>();
			List<BigDecimal> pflD2zRateList = new ArrayList<BigDecimal>();
			pflProfitList.forEach(pflObj -> {
				pflBrkRateList.add(pflObj.getBrokerRate());
				pflD2zRateList.add(pflObj.getD2zRate());
			});
			ProfitLossReport profitPfl = new ProfitLossReport();
			profitPfl.setBroker("PFL");
			BigDecimal pflBrokerRate = pflBrkRateList.stream().filter(pflBrkRat -> pflBrkRat != null).reduce(BigDecimal::add).get();
			BigDecimal pflD2zRate = pflD2zRateList.stream().filter(pflD2zRat -> pflD2zRat != null).reduce(BigDecimal::add).get();
			profitPfl.setBrokerRate(pflBrokerRate);
			profitPfl.setD2zRate(pflD2zRate);
			profitPfl.setParcel(pflProfitList.size());
			profitPfl.setRevenue(profitPfl.getBrokerRate());
			profitPfl.setProfit(profitPfl.getBrokerRate().subtract(profitPfl.getD2zRate()));
			profitPfl.setProfitPerParcel(profitPfl.getProfit().divide(new BigDecimal(profitPfl.getParcel()), 4));
			profitLossReport.add(profitPfl);
		}
		if(pcaProfitList.size() > 0) {
			List<BigDecimal> pcaBrkRateList = new ArrayList<BigDecimal>();
			List<BigDecimal> pcaD2zRateList = new ArrayList<BigDecimal>();
			pcaProfitList.forEach(pcaObj -> {
				pcaBrkRateList.add(pcaObj.getBrokerRate());
				pcaD2zRateList.add(pcaObj.getD2zRate());
			});
			ProfitLossReport profitPca = new ProfitLossReport();
			profitPca.setBroker("PCA");
			BigDecimal pcaBrokerRate = pcaBrkRateList.stream().filter(pcaBrkRat -> pcaBrkRat != null).reduce(BigDecimal::add).get();
			BigDecimal pcaD2zRate = pcaD2zRateList.stream().filter(pcaD2zRat -> pcaD2zRat != null).reduce(BigDecimal::add).get();
			profitPca.setBrokerRate(pcaBrokerRate);
			profitPca.setD2zRate(pcaD2zRate);
			profitPca.setParcel(pcaProfitList.size());
			profitPca.setRevenue(profitPca.getBrokerRate());
			profitPca.setProfit(profitPca.getBrokerRate().subtract(profitPca.getD2zRate()));
			profitPca.setProfitPerParcel(profitPca.getProfit().divide(new BigDecimal(profitPca.getParcel()), 4));
			profitLossReport.add(profitPca);
		}
		if(ubiProfitList.size() > 0) {
			List<BigDecimal> ubiBrkRateList = new ArrayList<BigDecimal>();
			List<BigDecimal> ubiD2zRateList = new ArrayList<BigDecimal>();
			ubiProfitList.forEach(ubiObj -> {
				ubiBrkRateList.add(ubiObj.getBrokerRate());
				ubiD2zRateList.add(ubiObj.getD2zRate());
			});
			ProfitLossReport profitUbi = new ProfitLossReport();
			profitUbi.setBroker("UBI");
			BigDecimal ubiBrokerRate = ubiBrkRateList.stream().filter(ubiBrkRat -> ubiBrkRat != null).reduce(BigDecimal::add).get();
			BigDecimal ubiD2zRate = ubiD2zRateList.stream().filter(ubiD2zRat -> ubiD2zRat != null).reduce(BigDecimal::add).get();
			profitUbi.setBrokerRate(ubiBrokerRate);
			profitUbi.setD2zRate(ubiD2zRate);
			profitUbi.setParcel(ubiProfitList.size());
			profitUbi.setRevenue(profitUbi.getBrokerRate());
			profitUbi.setProfit(profitUbi.getBrokerRate().subtract(profitUbi.getD2zRate()));
			profitUbi.setProfitPerParcel(profitUbi.getProfit().divide(new BigDecimal(profitUbi.getParcel()), 4));
			profitLossReport.add(profitUbi);
		}
		if(fdmProfitList.size() > 0) {
			List<BigDecimal> fdmBrkRateList = new ArrayList<BigDecimal>();
			List<BigDecimal> fdmD2zRateList = new ArrayList<BigDecimal>();
			fdmProfitList.forEach(fdmObj -> {
				fdmBrkRateList.add(fdmObj.getBrokerRate());
				fdmD2zRateList.add(fdmObj.getD2zRate());
			});
			ProfitLossReport profitFdm = new ProfitLossReport();
			profitFdm.setBroker("FDM");
			BigDecimal fdmBrokerRate = fdmBrkRateList.stream().filter(fdmBrkRat -> fdmBrkRat != null).reduce(BigDecimal::add).get();
			BigDecimal fdmD2zRate = fdmD2zRateList.stream().filter(fdmD2zRat -> fdmD2zRat != null).reduce(BigDecimal::add).get();
			profitFdm.setBrokerRate(fdmBrokerRate);
			profitFdm.setD2zRate(fdmD2zRate);
			profitFdm.setParcel(fdmProfitList.size());
			profitFdm.setRevenue(profitFdm.getBrokerRate());
			profitFdm.setProfit(profitFdm.getBrokerRate().subtract(profitFdm.getD2zRate()));
			profitFdm.setProfitPerParcel(profitFdm.getProfit().divide(new BigDecimal(profitFdm.getParcel()), 4));
			profitLossReport.add(profitFdm);
		}
		
		List<BigDecimal> totlaRevList = new ArrayList<BigDecimal>();
		List<BigDecimal> totalProfitList = new ArrayList<BigDecimal>();
		List<Integer> 	 totalParcelList  = new ArrayList<Integer>();
		List<BigDecimal> profitParcelList = new ArrayList<BigDecimal>();
		profitLossReport.forEach(profitObj -> {
			totlaRevList.add(profitObj.getRevenue());
			totalProfitList.add(profitObj.getProfit());
			totalParcelList.add(profitObj.getParcel());
			profitParcelList.add(profitObj.getProfitPerParcel());
		});
		Integer totalParcel    = totalParcelList.stream().reduce(Integer::sum).get();
		BigDecimal totalRev    = totlaRevList.stream().reduce(BigDecimal::add).get();
		BigDecimal totalPrf    = totalProfitList.stream().reduce(BigDecimal::add).get();
		//BigDecimal totalPrfPrl = profitParcelList.stream().reduce(BigDecimal::add).get();
		BigDecimal totalPrfPrl = totalPrf.divide(new BigDecimal(totalParcel), 4);

		ProfitLossReport profitTotal = new ProfitLossReport();
		profitTotal.setBroker("Total");
		profitTotal.setParcel(totalParcel);
		profitTotal.setRevenue(totalRev);
		profitTotal.setProfit(totalPrf);
		profitTotal.setProfitPerParcel(totalPrfPrl);
		profitLossReport.add(profitTotal);
		
		return profitLossReport;
	}

	@Override
	public List<Returns> fetchAllReferenceNumber() {
    	List<Returns> referenceNumbers= (List<Returns>) returnsRepository.findAll();
		return referenceNumbers;
	}

}