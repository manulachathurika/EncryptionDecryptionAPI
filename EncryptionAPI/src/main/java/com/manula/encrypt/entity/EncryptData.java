package com.manula.encrypt.entity;

import java.util.Arrays;

public class EncryptData {

	private int id;
	//private String encryptMessage;

	private byte[] byteCipherText;
	private byte[] encryptedKey;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getByteCipherText() {
		return byteCipherText;
	}
	public void setByteCipherText(byte[] byteCipherText) {
		this.byteCipherText = byteCipherText;
	}
	public byte[] getEncryptedKey() {
		return encryptedKey;
	}
	public void setEncryptedKey(byte[] encryptedKey) {
		this.encryptedKey = encryptedKey;
	}
	@Override
	public String toString() {
		return "EncryptData [id=" + id + ", byteCipherText=" + Arrays.toString(byteCipherText) + ", encryptedKey="
				+ Arrays.toString(encryptedKey) + "]";
	}
	
	
	
}
