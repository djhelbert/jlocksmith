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

import java.util.Map;

import com.jlocksmith.util.LocaleUtil;

/**
 * Map Table Model
 * 
 * @author Derek Helbert
 */
public class MapTableModel extends SortableTableModel {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7794256208160696811L;

	/** Locale Utility */
	LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Map */
	private Map<Object,Object> map;

	/** Keys */
	private Object[] keys;

	/**
	 * Constructoe
	 * 
	 * @param map Value Map
	 */
	public MapTableModel(Map<Object,Object> map) {
		this.map = map;
		keys = map.keySet().toArray();
	}

	/**
	 * Get Column Count
	 */
	public int getColumnCount() {
		return 2;
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
			return localeUtil.getString("property");
		} else {
			return localeUtil.getString("value");
		}
	}

	/**
	 * Get Row Count
	 */
	public int getRowCount() {
		return keys.length;
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
		Object k = keys[rowIndex];

		if (columnIndex == 0)
			return k;
		else
			return map.get(k);
	}

	/**
	 * Swap Rows
	 * 
	 * @param row1
	 * @param row2
	 */
	public void swapRows(int row1, int row2) {
		Object temp = keys[row1];
		
		keys[row1] = keys[row2];
		keys[row2] = temp;
	}
}
