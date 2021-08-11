package com.d2z.d2zservice.supplier;

import com.d2z.d2zservice.entity.FFResponse;
import com.d2z.d2zservice.entity.SenderdataMaster;
import com.d2z.d2zservice.entity.SupplierEntity;
import com.d2z.d2zservice.mapper.TransVirtualRequestMapper;
import com.d2z.d2zservice.model.FDMManifestDetails;
import com.d2z.d2zservice.model.LabelData;
import com.d2z.d2zservice.model.PFLCreateShippingResponse;
import com.d2z.d2zservice.model.TransVirtual.*;
import com.d2z.d2zservice.model.etower.CreateShippingResponse;
import com.d2z.d2zservice.model.fdm.*;
import com.d2z.d2zservice.model.veloce.VeloceTrackingRequest;
import com.d2z.d2zservice.repository.FFResponseRepository;
import com.d2z.d2zservice.util.D2ZCommonUtil;
import org.apache.commons.collections4.ListUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransVirtualSupplier {

    @Value("${transvirtual.url}")
    private String baseURL;

    @Value("${jasypt.encryptor.password}")
    private String encryptionPassword;

    @Autowired
    private SupplierInterface supplier;

    @Autowired
    private TransVirtualRequestMapper transVirtualRequestMapper;

    @Autowired
    private FFResponseRepository ffResponseRepository;


    StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);


    public void allocateShipment(List<SenderdataMaster> senderData, SupplierEntity config) {

senderData.forEach(senderdataMaster -> {
    String request = constructRequest(senderdataMaster,config);
    ResponseEntity<TransVirtualResponse> responeEntity = supplier.makeCall(HttpMethod.POST,
            baseURL , request,
            constructHeader(config), TransVirtualResponse.class);
    saveResponse(responeEntity.getBody(),config.getSupplierName());

});

    }

    public void saveResponse(TransVirtualResponse response,String supplierName){
        FFResponse ffresponse = new FFResponse();
        ffresponse.setSupplier(supplierName);
        ffresponse.setResponse(response.getStatusCode()+"");
        ffresponse.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        ffresponse.setArticleid(response.getData().getItemScanValues().get(0));
        ffresponse.setBarcodelabelnumber(response.getData().getItemScanValues().get(0));
        ffresponse.setReferencenumber(response.getData().getConsignmentNumber());
      ffResponseRepository.save(ffresponse);
    }

    private HttpHeaders constructHeader(SupplierEntity config) {
        StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
        System.out.println("Decrypted Key :"+stringEncryptor.decrypt(config.getSupplierKey()));
        System.out.println("Decrypted Token :"+stringEncryptor.decrypt(config.getSupplierToken()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = stringEncryptor.decrypt(config.getSupplierToken());
        headers.add("Authorization",auth);
        return headers;
    }


    private String	constructRequest(SenderdataMaster senderdataMaster,SupplierEntity config){
        StringEncryptor stringEncryptor = D2ZCommonUtil.stringEncryptor(encryptionPassword);
        TransVirtualRequest transVirtualRequest = transVirtualRequestMapper.convertSenderdataMasterToTransVirtualRequest(senderdataMaster);
        transVirtualRequest.setCustomerCode(stringEncryptor.decrypt(config.getSupplierKey()));
        Items item = transVirtualRequestMapper.convertSenderdataMasterToItems(senderdataMaster);
        Rows rows = transVirtualRequestMapper.convertSenderdataMasterToRows(senderdataMaster);
        rows.setItems(Collections.singletonList(item));
        transVirtualRequest.setRows(Collections.singletonList(rows));
        transVirtualRequest.setDate(LocalDate.now().toString());
        if(config.getSupplierName().contains("-")){
            String agent = config.getSupplierName().split("-")[1];
            try{
                transVirtualRequest.setAutoAssignAgentEmployee(AutoAssignAgent.valueOf(agent).getAgent());
          }
            catch(Exception ex){
                transVirtualRequest.setAutoAssignAgentEmployee("");
            }

        }
        else{
            transVirtualRequest.setAutoAssignAgentEmployee("");
        }

        return D2ZCommonUtil.convertToJsonString(transVirtualRequest);
    }


}
