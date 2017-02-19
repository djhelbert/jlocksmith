/*
 * Copyright © 2011 Derek Helbert, djhelbert@gmail.com
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.jlocksmith.util.Util;

/**
 * Preferences
 * 
 * @author Derek Helbert
 * @version $Revision $Date: 2006/12/06 06:06:47 $
 */
public class Preferences {
	/** Use Ca Certs Keystore */
	private boolean useCaCertsKeystore = false;

	/** Ca Certs Keystore Path */
	private String caCertsKeystorePath = null;

	/** Ca Certs Keystore Path */
	private String caCertsKeystorePassword = "changeit";

	/** Pref File */
	private static String prefFile = ".jlocksmith";

	/** Use CA Certs Key */
	private static String USECACERTS = "use.cacerts";

	/** CA Certs Path Key */
	private static String CACERTSPATH = "cacerts.path";

	/** CA Certs Path Key */
	private static String CACERTSPASSWD = "cacerts.password";

	/**
	 * Constructor
	 * 
	 */
	public Preferences() throws IOException {
		String path = Util.getUserHome() + Util.getFileSeparator() + prefFile;
		load(path);
	}

	/**
	 * Get Ca Certs Keystore Path
	 * 
	 * @return
	 */
	public String getCaCertsKeystorePath() {
		return caCertsKeystorePath;
	}

	/**
	 * Set Ca Certs Keystore Path
	 * 
	 * @param caCertsKeystorePath
	 */
	public void setCaCertsKeystorePath(String caCertsKeystorePath) {
		this.caCertsKeystorePath = caCertsKeystorePath;
	}

	/**
	 * Is Use Ca Certs Keystore
	 * 
	 * @return
	 */
	public boolean isUseCaCertsKeystore() {
		return useCaCertsKeystore;
	}

	/**
	 * Set Use Ca Certs Keystore
	 * 
	 * @param useCaCertsKeystore
	 */
	public void setUseCaCertsKeystore(boolean useCaCertsKeystore) {
		this.useCaCertsKeystore = useCaCertsKeystore;
	}

	/**
	 * Get CA Certs Keystore Password
	 * 
	 * @return String
	 */
	public String getCaCertsKeystorePassword() {
		return caCertsKeystorePassword;
	}

	/**
	 * Set CA Certs Keystore Password
	 * 
	 * @param caCertsKeystorePassword
	 */
	public void setCaCertsKeystorePassword(String caCertsKeystorePassword) {
		this.caCertsKeystorePassword = caCertsKeystorePassword;
	}

	/**
	 * Load
	 * 
	 * @param path File Path
	 * 
	 * @throws IOException
	 */
	public void load(String path) throws IOException {
		File file = new File(path);

		if (file.exists()) {
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream(file);
			prop.load(fis);
			fis.close();

			setUseCaCertsKeystore("true".equals(prop.getProperty(USECACERTS)));
			setCaCertsKeystorePath(prop.getProperty(CACERTSPATH));
			setCaCertsKeystorePassword(prop.getProperty(CACERTSPASSWD));
		}
	}

	/**
	 * Delete
	 * 
	 */
	public void delete() {
		String path = Util.getUserHome() + Util.getFileSeparator() + prefFile;

		File file = new File(path);

		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Save
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		Properties prop = new Properties();
		prop.setProperty(USECACERTS, useCaCertsKeystore ? "true" : "false");
		prop.setProperty(CACERTSPATH, caCertsKeystorePath == null ? ""
				: caCertsKeystorePath);
		prop.setProperty(CACERTSPASSWD, caCertsKeystorePassword == null ? ""
				: caCertsKeystorePassword);

		FileOutputStream fos = new FileOutputStream(Util.getUserHome()
				+ Util.getFileSeparator() + prefFile);
		prop.store(fos, "jLocksmith");
		fos.close();
	}
}
