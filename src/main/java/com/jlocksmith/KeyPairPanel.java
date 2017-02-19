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

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jlocksmith.util.KeyUtil;
import com.jlocksmith.util.LocaleUtil;

/**
 * Key Pair Panel
 * 
 * @author Derek Helbert
 */
public class KeyPairPanel extends JPanel implements ChangeListener, KeyListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8300204892714782637L;

	/** Size Field */
	private JSlider sizeSlider = new JSlider(512,1024,1024);

	/** Locale Utility */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Algorithm ComboBox */
	private JComboBox algCombo = new JComboBox();

	/** Size Label */
	private JLabel sizeLabel = new JLabel("1024 " + localeUtil.getString("bits"));
	
	/** Tick Size */
	private static int ticksize = 64;
	
	/**
	 * Constructor
	 */
	public KeyPairPanel() {
		super();
		init();
	}

	/**
	 * Init
	 * 
	 */
	private void init() {
		setLayout(new GridLayout(2, 2, 5, 5));

		String[] algs = KeyUtil.getKeyAlgorithms();

		for (int i = 0; i < algs.length; i++) {
			algCombo.addItem(algs[i]);
		}

		sizeSlider.setMajorTickSpacing(ticksize);
		sizeSlider.setSnapToTicks(true);
		sizeSlider.addChangeListener(this);
		sizeSlider.addKeyListener(this);
		
		add(new JLabel(localeUtil.getString("algorithm")));
		add(algCombo);
		add(sizeLabel);
		add(sizeSlider);
		
		setBorder(new TitledBorder(localeUtil.getString("keypair")));
	}

	/**
	 * Get Key Size
	 * 
	 * @return String
	 */
	public int getKeySize() {
		return sizeSlider.getValue();
	}

	/**
	 * Get Key Algorithm
	 * 
	 * @return String
	 */
	public String getKeyAlgorithm() {
		return algCombo.getSelectedItem().toString();
	}

	/**
	 * State Changed
	 */
	public void stateChanged(ChangeEvent e) {
		sizeLabel.setText(sizeSlider.getValue()+ " " + localeUtil.getString("bits"));
	}

	/**
	 * Key 
	 * 
	 * @param e Key Event
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * Key 
	 * 
	 * @param e Key Event
	 */
	public void keyReleased(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_LEFT ) {
			if( sizeSlider.getValue() > 512) {
				sizeSlider.setValue( sizeSlider.getValue() - ticksize);
			}
		}
		else if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			if( sizeSlider.getValue() < 1024) {
				sizeSlider.setValue( sizeSlider.getValue() + ticksize);
			}		
		}
	}

	/**
	 * Key 
	 * 
	 * @param e Key Event
	 */
	public void keyTyped(KeyEvent e) {
	}

}
