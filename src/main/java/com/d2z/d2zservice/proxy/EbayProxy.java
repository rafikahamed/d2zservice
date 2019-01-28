package com.d2z.d2zservice.proxy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.d2z.d2zservice.model.Ebay_Shipment;
import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.sdk.call.CompleteSaleCall;
import com.ebay.soap.eBLBaseComponents.CompleteSaleRequestType;
import com.ebay.soap.eBLBaseComponents.CompleteSaleResponseType;
import com.ebay.soap.eBLBaseComponents.ShipmentTrackingDetailsType;
import com.ebay.soap.eBLBaseComponents.ShipmentType;

@Service
public class EbayProxy {

	public CompleteSaleResponseType makeCalltoEbay_CompleteSale(Ebay_Shipment shipmentRequest, String clientEbayToken) {
	      ApiContext apiContext = new ApiContext();
	      ApiCredential apiCredential = new ApiCredential();
	      ApiAccount apiAccount = new ApiAccount();
	      apiCredential.seteBayToken(clientEbayToken);
	      apiAccount.setApplication("D2ZLogis-Tracking-SBX-c392af54d-d4749d10");
	      apiAccount.setDeveloper("0a67b6a8-6e56-4b90-83c2-5efd2851c7bc");
	      apiAccount.setCertificate("SBX-392af54dbf1c-46dd-4041-931f-6388");
	      apiCredential.setApiAccount(apiAccount);
	      apiContext.setApiCredential(apiCredential);
	      apiContext.setApiServerUrl("https://api.sandbox.ebay.com/wsapi");
	      CompleteSaleCall completeSaleCall = new CompleteSaleCall(apiContext);
	      CompleteSaleRequestType req =  new CompleteSaleRequestType();
	      req.setItemID(shipmentRequest.getReferenceNumber());
	      ShipmentType shipment = new ShipmentType();
	      ShipmentTrackingDetailsType shipmentDetails = new ShipmentTrackingDetailsType();
	      shipmentDetails.setShippingCarrierUsed(shipmentRequest.getCarrierUsed());
	      shipmentDetails.setShipmentTrackingNumber(shipmentRequest.getShipmentTrackingNumber());
	     List<ShipmentTrackingDetailsType> shipmentList = new ArrayList<ShipmentTrackingDetailsType>();
	     shipmentList.add(shipmentDetails);
	      shipment.setShipmentTrackingDetails(shipmentList.toArray(new ShipmentTrackingDetailsType[shipmentList.size()] ));
	      req.setShipment(shipment);
	      CompleteSaleResponseType resp = null;
		try {
			resp = (CompleteSaleResponseType) completeSaleCall.execute(req);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return resp;
	      
	}
}