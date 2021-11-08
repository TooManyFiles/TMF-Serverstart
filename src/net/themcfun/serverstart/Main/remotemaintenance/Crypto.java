/**
 * 
 */
package net.themcfun.serverstart.Main.remotemaintenance;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Lukas
 *
 */
public class Crypto {
	public static byte[] encrypt(byte[] message, Key pk)
	{
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pk);
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] chiffrat = null;

		try {
			chiffrat = cipher.doFinal(message);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chiffrat;
	}

	public static byte[] decrypt(byte[] chiffrat, PrivateKey sk)
	{
		byte[] dec = null;
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, sk);
		} catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		try {
			dec = cipher.doFinal(chiffrat);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dec;
	}




	public static byte[][] gen()
	{
		KeyPairGenerator keygen = null;
		try
		{
			keygen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keygen.initialize(1024);
		KeyPair key = null;
		key = keygen.generateKeyPair();
		byte[] privatekey = key.getPrivate().getEncoded();
		byte[] publickey = key.getPublic().getEncoded();


		System.out.println("\nPrivat:\n"+key.getPrivate()+"\n");
		for (byte b : privatekey) {
			System.out.print(b+"!");
		}

		System.out.println("\nPublic:\n"+key.getPublic()+"\n");

		for (byte b : publickey) {
			System.out.print(b+"!");
		}
		byte[][] out = new byte[2][];
		out[0] = publickey;
		out[1] = privatekey;
		return out;
	}

	public static PublicKey byteToPublickey(byte[] in) {
		try {
			X509EncodedKeySpec ks = new X509EncodedKeySpec(in);
			KeyFactory kf = KeyFactory.getInstance("RSA");
			PublicKey pub = kf.generatePublic(ks);
			return pub;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static PrivateKey byteToPrivatekey(byte[] in) {
		try {
			PKCS8EncodedKeySpec prks = new PKCS8EncodedKeySpec(in);
			KeyFactory prkf;
			prkf = KeyFactory.getInstance("RSA");
			PrivateKey pvt = prkf.generatePrivate(prks);
			return pvt;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

}


