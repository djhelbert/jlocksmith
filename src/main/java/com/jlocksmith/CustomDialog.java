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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.jlocksmith.util.LocaleUtil;

/**
 * Custom Dialog
 * 
 * @author Derek Helbert
 */
public class CustomDialog extends JDialog implements ActionListener {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -965395911341728535L;

	/** Button Panel */
	private JPanel buttonPanel = new JPanel();

	/** OK Button */
	private JButton okButton = new JButton();

	/** Cancel Button */
	private JButton cancelButton = new JButton();

	/** Locale Util */
	private LocaleUtil localeUtil = LocaleUtil.getInstance();

	/** Validate Form */
	private ValidatedForm form = null;

	/**
	 * Constructor
	 * 
	 * @param frame Parent Frame
	 * @param content Content Panel
	 */
	public CustomDialog(Frame frame, JPanel content) {
		this(frame, null, content);
	}

	/**
	 * Constructor
	 * 
	 * @param frame Parent Frame
	 * @param form Validated Form
	 * @param content Content Panel
	 */
	public CustomDialog(Frame frame, ValidatedForm form, JPanel content) {
		super(frame, true);
		this.form = form;
		okButton.setText(localeUtil.getString("ok"));
		cancelButton.setText(localeUtil.getString("cancel"));
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, content);
		add(BorderLayout.SOUTH, buttonPanel);
		pack();
		setVisible(true);
	}

	/**
	 * OK Action
	 * 
	 */
	private void okAction() {
		if (form == null) {
			setVisible(false);
		} else {
			if (form.isFormValid()) {
				setVisible(false);
			}
		}
	}

	/**
	 * Cancel Action
	 * 
	 */
	private void cancelAction() {
		setVisible(false);
	}

	/**
	 * Action Performed
	 * 
	 * @param e Event
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			okAction();
		} else {
			cancelAction();
		}
	}

}
