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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.security.Key;
import java.security.cert.Certificate;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.jlocksmith.util.LocaleUtil;

/**
 * Key Info Panel
 * 
 * @author Derek Helbert
 */
public class KeyInfoPanel extends JPanel {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5049179811914679295L;

	/** Header Panel */
	private JPanel headerPanel = new JPanel();

	/** Chain Panel */
	private JPanel chainPanel = new JPanel();

	/** Locale Util */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/**
	 * Constructor
	 * 
	 * @param c Certificate
	 */
	public KeyInfoPanel(Key key, Certificate[] chain, Frame f) {
		super();
		init(key, chain, f);
		
		if( chain != null ) {
			setPreferredSize(new Dimension(600, 400));
		}
	}

	/**
	 * Init
	 * 
	 * @param key Key
	 * @param chain Certificate Chain
	 * @param f Frame
	 */
	private void init(Key key, Certificate[] chain, Frame f) {
		setLayout(new BorderLayout(5, 5));

		if (chain != null) {
			// Setup table panel
			chainPanel.setLayout(new BorderLayout(5, 5));
			chainPanel.add(new JScrollPane());
			chainPanel.setBorder(new TitledBorder(localeUtil
					.getString("certificatechain")));
			chainPanel.add(BorderLayout.CENTER,
					new CertificateChainPanel(chain,f));
			add(BorderLayout.CENTER, chainPanel);
		}

		// Setup Header Panel
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		headerPanel.add(new JLabel(localeUtil.getString("algorithm") + ":"));
		JTextField field2 = new JTextField(key.getAlgorithm(), 5);
		field2.setEnabled(false);
		headerPanel.add(field2);
		headerPanel.add(new JLabel(localeUtil.getString("format") + ":"));
		JTextField field3 = new JTextField(key.getFormat(), 10);
		field3.setEnabled(false);
		headerPanel.add(field3);
		headerPanel.setBorder(new TitledBorder(localeUtil.getString("key")));

		field2.setDisabledTextColor(field2.getForeground());
		field3.setDisabledTextColor(field3.getForeground());

		add(BorderLayout.NORTH, headerPanel);
	}
}
