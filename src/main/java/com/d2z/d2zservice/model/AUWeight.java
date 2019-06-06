package com.d2z.d2zservice.model;

import java.math.BigDecimal;

public class AUWeight {
private String  articleID;
public String getArticleID() {
	return articleID;
}
public void setArticleID(String articleID) {
	this.articleID = articleID;
}
public BigDecimal getCubicWeight() {
	return cubicWeight;
}
public void setCubicWeight(BigDecimal cubicWeight) {
	this.cubicWeight = cubicWeight;
}
private BigDecimal cubicWeight;
}
