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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V1CertificateGenerator;
import org.bouncycastle.util.encoders.Base64;

/**
 * Certificate Utility
 * 
 * @author Derek Helbert
 */
public class CertificateUtil {

	/** Begin Certificate */
	public static final String BEGIN_CERTICATE = "-----BEGIN CERTIFICATE-----";

	/** End Certificate */
	public static final String END_CERTIFICATE = "-----END CERTIFICATE-----";

	/** Begin Certification Request */
	public static final String BEGIN_CERT_REQUEST = "-----BEGIN CERTIFICATE REQUEST-----";

	/** End Certification Request */
	public static final String END_CERT_REQUEST = "-----END CERTIFICATE REQUEST-----";

	/** Maximum Length CSR */
	public static final int CERT_REQ_LINE_LENGTH = 76;

	/** X.509 Certificates Type */
	public static final String X509 = "X.509";

	/**
	 * Convert Certificate To X.509
	 * 
	 * @param cert
	 * @return X509Certificate
	 * @throws Exception
	 */
	public static X509Certificate convertCertificate(Certificate cert)
			throws CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance(X509);
		ByteArrayInputStream bais = new ByteArrayInputStream(cert.getEncoded());
		return (X509Certificate) cf.generateCertificate(bais);
	}

	/**
	 * Convert To X509Certificate Chain
	 * 
	 * @param certs Certificates
	 * 
	 * @return X509Certificate[]
	 * 
	 * @throws CertificateException
	 */
	public static X509Certificate[] convertCertificates(Certificate[] certsIn)
			throws CertificateException {
		X509Certificate[] chain = new X509Certificate[certsIn.length];

		for (int i = 0; i < certsIn.length; i++) {
			chain[i] = convertCertificate(certsIn[i]);
		}

		return chain;
	}

	/**
	 * Generate PKCS10 CSR
	 * 
	 * @param cert X590 Certificate
	 * @param privateKey Private Key
	 * @param path File Path
	 * 
	 * @return String
	 * @throws Exception
	 */
	public static void generatePKCS10CSR(X509Certificate cert,
			PrivateKey privateKey, String path) throws Exception {
		X509Name subject = new X509Name(cert.getSubjectDN().toString());

		PKCS10CertificationRequest csr = new PKCS10CertificationRequest(cert
				.getSigAlgName(), subject, cert.getPublicKey(), null,
				privateKey);

		// Verify CSR
		csr.verify();

		// Get Base 64 encoding of CSR
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DEROutputStream dos = new DEROutputStream(baos);
		dos.writeObject(csr.getDERObject());
		String sTmp = new String(Base64.encode(baos.toByteArray()));

		// CSR Header
		String csrText = BEGIN_CERT_REQUEST + "\n";

		// Wrap lines
		for (int iCnt = 0; iCnt < sTmp.length(); iCnt += CERT_REQ_LINE_LENGTH) {
			int iLineLength;

			if ((iCnt + CERT_REQ_LINE_LENGTH) > sTmp.length()) {
				iLineLength = sTmp.length() - iCnt;
			} else {
				iLineLength = CERT_REQ_LINE_LENGTH;
			}

			csrText += sTmp.substring(iCnt, iCnt + iLineLength) + "\n";
		}

		// CSR Footer
		csrText += END_CERT_REQUEST + "\n";

		// Write it out to file
		FileWriter fw = null;

		try {
			fw = new FileWriter(path);
			fw.write(csrText);
		} catch (Exception err) {
			throw err;
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	/**
	 * Generate Certificate
	 * 
	 * @param commonName Common Name
	 * @param organizationUnit Organization Unit
	 * @param organization Org
	 * @param locality Locality
	 * @param state State
	 * @param countryCode Country Code
	 * @param emailAddress Email Address
	 * @param validity Validity Length (Days)
	 * @param keyPair Key Pair
	 * @param signatureType Sig Type
	 * 
	 * @return X509Certificate
	 * 
	 * @throws Exception
	 */
	public static X509Certificate generateCertificate(String commonName,
			String organizationUnit, String organization, String locality,
			String state, String countryCode, String emailAddress,
			int validity, KeyPair keyPair, String signatureType)
			throws Exception {
		return generateCertificate(commonName, organizationUnit, organization,
				locality, state, countryCode, emailAddress, validity, keyPair
						.getPublic(), keyPair.getPrivate(), signatureType);
	}

	/**
	 * Generate Certificate
	 * 
	 * @param commonName Common Name
	 * @param organizationUnit Organization Unit
	 * @param organization Organization
	 * @param locality Locality
	 * @param state State
	 * @param countryCode Country Code
	 * @param emailAddress Email Address
	 * @param validity Validity (Days)
	 * @param publicKey Public Key
	 * @param privateKey Private Key
	 * @param signatureType Signature Type
	 * 
	 * @return X509Certificate
	 * 
	 * @throws Exception
	 */
	public static X509Certificate generateCertificate(String commonName,
			String organizationUnit, String organization, String locality,
			String state, String countryCode, String emailAddress,
			int validity, PublicKey publicKey, PrivateKey privateKey,
			String signatureType) throws Exception {
		// Holds certificate attributes
		Hashtable<Object,Object> attrs = new Hashtable<Object,Object>();
		Vector<Object>           order = new Vector<Object>();

		// Set certificate attributes
		if (commonName != null) {
			attrs.put(X509Principal.CN, commonName);
			order.add(0, X509Principal.CN);
		}

		if (organizationUnit != null) {
			attrs.put(X509Principal.OU, organizationUnit);
			order.add(0, X509Principal.OU);
		}

		if (organization != null) {
			attrs.put(X509Principal.O, organization);
			order.add(0, X509Principal.O);
		}

		if (locality != null) {
			attrs.put(X509Principal.L, locality);
			order.add(0, X509Principal.L);
		}

		if (state != null) {
			attrs.put(X509Principal.ST, state);
			order.add(0, X509Principal.ST);
		}

		if (countryCode != null) {
			attrs.put(X509Principal.C, countryCode);
			order.add(0, X509Principal.C);
		}

		if (emailAddress != null) {
			attrs.put(X509Principal.E, emailAddress);
			order.add(0, X509Principal.E);
		}

		// Get an X509 Version 1 Certificate generator
		X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();

		certGen.setIssuerDN(new X509Principal(order, attrs));

		// Valid before and after dates now to iValidity days in the future
		certGen.setNotBefore(new Date(System.currentTimeMillis()));
		certGen.setNotAfter(new Date(System.currentTimeMillis() + ((long) validity * 24 * 60 * 60 * 1000)));

		certGen.setSubjectDN(new X509Principal(order, attrs));
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm(signatureType);
		certGen.setSerialNumber(generateX509SerialNumber());

		// Generate an X.509 certificate, based on the current issuer and
		// subject
		X509Certificate cert = certGen.generateX509Certificate(privateKey);

		// Return the certificate
		return cert;
	}

	/**
	 * Generate Serial Number
	 * 
	 * @return BigInteger
	 */
	private static BigInteger generateX509SerialNumber() {
		return new BigInteger(Long.toString(System.currentTimeMillis() / 1000));
	}

	/**
	 * Get Certification Path
	 * 
	 * @param host Host Name
	 * @param port Port
	 * 
	 * @return Certificate[]
	 * 
	 * @throws SSLPeerUnverifiedException
	 * @throws IOException
	 */
	public static Certificate[] getCertificationPath(String host, int port)
			throws SSLPeerUnverifiedException, IOException {
		// Create the client socket
		SSLSocketFactory factory = HttpsURLConnection
				.getDefaultSSLSocketFactory();
		SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

		// Connect to the server
		socket.startHandshake();

		// Retrieve the server's certificate chain
		java.security.cert.Certificate[] certs = socket.getSession()
				.getPeerCertificates();

		// Close the socket
		socket.close();

		return certs;
	}

	/**
	 * Verify X.509 Certificate
	 * 
	 * @param signed Signed certificate
	 * @param signer Signing certificate
	 * 
	 * @return boolean
	 */
	public static boolean verifyCertificate(X509Certificate signed,
			X509Certificate signer) {
		try {
			signed.verify(signer.getPublicKey());
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	/**
	 * Establish Trust
	 * 
	 * @param cert Certificate
	 * @param keyStores Key Stores
	 * 
	 * @return X509Certificate[]
	 * 
	 * @throws KeyStoreException
	 */
	public static X509Certificate[] establishTrust(KeyStore[] keyStores, X509Certificate cert) throws KeyStoreException, CertificateException {
		List<Certificate> certs = new ArrayList<Certificate>();

		for (int iCnt = 0; iCnt < keyStores.length; iCnt++) {
			certs.addAll(getCertificates(keyStores[iCnt]));
		}

		return verifyTrust(certs, cert);
	}

	/**
	 * Get All Trusted Certificates
	 * 
	 * @param keyStore Keystore
	 * 
	 * @return Collection
	 * 
	 * @throws KeyStoreException
	 */
	private static Collection<Certificate> getCertificates(KeyStore keyStore) throws KeyStoreException, CertificateException {
		ArrayList<Certificate> vCerts = new ArrayList<Certificate>();

		for (Enumeration<String> en = keyStore.aliases(); en.hasMoreElements();) {
			String sAlias = en.nextElement();

			if (keyStore.isCertificateEntry(sAlias)) {
				vCerts.add(convertCertificate(keyStore.getCertificate(sAlias)));
			}
		}

		return vCerts;
	}

	/**
	 * Verify Trust
	 * 
	 * @param cert Certificate
	 * @param testCerts Test Certificates
	 * 
	 * @return X509Certificate[]
	 */
	private static X509Certificate[] verifyTrust(List<Certificate> testCerts, X509Certificate cert) {
		for (int iCnt = 0; iCnt < testCerts.size(); iCnt++) {
			X509Certificate compCert = (X509Certificate) testCerts.get(iCnt);

			// Check certificate issuer
			if (cert.getIssuerDN().equals(compCert.getSubjectDN())) {
				// Verify Certificate
				if (verifyCertificate(cert, compCert)) {
					if (compCert.getSubjectDN().equals(compCert.getIssuerDN())) {
						return new X509Certificate[] { cert, compCert };
					}

					// Establish a chain of trust
					X509Certificate[] tmpChain = verifyTrust(testCerts,
							compCert);

					if (tmpChain != null) {
						X509Certificate[] trustChain = new X509Certificate[tmpChain.length + 1];
						trustChain[0] = cert;

						for (int j = 1; j <= tmpChain.length; j++) {
							trustChain[j] = tmpChain[j - 1];
						}

						return trustChain;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Order X.509 Certificates
	 * 
	 * @param certs X590 Certificates
	 * 
	 * @return X509Certificate[]
	 */
	public static X509Certificate[] orderX509CertChain(X509Certificate[] certs) {
		int ordered = 0;

		X509Certificate[] clonedCerts = (X509Certificate[]) certs.clone();
		X509Certificate[] orderedCerts = new X509Certificate[certs.length];
		X509Certificate issuerCert = null;

		for (int i = 0; i < clonedCerts.length; i++) {
			X509Certificate tempCert = clonedCerts[i];

			if (tempCert.getIssuerDN().equals(tempCert.getSubjectDN())) {
				issuerCert = tempCert;
				orderedCerts[ordered] = issuerCert;
				ordered++;
			}
		}

		if (issuerCert == null) {
			return certs;
		}

		while (true) {
			boolean found = false;

			for (int i = 0; i < clonedCerts.length; i++) {
				X509Certificate tempCert = clonedCerts[i];

				if (tempCert.getIssuerDN().equals(issuerCert.getSubjectDN())
						&& tempCert != issuerCert) {
					issuerCert = tempCert;
					orderedCerts[ordered] = issuerCert;
					ordered++;
					found = true;
					break;
				}
			}

			if (!found) {
				break;
			}
		}

		return orderedCerts;
	}

	/**
	 * Read X509 Certificate From File
	 * 
	 * @param path File Path
	 * 
	 * @return Certificate
	 * 
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	public static X509Certificate readX509Certificate(String path)
			throws CertificateException, IOException, KeyStoreException {
		FileInputStream is = new FileInputStream(new File(path));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert = cf.generateCertificate(is);
		return convertCertificate(cert);
	}

	/**
	 * Read X509 Certificate Chain From File
	 * 
	 * @param path File Path
	 * 
	 * @return X509Certificate[]
	 * 
	 * @throws CertificateException
	 * @throws IOException
	 * @throws KeyStoreException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static X509Certificate[] readX509Certificates(String path) throws CertificateException, IOException, KeyStoreException {
		FileInputStream is = new FileInputStream(new File(path));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		
		Collection c = cf.generateCertificates(is);

		X509Certificate[] chain = new X509Certificate[c.size()];
		Iterator<Certificate> i = c.iterator();
		
		int cnt = 0;

		while (i.hasNext()) {
			chain[cnt++] = convertCertificate((Certificate) i.next());
		}

		return chain;
	}
}
