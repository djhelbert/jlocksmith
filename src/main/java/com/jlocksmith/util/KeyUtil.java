/*
 * Copyright � 2011 Derek Helbert, djhelbert@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.jlocksmith.util;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.*;

/**
 * Key Utility
 * 
 * @author Derek Helbert
 */
public class KeyUtil {

	/** DSA */
	public static String DSA = "DSA";

	/** RSA */
	public static String RSA = "RSA";

	/** Algorithms */
	private static String[] KEY_ALGORITHMS = { DSA, RSA };

	/**
	 * Get Algorithms
	 * 
	 * @return String[]
	 */
	public static String[] getKeyAlgorithms() {
		return KEY_ALGORITHMS;
	}

	/**
	 * Get Key Pair
	 * 
	 * @return KeyPair
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
		return getKeyPair(RSA, 1024);
	}

	/**
	 * Get Key Pair
	 * 
	 * @param alg
	 * @param size
	 * @return KeyPair
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair(String alg, int size)
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(alg);
		keyGen.initialize(size);
		return keyGen.genKeyPair();
	}

	/**
	 * Get Key Store
	 * 
	 * @param path File Path
	 * @param password Password
	 * 
	 * @return KeyStore
	 * 
	 * @throws Exception
	 */
	public static KeyStore loadKeyStore(String path, String password)
			throws KeyStoreException, IOException, CertificateException,
			NoSuchAlgorithmException {
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(new File(path)), password
				.toCharArray());
		return keyStore;
	}

	/**
	 * Get Key Store
	 * 
	 * @param path
	 * @param password
	 * @param type
	 * @param prov
	 * 
	 * @return KeyStore
	 * 
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static KeyStore loadKeyStore(String path, String password,
			String type, String prov) throws KeyStoreException, IOException,
			CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException {
		KeyStore keyStore = KeyStore.getInstance(type, prov);
		keyStore.load(new FileInputStream(new File(path)), password
				.toCharArray());
		return keyStore;
	}

	/**
	 * Store Key Store
	 * 
	 * @param ks Key Store
	 * @param path File Path
	 * @param password Password
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws CertificateException
	 */
	public static void storeKeyStore(KeyStore ks, String path , String password) throws NoSuchAlgorithmException, FileNotFoundException, KeyStoreException, IOException, CertificateException    {
		FileOutputStream fos = new FileOutputStream(new File(path));
		ks.store(fos,password.toCharArray());
		fos.close();
	}
	
	/**
	 * Get Key Pair Aliases
	 * 
	 * @param keyStore Key Store
	 * @return String[]
	 * @throws Exception
	 */
	public static String[] getKeyPairAliases(KeyStore keyStore) throws Exception {
		String[] temp = null;
		String alias = null;
		List<String> list = new ArrayList<String>();

		for (Enumeration<String> e = keyStore.aliases(); e.hasMoreElements();) {
			alias = e.nextElement().toString();

			if (keyStore.isKeyEntry(alias)) {
				Certificate[] chain = keyStore.getCertificateChain(alias);

				if (chain != null && chain.length > 0) {
					list.add(alias);
				}
			}
		}

		temp = new String[list.size()];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = list.get(i).toString();
		}

		return temp;
	}
}
