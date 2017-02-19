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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Table Header Listener
 *
 * @author Derek Helbert
 */
public class TableHeaderListener extends MouseAdapter
{
	/** Table */
	private JTable table = null;
	
	/** Table Model */
	private SortableTableModel model = null;
	
	/** Last Column */
	private int lastColumn = -1;
	
	/** Direction 0 == down, 1 == up  */
	private int direction = 0;

	
	/**
	 * Constructor for class TableHeaderListener.
	 *
	 * @param table JTable
	 * @param model DicRowTableModel
	 * @param config Module Configuration
	 */
	public TableHeaderListener( JTable table, SortableTableModel model  )
	{
		// Set table and model
		this.table = table;
		this.model = model;
		
		// Disbale reordering of table columns
		table.getTableHeader().setReorderingAllowed(false);
		
		// Set header renderer
		table.getTableHeader().setDefaultRenderer(new SortRenderer(this));
	}
	
	/**
	 * Get Direction Flag
	 *
	 * @return boolean
	 */
	public boolean isDirectionUp()
	{
		return direction == 1 ? true : false;
	}
	
	/**
	 * Reset Column Sorted and Direction
	 */
	public void reset()
	{
		// Set Last Column
		lastColumn = -1;
		
		// Set direction
		direction = 0;
		
		// Repaint header
		table.getTableHeader().resizeAndRepaint();
	}
	
	/**
	 * Swap Direction
	 */
	private void swapDirection()
	{
		if( direction == 0 ) {
			direction = 1;
		}
		else {
			direction = 0;
		}
	}
	
	/**
	 * Get Last Column Sorted
	 *
	 * @return int
	 */
	public int getLastColumn()
	{
		return lastColumn;
	}
	
	/**
	 * Mouse Clicked
	 *
	 * @param event Mouse Event
	 */
	public void mouseClicked(MouseEvent event)
	{
		try {
			table.setCursor(new Cursor(java.awt.Cursor.WAIT_CURSOR));
			
			// Get selected row
			int selected = table.getSelectedRow();
			
			// Find column of click and
			int tableColumn = table.columnAtPoint(event.getPoint());
			
			// Translate to table model index and sort
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			
			// If same swap
			if( modelColumn == lastColumn )
			{
				swapDirection();
			}
			
			// Set last col
			lastColumn = tableColumn;
			
			// Sort
			model.sort(modelColumn, isDirectionUp());
			
			if( selected != -1 )
			{
				table.setRowSelectionInterval(selected, selected);
			}
		}
		catch( Exception err )
		{
			err.printStackTrace();
		}
		finally {
			// Repaint header
			table.getTableHeader().resizeAndRepaint();
			
			// Reset cursor
			table.setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		}
	}
}