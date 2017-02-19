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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.UiUtil;

/**
 * Host Port Panel
 * 
 * @author Derek Helbert
 */
public class HostPortPanel extends JPanel implements ValidatedForm {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4042006561894306217L;

	/** Size Field */
	private JTextField portField = new JTextField("443",5);

	/** Locale Util */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Host Field */
	private JTextField hostField = new JTextField(35);

	/** Size Label */
	private JLabel portLabel = new JLabel(localeUtil.getString("port"));

	/**
	 * Constructor
	 */
	public HostPortPanel() {
		super();
		init();
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel label = new JLabel(localeUtil.getString("host"));

		UiUtil.setConstraints(gbc, 50, 50, 0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(label,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 1, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(hostField,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(portLabel,gbc);
		UiUtil.setConstraints(gbc, 50, 50, 1, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
		add(portField,gbc);

		setBorder(new EtchedBorder());
	}

	/**
	 * Get Port
	 * 
	 * @return int
	 */
	public int getPort() {
		return Integer.parseInt(portField.getText());
	}

	/**
	 * Get Host
	 * 
	 * @return String
	 */
	public String getHost() {
		return hostField.getText();
	}

	/**
	 * Is Form Valid
	 */
	public boolean isFormValid() {
		try {
			Integer.parseInt(portField.getText());
		}
		catch( Exception err ) {
			return false;
		}
		
		if( hostField.getText().length() <= 0 ) {
			return false;
		}
		
		return true;
	}
}
