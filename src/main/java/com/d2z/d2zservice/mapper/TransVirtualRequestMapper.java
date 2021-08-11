package com.d2z.d2zservice.mapper;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.TransVirtual.Items;
import com.d2z.d2zservice.model.TransVirtual.Rows;
import com.d2z.d2zservice.model.TransVirtual.TransVirtualRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TransVirtualRequestMapper {

    TransVirtualRequestMapper INSTANCE = Mappers.getMapper(TransVirtualRequestMapper.class);


    @Mappings({
            @Mapping(target="uniqueId", source = "reference_number"),
            @Mapping(target="number", source = "reference_number"),
            @Mapping(target="senderReference", source = "reference_number"),
            @Mapping(target="senderSuburb", source = "shipper_City"),
            @Mapping(target="senderName", source = "shipper_Name"),
            @Mapping(target="consignmentSenderContact", source = "shipper_Name"),
            @Mapping(target="senderAddress", source = "shipper_Addr1"),
            @Mapping(target="senderAddress2", source = "shipper_Addr1"),
            @Mapping(target="senderPostcode", source = "shipper_Postcode"),
            @Mapping(target="senderState", source = "shipper_State"),
            //@Mapping(target="SenderEmail", source = "shipper_Email"),
            @Mapping(target="receiverName", source = "consignee_name"),
            @Mapping(target="receiverAddress", source = "consignee_addr1"),
            @Mapping(target="receiverSuburb", source = "consignee_Suburb"),
            @Mapping(target="receiverState", source = "consignee_State"),
            @Mapping(target="receiverPostcode", source = "consignee_Postcode"),
            @Mapping(target="consignmentReceiverContact", source = "consignee_name"),
            @Mapping(target="consignmentReceiverPhone", source = "consignee_Phone"),
            @Mapping(target="receiverEmail", source = "consignee_Email"),
            @Mapping(target = "specialInstructions", source ="deliveryInstructions"),
            @Mapping(target = "pickupRequest", constant = "n")
    })
    TransVirtualRequest convertSenderdataMasterToTransVirtualRequest(SenderdataMaster senderdataMaster);


    @Mappings({
            @Mapping(target="Qty", source = "shippedQuantity"),
            @Mapping(target="Description", source = "product_Description"),
            @Mapping(target="ItemContentsDescription", source = "product_Description"),
            @Mapping(target="Weight", source = "weight"),
            @Mapping(target="Width", source = "dimensions_Width"),
            @Mapping(target="Length", source = "dimensions_Length"),
            @Mapping(target="Height", source = "dimensions_Height"),
            @Mapping(target="Reference", source = "reference_number")
    })
    Rows convertSenderdataMasterToRows(SenderdataMaster senderdataMaster);

    @Mappings({
            @Mapping(target = "Barcode", source= "barcodelabelNumber")
    })
    Items convertSenderdataMasterToItems(SenderdataMaster senderdataMaster);

}
