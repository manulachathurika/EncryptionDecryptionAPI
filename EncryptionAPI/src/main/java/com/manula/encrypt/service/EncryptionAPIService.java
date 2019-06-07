package com.manula.encrypt.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import com.manula.encrypt.entity.EncryptData;
import com.manula.encrypt.entity.Region;

public interface EncryptionAPIService {

	public List<Region> getAllRegions();
	
	public EncryptData doEncrypt(String jsonStr) throws NoSuchAlgorithmException, NoSuchPaddingException, Exception;
	
	public void doDecrypt(EncryptData encryptData) throws NoSuchAlgorithmException, NoSuchPaddingException, Exception;

}
