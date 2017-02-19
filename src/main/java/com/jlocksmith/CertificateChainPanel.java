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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.security.cert.Certificate;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.jlocksmith.util.LocaleUtil;

/**
 * CertificateChainPanel
 * 
 * @author Derek Helbert
 */
public class CertificateChainPanel extends JPanel {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8652614462586999658L;

	/** Tabbed Pane */
	private JTabbedPane pane = new JTabbedPane();

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();
	
	/**
	 * Constructor
	 * 
	 * @param chain Certificate Chain
	 * @param f Frame
	 */
	public CertificateChainPanel(Certificate[] chain, Frame f) {
		super();
		init(chain,f);
	}

	/**
	 * Init
	 * 
	 * @param chain
	 */
	private void init(Certificate[] chain, Frame f) {
		setLayout( new BorderLayout() );
		
		if (chain != null) {
			for (int i = 0; i < chain.length; i++) {
				pane.add(localeUtil.getString("certificate") + " [" + (i+1) + "]",new CertificateInfoPanel(chain[i],f));
			}
		}
		
		add(BorderLayout.CENTER,pane);
	}
}
