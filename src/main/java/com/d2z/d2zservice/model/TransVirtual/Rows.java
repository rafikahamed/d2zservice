package com.d2z.d2zservice.model.TransVirtual;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	public int getQty() {
		return Qty;
	}

	public void setQty(int qty) {
		Qty = qty;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getItemContentsDescription() {
		return ItemContentsDescription;
	}

	public void setItemContentsDescription(String itemContentsDescription) {
		ItemContentsDescription = itemContentsDescription;
	}

	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}

	public String getWidth() {
		return Width;
	}

	public void setWidth(String width) {
		Width = width;
	}

	public String getLength() {
		return Length;
	}

	public void setLength(String length) {
		Length = length;
	}

	public String getHeight() {
		return Height;
	}

	public void setHeight(String height) {
		Height = height;
	}

	public String getReference() {
		return Reference;
	}

	public void setReference(String reference) {
		Reference = reference;
	}

	public List<com.d2z.d2zservice.model.TransVirtual.Items> getItems() {
		return Items;
	}

	public void setItems(List<com.d2z.d2zservice.model.TransVirtual.Items> items) {
		Items = items;
	}

	@JsonProperty("Length")
	public String Length;
	@JsonProperty("Height")
	public String Height;
	@JsonProperty("Reference")
	public String Reference;
	@JsonProperty("Items")
	public List<Items> Items;

	
	
	

}
