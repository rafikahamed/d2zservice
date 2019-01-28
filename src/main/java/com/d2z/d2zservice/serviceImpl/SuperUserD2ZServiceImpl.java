package com.d2z.d2zservice.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.d2z.d2zservice.dao.ID2ZSuperUserDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.entity.User;
import com.d2z.d2zservice.entity.UserService;
import com.d2z.d2zservice.excelWriter.ShipmentDetailsWriter;
import com.d2z.d2zservice.model.ArrivalReportFileData;
import com.d2z.d2zservice.model.BrokerRatesData;
import com.d2z.d2zservice.model.D2ZRatesData;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.ETowerTrackingDetails;
import com.d2z.d2zservice.model.ResponseMessage;
import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserDetails;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.proxy.ETowerProxy;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@Service
public class SuperUserD2ZServiceImpl implements ISuperUserD2ZService{

	@Autowired
    private ID2ZSuperUserDao d2zDao;
	
	@Autowired
	ShipmentDetailsWriter shipmentWriter;
	
	@Autowired
	private ETowerProxy proxy;
	
	
	@Override
	public UserMessage uploadTrackingFile(List<UploadTrackingFileData> fileData){
		UserMessage userMsg = new UserMessage();
		List<Trackandtrace> insertedData = d2zDao.uploadTrackingFile(fileData);
		if(insertedData.isEmpty()) {
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
		if(insertedData.isEmpty()) {
			userMsg.setMessage("Failed to upload data");
			return userMsg;
		}
		userMsg.setMessage("Data uploaded successfully");
		return userMsg;
	}

	@Override
	public List<DropDownModel> brokerCompanyDetails() {
		List<String> listOfCompany = d2zDao.brokerCompanyDetails();
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
	public UserDetails fetchUserDetails(String companyName) {
		User user = d2zDao.fetchUserDetails(companyName);
		UserDetails userDetails = new UserDetails();
		userDetails.setAddress(user.getAddress());
		userDetails.setCompanyName(user.getCompanyName());
		userDetails.setContactName(user.getName());
		userDetails.setContactPhoneNumber(user.getPhoneNumber());
		userDetails.setCountry(user.getCountry());
		userDetails.setEmailAddress(user.getEmailAddress());
		userDetails.setPassword(user.getUser_Password());
		userDetails.setPostCode(user.getPostcode());
		userDetails.setState(user.getState());
		userDetails.setSuburb(user.getSuburb());
		userDetails.setUserName(user.getUser_Name());
		Set<UserService> userServiceList = user.getUserService();
		List<String> serviceType = userServiceList.stream().map(obj ->{
			return obj.getServiceType();}).collect(Collectors.toList());
		userDetails.setServiceType(serviceType);
		return userDetails;
	
	}

	@Override
	public List<SenderdataMaster> exportDeteledConsignments(String fromDate, String toDate) {
		return d2zDao.exportDeteledConsignments(fromDate,toDate);
		//byte[] bytes = shipmentWriter.generateDeleteConsignmentsxls(deletedConsignments);
		//return bytes;
	}
	@Override
	public List<SenderdataMaster> exportConsignmentData(String fromDate, String toDate) {
		return d2zDao.exportConsignments(fromDate, toDate);
	}
	@Override
	public List<SenderdataMaster> exportShipmentData(String fromDate, String toDate) {
		return d2zDao.exportShipment(fromDate, toDate);
	}

	@Override
	public ResponseMessage trackingEvent(List<String> trackingNumbers) {
		System.out.println(trackingNumbers);
		List<List<ETowerTrackingDetails>> response = proxy.makeCallForTrackingEvents(trackingNumbers);
		//List<List<ETowerTrackingDetails>> response = proxy.stubETower();
		return d2zDao.insertTrackingDetails(response);
	}

	@Scheduled(cron = "0 0 0/2 * * ?")
	//@Scheduled(cron = "0 0/10 * * * ?")
	public void scheduledTrackingEvent() {
		List<String> trackingNumbers = d2zDao.fetchTrackingNumbersForETowerCall();
		if(trackingNumbers.isEmpty()) {
			System.out.println("ETower call not required");
		}
		else {
			trackingEvent(trackingNumbers);
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
	

}
