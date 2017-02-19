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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.jlocksmith.util.CustomFileFilter;
import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.SecurityUtil;
import com.jlocksmith.util.UiUtil;

/**
 * Preferences Panel
 * 
 * @author Derek Helbert
 */
public class PreferencesPanel extends JPanel implements ValidatedForm, ActionListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5492498856240690294L;

	/** Security Utility */
	SecurityUtil securityUtil = SecurityUtil.getInstance();
	
	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();
	
	/** CA Certificates Field */
	private JCheckBox useCaCerts = new JCheckBox();

	/** Path Field */
	private JTextField filePathField = new JTextField(40);

	/** Password Field */
	private JTextField passwPathField = new JTextField(20);
	
	/** Size Label */
	private JButton fileButton;

	/**
	 * Constructor
	 */
	public PreferencesPanel() {
		super();
		init();
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		Preferences pref = MainFrame.getPreferences();
		filePathField.setText(pref.getCaCertsKeystorePath());
		passwPathField.setText(pref.getCaCertsKeystorePassword());
		useCaCerts.setSelected(pref.isUseCaCertsKeystore());		

		// Setup File Button
		fileButton = new JButton( localeUtil.getString("file") );
		fileButton.addActionListener(this);
		fileButton.setText( localeUtil.getString("file") + "..." );
		
		JLabel label1 = new JLabel(localeUtil.getString("usecakeystore"));
		JLabel label2 = new JLabel(localeUtil.getString("password"));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

		UiUtil.setConstraints(gbc, 50, 50, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(label1,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 1, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(useCaCerts,gbc);
		
		UiUtil.setConstraints(gbc, 50, 50, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(label2,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(passwPathField,gbc);
		
		UiUtil.setConstraints(gbc, 50, 50, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
		add(fileButton,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 1, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(filePathField,gbc);
		
		setBorder(new EtchedBorder());
	}

	/**
	 * Get File Path
	 * 
	 * @return String
	 */
	public String getFilePath() {
		return filePathField.getText();
	}

	/**
	 * Get Password
	 * 
	 * @return String
	 */
	public String getPassword() {
		return passwPathField.getText();
	}
	
	/**
	 * Get Use CA Certs
	 * 
	 * @return boolean
	 */
	public boolean getUseCaCerts() {
		return useCaCerts.isSelected();
	}

	/**
	 * Is Form Valid
	 */
	public boolean isFormValid() {		
		if( filePathField.getText().length() <= 0 ) {
			return false;
		}
		
		return true;
	}

	/**
	 * Action Performed
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		File file = UiUtil.getSelectedFile(this, JFileChooser.OPEN_DIALOG,
				new CustomFileFilter(securityUtil.getFileKeystoreMap(),
						localeUtil.getString("keystore")));

		if (file == null) {
			return;
		}
		else {
			filePathField.setText(file.getPath());
		}
	}
	

}
