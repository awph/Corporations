package ch.hearc.corporations.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Encrypt
{
	/**
	 * http://stackoverflow.com/a/9071224/2648956
	 * 
	 * @param a
	 *            text in string
	 * @return the encrypted text with SHA1 in string
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static final String SHA1(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(input.getBytes("UTF-8"));
		return byteToHex(crypt.digest());
	}

	/**
	 * Just convert a hash passed in a byte array to string
	 * @param the hash in bytes
	 * @return the hash in string
	 */
	private static String byteToHex(final byte[] hash)
	{
		Formatter formatter = new Formatter();
		for (byte b : hash)
		{
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * http://stackoverflow.com/a/11009612/2648956
	 * 
	 * @param userID
	 *            the ID of the Facebook user
	 * @return the userID encrypted
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static final String encryptUserIDSHA512(String userID) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		byte[] hash = digest.digest(SALT_USER_ID.concat(userID).getBytes("UTF-8"));

		StringBuffer encryptedUserID = new StringBuffer();
		for (int i = 0; i < hash.length; ++i)
		{
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) encryptedUserID.append('0');
			encryptedUserID.append(hex);
		}

		return encryptedUserID.toString();
	}

	private static final String	SALT_USER_ID	= "w?n41yunI|z+0$=xzqx:/KqctZ5[D&w8qR08N~e&HY!M-yafvA:DUt00=V-^>7Kg";
}
