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
package com.jlocksmith;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import com.jlocksmith.util.CertificateUtil;

/**
 * Key Store Manager Singleton
 * 
 * @author Derek Helbert
 */
public class KeystoreManager {

	/** Key Store Manager */
	private static KeystoreManager manager;

	/** Key Store */
	private KeyStore keyStore;

	/** Listeners */
	private List<KeystoreListener> listeners = new ArrayList<KeystoreListener>();

	/** Loaded Flag */
	private boolean loaded = false;

	/** Loaded Flag */
	private boolean updated = false;

	/** File Path */
	private String filePath;

	/** File Path */
	private String keystorePassword;

	/**
	 * Private Constructor
	 * 
	 */
	private KeystoreManager() {
	}

	/**
	 * Get Instance
	 * 
	 * @return KeystoreManager
	 */
	public final static KeystoreManager getInstance() {
		if (manager == null) {
			manager = new KeystoreManager();
			return manager;
		} else {
			return manager;
		}
	}

	/**
	 * Get Certificate
	 * 
	 * @return Certificate
	 */
	public Certificate getCertificate(String alias) throws KeyStoreException {
		return keyStore.getCertificate(alias);
	}

	/**
	 * Get Certificate
	 * 
	 * @return Certificate
	 */
	public Certificate[] getCertificateChain(String alias)
			throws KeyStoreException {
		return keyStore.getCertificateChain(alias);
	}

	/**
	 * Get Key Store
	 * 
	 * @return KeyStore
	 */
	protected KeyStore getKeyStore() {
		return keyStore;
	}
	
	/**
	 * Get Key
	 * 
	 * @param alias
	 * @param password
	 * 
	 * @return Certificate
	 */
	public Key getKey(String alias, String password) throws KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException {
		return keyStore.getKey(alias, password.toCharArray());
	}

	/**
	 * Get Key Store Provider
	 * 
	 * @return Provider
	 */
	public Provider getProvider() {
		return keyStore.getProvider();
	}

	/**
	 * Get Aliases
	 * 
	 * @return List
	 * @throws KeyStoreException
	 */
	public List<String> getAliases() throws KeyStoreException {
		List<String> list = new ArrayList<String>();

		for (Enumeration<String> e = keyStore.aliases(); e.hasMoreElements();) {
			list.add(e.nextElement());
		}

		return list;
	}

	/**
	 * Get File Path
	 * 
	 * @return String
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Delete Entry
	 * 
	 * @param alias
	 * @throws Exception
	 */
	public void deleteEntry(String alias) throws KeyStoreException {
		keyStore.deleteEntry(alias);
		processEntryDeleted(new KeystoreEvent(this));
	}

	/**
	 * Is Key Pair Entry
	 * 
	 * @param alias
	 * @throws Exception
	 */
	public boolean isKeyPairEntry(String alias) {
		try {
			if (isKeyEntry(alias)) {
				Certificate[] chain = getCertificateChain(alias);

				if (chain != null && chain.length > 0) {
					return true;
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Is Cert Entry
	 * 
	 * @param alias
	 * @throws Exception
	 */
	public boolean isCertificateEntry(String alias) {
		try {
			return keyStore.isCertificateEntry(alias);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Rename Certificate Entry
	 * 
	 * @param alias Alias
	 * @param newAlias New Alias
	 * 
	 * @throws KeyStoreException
	 */
	public void renameCertificateEntry(String alias, String newAlias)
			throws KeyStoreException {
		if (isCertificateEntry(alias)) {
			Certificate temp = getCertificate(alias);
			setCertificateEntry(newAlias, temp);
			deleteEntry(alias);
			processEntryImported(new KeystoreEvent(this));
		}
	}

	/**
	 * Rename Key Entry
	 * 
	 * @param alias Alias
	 * @param newAlias New Alias
	 * @param passw Password
	 * 
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public void renameKeyEntry(String alias, String newAlias, String passw)
			throws KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException {
		if (isKeyEntry(alias)) {
			if (isKeyPairEntry(alias)) {
				Key temp = getKey(alias, passw);
				Certificate[] chain = getCertificateChain(alias);
				setKeyEntry(newAlias,temp,passw,chain);
				deleteEntry(alias);
				processEntryImported(new KeystoreEvent(this));
			} else {
				Key temp = getKey(alias, passw);
				setKeyEntry(newAlias,temp,passw,new Certificate[0]);
				deleteEntry(alias);
				processEntryImported(new KeystoreEvent(this));
			}
		}
	}

	/**
	 * Get Creation Date
	 * 
	 * @param alias
	 * @throws Exception
	 */
	public Date getCreationDate(String alias) {
		try {
			return keyStore.getCreationDate(alias);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return null;
	}

	/**
	 * Entry Instance Of TrustedCertificate
	 * 
	 * @param alias
	 * 
	 * @return boolean
	 * 
	 * @throws KeyStoreException
	 */
	public boolean entryInstanceOfTrustedCertificate(String alias) {
		try {
			return entryInstanceOf(alias, TrustedCertificateEntry.class);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Entry Instance Of PrivateKeyEntry
	 * 
	 * @param alias
	 * 
	 * @return boolean
	 * 
	 * @throws KeyStoreException
	 */
	public boolean entryInstanceOfPrivateKey(String alias) {
		try {
			return entryInstanceOf(alias, PrivateKeyEntry.class);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Entry Instance Of SecretKeyEntry
	 * 
	 * @param alias
	 * 
	 * @return boolean
	 * 
	 * @throws KeyStoreException
	 */
	public boolean entryInstanceOfSecretKey(String alias) {
		try {
			return entryInstanceOf(alias, SecretKeyEntry.class);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Clone Key
	 * 
	 * @param alias Alias
	 * @param newalias New Alias
	 * @param passwd Password
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 */
	public void cloneKey(String alias, String newalias, String passwd)
			throws KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException {
		if (entryInstanceOfPrivateKey(alias)) {
			Certificate[] chain = getCertificateChain(alias);
			Key key = (Key) getKey(alias, passwd);
			keyStore.setKeyEntry(newalias, key, passwd.toCharArray(), chain);
			processEntryImported(new KeystoreEvent(this));
		} else {
			Key key = getKey(alias, passwd);
			keyStore.setKeyEntry(newalias, key, passwd.toCharArray(), null);
			processEntryImported(new KeystoreEvent(this));
		}
	}

	/**
	 * Entry Instance Of
	 * 
	 * @param alias
	 * @param c
	 * 
	 * @return boolean
	 * 
	 * @throws KeyStoreException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean entryInstanceOf(String alias, Class c) throws KeyStoreException {
		return keyStore.entryInstanceOf(alias, c);
	}

	/**
	 * Is Key Entry
	 * 
	 * @param alias
	 * @throws Exception
	 */
	public boolean isKeyEntry(String alias) {
		try {
			return keyStore.isKeyEntry(alias);
		} catch (Exception err) {
			err.printStackTrace();
		}

		return false;
	}

	/**
	 * Export Certificate to File
	 * 
	 * @param alias Certificate Alias
	 * @param path File Path
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	public void exportBase64Certificate(String alias, String path)
			throws IOException, CertificateEncodingException, KeyStoreException {
		Certificate cert = keyStore.getCertificate(alias);

		// Get the encoded form which is suitable for exporting
		byte[] buf = cert.getEncoded();

		FileOutputStream os = new FileOutputStream(new File(path));

		// Write in text form
		Writer wr = new OutputStreamWriter(os, Charset.forName("UTF-8"));
		wr.write(CertificateUtil.BEGIN_CERTICATE);
		wr.write(new sun.misc.BASE64Encoder().encode(buf));
		wr.write(CertificateUtil.END_CERTIFICATE);
		wr.flush();

		os.close();
	}

	/**
	 * Import Certificate
	 * 
	 * @param alias Alias
	 * @param file File Path
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	public void importX509Certificate(String alias, String path)
			throws CertificateException, IOException, KeyStoreException {
		Certificate cert = CertificateUtil.readX509Certificate(path);
		keyStore.setCertificateEntry(alias, cert);
		processEntryImported(new KeystoreEvent(this));
	}

	/**
	 * Set Certificate Entry
	 * 
	 * @param alias
	 * @param cert
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	public void setCertificateEntry(String alias, Certificate cert)
			throws KeyStoreException {
		keyStore.setCertificateEntry(alias, cert);
		processEntryImported(new KeystoreEvent(this));
	}

	/**
	 * Set Key Entry
	 * 
	 * @param alias Entry Alias
	 * @param key Key
	 * @param passwd Key Password
	 * @param chain Certificate Chain
	 * 
	 * @throws KeyStoreException
	 */
	public void setKeyEntry(String alias, Key key, String passwd,
			Certificate[] chain) throws KeyStoreException {
		keyStore.setKeyEntry(alias, key, passwd.toCharArray(), chain);
		processEntryImported(new KeystoreEvent(this));
	}

	/**
	 * Get Key Store Type
	 * 
	 * @return String
	 */
	public final String getKeyStoreType() {
		return keyStore.getType();
	}

	/**
	 * Get Key Store Size
	 * 
	 * @return int
	 */
	public final int getSize() throws KeyStoreException {
		return keyStore.size();
	}

	/**
	 * Get Key Store Password
	 * 
	 * @return String
	 */
	public final String getKeystorePassword() {
		return keystorePassword;
	}

	/**
	 * Is Loaded
	 * 
	 * @return boolean
	 */
	public final boolean isLoaded() {
		return loaded;
	}

	/**
	 * Load Key Store
	 * 
	 * @param path File Path
	 * @param password Key Store Password
	 */
	public final void loadKeyStore(String path, String password)
			throws CertificateException, IOException, NoSuchAlgorithmException,
			KeyStoreException {
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(new File(path)), password
				.toCharArray());
		loaded = true;
		filePath = path;
		keystorePassword = password;
		processKeystoreLoaded(new KeystoreEvent(this));
	}

	/**
	 * Load Key Store
	 * 
	 * @param path File Path
	 * @param password Password
	 * @param type Key Store Type
	 * @param prov Provider Name
	 * 
	 * @throws CertificateException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws NoSuchProviderException
	 */
	public final void loadKeyStore(String path, String password, String type,
			String prov) throws CertificateException, IOException,
			NoSuchAlgorithmException, KeyStoreException,
			NoSuchProviderException {
		keyStore = KeyStore.getInstance(type, prov);
		keyStore.load(new FileInputStream(new File(path)), password
				.toCharArray());
		loaded = true;
		filePath = path;
		keystorePassword = password;
		processKeystoreLoaded(new KeystoreEvent(this));
	}

	/**
	 * New Key Store
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public void newKeyStore() throws CertificateException, KeyStoreException,
			NoSuchAlgorithmException, IOException {
		keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(null, null);
		filePath = null;
		keystorePassword = null;
		processKeystoreLoaded(new KeystoreEvent(this));
	}

	/**
	 * New Key Store
	 * 
	 * @param type Key Store Type
	 * @param provider Provider
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public void newKeyStore(String type, String prov)
			throws CertificateException, KeyStoreException,
			NoSuchAlgorithmException, IOException, NoSuchProviderException {
		keyStore = KeyStore.getInstance(type, prov);
		keyStore.load(null, null);
		filePath = null;
		keystorePassword = null;
		processKeystoreLoaded(new KeystoreEvent(this));
	}

	/**
	 * Add Keystore Listener
	 * 
	 * @param listener Keystore Listener
	 */
	public void addKeystoreListener(KeystoreListener listener) {
		listeners.add(listener);
	}

	/**
	 * Process Certificate Imported
	 * 
	 * @param evt Key Store Event
	 */
	private void processEntryImported(KeystoreEvent evt) {
		KeystoreListener listener;

		for (int i = 0; i < listeners.size(); i++) {
			listener = (KeystoreListener) listeners.get(i);
			listener.entryImported(evt);
		}

		updated = true;
	}

	/**
	 * Process Entry Deleted
	 * 
	 * @param evt Keystore Event
	 */
	private void processEntryDeleted(KeystoreEvent evt) {
		KeystoreListener listener;

		for (int i = 0; i < listeners.size(); i++) {
			listener = (KeystoreListener) listeners.get(i);
			listener.entryDeleted(evt);
		}

		updated = true;
	}

	/**
	 * Process Keystore Loaded
	 * 
	 * @param evt Keystore Event
	 */
	private void processKeystoreLoaded(KeystoreEvent evt) {
		KeystoreListener listener;

		for (int i = 0; i < listeners.size(); i++) {
			listener = (KeystoreListener) listeners.get(i);
			listener.keyStoreLoaded(evt);
		}

		updated = false;
	}

	/**
	 * Store Key Store
	 * 
	 * @param path File Path
	 * @param password Key Store Password
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public final void storeKeyStore(String path, String password)
			throws FileNotFoundException, IOException, CertificateException,
			KeyStoreException, NoSuchAlgorithmException {
		FileOutputStream fos = new FileOutputStream(path);
		keyStore.store(fos, password.toCharArray());
		fos.close();
		filePath = path;
		keystorePassword = password;
		updated = false;
	}

	/**
	 * Set Password
	 * 
	 * @param passwd New Password Takes Effect Upon Save
	 */
	public void setKeystorePassword(String passwd) {
		this.keystorePassword = passwd;
	}

	/**
	 * Is Keystore Updated
	 * 
	 * @return boolean
	 */
	public boolean isUpdated() {
		return updated;
	}
}
