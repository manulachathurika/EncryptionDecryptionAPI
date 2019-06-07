package com.manula.encrypt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.manula.encrypt.DAO.WebscrapeDAO;
import com.manula.encrypt.entity.EncryptData;
import com.manula.encrypt.entity.Region;
import com.manula.encrypt.entity.Request;
import com.manula.encrypt.entity.Team;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

@Service
public class EncryptionAPIServiceImpl implements EncryptionAPIService {

	private Cipher cipher;

	@Autowired
	private Environment env;

	public PrivateKey getPrivate(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public PublicKey getPublic(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	@Override
	public EncryptData doEncrypt(String jsonStr) throws Exception {

		EncryptionAPIServiceImpl ac = new EncryptionAPIServiceImpl();

		// PrivateKey privateKey = ac.getPrivate(env.getProperty("PRIVATE_KEY"));
		PublicKey publicKey = ac.getPublic(env.getProperty("PUBLIC_KEY"));

		// ---------------------
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();

		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] byteCipherText = aesCipher.doFinal(jsonStr.getBytes());

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.PUBLIC_KEY, publicKey);
		byte[] encryptedKey = cipher.doFinal(secKey.getEncoded());

		EncryptData encryptData = new EncryptData();
		encryptData.setId(1);
		encryptData.setByteCipherText(byteCipherText);
		encryptData.setEncryptedKey(encryptedKey);

		return encryptData;
	}

	@Override
	public void doDecrypt(EncryptData encryptData) throws Exception {

		EncryptionAPIServiceImpl ac = new EncryptionAPIServiceImpl();
		PrivateKey privateKey = ac.getPrivate(env.getProperty("PRIVATE_KEY"));

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.PRIVATE_KEY, privateKey);
		byte[] decryptedKey = cipher.doFinal(encryptData.getEncryptedKey());

		SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
		Cipher aesCipher = Cipher.getInstance("AES");
		aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
		byte[] bytePlainText = aesCipher.doFinal(encryptData.getByteCipherText());
		String plainText = new String(bytePlainText);

		System.out.println(plainText);

	}

	@Autowired
	private WebscrapeDAO webscrapeDAO;

	@Override
	public List<Region> getAllRegions() {
		return webscrapeDAO.getAllRegions();
	}

}
