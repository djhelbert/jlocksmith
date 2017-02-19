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

import java.security.cert.X509Certificate;

import javax.swing.table.AbstractTableModel;

import com.jlocksmith.util.LocaleUtil;

/**
 * X509 Certificate Extension Model
 * 
 * @author Derek Helbert
 */
public class CertExtensionModel extends AbstractTableModel {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7693742675210946808L;

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Critical Extensions OID */
	private Object[] crit = new Object[0];

	/** Non Critical Extensions OID */
	private Object[] noncrit = new Object[0];

	/**
	 * Constructor
	 * 
	 * @param cert X509Certificate
	 */
	public CertExtensionModel(X509Certificate cert) {
		if (cert.getCriticalExtensionOIDs() != null && !cert.getCriticalExtensionOIDs().isEmpty()) {
			crit = cert.getCriticalExtensionOIDs().toArray();
		}

		if (cert.getNonCriticalExtensionOIDs() != null && !cert.getNonCriticalExtensionOIDs().isEmpty()) {
			noncrit = cert.getNonCriticalExtensionOIDs().toArray();
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
	 * Get Row Count
	 * 
	 * @return int
	 */
	public int getRowCount() {
		return crit.length + noncrit.length;
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
			return localeUtil.getString("type");
		} else if (col == 1) {
			return localeUtil.getString("oid");
		} else {
			return localeUtil.getString("name");
		}
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
	 * Get OID
	 * 
	 * @param rowIndex
	 * 
	 * @return Object
	 */
	public String getOID(int rowIndex) {
		return getValueAt(rowIndex, 1).toString();
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
		int test = crit.length - 1;

		if (rowIndex > test) {
			if (columnIndex == 0) {
				return localeUtil.getString("noncritical");
			} else if (columnIndex == 1) {
				return noncrit[rowIndex - crit.length];
			} else {
				return localeUtil.getString(noncrit[rowIndex - crit.length]
						.toString());
			}
		} else {
			if (columnIndex == 0) {
				return localeUtil.getString("critical");
			} else if (columnIndex == 1) {
				return crit[rowIndex];
			} else {
				return localeUtil.getString(crit[rowIndex].toString());
			}
		}
	}
}
