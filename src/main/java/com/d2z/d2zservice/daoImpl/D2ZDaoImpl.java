package com.d2z.d2zservice.daoImpl;

import java.awt.Label;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.APIRates;
import com.d2z.d2zservice.entity.AUPostResponse;
import com.d2z.d2zservice.entity.CSTickets;
import com.d2z.d2zservice.entity.Currency;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.entity.EbayResponse;
import com.d2z.d2zservice.entity.FastwayPostcode;
import com.d2z.d2zservice.entity.MasterPostCode;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.Returns;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.StarTrackPostcode;
import com.d2z.d2zservice.entity.SystemRefCount;
import com.d2z.d2zservice.entity.TrackEvents;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.TrackingEvent;
import com.d2z.d2zservice.entity.TransitTime;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.model.ClientDashbaord;
import com.d2z.d2zservice.model.CreateEnquiryRequest;
import com.d2z.d2zservice.model.CurrencyDetails;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.EmailEnquiryDetails;
import com.d2z.d2zservice.model.EmailReturnDetails;
import com.d2z.d2zservice.model.Enquiry;
import com.d2z.d2zservice.model.EnquiryResponse;
import com.d2z.d2zservice.model.EnquiryUpdate;
import com.d2z.d2zservice.model.HeldParcelDetails;
import com.d2z.d2zservice.model.PFLSubmitOrderData;
import com.d2z.d2zservice.model.PerformanceReportTrackingData;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.ReturnsAction;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SuperUserEnquiry;
import com.d2z.d2zservice.model.TrackParcelResponse;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.model.auspost.TrackableItems;
import com.d2z.d2zservice.model.auspost.TrackingEvents;
import com.d2z.d2zservice.model.auspost.TrackingResponse;
import com.d2z.d2zservice.model.auspost.TrackingResults;
import com.d2z.d2zservice.model.etower.ETowerTrackingDetails;
import com.d2z.d2zservice.model.etower.EtowerErrorResponse;
import com.d2z.d2zservice.model.etower.GainLabelsResponse;
import com.d2z.d2zservice.model.etower.LabelData;
import com.d2z.d2zservice.model.etower.TrackEventResponseData;
import com.d2z.d2zservice.model.etower.TrackingEventResponse;
import com.d2z.d2zservice.proxy.CurrencyProxy;
import com.d2z.d2zservice.repository.APIRatesRepository;
import com.d2z.d2zservice.repository.AUPostResponseRepository;
import com.d2z.d2zservice.repository.CSTicketsRepository;
import com.d2z.d2zservice.repository.ConsigneeCountRepository;
import com.d2z.d2zservice.repository.CurrencyRepository;
import com.d2z.d2zservice.repository.ETowerResponseRepository;
import com.d2z.d2zservice.repository.EbayResponseRepository;
import com.d2z.d2zservice.repository.FastwayPostcodeRepository;
import com.d2z.d2zservice.repository.MasterPostcodeRepository;
import com.d2z.d2zservice.repository.NzPostcodesRepository;
import com.d2z.d2zservice.repository.PFLPostcodeRepository;
import com.d2z.d2zservice.repository.ParcelRepository;
import com.d2z.d2zservice.repository.PostcodeZoneRepository;
import com.d2z.d2zservice.repository.ReturnsRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.StarTrackPostcodeRepository;
import com.d2z.d2zservice.repository.SystemRefCountRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.TrackEventsRepository;
import com.d2z.d2zservice.repository.TrackingEventRepository;
import com.d2z.d2zservice.repository.TransitTimeRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.entity.NZPostcodes;
import com.d2z.d2zservice.entity.PFLPostcode;
//import com.d2z.d2zservice.wrapper.FreipostWrapper;
import com.d2z.singleton.D2ZSingleton;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;

@Repository
public class D2ZDaoImpl implements ID2ZDao {

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SenderDataRepository senderDataRepository;

	@Autowired
	SystemRefCountRepository systemRefCountRepository;

	@Autowired
	CSTicketsRepository csticketsRepository;

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;

	@Autowired
	TrackingEventRepository trackingEventRepository;

	@Autowired
	PostcodeZoneRepository postcodeZoneRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserServiceRepository userServiceRepository;

	@Autowired
	EbayResponseRepository ebayResponseRepository;

	@Autowired
	ETowerResponseRepository eTowerResponseRepository;

	@Autowired
	APIRatesRepository apiRatesRepository;

	/*
	 * @Autowired FreipostWrapper freipostWrapper;
	 */

	@Autowired
	AUPostResponseRepository aupostresponseRepository;

	@Autowired
	FastwayPostcodeRepository fastwayPostcodeRepository;

	@Autowired
	StarTrackPostcodeRepository starTrackPostcodeRepository;

	@Autowired
	CurrencyRepository currencyRepository;

	@Autowired
	ConsigneeCountRepository consigneeCountRepository;

	@Autowired
	CurrencyProxy currencyproxy;

	@Autowired
	ReturnsRepository returnsRepository;

	@Autowired
	@Lazy
	private D2ZValidator d2zValidator;

	@Autowired
	TransitTimeRepository transitTimeRepository;

	@Autowired
	ParcelRepository parcelRepository;

	@Autowired
	NzPostcodesRepository nzPostcodesRepository;

	@Autowired
	PFLPostcodeRepository pflPostcodesRepository;

	@Autowired
	MasterPostcodeRepository masterPostcodesRepository;
	
	@Autowired
	TrackEventsRepository trackEventsRepository;

	@Override
	public String exportParcel(List<SenderData> orderDetailList, Map<String, LabelData> barcodeMap) {
		Map<String, String> postCodeStateMap = D2ZSingleton.getInstance().getPostCodeStateMap();
		List<String> incomingRefNbr = new ArrayList<String>();
		LabelData provider = null;
		User userInfo = userRepository.findByUsername(orderDetailList.get(0).getUserName());
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		String fileSeqId = "D2ZUI" + senderDataRepository.fetchNextSeq().toString();
		for (SenderData senderDataValue : orderDetailList) {
			incomingRefNbr.add(senderDataValue.getReferenceNumber());
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(senderDataValue.getReferenceNumber());
			senderDataObj.setConsigneeCompany(senderDataValue.getConsigneeCompany());
			senderDataObj.setConsignee_name(senderDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Suburb(senderDataValue.getConsigneeSuburb().trim());
			senderDataObj.setConsignee_State(senderDataValue.getConsigneeState().trim());
			senderDataObj.setConsignee_Postcode(senderDataValue.getConsigneePostcode().trim());
			senderDataObj.setConsignee_Phone(senderDataValue.getConsigneePhone());
			senderDataObj.setProduct_Description(senderDataValue.getProductDescription());
			senderDataObj.setValue(senderDataValue.getValue());
			senderDataObj.setCurrency(senderDataValue.getCurrency());
			senderDataObj.setShippedQuantity(senderDataValue.getShippedQuantity());
			senderDataObj.setWeight(Double.parseDouble(senderDataValue.getWeight()));
			senderDataObj.setDimensions_Length(senderDataValue.getDimensionsLength());
			senderDataObj.setDimensions_Width(senderDataValue.getDimensionsWidth());
			senderDataObj.setDimensions_Height(senderDataValue.getDimensionsHeight());
			senderDataObj.setServicetype(senderDataValue.getServiceType());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			String shipperName = (senderDataValue.getShipperName() != null
					&& !senderDataValue.getShipperName().isEmpty()) ? senderDataValue.getShipperName()
							: userInfo.getCompanyName();
			senderDataObj.setShipper_Name(shipperName);
			String shipperAddress = (senderDataValue.getShipperAddr1() != null
					&& !senderDataValue.getShipperAddr1().isEmpty()) ? senderDataValue.getShipperAddr1()
							: userInfo.getAddress();
			senderDataObj.setShipper_Addr1(shipperAddress);
			String shipperCity = (senderDataValue.getShipperCity() != null
					&& !senderDataValue.getShipperCity().isEmpty()) ? senderDataValue.getShipperCity()
							: userInfo.getSuburb();
			senderDataObj.setShipper_City(shipperCity);
			String shipperState = (senderDataValue.getShipperState() != null
					&& !senderDataValue.getShipperState().isEmpty()) ? senderDataValue.getShipperState()
							: userInfo.getState();
			senderDataObj.setShipper_State(shipperState);
			String shipperPostcode = (senderDataValue.getShipperPostcode() != null
					&& !senderDataValue.getShipperPostcode().isEmpty()) ? senderDataValue.getShipperPostcode()
							: userInfo.getPostcode();
			senderDataObj.setShipper_Postcode(shipperPostcode);
			String shipperCountry = (senderDataValue.getShipperCountry() != null
					&& !senderDataValue.getShipperCountry().isEmpty()) ? senderDataValue.getShipperCountry()
							: userInfo.getCountry();
			senderDataObj.setShipper_Country(shipperCountry);
			senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataObj.setInnerItem(1);
			senderDataObj.setInjectionType(senderDataValue.getInjectionType());
			senderDataObj.setBagId(senderDataValue.getBagId());
			senderDataObj.setUser_ID(senderDataValue.getUserID());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setDeliveryInstructions((senderDataValue.getDeliveryInstructions() != null
					&& senderDataValue.getDeliveryInstructions().length() > 250)
							? senderDataValue.getDeliveryInstructions().substring(0, 250)
							: senderDataValue.getDeliveryInstructions());
			senderDataObj.setCarrier(senderDataValue.getCarrier());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Email(senderDataValue.getConsigneeEmail());
			senderDataObj.setIsDeleted("N");
			senderDataObj.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
			senderDataObj.setStatus("CONSIGNMENT CREATED");
			senderDataObj.setInjectionType("Direct Injection");
			if ("1PM3E".equalsIgnoreCase(senderDataValue.getServiceType())
					|| "1PME".equalsIgnoreCase(senderDataValue.getServiceType())
					|| "1PSE".equalsIgnoreCase(senderDataValue.getServiceType())) {
				senderDataObj.setCarrier("Express");
			} else {
				senderDataObj.setCarrier("eParcel");
			}
			if (barcodeMap != null && !barcodeMap.isEmpty())
				provider = barcodeMap.get(barcodeMap.keySet().toArray()[0]);

			if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("Etower")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData labelData = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setBarcodelabelNumber(labelData.getBarCode());
				senderDataObj.setArticleId(labelData.getArticleId());
				if (labelData.getBarCode2D().equals(labelData.getArticleId())) {
					senderDataObj.setBarcodelabelNumber(labelData.getArticleId());
					senderDataObj.setDatamatrix(labelData.getArticleId());
					senderDataObj.setCarrier("FastwayNZ");
				} else if (senderDataValue.getServiceType().equalsIgnoreCase("TL1")) {
					senderDataObj.setDatamatrix(labelData.getBarCode2D());
				} else {
					senderDataObj.setDatamatrix(
							D2ZCommonUtil.formatDataMatrix(labelData.getBarCode2D().replaceAll("\\(|\\)|\u001d", "")));
				}

				senderDataObj.setInjectionState(senderDataValue.getInjectionState());
				if ("MCS".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setMlid(senderDataObj.getArticleId().substring(0, 5));
					senderDataObj.setInjectionState("SYD");
				}
			} else if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("PFL")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData pflLabel = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setInjectionState(pflLabel.getHub());
				senderDataObj.setBarcodelabelNumber(pflLabel.getTrackingNo());
				senderDataObj.setArticleId(pflLabel.getTrackingNo());
				senderDataObj.setMlid(pflLabel.getArticleId());
				senderDataObj.setDatamatrix(pflLabel.getMatrix());
				if ("MCS".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setCarrier("Fastway");
				}
				if (!"1PS4".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setCarrier("FastwayM");
				}
				if ("PFL".equalsIgnoreCase(pflLabel.getCarrier())) {
					senderDataObj.setD2zRate(pflLabel.getHeader());
					senderDataObj.setBrokerRate(pflLabel.getFooter());
					senderDataObj.setCarrier(pflLabel.getCarrier());
				}
			} else if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("PCA")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData pflLabel = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setInjectionState(pflLabel.getHub());
				senderDataObj.setBarcodelabelNumber(pflLabel.getTrackingNo());
				senderDataObj.setArticleId(pflLabel.getTrackingNo());
				senderDataObj.setMlid(pflLabel.getArticleId());
				senderDataObj.setDatamatrix(pflLabel.getMatrix());
				senderDataObj.setCarrier(pflLabel.getCarrier());
			}
			senderDataList.add(senderDataObj);
		}
		senderDataRepository.saveAll(senderDataList);
		System.out.println(
				"create consignment UI object construction Done data got inserted--->" + senderDataList.size());
		storProcCall(fileSeqId);
		updateTrackAndTrace(fileSeqId, userInfo.getUser_Id());
		return fileSeqId;
	}

	@Override
	public List<String> fileList(Integer userId) {
		List<String> listOfFileNames = senderDataRepository.fetchFileName(userId);
		return listOfFileNames;
	}

	@Override
	public List<String> labelFileList(Integer userId) {
		List<String> listOfFileNames = senderDataRepository.fetchLabelFileName(userId);
		return listOfFileNames;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> listOfFileNames = senderDataRepository.fetchConsignmentData(fileName);
		return listOfFileNames;
	}

	@Override
	public List<SenderdataMaster> fetchManifestData(String fileName) {
		List<SenderdataMaster> allConsignmentData = senderDataRepository.fetchManifestData(fileName);
		return allConsignmentData;
	}

	@Override
	public String consignmentDelete(String refrenceNumlist) {
		// Calling Delete Store Procedure
		senderDataRepository.consigneeDelete(refrenceNumlist);
		return "Selected Consignments Deleted Successfully";
	}

	@Override
	public List<String> trackingDetails(String fileName) {
		List<String> trackingDetails = senderDataRepository.fetchTrackingDetails(fileName);
		return trackingDetails;
	}

	@Override
	public List<String> trackingLabel(List<String> refBarNum, String identifier) {
		List<String> trackingDetails = new ArrayList<String>();
		if ("articleId".equalsIgnoreCase(identifier)) {
			trackingDetails = senderDataRepository.fetchTrackingLabelByArticleId(refBarNum);
		} else if ("reference_number".equalsIgnoreCase(identifier)) {
			trackingDetails = senderDataRepository.fetchTrackingLabelByReferenceNbr(refBarNum);
		} else {
			trackingDetails = senderDataRepository.fetchTrackingLabel(refBarNum);
		}
		System.out.println(trackingDetails.size());
		return trackingDetails;
	}

	@Override
	public String manifestCreation(String manifestNumber, String[] refrenceNumber) {
		// Calling Delete Store Procedure
		senderDataRepository.manifestCreation(manifestNumber, refrenceNumber);
		return "Manifest Updated Successfully";
	}

	public List<Trackandtrace> trackParcel(String refNbr) {
		List<Trackandtrace> trackAndTrace = trackAndTraceRepository.fetchTrackEventByRefNbr(refNbr);
		return trackAndTrace;
	}

	@Override
	public String createConsignments(List<SenderDataApi> orderDetailList, int userId, String userName,
			Map<String, LabelData> barcodeMap) {
		Map<String, String> postCodeStateMap = D2ZSingleton.getInstance().getPostCodeStateMap();
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		LabelData provider = null;
		User userInfo = userRepository.findByUsername(userName);

		String fileSeqId = "D2ZAPI" + senderDataRepository.fetchNextSeq();
		System.out.println("create consignment API object construction --->" + orderDetailList.size());
		for (SenderDataApi senderDataValue : orderDetailList) {
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setUser_ID(userId);
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(senderDataValue.getReferenceNumber());
			senderDataObj.setConsigneeCompany(senderDataValue.getConsigneeCompany());
			senderDataObj.setConsignee_name(senderDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_addr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsignee_Suburb(senderDataValue.getConsigneeSuburb().trim());
			senderDataObj.setConsignee_State(senderDataValue.getConsigneeState().trim());
			senderDataObj.setConsignee_Postcode(senderDataValue.getConsigneePostcode().trim());
			senderDataObj.setConsignee_Phone(senderDataValue.getConsigneePhone());
			senderDataObj.setProduct_Description(senderDataValue.getProductDescription());
			senderDataObj.setValue(senderDataValue.getValue());
			senderDataObj.setCurrency(senderDataValue.getCurrency());
			senderDataObj.setShippedQuantity(senderDataValue.getShippedQuantity());
			senderDataObj.setWeight(Double.valueOf(senderDataValue.getWeight()));
			senderDataObj.setDimensions_Length(senderDataValue.getDimensionsLength());
			senderDataObj.setDimensions_Width(senderDataValue.getDimensionsWidth());
			senderDataObj.setDimensions_Height(senderDataValue.getDimensionsHeight());
			senderDataObj.setCubic_Weight(senderDataValue.getCubicWeight());
			senderDataObj.setServicetype(senderDataValue.getServiceType());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			String shipperName = (senderDataValue.getShipperName() != null
					&& !senderDataValue.getShipperName().isEmpty()) ? senderDataValue.getShipperName()
							: userInfo.getCompanyName();
			senderDataObj.setShipper_Name(shipperName);
			String shipperAddress = (senderDataValue.getShipperAddr1() != null
					&& !senderDataValue.getShipperAddr1().isEmpty()) ? senderDataValue.getShipperAddr1()
							: userInfo.getAddress();
			senderDataObj.setShipper_Addr1(shipperAddress);
			String shipperCity = (senderDataValue.getShipperCity() != null
					&& !senderDataValue.getShipperCity().isEmpty()) ? senderDataValue.getShipperCity()
							: userInfo.getSuburb();
			senderDataObj.setShipper_City(shipperCity);
			String shipperState = (senderDataValue.getShipperState() != null
					&& !senderDataValue.getShipperState().isEmpty()) ? senderDataValue.getShipperState()
							: userInfo.getState();
			senderDataObj.setShipper_State(shipperState);
			String shipperPostcode = (senderDataValue.getShipperPostcode() != null
					&& !senderDataValue.getShipperPostcode().isEmpty()) ? senderDataValue.getShipperPostcode()
							: userInfo.getPostcode();
			senderDataObj.setShipper_Postcode(shipperPostcode);
			String shipperCountry = (senderDataValue.getShipperCountry() != null
					&& !senderDataValue.getShipperCountry().isEmpty()) ? senderDataValue.getShipperCountry()
							: userInfo.getCountry();
			senderDataObj.setShipper_Country(shipperCountry);
			senderDataObj.setFilename("D2ZAPI" + D2ZCommonUtil.getCurrentTimestamp());
			// senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setDeliveryInstructions((senderDataValue.getDeliveryInstructions() != null
					&& senderDataValue.getDeliveryInstructions().length() > 250)
							? senderDataValue.getDeliveryInstructions().substring(0, 250)
							: senderDataValue.getDeliveryInstructions());
			if (senderDataValue.getBarcodeLabelNumber() != null
					&& !senderDataValue.getBarcodeLabelNumber().trim().isEmpty()
					&& senderDataValue.getDatamatrix() != null && !senderDataValue.getDatamatrix().trim().isEmpty()) {
				senderDataObj.setBarcodelabelNumber(senderDataValue.getBarcodeLabelNumber());
				if (senderDataValue.getBarcodeLabelNumber().length() == 20) {
					senderDataObj.setArticleId(senderDataValue.getBarcodeLabelNumber());
					senderDataObj.setMlid(senderDataValue.getBarcodeLabelNumber());
				} else {
					senderDataObj.setArticleId(senderDataValue.getBarcodeLabelNumber().substring(18));
				}
				if (senderDataValue.getBarcodeLabelNumber().length() == 41)
					senderDataObj.setMlid(senderDataValue.getBarcodeLabelNumber().substring(18, 23));
				else if (senderDataValue.getBarcodeLabelNumber().length() == 39)
					senderDataObj.setMlid(senderDataValue.getBarcodeLabelNumber().substring(18, 21));
				senderDataObj.setDatamatrix(senderDataValue.getDatamatrix());

			}
			if (senderDataValue.getInjectionState() != null) {
				senderDataObj.setInjectionState(senderDataValue.getInjectionState());
			}
			if ("1PM3E".equalsIgnoreCase(senderDataValue.getServiceType())
					|| "1PME".equalsIgnoreCase(senderDataValue.getServiceType())
					|| "1PSE".equalsIgnoreCase(senderDataValue.getServiceType())) {
				senderDataObj.setCarrier("Express");
			} else {
				senderDataObj.setCarrier("eParcel");
			}
			senderDataObj.setConsignee_Email(senderDataValue.getConsigneeEmail());
			senderDataObj.setStatus("CONSIGNMENT CREATED");
			senderDataObj.setInjectionType("Direct Injection");
			senderDataObj.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
			senderDataObj.setIsDeleted("N");
			if (barcodeMap != null && !barcodeMap.isEmpty())
				provider = barcodeMap.get(barcodeMap.keySet().toArray()[0]);
			if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("Etower")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData labelData = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setBarcodelabelNumber(labelData.getBarCode());
				senderDataObj.setArticleId(labelData.getArticleId());
				if (senderDataValue.getServiceType().equalsIgnoreCase("TL1")) {
					senderDataObj.setDatamatrix(labelData.getArticleId());
				}
				else if (labelData.getArticleId().equals(labelData.getBarCode2D())) {
					senderDataObj.setBarcodelabelNumber(labelData.getArticleId());
					senderDataObj.setDatamatrix(labelData.getArticleId());
					senderDataObj.setCarrier("FastwayNZ");
				} else {
					senderDataObj.setDatamatrix(
							D2ZCommonUtil.formatDataMatrix(labelData.getBarCode2D().replaceAll("\\(|\\)|\u001d", "")));
				}

				senderDataObj.setInjectionState(senderDataValue.getInjectionState());
				if ("MCS".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setMlid(senderDataObj.getArticleId().substring(0, 5));
					senderDataObj.setInjectionState("SYD");
				}
			} else if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("PFL")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData pflLabel = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setInjectionState(pflLabel.getHub());
				senderDataObj.setBarcodelabelNumber(pflLabel.getTrackingNo());
				senderDataObj.setArticleId(pflLabel.getTrackingNo());
				senderDataObj.setMlid(pflLabel.getArticleId());
				senderDataObj.setDatamatrix(pflLabel.getMatrix());
				if ("MCS".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setCarrier("Fastway");
				} else if (!"1PS4".equalsIgnoreCase(senderDataValue.getServiceType())) {
					senderDataObj.setCarrier("FastwayM");
				}
				if ("PFL".equalsIgnoreCase(pflLabel.getCarrier())) {
					senderDataObj.setD2zRate(pflLabel.getHeader());
					senderDataObj.setBrokerRate(pflLabel.getFooter());
					senderDataObj.setCarrier(pflLabel.getCarrier());
				}
			} else if (null != barcodeMap && !barcodeMap.isEmpty() && provider.getProvider().equalsIgnoreCase("PCA")
					&& barcodeMap.containsKey(senderDataValue.getReferenceNumber())) {
				LabelData pflLabel = barcodeMap.get(senderDataValue.getReferenceNumber());
				senderDataObj.setInjectionState(pflLabel.getHub());
				senderDataObj.setBarcodelabelNumber(pflLabel.getTrackingNo());
				senderDataObj.setArticleId(pflLabel.getTrackingNo());
				senderDataObj.setMlid(pflLabel.getArticleId());
				senderDataObj.setDatamatrix(pflLabel.getMatrix());
				senderDataObj.setCarrier(pflLabel.getCarrier());
			}
			senderDataList.add(senderDataObj);
		}
		List<SenderdataMaster> insertedOrder = (List<SenderdataMaster>) senderDataRepository.saveAll(senderDataList);
		System.out.println(
				"create consignment API object construction Done data got inserted--->" + insertedOrder.size());
		if (orderDetailList.get(0).getBarcodeLabelNumber() == null
				|| orderDetailList.get(0).getBarcodeLabelNumber().trim().isEmpty()
				|| orderDetailList.get(0).getDatamatrix() == null
				|| orderDetailList.get(0).getDatamatrix().trim().isEmpty()) {
			storProcCall(fileSeqId);
		}
		updateTrackAndTrace(fileSeqId, userId);
		return fileSeqId;
	}

	/**
	 * @param senderDataList
	 */

	public void updateTrackAndTrace(String fileSeqId, int userId) {
		Runnable r = new Runnable() {
			public void run() {

				List<String> insertedOrder = fetchBySenderFileID(fileSeqId);
				List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					Trackandtrace trackAndTrace = new Trackandtrace();
					// trackAndTrace.setRowId(D2ZCommonUtil.generateTrackID());
					trackAndTrace.setUser_Id(String.valueOf(userId));
					trackAndTrace.setReference_number(obj[0].toString());
					trackAndTrace.setTrackEventCode("CC");
					trackAndTrace.setTrackEventDetails("CONSIGNMENT CREATED");
					trackAndTrace.setTrackEventDateOccured(D2ZCommonUtil.getAETCurrentTimestamp());
					trackAndTrace.setCourierEvents(null);
					trackAndTrace.setTrackSequence(1);
					trackAndTrace.setBarcodelabelNumber(obj[3].toString());
					trackAndTrace.setFileName("SP");
					trackAndTrace.setAirwayBill(null);
					trackAndTrace.setSignerName(null);
					trackAndTrace.setSignature(null);
					trackAndTrace.setIsDeleted("N");
					trackAndTrace.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
					trackAndTrace.setArticleID(obj[2].toString());
					trackAndTraceList.add(trackAndTrace);
				}
				List<Trackandtrace> trackAndTraceInsert = (List<Trackandtrace>) trackAndTraceRepository
						.saveAll(trackAndTraceList);

			}
		};
		new Thread(r).start();
	}

	public synchronized void storProcCall(String fileSeqId) {
		System.out.println("Before calling the store procedure, Sequence Id --->" + fileSeqId);
		System.out.println("Before the store procedure call, Timing --->" + D2ZCommonUtil.getAETCurrentTimestamp());
		senderDataRepository.inOnlyTest(fileSeqId);
		System.out.println("After the store procedure call, Timing --->" + D2ZCommonUtil.getAETCurrentTimestamp());
		System.out.println("After calling the store procedure, Sequence Id --->" + fileSeqId);
	}

	public List<PostcodeZone> fetchAllPostCodeZone() {
		List<PostcodeZone> postCodeZoneList = (List<PostcodeZone>) postcodeZoneRepository.findAll();
		System.out.println(postCodeZoneList.size());
		return postCodeZoneList;
	}

	public List<String> fetchAllReferenceNumbers() {
		List<String> referenceNumber_DB = senderDataRepository.fetchAllReferenceNumbers();
		return referenceNumber_DB;
	}

	@Override
	public List<String> fetchBySenderFileID(String senderFileID) {
		List<String> senderDataMaster = senderDataRepository.fetchBySenderFileId(senderFileID);
		return senderDataMaster;
	}

	@Override
	public List<Trackandtrace> trackParcelByArticleID(String articleID) {
		List<Trackandtrace> trackAndTrace = trackAndTraceRepository.fetchTrackEventByArticleID(articleID);
		return trackAndTrace;
	}

	@Override

	public ResponseMessage editConsignments(List<EditConsignmentRequest> requestList) {
		/*
		 * requestList.forEach(obj->{
		 * senderDataRepository.editConsignments(obj.getReferenceNumber(),
		 * obj.getWeight()); });
		 */
		List<String> incorrectRefNbrs = new ArrayList<String>();
		int updatedRows = 0;
		// Timestamp start = Timestamp.from(Instant.now());
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		for (EditConsignmentRequest obj : requestList) {
			SenderdataMaster senderData = senderDataRepository.fetchByReferenceNumbers(obj.getReferenceNumber());
			if (senderData != null) {
				updatedRows++;
				senderData.setWeight(obj.getWeight());
				senderDataList.add(senderData);
			} else {
				incorrectRefNbrs.add(obj.getReferenceNumber());
			}
		}
		senderDataRepository.saveAll(senderDataList);
		/*
		 * Timestamp end = Timestamp.from(Instant.now()); long callDuration =
		 * end.getTime() - start.getTime();
		 * System.out.println("Call Duration : "+callDuration);
		 */
		ResponseMessage responseMsg = new ResponseMessage();
		if (updatedRows == 0) {
			responseMsg.setResponseMessage("Update failed");
		} else if (updatedRows == requestList.size()) {
			responseMsg.setResponseMessage("Weight updated successfully");
		} else {
			responseMsg.setResponseMessage("Partially updated");
		}
		responseMsg.setMessageDetail(incorrectRefNbrs);
		return responseMsg;
	}

	@Override
	public String allocateShipment(String referenceNumbers, String shipmentNumber) {
		senderDataRepository.updateAirwayBill(referenceNumbers.split(","), shipmentNumber,
				D2ZCommonUtil.getAETCurrentTimestamp());
		senderDataRepository.allocateShipment(referenceNumbers, shipmentNumber);
		return "Shipment Allocated Successfully";
	}

	@Override
	public User addUser(UserDetails userData) {
		User userObj = new User();
		userObj.setCompanyName(userData.getCompanyName());
		userObj.setAddress(userData.getAddress());
		userObj.setSuburb(userData.getSuburb());
		userObj.setState(userData.getState());
		userObj.setPostcode(userData.getPostCode());
		userObj.setCountry(userData.getCountry());
		userObj.setEmail(userData.getEmailAddress());
		userObj.setUsername(userData.getUserName());
		userObj.setPassword(D2ZCommonUtil.hashPassword(userData.getPassword()));
		userObj.setRole_Id(userData.getRole_Id());
		userObj.setName(userData.getContactName());
		userObj.setPhoneNumber(userData.getContactPhoneNumber());
		userObj.setUser_IsDeleted(false);
		userObj.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
		userObj.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
		userObj.setClientBrokerId(userData.getClientBroker());
		userObj.setEBayToken(userData.geteBayToken());
		userObj.setPassword_value(userData.getPassword());
		User savedUser = userRepository.save(userObj);
		return savedUser;
	}

	@Override
	public List<UserService> addUserService(User user, List<String> serviceTypeList) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		for (String serviceType : serviceTypeList) {
			UserService userService = new UserService();
			userService.setUserId(user.getUser_Id());
			userService.setCompanyName(user.getCompanyName());
			userService.setUser_Name(user.getUsername());
			userService.setServiceType(serviceType);
			if (serviceType.equalsIgnoreCase("UnTracked")) {
				userService.setInjectionType("Origin Injection");
			} else {
				userService.setInjectionType("Direct Injection");
			}
			userService.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			userService.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			userServiceList.add(userService);
		}
		List<UserService> savedUserService = (List<UserService>) userServiceRepository.saveAll(userServiceList);
		return savedUserService;
	}

	@Override
	public User updateUser(User existingUser) {
		User updateduser = userRepository.save(existingUser);
		return updateduser;
	}

	@Override
	public void updateUserService(User existingUser, UserDetails userDetails) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		if (!userDetails.getServiceType().isEmpty()) {
			for (String serviceType : userDetails.getServiceType()) {
				UserService userService = userServiceRepository.fetchbyCompanyNameAndServiceType(
						existingUser.getCompanyName(), serviceType, userDetails.getUserName());
				if (userService == null) {
					UserService newUserService = new UserService();
					newUserService.setUserId(existingUser.getUser_Id());
					newUserService.setCompanyName(existingUser.getCompanyName());
					newUserService.setUser_Name(existingUser.getUsername());
					newUserService.setServiceType(serviceType);
					newUserService.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					newUserService.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					userServiceList.add(newUserService);
				} else {
					if (userService.isService_isDeleted()) {
						userService.setService_isDeleted(false);
						userService.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						userServiceList.add(userService);
					}
				}
			}
		}
		if (!userDetails.getDeletedServiceTypes().isEmpty()) {

			for (String serviceType : userDetails.getDeletedServiceTypes()) {

				UserService userService = userServiceRepository.fetchbyCompanyNameAndServiceType(
						existingUser.getCompanyName(), serviceType, userDetails.getUserName());

				if (userService != null) {
					userService.setService_isDeleted(true);
					userService.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					userServiceList.add(userService);
				}

			}
		}

		userServiceRepository.saveAll(userServiceList);

	}

	/*
	 * private void deleteUserService(User existingUser, List<String>
	 * deletedServiceTypes) { List<UserService> userServiceList = new
	 * ArrayList<UserService>(); if(!deletedServiceTypes.isEmpty()) { for(String
	 * serviceType : deletedServiceTypes ) { UserService userService =
	 * userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.
	 * getCompanyName(), serviceType); if(userService!=null) {
	 * userService.setService_isDeleted(true);
	 * userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
	 * userServiceList.add(userService); }
	 * 
	 * } userServiceRepository.saveAll(userServiceList); } }
	 */

	@Override
	public String deleteUser(String companyName, String roleId) {
		User existingUser = userRepository.fetchUserbyCompanyName(companyName, Integer.parseInt(roleId));
		if (existingUser == null) {
			return "Company Name does not exist";
		} else {
			existingUser.setUser_IsDeleted(true);
			existingUser.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			userRepository.save(existingUser);
			List<UserService> userService_DB = userServiceRepository.fetchbyCompanyName(companyName);
			List<UserService> userServiceList = new ArrayList<UserService>();
			for (UserService userService : userService_DB) {
				userService.setService_isDeleted(true);
				userService.setModifiedTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
				userServiceList.add(userService);
			}
			userServiceRepository.saveAll(userServiceList);
		}
		return "User deleted successfully";
	}

	public User login(String userName, String passWord) {
		User userDaetils = userRepository.fetchUserDetails(userName, passWord);
		return userDaetils;
	}

	@Override
	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber, List<Integer> clientIds) {
		List<SenderdataMaster> senderData = senderDataRepository.fetchShipmentData(shipmentNumber, clientIds);
		return senderData;
	}

	@Override
	public List<String> fetchServiceTypeByUserName(String userName) {
		List<String> serviceTypeList = userServiceRepository.fetchAllServiceTypeByUserName(userName);
		return serviceTypeList;
	}

	@Override
	public Trackandtrace getLatestStatusByReferenceNumber(String referenceNumber) {
		List<Trackandtrace> trackAndTraceList = trackAndTraceRepository.fetchTrackEventByRefNbr(referenceNumber);
		Trackandtrace trackandTrace = null;
		if (!trackAndTraceList.isEmpty()) {
			trackandTrace = trackAndTraceList.get(0);
		}
		return trackandTrace;
	}

	@Override
	public List<String> fetchReferenceNumberByUserId(Integer userId) {
		List<String> referenceNumbers_DB = senderDataRepository.fetchReferenceNumberByUserId(userId);
		return referenceNumbers_DB;
	}

	@Override
	public Trackandtrace getLatestStatusByArticleID(String articleID) {
		List<Trackandtrace> trackAndTraceList = trackAndTraceRepository.fetchTrackEventByArticleID(articleID);
		Trackandtrace trackandTrace = null;
		if (!trackAndTraceList.isEmpty()) {
			trackandTrace = trackAndTraceList.get(0);
		}
		return trackandTrace;
	}

	@Override
	public List<SenderdataMaster> findRefNbrByShipmentNbr(String[] referenceNumbers) {
		return senderDataRepository.findRefNbrByShipmentNbr(referenceNumbers);
	}

	@Override
	public void logEbayResponse(CompleteSaleResponseType response) {
		EbayResponse resp = new EbayResponse();
		resp.setAck(response.getAck().toString());
		if (null != response.getErrors() && response.getErrors().length > 0) {
			resp.setShortMessage(response.getErrors(0).getShortMessage());
			resp.setLongMessage(response.getErrors(0).getLongMessage());
		}
		ebayResponseRepository.save(resp);
	}

	public ClientDashbaord clientDahbaord(Integer userId) {
		ClientDashbaord clientDashbaord = new ClientDashbaord();
		clientDashbaord.setConsignmentsCreated(senderDataRepository.fecthConsignmentsCreated(userId));
		clientDashbaord.setConsignmentsManifested(senderDataRepository.fetchConsignmentsManifested(userId));
		clientDashbaord.setConsignmentsManifests(senderDataRepository.fetchConsignmentsManifests(userId));
		clientDashbaord.setConsignmentsDeleted(senderDataRepository.fetchConsignmentsDeleted(userId));
		clientDashbaord.setConsignmentDelivered(senderDataRepository.fetchConsignmentDelivered(userId));
		return clientDashbaord;
	}

	@Override
	public void deleteConsignment(String referenceNumbers) {
		senderDataRepository.deleteConsignments(referenceNumbers);
	}

	@Override
	public List<String> fetchServiceType(Integer user_id) {
		List<String> userServiceType = userServiceRepository.fetchUserServiceById(user_id);
		return userServiceType;
	}

	@Override
	public List<APIRates> fetchAllAPIRates() {
		List<APIRates> apiRates = (List<APIRates>) apiRatesRepository.findAll();
		System.out.println(apiRates.size());
		return apiRates;
	}

	@Override
	public void logEtowerResponse(List<ETowerResponse> responseEntity) {
		eTowerResponseRepository.saveAll(responseEntity);

	}

	@Override
	public ResponseMessage insertTrackingDetails(TrackingEventResponse trackEventresponse) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		List<TrackEventResponseData> responseData = trackEventresponse.getData();
		ResponseMessage responseMsg = new ResponseMessage();

		if (responseData != null && responseData.isEmpty()) {
			responseMsg.setResponseMessage("No Data from ETower");
		} else {

			for (TrackEventResponseData data : responseData) {
				if (data != null && data.getEvents() != null) {

					for (ETowerTrackingDetails trackingDetails : data.getEvents()) {

						Trackandtrace trackandTrace = new Trackandtrace();
						trackandTrace.setArticleID(trackingDetails.getTrackingNo());
						trackandTrace.setFileName("eTowerAPI");

						trackandTrace.setTrackEventDateOccured(trackingDetails.getEventTime());
						trackandTrace.setTrackEventCode(trackingDetails.getEventCode());

						trackandTrace.setTrackEventDetails(trackingDetails.getActivity());
						trackandTrace.setCourierEvents(trackingDetails.getActivity());
						trackandTrace
								.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()).toString());
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

	public List<SenderdataMaster> fetchConsignmentsManifestShippment(List<String> incomingRefNbr) {
		return senderDataRepository.fetchConsignmentsManifestShippment(incomingRefNbr);
	}

	@Override
	public List<SenderdataMaster> fetchDataForAusPost(List<String> refNbrs) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchDataForAusPost(refNbrs);
	}

	@Override
	public int fetchUserIdByReferenceNumber(String reference_number) {
		int userID = senderDataRepository.fetchUserIdByReferenceNumber(reference_number);
		return userID;
	}

	@Override
	public List<String> fetchArticleIDForFDMCall() {
		List<String> referenceNumber = trackAndTraceRepository.fetchArticleIDForFDMCall();
		return referenceNumber;
	}

	private Map<String, LabelData> processGainLabelsResponse(GainLabelsResponse response) {
		Map<String, LabelData> barcodeMap = new HashMap<String, LabelData>();
		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		if (response != null) {
			List<LabelData> responseData = response.getData();
			if (responseData == null && null != response.getErrors()) {
				for (EtowerErrorResponse error : response.getErrors()) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(response.getStatus());
					errorResponse.setErrorCode(error.getCode());
					errorResponse.setErrorMessage(error.getMessage());
					responseEntity.add(errorResponse);
				}
			}

			for (LabelData data : responseData) {
				List<EtowerErrorResponse> errors = data.getErrors();
				if (null == errors) {
					ETowerResponse errorResponse = new ETowerResponse();
					errorResponse.setAPIName("Gain Labels");
					errorResponse.setStatus(data.getStatus());
					errorResponse.setOrderId(data.getOrderId());
					errorResponse.setReferenceNumber(data.getReferenceNo());
					errorResponse.setTrackingNo(data.getTrackingNo());
					errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					responseEntity.add(errorResponse);
					barcodeMap.put(data.getReferenceNo(), data);
				} else {
					for (EtowerErrorResponse error : errors) {
						ETowerResponse errorResponse = new ETowerResponse();
						errorResponse.setAPIName("Gain Labels");
						errorResponse.setStatus(response.getStatus());
						errorResponse.setStatus(data.getStatus());
						errorResponse.setOrderId(data.getOrderId());
						errorResponse.setReferenceNumber(data.getReferenceNo());
						errorResponse.setTrackingNo(data.getTrackingNo());
						errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
						errorResponse.setErrorCode(error.getCode());
						errorResponse.setErrorMessage(error.getMessage());
						responseEntity.add(errorResponse);
					}
				}
			}
		}
		logEtowerResponse(responseEntity);

		return barcodeMap;

	}

	@Override
	public List<String> fetchDataForAUPost() {
		List<Trackandtrace> trackandtraceData = trackAndTraceRepository.fetchArticleIDForAUPost();
		List<Trackandtrace> updatedData = new ArrayList<Trackandtrace>();
		List<String> articleID = new ArrayList<String>();
		if (trackandtraceData.size() >= 10) {
			for (Trackandtrace data : trackandtraceData) {
				data.setFileName("AUPost");
				updatedData.add(data);
				articleID.add(data.getArticleID());
			}
			trackAndTraceRepository.saveAll(updatedData);
		}

		return articleID;
	}

	public ResponseMessage insertAUTrackingDetails(TrackingResponse auTrackingDetails, Map<String, String> map) {
		List<Trackandtrace> trackAndTraceList = new ArrayList<Trackandtrace>();
		List<TrackingResults> trackingData = auTrackingDetails.getTracking_results();
		ResponseMessage responseMsg = new ResponseMessage();
		if (trackingData.isEmpty()) {
			responseMsg.setResponseMessage("No Data from AUPost");
		} else {
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			for (TrackingResults data : trackingData) {
				if (data != null && data.getTrackable_items() != null) {
					for (TrackableItems trackingLabel : data.getTrackable_items()) {
						Date latestTime = null;
						try {
							latestTime = output.parse(map.get(trackingLabel.getArticle_id()));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						if (trackingLabel != null && trackingLabel.getEvents() != null) {
							for (TrackingEvents trackingEvents : trackingLabel.getEvents()) {
								Date eventTime = null;
								try {
									eventTime = inputFormat.parse(trackingEvents.getDate().substring(0, 19));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								if (eventTime.after(latestTime)) {
									/*
									 * System.out.println("Inserting..." + trackingLabel.getArticle_id()
									 * +" : "+trackingEvents.getDate().substring(0,19) +
									 * " : "+trackingEvents.getDescription());
									 */
									Trackandtrace trackandTrace = new Trackandtrace();
									// trackandTrace.setRowId(D2ZCommonUtil.generateTrackID());
									trackandTrace.setArticleID(trackingLabel.getArticle_id());
									trackandTrace.setTrackEventDetails(trackingEvents.getDescription());
									trackandTrace.setTrackEventDateOccured(trackingEvents.getDate().substring(0, 19));
									trackandTrace.setLocation(trackingEvents.getLocation());
									trackandTrace.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
									// trackandTrace.setFileName("AU-Post");
									trackandTrace.setFileName("AUPostTrack");
									trackAndTraceList.add(trackandTrace);
								}
							}
						}
					}
				}
			}
			trackAndTraceRepository.saveAll(trackAndTraceList);
			responseMsg.setResponseMessage("Data uploaded successfully from AU Post");
		}
		return responseMsg;
	}

	@Override
	public void logAUPostResponse(List<AUPostResponse> aupostresponse) {
		aupostresponseRepository.saveAll(aupostresponse);
	}

	@Override
	public void updateCubicWeight() {
		senderDataRepository.updateCubicWeight();
	}

	@Override
	public void updateRates() {
		senderDataRepository.updateRates();
	}

	@Override
	public int fetchUserIdbyUserName(String userName) {
		return userRepository.fetchUserIdbyUserName(userName);
	}

	@Override
	public List<SenderdataMaster> fetchDataBasedonSupplier(List<String> incomingRefNbr, String supplier) {
		return senderDataRepository.fetchDataBasedonSupplier(incomingRefNbr, supplier);
	}

	@Override
	public List<String> fetchDataForEtowerForeCastCall(String[] refNbrs) {
		return senderDataRepository.fetchDataForEtowerForeCastCall(refNbrs);
	}

	@Override
	public List<FastwayPostcode> fetchFWPostCodeZone() {
		List<FastwayPostcode> postCodeFWZoneList = (List<FastwayPostcode>) fastwayPostcodeRepository.findAll();
		return postCodeFWZoneList;
	}

	@Override
	public List<PFLSubmitOrderData> fetchDataforPFLSubmitOrder(String[] refNbrs) {
		List<PFLSubmitOrderData> submitOrderData = new ArrayList<PFLSubmitOrderData>();
		List<Object[]> objArr = senderDataRepository.fetchDataforPFLSubmitOrder(refNbrs);
		objArr.forEach(obj -> submitOrderData.add(new PFLSubmitOrderData(obj)));
		return submitOrderData;
	}

	@Override
	public String fetchUserById(int userId) {
		return userRepository.fetchUserById(userId);
	}

	@Override

	public List<String> getArticleIDForFreiPostTracking() {
		return trackAndTraceRepository.getArticleIDForFreiPostTracking();
	}

	public EnquiryResponse createEnquiry(Enquiry createEnquiry) throws ReferenceNumberNotUniqueException {
		List<CSTickets> csTctList = new ArrayList<CSTickets>();
		for (CreateEnquiryRequest enquiryRequest : createEnquiry.getEnquiryDetails()) {
			CSTickets tickets = new CSTickets();
			String timestamp = null;
			if (enquiryRequest.getType().equalsIgnoreCase("Article Id")) {
				SenderdataMaster senderArticleId = senderDataRepository
						.fetchDataArticleId(enquiryRequest.getIdentifier());
				if (null != senderArticleId) {
					tickets.setArticleID(senderArticleId.getArticleId());
					tickets.setReferenceNumber(senderArticleId.getReference_number());
					tickets.setConsigneeName(senderArticleId.getConsignee_name());
					tickets.setConsigneeaddr1(senderArticleId.getConsignee_addr1());
					tickets.setConsigneeSuburb(senderArticleId.getConsignee_Suburb());
					tickets.setConsigneeState(senderArticleId.getConsignee_State());
					tickets.setConsigneePostcode(senderArticleId.getConsignee_Postcode());
					tickets.setProductDescription(senderArticleId.getProduct_Description());
					tickets.setBarcodelabelNumber(senderArticleId.getBarcodelabelNumber());
					tickets.setCarrier(senderArticleId.getCarrier());
					if (senderArticleId.getAirwayBill() != null) {
						timestamp = senderArticleId.getTimestamp();
					}
					tickets.setTicketID("INC" + D2ZCommonUtil.getday() + csticketsRepository.fetchNextSeq().toString());
					tickets.setComments(enquiryRequest.getComments());
					tickets.setDeliveryEnquiry(enquiryRequest.getEnquiry());
					tickets.setPod(enquiryRequest.getPod());
					tickets.setStatus("open");
					tickets.setUserId(userRepository.fetchUserIdbyUserName(createEnquiry.getUserName()));
					tickets.setClientBrokerId(
							userRepository.fetchClientBrokerIdbyUserName(createEnquiry.getUserName()));
					tickets.setEnquiryOpenDate(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					TransitTime transitTimeResponse = transitTimeRepository
							.fetchTransitTime(tickets.getConsigneePostcode());
					if (null != transitTimeResponse && null != transitTimeResponse.getTransitTime()
							&& null != timestamp) {
						String deliveryDate = D2ZCommonUtil.getIncreasedTime(timestamp,
								transitTimeResponse.getTransitTime());
						tickets.setExpectedDeliveryDate(deliveryDate);
					}
					csTctList.add(tickets);
				}
			} else if (enquiryRequest.getType().equalsIgnoreCase("Reference Number")) {
				SenderdataMaster senderRefId = senderDataRepository
						.fetchDataReferenceNum(enquiryRequest.getIdentifier());
				if (null != senderRefId) {
					tickets.setArticleID(senderRefId.getArticleId());
					tickets.setReferenceNumber(senderRefId.getReference_number());
					tickets.setConsigneeName(senderRefId.getConsignee_name());
					tickets.setConsigneeaddr1(senderRefId.getConsignee_addr1());
					tickets.setConsigneeSuburb(senderRefId.getConsignee_Suburb());
					tickets.setConsigneeState(senderRefId.getConsignee_State());
					tickets.setConsigneePostcode(senderRefId.getConsignee_Postcode());
					tickets.setProductDescription(senderRefId.getProduct_Description());
					tickets.setBarcodelabelNumber(senderRefId.getBarcodelabelNumber());
					tickets.setCarrier(senderRefId.getCarrier());
					if (senderRefId.getAirwayBill() != null) {
						timestamp = senderRefId.getTimestamp();
					}
					tickets.setTicketID("INC" + D2ZCommonUtil.getday() + csticketsRepository.fetchNextSeq().toString());
					tickets.setComments(enquiryRequest.getComments());
					tickets.setDeliveryEnquiry(enquiryRequest.getEnquiry());
					tickets.setPod(enquiryRequest.getPod());
					tickets.setStatus("open");
					tickets.setUserId(userRepository.fetchUserIdbyUserName(createEnquiry.getUserName()));
					tickets.setClientBrokerId(
							userRepository.fetchClientBrokerIdbyUserName(createEnquiry.getUserName()));
					tickets.setEnquiryOpenDate(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
					TransitTime transitTimeResponse = transitTimeRepository
							.fetchTransitTime(tickets.getConsigneePostcode());
					if (null != transitTimeResponse && null != transitTimeResponse.getTransitTime()
							&& null != timestamp) {
						String deliveryDate = D2ZCommonUtil.getIncreasedTime(timestamp,
								transitTimeResponse.getTransitTime());
						tickets.setExpectedDeliveryDate(deliveryDate);
					}
					csTctList.add(tickets);
				}
			}
		}
		EnquiryResponse usrMsg = new EnquiryResponse();
		if (csTctList.size() > 0) {
			List<String> incomingRefNbr = csTctList.stream().map(obj -> {
				return obj.getReferenceNumber();
			}).collect(Collectors.toList());
			isReferenceNumberUniqueUI(incomingRefNbr);
			for (CSTickets csTicket : csTctList) {
				if (null == csTicket.getReferenceNumber()) {
					throw new ReferenceNumberNotUniqueException(
							"Reference Number (or) Article Id is not avilable in the system", null);
				}
			}
			csticketsRepository.saveAll(csTctList);
			List<String> tickets = new ArrayList<String>();
			for (CSTickets csTicket : csTctList) {
				tickets.add(csTicket.getTicketID());
			}
			usrMsg.setMessage("Enquiry created Successfully");
			usrMsg.setTicketId(tickets);
			return usrMsg;
		} else {
			usrMsg.setMessage("Records are Not avilable for the given Enquiry");
			return usrMsg;
		}

	}

	public void isReferenceNumberUniqueUI(List<String> incomingRefNbr) throws ReferenceNumberNotUniqueException {
		System.out.println(incomingRefNbr.toString());
		List<String> referenceNumber_DB = csticketsRepository.fetchAllReferenceNumbers();
		referenceNumber_DB.addAll(incomingRefNbr);

		System.out.println(referenceNumber_DB);
		List<String> duplicateRefNbr = referenceNumber_DB.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.filter(e -> e.getValue() > 1).map(e -> e.getKey()).collect(Collectors.toList());

		if (!duplicateRefNbr.isEmpty()) {
			throw new ReferenceNumberNotUniqueException("Reference Number or Article Id must be unique",
					duplicateRefNbr);
		}
	}

	@Override
	public List<CSTickets> fetchEnquiry(String status, String fromDate, String toDate, String userId) {
		List<CSTickets> enquiryDetails = null;
		Integer[] userIds = Arrays.stream(userId.split(",")).map(String::trim).map(Integer::valueOf)
				.toArray(Integer[]::new);
		if (!fromDate.equals("null") && !toDate.equals("null")) {
			enquiryDetails = csticketsRepository.fetchEnquiry(fromDate, toDate, userIds);
		} else {
			enquiryDetails = csticketsRepository.fetchEnquiry(userIds);
		}
		return enquiryDetails;
	}

	@Override
	public List<CSTickets> fetchCompletedEnquiry(String userId) {
		Integer[] userIds = Arrays.stream(userId.split(",")).map(String::trim).map(Integer::valueOf)
				.toArray(Integer[]::new);
		List<CSTickets> enquiryDetails = csticketsRepository.fetchCompletedEnquiry(userIds);
		return enquiryDetails;
	}

	@Override
	public List<Integer> fetchUserId(String userId) {
		List<Integer> userIds = userRepository.getClientId(userId);
		return userIds;
	}

	@Override
	public List<String> fetchReferencenumberByArticleid(List<String> ArticleID) {
		// TODO Auto-generated method stub
		List<String> refnbrs = senderDataRepository.fetchreferencenumberforArticleid(ArticleID);
		return refnbrs;
	}

	@Override
	public void logcurrencyRate() {
		// TODO Auto-generated method stub
		List<CurrencyDetails> currencyList = currencyproxy.currencyRate();
		currencyRepository.deleteAll();
		List<Currency> currencyObjList = new ArrayList<Currency>();
		for (CurrencyDetails currencyValue : currencyList) {
			Currency currencyObj = new Currency();
			currencyObj.setAudCurrencyRate(currencyValue.getAudCurrencyRate().doubleValue());
			currencyObj.setCountry(currencyValue.getCountry());
			currencyObj.setCurrencyCode(currencyValue.getCurrencyCode());
			currencyObj.setLastUpdated(new Date());
			currencyObjList.add(currencyObj);
		}

		currencyRepository.saveAll(currencyObjList);

	}

	@Override
	public Double getAudcurrency(String country) {
		return currencyRepository.getaud(country);
	}

	@Override
	public List<SenderdataMaster> fetchDataBasedonrefnbr(List<String> incomingRefNbr) {
		return senderDataRepository.fetchConsignmentsByRefNbr(incomingRefNbr);
	}

	@Override
	public List<SenderdataMaster> fetchConsignmentsByRefNbr(List<String> refNbrs) {
		return senderDataRepository.fetchConsignmentsByRefNbr(refNbrs);
	}

	@Override
	public List<Returns> returnsOutstanding(String fromDate, String toDate, String userId) {
		Integer[] userIds = Arrays.stream(userId.split(",")).map(String::trim).map(Integer::valueOf)
				.toArray(Integer[]::new);
		List<Returns> returnsDetails = new ArrayList<Returns>();
		if (!fromDate.equals("null") && !toDate.equals("null")) {
			returnsDetails = returnsRepository.fetchOutstandingDetails(fromDate, toDate, userIds);
		} else {
			returnsDetails = returnsRepository.fetchOutstandingCompleteDetails(userIds);
		}
		return returnsDetails;
	}

	@Override
	public List<SenderdataMaster> fetchShipmentDatabyType(List<String> number, List<Integer> listOfClientId,
			String type) {
		List<SenderdataMaster> senderData;
		if (type.equals("articleid")) {
			senderData = senderDataRepository.fetchShipmentDatabyArticleId(number, listOfClientId);
		} else if (type.equals("barcodelabel")) {
			senderData = senderDataRepository.fetchShipmentDatabyBarcode(number, listOfClientId);
		} else {
			System.out.print("in else");
			senderData = senderDataRepository.fetchShipmentDatabyReference(number, listOfClientId);
		}
		return senderData;
	}

	@Override
	public UserMessage returnAction(List<ReturnsAction> returnsAction) {
		for (ReturnsAction actionRequest : returnsAction) {
			returnsRepository.updateReturnAction(actionRequest.getAction(), actionRequest.getResendRefNumber(),
					actionRequest.getArticleId());
		}
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Return Action Updated Successfully");
		return usrMsg;
	}

	@Override
	public List<SystemRefCount> fetchAllSystemRefCount() {
		// TODO Auto-generated method stub
		List<SystemRefCount> systemRefCount = (List<SystemRefCount>) systemRefCountRepository.findAll();
		System.out.println(systemRefCount.size());
		return systemRefCount;
	}

	@Override
	public void updateSystemRefCount(Map<String, Integer> currentSysRefCount) {
		List<SystemRefCount> systemRefCountList = new ArrayList<SystemRefCount>();
		for (String supplier : currentSysRefCount.keySet()) {
			if (currentSysRefCount.get(supplier) > 0) {
				SystemRefCount sysRefCount = new SystemRefCount();
				sysRefCount.setSupplier(supplier);
				sysRefCount.setSystemRefNo(currentSysRefCount.get(supplier));
				systemRefCountList.add(sysRefCount);
			}
		}
		systemRefCountRepository.saveAll(systemRefCountList);
	}

	@Override
	public UserMessage enquiryFileUpload(byte[] blob, String fileName, String ticketNumber) {
		csticketsRepository.enquiryFileUpload(blob, fileName, ticketNumber);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Enquiry Data Updated Successfully");
		return usrMsg;
	}

	@Override
	public String fetchServiceTypeByRefNbr(String refNbr) {
		return senderDataRepository.fetchServiceTypeByRefNbr(refNbr);
	}

	@Override
	public String fetchServiceType(String articleID) {
		return senderDataRepository.fetchServiceType(articleID);
	}

	@Override
	public List<String> fetchMlidsBasedOnSupplier(String supplier) {
		return consigneeCountRepository.getMlidBasedonSupplier(supplier);
	}

	@Override
	public List<StarTrackPostcode> fetchSTPostCodeZone() {
		List<StarTrackPostcode> postCodeSTZoneList = (List<StarTrackPostcode>) starTrackPostcodeRepository.findAll();
		return postCodeSTZoneList;
	}

	@Override
	public UserMessage enquiryFileUpload(List<SuperUserEnquiry> SuperUserEnquiry) {
		for (SuperUserEnquiry enquiry : SuperUserEnquiry) {
			csticketsRepository.enquiryUpdate(enquiry.getTicketNumber(), enquiry.getComments(),
					enquiry.getD2zComments(), enquiry.getSendUpdate(), enquiry.getStatus());
		}
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Enquiry Data Updated Successfully");
		return usrMsg;
	}

	public EnquiryResponse enquiryClientUpdate(EnquiryUpdate updateEnquiry) {
		csticketsRepository.enquiryUpdate(updateEnquiry.getTicketId(), updateEnquiry.getComments());
		EnquiryResponse usrMsg = new EnquiryResponse();
		usrMsg.setMessage("Enquiry Comments Updated Successfully");
		return usrMsg;
	}

	public UserMessage enquiryUpdate(String ticketNum, String cmts, String d2zCmts, String update, String sts) {
		csticketsRepository.enquiryUpdate(ticketNum, cmts, d2zCmts, update, sts);
		UserMessage usrMsg = new UserMessage();
		usrMsg.setMessage("Enquiry Data Updated Successfully");
		return usrMsg;
	}

	@Override
	public CSTickets fetchCSTicketDetails(String ticketId) {
		CSTickets csTicket = csticketsRepository.fetchCSTicketDetails(ticketId);
		return csTicket;
	}

	@Override
	public List<String> fetchPerformanceReportData() {
		return senderDataRepository.fetchPerformanceReportData();
	}

	@Override
	public List<PerformanceReportTrackingData> fetchArticleIdForPerformanceReport(int day, int month) {
		List<PerformanceReportTrackingData> trackingData = new ArrayList<PerformanceReportTrackingData>();
		List<Object[]> objArr = senderDataRepository.fetchArticleIdForPerformanceReport(day);
		objArr.forEach(obj -> trackingData.add(new PerformanceReportTrackingData(obj)));
		return trackingData;
	}

	@Override
	public List<EmailEnquiryDetails> fetchEmailEnquiryDetails() {
		List<EmailEnquiryDetails> enquiryData = new ArrayList<EmailEnquiryDetails>();
		List<Object[]> csTicket = csticketsRepository.fetchOpenTicketDetails();
		csTicket.forEach(obj -> enquiryData.add(new EmailEnquiryDetails(obj)));
		return enquiryData;
	}

	@Override
	public List<User> fetchEmailDetails() {
		List<User> userEmailDetails = userRepository.fetchEmailDetails();
		return userEmailDetails;
	}

	@Override
	public List<EmailReturnDetails> fetchReturnsDetails() {
		List<EmailReturnDetails> returnsData = new ArrayList<EmailReturnDetails>();
		List<Object[]> returns = returnsRepository.fetchReturnsDetails();
		returns.forEach(obj -> returnsData.add(new EmailReturnDetails(obj)));
		return returnsData;
	}

	@Override
	public List<HeldParcelDetails> parcelEmail() {
		List<HeldParcelDetails> parcelData = new ArrayList<HeldParcelDetails>();
		List<Object[]> parcel = parcelRepository.fetchParcelDetails();
		parcel.forEach(obj -> parcelData.add(new HeldParcelDetails(obj)));
		return parcelData;
	}

	@Override
	public void updateForPFLSubmitOrder(List<String> fastwayOrderId, String status) {
		trackAndTraceRepository.updateForPFLSubmitOrder(fastwayOrderId, status);

	}

	@Override
	public List<PFLSubmitOrderData> fetchDataForPFLSubmitOrder() {
		List<PFLSubmitOrderData> submitOrderData = new ArrayList<PFLSubmitOrderData>();
		List<Object[]> objArr = senderDataRepository.fetchDataForPFLSubmitOrder();
		objArr.forEach(obj -> submitOrderData.add(new PFLSubmitOrderData(obj)));
		return submitOrderData;

	}

	@Override
	public void updateForPFLSubmitOrderCompleted() {
		trackAndTraceRepository.updateForPFLSubmitOrderCompleted();
	}

	@Override
	public List<NZPostcodes> fetchAllNZPostCodeZone() {

		List<NZPostcodes> postCodeZoneList = (List<NZPostcodes>) nzPostcodesRepository.findAll();
		System.out.println(postCodeZoneList.size());
		return postCodeZoneList;
	}

	@Override
	public List<String> fetchArticleID(List<String> refBarNum) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchArticleId(refBarNum);
	}

	@Override
	public List<String> fetchMlid(List<String> refBarNum) {
		return senderDataRepository.fetchMlid(refBarNum);
	}

	@Override
	public List<String> fetchPerformanceReportDataByArticleId(List<String> articleIds) {
		// TODO Auto-generated method stub
		return senderDataRepository.fetchPerformanceReportDataByArticleId(articleIds);
	}

	@Override
	public Map<String, com.d2z.d2zservice.model.TrackingEvents> fetchTrackingEvents(List<String> articleIds) {
		List<TrackingEvent> data = trackingEventRepository.findbyArticleIds(articleIds);
		Map<String, com.d2z.d2zservice.model.TrackingEvents> trackingDataMap = new HashMap<String, com.d2z.d2zservice.model.TrackingEvents>();
		data.forEach(obj -> {
			com.d2z.d2zservice.model.TrackingEvents events = new com.d2z.d2zservice.model.TrackingEvents();
			events.setEventDetails(obj.getTrackEventDetails());
			events.setTrackEventDateOccured(obj.getTrackEventDateOccured());
			trackingDataMap.put(obj.getOrderId(), events);
		});
		return trackingDataMap;
	}

	@Override
	public void saveTrackingEvents(String articleId, com.d2z.d2zservice.model.TrackingEvents trackEvents) {

		TrackingEvent event = new TrackingEvent();
		event.setOrderId(articleId);
		event.setTrackEventDateOccured(trackEvents.getTrackEventDateOccured());
		event.setTrackEventDetails(trackEvents.getEventDetails());

		trackingEventRepository.save(event);
	}

	@Override
	public List<PFLPostcode> fetchAllPFLPostCodeZone() {

		List<PFLPostcode> postCodeZoneList = (List<PFLPostcode>) pflPostcodesRepository.findAll();
		return postCodeZoneList;
	}

	@Override
	public List<String> fetchDataForFDMCall(String[] refNbrs) {
		return senderDataRepository.fetchDataForFDMCall(refNbrs);
	}

	@Override
	public List<MasterPostCode> fetchAllMasterPostCodeZone() {
		// TODO Auto-generated method stub
		return (List<MasterPostCode>) masterPostcodesRepository.findAll();
	}

	@Override
	public ResponseMessage createTrackEvents(List<TrackParcelResponse> request) {

		
		ResponseMessage responseMsg = new ResponseMessage();

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date eventTime = null;
		Date latestTime = null;
		List<TrackEvents> events = new ArrayList<TrackEvents>();
		for (TrackParcelResponse data : request) {
			TrackEvents eventDB = trackEventsRepository.fetchLatestStatus(data.getArticleId());
			List<com.d2z.d2zservice.model.TrackingEvents> trackingEvents = data.getTrackingEvents();
			for (com.d2z.d2zservice.model.TrackingEvents trackingEvent : trackingEvents) {
				if (eventDB != null) {

					try {
						eventTime = inputFormat.parse(trackingEvent.getTrackEventDateOccured());
						latestTime = output.parse(eventDB.getTrackEventDateOccured());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (eventTime.after(latestTime)) {
						insertTrackEvents(events,data.getArticleId(),trackingEvent);
					}

				}else {
					 insertTrackEvents(events,data.getArticleId(),trackingEvent);
				}
			}
			if(events.size()>0) {
			trackEventsRepository.saveAll(events);
			}
			responseMsg.setResponseMessage("Events updated successfully");
		}
		return responseMsg;

	}

	private void insertTrackEvents(List<TrackEvents> events,String articleId, com.d2z.d2zservice.model.TrackingEvents trackingEvent) {
		
		TrackEvents trackandTrace = new TrackEvents();
		trackandTrace.setArticleID(articleId);
		trackandTrace.setFileName("FDMTracking");
		trackandTrace.setTrackEventDateOccured(trackingEvent.getTrackEventDateOccured());
		trackandTrace.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
		trackandTrace.setReference_number(articleId);
		trackandTrace.setLocation(trackingEvent.getLocation());
		trackandTrace.setTrackEventCode(trackingEvent.getStatusCode());
		if(null == trackingEvent.getEventDetails() ) {
		if("INCG".equalsIgnoreCase(trackingEvent.getStatusCode())) {
			trackandTrace.setTrackEventDetails("Arrived at facility");
		}else if("OBD".equalsIgnoreCase(trackingEvent.getStatusCode())) {
			trackandTrace.setTrackEventDetails("On board for delivery");
		}else if("DEL".equalsIgnoreCase(trackingEvent.getStatusCode())) {
			trackandTrace.setTrackEventDetails("Package Delivered");
		}
		}else {
			trackandTrace.setTrackEventDetails(trackingEvent.getEventDetails());
		}
		trackandTrace.setIsDeleted("N");
		events.add(trackandTrace);		
	}

	@Override
	public List<TrackEvents> fetchEventsFromTrackEvents(List<String> articleIds) {
		// TODO Auto-generated method stub
		return trackEventsRepository.fetchEventsFromTrackEvents(articleIds);
	}

	@Override
	public List<String> fetchTrackingNumberFromEtowerResponse(List<String> artileIDList) {
		// TODO Auto-generated method stub
		return eTowerResponseRepository.fetchTrackingNumberFromEtowerResponse(artileIDList);
	}
	

}
