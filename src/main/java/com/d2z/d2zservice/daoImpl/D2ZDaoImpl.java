package com.d2z.d2zservice.daoImpl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.repository.SenderDataRepository;

@Repository
public class D2ZDaoImpl implements ID2ZDao{
	
	@Autowired
	SenderDataRepository senderDataRepository;

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

}
