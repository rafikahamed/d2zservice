package com.d2z.d2zservice.daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.PostcodeZone;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.Trackandtrace;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.repository.PostcodeZoneRepository;
import com.d2z.d2zservice.repository.SenderDataRepository;
import com.d2z.d2zservice.repository.TrackAndTraceRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import com.d2z.singleton.D2ZSingleton;

@Repository
public class D2ZDaoImpl implements ID2ZDao{
	
	@Autowired
	SenderDataRepository senderDataRepository;

	@Autowired
	TrackAndTraceRepository trackAndTraceRepository;
	
	@Autowired
	PostcodeZoneRepository postcodeZoneRepository;
	
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
			senderDataObj.setFilename("D2ZAPI"+D2ZCommonUtil.getCurrentTimestamp());
			senderDataList.add(senderDataObj);
		}
		List<SenderdataMaster> insertedOrder = (List<SenderdataMaster>) senderDataRepository.saveAll(senderDataList);
		senderDataRepository.inOnlyTest(fileSeqId);
		return fileSeqId;
	}

    public List<PostcodeZone> fetchAllPostCodeZone(){
    	List<PostcodeZone> postCodeZoneList= postcodeZoneRepository.fetchAllData();
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

}
