package com.gaspar.clipsync.network;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
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
 * This is the controller for the network clip sync mode.
 * @author Gáspár Tamás
 */
public class NetworkPaneController {
	
	public static final String NETWORK_PANEL_ID = "network_panel";
	
	public static JPanel buildNetworkPanel() {
		JPanel networkPanel = new JPanel();
		networkPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		networkPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		networkPanel.setLayout(new BoxLayout(networkPanel, BoxLayout.Y_AXIS));
		
		JPanel networkBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		networkBar.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		
		JLabel label = new JLabel(Lang.getTranslation("network_active"));
		label.setBorder(new EmptyBorder(new Insets(20, 20, 20, 20)));
		networkBar.add(label);
		
		JLabel icon = new JLabel();
		icon.setIcon(new ImageIcon(ClipSyncMain.class.getResource("/resources/success.png")));
		networkBar.add(icon);
		
		networkPanel.add(networkBar);
		
		JButton backButton = new JButton();
		backButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		backButton.setMargin(new Insets(20, 20, 20, 20));
		backButton.setMaximumSize(new Dimension(100,30));
		backButton.setText(Lang.getTranslation("back"));
		backButton.addActionListener(NetworkPaneController::backButtonPressed);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ClipSyncMain.LEARN_JAVA_COLOR);
		buttonPanel.add(backButton);
		networkPanel.add(buttonPanel);
		//networkPanel.add(backButton);
		
		return networkPanel;
	}
	
	/**
	 * Sets the central pane to the bluetooth pane. Starts the network server.
	 */
	public static void showNetworkPane() {
		try {
			NetworkManager.instance().startServer();
			//server is up
			ClipSyncMain.getCardLayout().show(ClipSyncMain.getCardPanel(), NETWORK_PANEL_ID);
			ClipSyncMain.getFrame().pack();
		} catch (Exception e) {
			ClipSyncMain.showError(Lang.getTranslation("network_fail"));
			ClipSyncMain.logMessage("Network error: " + e.getMessage());
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
			NetworkManager.instance().stopServer(); //stop server
			Utils.writePreferredMode(Mode.NOT_SET); //forget preference
			SelectorPaneController.showSelectorPanel(); //show main menu
		}
	}
}
