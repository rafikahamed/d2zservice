package com.d2z.d2zservice.serviceImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.exception.FailureResponseException;
import com.d2z.d2zservice.exception.ReferenceNumberNotUniqueException;
import com.d2z.d2zservice.mapper.ConsignmentDTOMapper;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.service.ConsignmentCreator;
import com.d2z.d2zservice.service.ShipmentAllocator;
import com.d2z.d2zservice.supplier.EtowerSupplier;
import com.d2z.d2zservice.supplier.PFLSupplier;
import com.d2z.d2zservice.supplier.Tracker;
import com.d2z.d2zservice.util.D2ZCommonUtil;


@Service
public class ShipmentAllocatorImpl implements ShipmentAllocator {

	@Autowired 
	ID2ZDao dao;
	
	@Autowired
	PFLSupplier pflSupplier;
	
	@Autowired
	EtowerSupplier etowerSupplier;
	
	@Autowired
	Tracker tracker;
	
	@Autowired
	ConsignmentCreator creator;
	
	@Override
	public ResponseMessage allocateShipment(List<String> ids, String identifier, String shipmentNumber)
			throws ReferenceNumberNotUniqueException {
		
		  
		  
		  ResponseMessage userMsg = new ResponseMessage();
		  
		  
		  List<Object[]> consignments = dao.fetchAirwayBill(ids,identifier);
		  
		  List<String> invalidData = new ArrayList<String>();
		  List<String> validData = new ArrayList<String>();
		  if (consignments.isEmpty()) {
		  throw new ReferenceNumberNotUniqueException("Failure - Invalid Reference Numbers",ids); 
		  }
		  else if(consignments.size()<ids.size()) {
			  List<String> refNbr_DB = consignments.stream().map(obj -> { 
				  String id = fetchId(obj, identifier);
				  return id;
				  }).collect(Collectors.toList());
			  List<String> invalidRefNbrs = new ArrayList<String>(ids);
			  invalidRefNbrs.removeAll(refNbr_DB);			 
		      userMsg.setResponseMessage("Partial Success - Invalid Reference Numbers : " + String.join(",",invalidRefNbrs));
		    
		  }else 
		  { 
			  
			  consignments.forEach(data->{
				  if((String)data[0] == null) {
					  validData.add((String)data[1]);
				  }else {
					  invalidData.add(fetchId(data,identifier));					
				  }
			  }); 
		  } 
		
		  if (!invalidData.isEmpty()) {
				throw new ReferenceNumberNotUniqueException("Shipment is already allocated", invalidData);
			}
		  int updatedConsignments=0;
		  System.out.println(D2ZCommonUtil.convertToJsonString(validData));
		  if(validData.size()>0) {
			  updatedConsignments = dao.updateAirwayBill(validData,shipmentNumber);
		      insertIntoTrackandTrace(validData,shipmentNumber,identifier);
		  }
		  if(updatedConsignments == ids.size()) {
			  userMsg.setResponseMessage("Shipment Allocated Successfully");
		  }
		  
		  
		  return userMsg;
		  
		 
		}


	private String fetchId(Object[] data, String identifier) {

		if(identifier.equalsIgnoreCase("referenceNumber")) {
			return (String)data[1];
		}else {
			return (String)data[2];
		}
	
	}

	private void populateData(SenderdataMaster data, String shipmentNumber) {
		  data.setAirwayBill(shipmentNumber);
		  data.setStatus("SHIPMENT ALLOCATED");
		  data.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
	}

	@Override
	public List<Trackandtrace> insertSAintoTrackandTrace(List<SenderdataMaster> consignments,String shipmentNumber) {
		List<Trackandtrace> list = new ArrayList<Trackandtrace>();
		consignments.forEach(senderDataValue->{
		Trackandtrace trackAndTrace = new Trackandtrace();
		trackAndTrace.setUser_Id(String.valueOf(senderDataValue.getUser_ID()));
		trackAndTrace.setReference_number(senderDataValue.getReference_number());
		trackAndTrace.setTrackEventCode("SA");
		trackAndTrace.setTrackEventDetails("SHIPMENT ALLOCATED");
		trackAndTrace.setTrackEventDateOccured(D2ZCommonUtil.getAETCurrentTimestamp());
		trackAndTrace.setTrackSequence(2);
		trackAndTrace.setBarcodelabelNumber(senderDataValue.getBarcodelabelNumber());
		trackAndTrace.setAirwayBill(shipmentNumber);
		trackAndTrace.setConnoteNo(senderDataValue.getMlid());
		trackAndTrace.setIsDeleted("N");
		trackAndTrace.setTimestamp(D2ZCommonUtil.getAETCurrentTimestamp());
		trackAndTrace.setArticleID(senderDataValue.getArticleId());
		list.add(trackAndTrace);
		});
		return list;
	}
	
	
	private void insertIntoTrackandTrace(List<String> ids,String shipmentNumber,String identifier) {
		new Thread(()->
		{		
			List<SenderdataMaster> consignments = dao.fetchByRefNbr(ids);
			dao.insertIntoTrackandTrace(insertSAintoTrackandTrace(consignments,shipmentNumber));
			makeCallToSupplier(consignments);	
		}).start();
		
	}
	
	@Override
	public void autoAllocate(List<SenderdataMaster> consignments, String shipmentNumber) {
		consignments.forEach(data->{
			populateData(data,shipmentNumber);
		});
		List<SenderdataMaster> updatedConsignments = dao.createConsignment(consignments);
		dao.insertIntoTrackandTrace(insertSAintoTrackandTrace(consignments,shipmentNumber));
		makeCallToSupplier(updatedConsignments);
	}

	private void makeCallToSupplier(List<SenderdataMaster> updatedConsignments) {
		
			Map<String,List<SenderdataMaster>> identifierMap = 	dao.fetchallocationIdentifier(updatedConsignments);
			Map<List<SupplierEntity>,List<SenderdataMaster>> supplierConsignmentMap = fetchSupplierConsignmentMap(identifierMap);
		supplierConsignmentMap.forEach((supplierList,consignments) -> {
			
			if(supplierList.size()>1) {
				notifyShipmentAllocationToSupplier(supplierList.get(0),consignments);
				reLabelConsignment(supplierList.get(1),consignments);
			}else if (supplierList.size()==1) {
				notifyShipmentAllocationToSupplier(supplierList.get(0),consignments);
			}
		
		});
	}

    private void reLabelConsignment(SupplierEntity supplier, List<SenderdataMaster> consignments) {
    	if(supplier.getSupplierName().contains("ETOWER")) {
    		reLabelCallToEtower(supplier,consignments);
		}	
	}

	private void reLabelCallToEtower(SupplierEntity supplier, List<SenderdataMaster> consignments) {
		try {
			consignments.forEach(data->{
				data.setCust_reference(data.getReference_number());
				data.setReference_number(data.getBarcodelabelNumber());
			});
			List<ConsignmentDTO> consignmentDto = ConsignmentDTOMapper.INSTANCE.getConsignments(consignments);
			etowerSupplier.createOrder(consignmentDto, supplier);
			List<SenderdataMaster> updatedConsignment = creator.saveConsignment(consignmentDto);
			tracker.saveData(updatedConsignment);
		} catch (FailureResponseException e) {
			e.printStackTrace();
		}		
	}

	private void notifyShipmentAllocationToSupplier(SupplierEntity supplier,
			List<SenderdataMaster> consignments) {
    	if(supplier.getSupplierName().contains("PFL")){
			makeCalltoPFL(consignments, supplier);
		}
		else if(supplier.getSupplierName().contains("ETOWER")) {
			makeCalltoEtower(consignments, supplier);
		}
		/*else if(supplier.getSupplierName().contains("TRANSVIRTUAL")){
			makeCallToTransvirtual(consignments,supplier);
		}else if(supplier.getSupplierName().contains("VELOCE")) {
			saveVeloceConsignments(consignments, supplier);
		}*/else if(supplier.getSupplierName().contains("FDM")) {
			makeCallToFDM(consignments, supplier);
		}
		
	}

	private void makeCallToFDM(List<SenderdataMaster> consignments, SupplierEntity supplier) {
		// TODO Auto-generated method stub
		
	}

	private Map<List<SupplierEntity>,List<SenderdataMaster>> fetchSupplierConsignmentMap(Map<String,List<SenderdataMaster>> identifierMap) {
		Map<List<SupplierEntity>,List<SenderdataMaster>> map = new HashMap<List<SupplierEntity>,List<SenderdataMaster>>();
		
    	identifierMap.forEach((supplierId,consignments) -> {
			List<SupplierEntity> supplierDetailsList = new ArrayList<SupplierEntity>();
    		String[] arr = supplierId.split(",");
    		List<String> supplierList = Arrays.asList(arr);
    		supplierList.forEach(id -> {
    		if(StringUtils.isNumeric(id)) {
			int supplierAuthId = Integer.parseInt(id);
			if(supplierAuthId>0) {
				supplierDetailsList.add(dao.fetchSupplierData(supplierAuthId));
			}
    		}
    		});
			map.put(supplierDetailsList, consignments);
		});
    	return map;
    }

	private void makeCalltoEtower(List<SenderdataMaster> consignments, SupplierEntity supplier) {
		List<String> articleIds = consignments.stream().map(SenderdataMaster :: getArticleId).collect(Collectors.toList());
		etowerSupplier.allocateShipment(articleIds,supplier);
	}

	private void makeCalltoPFL(List<SenderdataMaster> consignments, SupplierEntity supplier) {
		List<String> orderIds = consignments.stream().map(SenderdataMaster :: getMlid).collect(Collectors.toList());
		ZoneId zoneId = ZoneId.of ( "Australia/Sydney" );
		int dayofWeek = LocalDate.now(zoneId).getDayOfWeek().getValue();
		if(dayofWeek>=5) {
			//Fri - Sun
			dao.updateForPFLSubmitOrder(orderIds,"PFLSubmitOrder");
		}else {
		try {
			pflSupplier.allocateShipment(orderIds,supplier);
		} catch (FailureResponseException e) {
			e.printStackTrace();
		}
		}
			
	}

	private String fetchId(SenderdataMaster data, String identifier) {
		if(identifier.equalsIgnoreCase("referenceNumber")) {
			return data.getReference_number();
		}else {
			return data.getArticleId();
		}
	}
	

}
