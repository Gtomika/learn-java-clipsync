package com.gaspar.clipsync.bluetooth;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Lang;
import com.gaspar.clipsync.Mode;
import com.gaspar.clipsync.SelectorPaneController;
import com.gaspar.clipsync.Utils;



/**
 * The controller for the pane that displays when the user selected bluetooth mode.
 * @author Gáspár Tamás
 */
public class BluetoothPaneController {
	
	public static final String BLUEOTOOTH_PANEL_ID = "bluetooth_panel";
	
	public static JPanel buildBluetoothPanel() {
		JPanel bluetoothPanel = new JPanel();
		bluetoothPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		bluetoothPanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		bluetoothPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		bluetoothPanel.setLayout(new BoxLayout(bluetoothPanel, BoxLayout.Y_AXIS));
		
		JPanel bluetoothBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		bluetoothBar.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		
		JLabel label = new JLabel(Lang.getTranslation("bluetooth_active"));
		label.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		bluetoothBar.add(label);
		
		JLabel icon = new JLabel();
		icon.setIcon(new ImageIcon(ClipSyncMain.class.getResource("/resources/success.png")));
		bluetoothBar.add(icon);
		
		bluetoothPanel.add(bluetoothBar);
		
		JButton backButton = new JButton();
		backButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		backButton.setMargin(new Insets(20, 20, 20, 20));
		backButton.setMaximumSize(new Dimension(100,30));
		backButton.setText(Lang.getTranslation("back"));
		backButton.addActionListener(BluetoothPaneController::backButtonPressed);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		buttonPanel.add(backButton);
		bluetoothPanel.add(buttonPanel);
		//bluetoothPanel.add(backButton);
		
		return bluetoothPanel;
	}
	
	/**
	 * Sets the central pane to the bluetooth pane. Attempts to start the bluetooth server.
	 * @throws IOException On FXML load error.
	 */
	public static void showBluetoothPane() {
		try {
			BluetoothManager.instance().startServer();
			//bluetooth started
			ClipSyncMain.getCardLayout().show(ClipSyncMain.getCardPanel(), BLUEOTOOTH_PANEL_ID);
			ClipSyncMain.getFrame().pack();
		} catch(BluetoothStateException e) {
			ClipSyncMain.showError(Lang.getTranslation("bluetooth_fail"));
			ClipSyncMain.logMessage("Bluetooth error: " + e.getMessage());
		}
	}
	
	/**
	 * This method is called when the user selects the back button (the mode select button).
	 * It will make the app "forget" the preferred mode and show the selection screen.
	 * @param event
	 */
	private static void backButtonPressed(ActionEvent event) {
		int res = JOptionPane.showConfirmDialog(ClipSyncMain.getFrame(), Lang.getTranslation("back_confirm"));
		if(res == JOptionPane.YES_OPTION) {
			BluetoothManager.instance().stopServer(); //stop server
			Utils.writePreferredMode(Mode.NOT_SET); //forget preference
			SelectorPaneController.showSelectorPanel(); //show main menu
		}
	}
	
}
