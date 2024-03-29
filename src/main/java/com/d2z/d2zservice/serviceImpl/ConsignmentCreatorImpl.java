package com.d2z.d2zservice.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.model.ConsignmentConfig;
import com.d2z.d2zservice.model.CreateConsignmentResponse;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.service.ConsignmentCreator;
import com.d2z.d2zservice.service.ShipmentAllocator;
import com.d2z.d2zservice.supplier.EtowerSupplier;
import com.d2z.d2zservice.supplier.PFLSupplier;
import com.d2z.d2zservice.supplier.Tracker;
import com.d2z.d2zservice.supplier.VeloceSupplier;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.d2zservice.util.ValidationUtils;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.ETowerWrapper;

@Service
public class ConsignmentCreatorImpl implements ConsignmentCreator {

	
	@Autowired 
	ID2ZDao dao;
	
	@Autowired
	PFLSupplier pflSupplier;
	
	@Autowired
	EtowerSupplier etowerSupplier;
	
	@Autowired
	VeloceSupplier veloceSupplier;
	
	@Autowired
	ETowerWrapper eTowerWrapper;
	
	@Autowired
	Tracker tracker;
	
	@Autowired
	ShipmentAllocator allocator;
	
	@Override
	public Map<String,ConsignmentConfig> fetchConfigDetails(Map<String,List<SenderDataApi>> request,int userId,String fileSeqId,Map<String, List<ErrorDetails>> errorMap){
		Map<String,ConsignmentConfig> requestMap = new HashMap<String,ConsignmentConfig>();
		request.forEach((serviceType,requestData) -> {
			
			ConsignmentConfig config =  new ConsignmentConfig();
			config.setUserId(userId);
			config.setServiceType(serviceType);
			config.setAutoShipmentIndicator(dao.fetchAutoShipmentIndicator(userId,serviceType));
			config.setPostCodeValidationRequired(dao.fetchPostCodeValidationRequired(userId,serviceType));
			config.setSupplierConsignmentMap(fetchSupplierData(serviceType,convertToDTO(requestData,userId,fileSeqId),
					config.isPostCodeValidationRequired(),errorMap));
			requestMap.put(serviceType, config);
			
	});
	return requestMap;
	
	}
	private Map<SupplierEntity,List<ConsignmentDTO>>  fetchSupplierData(String serviceType,List<ConsignmentDTO> consignmentList,
			boolean isPostCodeValidationRequired, Map<String, List<ErrorDetails>> errorMap) {
		String indicator = dao.getPostCodeLogic(serviceType);
		if(StringUtils.isNumeric(indicator)) {
			consignmentList.forEach(consignment ->{
				consignment.setSupplierAuthId(indicator);
			});
		}
		else {
		dao.fetchAllMasterPostCodeZone(indicator,consignmentList);
		}
		Map<SupplierEntity,List<ConsignmentDTO>> consignmentSupplierMap = new HashMap<SupplierEntity,List<ConsignmentDTO>>();
		consignmentList.stream().collect(Collectors.groupingBy(ConsignmentDTO::getSupplierAuthId))
		.forEach((supplierAuthId, consignments) -> {
		 
		 if(Integer.parseInt(supplierAuthId) ==0) {
				if(isPostCodeValidationRequired) {
					D2ZValidator.validatePostcode(consignments, errorMap);
				}
		 }
		 if(Integer.parseInt(supplierAuthId)>0) {
				SupplierEntity supplier = dao.fetchSupplierData(Integer.parseInt(supplierAuthId));
				consignmentSupplierMap.put(supplier, consignments);
			}
		});	
		return consignmentSupplierMap;
	}
	
	@Override
	public List<SenderdataMaster> createConsignment(Map<String,List<SenderDataApi>> request,int userId,Map<String, List<ErrorDetails>> errorMap,List<String> autoShipRefNbrs ) {
		Map<String,ConsignmentConfig> consignmentConfig = fetchConfigDetails(request, userId, "D2ZAPI"+dao.fetchNextFileSeqId(),errorMap);
		List<SenderdataMaster> savedData = new ArrayList<SenderdataMaster>();
        
		consignmentConfig.forEach((serviceType,config) ->{
			
			config.getSupplierConsignmentMap().forEach((supplier,consignments) -> {
			
				if(supplier.getSupplierName().contains("PFL")){
					List<ConsignmentDTO> invalidConsignment = consignments.stream().filter(data->isAddressInvalid(data)).collect(Collectors.toList());
					consignments.removeAll(invalidConsignment);
					if(consignments.size()>0) {
						savedData.addAll(makeCalltoPFL(consignments, supplier,errorMap));
					}
					if(invalidConsignment.size()>0) {
						savedData.addAll(generateBarcode(invalidConsignment));
					}
				}
				else if(supplier.getSupplierName().contains("ETOWER")) {
					savedData.addAll(makeCalltoEtower(consignments, supplier,errorMap));
				}
				else if(supplier.getSupplierName().contains("D2Z")) {
					savedData.addAll(generateBarcode(consignments));
				}else if(supplier.getSupplierName().contains("TRANSVIRTUAL")){
					savedData.addAll(saveTransvirtualConsignments(consignments,supplier.getSupplierName()));
				}else if(supplier.getSupplierName().contains("VELOCE")) {
					savedData.addAll(saveVeloceConsignments(consignments, supplier));
				}
				else{
					savedData.addAll(saveConsignment(consignments));
				}
				
				
				
			});
			
			if(config.isAutoShipmentIndicator()) {
				autoShipRefNbrs.addAll(savedData.stream().map(SenderdataMaster :: getReference_number).collect(Collectors.toList()));
			}
			
		});
		
		return savedData;
	}
	
private boolean isAddressInvalid(ConsignmentDTO data) {
	String addr1 = data.getConsigneeAddr1();
	String addr2 = data.getConsigneeAddr2();
	boolean isInvalidAddr1 = D2ZValidator.isPFLAddressValid(addr1.replaceAll("[^a-zA-Z0-9]", ""));
	boolean isInvalidAddr2 = false;
	if(addr2!=null) {
		isInvalidAddr2 = D2ZValidator.isPFLAddressValid(addr2.replaceAll("[^a-zA-Z0-9]", ""));
	}
	return isInvalidAddr1 || isInvalidAddr2;
	}
private List<SenderdataMaster> makeCalltoEtower(List<ConsignmentDTO> consignments, SupplierEntity supplier,
		Map<String, List<ErrorDetails>> errorMap) {
	List<SenderdataMaster> savedData = new ArrayList<SenderdataMaster>();
	try {
		List<ConsignmentDTO> modifiedData = etowerSupplier.createOrder(consignments, supplier);
		savedData = saveConsignment(modifiedData);
	} catch (FailureResponseException e) {
		consignments.forEach(obj->{
		ValidationUtils.populateErrorDetails(obj.getReferenceNumber(), "", "Shipment Error. Please contact us", errorMap);
		});
	}	
	return savedData;
	
}
	private List<SenderdataMaster> saveTransvirtualConsignments(List<ConsignmentDTO> consignments,String supplierName) {
		consignments.stream().forEach(obj->{
			obj.setCarrier(D2ZCommonUtil.getCarrierName(supplierName));
		});		
		return saveConsignment(consignments);
	}
	private List<SenderdataMaster> saveVeloceConsignments(List<ConsignmentDTO> consignments,SupplierEntity supplier) {
		consignments.stream().forEach(obj->{
			obj.setCarrier(D2ZCommonUtil.getCarrierName(supplier.getSupplierName()));
		});		
		new Thread(() -> veloceSupplier.createOrder(consignments, supplier)).start();
		return saveConsignment(consignments);

	}
	private  List<SenderdataMaster> generateBarcode(List<ConsignmentDTO> consignments) {
		List<SenderdataMaster> savedData = new ArrayList<SenderdataMaster>();

		consignments.stream().forEach(obj->{
			obj.setBarcodelabelNumber(null);
			obj.setCarrier("eParcel");
		});
		saveConsignment(consignments);
		dao.generateBarcode(consignments.get(0).getSenderFilesID());
		savedData = dao.fetchConsignmentsByRefNbr(consignments.stream().map(ConsignmentDTO :: getReferenceNumber).collect(Collectors.toList()));
		return savedData;
	}
	private List<SenderdataMaster> makeCalltoPFL(List<ConsignmentDTO> consignments, SupplierEntity supplier,
			Map<String, List<ErrorDetails>> errorMap) {
		List<SenderdataMaster> savedData = new ArrayList<SenderdataMaster>();
		try {
			List<ConsignmentDTO> modifiedData = pflSupplier.createOrder(consignments, supplier);
			savedData = saveConsignment(modifiedData);
		} catch (FailureResponseException e) {
			if("Not available because remote service!".equalsIgnoreCase(e.getMessage())) {
				generateBarcode(consignments);
			}else {
			consignments.forEach(obj->{
			ValidationUtils.populateErrorDetails(obj.getReferenceNumber(), "", "Shipment Error. Please contact us", errorMap);
			});
			}
		}
		return savedData;
	}
	public List<ConsignmentDTO> convertToDTO(List<SenderDataApi> request,int userId,String fileSeqId){

		List<ConsignmentDTO> senderDataList = new ArrayList<ConsignmentDTO>();
		for (SenderDataApi senderDataValue : request) {
			ConsignmentDTO senderDataObj = new ConsignmentDTO();
			senderDataObj.setUserID(userId);
			senderDataObj.setSenderFilesID(fileSeqId);
			senderDataObj.setReferenceNumber(senderDataValue.getReferenceNumber());
			senderDataObj.setConsigneeCompany(senderDataValue.getConsigneeCompany());
			senderDataObj.setConsigneeName(senderDataValue.getConsigneeName());
			senderDataObj.setConsigneeAddr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsigneeAddr2(senderDataValue.getConsigneeAddr2());
			senderDataObj.setConsigneeSuburb(senderDataValue.getConsigneeSuburb().trim());
			senderDataObj.setConsigneeState(senderDataValue.getConsigneeState().trim());
			senderDataObj.setConsigneePostcode(senderDataValue.getConsigneePostcode().trim());
			senderDataObj.setConsigneePhone(senderDataValue.getConsigneePhone());
			senderDataObj.setProductDescription(senderDataValue.getProductDescription());
			senderDataObj.setValue(senderDataValue.getValue());
			senderDataObj.setCurrency(senderDataValue.getCurrency());
			senderDataObj.setShippedQuantity(senderDataValue.getShippedQuantity());
			senderDataObj.setWeight(Double.valueOf(senderDataValue.getWeight()));
			senderDataObj.setDimensionsLength(senderDataValue.getDimensionsLength());
			senderDataObj.setDimensionsWidth(senderDataValue.getDimensionsWidth());
			senderDataObj.setDimensionsHeight(senderDataValue.getDimensionsHeight());
			senderDataObj.setCubicWeight(D2ZCommonUtil.calculateCubicWeight(senderDataValue.getServiceType(), senderDataObj.getWeight()));
			senderDataObj.setServicetype(senderDataValue.getServiceType());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			senderDataObj.setShipperName(senderDataValue.getShipperName());
			senderDataObj.setShipperAddr1(senderDataValue.getShipperAddr1());
			senderDataObj.setShipperCity(senderDataValue.getShipperCity());
			senderDataObj.setShipperState(senderDataValue.getShipperState());
			senderDataObj.setShipperPostcode(senderDataValue.getShipperPostcode());
			senderDataObj.setShipperCountry(senderDataValue.getShipperCountry());
			senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setCourier(senderDataValue.getCourier());
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
				} else if (senderDataValue.getBarcodeLabelNumber().length() < 20) {
					senderDataObj.setArticleId(senderDataValue.getBarcodeLabelNumber());
				} else {
					senderDataObj.setArticleId(senderDataValue.getBarcodeLabelNumber().substring(18));
				}
				if (senderDataValue.getBarcodeLabelNumber().length() == 41)
					senderDataObj.setMlid(senderDataValue.getBarcodeLabelNumber().substring(18, 23));
				else if (senderDataValue.getBarcodeLabelNumber().length() == 39)
					senderDataObj.setMlid(senderDataValue.getBarcodeLabelNumber().substring(18, 21));
				senderDataObj.setDatamatrix(senderDataValue.getDatamatrix());

			}
			if (null != senderDataValue.getCustReference() && !senderDataValue.getCustReference().isEmpty()) {
				senderDataObj.setCustReference(senderDataValue.getCustReference());
			} else {
				senderDataObj.setCustReference(senderDataValue.getDatamatrix());
			}
			senderDataObj.setInjectionState(senderDataValue.getInjectionState());
			senderDataObj.setConsigneeEmail(senderDataValue.getConsigneeEmail());
			senderDataObj.setStatus("CONSIGNMENT CREATED");
			senderDataObj.setInjectionType("Direct Injection");
			senderDataObj.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
			senderDataObj.setIsDeleted("N");
			senderDataObj.setCarrier(senderDataValue.getCarrier());
			senderDataList.add(senderDataObj);
		}
		return senderDataList;

	}
	
	@Override
	public List<SenderdataMaster> saveConsignment(List<ConsignmentDTO> request) {
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		for (ConsignmentDTO senderDataValue : request) {
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setUser_ID(senderDataValue.getUserID());
			senderDataObj.setSender_Files_ID(senderDataValue.getSenderFilesID());
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
			senderDataObj.setServicetype(senderDataValue.getServicetype());
			senderDataObj.setDeliverytype(senderDataValue.getDeliverytype());
			senderDataObj.setShipper_Name(senderDataValue.getShipperName());
			senderDataObj.setShipper_Addr1(senderDataValue.getShipperAddr1());
			senderDataObj.setShipper_City(senderDataValue.getShipperCity());
			senderDataObj.setShipper_State(senderDataValue.getShipperState());
			senderDataObj.setShipper_Postcode(senderDataValue.getShipperPostcode());
			senderDataObj.setShipper_Country(senderDataValue.getShipperCountry());
			senderDataObj.setFilename(senderDataValue.getFilename());
			senderDataObj.setSku(senderDataValue.getSku());
			senderDataObj.setLabelSenderName(senderDataValue.getLabelSenderName());
			senderDataObj.setDeliveryInstructions(senderDataValue.getDeliveryInstructions());
			senderDataObj.setBarcodelabelNumber(senderDataValue.getBarcodelabelNumber());
			senderDataObj.setArticleId(senderDataValue.getArticleId());
			senderDataObj.setMlid(senderDataValue.getMlid());
			senderDataObj.setDatamatrix(senderDataValue.getDatamatrix());
			senderDataObj.setInjectionState(senderDataValue.getInjectionState());
			senderDataObj.setConsignee_Email(senderDataValue.getConsigneeEmail());
			senderDataObj.setStatus("CONSIGNMENT CREATED");
			senderDataObj.setInjectionType(senderDataValue.getInjectionType());
			senderDataObj.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
			senderDataObj.setIsDeleted("N");
			senderDataObj.setD2zRate(senderDataValue.getD2zRate());
			senderDataObj.setBrokerRate(senderDataValue.getBrokerRate());
			senderDataObj.setCarrier(senderDataValue.getCarrier());
			senderDataObj.setCust_reference(senderDataValue.getCustReference());
			senderDataObj.setTrackAndTrace(getTrackandTrace(senderDataValue));
			senderDataList.add(senderDataObj);
			
		}
		return dao.createConsignment(senderDataList);

	}

	private List<Trackandtrace> getTrackandTrace(ConsignmentDTO senderDataValue) {
		List<Trackandtrace> list = new ArrayList<Trackandtrace>();
		Trackandtrace trackAndTrace = new Trackandtrace();
		// trackAndTrace.setRowId(D2ZCommonUtil.generateTrackID());
		trackAndTrace.setUser_Id(String.valueOf(senderDataValue.getUserID()));
		trackAndTrace.setReference_number(senderDataValue.getReferenceNumber());
		trackAndTrace.setTrackEventCode("CC");
		trackAndTrace.setTrackEventDetails("CONSIGNMENT CREATED");
		trackAndTrace.setTrackEventDateOccured(D2ZCommonUtil.getAETCurrentTimestamp());
		trackAndTrace.setCourierEvents(null);
		trackAndTrace.setTrackSequence(1);
		trackAndTrace.setBarcodelabelNumber(senderDataValue.getBarcodelabelNumber());
		trackAndTrace.setFileName("SP");
		trackAndTrace.setAirwayBill(null);
		trackAndTrace.setSignerName(null);
		trackAndTrace.setSignature(null);
		trackAndTrace.setIsDeleted("N");
		trackAndTrace.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
		trackAndTrace.setArticleID(senderDataValue.getArticleId());
		list.add(trackAndTrace);
		return list;
	}
	@Override
	public ResponseEntity<Object> sendResponse(List<SenderdataMaster> savedData,Map<String, List<ErrorDetails>> errorMap,int intialRequestSize,List<String> autoshipRefNbrs,String userName) {
		CreateConsignmentResponse response = new CreateConsignmentResponse();
		List<SenderDataResponse> responseList = new ArrayList<SenderDataResponse>();
		HttpStatus status = null;
		if(errorMap.size() > 0) {
		    for(String refNbr : errorMap.keySet()) {
		    	SenderDataResponse responseData = new SenderDataResponse();
		    	responseData.setReferenceNumber(refNbr);
		    	responseData.setErrorDetails(errorMap.get(refNbr));
				responseList.add(responseData);
			}
		}	
		
		if(savedData.size()>0) {
		new Thread(() -> {
			if(autoshipRefNbrs.size()>0) {
				List<SenderdataMaster> data = savedData.stream().filter(obj ->autoshipRefNbrs.contains(obj.getReference_number())).collect(Collectors.toList());
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    			String shipmentNumber = userName+simpleDateFormat.format(new Date());
				allocator.autoAllocate(data, shipmentNumber);
			}
			
			List<SenderdataMaster> eTowerOrders = dao.fetchDataBasedonSupplier(savedData.stream().map(SenderdataMaster::getReference_number).collect(Collectors.toList()), "eTower");
			if(eTowerOrders.size()>0) {
				eTowerWrapper.makeCalltoEtower(eTowerOrders);
			}
			tracker.saveData(savedData);
		}).start();
		savedData.forEach(data->{
			SenderDataResponse responseData = new SenderDataResponse();
			if(data.getServicetype().equals("VC1")) {
	    	responseData.setBarcodeLabelNumber(data.getReference_number());
			}else {
		    responseData.setReferenceNumber(data.getReference_number());
		    responseData.setBarcodeLabelNumber(data.getBarcodelabelNumber());
			}
			responseData.setMessage("Success");
			responseList.add(responseData);
		});
		}
		if(intialRequestSize == savedData.size()) {
			status = HttpStatus.OK;
			response.setStatus("Success");
		}else if(savedData.size() ==0) {
			status = HttpStatus.BAD_REQUEST;
			ErrorDetails err = new ErrorDetails();
			err.setValue(errorMap.keySet().toString());
			response.setErrors(err);
			response.setStatus("Failure");
		}else if( savedData.size() < intialRequestSize) {
			ErrorDetails err = new ErrorDetails();
			err.setValue(errorMap.keySet().toString());
			response.setErrors(err);
			status = HttpStatus.PARTIAL_CONTENT;
			response.setStatus("Partial Success");
		}
		response.setResponseData(responseList);
		
			
	
		return new ResponseEntity(response,status);
	}


	
	

}
