package com.d2z.d2zservice.serviceImpl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d2z.d2zservice.dao.ID2ZDao;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.model.SenderData;
import com.d2z.d2zservice.service.ConsignmentLabelGenerator;
import com.d2z.d2zservice.supplier.EtowerSupplier;
import com.d2z.d2zservice.supplier.PFLSupplier;
import com.d2z.d2zservice.supplier.VeloceSupplier;
import com.d2z.singleton.D2ZSingleton;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import uk.org.okapibarcode.backend.DataMatrix;
import uk.org.okapibarcode.backend.DataMatrix.ForceMode;
import uk.org.okapibarcode.backend.OkapiException;
import uk.org.okapibarcode.backend.Symbol;
import uk.org.okapibarcode.backend.Symbol.DataType;
import uk.org.okapibarcode.output.Java2DRenderer;

@Service
public class ConsignmentLabelGeneratorImpl implements ConsignmentLabelGenerator{

	@Autowired 
	ID2ZDao dao;
	
	@Autowired
	VeloceSupplier veloceSupplier;
	
	@Autowired
	PFLSupplier pflSupplier;
	
	@Autowired
	EtowerSupplier etowerSupplier;
	
	@Override
	public List<SenderdataMaster> fetchLabelData(List<String> refBarNum) {
		return dao.fetchLabelData(refBarNum);
	}

	@Override
	public List<SenderData> populateLabelData(List<SenderdataMaster> labelData,String labelName) {
		
		List<SenderData> senderData = new ArrayList<SenderData>();
		
		labelData.forEach(consignment -> {
			
		SenderData trackingLabel =  new SenderData();//SenderDataMapper.INSTANCE.convertSenderdataMasterToSenderData(consignment);
			trackingLabel.setReferenceNumber(consignment.getReference_number());
			trackingLabel.setConsigneeName(consignment.getConsignee_name());
			trackingLabel.setConsigneeAddr1(consignment.getConsignee_addr1());
			trackingLabel.setConsigneeSuburb(consignment.getConsignee_Suburb());
			trackingLabel.setConsigneeState(consignment.getConsignee_State());
			trackingLabel.setConsigneePostcode(consignment.getConsignee_Postcode());
			trackingLabel.setConsigneePhone(consignment.getConsignee_Phone());
			trackingLabel.setWeight(String.valueOf(consignment.getWeight()));
			trackingLabel.setShipperName(consignment.getShipper_Name());
			trackingLabel.setShipperAddr1(consignment.getShipper_Addr1());
			trackingLabel.setShipperCity(consignment.getShipper_City());
			trackingLabel.setShipperState(consignment.getShipper_State());
			trackingLabel.setShipperCountry(consignment.getShipper_Country());
			trackingLabel.setShipperPostcode(consignment.getShipper_Postcode());
			trackingLabel.setBarcodeLabelNumber(consignment.getBarcodelabelNumber());
			trackingLabel.setDatamatrix(consignment.getDatamatrix());
			trackingLabel.setInjectionState(consignment.getInjectionState());
			trackingLabel.setSku(consignment.getSku());
			trackingLabel.setLabelSenderName(consignment.getLabelSenderName());
			trackingLabel.setDeliveryInstructions(consignment.getDeliveryInstructions());
			trackingLabel.setConsigneeCompany(consignment.getConsigneeCompany());
			trackingLabel.setCarrier(consignment.getCarrier());
			trackingLabel.setConsigneeAddr2(consignment.getConsignee_addr2());
			trackingLabel.setReturnAddress1(consignment.getReturnAddress1());
			trackingLabel.setReturnAddress2(consignment.getReturnAddress2());
			trackingLabel.setProductDescription(consignment.getProduct_Description());
			trackingLabel.setServiceType(consignment.getServicetype());
			trackingLabel.setD2zRate(consignment.getD2zRate());
			trackingLabel.setBrokerRate(consignment.getBrokerRate());
	
	    
	    if("FDM".equalsIgnoreCase(labelName)) {
		String state = consignment.getConsignee_State().trim().toUpperCase();
		String suburb = consignment.getConsignee_Suburb().trim().toUpperCase();
		String postcode = consignment.getConsignee_Postcode().trim();
		trackingLabel.setLabelSenderName(dao.fetchFDMRoute(state,suburb,postcode));
		}
	    
	    if("FWLabel".equalsIgnoreCase(labelName)) {
	    	trackingLabel.setSku(D2ZSingleton.getInstance().getFwPostCodeZoneNoMap().get(trackingLabel.getConsigneePostcode()));
	    }
	
	    if("FastWayLabel".equalsIgnoreCase(labelName)) {
	    	trackingLabel.setProductDescription(D2ZSingleton.getInstance().getFwPostCodeZoneNoMap().get(trackingLabel.getConsigneePostcode()));
	    }
	    if(null!=consignment.getDatamatrix()) {
			boolean setGS1Type = false; 
			if(consignment.getCarrier().equalsIgnoreCase("eParcel") || consignment.getCarrier().equalsIgnoreCase("Express")) {
				setGS1Type = true;
			}
			if(!consignment.getCarrier().contains("NEX")) {
			trackingLabel.setDatamatrixImage(generateDataMatrix(consignment.getDatamatrix(), setGS1Type));
			}
			}	
		senderData.add(trackingLabel);
		});

		return senderData;
	}

	@Override
	public void export(Map<String,List<SenderData>> labelData,List<JasperPrint> jasperPrintList) {
		Map<String, Object> parameters = new HashMap<>();
		labelData.forEach((labelName,data) -> {
			System.out.println(labelName+":::"+data.size());
		JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(data);
		try {
			JasperReport label = JasperCompileManager.compileReport(getClass().getResource("/"+labelName+".jrxml").openStream());
			jasperPrintList.add(JasperFillManager.fillReport(label, parameters, datasource));
		} 
		catch (JRException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		});
	}

	@Override
	public byte[] generateLabel(List<String> refBarNum,String identifier) {
		byte[] bytes = null;
	      PDFMergerUtility mergePdf = new PDFMergerUtility();
	      Map<String,List<String>> labelMap= dao.fetchLabelName(refBarNum,identifier);
		Map<String,List<SenderData>> labelData = new HashMap<String,List<SenderData>>();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		labelMap.forEach((labelName,ids) -> {
		if(null!=labelName) {
		if(StringUtils.isNumeric(labelName)) {
			SupplierEntity supplier = dao.fetchSupplierData(Integer.parseInt(labelName));
			if(supplier.getSupplierName().contains("VELOCE")) {
				byte [] veloceResponse= veloceSupplier.printLabel(ids,supplier);
				mergePdf.addSource(new ByteArrayInputStream(veloceResponse));
			}else if(supplier.getSupplierName().contains("PFL")) {
				List<String> mlidList = dao.fetchMlid(ids);
				byte[] pflResponse = pflSupplier.printLabel(mlidList,supplier);
				mergePdf.addSource(new ByteArrayInputStream(pflResponse));
			}else if(supplier.getSupplierName().contains("ETOWER")) {
				byte[] etowerResponse = etowerSupplier.printLabel(ids,supplier);
				mergePdf.addSource(new ByteArrayInputStream(etowerResponse));
			}
		}else {
			Map<String, List<String>> map = new HashMap<String,List<String>>();
			map.put(labelName,ids);
			map.forEach((label,refNums) -> {
				labelData.put(label, populateLabelData(fetchLabelData(refNums),label));
			});
		}
		}});
		if(labelData.size()>0) {
		byte[] response = generate(labelData);
		mergePdf.addSource(new ByteArrayInputStream(response));
		}
		mergePdf.setDestinationStream(outputStream);
		
			try {
				mergePdf.mergeDocuments(null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		bytes =  outputStream.toByteArray();
		System.out.println(bytes);
	return bytes;		
	}

private byte[] generate(Map<String,List<SenderData>> labelData) {
	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	SimpleOutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(outputStream);
	JRPdfExporter exporter = new JRPdfExporter();
	List<JasperPrint> jasperPrint = new ArrayList<JasperPrint>();
	export(labelData,jasperPrint);
	System.out.println(jasperPrint.size());
	if(jasperPrint.size()>0) {
	exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrint));
	exporter.setExporterOutput(exporterOutput);
	try {
		exporter.exportReport();
	} catch (JRException e) {
		e.printStackTrace();
	}
	}
	return outputStream.toByteArray();
	}

private BufferedImage generateDataMatrix(String datamatrixInput, boolean setGS1DataType) {
	BufferedImage datamatrixImage = null;
	try {
		// Set up the DataMatrix object
		DataMatrix dataMatrix = new DataMatrix();
		// We need a GS1 DataMatrix barcode.
		if (setGS1DataType)
			dataMatrix.setDataType(DataType.GS1);

		// 0 means size will be set automatically according to amount of data (smallest
		// possible).
		dataMatrix.setPreferredSize(0);
		// Don't want no funky rectangle shapes, if we can avoid it.
		dataMatrix.setForceMode(ForceMode.SQUARE);
		dataMatrix.setContent(datamatrixInput);
		datamatrixImage = getMagnifiedBarcode(dataMatrix);
		// return getBase64FromByteArrayOutputStream(getMagnifiedBarcode(dataMatrix,
		// MAGNIFICATION));
	} catch (OkapiException oe) {
		oe.printStackTrace();
	}
	return datamatrixImage;

}

private BufferedImage getMagnifiedBarcode(Symbol symbol) {
	final int MAGNIFICATION = 10;
	final int BORDER_SIZE = 0 * MAGNIFICATION;
	// Make DataMatrix object into bitmap
	BufferedImage image = new BufferedImage((symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
			(symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE), BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d = image.createGraphics();
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	g2d.setColor(Color.WHITE);
	g2d.fillRect(0, 0, (symbol.getWidth() * MAGNIFICATION) + (2 * BORDER_SIZE),
			(symbol.getHeight() * MAGNIFICATION) + (2 * BORDER_SIZE));
	Java2DRenderer renderer = new Java2DRenderer(g2d, 10, Color.WHITE, Color.BLACK);
	renderer.render(symbol);

	return image;
}

}
