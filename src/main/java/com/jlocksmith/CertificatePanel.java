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

import java.awt.GridLayout;
import java.security.KeyPair;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.jlocksmith.util.KeyUtil;
import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.SignatureUtil;

/**
 * New Certificate Panel
 * 
 * @author Derek Helbert
 */
public class CertificatePanel extends JPanel implements ValidatedForm {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6984584501986720011L;

	/** Validity Field */
	private JTextField valField = new JTextField("0", 3);

	/** Common Name Field */
	private JTextField cnField = new JTextField(20);

	/** Organization Unit Field */
	private JTextField ouField = new JTextField(20);

	/** Organization Name Field */
	private JTextField onField = new JTextField(20);

	/** Locality Field */
	private JTextField locField = new JTextField(20);

	/** State Field */
	private JTextField stField = new JTextField(20);

	/** Country Field */
	private JTextField coField = new JTextField(2);

	/** Email Field */
	private JTextField emailField = new JTextField(20);

	/** Locale Util */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Alg Combo */
	private JComboBox sigCombo = new JComboBox();

	/**
	 * Constructor
	 * 
	 * @param keyPair KeyPair
	 */
	public CertificatePanel(KeyPair keyPair) {
		super();
		init(keyPair);
	}

	/**
	 * Init
	 * 
	 * @param keyPair KeyPair
	 */
	private void init(KeyPair keyPair) {
		setLayout(new GridLayout(9, 2, 5, 5));

		String[] algs = null;

		if (KeyUtil.DSA.equals(keyPair.getPrivate().getAlgorithm())) {
			algs = SignatureUtil.getDsaSignatureTypes();
		} else {
			algs = SignatureUtil.getRsaSignatureTypes();
		}

		for (int i = 0; i < algs.length; i++) {
			sigCombo.addItem(algs[i]);
		}

		add(new JLabel(localeUtil.getString("signature")));
		add(sigCombo);
		add(new JLabel(localeUtil.getString("validity") + "("
				+ localeUtil.getString("days") + "):"));
		add(valField);
		add(new JLabel(localeUtil.getString("commonname") + "(CN):"));
		add(cnField);
		add(new JLabel(localeUtil.getString("organizationunit") + "(OU):"));
		add(ouField);
		add(new JLabel(localeUtil.getString("organizationname") + "(ON):"));
		add(onField);
		add(new JLabel(localeUtil.getString("locality") + "(L):"));
		add(locField);
		add(new JLabel(localeUtil.getString("state") + "(ST):"));
		add(stField);
		add(new JLabel(localeUtil.getString("country") + "(C):"));
		add(coField);
		add(new JLabel(localeUtil.getString("email") + "(E):"));
		add(emailField);

		setBorder(new TitledBorder(localeUtil.getString("properties")));
	}

	/**
	 * Get Validity
	 * 
	 * @return String
	 */
	public String getValidity() {
		return valField.getText();
	}

	/**
	 * Get Common Name
	 * 
	 * @return String
	 */
	public String getCommonName() {
		return cnField.getText();
	}

	/**
	 * Get Org Unit
	 * 
	 * @return String
	 */
	public String getOrganizationUnit() {
		return ouField.getText();
	}

	/**
	 * Get Org Name
	 * 
	 * @return String
	 */
	public String getOrganizationName() {
		return onField.getText();
	}

	public String getLocality() {
		return locField.getText();
	}

	/**
	 * Get State
	 * 
	 * @return String
	 */
	public String getState() {
		return stField.getText();
	}

	/**
	 * Get Country
	 * 
	 * @return String
	 */
	public String getCountry() {
		return coField.getText();
	}

	/**
	 * Get Email
	 * 
	 * @return String
	 */
	public String getEmail() {
		return emailField.getText();
	}

	/**
	 * Get Sig
	 * 
	 * @return
	 */
	public String getSigAlg() {
		return sigCombo.getSelectedItem().toString();
	}

	/**
	 * Is Form Valid
	 */
	public boolean isFormValid() {
		try {
			Integer.parseInt(valField.getText());
		} catch (Exception err) {
			return false;
		}

		if (cnField.getText().length() <= 0) {
			return false;
		}

		if (ouField.getText().length() <= 0) {
			return false;
		}

		if (onField.getText().length() <= 0) {
			return false;
		}

		if (locField.getText().length() <= 0) {
			return false;
		}

		if (stField.getText().length() <= 0) {
			return false;
		}

		if (coField.getText().length() <= 0) {
			return false;
		}

		if (emailField.getText().length() <= 0) {
			return false;
		}

		return true;
	}
}
