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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.jlocksmith.util.CertificateUtil;
import com.jlocksmith.util.CustomFileFilter;
import com.jlocksmith.util.KeyUtil;
import com.jlocksmith.util.LocaleUtil;
import com.jlocksmith.util.SecurityUtil;
import com.jlocksmith.util.UiUtil;

/**
 * Main Frame
 * 
 * @author Derek Helbert
 */
public class MainFrame extends JFrame implements WindowListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3274520862445824847L;

	/** Keystore Manager */
	KeystoreManager manager = KeystoreManager.getInstance();

	/** Locale Util */
	LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Security Util */
	SecurityUtil securityUtil = SecurityUtil.getInstance();

	/** UiUtl Util */
	UiUtil uiUtil = new UiUtil();

	/** Menu Bar */
	private JMenuBar bar = new JMenuBar();

	/** File Menu */
	private JMenu fileMenu = null;

	/** Help Menu */
	private JMenu helpMenu = null;

	/** Import Menu */
	private JMenu importMenu = null;

	/** Keystore Menu */
	private JMenu keystoreMenu = null;

	/** Keystore Menu */
	private JMenu toolsMenu = null;

	/** Entry Menu */
	private JMenu entryMenu = null;

	/** New Entry Menu */
	private JMenu newEntryMenu = null;

	/** New Item */
	private JMenuItem newFileItem = null;

	/** Open Item */
	private JMenuItem openItem = null;

	/** Save Item */
	private JMenuItem saveItem = null;

	/** Save As Item */
	private JMenuItem saveAsItem = null;

	/** Exit Item */
	private JMenuItem exitItem = null;

	/** About Item */
	private JMenuItem aboutItem = null;

	/** License Item */
	private JMenuItem licenseItem = null;

	/** Print Cert Item */
	private JMenuItem printCertItem = null;

	/** SSL Item */
	private JMenuItem sslItem = null;

	/** Prop Item */
	private JMenuItem propItem = null;

	/** Password Item */
	private JMenuItem passwordItem = null;

	/** New CSR Item */
	private JMenuItem newCsrItem = null;

	/** Import CSR Item */
	private JMenuItem importCsrItem = null;

	/** CSR Item */
	private JMenuItem copyKeyItem = null;

	/** Password Item */
	private JMenuItem newKeyPairItem = null;

	/** Rename Entry Item */
	private JMenuItem renameEntryItem = null;

	/** Prefs Item */
	private JMenuItem prefsItem = null;

	/** Version Number */
	private String versionNum = "1.0.2";

	/** Delete Button */
	private JButton deleteButton = new JButton();

	/** Export Button */
	private JButton exportButton = new JButton();

	/** Info Button */
	private JButton infoButton = new JButton();

	/** Export Button */
	private JButton newButton = new JButton();

	/** Info Button */
	private JButton openButton = new JButton();

	/** Save Button */
	private JButton saveButton = new JButton();

	/** Delete Button */
	private JMenuItem deleteItem;

	/** Export Button */
	private JMenuItem exportItem;

	/** Info Button */
	private JMenuItem infoItem;

	/** Import Cert Item */
	private JMenuItem importCertItem;

	/** Import Key Item */
	private JMenuItem importKeyItem;

	/** New Certificate Button */
	private JMenuItem newCertificateItem;

	/** Main Panel */
	private MainPanel mainPanel;

	/** Toolbar */
	private JToolBar toolbar = new JToolBar(JToolBar.HORIZONTAL);

	/** Prefs */
	private static Preferences preferences = null;

	/**
	 * Constructor
	 * 
	 */
	public MainFrame() {
		super();

		try {
			preferences = new Preferences();
		} catch (Exception err) {
			err.printStackTrace();
		}

		init();
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		// Set Title
		setTitle(localeUtil.getString("jlocksmith"));
		// setIconImage(uiUtil.getImageIcon("logo.gif").getImage());

		// Set Layout
		getContentPane().setLayout(new BorderLayout(5, 5));

		// Toolbar setup
		saveButton.setEnabled(false);
		deleteButton.setEnabled(false);
		exportButton.setEnabled(false);
		infoButton.setEnabled(false);
		newButton.setToolTipText(localeUtil.getString("new.tooltip"));
		openButton.setToolTipText(localeUtil.getString("open.tooltip"));
		saveButton.setToolTipText(localeUtil.getString("save.tooltip"));
		deleteButton.setToolTipText(localeUtil.getString("delete.tooltip"));
		exportButton.setToolTipText(localeUtil.getString("export.tooltip"));
		infoButton.setToolTipText(localeUtil.getString("info.tooltip"));
		deleteButton.setIcon(uiUtil.getImageIcon("x.gif"));
		exportButton.setIcon(uiUtil.getImageIcon("export.gif"));
		infoButton.setIcon(uiUtil.getImageIcon("search.gif"));
		newButton.setIcon(uiUtil.getImageIcon("new.gif"));
		openButton.setIcon(uiUtil.getImageIcon("open.gif"));
		saveButton.setIcon(uiUtil.getImageIcon("save.gif"));
		deleteButton.addActionListener(this);
		exportButton.addActionListener(this);
		infoButton.addActionListener(this);
		newButton.addActionListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);

		toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolbar.setRollover(false);
		toolbar.setFloatable(true);
		toolbar.add(newButton);
		toolbar.add(openButton);
		toolbar.add(saveButton);
		toolbar.add(deleteButton);
		toolbar.add(exportButton);
		toolbar.add(infoButton);

		// Setup items
		printCertItem = new JMenuItem(localeUtil
				.getString("previewcertificate"));
		newFileItem = new JMenuItem(localeUtil.getString("new"));
		openItem = new JMenuItem(localeUtil.getString("open"));
		saveItem = new JMenuItem(localeUtil.getString("save"));
		saveAsItem = new JMenuItem(localeUtil.getString("saveas"));
		exitItem = new JMenuItem(localeUtil.getString("exit"));
		aboutItem = new JMenuItem(localeUtil.getString("about") + "...");
		importCertItem = new JMenuItem(localeUtil.getString("certificate"));
		importKeyItem = new JMenuItem(localeUtil.getString("keypair"));
		exportItem = new JMenuItem(localeUtil.getString("exportentry"));
		infoItem = new JMenuItem(localeUtil.getString("examineentry"));
		deleteItem = new JMenuItem(localeUtil.getString("deleteentry"));
		propItem = new JMenuItem(localeUtil.getString("examine.properties"));
		passwordItem = new JMenuItem(localeUtil.getString("setpassword")
				+ "...");
		newCertificateItem = new JMenuItem(localeUtil.getString("certificate"));
		newKeyPairItem = new JMenuItem(localeUtil.getString("keypair"));
		licenseItem = new JMenuItem(localeUtil.getString("license"));
		newCsrItem = new JMenuItem(localeUtil.getString("certificaterequest"));
		importCsrItem = new JMenuItem(localeUtil
				.getString("certificaterequest"));
		copyKeyItem = new JMenuItem(localeUtil.getString("copykey"));
		sslItem = new JMenuItem(localeUtil.getString("examinesslconnection"));
		prefsItem = new JMenuItem(localeUtil.getString("preferences") + "...");
		renameEntryItem = new JMenuItem(localeUtil.getString("renameentry"));
		saveAsItem.setEnabled(false);
		saveItem.setEnabled(false);
		exportItem.setEnabled(false);
		infoItem.setEnabled(false);
		deleteItem.setEnabled(false);
		propItem.setEnabled(false);
		passwordItem.setEnabled(false);
		newCsrItem.setEnabled(false);
		importCsrItem.setEnabled(false);
		copyKeyItem.setEnabled(false);
		renameEntryItem.setEnabled(false);
		exitItem.setIcon(uiUtil.getImageIcon("exit.gif"));
		newFileItem.setIcon(uiUtil.getImageIcon("new.gif"));
		openItem.setIcon(uiUtil.getImageIcon("open.gif"));
		saveItem.setIcon(uiUtil.getImageIcon("save.gif"));
		importCertItem.setIcon(uiUtil.getImageIcon("certificate.gif"));
		importKeyItem.setIcon(uiUtil.getImageIcon("keypair.gif"));
		exportItem.setIcon(uiUtil.getImageIcon("export.gif"));
		infoItem.setIcon(uiUtil.getImageIcon("search.gif"));
		deleteItem.setIcon(uiUtil.getImageIcon("x.gif"));
		// aboutItem.setIcon(uiUtil.getImageIcon("help.gif"));
		propItem.setIcon(uiUtil.getImageIcon("search.gif"));
		prefsItem.setIcon(uiUtil.getImageIcon("props.gif"));
		printCertItem.setIcon(uiUtil.getImageIcon("preview.gif"));
		newCertificateItem.setIcon(uiUtil.getImageIcon("newcertificate.gif"));
		newKeyPairItem.setIcon(uiUtil.getImageIcon("newkeypair.gif"));
		licenseItem.setIcon(uiUtil.getImageIcon("doc.gif"));
		newCsrItem.setIcon(uiUtil.getImageIcon("csr.gif"));
		importCsrItem.setIcon(uiUtil.getImageIcon("csrimport.gif"));
		copyKeyItem.setIcon(uiUtil.getImageIcon("copy.gif"));
		sslItem.setIcon(uiUtil.getImageIcon("search.gif"));
		renameEntryItem.setIcon(uiUtil.getImageIcon("rename.gif"));
		exitItem.addActionListener(this);
		newFileItem.addActionListener(this);
		saveItem.addActionListener(this);
		saveAsItem.addActionListener(this);
		openItem.addActionListener(this);
		aboutItem.addActionListener(this);
		importCertItem.addActionListener(this);
		importKeyItem.addActionListener(this);
		exportItem.addActionListener(this);
		infoItem.addActionListener(this);
		deleteItem.addActionListener(this);
		propItem.addActionListener(this);
		passwordItem.addActionListener(this);
		printCertItem.addActionListener(this);
		newCertificateItem.addActionListener(this);
		newKeyPairItem.addActionListener(this);
		licenseItem.addActionListener(this);
		newCsrItem.addActionListener(this);
		importCsrItem.addActionListener(this);
		copyKeyItem.addActionListener(this);
		sslItem.addActionListener(this);
		prefsItem.addActionListener(this);
		renameEntryItem.addActionListener(this);

		// Setup Main Panel
		mainPanel = new MainPanel(this);
		mainPanel.setDeleteButton(deleteButton);
		mainPanel.setExportButton(exportButton);
		mainPanel.setInfoButton(infoButton);
		mainPanel.setExportItem(exportItem);
		mainPanel.setImportCertItem(importCertItem);
		mainPanel.setImportKeyItem(importCertItem);
		mainPanel.setDeleteItem(deleteItem);
		mainPanel.setInfoItem(infoItem);
		mainPanel.setPropItem(propItem);
		mainPanel.setPasswordItem(passwordItem);
		mainPanel.setNewCsrItem(newCsrItem);
		mainPanel.setImportCsrItem(importCsrItem);
		mainPanel.setCopyKeyItem(copyKeyItem);
		mainPanel.setRenameEntryItem(renameEntryItem);

		// Setup menus
		newEntryMenu = new JMenu(localeUtil.getString("new"));
		importMenu = new JMenu(localeUtil.getString("import"));
		fileMenu = new JMenu(localeUtil.getString("file"));
		helpMenu = new JMenu(localeUtil.getString("help"));
		keystoreMenu = new JMenu(localeUtil.getString("keystore"));
		entryMenu = new JMenu(localeUtil.getString("entry"));
		toolsMenu = new JMenu(localeUtil.getString("tools"));
		fileMenu.setMnemonic(localeUtil.getString("file").charAt(0));
		helpMenu.setMnemonic(localeUtil.getString("help").charAt(0));
		entryMenu.setMnemonic(localeUtil.getString("entry").charAt(0));
		keystoreMenu.setMnemonic(localeUtil.getString("keystore").charAt(0));
		toolsMenu.setMnemonic(localeUtil.getString("tools").charAt(0));
		newEntryMenu.setEnabled(false);
		importMenu.setEnabled(false);

		newEntryMenu.add(newCertificateItem);
		newEntryMenu.add(newCsrItem);
		newEntryMenu.add(newKeyPairItem);
		importMenu.add(importCertItem);
		importMenu.add(importCsrItem);
		importMenu.add(importKeyItem);
		keystoreMenu.add(propItem);
		keystoreMenu.add(passwordItem);
		fileMenu.add(newFileItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		entryMenu.add(newEntryMenu);
		entryMenu.add(importMenu);
		entryMenu.addSeparator();
		entryMenu.add(copyKeyItem);
		entryMenu.add(renameEntryItem);
		entryMenu.add(deleteItem);
		entryMenu.add(exportItem);
		entryMenu.addSeparator();
		entryMenu.add(infoItem);
		helpMenu.add(licenseItem);
		helpMenu.addSeparator();
		helpMenu.add(aboutItem);
		toolsMenu.add(printCertItem);
		toolsMenu.add(sslItem);
		toolsMenu.addSeparator();
		toolsMenu.add(prefsItem);

		// Set Menu Bar
		bar.add(fileMenu);
		bar.add(keystoreMenu);
		bar.add(entryMenu);
		bar.add(toolsMenu);
		bar.add(helpMenu);
		setJMenuBar(bar);

		// Set size and display
		setSize(new Dimension(800, 600));

		getContentPane().add(mainPanel);
		getContentPane().add(toolbar, BorderLayout.PAGE_START);

		// Center
		UiUtil.centerComponent(this);

		// Set Visible
		setVisible(true);
	}

	/**
	 * Get Preferences
	 * 
	 * @return Preferences
	 */
	public static Preferences getPreferences() {
		return preferences;
	}

	/**
	 * SSL Action
	 * 
	 */
	private void sslAction() {
		try {
			HostPortPanel hpPanel = new HostPortPanel();
			String host = null;

			if (UiUtil.showConfirmation(this, hpPanel, localeUtil
					.getString("examinesslconnection"))) {
				host = hpPanel.getHost();
			} else {
				return;
			}

			setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));

			Certificate[] chain = CertificateUtil.getCertificationPath(host,
					hpPanel.getPort());
			UiUtil.showInfo(this, new CertificateChainPanel(chain,this), localeUtil
					.getString("certificatechain"));
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * License Action
	 * 
	 */
	private void licenseAction() {
		try {
			UiUtil util = new UiUtil();
			String text = util.getFileText("gpl.txt");
			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout(5, 5));
			JTextArea textArea = new JTextArea(text, 25, 80);
			textArea.setEditable(false);
			JScrollPane spane = new JScrollPane(textArea);
			panel.setBorder(new EtchedBorder());
			panel.add(BorderLayout.CENTER, spane);
			UiUtil.showInfo(this, panel, localeUtil.getString("license"));
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
			err.printStackTrace();
		}
	}

	/**
	 * New Key Pair Action
	 * 
	 */
	private void newKeyPairAction() {
		try {
			KeyPair keyPair = null;
			KeyPairPanel kpPanel = new KeyPairPanel();

			if (UiUtil.showConfirmation(this, kpPanel, localeUtil
					.getString("keypair"))) {
				keyPair = KeyUtil.getKeyPair(kpPanel.getKeyAlgorithm(), kpPanel
						.getKeySize());
			} else {
				return;
			}

			Certificate cert = null;
			CertificatePanel cpanel = new CertificatePanel(keyPair);

			if (UiUtil.showConfirmation(this, cpanel, localeUtil
					.getString("certificate"))) {
				cert = CertificateUtil.generateCertificate(cpanel
						.getCommonName(), cpanel.getOrganizationUnit(), cpanel
						.getOrganizationName(), cpanel.getLocality(), cpanel
						.getState(), cpanel.getCountry(), cpanel.getEmail(),
						Integer.parseInt(cpanel.getValidity()), keyPair, cpanel
								.getSigAlg());
			} else {
				return;
			}

			String alias = null;
			JTextField aliasField = new JTextField(20);

			if (UiUtil.showConfirmation(this, UiUtil.getAliasPanel(localeUtil,
					aliasField), localeUtil.getString("keypair"),
					JOptionPane.QUESTION_MESSAGE)) {
				alias = aliasField.getText();
			}
			if (alias == null) {
				return;
			}

			String password = UiUtil
					.getPassword(this, localeUtil.getString("new.password"),
							localeUtil.getString("keypair"));

			if (password == null) {
				return;
			}

			manager.setKeyEntry(alias, keyPair.getPrivate(), password,
					new Certificate[] { cert });
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
			err.printStackTrace();
		}
	}

	/**
	 * New Cert Action
	 * 
	 */
	private void newCertAction() {
		try {
			KeyPairPanel kpPanel = new KeyPairPanel();

			if (UiUtil.showConfirmation(this, kpPanel, localeUtil
					.getString("certificate"))) {
				KeyPair keyPair = KeyUtil.getKeyPair(kpPanel.getKeyAlgorithm(),
						kpPanel.getKeySize());

				CertificatePanel cpanel = new CertificatePanel(keyPair);
				JTextField aliasField = new JTextField(20);

				if (UiUtil.showConfirmation(this, cpanel, localeUtil
						.getString("certificate"))) {

					if (UiUtil.showConfirmation(this, UiUtil.getAliasPanel(
							localeUtil, aliasField), localeUtil
							.getString("certificate"),
							JOptionPane.QUESTION_MESSAGE)) {
						Certificate newcert = CertificateUtil
								.generateCertificate(cpanel.getCommonName(),
										cpanel.getOrganizationUnit(), cpanel
												.getOrganizationName(), cpanel
												.getLocality(), cpanel
												.getState(), cpanel
												.getCountry(), cpanel
												.getEmail(),
										Integer.parseInt(cpanel.getValidity()),
										keyPair, cpanel.getSigAlg());

						manager.setCertificateEntry(aliasField.getText(),
								newcert);
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
	 * Print Cert Action
	 * 
	 */
	private void printCertAction() {
		try {
			File file = UiUtil.getSelectedFile(this, JFileChooser.OPEN_DIALOG,
					new CustomFileFilter(".cer", localeUtil
							.getString("certificate")));

			if (file != null) {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				FileInputStream inStream = new FileInputStream(file);
				X509Certificate cert = (X509Certificate) cf
						.generateCertificate(inStream);
				inStream.close();
				UiUtil.showInfo(this, new CertificateInfoPanel(cert,this), file
						.getName());
			}
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		}
	}

	/**
	 * New Action
	 * 
	 */
	private void newFileAction() {
		try {
			String storeType = UiUtil.showValues(this, localeUtil
					.getString("type"), localeUtil.getString("keystore"),
					securityUtil.getStoreTypes());

			if (storeType != null) {
				setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
				manager.newKeyStore(storeType, securityUtil
						.getKeystoreProviderName(storeType));
				saveAsItem.setEnabled(true);
				saveItem.setEnabled(false);
				saveButton.setEnabled(false);
				newEntryMenu.setEnabled(true);
				importMenu.setEnabled(true);
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
	 * Open Action
	 * 
	 */
	private void openAction() {
		try {
			File file = UiUtil.getSelectedFile(this, JFileChooser.OPEN_DIALOG,
					new CustomFileFilter(securityUtil.getFileKeystoreMap(), localeUtil.getString("keystore")));

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
					manager.loadKeyStore(file.getPath(), password, ksType,
							securityUtil.getKeystoreProviderName(ksType));
					setTitle(localeUtil.getString("jlocksmith") + " - "
							+ file.getName());
					saveItem.setEnabled(true);
					saveButton.setEnabled(true);
					saveAsItem.setEnabled(true);
					newEntryMenu.setEnabled(true);
					importMenu.setEnabled(true);
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
	 * Prefs Action
	 * 
	 */
	private void prefsAction() {
		try {
			PreferencesPanel prefPanel = new PreferencesPanel();

			if (UiUtil.showConfirmation(this, prefPanel, localeUtil.getString("preferences"))) {
				// Set prefs
				preferences.setUseCaCertsKeystore(prefPanel.getUseCaCerts());
				preferences.setCaCertsKeystorePath(prefPanel.getFilePath());
				preferences.setCaCertsKeystorePassword(prefPanel.getPassword());

				// Save
				preferences.save();
			} else {
				return;
			}
		} catch (Exception err) {
			err.printStackTrace();
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Save Action
	 * 
	 */
	private void passwordAction() {
		try {
			String password = UiUtil.getPassword(this, localeUtil
					.getString("new.password"), localeUtil
					.getString("keystore"));

			if (password != null) {
				manager.setKeystorePassword(password);
			}
		} catch (Exception err) {
			err.printStackTrace();
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Save Action
	 * 
	 */
	private void saveAction() {
		try {
			setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			manager.storeKeyStore(manager.getFilePath(), manager
					.getKeystorePassword());
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Save As Action
	 * 
	 */
	private void saveAsAction() {
		try {
			File file = UiUtil.getSelectedFile(this, JFileChooser.SAVE_DIALOG,
					new CustomFileFilter(securityUtil
							.getKeystoreFileMap(manager.getKeyStoreType()),
							localeUtil.getString("keystore")));

			if (file != null) {
				String password = null;

				// Use existing
				password = manager.getKeystorePassword();

				// Get new password
				if (password == null) {
					password = UiUtil.getPassword(this, localeUtil
							.getString("password"), localeUtil
							.getString("keystore"));
				}

				// Not cancelled
				if (password != null) {
					setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
					manager.storeKeyStore(file.getPath(), password);
					saveItem.setEnabled(true);
					saveButton.setEnabled(true);
					setTitle(localeUtil.getString("jlocksmith") + " - "
							+ file.getName());
				}
			}
		} catch (Exception err) {
			UiUtil.showError(this, err.getMessage(), localeUtil
					.getString("error"));
		} finally {
			setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Exit Action
	 * 
	 */
	private void exitAction() {
		System.exit(0);
	}

	/**
	 * About Action
	 * 
	 */
	private void aboutAction() {
		UiUtil.showInfo(this, localeUtil.getString("jlocksmith") + " "
				+ versionNum, localeUtil.getString("about"));
	}

	/**
	 * Window Activated
	 * 
	 * @param e WindowEvent
	 */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * Window Closed
	 * 
	 * @param e WindowEvent
	 */
	public void windowClosed(WindowEvent e) {
	}

	/**
	 * Window Closing
	 * 
	 * @param e WindowEvent
	 */
	public void windowClosing(WindowEvent e) {
	}

	/**
	 * Window Deactivated
	 * 
	 * @param e WindowEvent
	 */
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * Window Deiconified
	 * 
	 * @param e WindowEvent
	 */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * Window Iconified
	 * 
	 * @param e WindowEvent
	 */
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * Window Opened
	 * 
	 * @param e WindowEvent
	 */
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Action Performed
	 * 
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitItem) {
			exitAction();
		} else if (e.getSource() == printCertItem) {
			printCertAction();
		} else if (e.getSource() == newButton || e.getSource() == newFileItem) {
			newFileAction();
		} else if (e.getSource() == aboutItem) {
			aboutAction();
		} else if (e.getSource() == saveAsItem) {
			saveAsAction();
		} else if (e.getSource() == saveButton || e.getSource() == saveItem) {
			saveAction();
		} else if (e.getSource() == openButton || e.getSource() == openItem) {
			openAction();
		} else if (e.getSource() == passwordItem) {
			passwordAction();
		} else if (e.getSource() == propItem) {
			mainPanel.propAction();
		} else if (e.getSource() == deleteButton || e.getSource() == deleteItem) {
			mainPanel.deleteAction();
		} else if (e.getSource() == exportButton || e.getSource() == exportItem) {
			mainPanel.exportAction();
		} else if (e.getSource() == infoButton || e.getSource() == infoItem) {
			mainPanel.infoAction();
		} else if (e.getSource() == importCertItem) {
			mainPanel.importCertAction();
		} else if (e.getSource() == importKeyItem) {
			mainPanel.importKeyAction();
		} else if (e.getSource() == newCertificateItem) {
			newCertAction();
		} else if (e.getSource() == newKeyPairItem) {
			newKeyPairAction();
		} else if (e.getSource() == licenseItem) {
			licenseAction();
		} else if (e.getSource() == newCsrItem) {
			mainPanel.newCsrAction();
		} else if (e.getSource() == importCsrItem) {
			mainPanel.importCsrAction();
		} else if (e.getSource() == copyKeyItem) {
			mainPanel.copyKeyAction();
		} else if (e.getSource() == sslItem) {
			sslAction();
		} else if (e.getSource() == prefsItem) {
			prefsAction();
		} else if (e.getSource() == renameEntryItem) {
			mainPanel.renameEntryAction();
		}
	}

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception err) {
			err.printStackTrace();
		}

		try {
			SecurityUtil util = SecurityUtil.getInstance();
			util.addProvider(new BouncyCastleProvider());
		} catch (Exception err) {
			err.printStackTrace();
		}

		@SuppressWarnings("unused")
		MainFrame main = new MainFrame();
	}

}
