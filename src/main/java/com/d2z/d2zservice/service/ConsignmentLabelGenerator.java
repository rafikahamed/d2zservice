package com.d2z.d2zservice.service;

import java.util.List;
import java.util.Map;

import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.SenderData;

import net.sf.jasperreports.engine.JasperPrint;

public interface ConsignmentLabelGenerator {
	
	public List<SenderdataMaster> fetchLabelData(List<String> refBarNum);
	public List<SenderData>populateLabelData(List<SenderdataMaster> data,String labelName);
	public void export(Map<String,List<SenderData>> labelData,List<JasperPrint> list);
	public byte[] generateLabel(List<String> refBarNum,String identifier);

}
