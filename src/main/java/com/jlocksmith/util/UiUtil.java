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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * UI Utility
 * 
 * @author Derek Helbert
 */
public class UiUtil {

	/**
	 * Method to center component on screen.
	 * 
	 * @param comp Component
	 */
	public static void centerComponent(Component comp) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = comp.getSize();
		screenSize.height = screenSize.height / 2;
		screenSize.width = screenSize.width / 2;
		size.height = size.height / 2;
		size.width = size.width / 2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		comp.setLocation(x, y);
	}

	/**
	 * Constraint Utility Method
	 * 
	 * @param gbc GridBagConstraints
	 * @param wx weight x
	 * @param wy weight y
	 * @param gx grid x
	 * @param gy grid y
	 * @param gw grid width x
	 * @param gh grid width y
	 * @param fill Fill
	 * @param anch Anchor
	 */
	public static final void setConstraints(GridBagConstraints gbc, int wx,
			int wy, int gx, int gy, int gw, int gh, int fill, int anch) {
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.fill = fill;
		gbc.anchor = anch;
		gbc.insets = new Insets(5, 5, 5, 5);
	}

	/**
	 * Show Confirm Dialog
	 * 
	 * @param c Component
	 * @param message Message
	 * @param title Dialog Title
	 */
	public static boolean showConfirmation(Component c, String message,
			String title) {
		int val = JOptionPane.showConfirmDialog(c, message, title,
				JOptionPane.YES_NO_OPTION);
		return val == JOptionPane.YES_OPTION ? true : false;
	}

	/**
	 * Show Confirm Dialog
	 * 
	 * @param c Component
	 * @param message Component for Message
	 * @param title Dialog Title
	 */
	public static boolean showConfirmation(Component c, Component m,
			String title, int type) {
		int val = JOptionPane.showConfirmDialog(c, m, title,
				JOptionPane.OK_CANCEL_OPTION, type);
		return val == JOptionPane.OK_OPTION ? true : false;
	}

	/**
	 * Show Confirm Dialog
	 * 
	 * @param c Component
	 * @param message Component for Message
	 * @param title Dialog Title
	 */
	public static boolean showConfirmation(Component c, Component m,
			String title) {
		int val = JOptionPane.showConfirmDialog(c, m, title,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		return val == JOptionPane.OK_OPTION ? true : false;
	}

	/**
	 * Show Error Dialog
	 * 
	 * @param c Component
	 * @param res Ixn Result
	 * @param title Dialog Title
	 */
	public static void showError(Component c, String message, String title) {
		JOptionPane.showMessageDialog(c, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show Error Dialog
	 * 
	 * @param c Component
	 * @param message Message
	 * @param title Title
	 */
	public static void showWarning(Component c, String message, String title) {
		JOptionPane.showMessageDialog(c, message, title,
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Show Error Dialog
	 * 
	 * @param c Component
	 * @param message Message String
	 * @param title Dialog Title
	 */
	public static void showInfo(Component c, String message, String title) {
		JLabel label = new JLabel(message);
		JOptionPane.showMessageDialog(c, label, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show Error Dialog
	 * 
	 * @param c Component
	 * @param m Component
	 * @param title Dialog Title
	 */
	public static void showInfo(Component c, Component m, String title) {
		JOptionPane.showMessageDialog(c, m, title, JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Show Error Dialog
	 * 
	 * @param c Component
	 * @param message Message String
	 * @param title Dialog Title
	 */
	public static String showValues(Component c, String message, String title,
			String[] values) {
		Object o = JOptionPane.showInputDialog(c, message, title,
				JOptionPane.INFORMATION_MESSAGE, null, values, values[0]);
		return o == null ? null : o.toString();
	}

	/**
	 * Get Password - Return NULL if cancelled.
	 * 
	 * @param c
	 * @param message
	 * @param title
	 * 
	 * @return String
	 */
	public static String getPassword(Component c, String message, String title) {
		return JOptionPane.showInputDialog(c, message, title,
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Get Text Value - Return NULL if cancelled.
	 * 
	 * @param c
	 * @param message
	 * @param title
	 * 
	 * @return String
	 */
	public static String getValue(Component c, String message, String title) {
		return JOptionPane.showInputDialog(c, message, title,
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Show File Dialog
	 * 
	 * @param c Component
	 * @param dialogType JFileChooser.OPEN_DIALOG, JFileChooser.SAVE_DIALOG,
	 *            JFileChooser.CUSTOM_DIALOG
	 * 
	 * @return File
	 */
	public static File getSelectedFile(Component c, int dialogType) {
		return getSelectedFile(c, dialogType, null);
	}

	/**
	 * Show File Dialog
	 * 
	 * @param c Component
	 * @param dialogType JFileChooser.OPEN_DIALOG, JFileChooser.SAVE_DIALOG,
	 *            JFileChooser.CUSTOM_DIALOG
	 * @pram cff Custom File Filter
	 * 
	 * @return File
	 */
	public static File getSelectedFile(Component c, int dialogType, CustomFileFilter cff) {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogType(dialogType);
		chooser.setMultiSelectionEnabled(false);

		if (cff != null)
			chooser.setFileFilter(cff);

		int returnVal = 0;

		if (dialogType == JFileChooser.OPEN_DIALOG)
			returnVal = chooser.showOpenDialog(c);
		else
			returnVal = chooser.showSaveDialog(c);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	/**
	 * Get Image Icon From Resource Path
	 * 
	 * @param name Image Name
	 * 
	 * @return String
	 */
	public ImageIcon getImageIcon(String name) {
		String imageResPath = "images/" + name;
		ImageIcon icon = null;

		@SuppressWarnings("rawtypes")
		Class c = this.getClass();
		ClassLoader cload = c.getClassLoader();
		URL iconURL = cload.getResource(imageResPath);

		if (iconURL != null) {
			icon = new ImageIcon(iconURL, name);
		}

		return icon;
	}

	/**
	 * Get File Text
	 * 
	 * @param name
	 * @return String
	 * @throws Exception
	 */
	public String getFileText(String name) throws Exception {
		String text = "";

		@SuppressWarnings("rawtypes")
		Class c = this.getClass();
		ClassLoader cload = c.getClassLoader();
		InputStream is = cload.getResourceAsStream(name);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String temp = br.readLine();

		while (temp != null) {
			text += temp + "\n";
			temp = br.readLine();
		}

		br.close();

		return text;
	}

	/**
	 * Auto Size Table
	 * 
	 * @param table JTable
	 */
	public static void autoSizeTable(JTable table) {
		JTableHeader header = table.getTableHeader();
		TableCellRenderer defaultHeaderRenderer = null;

		if (header != null) {
			defaultHeaderRenderer = header.getDefaultRenderer();
		}

		TableColumnModel columns = table.getColumnModel();
		TableModel data = table.getModel();

		int margin = 10; // Just some padding
		int rowCount = data.getRowCount();
		int totalWidth = 0;

		for (int i = columns.getColumnCount() - 1; i >= 0; --i) {
			TableColumn column = columns.getColumn(i);
			int columnIndex = column.getModelIndex();
			int width = -1;

			TableCellRenderer h = column.getHeaderRenderer();

			if (h == null) {
				h = defaultHeaderRenderer;
			}

			if (h != null) // Not explicitly impossible
			{
				Component c = h.getTableCellRendererComponent(table, column
						.getHeaderValue(), false, false, -1, i);

				width = c.getPreferredSize().width;
			}

			for (int row = rowCount - 1; row >= 0; --row) {
				TableCellRenderer r = table.getCellRenderer(row, i);
				Component c = r.getTableCellRendererComponent(table, data
						.getValueAt(row, columnIndex), false, false, row, i);

				width = Math.max(width, c.getPreferredSize().width);
			}

			if (width >= 0) {
				column.setPreferredWidth(width + margin);
			} else {
				column.setPreferredWidth(25); // Reasonable default
			}

			totalWidth += column.getPreferredWidth();
		}
	}
	
	/**
	 * Get Alias Panel
	 * 
	 * @param localeUtil Locale Utility
	 * 
	 * @return JPanel
	 */
	public static JPanel getAliasPanel(LocaleUtil localeUtil, JTextField aliasField ) {
		JPanel aliasPanel = new JPanel();
		aliasPanel.setLayout(new GridLayout(2, 1, 5, 5));
		aliasPanel.add(new JLabel(localeUtil.getString("new.alias")));
		aliasPanel.add(aliasField);
		return aliasPanel;
	}
}
