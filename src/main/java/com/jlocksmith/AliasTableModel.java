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

import java.security.cert.Certificate;
import java.util.List;

import com.jlocksmith.util.LocaleUtil;

/**
 * Table Model For Key Store Aliases
 * 
 * @author Derek Helbert
 */
public class AliasTableModel extends SortableTableModel implements KeystoreListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4400217235184819844L;

	/** Aliases */
	List<String> aliasList = null;

	/** Key Store Manager */
	KeystoreManager manager = KeystoreManager.getInstance();

	/** Locale Utility */
	LocaleUtil localeUtil = LocaleUtil.getInstance();

	/**
	 * Constructor
	 * 
	 */
	public AliasTableModel() {
		manager.addKeystoreListener(this);
	}

	/**
	 * Load List
	 * 
	 */
	private void init() {
		try {
			aliasList = manager.getAliases();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * Get Column Name
	 * 
	 * @param col Column Index
	 * 
	 * @return String
	 */
	public String getColumnName(int col) {
		if (col == 0) {
			return localeUtil.getString("alias");
		} else if (col == 1) {
			return localeUtil.getString("datecreated");
		} else {
			return localeUtil.getString("type");
		}
	}

	/**
	 * Is Cell Editable
	 * 
	 * @param row Row Number
	 * @param col Column Number
	 * 
	 * @return boolean
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	/**
	 * Certificate Imported
	 * 
	 * @param evt
	 */
	public void entryImported(KeystoreEvent evt) {
		init();
		sort(0,true);
		fireTableRowsInserted(getRowCount(), getRowCount());
	}

	/**
	 * Entry Deleted
	 * 
	 * @param evt
	 */
	public void entryDeleted(KeystoreEvent evt) {
		init();
		sort(0,true);
		fireTableDataChanged();
	}

	/**
	 * Key Store Loaded
	 * 
	 * @param evt
	 */
	public void keyStoreLoaded(KeystoreEvent evt) {
		init();
		sort(0,true);
		fireTableDataChanged();
	}

	/**
	 * Get Column Count
	 * 
	 * @return int
	 */
	public int getColumnCount() {
		return 3;
	}

	/**
	 * Get Row Count
	 * 
	 * @return int
	 */
	public int getRowCount() {
		return aliasList == null ? 0 : aliasList.size();
	}

	/**
	 * Get Alias 
	 * @param rowIndex
	 * @return String
	 */
	public String getAlias(int rowIndex ) {
		return getValueAt(rowIndex, 0).toString();
	}
	
	/**
	 * Get Value
	 * 
	 * @param rowIndex
	 * @param columnIndex
	 * 
	 * @return Object
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		String alias = aliasList.get(rowIndex).toString();
		int size = 0;

		try {
			Certificate[] chain = manager.getCertificateChain(alias);

			if (chain != null) {
				size = chain.length;
			}
		} catch (Exception err) {
			err.printStackTrace();
		}

		if (columnIndex == 0) {
			return alias;
		}
		if (columnIndex == 1) {
			return manager.getCreationDate(alias);
		} else {
			if (manager.isCertificateEntry(alias)) {
				return localeUtil.getString("certificate");
			} else if (manager.isKeyEntry(alias) && size > 0) {
				return localeUtil.getString("keypair");
			} else {
				return localeUtil.getString("key");
			}
		}
	}

	/**
	 * Swap Rows
	 * 
	 * @param row1
	 * @param row2
	 */
	public void swapRows(int row1, int row2) {
		String temp = aliasList.get(row1);
		aliasList.set(row1, aliasList.get(row2));
		aliasList.set(row2, temp);
	}

}
