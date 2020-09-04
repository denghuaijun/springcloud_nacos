package com.taikang.business.common.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

	private static final String KEY_ALGORITHM = "RSA";

	/**
	 * 公钥加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param key
	 *            公钥
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// 分段加密
		int inputLength = data.length;
		int inputOffset = 0, i = 0;
		int blockSize = 245;
		ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
		while (inputLength - inputOffset > 0) {
			byte[] cache;
			if (inputLength - inputOffset > blockSize) {
				cache = cipher.doFinal(data, inputOffset, blockSize);
			} else {
				cache = cipher.doFinal(data, inputOffset, inputLength - inputOffset);
			}
			bArrayOutputStream.write(cache);
			i++;
			inputOffset = i * blockSize;
		}
		byte[] encryptData = bArrayOutputStream.toByteArray();
		bArrayOutputStream.close();
		return encryptData;
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param key
	 *            公钥
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成公钥
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		// 分段解密
		int inputLength = data.length;
		int inputOffset = 0, i = 0;
		int blockSize = 256;
		ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
		while (inputLength - inputOffset > 0) {
			byte[] cache;
			if (inputLength - inputOffset > blockSize) {
				cache = cipher.doFinal(data, inputOffset, blockSize);
			} else {
				cache = cipher.doFinal(data, inputOffset, inputLength - inputOffset);
			}
			bArrayOutputStream.write(cache);
			i++;
			inputOffset = i * blockSize;
		}
		byte[] encryptData = bArrayOutputStream.toByteArray();
		bArrayOutputStream.close();
		return encryptData;
	}

}
