package com.d2z.d2zservice.wrapper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.ETowerResponse;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.veloce.Consignment;
import com.d2z.d2zservice.model.veloce.ConsignmentRequest;
import com.d2z.d2zservice.model.veloce.ConsignmentResponse;
import com.d2z.d2zservice.model.veloce.ConsignmentStatus;
import com.d2z.d2zservice.model.veloce.RequestHeader;
import com.d2z.d2zservice.proxy.VeloceProxy;
import com.d2z.d2zservice.util.D2ZCommonUtil;

@Service
public class VeloceWrapper {
	
	@Value("${veloce.secret}")
	String secretKey;
	
	@Autowired
	VeloceProxy proxy;
	
	@Autowired
	private ID2ZDao d2zDao;


	public void makeCalltoVeloce(List<SenderDataApi> mlydata) {
		
		ConsignmentRequest request = constructVeloceRequest(mlydata);
		ConsignmentResponse response = proxy.makeCalltoVeloce(request);
		logResponse(response);
		
	}

	private void logResponse(ConsignmentResponse response) {

		List<ETowerResponse> responseEntity = new ArrayList<ETowerResponse>();
		for(ConsignmentStatus responseData : response.getBdr().getConsignment_status())
		{
			ETowerResponse errorResponse = new ETowerResponse();
			errorResponse.setAPIName("Veloce");
			errorResponse.setStatus(responseData.getSubmit_status());
			errorResponse.setOrderId(responseData.getTracking_id());
			errorResponse.setTrackingNo(responseData.getTracking_id());
			errorResponse.setReferenceNumber(responseData.getTracking_id());
			errorResponse.setTimestamp(Timestamp.valueOf(D2ZCommonUtil.getAETCurrentTimestamp()));
			responseEntity.add(errorResponse);
		}
		
		d2zDao.logEtowerResponse(responseEntity);	}

	private ConsignmentRequest constructVeloceRequest(List<SenderDataApi> mlydata) {
		ConsignmentRequest request = new ConsignmentRequest();
		String clientId = "veloce";
		
		RequestHeader hdr = new RequestHeader();
		hdr.setClient_id(clientId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String formattedDate = sdf.format(new Date());
		hdr.setRequest_date(formattedDate);
		String sign = clientId.concat(secretKey).concat(formattedDate);
		hdr.setRequest_sign(D2ZCommonUtil.sha1Encrpytion(sign)); 
		request.setHdr(hdr);
		List<Consignment> bdr = new ArrayList<Consignment>();
		for(SenderDataApi data : mlydata) {
			Consignment consignment = new Consignment();
			consignment.setCourier(data.getCourier());
			consignment.setCurrency("MYR");
			consignment.setHeight(data.getDimensionsHeight()==null?"":data.getDimensionsHeight().toString());
			consignment.setItem_desc(data.getProductDescription());
			consignment.setLength(data.getDimensionsLength()==null?"":data.getDimensionsLength().toString());
			consignment.setRecipient_address1(data.getConsigneeAddr1());
			consignment.setRecipient_address2(data.getConsigneeAddr2()==null?"":data.getConsigneeAddr2());
			consignment.setRecipient_city(data.getConsigneeSuburb());
			consignment.setRecipient_country("MALAYSIA");
			consignment.setRecipient_email(data.getConsigneeEmail()==null?"":data.getConsigneeEmail());
			consignment.setRecipient_name(data.getConsigneeName());
			consignment.setRecipient_phone(data.getConsigneePhone());
			consignment.setRecipient_postcode(data.getConsigneePostcode());
			consignment.setRecipient_state_name(data.getConsigneeState());
			consignment.setShipper_address1(data.getShipperAddr1()==null?"":data.getShipperAddr1());
			consignment.setShipper_city(data.getShipperCity()==null?"":data.getShipperCity());
			consignment.setShipper_country("MALAYSIA");
			consignment.setShipper_state_name(data.getShipperState()==null?"":data.getShipperState());
			consignment.setShipper_name(data.getShipperName()==null?"":data.getShipperName());
			consignment.setShipper_postcode(data.getShipperPostcode()==null?"":data.getShipperPostcode());
			consignment.setShipper_ref(data.getReferenceNumber());
			consignment.setTracking_id(data.getBarcodeLabelNumber());
			consignment.setWarehouse_code("VELOCE_WAREHOUSE");
			consignment.setWeight(data.getWeight());
			consignment.setValue(String.valueOf(data.getValue()));
			consignment.setWidth(data.getDimensionsWidth()==null?"":data.getDimensionsWidth().toString());
			bdr.add(consignment);
		}
		request.setBdr(bdr);
		return request;
	}
	
	

}
