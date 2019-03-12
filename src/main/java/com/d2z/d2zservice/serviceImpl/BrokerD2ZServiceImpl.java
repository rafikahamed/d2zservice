package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZBrokerDao;
import com.d2z.d2zservice.entity.Consignments;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.BaggingRequest;
import com.d2z.d2zservice.model.BaggingResponse;
import com.d2z.d2zservice.model.Bags;
import com.d2z.d2zservice.model.DirectInjectionDetails;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.service.IBrokerD2ZService;

@Service
public class BrokerD2ZServiceImpl implements IBrokerD2ZService{
	
	@Autowired
    private ID2ZBrokerDao d2zDao;
	
	@Override
	public List<DropDownModel> companyDetails(String brokerId) {
		List<String> listOfCompany = d2zDao.companyDetails(brokerId);
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		for(String companyName:listOfCompany) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(companyName);
			dropDownVaL.setValue(companyName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}

	@Override
	public UserDetails fetchUserDetails(String companyName, String roleId) {
		User user = d2zDao.fetchUserDetails(companyName, roleId);
		UserDetails userDetails = new UserDetails();
		if(user != null) {
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
			userDetails.seteBayToken(user.getEBayToken());
			Set<UserService> userServiceList = user.getUserService();
			List<String> serviceType = userServiceList.stream().map(obj -> { 
				 System.out.println("filter: " + obj);
				return obj.getServiceType();}).collect(Collectors.toList());
			userDetails.setServiceType(serviceType);
		}
		return userDetails;
	}

	@Override
	public List<DropDownModel> getManifestList(Integer userId) {
		List<Integer> listOfClientId = d2zDao.getClientId(userId);
		List<String> listOfManifestId = d2zDao.getManifestList(listOfClientId);
		List<DropDownModel> manifestDropDownList= new ArrayList<DropDownModel>();
		for(String manifestId:listOfManifestId) {
			if( null != manifestId &&  !"undefined".equalsIgnoreCase(manifestId)) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(manifestId);
				dropDownVaL.setValue(manifestId);
				manifestDropDownList.add(dropDownVaL);
			}
		}
		return manifestDropDownList;
	}

	@Override
	public List<SenderdataMaster> consignmentDetails(String manifestNumber, Integer userId) {
		List<Integer> listOfClientId = d2zDao.getClientId(userId);
		List<SenderdataMaster> consignmentDetails = d2zDao.consignmentDetails(manifestNumber,listOfClientId);
		return consignmentDetails;
	}

	@Override
	public List<DropDownModel> fetchShipmentList(Integer userId) {
		List<Integer> listOfClientId = d2zDao.getClientId(userId);
		List<String> listOfShipment = d2zDao.fetchShipmentList(listOfClientId);
		List<DropDownModel> shipmentDropDownList= new ArrayList<DropDownModel>();
		for(String manifestId:listOfShipment) {
			if(manifestId != null) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(manifestId);
				dropDownVaL.setValue(manifestId);
				shipmentDropDownList.add(dropDownVaL);
			}
		}
		return shipmentDropDownList;
	}

	@Override
	public List<DirectInjectionDetails> directInjection(String companyName) {
		List<DirectInjectionDetails> directInjectionDetails = new ArrayList<DirectInjectionDetails>();
		DirectInjectionDetails directInjection = null;
		List<String> trackingService = d2zDao.directInjection(companyName);
		Iterator itr = trackingService.iterator();
		 while(itr.hasNext()) {   
			 Object[] obj = (Object[]) itr.next();
			 directInjection = new DirectInjectionDetails();
			 if(null != obj[0] )
				 directInjection.setReferenceNumber(obj[0].toString());
			 if(null != obj[1])
				 directInjection.setArticleId(obj[1].toString());
			 if(null != obj[2])
				 directInjection.setConsigneeName(obj[2].toString());
			 if(null != obj[3])
				 directInjection.setPostCode(obj[3].toString());
			 if(null != obj[4])
				 directInjection.setWeight(obj[4].toString());
			 if(null != obj[5])
				 directInjection.setShipperName(obj[5].toString());
			 directInjectionDetails.add(directInjection);
        }
		return directInjectionDetails;
	}

	@Override
	public List<DropDownModel> fetchApiShipmentList() {
		List<String> listOfApiShipment = d2zDao.fetchApiShipmentList();
		List<DropDownModel> apiShipmentDropDownList= new ArrayList<DropDownModel>();
		for(String shipmentId:listOfApiShipment) {
			if(shipmentId != null) {
				DropDownModel dropDownVaL = new DropDownModel();
				dropDownVaL.setName(shipmentId);
				dropDownVaL.setValue(shipmentId);
				apiShipmentDropDownList.add(dropDownVaL);
			}
		}
		return apiShipmentDropDownList;
	}

	@Override
	public List<SenderdataMaster> downloadShipmentData(String shipmentNumber, Integer userId) {
		// TODO Auto-generated method stub
		List<Integer> listOfClientId = d2zDao.getClientId(userId);
		return d2zDao.fetchShipmentData(shipmentNumber,listOfClientId);
	}


	@Override
	public BaggingResponse getbagDetails(BaggingRequest request) {
		BaggingResponse baggingResponse = new BaggingResponse();
		User user = d2zDao.login(request.getUserName(), request.getPassword());
		if(null==user) {
			baggingResponse.setResponseMessage("Invalid UserName or Password");
			return baggingResponse;
		}
		List<Bags> bags = new ArrayList<>();
		List<Consignments> consignmentsDB = d2zDao.fetchConsignmentsByState(request.getReferenceNumbers());
		Map<String, List<Consignments>> grouped = new HashMap<String, List<Consignments>>();
		grouped = consignmentsDB.stream().collect(Collectors.groupingBy(Consignments::getStateCode));
		List<List<Consignments>> consignmentsByState = grouped.values().stream().collect(Collectors.toList());
		System.out.println("NumberofGroups: " +consignmentsByState.size());
		for(List<Consignments> consignments : consignmentsByState) {
			System.out.println("Consignments - "+consignments.get(0).getStateCode()+" : "+consignments);
			Collections.sort(consignments, new Comparator<Consignments>() {
			    public int compare(Consignments c1, Consignments c2) {
			        return Double.compare(c1.getWeight(), c2.getWeight());
			    }
			});
			System.out.println("Sorted Consignment - "+consignments.get(0).getStateCode()+" : "+consignments);
			
				Bags bag = null;
				putIntoBag(user.getUser_Id(),bags,consignments,bag,false);
			
			
			baggingResponse.setBags(bags);
			baggingResponse.setResponseMessage("Success");
		}
		return baggingResponse;
	}

	private void putIntoBag(int userid,List<Bags> bags,List<Consignments> consignments,Bags bag,boolean isBagFull) {
		int length = consignments.size();
		if(length<=0) {
			bags.add(bag);
			return;
		}
		if(null==bag || isBagFull) {
			System.out.println("New Bag");
			bag = new Bags(22);
			isBagFull=false;
			Random rnd = new Random();
			int uniqueNumber = 100000 + rnd.nextInt(900000);
			bag.setBagId(userid+"_"+consignments.get(0).getStateCode()+"_"+uniqueNumber);
		}
		System.out.println("Consignments - "+consignments);
		System.out.println("Consignments in Bag - "+bag.getConsignments());
		if(length>0) {
		if(length==1) {
			if(consignments.get(length-1).getWeight() <= bag.getAvailableWeight()) {
				Consignments consignment = consignments.get(length-1);
				com.d2z.d2zservice.model.Consignments consignmentInBag = new com.d2z.d2zservice.model.Consignments();
				consignmentInBag.setReferenceNumber(consignment.getReferenceNumber());
				consignmentInBag.setStateCode(consignment.getStateCode());
				consignmentInBag.setWeight(consignment.getWeight());
				bag.getConsignments().add(consignmentInBag);
				System.out.println("added to Bag 1- "+bag.getConsignments());
				bag.setAvailableWeight(bag.getAvailableWeight() - consignment.getWeight());
				consignments.remove(length-1);
			}
			else {
				bags.add(bag);
				isBagFull = true;
			}
			
		}
		if(length>1) {
			System.out.println(length+" : "+consignments.size());
			if(bag.getAvailableWeight()<=0) {
				bags.add(bag);
				isBagFull = true;
			}
			if(bag.getAvailableWeight()>0)// && bag.getAvailableWeight()<=20) 
				{
				Consignments consignment = consignments.get(length-1);
				if(consignment.getWeight() <= bag.getAvailableWeight()) {
					com.d2z.d2zservice.model.Consignments consignmentInBag = new com.d2z.d2zservice.model.Consignments();
					consignmentInBag.setReferenceNumber(consignment.getReferenceNumber());
					consignmentInBag.setStateCode(consignment.getStateCode());
					consignmentInBag.setWeight(consignment.getWeight());
					bag.getConsignments().add(consignmentInBag);
					System.out.println("added to Bag from last- "+bag.getConsignments());
					bag.setAvailableWeight(bag.getAvailableWeight() - consignment.getWeight());
					consignments.remove(length-1);
				}
				
			}
			if(bag.getAvailableWeight()>0 )//&& bag.getAvailableWeight()<=20)
				{
				if(consignments.get(0).getWeight()<=bag.getAvailableWeight()) {
					Consignments consignment = consignments.get(0);
					com.d2z.d2zservice.model.Consignments consignmentInBag = new com.d2z.d2zservice.model.Consignments();
					consignmentInBag.setReferenceNumber(consignment.getReferenceNumber());
					consignmentInBag.setStateCode(consignment.getStateCode());
					consignmentInBag.setWeight(consignment.getWeight());
					bag.getConsignments().add(consignmentInBag);
					System.out.println("added to Bag from first- "+bag.getConsignments());
					bag.setAvailableWeight(bag.getAvailableWeight() - consignment.getWeight());
					consignments.remove(0);
				}
				else {
					bags.add(bag);
					isBagFull=true;
			}
		}

		}
		putIntoBag(userid,bags, consignments,bag,isBagFull);

	}
		
	}

}
