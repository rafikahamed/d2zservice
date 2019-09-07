package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.exception.EtowerFailureResponseException;
import com.d2z.d2zservice.exception.InvalidUserException;
import com.d2z.d2zservice.exception.MaxSizeCountException;
import com.d2z.d2zservice.model.CreateConsignmentRequest;
import com.d2z.d2zservice.model.PFLSenderDataRequest;
import com.d2z.d2zservice.model.PflCreateShippingOrderInfo;
import com.d2z.d2zservice.model.PflCreateShippingRequest;
import com.d2z.d2zservice.model.SenderDataApi;
import com.d2z.d2zservice.model.SenderDataResponse;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.service.ID2ZAPIService;
import com.d2z.d2zservice.util.ValidationUtils;
import com.d2z.d2zservice.model.ErrorDetails;
import com.d2z.d2zservice.validation.D2ZValidator;
import com.d2z.d2zservice.wrapper.ETowerWrapper;
import com.d2z.d2zservice.wrapper.PCAWrapper;
import com.d2z.d2zservice.wrapper.PFLWrapper;

@Service
public class D2ZAPIServiceImpl implements ID2ZAPIService{

	@Autowired
	private ID2ZDao d2zDao;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private D2ZValidator d2zValidator;

	@Autowired
	ETowerWrapper eTowerWrapper; 
	
	@Autowired
	PFLWrapper pflWrapper;
	
	@Autowired
	PCAWrapper pcaWrapper;

	
	@Override
	public void createConsignments(CreateConsignmentRequest orderDetail,  List<SenderDataResponse> senderDataResponseList ,Map<String, List<ErrorDetails>> errorMap) throws EtowerFailureResponseException {
		
		Integer userId = userRepository.fetchUserIdbyUserName(orderDetail.getUserName());
		if (userId == null) {
			throw new InvalidUserException("User does not exist", orderDetail.getUserName());
		}
		if (orderDetail.getConsignmentData().size() > 300) {
			throw new MaxSizeCountException("We are allowing max 300 records, Your Request contains - "
					+ orderDetail.getConsignmentData().size() + " Records");
		
		}
		d2zValidator.isReferenceNumberUnique(orderDetail,errorMap);
		d2zValidator.isServiceValid(orderDetail,errorMap);
		d2zValidator.isPostCodeValid(orderDetail,errorMap);
		ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
				
		String barcodeLabelNumber = orderDetail.getConsignmentData().get(0).getBarcodeLabelNumber();
		String datamatrix = orderDetail.getConsignmentData().get(0).getDatamatrix();
		if(null==barcodeLabelNumber || barcodeLabelNumber.trim().isEmpty() || null==datamatrix || datamatrix.trim().isEmpty()) {
	    String serviceType = orderDetail.getConsignmentData().get(0).getServiceType();
	    if("1PS".equalsIgnoreCase(serviceType) || "1PS2".equalsIgnoreCase(serviceType) || "1PM3E".equalsIgnoreCase(serviceType) 
				|| "1PS3".equalsIgnoreCase(serviceType) || "1PS5".equalsIgnoreCase(serviceType) || "2PSP".equalsIgnoreCase(serviceType)
				|| "1PM5".equalsIgnoreCase(serviceType) || "TST1".equalsIgnoreCase(serviceType)) {
			//d2zValidator.isPostCodeValid(orderDetail,errorMap);
			//ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			eTowerWrapper.makeCreateShippingOrderEtowerCallForAPIData(orderDetail,senderDataResponseList);
			}
			return;
			
		}else if ("FWM".equalsIgnoreCase(serviceType) || "FW".equalsIgnoreCase(serviceType)) {
			d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData(),errorMap);
			ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			makeCreateShippingOrderPFLCall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(), serviceType);
			}
			return;// senderDataResponseList;
		}else if("FWS".equalsIgnoreCase(serviceType)) {
			d2zValidator.isFWPostCodeValid(orderDetail.getConsignmentData(),errorMap);
			ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			pcaWrapper.makeCreateShippingOrderPFACall(orderDetail.getConsignmentData(),senderDataResponseList,orderDetail.getUserName(),serviceType);
			}
			return;// senderDataResponseList;
		}else if("MCM".equalsIgnoreCase(serviceType) || "MCM1".equalsIgnoreCase(serviceType) || "MCM2".equalsIgnoreCase(serviceType) 
					|| "MCM3".equalsIgnoreCase(serviceType) || "MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)){
			//ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
			if(orderDetail.getConsignmentData().size()>0) {
			PFLSenderDataRequest consignmentData = d2zValidator.isFWSubPostCodeValid(orderDetail);
			if(consignmentData.getPflSenderDataApi().size() > 0) {
				if("MCS".equalsIgnoreCase(serviceType) || "STS".equalsIgnoreCase(serviceType)) 	
					pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),serviceType);
				else
					makeCreateShippingOrderPFLCall(consignmentData.getPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(), serviceType);
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && "STS".equalsIgnoreCase(serviceType)) {
				pcaWrapper.makeCreateShippingOrderPFACall(consignmentData.getNonPflSenderDataApi(),senderDataResponseList,orderDetail.getUserName(),"STS-Sub");
			}
			
			if(consignmentData.getNonPflSenderDataApi().size() > 0 && !"STS".equalsIgnoreCase(serviceType)) {
				//d2zValidator.isPostCodeValid(orderDetail,errorMap);
				//ValidationUtils.removeInvalidconsignments(orderDetail,errorMap);
				if(orderDetail.getConsignmentData().size()>0) {
				String senderFileID = d2zDao.createConsignments(consignmentData.getNonPflSenderDataApi(), userId, orderDetail.getUserName(), null);
				List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
				Iterator itr = insertedOrder.iterator();
				while (itr.hasNext()) {
					Object[] obj = (Object[]) itr.next();
					SenderDataResponse senderDataResponse = new SenderDataResponse();
					senderDataResponse.setReferenceNumber(obj[0].toString());
					//senderDataResponse.setBarcodeLabelNumber(obj[3] != null ? obj[3].toString() : "");
					senderDataResponse.setCarrier(obj[4].toString());
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
					senderDataResponseList.add(senderDataResponse);
				}
				}
			}
			}
			return;
		}}
		if(orderDetail.getConsignmentData().size()>0) {
		String senderFileID = d2zDao.createConsignments(orderDetail.getConsignmentData(), userId, orderDetail.getUserName(), null);
		List<String> insertedOrder = d2zDao.fetchBySenderFileID(senderFileID);
		Iterator itr = insertedOrder.iterator();
		while (itr.hasNext()) {
			Object[] obj = (Object[]) itr.next();
			SenderDataResponse senderDataResponse = new SenderDataResponse();
			senderDataResponse.setReferenceNumber(obj[0].toString());
			String barcode = obj[1].toString();
			senderDataResponse.setBarcodeLabelNumber("]d2".concat(barcode.replaceAll("\\[|\\]", "")));
			senderDataResponse.setCarrier(obj[4].toString());
			senderDataResponse.setInjectionPort(obj[5] != null ? obj[5].toString() : "");
			senderDataResponseList.add(senderDataResponse);
		}
		}
		return; //senderDataResponseList;
	}
	

	private void makeCreateShippingOrderPFLCall (List<SenderDataApi> data,
			List<SenderDataResponse> senderDataResponseList, String userName, String serviceType) throws EtowerFailureResponseException {
		PflCreateShippingRequest pflRequest = new PflCreateShippingRequest();
		Map<String, String> systemRefNbrMap = new HashMap<String, String>();

		List<PflCreateShippingOrderInfo> pflOrderInfoRequest = new ArrayList<PflCreateShippingOrderInfo>();
		for (SenderDataApi orderDetail : data) {
			PflCreateShippingOrderInfo request = new PflCreateShippingOrderInfo();
			 Random rnd = new Random();
			 int uniqueNumber = 1000000 + rnd.nextInt(9000000);
    		 String sysRefNbr = "RTFG"+uniqueNumber;
    		 request.setCustom_ref(sysRefNbr);
    		 
			systemRefNbrMap.put(request.getCustom_ref(), orderDetail.getReferenceNumber());

			request.setCustom_ref(orderDetail.getReferenceNumber());
			request.setRecipientCompany(orderDetail.getConsigneeCompany());
			String recpName = orderDetail.getConsigneeName().length() > 34
					? orderDetail.getConsigneeName().substring(0, 34)
					: orderDetail.getConsigneeName();
			request.setRecipientName(recpName);
			request.setAddressLine1(orderDetail.getConsigneeAddr1());
			request.setAddressLine2(orderDetail.getConsigneeAddr2());
			request.setEmail(orderDetail.getConsigneeEmail());
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

	@Override
	public void makeCallToEtowerBasedonSupplierUI(List<String> incomingRefNbr) {
		 Runnable r = new Runnable( ) {			
		        public void run() {
		List<SenderdataMaster> eTowerOrders = d2zDao.fetchDataBasedonSupplier(incomingRefNbr,"eTower");
		eTowerWrapper.makeCalltoEtower(eTowerOrders);
	
		   }
    };
   new Thread(r).start();
	}
}
