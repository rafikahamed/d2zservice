package com.d2z.d2zservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.d2z.d2zservice.dto.ConsignmentDTO;
import com.d2z.d2zservice.entity.SenderdataMaster;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")

public interface ConsignmentDTOMapper {
	

    
	ConsignmentDTOMapper INSTANCE = Mappers.getMapper(ConsignmentDTOMapper.class);

	@Mappings({
		  @Mapping(target="referenceNumber", source = "reference_number"),
		  @Mapping(target="consigneeName", source = "consignee_name"),
		  @Mapping(target="consigneeAddr1", source = "consignee_addr1"),
		  @Mapping(target="consigneeAddr2", source = "consignee_addr2"),
		  @Mapping(target="consigneeSuburb", source = "consignee_Suburb"),
		  @Mapping(target="consigneeState", source = "consignee_State"),
		  @Mapping(target="consigneePostcode", source = "consignee_Postcode"),
		  @Mapping(target="consigneePhone", source = "consignee_Phone"),
		  @Mapping(target="consigneeEmail",source="consignee_Email"),
		  @Mapping(target="weight", source = "weight"),
		  @Mapping(target="shipperName", source = "shipper_Name"),
		  @Mapping(target="shipperAddr1", source = "shipper_Addr1"),
		  @Mapping(target="shipperCity", source = "shipper_City"),
		  @Mapping(target="shipperState", source = "shipper_State"),
		  @Mapping(target="shipperCountry", source = "shipper_Country"),
		  @Mapping(target="shipperPostcode", source = "shipper_Postcode"),
		  @Mapping(target="productDescription", source = "product_Description"),
		  @Mapping(target="custReference",source="cust_reference"),
		  @Mapping(target="cubicWeight",source="cubic_Weight"),
		  @Mapping(target="userID",source="user_ID"),
		  @Mapping(target = "timestamp", ignore = true)
		  })
	ConsignmentDTO convertSenderDataMasterToDTO(SenderdataMaster senderdataMaster);
	
	List<ConsignmentDTO> getConsignments(List<SenderdataMaster> data);




}
