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

import java.security.*;
import java.util.*;

/**
 * Security Utility
 * 
 * @author Derek Helbert
 */
public class SecurityUtil {

	/** PKCS12 */
	public static String PKCS12 = "pkcs12";

	/** JKS */
	public static String JKS = "jks";

	/** BKS */
	public static String BKS = "bks";

	/** UBER */
	public static String UBER = "uber";

	/** JCEKS */
	public static String JCEKS = "jceks";

	/** SUN */
	public static String SUN = "SUN";

	/** BC */
	public static String BC = "BC";

	/** SUN */
	public static String SunJCE = "SunJCE";

	/** Util */
	private static SecurityUtil util;

	/**
	 * Constructor
	 * 
	 */
	private SecurityUtil() {
	}

	/**
	 * Get Instance
	 * 
	 * @return SecurityUtil
	 */
	public static SecurityUtil getInstance() {
		if (util == null) {
			util = new SecurityUtil();
			return util;
		} else {
			return util;
		}
	}

	/**
	 * Add Provider
	 * 
	 * @param provider
	 */
	public void addProvider(Provider provider) {
		Security.addProvider(provider);
	}

	/**
	 * Get Providers
	 * 
	 * @return Provider[]
	 */
	public Provider[] getProviders() {
		return Security.getProviders();
	}

	/**
	 * Get Providers
	 * 
	 * @return Provider[]
	 */
	public String[] getProviderNames() {
		Provider[] provs = getProviders();
		String[] names = new String[provs.length];

		for (int i = 0; i < provs.length; i++) {
			names[i] = provs[i].getName();
		}

		return names;
	}

	/**
	 * Get Providers
	 * 
	 * @return Provider[]
	 */
	public String[] getStoreTypes() {
		String[] names = new String[5];

		names[0] = JKS;
		names[1] = JCEKS;
		names[2] = PKCS12;
		names[3] = BKS;
		names[4] = UBER;

		return names;
	}

	/**
	 * Get Key Store Provider Name
	 * 
	 * @param storeType
	 * @return String
	 */
	public String getKeystoreProviderName(String storeType) {
		if (JKS.equals(storeType)) {
			return SUN;
		} else if (JCEKS.equals(storeType)) {
			return SunJCE;
		} else if (PKCS12.equals(storeType)) {
			return BC;
		} else if (BKS.equals(storeType)) {
			return BC;
		} else if (UBER.equals(storeType)) {
			return BC;
		} else {
			return null;
		}
	}

	/**
	 * Get File Ext Keystore Map
	 * 
	 * @return Map
	 */
	public Map<String,String> getFileKeystoreMap() {
		Map<String,String> map = new HashMap<String,String>();

		map.put(".keystore", JKS);
		map.put(".ks", JKS);
		map.put(".jks", JKS);
		map.put(".jce", JCEKS);
		map.put(".ubr", UBER);
		map.put(".bks", BKS);
		map.put(".p12", PKCS12);
		map.put(".pfx", PKCS12);

		return map;
	}

	/**
	 * Get Keystore File Ext Map
	 * 
	 * @param keystoreType Keystore Type
	 * @return Map
	 */
	public Map<String,String> getKeystoreFileMap(String keystoreType) {
		Map<String,String> map = new HashMap<String,String>();

		if (keystoreType == null) {
			return map;
		} else if (keystoreType.equals(JKS)) {
			map.put(".keystore", JKS);
			map.put(".ks", JKS);
			map.put(".jks", JKS);
		} else if (keystoreType.equals(JCEKS)) {
			map.put(".jce", JKS);
		} else if (keystoreType.equals(BKS)) {
			map.put(".bks", BKS);
		} else if (keystoreType.equals(UBER)) {
			map.put(".ubr", UBER);
		} else if (keystoreType.equals(PKCS12)) {
			map.put(".p12", PKCS12);
			map.put(".pfx", PKCS12);
		}

		return map;
	}

	/**
	 * Get Provider
	 * 
	 * @param name
	 * @return Provider
	 */
	public Provider getProvider(String name) {
		return Security.getProvider(name);
	}

	/**
	 * Get Cryptographic Services from Provider
	 * 
	 * @param serviceType
	 * 
	 * @return String[]
	 */
	public static String[] getCryptoImpls(Provider prov, String serviceType) {
		Set<String> result = new HashSet<String>();
		Set<Object> keys   = prov.keySet();

		for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			
			key = key.split(" ")[0];

			if (key.startsWith(serviceType + ".")) {
				result.add(key.substring(serviceType.length() + 1));
			} else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
				// This is an alias
				result.add(key.substring(serviceType.length() + 11));
			}
		}

		return (String[]) result.toArray(new String[result.size()]);
	}

	/**
	 * Get Cryptographic Services from All Providers
	 * 
	 * @param serviceType
	 * 
	 * @return String[]
	 */
	public static String[] getCryptoImpls(String serviceType) {
		Set<String> result = new HashSet<String>();

		// All all providers
		Provider[] providers = Security.getProviders();

		for (int i = 0; i < providers.length; i++) {
			// Get services provided by each provider
			Set<Object> keys = providers[i].keySet();

			for (Iterator<Object> it = keys.iterator(); it.hasNext();) {
				String key = (String) it.next();
				
				key = key.split(" ")[0];

				if (key.startsWith(serviceType + ".")) {
					result.add(key.substring(serviceType.length() + 1));
				} else if (key.startsWith("Alg.Alias." + serviceType + ".")) {
					// This is an alias
					result.add(key.substring(serviceType.length() + 11));
				}
			}
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
}
