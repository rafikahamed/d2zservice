package com.d2z.d2zservice.model.TransVirtual;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Rows {
	
	@JsonProperty("Qty")
	public int Qty;
	@JsonProperty("Description")
	public String Description;
	@JsonProperty("ItemContentsDescription")
	public String ItemContentsDescription;
	@JsonProperty("Weight")
	public String Weight;
	@JsonProperty("Width")
	public String Width;
	@JsonProperty("Length")
	public String Length;
	@JsonProperty("Height")
	public String Height;
	@JsonProperty("Reference")
	public String Reference;
	@JsonProperty("Items")
	public List<Items> Items;

	
	
	

}
