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

import javax.swing.table.*;

/**
 * Makes Table Model Sortable
 * 
 * @author Derek Helbert
 */
public abstract class SortableTableModel extends AbstractTableModel {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6256581706844375146L;

	/**
	 * Swap Rows
	 * 
	 * @param row
	 */
	public abstract void swapRows(int row1, int row2);
	
	/**
	 * Sort Rows Based on Selected Column. All values in column must implement
	 * Comparable interface.
	 *
	 * @param col       Column Number
	 * @param ascending Ascending Flag
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sort(int col, boolean ascending)
	{
		int n = getRowCount();
		
		for (int i = 0; i < n - 1; i++)
		{
			for (int j = 0; j < n - 1 - i; j++)
			{
				if( ascending )
				{
					if( ((Comparable)getValueAt(j + 1,col)).compareTo(getValueAt(j,col)) < 0 )
					{
						swapRows(j,j+1);
					}
				}
				else
				{
					if( ((Comparable)getValueAt(j + 1,col)).compareTo(getValueAt(j,col)) > 0 )
					{
						swapRows(j,j+1);
					}
				}
			}
			
			// Redraw table
			fireTableDataChanged();
		}
	}

}
