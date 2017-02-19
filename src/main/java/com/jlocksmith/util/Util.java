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

/**
 * Utility Class
 * 
 * @author Derek Helbert
 */
public class Util {

	/**
	 * Get User Country Code
	 * 
	 * @return String
	 */
	public static String getCountry() {
		return System.getProperty("user.country");
	}
	
	/**
	 * Get System File Separator
	 * 
	 * @return String
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	/**
	 * Get User Home
	 * 
	 * @return String
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * Get User Name
	 * 
	 * @return String
	 */
	public static String getUserName() {
		return System.getProperty("user.name");
	}

	/**
	 * Get User Language Code
	 * 
	 * @return String
	 */
	public static String getLangauge() {
		return System.getProperty("user.language");
	}
}
