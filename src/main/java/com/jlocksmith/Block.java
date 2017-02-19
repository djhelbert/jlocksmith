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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertInfo;

/**
 * Block
 * 
 * @author Derek Helbert
 */
@SuppressWarnings("restriction")
public class Block {

	/** Block */
	private PKCS7 block;

	/** Block File Name */
	private String blockFileName;

	/**
	 * Block
	 * 
	 * @param sfg
	 * @param privateKey
	 * @param certChain
	 * @param externalSF
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws SignatureException
	 * @throws CertificateException
	 */
	public Block(SignatureFile sfg, PrivateKey privateKey,
			X509Certificate certChain[], boolean externalSF)
			throws NoSuchAlgorithmException, InvalidKeyException, IOException,
			SignatureException, CertificateException {
		Principal issuerName = certChain[0].getIssuerDN();

		if (!(issuerName instanceof X500Name)) {
			X509CertInfo tbsCert = new X509CertInfo(certChain[0]
					.getTBSCertificate());
			issuerName = (Principal) tbsCert.get("issuer.dname");
		}

		java.math.BigInteger serial = certChain[0].getSerialNumber();

		String keyAlgorithm = privateKey.getAlgorithm();
		String digestAlgorithm;

		if (keyAlgorithm.equalsIgnoreCase("DSA")) {
			digestAlgorithm = "SHA1";
		} else if (keyAlgorithm.equalsIgnoreCase("RSA")) {
			digestAlgorithm = "MD5";
		} else {
			throw new NoSuchAlgorithmException();
		}

		String signatureAlgorithm = digestAlgorithm + "with" + keyAlgorithm;
		blockFileName = "META-INF/" + sfg.getBaseName() + "." + keyAlgorithm;

		AlgorithmId digestAlg  = AlgorithmId.get(digestAlgorithm);
		AlgorithmId digEncrAlg = AlgorithmId.get(keyAlgorithm);
		
		Signature sig = Signature.getInstance(signatureAlgorithm);
		sig.initSign(privateKey);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		sfg.writeManifest(baos);

		byte bytes[] = baos.toByteArray();

		ContentInfo contentInfo;

		if (externalSF) {
			contentInfo = new ContentInfo(ContentInfo.DATA_OID, null);
		} else {
			contentInfo = new ContentInfo(bytes);
		}

		sig.update(bytes);

		byte signature[] = sig.sign();

		SignerInfo signerInfo = new SignerInfo((X500Name) issuerName, serial,
				digestAlg, digEncrAlg, signature);

		AlgorithmId algs[] = { digestAlg };
		SignerInfo infos[] = { signerInfo };

		block = new PKCS7(algs, contentInfo, certChain, infos);
	}

	/**
	 * Get Meta Name
	 * 
	 * @return String
	 */
	public String getMetaName() {
		return blockFileName;
	}

	/**
	 * Write Block
	 * 
	 * @param out Output Stream
	 * 
	 * @throws IOException
	 */
	public void writeBlock(OutputStream out) throws IOException {
		block.encodeSignedData(out);
	}
}
