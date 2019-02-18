package de.slag.core.logic.utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashUtils {

	public static String hash(String original, Algorythm algorythm) {
		MessageDigest sha256;
		try {
			sha256 = MessageDigest.getInstance(algorythm.toString());
		} catch (NoSuchAlgorithmException e) {
			throw new BaseException(e);
		}
		byte[] originalBytes;
		try {
			originalBytes = original.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(e);
		}
		final byte[] digest = sha256.digest(originalBytes);
		final String bytesToHex = bytesToHex(digest);
		return bytesToHex;
}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}

	public enum Algorythm {
		SHA256("SHA-256");

		Algorythm(String string) {
			this.string = string;
		}

		private String string;
		
		public String toString() {
			return string;
		}
	}

}
