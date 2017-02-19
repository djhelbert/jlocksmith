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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jlocksmith.util.UiUtil;

/**
 * Sort Renderer
 *
 * @author Derek Helbert
 */
public class SortRenderer extends DefaultTableCellRenderer
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4818476334242282850L;

	/** Table Header Listener */
	private TableHeaderListener thl = null;
	
	/** Default Table */
	private JTable defTable = new JTable();
	
	/** UI Utility */
	private UiUtil uiUtil = new UiUtil();
	
	/**
	 * Constructor for class SortRenderer.
	 *
	 * @param thl    Table Header Listener
	 * @param config ModuleConfig
	 */
	public SortRenderer(TableHeaderListener thl)
	{
		super();
		
		// Set header listener and module config
		this.thl = thl;
	}
	
	/**
	 * Get Table Cell Renderer. Assumes that uparrow.gif and downarrow.gif
	 * images are available in images.
	 *
	 * @param table    Table
	 * @param value    Value
	 * @param selected Selected Flag
	 * @param focus    Focus Flag
	 * @param row      Row Number
	 * @param col      Column Number
	 *
	 * @return Component
	 */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int col)
	{
		JLabel c = (JLabel)defTable.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, selected, focus, row, col);
		
		// Set icon based on direction
		if ( col == thl.getLastColumn() && thl.isDirectionUp() )
			c.setIcon(uiUtil.getImageIcon("uparrow.gif"));
		else if ( col == thl.getLastColumn() && !thl.isDirectionUp() )
			c.setIcon(uiUtil.getImageIcon("downarrow.gif"));
		else
			c.setIcon(null);
		
		// Return header
		return c;
	}
}
