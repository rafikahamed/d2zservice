package com.d2z.d2zservice.model.TransVirtual;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rows {
	
	private int Qty = 1;
	private String Description ="Carton";
	private String ItemContentsDescription="Carton";
	private int ItemRowChargeQty=1;
	private List<Items> Items;

	@JsonProperty("Qty")
	public int getQty() {
		return Qty;
	}

	public void setQty(int qty) {
		Qty = qty;
	}

	@JsonProperty("Description")
	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}
	@JsonProperty("ItemContentsDescription")
	public String getItemContentsDescription() {
		return ItemContentsDescription;
	}

	public void setItemContentsDescription(String itemContentsDescription) {
		ItemContentsDescription = itemContentsDescription;
	}
	@JsonProperty("ItemRowChargeQty")
	public int getItemRowChargeQty() {
		return ItemRowChargeQty;
	}

	public void setItemRowChargeQty(int itemRowChargeQty) {
		ItemRowChargeQty = itemRowChargeQty;
	}
	@JsonProperty("Items")
	public List<Items> getItems() {
		if(Items == null) {
			return new ArrayList<Items>();
		}
		return Items;
	}

	public void setItems(List<Items> items) {
		Items = items;
	}
	
	
	

}
