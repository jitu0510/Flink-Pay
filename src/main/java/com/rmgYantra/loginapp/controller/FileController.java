package com.rmgYantra.loginapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rmgYantra.loginapp.model.FileEntity;
import com.rmgYantra.loginapp.service.DBFileStorageService;
import com.rmgYantra.loginapp.service.FileService;


@RestController
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService dbFileStorageService;
    
    @Autowired
    private FileService fileService;
    
    @PostMapping(value="/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file) {
    	try {
			FileEntity savedFile = fileService.saveFile(file);
			savedFile.setFileData(null);
			return new ResponseEntity(savedFile,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error saving photo",e);
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).build();
		}
    	
    }
    
 
}
