package com.d2z.d2zservice.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.d2z.d2zservice.model.SenderDataApi;


public class ConsignmentValidator  implements
					ConstraintValidator<ValidConsignment, SenderDataApi>{

	@Override
	public boolean isValid(SenderDataApi value, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		if(null == value.getReferenceNumber() || value.getReferenceNumber().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getReferenceNumber()+","+"Reference Number is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if(null == value.getConsigneeSuburb() || value.getConsigneeSuburb().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeSuburb()+","+"Consignee Suburb is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if(!(null != value.getServiceType() && (value.getServiceType().startsWith("MY") || value.getServiceType().startsWith("SG"))) && (null == value.getConsigneeState() || value.getConsigneeState().isEmpty())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeState()+","+"Consignee State is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if(null == value.getConsigneePostcode() || value.getConsigneePostcode().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneePostcode()+","+"Consignee Postcode is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if(null == value.getConsigneeName() || value.getConsigneeName().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeName()+","+"Consignee name is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if(null == value.getConsigneeAddr1() || value.getConsigneeAddr1().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeAddr1()+","+"Consignee Addr1 is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
			else if(null != value.getServiceType() && (value.getServiceType().startsWith("FW"))) {
					if(isFastwayAddressValid(value.getConsigneeAddr1().replaceAll("[^a-zA-Z0-9]", ""))){
						System.out.println(value.getReferenceNumber()+","+value.getConsigneeAddr1());
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeAddr1()+","+"PO Box and Parcel collect/Parcel Locker not accepted on this service")
						.addConstraintViolation();
						isValid= false;
					}
					if(null != value.getConsigneeAddr2() && (isFastwayAddressValid(value.getConsigneeAddr2().replaceAll("[^a-zA-Z0-9]", "")))){
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getConsigneeAddr2()+","+"PO Box and Parcel collect/Parcel Locker not accepted on this service")
						.addConstraintViolation();
						isValid= false;	
			}
		}
		if(null == value.getProductDescription() || value.getProductDescription().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getProductDescription()+","+"Product Description is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		
		if(value.getValue() <= 0) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getValue()+","+"Value should be greater than 0")
			.addConstraintViolation();
			isValid= false;
		}
		
		if(null == value.getWeight() || value.getWeight().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getWeight()+","+"Weight is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		else if(null != value.getServiceType() && value.getServiceType().equalsIgnoreCase("FW3") && (Double.parseDouble(value.getWeight()) <= 0 || Double.parseDouble(value.getWeight()) > 0.3)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getWeight()+","+"Weight should be between 0 and 0.3")
			.addConstraintViolation();
			isValid= false;
		}
		else if(null != value.getServiceType() && value.getServiceType().startsWith("FW") && (Double.parseDouble(value.getWeight()) <= 0 || Double.parseDouble(value.getWeight()) > 25)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getWeight()+","+"Weight should be between 0 and 25")
			.addConstraintViolation();
			isValid= false;
		}
		else if(null != value.getServiceType() && value.getServiceType().startsWith("MCS") && (Double.parseDouble(value.getWeight()) <= 0 || Double.parseDouble(value.getWeight()) >= 5)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getWeight()+","+"Weight should be between 0 and 5")
			.addConstraintViolation();
			isValid= false;
		}
		
		else if(null != value.getServiceType() && !(value.getServiceType().equalsIgnoreCase("TL1") || value.getServiceType().equalsIgnoreCase("TL2")) 
				&& (Double.parseDouble(value.getWeight()) < 0 || Double.parseDouble(value.getWeight()) > 22)) {

			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getWeight()+","+"Weight should be between 0 and 22")
			.addConstraintViolation();
			isValid= false;
		}
		if(null == value.getServiceType()  || value.getServiceType().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getServiceType()+","+"Service Type is mandatory")
			.addConstraintViolation();
			isValid= false;
		}
		if("STS".equalsIgnoreCase(value.getServiceType()) || "TL1".equalsIgnoreCase(value.getServiceType())){
			if(null == value.getDimensionsHeight()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getDimensionsHeight()+","+"Dimensions Height is mandatory")
				.addConstraintViolation();
				isValid= false;
			}
			if(null == value.getDimensionsWidth()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getDimensionsWidth()+","+"Dimensions Width is mandatory")
				.addConstraintViolation();
				isValid= false;
			}
			if(null == value.getDimensionsLength()) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getDimensionsLength()+","+"Dimensions Length is mandatory")
				.addConstraintViolation();
				isValid= false;
			}
		}
		if((null!=value.getBarcodeLabelNumber() && !value.getBarcodeLabelNumber().isEmpty()) && (null == value.getDatamatrix()  || value.getDatamatrix().isEmpty())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getDatamatrix()+","+"Please provide valid Datamatrix")
			.addConstraintViolation();
			isValid= false;
		}
		if((null!=value.getDatamatrix() && !value.getDatamatrix().isEmpty()) && (null == value.getBarcodeLabelNumber()  || value.getBarcodeLabelNumber().isEmpty())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getBarcodeLabelNumber()+","+"Please provide valid BarcodeLabelNumber")
			.addConstraintViolation();
			isValid= false;
		}
		if("RC1".equalsIgnoreCase(value.getServiceType())) {
			if(null != value.getBarcodeLabelNumber() && value.getBarcodeLabelNumber().length() != 20) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getBarcodeLabelNumber()+","+"BarcodeLabelNumber must have 20 characters")
				.addConstraintViolation();
				isValid= false;
			}
		}
		
		if(null != value.getServiceType() && value.getServiceType().startsWith("VC")) {
			if((null==value.getBarcodeLabelNumber() || value.getBarcodeLabelNumber().isEmpty()) || (null == value.getDatamatrix()  || value.getDatamatrix().isEmpty())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(value.getReferenceNumber()+","+value.getBarcodeLabelNumber()+","+"BarcodeLabelNumber and Datamatrix are mandatory")
				.addConstraintViolation();
				isValid= false;
			}

		}
		return isValid;
	}
	
	public boolean isFastwayAddressValid(String address) {
		String regex = "\\Bpobox|pobox\\B|\\Bpocase|pocase\\B|\\Bbox|box\\B|\\BBx|Bx\\B|\\Bparcel|parcel\\B";
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE); 
		Matcher m = p.matcher(address); 
		return m.find();
	}

}
