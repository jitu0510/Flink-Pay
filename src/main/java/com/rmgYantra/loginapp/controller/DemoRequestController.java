package com.rmgYantra.loginapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rmgYantra.loginapp.model.DemoRequest;
import com.rmgYantra.loginapp.repo.DemoRequestRepo;

@RestController
@CrossOrigin(origins = "*")
public class DemoRequestController {
	
	@Autowired
	private DemoRequestRepo demoRequestRepo;
	
	@PostMapping("/demorequest")
	public ResponseEntity<?> requestDemo(@RequestBody DemoRequest demoRequest){
		System.out.println(demoRequest);
		try {
			DemoRequest savedDemoRequest = demoRequestRepo.save(demoRequest);
			return new ResponseEntity(savedDemoRequest,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(e,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
