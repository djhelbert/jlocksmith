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

import java.io.File;
import java.util.*;

/**
 * Custom File Filter
 * 
 * @author Derek Helbert
 */
public class CustomFileFilter extends javax.swing.filechooser.FileFilter {

	/** Map */
	private Map<String,String> map = new HashMap<String,String>();

	/** Description */
	private String desc;

	/**
	 * Constructor
	 * 
	 * @param e Extension eg. xml html doc
	 * @param d Description File Description
	 */
	public CustomFileFilter(String e, String d) {
		super();
		
		map.put(e, d);
	}

	/**
	 * Constructor
	 * 
	 * @param map Extension Map
	 * @param d Description
	 */
	public CustomFileFilter(Map<String,String> map, String d) {
		super();
		this.desc = d;
		this.map = map;
	}

	/**
	 * Accept File
	 * 
	 * @param f File
	 */
	public boolean accept(File f) {
		if( f.isDirectory() ) {
			return true;
		}
		
		String name = f.getName();

		if (name.lastIndexOf('.') != -1) {
			String ext = name.substring(name.lastIndexOf('.'), name.length());

			if (map.get(ext) != null) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Get File Description
	 * 
	 * @return String
	 */
	public String getDescription() {
		String ext = "";

		Set<String> set = map.keySet();
		
		Object[] items = set.toArray();

		for (int i = 0; i < items.length; i++) {
			ext += (i == 0 ? "" : ",") + "*" + items[i].toString();
		}

		return desc + " (" + ext + ")";
	}

}
