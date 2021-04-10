package com.gaspar.clipsync;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import com.gaspar.clipsync.bluetooth.BluetoothPaneController;
import com.gaspar.clipsync.network.NetworkPaneController;

/**
 * Acts as the controller for the mode selector panel.
 * @author Gáspár Tamás
 */
public class SelectorPaneController {
	
	public static final String SELECTOR_PANEL_ID = "selector_panel";
	
	private static final String BLUETOOTH_TEXT = "Bluetooth mode";
	
	private static final String NETWORK_TEXT = "Local network mode";
	
	/**
	 * Contains the radio buttons which allow for the selection of a clip sync mode.
	 */
	private static ButtonGroup group;
	
	/**
	 * @return A panel that contains tools for the user to select a clip sync mode. This is 
	 * added to the global card layout.
	 */
	public static JPanel buildSelectorPanel() {
		JPanel selectorPanel = new JPanel();
		selectorPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		selectorPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.Y_AXIS));
		
		//add title
		JLabel title = new JLabel();
		title.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		title.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		title.setText("Select a ClipSync mode!");
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		selectorPanel.add(title);
		
		//add radio buttons
		group = new ButtonGroup();
		JPanel buttonPanel = new JPanel();
		buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		buttonPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		buttonPanel.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
		buttonPanel.setLayout(buttonLayout);
		
		JRadioButton bluetoothButton = new JRadioButton();
		bluetoothButton.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		bluetoothButton.setText(BLUETOOTH_TEXT);
		bluetoothButton.setSelected(true); //selected by default
		buttonPanel.add(bluetoothButton);
		group.add(bluetoothButton);
		
		JRadioButton networkButton = new JRadioButton();
		networkButton.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		networkButton.setText(NETWORK_TEXT);
		buttonPanel.add(networkButton);
		group.add(networkButton);
		selectorPanel.add(buttonPanel);
		
		//add ok button
		JButton button = new JButton();
		button.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		button.setMargin(new Insets(20, 20, 20, 20));
		button.setMaximumSize(new Dimension(100,30));
		button.setText("SELECT");
		button.addActionListener(SelectorPaneController::selectButtonPressed);
		selectorPanel.add(button);
		
		return selectorPanel;
	}
	
	/**
	 * This method is called when the user selects a preferred mode.
	 * @param event
	 */
	private static void selectButtonPressed(ActionEvent event)  {
		Enumeration<AbstractButton> buttons = group.getElements();
		AbstractButton button = null;
		while(buttons.hasMoreElements()) {
			button = buttons.nextElement();
			if(button.isSelected()) {
				//this is selected
				if(button.getText().equals(BLUETOOTH_TEXT)) {
					BluetoothPaneController.showBluetoothPane();
				} else if(button.getText().equals(NETWORK_TEXT)) {
					NetworkPaneController.showNetworkPane();
				}
				return;
			}
		}
	}
	
	/**
	 * Shows the selector panel where the user can select from bluetooth and network mode.
	 */
	public static void showSelectorPanel() {
		ClipSyncMain.getCardLayout().show(ClipSyncMain.getCardPanel(), SELECTOR_PANEL_ID);
		ClipSyncMain.logMessage("Showing selector screen...");
		ClipSyncMain.getFrame().pack();
	}
}
