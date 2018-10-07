package com.d2z.d2zservice.daoImpl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.model.EditConsignmentRequest;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.repository.PostcodeZoneRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.repository.UserRepository;
import com.d2z.d2zservice.repository.UserServiceRepository;
import com.d2z.singleton.D2ZSingleton;

@Repository
public class D2ZDaoImpl implements ID2ZDao{
	
	@Autowired
	SenderDataRepository senderDataRepository;

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Autowired
	PostcodeZoneRepository postcodeZoneRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserServiceRepository userServiceRepository;

	@Override
	public List<FileUploadData> exportParcel(List<FileUploadData> fileData) {
		List<SenderdataMaster> fileObjList = new ArrayList<SenderdataMaster>();
		String fileSeqId = "D2Z"+senderDataRepository.fetchNextSeq();
		for(FileUploadData fileDataValue: fileData) {
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(fileDataValue.getReferenceNumber());
			senderDataObj.setConsignee_name(fileDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(fileDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_Suburb(fileDataValue.getConsigneeSubUrb());
			senderDataObj.setConsignee_State(fileDataValue.getConsigneeState());
			senderDataObj.setConsignee_Postcode(fileDataValue.getConsigneePostCode());
			senderDataObj.setConsignee_Phone(fileDataValue.getConsigneePhone());
			senderDataObj.setProduct_Description(fileDataValue.getProductDes());
			senderDataObj.setValue(fileDataValue.getValue());
			senderDataObj.setShippedQuantity(fileDataValue.getShippedQty());
			senderDataObj.setWeight( fileDataValue.getCubicWeight().doubleValue());
			senderDataObj.setDimensions_Length(fileDataValue.getDimLength());
			senderDataObj.setDimensions_Width(fileDataValue.getDimWidth());
			senderDataObj.setDimensions_Height(fileDataValue.getDimHeight());
			senderDataObj.setServicetype(fileDataValue.getServiceType());
			senderDataObj.setDeliverytype(fileDataValue.getDeliveryType());
			senderDataObj.setShipper_Name(fileDataValue.getShipperName());
			senderDataObj.setShipper_Addr1(fileDataValue.getShipperAddrs1());
			senderDataObj.setShipper_Addr2(fileDataValue.getShipperAddres2());
			senderDataObj.setShipper_City(fileDataValue.getShipperCity());
			senderDataObj.setShipper_State(fileDataValue.getShipperState());
			senderDataObj.setShipper_Postcode(fileDataValue.getShipperPostCode());
			senderDataObj.setShipper_Country(fileDataValue.getShipperCountry());
			senderDataObj.setFilename(fileDataValue.getFileName());
			fileObjList.add(senderDataObj);
		}
		senderDataRepository.saveAll(fileObjList);
		//Calling Store Procedure
		senderDataRepository.inOnlyTest(fileSeqId);
		return fileData;
	}

	@Override
	public List<String> fileList() {
		List<String> listOfFileNames= senderDataRepository.fetchFileName();
		return listOfFileNames;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> listOfFileNames= senderDataRepository.fetchConsignmentData(fileName);
		return listOfFileNames;
	}
	
	@Override
	public List<SenderdataMaster> fetchManifestData(String fileName) {
		List<SenderdataMaster> allConsignmentData= senderDataRepository.fetchManifestData(fileName);
		return allConsignmentData;
	}

	@Override
	public String consignmentDelete(String refrenceNumlist) {
		//Calling Delete Store Procedure
		senderDataRepository.consigneeDelete(refrenceNumlist);
		return "Data Saved Successfully";
	}

	@Override
	public List<String> trackingDetails(String fileName) {
		List<String> trackingDetails= senderDataRepository.fetchTrackingDetails(fileName);
		return trackingDetails;
	}

	@Override
	public String trackingLabel(String refBarNum) {
		String trackingDetails= senderDataRepository.fetchTrackingLabel(refBarNum);
		return trackingDetails;
	}

	@Override
	public String manifestCreation(String manifestNumber, String refrenceNumber) {
		//Calling Delete Store Procedure
		senderDataRepository.manifestCreation(manifestNumber, refrenceNumber);
		return "Manifest Updated Successfully";
	}

	public List<Trackandtrace> trackParcel(String refNbr) {
		List<Trackandtrace> trackAndTrace = trackAndTraceRepository.fetchTrackEventByRefNbr(refNbr);
		return trackAndTrace;
	}


	@Override
	public String createConsignments(List<SenderData> orderDetailList) {
		
		Map<String,String> postCodeStateMap = D2ZSingleton.getInstance().getPostCodeStateMap();

		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		String fileSeqId = "D2ZAPI"+senderDataRepository.fetchNextSeq();
		for(SenderData senderDataValue: orderDetailList) {
			SenderdataMaster senderDataObj = new SenderdataMaster();
			senderDataObj.setSender_Files_ID(fileSeqId);
			senderDataObj.setReference_number(senderDataValue.getReferenceNumber());
			senderDataObj.setConsignee_name(senderDataValue.getConsigneeName());
			senderDataObj.setConsignee_addr1(senderDataValue.getConsigneeAddr1());
			senderDataObj.setConsignee_Suburb(senderDataValue.getConsigneeSuburb());
			senderDataObj.setConsignee_State(postCodeStateMap.get(senderDataValue.getConsigneePostcode()));
			senderDataObj.setConsignee_Postcode(senderDataValue.getConsigneePostcode());
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
			senderDataObj.setShipper_Name(senderDataValue.getShipperName());
			senderDataObj.setShipper_Addr1(senderDataValue.getShipperAddr1());
			senderDataObj.setShipper_Addr2(senderDataValue.getShipperAddr2());
			senderDataObj.setShipper_City(senderDataValue.getShipperCity());
			senderDataObj.setShipper_State(senderDataValue.getShipperState());
			senderDataObj.setShipper_Postcode(senderDataValue.getShipperPostcode());
			senderDataObj.setShipper_Country(senderDataValue.getShipperCountry());
			//senderDataObj.setFilename("D2ZAPI"+D2ZCommonUtil.getCurrentTimestamp());
			senderDataObj.setFilename(senderDataValue.getFileName());
			senderDataList.add(senderDataObj);
		}
		List<SenderdataMaster> insertedOrder = (List<SenderdataMaster>) senderDataRepository.saveAll(senderDataList);
		senderDataRepository.inOnlyTest(fileSeqId);
		return fileSeqId;
	}

    public List<PostcodeZone> fetchAllPostCodeZone(){
    	List<PostcodeZone> postCodeZoneList= (List<PostcodeZone>) postcodeZoneRepository.findAll();
    	System.out.println(postCodeZoneList.size());
    	return postCodeZoneList;
    }
    
    public List<String> fetchAllReferenceNumbers(){
    	List<String> referenceNumber_DB= senderDataRepository.fetchAllReferenceNumbers();
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

	public String editConsignments(List<EditConsignmentRequest> requestList) {
		/*requestList.forEach(obj->{
			senderDataRepository.editConsignments(obj.getReferenceNumber(), obj.getWeight());
		});*/
		Timestamp start = Timestamp.from(Instant.now());
		List<SenderdataMaster> senderDataList = new ArrayList<SenderdataMaster>();
		for(EditConsignmentRequest obj : requestList) {
			SenderdataMaster senderData = senderDataRepository.fetchByReferenceNumbers(obj.getReferenceNumber());
			senderData.setWeight(obj.getWeight());
			senderDataList.add(senderData);
		}
		senderDataRepository.saveAll(senderDataList);
		Timestamp end = Timestamp.from(Instant.now());
		long callDuration = end.getTime() - start.getTime();
		System.out.println("Call Duration : "+callDuration);
		return "Weight updated successfully!";
	}

	@Override
	public String allocateShipment(String referenceNumbers, String shipmentNumber) {
		senderDataRepository.allocateShipment(referenceNumbers, shipmentNumber);
		return "Shipment allocation Successful";
	}

	@Override
	public User addUser(UserDetails userData) {
		User userObj =new User();
		userObj.setCompanyName(userData.getCompanyName());
		userObj.setAddress(userData.getAddress());
		userObj.setSuburb(userData.getSuburb());
		userObj.setState(userData.getState());
		userObj.setPostcode(userData.getPostCode());
		userObj.setCountry(userData.getCountry());
		userObj.setEmailAddress(userData.getEmailAddress());
		userObj.setUser_Name(userData.getUserName());
		userObj.setUser_Password(userData.getPassword());
		userObj.setRole_Id(3);
		userObj.setName(userData.getContactName());
		userObj.setPhoneNumber(userData.getContactPhoneNumber());
		userObj.setUser_IsDeleted(false);
		userObj.setTimestamp(Timestamp.from(Instant.now()));
		userObj.setModifiedTimestamp(Timestamp.from(Instant.now()));
		User savedUser = userRepository.save(userObj);
		return savedUser;
	}

	@Override
	public List<UserService> addUserService(User user,List<String> serviceTypeList) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		for(String serviceType : serviceTypeList) {
		UserService userService = new UserService();
		userService.setUserId(user.getUser_Id());
		userService.setCompanyName(user.getCompanyName());
		userService.setUser_Name(user.getUser_Name());
		userService.setServiceType(serviceType);
		userService.setTimestamp(Timestamp.from(Instant.now()));
		userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
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
		if(!userDetails.getServiceType().isEmpty()) {
			for(String serviceType : userDetails.getServiceType() ) {
				UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType);
				if(userService == null) {
					UserService newUserService = new UserService();
					newUserService.setUserId(existingUser.getUser_Id());
					newUserService.setCompanyName(existingUser.getCompanyName());
					newUserService.setUser_Name(existingUser.getUser_Name());
					newUserService.setServiceType(serviceType);
					newUserService.setTimestamp(Timestamp.from(Instant.now()));
					newUserService.setModifiedTimestamp(Timestamp.from(Instant.now()));
					userServiceList.add(newUserService);			
					}
				else {
					if(userService.isService_isDeleted()) {
						userService.setService_isDeleted(false);
						userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
						userServiceList.add(userService);
					}
				}
			}
		}
		if(!userDetails.getDeletedServiceTypes().isEmpty()) {
			for(String serviceType : userDetails.getDeletedServiceTypes() ) {
				UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType);
				if(userService!=null) {
					userService.setService_isDeleted(true);
					userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
					userServiceList.add(userService);
				}
				
		}
		}
		userServiceRepository.saveAll(userServiceList);

	}

	/*private void deleteUserService(User existingUser, List<String> deletedServiceTypes) {
		List<UserService> userServiceList = new ArrayList<UserService>();
		if(!deletedServiceTypes.isEmpty()) {
		for(String serviceType : deletedServiceTypes ) {
			UserService userService  = userServiceRepository.fetchbyCompanyNameAndServiceType(existingUser.getCompanyName(), serviceType);
			if(userService!=null) {
				userService.setService_isDeleted(true);
				userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
				userServiceList.add(userService);
			}
			
	}
		userServiceRepository.saveAll(userServiceList);
	}
	}*/

	@Override
	public String deleteUser(String companyName) {
		User existingUser = userRepository.fetchUserbyCompanyName(companyName);
		if(existingUser==null) {
			return "Company Name does not exist";
		}
		else {
			existingUser.setUser_IsDeleted(true);
			existingUser.setModifiedTimestamp(Timestamp.from(Instant.now()));
			userRepository.save(existingUser);
			List<UserService> userService_DB  = userServiceRepository.fetchbyCompanyName(companyName);
			List<UserService> userServiceList = new ArrayList<UserService>();
			for(UserService userService: userService_DB) {
				userService.setService_isDeleted(true);
				userService.setModifiedTimestamp(Timestamp.from(Instant.now()));
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
	public List<SenderdataMaster> fetchShipmentData(String shipmentNumber) {
		List<SenderdataMaster> senderData = senderDataRepository.fetchShipmentData(shipmentNumber);
		return senderData;
	}


}
