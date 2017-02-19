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
package com.jlocksmith.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.smime.SMIMECapabilities;
import org.bouncycastle.asn1.smime.SMIMECapability;
import org.bouncycastle.asn1.x509.*;

/**
 * Extension Utility
 * 
 * @author Derek Helbert
 * @version $Revision: 1.1 $ $Date: 2006/12/06 06:06:46 $
 */
public class ExtensionUtil {

	/** Common name OID */
	private static final String COMMON_NAME = "2.5.4.3";

	/** Subject Key Identifier OID */
	private static final String SUBJECT_KEY_IDENTIFIER = "2.5.29.14";

	/** Key Usage OID */
	private static final String KEY_USAGE = "2.5.29.15";

	/** Private Key Usage Period OID */
	private static final String PRIVATE_KEY_USAGE_PERIOD = "2.5.29.16";

	/** Subject Alternative Name OID */
	private static final String SUBJECT_ALTERNATIVE_NAME = "2.5.29.17";

	/** Issuer Alternative Name OID */
	private static final String ISSUER_ALTERNATIVE_NAME = "2.5.29.18";

	/** Basic Constraints OID */
	private static final String BASIC_CONSTRAINTS = "2.5.29.19";

	/** CRL Number OID */
	private static final String CRL_NUMBER = "2.5.29.20";

	/** Reason code OID */
	private static final String REASON_CODE = "2.5.29.21";

	/** Hold Instruction Code OID */
	private static final String HOLD_INSTRUCTION_CODE = "2.5.29.23";

	/** Invalidity Date OID */
	private static final String INVALIDITY_DATE = "2.5.29.24";

	/** Delta CRL Indicator OID */
	private static final String DELTA_CRL_INDICATOR = "2.5.29.27";

	/** Certificate Issuer OID */
	private static final String CERTIFICATE_ISSUER = "2.5.29.29";

	/** CRL Distribution Points OID */
	private static final String CRL_DISTRIBUTION_POINTS = "2.5.29.31";

	/** Certificate Policies OID */
	private static final String CERTIFICATE_POLICIES = "2.5.29.32";

	/** Policy Mappings OID */
	private static final String POLICY_MAPPINGS = "2.5.29.33";

	/** Authority Key Identifier OID */
	private static final String AUTHORITY_KEY_IDENTIFIER = "2.5.29.35";

	/** Policy Constraints OID */
	private static final String POLICY_CONSTRAINTS = "2.5.29.36";

	/** Extended Key Usage OID */
	private static final String EXTENDED_KEY_USAGE = "2.5.29.37";

	/** Inhibit Any Policy OID */
	private static final String INHIBIT_ANY_POLICY = "2.5.29.54";

	/** Entrust version extension OID */
	private static final String ENTRUST_VERSION_EXTENSION = "1.2.840.113533.7.65.0";

	/** S/MIME capabilities OID */
	private static final String SMIME_CAPABILITIES = "1.2.840.113549.1.9.15";

	/** MS certificate template name OID */
	private static final String MICROSOFT_CERTIFICATE_TEMPLATE_V1 = "1.3.6.1.4.1.311.20.2";

	/** MS CA Version OID */
	private static final String MICROSOFT_CA_VERSION = "1.3.6.1.4.1.311.21.1";

	/** MS Certificate Template (v2) OID */
	private static final String MICROSOFT_CERTIFICATE_TEMPLATE_V2 = "1.3.6.1.4.1.311.21.7";

	/** Authority Information Access OID */
	private static final String AUTHORITY_INFORMATION_ACCESS = "1.3.6.1.5.5.7.1.1";

	/** Novell Security Attributes OID */
	private static final String NOVELL_SECURITY_ATTRIBUTES = "2.16.840.1.113719.1.9.4.1";

	/** Netscape Certificate Type OID */
	private static final String NETSCAPE_CERTIFICATE_TYPE = "2.16.840.1.113730.1.1";

	/** Netscape Base URL OID */
	private static final String NETSCAPE_BASE_URL = "2.16.840.1.113730.1.2";

	/** Netscape Revocation URL OID */
	private static final String NETSCAPE_REVOCATION_URL = "2.16.840.1.113730.1.3";

	/** Netscape CA Revocation URL OID */
	private static final String NETSCAPE_CA_REVOCATION_URL = "2.16.840.1.113730.1.4";

	/** Netscape Certificate Renewal URL OID */
	private static final String NETSCAPE_CERTIFICATE_RENEWAL_URL = "2.16.840.1.113730.1.7";

	/** Netscape CA Policy URL OID */
	private static final String NETSCAPE_CA_POLICY_URL = "2.16.840.1.113730.1.8";

	/** Netscape SSL Server Name OID */
	private static final String NETSCAPE_SSL_SERVER_NAME = "2.16.840.1.113730.1.12";

	/** Netscape Comment OID */
	private static final String NETSCAPE_COMMENT = "2.16.840.1.113730.1.13";

	/** D&B D-U-N-S number OID */
	private static final String DNB_DUNS_NUMBER = "2.16.840.1.113733.1.6.15";

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();
	
	/**
	 * Convert To Hex String
	 * 
	 * @param bytes Bytes
	 * 
	 * @return String
	 */
	private static String convertToHexString(byte[] bytes) {
		// Convert to hex
		StringBuffer strBuff = new StringBuffer(new BigInteger(1, bytes).toString(16).toUpperCase());

		// Place space every 4 chars
		if (strBuff.length() > 4) {
			for (int i = 4; i < strBuff.length(); i += 5) {
				strBuff.insert(i, ' ');
			}
		}

		return strBuff.toString();
	}
	
	/**
	 * Convert To Hex String
	 * 
	 * @param derInt
	 * 
	 * @return String
	 */
	private static String convertToHexString(DERInteger derInt) {
		String sHexCrlNumber = derInt.getValue().toString(16).toUpperCase();
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0; i < sHexCrlNumber.length(); i++) {
			strBuff.append(sHexCrlNumber.charAt(i));

			if ((((i + 1) % 4) == 0)
					&& ((i + 1) != sHexCrlNumber.length())) {
				strBuff.append(' ');
			}
		}

		return strBuff.toString();
	}
	
	/**
	 * Get String Value
	 * 
	 * @param bytes Bytes
	 * @param oid OID
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public String getStringValue(byte[] bytes, String oid) throws IOException, ParseException {
		byte[] bOctets = ((DEROctetString) toDERObject(bytes)).getOctets();

		if (oid.equals(COMMON_NAME)) {
			return getCommonNameStringValue(bOctets);
		} 
		else if (oid.equals(SUBJECT_KEY_IDENTIFIER)) {
			return getSubjectKeyIndentifierStringValue(bOctets);
		} 
		else if (oid.equals(KEY_USAGE)) {
			return getKeyUsageStringValue(bOctets);
		} 
		else if (oid.equals(PRIVATE_KEY_USAGE_PERIOD)) {
			return getPrivateKeyUsagePeriod(bOctets);
		} 
		else if (oid.equals(SUBJECT_ALTERNATIVE_NAME)) {
			return getSubjectAltName(bOctets);
		} 
		else if (oid.equals(ISSUER_ALTERNATIVE_NAME)) {
			return getIssuerAltName(bOctets);
		} 
		else if (oid.equals(BASIC_CONSTRAINTS)) {
			return getBasicConstraintsStringValue(bOctets);
		}
		else if (oid.equals(CRL_NUMBER)) {
			return getCrlNumStringValue(bOctets);
		} 
		else if (oid.equals(REASON_CODE)) {
			return getReasonCodeStringValue(bOctets);
		} 
		else if (oid.equals(HOLD_INSTRUCTION_CODE)) {
			return getHoldInstructionCodeStringValue(bOctets);
		} 
		else if (oid.equals(INVALIDITY_DATE)) {
			return getInvalidityDateStringValue(bOctets);
		} 
		else if (oid.equals(DELTA_CRL_INDICATOR)) {
			return getDeltaCrlIndicatorStringValue(bOctets);
		} 
		else if (oid.equals(CERTIFICATE_ISSUER)) {
			return getCertificateIssuerStringValue(bOctets);
		} 
		else if (oid.equals(POLICY_MAPPINGS)) {
			return getPolicyMappingsStringValue(bOctets);
		} 
		else if (oid.equals(AUTHORITY_KEY_IDENTIFIER)) {
			return getAuthorityKeyIdentifierStringValue(bOctets);
		} 
		else if (oid.equals(POLICY_CONSTRAINTS)) {
			return getPolicyConstraintsStringValue(bOctets);
		} 
		else if (oid.equals(EXTENDED_KEY_USAGE)) {
			return getExtendedKeyUsageStringValue(bOctets);
		} 
		else if (oid.equals(INHIBIT_ANY_POLICY)) {
			return getInhibitAnyPolicyStringValue(bOctets);
		} 
		else if (oid.equals(ENTRUST_VERSION_EXTENSION)) {
			return getEntrustVersionExtensionStringValue(bOctets);
		} 
		else if (oid.equals(SMIME_CAPABILITIES)) {
			return getSmimeCapabilitiesStringValue(bOctets);
		} 
		else if (oid.equals(MICROSOFT_CERTIFICATE_TEMPLATE_V1)) {
			return getMicrosoftCertificateTemplateV1StringValue(bOctets);
		} 
		else if (oid.equals(MICROSOFT_CA_VERSION)) {
			return getMicrosoftCAVersionStringValue(bOctets);
		} 
		else if (oid.equals(MICROSOFT_CERTIFICATE_TEMPLATE_V2)) {
			return getMicrosoftCertificateTemplateV2StringValue(bOctets);
		} 
		else if (oid.equals(AUTHORITY_INFORMATION_ACCESS)) {
			return getAuthorityInformationAccessStringValue(bOctets);
		}
		else if (oid.equals(NOVELL_SECURITY_ATTRIBUTES)) {
			return getNovellSecurityAttributesStringValue(bOctets);
		} 
		else if (oid.equals(NETSCAPE_CERTIFICATE_TYPE)) {
			return getNetscapeCertificateTypeStringValue(bOctets);
		} 
		else if (oid.equals(NETSCAPE_BASE_URL)
				|| oid.equals(NETSCAPE_REVOCATION_URL)
				|| oid.equals(NETSCAPE_CA_REVOCATION_URL)
				|| oid.equals(NETSCAPE_CERTIFICATE_RENEWAL_URL)
				|| oid.equals(NETSCAPE_CA_POLICY_URL)
				|| oid.equals(NETSCAPE_SSL_SERVER_NAME)
				|| oid.equals(NETSCAPE_COMMENT)) {
			return getNonNetscapeCertificateTypeStringValue(bOctets);
		} 
		else if (oid.equals(DNB_DUNS_NUMBER)) {
			return getDnBDUNSNumberStringValue(bOctets);
		} 
		else if (oid.equals(CRL_DISTRIBUTION_POINTS)) {
			return getCrlDistributionPointsStringValue(bOctets);
		} 
		else if (oid.equals(CERTIFICATE_POLICIES)) {
			return getCertificatePoliciesStringValue(bOctets);
		} 
		else {
			return "?";
		}
	}

	/**
	 * Get Common Name String Value
	 * 
	 * @param bValue
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getCommonNameStringValue(byte[] bytes) throws IOException {
		return getObjectString(toDERObject(bytes));
	}

	/**
	 * Get Subject Key Identifier String Value
	 * 
	 * @param bytes Bytes
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getSubjectKeyIndentifierStringValue(byte[] bytes) throws IOException {
		DEROctetString derOctetStr = (DEROctetString) toDERObject(bytes);

		byte[] octets = derOctetStr.getOctets();

		StringBuffer strBuff = new StringBuffer();
		strBuff.append(convertToHexString(octets));
		strBuff.append('\n');
		
		return strBuff.toString();
	}

	/**
	 * Get Key Usage String Value
	 * 
	 * @param bytes Bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getKeyUsageStringValue(byte[] bytes) throws IOException {
		DERBitString derBitStr = (DERBitString) toDERObject(bytes);
		StringBuffer strBuff = new StringBuffer();
		
		byte[] newBytes = derBitStr.getBytes();
		
		boolean keyAgreement = false;

		for (int i = 0; i < newBytes.length; i++) {
			boolean[] b = new boolean[8];

			b[7] = (newBytes[i] & 0x80) == 0x80;
			b[6] = (newBytes[i] & 0x40) == 0x40;
			b[5] = (newBytes[i] & 0x20) == 0x20;
			b[4] = (newBytes[i] & 0x10) == 0x10;
			b[3] = (newBytes[i] & 0x8)  == 0x8;
			b[2] = (newBytes[i] & 0x4)  == 0x4;
			b[1] = (newBytes[i] & 0x2)  == 0x2;
			b[0] = (newBytes[i] & 0x1)  == 0x1;

			if (i == 0) {
				if (b[7]) {
					strBuff.append(localeUtil.getString("DigitalSignatureKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[6]) {
					strBuff.append(localeUtil.getString("NonRepudiationKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[5]) {
					strBuff.append(localeUtil.getString("KeyEnciphermentKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[4]) {
					strBuff.append(localeUtil.getString("DataEnciphermentKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[3]) {
					strBuff.append(localeUtil.getString("KeyAgreementKeyUsageString"));
					strBuff.append('\n');
					keyAgreement = true;
				}
				if (b[2]) {
					strBuff.append(localeUtil.getString("KeyCertSignKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[1]) {
					strBuff.append(localeUtil.getString("CrlSignKeyUsageString"));
					strBuff.append('\n');
				}
				if (b[0] && keyAgreement) {
					strBuff.append(localeUtil.getString("EncipherOnlyKeyUsageString"));
					strBuff.append('\n');
				}
			} else if (i == 1) {
				if (b[7] && keyAgreement) {
					strBuff.append(localeUtil.getString("DecipherOnlyKeyUsageString"));
					strBuff.append('\n');
				}
			}
		}

		return strBuff.toString();
	}

	/**
	 * Get Private Key Usage Period
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	private String getPrivateKeyUsagePeriod(byte[] bytes) throws IOException,
			ParseException {
		ASN1Sequence times = (ASN1Sequence) toDERObject(bytes);

		StringBuffer strBuff = new StringBuffer();

		for (int i = 0, len = times.size(); i < len; i++) {
			DERTaggedObject derTag = (DERTaggedObject) times.getObjectAt(i);
			DEROctetString dOct = (DEROctetString) derTag.getObject();
			DERGeneralizedTime dTime = new DERGeneralizedTime(new String(dOct
					.getOctets()));

			strBuff.append(MessageFormat.format(localeUtil.getString("PrivateKeyUsagePeriod." + derTag.getTagNo()), new Object[] {formatGeneralizedTime(dTime)}));

			strBuff.append('\n');
		}

		return strBuff.toString();
	}

	/**
	 * Get Subject Alternative Name
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getSubjectAltName(byte[] bytes) throws IOException {
		ASN1Sequence generalNames = (ASN1Sequence) toDERObject(bytes);
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0, len = generalNames.size(); i < len; i++) {
			strBuff.append(getGeneralNameString((DERTaggedObject) generalNames
					.getObjectAt(i)));
			strBuff.append('\n');
		}

		return strBuff.toString();
	}

	/**
	 * Get Issuer Alternative Name
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getIssuerAltName(byte[] bytes) throws IOException {
		ASN1Sequence generalNames = (ASN1Sequence) toDERObject(bytes);
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0, len = generalNames.size(); i < len; i++) {
			strBuff.append(getGeneralNameString((DERTaggedObject) generalNames
					.getObjectAt(i)));
			
			strBuff.append('\n');
		}
		return strBuff.toString();
	}

	/**
	 * Get Basic Constraints String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getBasicConstraintsStringValue(byte[] bytes)
			throws IOException {
		// Get sequence
		ASN1Sequence asn1Seq = (ASN1Sequence) toDERObject(bytes);
		int aLen = asn1Seq.size();

		// Default values when none specified in sequence
		boolean bCa = false;
		int iPathLengthConstraint = -1;

		// Read CA boolean if present in sequence
		if (aLen > 0) {
			DERBoolean derBool = (DERBoolean) asn1Seq.getObjectAt(0);
			bCa = derBool.isTrue();
		}

		// Read Path Length Constraint boolean if present in sequence
		if (aLen > 1) {
			DERInteger derInt = (DERInteger) asn1Seq.getObjectAt(1);
			iPathLengthConstraint = derInt.getValue().intValue();
		}

		// Output information
		StringBuffer strBuff = new StringBuffer();

		strBuff.append(localeUtil.getString(bCa ? "SubjectIsCa" : "SubjectIsNotCa"));

		strBuff.append('\n');

		// Path length constraint (only has meaning when CA is true)
		if (iPathLengthConstraint != -1 && bCa) {
			strBuff.append(MessageFormat.format(localeUtil.getString("PathLengthConstraint"), new Object[] {"" + iPathLengthConstraint}));
		} else {
			strBuff.append(localeUtil.getString("NoPathLengthConstraint"));
		}
		strBuff.append('\n');

		return strBuff.toString();
	}

	/**
	 * Get Crl Number String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getCrlNumStringValue(byte[] bytes) throws IOException {
		// Get CRL number
		DERInteger derInt = (DERInteger) toDERObject(bytes);

		// Convert to and return hex string representation of number
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(convertToHexString(derInt));
		strBuff.append('\n');
		
		return strBuff.toString();
	}

	/**
	 * Get Reason Code Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getReasonCodeStringValue(byte[] bytes) throws IOException {
		int irc = ((DEREnumerated) toDERObject(bytes)).getValue().intValue();

		String sRc = getResource("CrlReason." + irc,
				"UnrecognisedCrlReasonString");

		return MessageFormat.format(sRc, new Object[] { "" + irc }) + '\n';
	}

	/**
	 * Get Hold Instruction Code String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getHoldInstructionCodeStringValue(byte[] bytes) throws IOException {
		String sHoldIns = ((DERObjectIdentifier) toDERObject(bytes)).getId();
		
		String r = getResource(sHoldIns, "UnrecognisedHoldInstructionCode");

		return MessageFormat.format(r, new Object[] {sHoldIns}) + '\n';
	}

	/**
	 * Get Invalidity Date String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	private String getInvalidityDateStringValue(byte[] bytes)
			throws IOException, ParseException {
		// Get invalidity
		DERGeneralizedTime invalidityDate = (DERGeneralizedTime) toDERObject(bytes);

		// Format invalidity date
		String sInvalidityTime = formatGeneralizedTime(invalidityDate);

		StringBuffer strBuff = new StringBuffer();
		strBuff.append(sInvalidityTime);
		strBuff.append('\n');

		return strBuff.toString();
	}

	/**
	 * Get Delta Crl Indicator String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getDeltaCrlIndicatorStringValue(byte[] bytes)
			throws IOException {
		// Get CRL number
		DERInteger derInt = (DERInteger) toDERObject(bytes);

		// Convert to and return hex string representation of number
		StringBuffer strBuff = new StringBuffer();
		strBuff.append(convertToHexString(derInt));
		strBuff.append('\n');

		return strBuff.toString();
	}

	/**
	 * Get Certificate Issuer String Value
	 * 
	 * @param bytes
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	private String getCertificateIssuerStringValue(byte[] bytes)
			throws IOException {
		ASN1Sequence generalNames = (ASN1Sequence) toDERObject(bytes);
		StringBuffer strBuff = new StringBuffer();

		for (int i = 0, len = generalNames.size(); i < len; i++) {
			strBuff.append(getGeneralNameString((DERTaggedObject) generalNames
					.getObjectAt(i)));
			
			strBuff.append('\n');
		}

		return strBuff.toString();
	}

	/**
	 * Get Policy Mappings String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getPolicyMappingsStringValue(byte[] bytes) throws IOException {
		// Get sequence of policy mappings
		ASN1Sequence policyMappings = (ASN1Sequence) toDERObject(bytes);
		StringBuffer strBuff        = new StringBuffer();

		// Get each policy mapping
		for (int i = 0, len = policyMappings.size(); i < len; i++) {
			ASN1Sequence policyMapping = (ASN1Sequence) policyMappings.getObjectAt(i);
			int pmLen = policyMapping.size();

			strBuff.append(MessageFormat.format(localeUtil.getString("PolicyMapping"), new Object[] {"" + (i + 1) }));
			strBuff.append('\n');

			if (pmLen > 0) { // Policy mapping issuer domain policy
				DERObjectIdentifier issuerDomainPolicy = (DERObjectIdentifier) policyMapping
						.getObjectAt(0);
				strBuff.append('\t');
				strBuff.append(MessageFormat.format(localeUtil.getString("IssuerDomainPolicy"), new Object[] {issuerDomainPolicy.getId()}));
				strBuff.append('\n');
			}

			if (pmLen > 1) {
				DERObjectIdentifier subjectDomainPolicy = (DERObjectIdentifier) policyMapping.getObjectAt(1);
				strBuff.append('\t');
				strBuff.append(MessageFormat.format(localeUtil.getString("SubjectDomainPolicy"),new Object[] { subjectDomainPolicy.getId() }));
				strBuff.append('\n');
			}
		}

		return strBuff.toString();
	}

	/**
	 * Get Authority Key Identifier String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getAuthorityKeyIdentifierStringValue(byte[] bytes) throws IOException {
		ASN1Sequence asn1Seq = (ASN1Sequence) toDERObject(bytes);

		DEROctetString keyIdentifier           = null;
		ASN1Sequence   authorityCertIssuer     = null;
		DEROctetString certificateSerialNumber = null;

		for (int i = 0, len = asn1Seq.size(); i < len; i++) {
			DERTaggedObject derTagObj = (DERTaggedObject) asn1Seq.getObjectAt(i);
			DERObject derObj = derTagObj.getObject();

			switch (derTagObj.getTagNo()) {
			case 0:
				keyIdentifier = (DEROctetString) derObj;
				break;
			case 1:
				if (derObj instanceof ASN1Sequence) {
					authorityCertIssuer = (ASN1Sequence) derObj;
				}
				else {
					authorityCertIssuer = new DERSequence(derObj);
				}
				break;
			case 2:
				certificateSerialNumber = (DEROctetString) derObj;
				break;
			}
		}

		StringBuffer strBuff = new StringBuffer();

		if (keyIdentifier != null) {
			byte[] bKeyIdent = keyIdentifier.getOctets();
			strBuff.append(MessageFormat.format(localeUtil.getString("KeyIdentifier"),new Object[] { convertToHexString(bKeyIdent) }));
			strBuff.append('\n');
		}

		if (authorityCertIssuer != null) {
			strBuff.append(localeUtil.getString("CertificateIssuer"));
			strBuff.append('\n');
			for (int i = 0, len = authorityCertIssuer.size(); i < len; i++) {
				DERTaggedObject generalName = (DERTaggedObject) authorityCertIssuer.getObjectAt(i);
				strBuff.append('\t');
				strBuff.append(getGeneralNameString(generalName));
				strBuff.append('\n');
			}
		}

		if (certificateSerialNumber != null) {
			byte[] bCertSerialNumber = certificateSerialNumber.getOctets();
			strBuff.append(MessageFormat.format(localeUtil.getString("CertificateSerialNumber"), new Object[] { convertToHexString(bCertSerialNumber) }));
			strBuff.append('\n');
		}

		return strBuff.toString();
	}

	/**
	 * Get Policy Constraints String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getPolicyConstraintsStringValue(byte[] bytes) throws IOException {
		ASN1Sequence policyConstraints = (ASN1Sequence) toDERObject(bytes);
		StringBuffer strBuff           = new StringBuffer();

		for (int i = 0, len = policyConstraints.size(); i < len; i++) {

			DERTaggedObject policyConstraint = (DERTaggedObject) policyConstraints
					.getObjectAt(i);
			DERInteger skipCerts = new DERInteger(
					((DEROctetString) policyConstraint.getObject()).getOctets());
			int iSkipCerts = skipCerts.getValue().intValue();

			switch (policyConstraint.getTagNo()) {
			case 0:
				strBuff.append(MessageFormat.format(localeUtil.getString("RequireExplicitPolicy"), new Object[] {"" + iSkipCerts }));
				strBuff.append('\n');
				break;
			case 1:
				strBuff.append(MessageFormat.format(localeUtil.getString("InhibitPolicyMapping"), new Object[] {"" + iSkipCerts }));
				strBuff.append('\n');
				break;
			}
		}

		return strBuff.toString();

	}

	/**
	 * Get Extended Key Usage String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getExtendedKeyUsageStringValue(byte[] bytes) throws IOException {
		ASN1Sequence asn1Seq = (ASN1Sequence) toDERObject(bytes);

		StringBuffer strBuff = new StringBuffer();

		for (int i = 0, len = asn1Seq.size(); i < len; i++) {
			String sOid = ((DERObjectIdentifier) asn1Seq.getObjectAt(i)).getId();
			String sEku = getResource(sOid, "UnrecognisedExtKeyUsageString");
			
			strBuff.append(MessageFormat.format(sEku, new Object[] { sOid }));
			strBuff.append('\n');
		}

		return strBuff.toString();
	}

	/**
	 * Get Inhibit Any Policy String Value
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private String getInhibitAnyPolicyStringValue(byte[] bytes) throws IOException {
		DERInteger skipCerts = (DERInteger) toDERObject(bytes);

		int iSkipCerts = skipCerts.getValue().intValue();

		StringBuffer strBuff = new StringBuffer();
		strBuff.append(MessageFormat.format(localeUtil.getString("InhibitAnyPolicy"), new Object[] { "" + iSkipCerts }));
		strBuff.append('\n');

		return strBuff.toString();
	}

	/**
	 * Get Entrust Version Extension String Value
	 * 
	 * @param bytes
	 * @return String
	 * @throws IOException
	 */
	private String getEntrustVersionExtensionStringValue(byte[] bytes) throws IOException {
		ASN1Sequence as = (ASN1Sequence) toDERObject(bytes);
		return ((DERGeneralString) as.getObjectAt(0)).getString();
	}

	/**
	 * Get Microsoft Certificate Template V1 String Value
	 * 
	 * @param bValue
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getMicrosoftCertificateTemplateV1StringValue(byte[] bytes) throws IOException {
		return ((DERBMPString) toDERObject(bytes)).getString() + '\n';
	}

	/**
	 * Get Microsoft Certificate Template V2 String Value
	 * 
	 * @param bytes
	 * @return String
	 * @throws IOException
	 */
	private String getMicrosoftCertificateTemplateV2StringValue(byte[] bytes)
			throws IOException {
		ASN1Sequence seq = (ASN1Sequence) toDERObject(bytes);
		StringBuffer sb = new StringBuffer();

		sb.append(MessageFormat.format(localeUtil.getString("MsftCertTemplateId"),new Object[] { ((DERObjectIdentifier) seq.getObjectAt(0)).getId() }));
		sb.append('\n');

		DERInteger derInt = (DERInteger) seq.getObjectAt(1);
		sb.append(MessageFormat.format(localeUtil.getString("MsftCertTemplateMajorVer"), new Object[] { derInt.getValue().toString() }));
		sb.append('\n');

		if ((derInt = (DERInteger) seq.getObjectAt(2)) != null) {
			sb.append(MessageFormat.format(localeUtil.getString("MsftCertTemplateMinorVer"),new Object[] { derInt.getValue().toString() }));
			sb.append('\n');
		}

		return sb.toString();
	}

	/**
	 * Get Microsoft CA Version String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getMicrosoftCAVersionStringValue(byte[] bytes) throws IOException {
		int ver = ((DERInteger) toDERObject(bytes)).getValue().intValue();

		String cIx = String.valueOf(ver & 0xffff);
		String kIx = String.valueOf(ver >> 16);

		return MessageFormat.format(localeUtil.getString("MsftCaVersion"), new Object[] {cIx, kIx}) + '\n';
	}

	/**
	 * Get SMIME Capabilities String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private String getSmimeCapabilitiesStringValue(byte[] bytes) throws IOException {
		SMIMECapabilities caps = SMIMECapabilities.getInstance(toDERObject(bytes));

		String sParams = localeUtil.getString("SmimeParameters");

		StringBuffer sb = new StringBuffer();

		for (Iterator<SMIMECapability> i = caps.getCapabilities(null).iterator(); i.hasNext();) {
			SMIMECapability cap = (SMIMECapability) i.next();

			String sCapId = cap.getCapabilityID().getId();
			String sCap = getResource(sCapId, "UnrecognisedSmimeCapability");

			sb.append(MessageFormat.format(sCap, new Object[] { sCapId }));

			DEREncodable params;

			if ((params = cap.getParameters()) != null) {
				sb.append("\n\t");
				sb.append(MessageFormat.format(sParams,new Object[] { getObjectString(params) }));
			}

			sb.append('\n');
		}

		return sb.toString();
	}

	/**
	 * Get Authority Information Access String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getAuthorityInformationAccessStringValue(byte[] bytes) throws IOException {
		ASN1Sequence accDescs = (ASN1Sequence) toDERObject(bytes);

		StringBuffer sb = new StringBuffer();
		
		String aia = localeUtil.getString("AuthorityInformationAccess");

		for (int i = 0, adLen = accDescs.size(); i < adLen; i++) {
			ASN1Sequence accDesc = (ASN1Sequence) accDescs.getObjectAt(i);

			String accOid = ((DERObjectIdentifier) accDesc.getObjectAt(0)).getId();

			String accMeth = getResource(accOid, "UnrecognisedAccessMethod");

			String accLoc = getGeneralNameString((DERTaggedObject) accDesc.getObjectAt(1));
			
			sb.append(MessageFormat.format(aia, new Object[] {MessageFormat.format(accMeth, new Object[] { accOid }), accLoc }));
			sb.append('\n');
		}

		return sb.toString();
	}

	/**
	 * Get Novell Security Attributes String Value
	 * 
	 * @param bytes
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 */
	private String getNovellSecurityAttributesStringValue(byte[] bytes)
			throws IOException {
		ASN1Sequence attrs = (ASN1Sequence) toDERObject(bytes);
		StringBuffer sb = new StringBuffer();

		// "Novell Security Attribute(tm)"
		String sTM = ((DERString) attrs.getObjectAt(2)).getString();
		sb.append(sTM);
		sb.append('\n');

		// OCTET STRING of size 2, 1st is major version, 2nd is minor version
		byte[] bVer = ((DEROctetString) attrs.getObjectAt(0)).getOctets();
		sb.append("Major version: ").append(Byte.toString(bVer[0]));
		sb.append(", minor version: ").append(Byte.toString(bVer[1]));
		sb.append('\n');

		// Nonverified Subscriber Information
		boolean bNSI = ((DERBoolean) attrs.getObjectAt(1)).isTrue();
		sb.append("Nonverified Subscriber Information: ").append(bNSI);
		sb.append('\n');

		// URI reference
		String sUri = ((DERString) attrs.getObjectAt(3)).getString();
		sb.append("URI: ").append(sUri);
		sb.append('\n');

		// GLB Extensions (GLB ~ "Greatest Lower Bound")
		ASN1Sequence glbs = (ASN1Sequence) attrs.getObjectAt(4);
		sb.append("GLB extensions:");
		sb.append('\n');

		// Key quality
		ASN1Sequence keyq = (ASN1Sequence) ((ASN1TaggedObject) glbs.getObjectAt(0)).getObject();
		sb.append('\t').append(localeUtil.getString("NovellKeyQuality"));
		sb.append('\n').append(getNovellQualityAttr(keyq));

		// Crypto process quality
		ASN1Sequence cpq = (ASN1Sequence) ((ASN1TaggedObject) glbs.getObjectAt(1)).getObject();
		sb.append('\t').append(localeUtil.getString("NovellCryptoProcessQuality"));
		sb.append('\n').append(getNovellQualityAttr(cpq));

		ASN1Sequence cclass = (ASN1Sequence) ((ASN1TaggedObject) glbs.getObjectAt(2)).getObject();
		sb.append('\t');
		sb.append(localeUtil.getString("NovellCertClass"));
		sb.append('\n');

		sb.append("\t\t");
		String sv = ((DERInteger) cclass.getObjectAt(0)).getValue().toString();
		String sc = getResource("NovellCertClass." + sv,"UnregocnisedNovellCertClass");
		sb.append(MessageFormat.format(sc, new Object[] { sv }));
		sb.append('\n');

		boolean valid = true;
		if (cclass.size() > 1) {
			valid = ((DERBoolean) cclass.getObjectAt(1)).isTrue();
		}

		sb.append("\t\t");
		sb.append(localeUtil.getString("NovellCertClassValid." + valid));
		sb.append('\n');

		sb.append('\t');
		sb.append(localeUtil.getString("NovellEnterpriseID"));
		sb.append(' ').append(localeUtil.getString("DecodeNotImplemented"));
		sb.append('\n');

		return sb.toString();
	}

	/**
	 * Get Novell Quality Attr
	 * 
	 * @param seq
	 * 
	 * @return CharSequence
	 */
	private CharSequence getNovellQualityAttr(ASN1Sequence seq) {
		StringBuffer res = new StringBuffer();

		boolean enforceQuality = ((DERBoolean) seq.getObjectAt(0)).isTrue();
		res.append("\t\t").append(localeUtil.getString("NovellQualityEnforce"));
		res.append(' ').append(enforceQuality).append('\n');

		ASN1Sequence compusecQ = (ASN1Sequence) seq.getObjectAt(1);
		int clen = compusecQ.size();

		if (clen > 0) {
			res.append("\t\t");
			res.append(localeUtil.getString("NovellCompusecQuality"));
			res.append('\n');
		}

		for (int i = 0; i < clen; i++) {
			ASN1Sequence cqPair = (ASN1Sequence) compusecQ.getObjectAt(i);

			DERInteger tmp = (DERInteger) cqPair.getObjectAt(0);
			long type = tmp.getValue().longValue();
			String csecCriteria = getResource("NovellCompusecQuality." + type,
					"UnrecognisedNovellCompusecQuality");
			csecCriteria = MessageFormat.format(csecCriteria,
					new Object[] { tmp.getValue() });
			res.append("\t\t\t").append(csecCriteria);

			tmp = (DERInteger) cqPair.getObjectAt(1);
			String csecRating;
			if (type == 1L) { // TCSEC
				csecRating = getResource("TCSECRating." + tmp.getValue(),
						"UnrecognisedTCSECRating");
			} else {
				csecRating = localeUtil
						.getString("UnrecognisedNovellQualityRating");
			}
			csecRating = MessageFormat.format(csecRating, new Object[] { tmp
					.getValue() });
			res.append("\n\t\t\t\t").append(
					localeUtil.getString("NovellQualityRating"));
			res.append(' ').append(csecRating).append('\n');
		}

		res.append("\t\t").append(localeUtil.getString("NovellCryptoQuality"));
		res.append(' ').append(localeUtil.getString("DecodeNotImplemented"));
		res.append('\n');

		String ksqv = ((DERInteger) seq.getObjectAt(3)).getValue().toString();
		String ksq = getResource("NovellKeyStorageQuality." + ksqv,"UnrecognisedNovellKeyStorageQuality");
		res.append("\t\t").append(localeUtil.getString("NovellKeyStorageQuality"));
		res.append("\n\t\t\t").append(MessageFormat.format(ksq, new Object[] { ksqv }));
		res.append('\n');

		return res;
	}

	/**
	 * Get Netscape Certificate Type String Value
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private String getNetscapeCertificateTypeStringValue(byte[] bytes) throws IOException {
		byte[] derBytes = ((DERBitString) toDERObject(bytes)).getBytes();

		StringBuffer strBuff = new StringBuffer();

		if (derBytes.length != 0) {
			boolean[] b = new boolean[8];

			b[7] = (derBytes[0] & 0x80) == 0x80;
			b[6] = (derBytes[0] & 0x40) == 0x40;
			b[5] = (derBytes[0] & 0x20) == 0x20;
			b[4] = (derBytes[0] & 0x10) == 0x10;
			b[3] = (derBytes[0] & 0x8) == 0x8;
			b[2] = (derBytes[0] & 0x4) == 0x4;
			b[1] = (derBytes[0] & 0x2) == 0x2;
			b[0] = (derBytes[0] & 0x1) == 0x1;

			if (b[7]) {
				strBuff.append(localeUtil.getString("SslClientNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[6]) {
				strBuff.append(localeUtil.getString("SslServerNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[5]) {
				strBuff.append(localeUtil.getString("SmimeNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[4]) {
				strBuff.append(localeUtil.getString("ObjectSigningNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[2]) {
				strBuff.append(localeUtil.getString("SslCaNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[1]) {
				strBuff.append(localeUtil.getString("SmimeCaNetscapeCertificateType"));
				strBuff.append('\n');
			}

			if (b[0]) {
				strBuff.append(localeUtil.getString("ObjectSigningCaNetscapeCertificateType"));
				strBuff.append('\n');
			}
		}

		return strBuff.toString();
	}

	/**
	 * Get Non Netscape Certificate Type String Value
	 * 
	 * @param bValue
	 * @return
	 * @throws IOException
	 */
	private String getNonNetscapeCertificateTypeStringValue(byte[] bytes) throws IOException {
		return ((DERIA5String) toDERObject(bytes)).getString() + '\n';
	}

	/**
	 * Get DN BDUNS Number String Value
	 * 
	 * @param bValue
	 * @return
	 * @throws IOException
	 */
	private String getDnBDUNSNumberStringValue(byte[] bytes) throws IOException {
		return ((DERIA5String) toDERObject(bytes)).getString() + '\n';
	}

	/**
	 * Get Crl Distribution Points String Value
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private String getCrlDistributionPointsStringValue(byte[] bytes) throws IOException {
		CRLDistPoint dps = CRLDistPoint.getInstance(toDERObject(bytes));
		DistributionPoint[] points = dps.getDistributionPoints();

		StringBuffer sb = new StringBuffer();

		for (int i = 0, len = points.length; i < len; i++) {
			DistributionPoint     point = points[i];
			DistributionPointName dpn;

			if ((dpn = point.getDistributionPoint()) != null) {
				ASN1TaggedObject tagObj = (ASN1TaggedObject) dpn.toASN1Object();
				
				switch (tagObj.getTagNo()) {
				case DistributionPointName.FULL_NAME:
					sb.append(localeUtil.getString("CrlDistributionPoint.0.0"));
					sb.append('\n');
					ASN1Sequence seq = (ASN1Sequence) tagObj.getObject();
					
					for (int j = 0, nLen = seq.size(); j < nLen; j++) {
						sb.append('\t');
						sb.append(getGeneralNameString((DERTaggedObject) seq
								.getObjectAt(j)));
						sb.append('\n');
					}
					break;
				case DistributionPointName.NAME_RELATIVE_TO_CRL_ISSUER:
					sb.append(localeUtil.getString("CrlDistributionPoint.0.1"));

					sb.append('\t');
					sb.append(tagObj.getObject());
					sb.append('\n');
					break;
				default:
					break;
				}
			}

			ReasonFlags flags;

			if ((flags = point.getReasons()) != null) {
				sb.append(localeUtil.getString("CrlDistributionPoint.1"));
				sb.append('\t');
				sb.append(flags);
				sb.append('\n');
			}

			GeneralNames issuer;

			if ((issuer = point.getCRLIssuer()) != null) {
				sb.append(localeUtil.getString("CrlDistributionPoint.2"));
				sb.append('\n');
				ASN1Sequence seq = (ASN1Sequence) issuer.getDERObject();

				for (int j = 0, iLen = seq.size(); j < iLen; j++) {
					sb.append('\t');
					sb.append(getGeneralNameString((DERTaggedObject) seq
							.getObjectAt(j)));
					sb.append('\n');
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Get Certificate Policies String Value
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private String getCertificatePoliciesStringValue(byte[] bytes)
			throws IOException {
		ASN1Sequence pSeq = (ASN1Sequence) toDERObject(bytes);
		StringBuffer sb = new StringBuffer();

		for (int i = 0, len = pSeq.size(); i < len; i++) {

			PolicyInformation pi = PolicyInformation.getInstance(pSeq
					.getObjectAt(i));

			sb.append(MessageFormat.format(localeUtil.getString("PolicyIdentifier"), new Object[] { pi.getPolicyIdentifier().getId() }));
			sb.append('\n');

			ASN1Sequence pQuals;

			if ((pQuals = pi.getPolicyQualifiers()) != null) {
				for (int j = 0, plen = pQuals.size(); j < plen; j++) {

					ASN1Sequence pqi = (ASN1Sequence) pQuals.getObjectAt(j);
					String pqId = ((DERObjectIdentifier) pqi.getObjectAt(0))
							.getId();

					sb.append('\t');
					sb.append(MessageFormat.format(getResource(pqId,"UnrecognisedPolicyQualifier"),new Object[] { pqId }));
					sb.append('\n');

					if (pQuals.size() > 0) {

						DEREncodable d = pqi.getObjectAt(1);

						if (pqId.equals("1.3.6.1.5.5.7.2.1")) {
							// cPSuri
							sb.append("\t\t");
							sb.append(MessageFormat.format(localeUtil.getString("CpsUri"),new Object[] { ((DERString) d).getString() }));
							sb.append('\n');
						} 
						else if (pqId.equals("1.3.6.1.5.5.7.2.2")) {
							ASN1Sequence un = (ASN1Sequence) d;

							for (int k = 0, dlen = un.size(); k < dlen; k++) {
								DEREncodable de = un.getObjectAt(k);

								if (de instanceof DERString) {
									// explicitText
									sb.append("\t\t");
									sb.append(localeUtil
											.getString("ExplicitText"));
									sb.append("\n\t\t\t");
									sb.append(getObjectString(de));
									sb.append('\n');
								} else if (de instanceof ASN1Sequence) {
									ASN1Sequence nr = (ASN1Sequence) de;
									
									String orgstr = getObjectString(nr.getObjectAt(0));
									ASN1Sequence nrs = (ASN1Sequence) nr.getObjectAt(1);
									
									StringBuffer nrstr = new StringBuffer();
									for (int m = 0, nlen = nrs.size(); m < nlen; m++) {
										nrstr.append(getObjectString(nrs.getObjectAt(m)));
										if (m != nlen - 1) {
											nrstr.append(", ");
										}
									}
									sb.append("\t\t");
									sb
											.append(localeUtil
													.getString("NoticeRef"));
									sb.append("\n\t\t\t");
									sb.append(MessageFormat.format(localeUtil.getString("NoticeRefOrganization"),new Object[] { orgstr }));
									sb.append("\n\t\t\t");
									sb.append(MessageFormat.format(localeUtil.getString("NoticeRefNumber"),new Object[] { nrstr }));
									sb.append('\n');
								}
							}
						} else {
							sb.append("\t\t");
							sb.append(getObjectString(d));
							sb.append('\n');
						}
					}
				}
			}

			if (i != len) {
				sb.append('\n');
			}
		}

		return sb.toString();
	}

	/**
	 * Get General Name String
	 * 
	 * @param generalName
	 * 
	 * @return String
	 */
	private String getGeneralNameString(DERTaggedObject generalName) {
		StringBuffer strBuff = new StringBuffer();

		switch (generalName.getTagNo()) {

		case 0:
			ASN1Sequence other = (ASN1Sequence) generalName.getObject();
			String sOid = ((DERObjectIdentifier) other.getObjectAt(0)).getId();
			String sVal = getObjectString(other.getObjectAt(1));
			strBuff.append(MessageFormat.format(localeUtil.getString("OtherGeneralName"),new Object[] { sOid, sVal }));
			break;

		case 1:
			DEROctetString rfc822 = (DEROctetString) generalName.getObject();
			String sRfc822 = new String(rfc822.getOctets());
			strBuff.append(MessageFormat.format(localeUtil.getString("Rfc822GeneralName"), new Object[] { sRfc822 }));
			break;

		case 2: // DNS Name
			DEROctetString dns = (DEROctetString) generalName.getObject();
			String sDns = new String(dns.getOctets());
			strBuff.append(MessageFormat.format(localeUtil.getString("DnsGeneralName"), new Object[] { sDns }));
			break;

		case 4: // Directory Name
			ASN1Sequence directory = (ASN1Sequence) generalName.getObject();
			X509Name name = new X509Name(directory);
			strBuff.append(MessageFormat.format(localeUtil.getString("DirectoryGeneralName"), new Object[] { name.toString() }));
			break;

		case 6: // URI
			DEROctetString uri = (DEROctetString) generalName.getObject();
			String sUri = new String(uri.getOctets());
			strBuff.append(MessageFormat.format(localeUtil.getString("UriGeneralName"), new Object[] { sUri }));
			break;

		case 7: // IP Address
			DEROctetString ipAddress = (DEROctetString) generalName.getObject();

			byte[] bIpAddress = ipAddress.getOctets();

			// Output the IP Address components one at a time separated by dots
			StringBuffer sbIpAddress = new StringBuffer();

			for (int iCnt = 0, bl = bIpAddress.length; iCnt < bl; iCnt++) {
				// Convert from (possibly negative) byte to positive int
				sbIpAddress.append((int) bIpAddress[iCnt] & 0xFF);
				if ((iCnt + 1) < bIpAddress.length) {
					sbIpAddress.append('.');
				}
			}

			strBuff.append(MessageFormat.format(localeUtil
					.getString("IpAddressGeneralName"),new Object[] { sbIpAddress.toString() }));
			break;

		case 8:
			DEROctetString registeredId = (DEROctetString) generalName.getObject();

			byte[] bRegisteredId = registeredId.getOctets();

			// Output the components one at a time separated by dots
			StringBuffer sbRegisteredId = new StringBuffer();

			for (int iCnt = 0; iCnt < bRegisteredId.length; iCnt++) {
				byte b = bRegisteredId[iCnt];
				// Convert from (possibly negative) byte to positive int
				sbRegisteredId.append((int) b & 0xFF);
				if ((iCnt + 1) < bRegisteredId.length) {
					sbRegisteredId.append('.');
				}
			}

			strBuff.append(MessageFormat.format(localeUtil
					.getString("RegisteredIdGeneralName"),
					new Object[] { sbRegisteredId.toString() }));
			break;

		default:
			strBuff.append(MessageFormat.format(localeUtil.getString("UnsupportedGeneralNameType"), new Object[] {"" + generalName.getTagNo() }));
			break;
		}

		return strBuff.toString();
	}

	/**
	 * Format Generalized Time
	 * 
	 * @param time
	 * 
	 * @return String
	 * 
	 * @throws ParseException
	 */
	private String formatGeneralizedTime(DERGeneralizedTime time)
			throws ParseException {
		// Get generalized time as a string
		String sTime = time.getTime();

		// Setup date formatter with expected date format of string
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");

		// Create date object from string using formatter
		Date date = dateFormat.parse(sTime);

		// Re-format date - include timezone
		sTime = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.LONG).format(date);

		return sTime;
	}

	/**
	 * Get Object String
	 * 
	 * @param obj
	 * 
	 * @return String
	 */
	private static String getObjectString(Object obj) {
		if (obj instanceof DERString) {
			return ((DERString) obj).getString();
		} 
		else if (obj instanceof DERInteger) {
			return convertToHexString((DERInteger) obj);
		} 
		else if (obj instanceof byte[]) {
			return convertToHexString((byte[]) obj);
		} 
		else if (obj instanceof ASN1TaggedObject) {
			ASN1TaggedObject tagObj = (ASN1TaggedObject) obj;

			return "[" + tagObj.getTagNo() + "] " + getObjectString(tagObj.getObject());
		} else {
			String hex = null;

			try {
				Method method = obj.getClass().getMethod("getOctets", null);
				
				hex = convertToHexString((byte[]) method.invoke(obj, null));
			} catch (Exception e) {
			}

			if (hex == null && obj != null) {
				hex = obj.toString();
			}

			return hex;
		}
	}

	/**
	 * Gets a resource string, with fallback.
	 * 
	 * @param key Key
	 * @param fallback Fallback key
	 * 
	 * @return String
	 */
	private String getResource(String key, String fallback) {
		try {
			return localeUtil.getString(key);
		} catch (Exception e) {
			return localeUtil.getString(fallback);
		}
	}
	
	/**
	 * Gets a DER object from the given byte array.
	 * 
	 * @param bytes bytes
	 * 
	 * @return DERObject
	 * 
	 * @throws IOException if a conversion error occurs
	 */
	private static DERObject toDERObject(byte[] bytes) throws IOException {
		ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(bytes));
		
		try {
			return in.readObject();
		} 
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
