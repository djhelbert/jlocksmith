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

/**
 * SignatureUtil
 * 
 * @author Derek Helbert
 */
public class SignatureUtil {

	/** SHA-1 with DSA Signature Type */
	public static String DSA_SHA1 = "SHA1withDSA";

	/** MD2 with RSA Signature Type */
	public static String RSA_MD2 = "MD2withRSA";

	/** MD5 with RSA Signature Type */
	public static String RSA_MD5 = "MD5withRSA";

	/** SHA-1 with RSA Signature Type */
	public static String RSA_SHA1 = "SHA1withRSA";

	/** SHA-224 with RSA Signature Type */
	public static String RSA_SHA224 = "SHA224withRSA";

	/** SHA-256 with RSA Signature Type */
	public static String RSA_SHA256 = "SHA256withRSA";

	/** SHA-384 with RSA Signature Type */
	public static String RSA_SHA384 = "SHA384withRSA";

	/** SHA-512 with RSA Signature Type */
	public static String RSA_SHA512 = "SHA512withRSA";

	/** RIPEMD160 with RSA Signature Type */
	public static String RSA_RIPEMD160 = "RIPEMD160withRSA";

	/** RSA Signature Types */
	private static String[] RSA_SIGNATURE_TYPES = { RSA_MD2, RSA_MD5, RSA_SHA1,
			RSA_SHA224, RSA_SHA224, RSA_SHA256, RSA_SHA384, RSA_SHA512,
			RSA_RIPEMD160 };

	/** DSA Signature Types */
	private static String[] DSA_SIGNATURE_TYPES = { DSA_SHA1 };

	/**
	 * Get RSA Signature Types
	 * 
	 * @return String[]
	 */
	public static String[] getRsaSignatureTypes() {
		return RSA_SIGNATURE_TYPES;
	}

	/**
	 * Get DSA Signature Types
	 * 
	 * @return String[]
	 */
	public static String[] getDsaSignatureTypes() {
		return DSA_SIGNATURE_TYPES;
	}
}
