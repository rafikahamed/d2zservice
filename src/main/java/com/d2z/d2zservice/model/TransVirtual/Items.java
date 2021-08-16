package com.d2z.d2zservice.model.TransVirtual;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Items {
	@JsonProperty("Barcode")
	public String Barcode;
}
