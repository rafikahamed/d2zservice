package com.d2z.d2zservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.SenderData;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SenderDataMapper {
    
	SenderDataMapper INSTANCE = Mappers.getMapper(SenderDataMapper.class);

	
	SenderdataMaster convertSenderDataToSenderdataMaster(SenderData senderData);
	
	@Mappings({
		  @Mapping(target="referenceNumber", source = "entity.reference_number"),
		  @Mapping(target="consigneeName", source = "entity.consignee_name"),
		  @Mapping(target="consigneeAddr1", source = "entity.consignee_addr1"),
		  @Mapping(target="consigneeAddr2", source = "entity.consignee_addr2"),
		  @Mapping(target="consigneeSuburb", source = "entity.consignee_Suburb"),
		  @Mapping(target="consigneeState", source = "entity.consignee_State"),
		  @Mapping(target="consigneePostcode", source = "entity.consignee_Postcode"),
		  @Mapping(target="consigneePhone", source = "entity.consignee_Phone"),
		  @Mapping(target="weight", source = "entity.weight",qualifiedByName="StringToDouble"),
		  @Mapping(target="shipperName", source = "entity.shipper_Name"),
		  @Mapping(target="shipperAddr1", source = "entity.shipper_Addr1"),
		  @Mapping(target="shipperCity", source = "entity.shipper_City"),
		  @Mapping(target="shipperState", source = "entity.shipper_State"),
		  @Mapping(target="shipperCountry", source = "entity.shipper_Country"),
		  @Mapping(target="shipperPostcode", source = "entity.shipper_Postcode"),
		  @Mapping(target="barcodeLabelNumber", source = "entity.barcodelabelNumber"),
		  @Mapping(target="productDescription", source = "entity.product_Description")
		  })
	SenderData convertSenderdataMasterToSenderData(SenderdataMaster senderdataMaster);

	  @Named("StringToDouble")
	    default double extractDoubleFromString(String string){
	        return Double.parseDouble(string);
	    }
}
