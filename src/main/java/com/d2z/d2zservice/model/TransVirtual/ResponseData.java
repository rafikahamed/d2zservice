package com.d2z.d2zservice.model.TransVirtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ResponseData {
    @JsonProperty("Id")
    private String Id;
    @JsonProperty("PdfLabels")
    private String PdfLabels;
    @JsonProperty("ConsignmentNumber")
    private String ConsignmentNumber;
    @JsonProperty("PdfConsignment")
    private String PdfConsignment;
    @JsonProperty("ItemScanValues")
    private List<String> ItemScanValues;

}
