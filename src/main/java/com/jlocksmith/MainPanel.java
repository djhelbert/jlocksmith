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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.jlocksmith.util.CertificateUtil;
import com.jlocksmith.util.CustomFileFilter;
import com.jlocksmith.util.KeyUtil;
import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.SecurityUtil;
import com.jlocksmith.util.UiUtil;

/**
 * Main Panel
 * 
 * @author Derek Helbert
 */
public class MainPanel extends JPanel implements ActionListener, MouseListener, KeystoreListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3866977566749468184L;

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** UI Utility */
	private UiUtil uiUtil = new UiUtil();

	/** Table */
	private JTable table = null;

	/** Table Panel */
	private JPanel storePanel = new JPanel();

	/** Table Panel */
	private JPanel tablePanel = new JPanel();

	/** Status Panel */
	private JPanel statusPanel = new JPanel();

	/** Delete Button */
	private JButton deleteButton = new JButton();

	/** Export Button */
	private JButton exportButton = new JButton();

	/** Info Button */
	private JButton infoButton = new JButton();

	/** Properties Button */
	private JButton propButton = new JButton();

	/** Delete Button */
	private JMenuItem deleteItem;

	/** Export Button */
	private JMenuItem exportItem;

	/** Info Button */
	private JMenuItem infoItem;

	/** Import Key Item */
	private JMenuItem importKeyItem;

	/** Import Cert Item */
	private JMenuItem importCertItem;

	/** Prop Item */
	private JMenuItem propItem = null;

	/** Password Item */
	private JMenuItem passwordItem = null;

	/** New CSR Item */
	private JMenuItem newCsrItem = null;

	/** Import CSR Item */
	private JMenuItem importCsrItem = null;

	/** Copy Key Item */
	private JMenuItem copyKeyItem = null;

	/** Rename Entry Item */
	private JMenuItem renameEntryItem = null;

	/** Label Button */
	private JTextField typeField = new JTextField(10);

	/** Label Button */
	private JTextField providerField = new JTextField(20);

	/** Label Button */
	private JTextField versionField = new JTextField(10);

	/** Status Label */
	private JLabel statusLabel = new JLabel("0 "
			+ localeUtil.getString("entries"));

	/** Keystore Manager */
	KeystoreManager manager = KeystoreManager.getInstance();

	/** Model */
	private AliasTableModel model = new AliasTableModel();

	/** Parent */
	private Frame parent = null;

	/** Security Util */
	private SecurityUtil securityUtil = SecurityUtil.getInstance();

	/**
	 * Constructor
	 * 
	 */
	public MainPanel(Frame frame) {
		super();
		parent = frame;
		init();
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		// Set Layout
		setLayout(new BorderLayout(5, 5));

		// Add listener
		manager.addKeystoreListener(this);

		// Table Panel
		model = new AliasTableModel();
		table = new JTable(model);
		tablePanel = new JPanel(new FlowLayout());
		tablePanel.setLayout(new BorderLayout(5, 5));
		tablePanel.add(BorderLayout.CENTER, new JScrollPane(table));
		tablePanel.setBorder(new TitledBorder(localeUtil.getString("entries")));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(this);
		table.addKeyListener(this);

		// Store Panel
		storePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		storePanel.add(new JLabel(localeUtil.getString("type") + ":"));
		storePanel.add(typeField);
		storePanel.add(new JLabel(localeUtil.getString("provider") + ":"));
		storePanel.add(providerField);
		storePanel.add(new JLabel(localeUtil.getString("version") + ":"));
		storePanel.add(versionField);
		storePanel
				.setBorder(new TitledBorder(localeUtil.getString("keystore")));
		propButton.setText(localeUtil.getString("examine.properties"));
		propButton.setIcon(uiUtil.getImageIcon("search.gif"));
		storePanel.add(propButton);
		typeField.setEnabled(false);
		providerField.setEnabled(false);
		versionField.setEnabled(false);
		propButton.setEnabled(false);
		propButton.addActionListener(this);
		typeField.setDisabledTextColor(typeField.getForeground());
		providerField.setDisabledTextColor(providerField.getForeground());
		versionField.setDisabledTextColor(versionField.getForeground());

		// Status Panel
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		statusPanel.add(statusLabel);

		// Add Panels
		add(BorderLayout.NORTH, storePanel);
		add(BorderLayout.CENTER, tablePanel);
		add(BorderLayout.SOUTH, statusPanel);
	}

	/**
	 * actionPerformed
	 * 
	 * @param e Action Event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == propButton) {
			propAction();
		}
	}

	/**
	 * Establish Trust
	 * 
	 * @param cert Certificate
	 * 
	 * @return X509Certificate[]
	 * 
	 * @throws KeyStoreException
	 * @throws CertificateException
	 * @throws IOException
	 */
	private X509Certificate[] establishTrust(X509Certificate cert)
			throws KeyStoreException, CertificateException, IOException,
			NoSuchAlgorithmException {
		KeyStore[] stores = null;

		if (MainFrame.getPreferences().isUseCaCertsKeystore()) {
			stores = new KeyStore[2];
			stores[0] = manager.getKeyStore();
			stores[1] = KeyUtil.loadKeyStore(MainFrame.getPreferences()
					.getCaCertsKeystorePath(), MainFrame.getPreferences()
					.getCaCertsKeystorePassword());
		} else {
			stores = new KeyStore[1];
			stores[0] = manager.getKeyStore();
		}

		return CertificateUtil.establishTrust(stores, cert);
	}

	/**
	 * Copy Key Action
	 * 
	 */
	public void copyKeyAction() {
		if (table.getSelectedRowCount() >= 1) {
			String alias = model.getAlias(table.getSelectedRow());

			String password = UiUtil.getPassword(parent, localeUtil
					.getString("password"), localeUtil.getString("copykey"));

			if (password == null) {
				return;
			}

			String newalias = UiUtil.getPassword(parent, localeUtil
					.getString("alias"), localeUtil.getString("copykey"));

			if (newalias == null) {
				return;
			}

			try {
				manager.cloneKey(alias, newalias, password);
			} catch (Exception err) {
				UiUtil.showError(parent, err.getMessage(), localeUtil
						.getString("error"));
			}
		}
	}

	/**
	 * Prop Action
	 * 
	 */
	public void propAction() {
		MapTableModel model = new MapTableModel(manager.getProvider());
		model.sort(0, true);
		JPanel panel = new JPanel();
		JTable table = new JTable(model);
		JScrollPane spane = new JScrollPane(table);
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(700, 450));
		panel.setBorder(new TitledBorder(localeUtil.getString("properties")));
		panel.add(BorderLayout.CENTER, spane);
		UiUtil.autoSizeTable(table);
		UiUtil.showInfo(parent, panel, localeUtil.getString("provider") + " - "
				+ manager.getProvider().getName() + " "
				+ manager.getProvider().getVersion());
	}

	/**
	 * Disable All
	 * 
	 */
	private void disableAll() {
		deleteButton.setEnabled(false);
		exportButton.setEnabled(false);
		infoButton.setEnabled(false);
		deleteItem.setEnabled(false);
		renameEntryItem.setEnabled(false);
		newCsrItem.setEnabled(false);
		importCsrItem.setEnabled(false);
		copyKeyItem.setEnabled(false);
		exportItem.setEnabled(false);
		infoItem.setEnabled(false);
	}

	private void enableButtonsItems() {
		deleteButton.setEnabled(true);
		exportButton.setEnabled(true);
		infoButton.setEnabled(true);
		deleteItem.setEnabled(true);
		renameEntryItem.setEnabled(true);
		exportItem.setEnabled(true);
		infoItem.setEnabled(true);

		if (manager.isKeyPairEntry(model.getAlias(table.getSelectedRow()))) {
			newCsrItem.setEnabled(true);
			importCsrItem.setEnabled(true);
			copyKeyItem.setEnabled(true);
		} else {
			newCsrItem.setEnabled(false);
			importCsrItem.setEnabled(false);
			copyKeyItem.setEnabled(false);
		}
	}

	/**
	 * Mouse Clicked
	 * 
	 * @param e Mouse Event
	 */
	public void mouseClicked(MouseEvent e) {
		if (table.getSelectedRowCount() <= 0) {
			disableAll();
		} else {
			enableButtonsItems();
		}

		if (e.getClickCount() >= 2) {
			infoAction();
		}
	}

	/**
	 * Rename Entry Action
	 * 
	 */
	public void renameEntryAction() {
		int row = table.getSelectedRow();

		if (row != -1) {
			String alias = model.getAlias(row);
			String newAlias = UiUtil.getPassword(parent, localeUtil
					.getString("alias"), localeUtil.getString("renameentry"));

			if (newAlias == null) {
				return;
			} else {
				String password = null;

				try {
					if (manager.isKeyEntry(alias)) {
						password = UiUtil.getPassword(parent, localeUtil
								.getString("password"), localeUtil
								.getString("key"));

						manager.renameKeyEntry(alias, newAlias, password);
					} else {
						manager.renameCertificateEntry(alias, newAlias);
					}
				} catch (Exception err) {
					err.printStackTrace();
				}
			}
		}
	}

	/**
	 * Certificate Request Action
	 * 
	 */
	public void newCsrAction() {
		int row = table.getSelectedRow();

		if (row != -1) {
			String alias = model.getAlias(row);
			String password = UiUtil.getPassword(parent, localeUtil
					.getString("password"), localeUtil
					.getString("certificaterequest"));

			if (password == null) {
				return;
			}

			try {
				PrivateKey privateKey = (PrivateKey) manager.getKey(alias,
						password);
				Certificate cert = manager.getCertificateChain(alias)[0];

				File file = UiUtil.getSelectedFile(this,
						JFileChooser.SAVE_DIALOG, new CustomFileFilter(".csr",
								localeUtil.getString("certificaterequest")));

				if (file == null) {
					return;
				}

				setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
				CertificateUtil.generatePKCS10CSR(CertificateUtil
						.convertCertificate(cert), privateKey, file.getPath());
			} catch (Exception err) {
				err.printStackTrace();
				UiUtil.showError(parent, err.getMessage(), localeUtil
						.getString("error"));
			} finally {
				setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * Delete Action
	 * 
	 */
	public void deleteAction() {
		int row = table.getSelectedRow();

		if (row != -1) {
			String alias = model.getAlias(row);

			try {
				manager.deleteEntry(alias);
				deleteButton.setEnabled(false);
				exportButton.setEnabled(false);
				infoButton.setEnabled(false);
				deleteItem.setEnabled(false);
				renameEntryItem.setEnabled(false);
				exportItem.setEnabled(false);
				newCsrItem.setEnabled(false);
				importCsrItem.setEnabled(false);
				copyKeyItem.setEnabled(false);
				infoItem.setEnabled(false);
				table.clearSelection();
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
	}

	/**
	 * Export Action
	 * 
	 */
	public void exportAction() {
		int row = table.getSelectedRow();

		if (row != -1) {
			String alias = model.getValueAt(row, 0).toString();

			if (manager.isCertificateEntry(alias)) {
				exportCert(alias);
			} else {
				exportKeyPair(alias);
			}
		}
	}

	/**
	 * Export Certificate
	 * 
	 * @param alias Key Store Alias
	 */
	private void exportCert(String alias) {
		File file = UiUtil.getSelectedFile(parent, JFileChooser.SAVE_DIALOG,
				new CustomFileFilter(".cer", localeUtil
						.getString("certificate")));

		if (file != null) {
			try {
				setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
				manager.exportBase64Certificate(alias, file.getPath());
			} catch (Exception err) {
				UiUtil.showError(parent, err.getMessage(), localeUtil
						.getString("error"));
			} finally {
				setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		}
	}

	/**
	 * Export Key Pair
	 * 
	 * @param row
	 */
	private void exportKeyPair(String alias) {
		try {
			File file = UiUtil.getSelectedFile(this, JFileChooser.OPEN_DIALOG,
					new CustomFileFilter(securityUtil.getFileKeystoreMap(),
							localeUtil.getString("keystore")));

			if (file == null) {
				return;
			}

			String fileExt = null;
			String ksType = null;

			if (file.getName().lastIndexOf('.') != -1) {
				fileExt = file.getName().substring(
						file.getName().lastIndexOf('.'),
						file.getName().length());

				if (securityUtil.getFileKeystoreMap().get(fileExt) != null) {
					ksType = securityUtil.getFileKeystoreMap().get(fileExt)
							.toString();
				}
			}

			if (ksType == null) {
				ksType = UiUtil.showValues(this, localeUtil.getString("type"),
						localeUtil.getString("keystore"), securityUtil
								.getStoreTypes());
			}

			if (ksType != null) {
				String kspassword = UiUtil.getPassword(this, localeUtil
						.getString("password"), localeUtil
						.getString("keystore"));

				if (kspassword != null) {
					setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
					KeyStore ks = KeyUtil.loadKeyStore(file.getPath(),
							kspassword, ksType, securityUtil
									.getKeystoreProviderName(ksType));

					String newAlias = null;
					JTextField aliasField = new JTextField(20);

					if (UiUtil
							.showConfirmation(this, UiUtil.getAliasPanel(
									localeUtil, aliasField), localeUtil
									.getString("keypair"),
									JOptionPane.QUESTION_MESSAGE)) {
						newAlias = aliasField.getText();
					}

					if (newAlias != null) {
						String kppassword = UiUtil.getPassword(this, localeUtil
								.getString("password"), localeUtil
								.getString("key"));

						if (kppassword != null) {
							PrivateKey key = (PrivateKey) manager.getKey(
									alias, kppassword);
							Certificate[] chain = manager
									.getCertificateChain(alias);
							ks.setKeyEntry(newAlias, key, kppassword
									.toCharArray(), chain);
							KeyUtil.storeKeyStore(ks, file.getPath(), kspassword);
						}
					}
				}
			}
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
			err.printStackTrace();
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Import CSR Action
	 * 
	 */
	public void importCsrAction() {
		File file = UiUtil.getSelectedFile(parent, JFileChooser.OPEN_DIALOG,
				new CustomFileFilter(".cer", localeUtil
						.getString("certificate")));

		if (file != null) {
			String alias = model.getValueAt(table.getSelectedRow(), 0)
					.toString();

			if (alias != null) {
				try {
					X509Certificate[] oldCerts = CertificateUtil
							.convertCertificates(manager
									.getCertificateChain(alias));

					setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
					X509Certificate[] replyCerts = CertificateUtil
							.orderX509CertChain(CertificateUtil
									.readX509Certificates(file.getPath()));
					setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));

					if ((replyCerts == null) || (replyCerts.length == 0)) {
						return;
					}

					// Check public keys
					if (!oldCerts[0].getPublicKey().equals(
							replyCerts[0].getPublicKey())) {
						throw new Exception(localeUtil
								.getString("error.publickeymatch"));
					}

					String password = UiUtil
							.getPassword(parent, localeUtil
									.getString("password"), localeUtil
									.getString("key"));

					if (password != null
							&& establishTrust(replyCerts[0]) != null) {
						Key privKey = manager.getKey(alias, password);
						manager.deleteEntry(alias);
						manager.setKeyEntry(alias, privKey, password,
								replyCerts);
					} else if (password != null) {
						if (UiUtil.showConfirmation(parent, localeUtil
								.getString("error.trustedcertificate"),
								localeUtil.getString("warning"))) {
							Key privKey = manager.getKey(alias, password);
							manager.deleteEntry(alias);
							manager.setKeyEntry(alias, privKey, password,
									replyCerts);
						}
					} else {
						return;
					}
				} catch (Exception err) {
					err.printStackTrace();
					UiUtil.showError(parent, err.getMessage(), localeUtil
							.getString("error"));
				} finally {
					setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

	/**
	 * Import Cert Action
	 * 
	 */
	public void importCertAction() {
		File file = UiUtil.getSelectedFile(parent, JFileChooser.OPEN_DIALOG,
				new CustomFileFilter(".cer", localeUtil
						.getString("certificate")));

		if (file != null) {
			String alias = UiUtil.getPassword(parent, localeUtil
					.getString("new.alias"), localeUtil
					.getString("certificate"));

			if (alias != null) {
				try {
					setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
					X509Certificate cert = CertificateUtil
							.readX509Certificate(file.getPath());
					if (establishTrust(cert) != null) {
						manager.setCertificateEntry(alias, cert);
					} else {
						if (UiUtil.showConfirmation(parent, localeUtil
								.getString("error.trustedcertificate"),
								localeUtil.getString("warning"))) {
							manager.setCertificateEntry(alias, cert);
						}
					}
				} catch (Exception err) {
					UiUtil.showError(parent, err.getMessage(), localeUtil
							.getString("error"));
				} finally {
					setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
				}
			}
		}
	}

	/**
	 * Import Key Action
	 * 
	 */
	public void importKeyAction() {
		try {
			File file = UiUtil.getSelectedFile(this, JFileChooser.OPEN_DIALOG,
					new CustomFileFilter(securityUtil.getFileKeystoreMap(),
							localeUtil.getString("keystore")));

			if (file == null) {
				return;
			}

			String fileExt = null;
			String ksType = null;

			if (file.getName().lastIndexOf('.') != -1) {
				fileExt = file.getName().substring(
						file.getName().lastIndexOf('.'),
						file.getName().length());

				if (securityUtil.getFileKeystoreMap().get(fileExt) != null) {
					ksType = securityUtil.getFileKeystoreMap().get(fileExt)
							.toString();
				}
			}

			if (ksType == null) {
				ksType = UiUtil.showValues(this, localeUtil.getString("type"),
						localeUtil.getString("keystore"), securityUtil
								.getStoreTypes());
			}

			if (ksType != null) {
				String password = UiUtil.getPassword(this, localeUtil
						.getString("password"), localeUtil
						.getString("keystore"));

				if (password != null) {
					setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
					KeyStore ks = KeyUtil.loadKeyStore(file.getPath(),
							password, ksType, securityUtil
									.getKeystoreProviderName(ksType));

					String alias = UiUtil.showValues(this, localeUtil
							.getString("alias"), localeUtil
							.getString("keypair"), KeyUtil
							.getKeyPairAliases(ks));

					if (alias != null) {
						password = UiUtil.getPassword(this, localeUtil
								.getString("new.password"), localeUtil
								.getString("key"));

						if (password != null) {
							manager.setKeyEntry(alias, ks.getKey(alias,
									password.toCharArray()), password, ks
									.getCertificateChain(alias));
						}
					}
				}
			}
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
			err.printStackTrace();
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Info Action
	 * 
	 */
	public void infoAction() {
		int row = table.getSelectedRow();

		if (row != -1) {
			String alias = model.getValueAt(row, 0).toString();

			if (manager.isCertificateEntry(alias)) {
				try {
					Certificate cert = manager.getCertificate(alias);
					UiUtil.showInfo(parent, new CertificateInfoPanel(cert,parent),
							localeUtil.getString("entry") + " - " + alias);
				} catch (Exception err) {
					UiUtil.showError(parent, err.getMessage(), localeUtil
							.getString("error"));
				}
			} else {
				try {
					String password = UiUtil
							.getPassword(parent, localeUtil
									.getString("password"), localeUtil
									.getString("key"));

					if (password != null) {
						Key key = manager.getKey(alias, password);
						Certificate[] chain = null;

						if (manager.isKeyPairEntry(alias)) {
							chain = manager.getCertificateChain(alias);
						}

						UiUtil.showInfo(parent, new KeyInfoPanel(key, chain, parent),
								localeUtil.getString("entry") + " - " + alias);
					}
				} catch (Exception err) {
					UiUtil.showError(parent, err.getMessage(), localeUtil
							.getString("error"));
				}
			}
		}
	}

	/**
	 * Mouse Entered
	 * 
	 * @param e Mouse Event
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * Mouse Exited
	 * 
	 * @param e Mouse Event
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Mouse Pressed
	 * 
	 * @param e Mouse Event
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * Mouse Released
	 * 
	 * @param e Mouse Event
	 */
	public void mouseReleased(MouseEvent e) {
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
		if (table.getSelectedRowCount() <= 0) {
			disableAll();
		} else {
			enableButtonsItems();
		}
	}

	/**
	 * Key Typed
	 * 
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Certificate Imported
	 * 
	 * @pram evt
	 */
	public void entryImported(KeystoreEvent evt) {
		try {
			statusLabel.setText(manager.getSize() + " "
					+ localeUtil.getString("entries"));
		} catch (Exception err) {
		}
	}

	/**
	 * Entry Deleted
	 * 
	 * @pram evt
	 */
	public void entryDeleted(KeystoreEvent evt) {
		deleteButton.setEnabled(false);
		exportButton.setEnabled(false);
		infoButton.setEnabled(false);
		deleteItem.setEnabled(false);
		renameEntryItem.setEnabled(false);
		newCsrItem.setEnabled(false);
		importCsrItem.setEnabled(false);
		copyKeyItem.setEnabled(false);
		exportItem.setEnabled(false);
		infoItem.setEnabled(false);
		table.clearSelection();

		try {
			statusLabel.setText(manager.getSize() + " "
					+ localeUtil.getString("entries"));
		} catch (Exception err) {
		}
	}

	/**
	 * Keystore Loaded
	 */
	public void keyStoreLoaded(KeystoreEvent evt) {
		// Clear table
		table.clearSelection();

		// Set enabled
		deleteButton.setEnabled(false);
		exportButton.setEnabled(false);
		infoButton.setEnabled(false);
		deleteItem.setEnabled(false);
		renameEntryItem.setEnabled(false);
		newCsrItem.setEnabled(false);
		importCsrItem.setEnabled(false);
		copyKeyItem.setEnabled(false);
		exportItem.setEnabled(false);
		infoItem.setEnabled(false);
		importKeyItem.setEnabled(true);
		importCertItem.setEnabled(true);
		propItem.setEnabled(false);
		passwordItem.setEnabled(true);
		propButton.setEnabled(true);
		propItem.setEnabled(true);

		typeField.setText(manager.getKeyStoreType());
		providerField.setText(manager.getProvider().getName());
		versionField.setText(manager.getProvider().getVersion() + "");

		try {
			statusLabel.setText(manager.getSize() + " "
					+ localeUtil.getString("entries"));
		} catch (Exception err) {
		}
	}

	/**
	 * Set Delete Button
	 * 
	 * @param deleteButton
	 */
	public void setDeleteButton(JButton deleteButton) {
		this.deleteButton = deleteButton;
	}

	/**
	 * Set Export Button
	 * 
	 * @param exportButton
	 */
	public void setExportButton(JButton exportButton) {
		this.exportButton = exportButton;
	}

	/**
	 * Set Info Button
	 * 
	 * @param infoButton
	 */
	public void setInfoButton(JButton infoButton) {
		this.infoButton = infoButton;
	}

	/**
	 * Set Export Item
	 * 
	 * @param exportItem
	 */
	public void setExportItem(JMenuItem exportItem) {
		this.exportItem = exportItem;
	}

	/**
	 * Set Import Cert Item
	 * 
	 * @param importCertItem
	 */
	public void setImportCertItem(JMenuItem importCertItem) {
		this.importCertItem = importCertItem;
	}

	/**
	 * Set Import Key Item
	 * 
	 * @param importKeyItem
	 */
	public void setImportKeyItem(JMenuItem importKeyItem) {
		this.importKeyItem = importKeyItem;
	}

	/**
	 * Set Delete Item
	 * 
	 * @param deleteItem
	 */
	public void setDeleteItem(JMenuItem deleteItem) {
		this.deleteItem = deleteItem;
	}

	/**
	 * Set Info Item
	 * 
	 * @param infoItem
	 */
	public void setInfoItem(JMenuItem infoItem) {
		this.infoItem = infoItem;
	}

	/**
	 * Set Password
	 * 
	 * @param passwordItem
	 */
	public void setPasswordItem(JMenuItem passwordItem) {
		this.passwordItem = passwordItem;
	}

	/**
	 * Set Prop Item
	 * 
	 * @param propItem
	 */
	public void setPropItem(JMenuItem propItem) {
		this.propItem = propItem;
	}

	/**
	 * Set CSR Item
	 * 
	 * @param csrItem
	 */
	public void setNewCsrItem(JMenuItem newCsrItem) {
		this.newCsrItem = newCsrItem;
	}

	/**
	 * Set CSR Item
	 * 
	 * @param csrItem
	 */
	public void setImportCsrItem(JMenuItem importCsrItem) {
		this.importCsrItem = importCsrItem;
	}

	/**
	 * Set Copy Key Item
	 * 
	 * @param copyKeyItem
	 */
	public void setCopyKeyItem(JMenuItem copyKeyItem) {
		this.copyKeyItem = copyKeyItem;
	}

	/**
	 * Set Rename Entry Item
	 * 
	 * @param renameEntryItem
	 */
	public void setRenameEntryItem(JMenuItem renameEntryItem) {
		this.renameEntryItem = renameEntryItem;
	}

}
