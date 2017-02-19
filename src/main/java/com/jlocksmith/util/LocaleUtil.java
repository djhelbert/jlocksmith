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

import java.util.*;

/**
 * Locale Util
 * 
 * @author Derek Helbert
 */
public class LocaleUtil {
	
	/** Resource Bundle */
	private ResourceBundle bundle = null;

	/** Locale Util */
	private static LocaleUtil localeUtil;
	
	/**
	 * Private Constructor
	 *
	 */
	private LocaleUtil() {
		setLocale( Util.getLangauge(), Util.getCountry() );
	}
	
	/**
	 * Get Instance 
	 * 
	 * @return LocalUtil
	 */
	public static LocaleUtil getInstance() {
		if( localeUtil == null ) {
			LocaleUtil localeUtil = new LocaleUtil();
			return localeUtil;
		}
		else {
			return localeUtil;
		}
	}
	
	/**
	 * Get String from resource bundle.
	 *
	 * @param name String Name
	 */
	public String getString( String name )
	{
		String temp = null;
		
		try {
			temp = bundle.getString(name);
		}
		catch(Exception err) {
			err.printStackTrace();
		}
		
		return temp == null ? "?": temp;
	}
	
	/**
	 * Setter for property language.
	 *
	 * @param lang Language Examples en, fr
	 * @param country Country Code Exmaples US, FR
	 */
	private void setLocale( String lang, String country)
	{
		bundle = ResourceBundle.getBundle("lang.LangBundle",new Locale(lang,country));
	}
	
}
