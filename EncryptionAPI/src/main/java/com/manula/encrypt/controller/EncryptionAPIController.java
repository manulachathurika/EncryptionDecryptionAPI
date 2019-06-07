package com.manula.encrypt.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.manula.encrypt.entity.EncryptData;
import com.manula.encrypt.entity.Region;
import com.manula.encrypt.exception.RequestNotFoundException;
import com.manula.encrypt.service.EncryptionAPIService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "/api", produces = "application/json")
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EncryptionAPIController {

	public static final Logger logger = LoggerFactory.getLogger(EncryptionAPIController.class);

	@Autowired
	EncryptionAPIService enctyptionAPIService;

	@GetMapping("/encryptAllRegions")
	public ResponseEntity<EncryptData> getEnctyptedTest() {
		
		logger.info(">>>>> getEnctyptedTest() service method called");
		
		ObjectMapper mapperObj = new ObjectMapper();
		
// 		This section is for call another web service and get results
		
//		System.out.println("-------------------------------------");
//		
//		List<Region> regionList = null;
//	    
//		final String uri = "http://localhost:9094/api/region";
//
//	    RestTemplate restTemplate = new RestTemplate();
//	    String result = restTemplate.getForObject(uri, String.class);
//
//	    System.out.println(result);
//	    
//	    try {
//			Region[] region = mapperObj.readValue(result, Region[].class);
//			
//			regionList = Arrays.asList(region);
//			
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	    
//	    System.out.println("------------------------------------");

		EncryptData encryptData = null;
		
		List<Region> regionList = enctyptionAPIService.getAllRegions();

		if (regionList.isEmpty() || regionList == null) {
			throw new RequestNotFoundException("No Regions found");
		}

		try {
			String jsonStr = mapperObj.writeValueAsString(regionList);
			System.out.println(jsonStr);
			
			encryptData = enctyptionAPIService.doEncrypt(jsonStr);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<EncryptData>(encryptData, HttpStatus.OK);
	}

	@PostMapping("/decryptAllRegions")
	public ResponseEntity<Void> doDecrypt(@RequestBody EncryptData encryptData, UriComponentsBuilder builder) {

		try {
			enctyptionAPIService.doDecrypt(encryptData);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders headers = new HttpHeaders();
		// headers.setLocation(builder.path("/request/{id}").buildAndExpand(request.getRequestId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

}
