package com.d2z.d2zservice.model.TransVirtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TransVirtualResponse {
@JsonProperty("TransactionID")
private String TransactionID;
@JsonProperty("StatusCode")
private int StatusCode;
@JsonProperty("StatusText")
private String StatusText;
@JsonProperty("Data")
private ResponseData Data;

}
