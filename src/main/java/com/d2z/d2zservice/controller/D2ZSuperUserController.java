package com.d2z.d2zservice.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.d2z.d2zservice.model.UploadTrackingFileData;
import com.d2z.d2zservice.model.UserMessage;
import com.d2z.d2zservice.service.ISuperUserD2ZService;

@RestController
@RequestMapping(value = "/v1/d2z/superUser-level")
public class D2ZSuperUserController {

Logger logger = LoggerFactory.getLogger(D2zController.class);
	
	@Autowired
    private  ISuperUserD2ZService superUserD2zService;
	
	@RequestMapping( method = RequestMethod.POST, path = "/track-fileUpload", consumes=MediaType.APPLICATION_JSON)
    public UserMessage uploadTrackingFile(@RequestBody List<UploadTrackingFileData> fileData) {
		UserMessage successMsg = superUserD2zService.uploadTrackingFile(fileData);
		return successMsg;
    }

}
