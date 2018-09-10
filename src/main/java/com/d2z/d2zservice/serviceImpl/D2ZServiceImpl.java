package com.d2z.d2zservice.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.model.DropDownModel;
import com.d2z.d2zservice.model.FileUploadData;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ID2ZService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;

@Service
public class D2ZServiceImpl implements ID2ZService{
	
	@Autowired
    private ID2ZDao d2zDao;

	@Override
	public UserMessage exportParcel(List<FileUploadData> fileData) {
		List<FileUploadData> fileUploadData= d2zDao.exportParcel(fileData);
		UserMessage userMsg = new UserMessage();
		userMsg.setMessage("Data Saved Successfully");
		return userMsg;
	}

	@Override
	public List<DropDownModel> fileList() {
		List<String> listOfFileNames= d2zDao.fileList();
		List<DropDownModel> dropDownList= new ArrayList<DropDownModel>();
		for(String fileName:listOfFileNames) {
			DropDownModel dropDownVaL = new DropDownModel();
			dropDownVaL.setName(fileName);
			dropDownVaL.setValue(fileName);
			dropDownList.add(dropDownVaL);
		}
		return dropDownList;
	}

	@Override
	public List<SenderdataMaster> consignmentFileData(String fileName) {
		List<SenderdataMaster> fileData= d2zDao.consignmentFileData(fileName);
		return fileData;
	}

	@Override
	public byte[] generateLabel(List<SenderData> senderData) {
		JRBeanCollectionDataSource beanColDataSource =
		         new JRBeanCollectionDataSource(senderData);
		 Map<String,Object> parameters = new HashMap<>();
		 //byte[] labelAsBytes = jasperService.generatePDFLabel(parameters, beanColDataSource);
		 
		 byte[] bytes = null;
		    JasperReport jasperReport = null;
		    try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
		    	
		  
		        String jrxml = "src/main/resources/eparcelLabel.jrxml";
		        jasperReport = JasperCompileManager.compileReport(jrxml);
		        // Save compiled report. Compiled report is loaded next time
		        JRSaver.saveObject(jasperReport, "label.jasper");
		      
		      JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);
		      // return the PDF in bytes
		      bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		    }
		    catch (JRException | IOException e) {
		      e.printStackTrace();
		    }
		    return bytes;
	}
	

}
