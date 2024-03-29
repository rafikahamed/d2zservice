package com.d2z.d2zservice.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.SenderData;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SenderDataMapper {
    
	SenderDataMapper INSTANCE = Mappers.getMapper(SenderDataMapper.class);

	@Mappings({
		  @Mapping(target="referenceNumber", source = "reference_number"),
		  @Mapping(target="consigneeName", source = "consignee_name"),
		  @Mapping(target="consigneeAddr1", source = "consignee_addr1"),
		  @Mapping(target="consigneeAddr2", source = "consignee_addr2"),
		  @Mapping(target="consigneeSuburb", source = "consignee_Suburb"),
		  @Mapping(target="consigneeState", source = "consignee_State"),
		  @Mapping(target="consigneePostcode", source = "consignee_Postcode"),
		  @Mapping(target="consigneePhone", source = "consignee_Phone"),
		  @Mapping(target="weight", source = "weight"),
		  @Mapping(target="shipperName", source = "shipper_Name"),
		  @Mapping(target="shipperAddr1", source = "shipper_Addr1"),
		  @Mapping(target="shipperCity", source = "shipper_City"),
		  @Mapping(target="shipperState", source = "shipper_State"),
		  @Mapping(target="shipperCountry", source = "shipper_Country"),
		  @Mapping(target="shipperPostcode", source = "shipper_Postcode"),
		  @Mapping(target="barcodeLabelNumber", source = "barcodelabelNumber"),
		  @Mapping(target="productDescription", source = "product_Description"),
		  @Mapping(target = "timestamp", ignore = true)
		  })
	SenderData convertSenderdataMasterToSenderData(SenderdataMaster senderdataMaster);


}
