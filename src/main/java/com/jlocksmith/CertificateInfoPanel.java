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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.jlocksmith.util.ExtensionUtil;
import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.UiUtil;

/**
 * Certificate Info Panel
 * 
 * @author Derek Helbert
 */
public class CertificateInfoPanel extends JPanel implements ActionListener, MouseListener, KeyListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5197807286220746120L;

	/** Certificate */
	private Certificate cert = null;

	/** Header Panel */
	private JPanel headerPanel = new JPanel();

	/** Table Panel */
	private JPanel tablePanel = new JPanel();

	/** Ext Panel */
	private JPanel extPanel = new JPanel();

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Table */
	private JTable table = null;

	/** Ext Button */
	private JButton extButton = new JButton();

	/** Text Area */
	private JTextArea descArea = new JTextArea("", 6, 80);

	/** Model */
	private CertExtensionModel ceModel = null;

	/** Ext Table */
	private JTable extTable = null;

	/** Parent */
	private Frame parent = null;

	/**
	 * Constructor
	 * 
	 * @param c Certificate
	 */
	public CertificateInfoPanel(Certificate c, Frame f) {
		super();
		this.cert = c;
		this.parent = f;
		init();
		setPreferredSize(new Dimension(600, 400));
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout(5, 5));

		// Setup table
		Map<Object,Object> map = new HashMap<Object,Object>();

		if (cert instanceof X509Certificate) {
			X509Certificate temp = (X509Certificate) cert;

			map.put(localeUtil.getString("version"),      temp.getVersion() + "");
			map.put(localeUtil.getString("validfrom"),    temp.getNotBefore().toString());
			map.put(localeUtil.getString("validto"),      temp.getNotAfter().toString());
			map.put(localeUtil.getString("issuer"),       temp.getIssuerDN().getName());
			map.put(localeUtil.getString("subject"),      temp.getSubjectDN().getName());
			map.put(localeUtil.getString("signature"),    temp.getSigAlgName());
			map.put(localeUtil.getString("serialnumber"), temp.getSerialNumber().toString());
		}

		MapTableModel model = new MapTableModel(map);
		model.sort(0, true);
		table = new JTable(model);
		UiUtil.autoSizeTable(table);

		// Setup table panel
		tablePanel.setLayout(new BorderLayout(5, 5));
		tablePanel.add(new JScrollPane(table));
		tablePanel.setBorder(new TitledBorder(localeUtil.getString("properties")));

		// Setup Header Panel
		headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		JTextField field1 = new JTextField(cert.getType(), 10);
		field1.setEnabled(false);
		headerPanel.add(new JLabel(localeUtil.getString("type") + ":"));
		headerPanel.add(field1);
		headerPanel.add(new JLabel(localeUtil.getString("algorithm") + ":"));
		JTextField field2 = new JTextField(cert.getPublicKey().getAlgorithm(),5);
		field2.setEnabled(false);
		headerPanel.add(field2);
		headerPanel.add(new JLabel(localeUtil.getString("format") + ":"));
		JTextField field3 = new JTextField(cert.getPublicKey().getFormat(), 10);
		field3.setEnabled(false);
		headerPanel.add(field3);
		extButton.addActionListener(this);
		extButton.setEnabled(false);
		extButton.setText(localeUtil.getString("extensions"));
		headerPanel.add(extButton);
		headerPanel.setBorder(new TitledBorder(localeUtil.getString("certificate")));

		field1.setDisabledTextColor(field1.getForeground());
		field2.setDisabledTextColor(field2.getForeground());
		field3.setDisabledTextColor(field3.getForeground());

		add(BorderLayout.CENTER, tablePanel);
		add(BorderLayout.NORTH, headerPanel);

		if (cert instanceof X509Certificate) {
			ceModel = new CertExtensionModel((X509Certificate) cert);

			extTable = new JTable(ceModel);
			extTable.addMouseListener(this);
			extTable.addKeyListener(this);
			extTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			UiUtil.autoSizeTable(extTable);

			if (ceModel.getRowCount() > 0) {
				extButton.setEnabled(true);
			}

			descArea.setEditable(false);

			// Setup table panel
			extPanel.setLayout(new BorderLayout(5, 5));
			extPanel.add(BorderLayout.CENTER, new JScrollPane(extTable));
			extPanel.add(BorderLayout.SOUTH, new JScrollPane(descArea));
			extPanel.setBorder(new TitledBorder(localeUtil.getString("properties")));
			extPanel.setPreferredSize(new Dimension(500, 350));
		}
	}

	/**
	 * Action Performed
	 * 
	 * @param e Action Event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == extButton) {
			extAction();
		}
	}

	/**
	 * Extension Action
	 * 
	 */
	private void extAction() {
		UiUtil.showInfo(parent, extPanel, localeUtil.getString("extensions"));
	}

	/**
	 * Key Pressed
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Key Released
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Key Typed
	 * 
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Mouse Clicked
	 * 
	 * @param e
	 */
	public void mouseClicked(MouseEvent e) {
		if (extTable.getSelectedRowCount() > 0) {
			String oid = ceModel.getOID(extTable.getSelectedRow());
			ExtensionUtil util = new ExtensionUtil();

			try {
				descArea.setText(util.getStringValue(((X509Certificate) cert)
						.getExtensionValue(oid), oid));
			} catch (Exception err) {
				err.printStackTrace();
			}

		} else {
			descArea.setText("");
		}
	}

	/**
	 * Mouse Entered
	 * 
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Mouse Exited
	 * 
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Mouse Pressed
	 * 
	 * @param e
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Mouse Released
	 * 
	 * @param e
	 */
	public void mouseReleased(MouseEvent e) {
	}

}
