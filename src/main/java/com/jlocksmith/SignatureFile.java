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

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import sun.misc.BASE64Encoder;
import sun.security.util.ManifestDigester;

/**
 * Signature File
 * 
 * @author Derek Helbert
 */
@SuppressWarnings("restriction")
public class SignatureFile {

	/** Manifest */
	private Manifest sf;

	/** Base Name */
	private String baseName;
	
	/**
	 * Constructor
	 * 
	 * @param digests
	 * @param mf
	 * @param md
	 * @param baseName
	 * @param signManifest
	 */
	public SignatureFile(MessageDigest digests[], Manifest mf, ManifestDigester md, String baseName, boolean signManifest) {
		this.baseName = baseName;
		String version = System.getProperty("java.version");
		String javaVendor = System.getProperty("java.vendor");
		sf = new Manifest();
		
		Attributes mattr = sf.getMainAttributes();
		BASE64Encoder encoder = new BASE64Encoder();
		mattr.putValue(java.util.jar.Attributes.Name.SIGNATURE_VERSION.toString(), "1.0");
		mattr.putValue("Created-By", version + " (" + javaVendor + ")");
		
		if (signManifest) {
			for (int i = 0; i < digests.length; i++) {
				mattr.putValue(digests[i].getAlgorithm() + "-Digest-Manifest",encoder.encode(md.manifestDigest(digests[i])));
			}
		}
		
		Map<String,Attributes> entries = sf.getEntries();
		Iterator<java.util.Map.Entry<String,Attributes>> iter = mf.getEntries().entrySet().iterator();
		
		do {
			if (!iter.hasNext())
				break;
			
			java.util.Map.Entry<String,Attributes> e = iter.next();
			
			String name = (String) e.getKey();
			ManifestDigester.Entry mde = md.get(name, false);
			
			if (mde != null) {
				Attributes attr = new Attributes();
				
				for (int i = 0; i < digests.length; i++) {
					attr.putValue(digests[i].getAlgorithm() + "-Digest", encoder.encode(mde.digest(digests[i])));
				}
				
				entries.put(name, attr);
			}
		} 
		while (true);
	}

	/**
	 * Write
	 * 
	 * @param out
	 * 
	 * @throws IOException
	 */
	public void writeManifest(OutputStream out) throws IOException {
		sf.write(out);
	}

	/**
	 * Get Meta Name
	 * @return
	 */
	public String getMetaName() {
		return "META-INF/" + baseName + ".SF";
	}

	/**
	 * Get Base Name
	 * 
	 * @return String
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * Generate Block
	 * 
	 * @param privateKey
	 * @param certChain
	 * @param externalSF
	 * 
	 * @return Block
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws SignatureException
	 * @throws CertificateException
	 */
	public Block generateBlock(PrivateKey privateKey,
			X509Certificate certChain[], boolean externalSF)
			throws NoSuchAlgorithmException, InvalidKeyException, IOException,
			SignatureException, CertificateException {
		return new Block(this, privateKey, certChain, externalSF);
	}
}
