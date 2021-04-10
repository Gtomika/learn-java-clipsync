package com.gaspar.clipsync;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.gaspar.clipsync.bluetooth.BluetoothManager;
import com.gaspar.clipsync.bluetooth.BluetoothPaneController;
import com.gaspar.clipsync.network.NetworkManager;
import com.gaspar.clipsync.network.NetworkPaneController;

/**
 * The main class for the ClipSync app.
 * @author Gáspár Tamás
 */
public class ClipSyncMain {
	
	public static final Color LEARN_JAVA_COLOR = new Color(255, 145, 0);

	private static final String ERROR_PANEL_ID = "error_panel";
	
	/**
	 * Shows one of the clip sync modes or a selector screen.
	 */
	private static JPanel cardPanel;
	
	/**
	 * Manages the card panel content.
	 */
	private static CardLayout cardLayout;
	
	/**
	 * Shows an error message to the user.
	 */
	private static JTextArea errorLogger;
	
	/**
	 * Window.
	 */
	private static JFrame frame;
	
	/**
	 * Console like text are that displays logs.
	 */
	private static JTextArea console;
	
	private static JScrollPane scroller;
	
	/**
	 * Main. Creates the window.
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new JFrame();
		//set title and icon
		frame.setTitle("Learn Java ClipSync");
		frame.setIconImage(new ImageIcon(ClipSyncMain.class.getResource("/resources/icon.png")).getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		//frame.setSize(new Dimension(300, 300));
		
		//add UI components
		setUpUI(frame);
		//show preferred clipsync
		setPreferredModePane();
		
		//set listeners
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				BluetoothManager.instance().stopServer(); //stop bluetooth server, if running
				NetworkManager.instance().stopServer(); //stop network server, if running
			}
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		
		//make visible
		logMessage("Learn java ClipSync server ready!");
		frame.setVisible(true);
	}
	
	/**
	 * Initializes the layout used by the app.
	 * @param frame The window.
	 */
	private static void setUpUI(final JFrame frame) {
		JPanel root = new JPanel();
		BorderLayout rootLayout = new BorderLayout();
		root.setLayout(rootLayout);
		
		cardPanel = new JPanel();
		cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		
		//add panels
		cardPanel.add(SelectorPaneController.buildSelectorPanel(), SelectorPaneController.SELECTOR_PANEL_ID);
		cardPanel.add(BluetoothPaneController.buildBluetoothPanel(), BluetoothPaneController.BLUEOTOOTH_PANEL_ID);
		cardPanel.add(NetworkPaneController.buildNetworkPanel(), NetworkPaneController.NETWORK_PANEL_ID);
		cardPanel.add(buildErrorPanel(), ERROR_PANEL_ID);
		
		root.add(cardPanel, BorderLayout.CENTER);
		
		//create and add "console"
		console = new JTextArea();
		console.setEditable(false);
		console.setBackground(Color.BLACK);
		console.setForeground(Color.WHITE);		
		scroller = new JScrollPane(console);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setPreferredSize(new Dimension(350, 200));
		root.add(scroller, BorderLayout.LINE_END);
		
		//add to window
		frame.getContentPane().add(root);
	}
	
	private static JPanel buildErrorPanel() {
		JPanel errorPanel = new JPanel();
		errorPanel.setBackground(LEARN_JAVA_COLOR);
		errorPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
		
		JPanel errorBar = new JPanel();
		errorBar.setBackground(LEARN_JAVA_COLOR);
		errorBar.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		//add text field
		errorLogger = new JTextArea();
		errorLogger.setBackground(LEARN_JAVA_COLOR);
		errorLogger.setEditable(false);
		errorBar.add(errorLogger);
		
		//add error icon
		JLabel errorIcon = new JLabel();
		errorIcon.setIcon(new ImageIcon(ClipSyncMain.class.getResource("/resources/fail.png")));
		errorBar.add(errorIcon);
		
		errorPanel.add(errorBar);
		
		JButton backButton = new JButton();
		backButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		backButton.setMargin(new Insets(20, 20, 20, 20));
		backButton.setText("OK");
		backButton.addActionListener(evt -> {
			SelectorPaneController.showSelectorPanel();
		});
		errorPanel.add(backButton);
		
		return errorPanel;
	}
	
	/**
	 * Makes the mode (bluetooth/network) panel visible, if there is a preferred mode. 
	 * If there isn't, shows a selector screen.
	 */
	private static void setPreferredModePane() {
		Mode prefMode = Utils.getPreferredMode();
		switch (prefMode) {
		case BLUETOOTH:
			BluetoothPaneController.showBluetoothPane();
			break;
		case NETWORK:
			NetworkPaneController.showNetworkPane();
			break;
		default: //not set
			SelectorPaneController.showSelectorPanel();
			break;
		}
	}

	/**
	 * @return The global card panel showing the components.
	 */
	public static JPanel getCardPanel() {
		return cardPanel;
	}
	
	/**
	 * @return The global card layout.
	 */
	public static CardLayout getCardLayout() {
		return cardLayout;
	}
	
	/**
	 * Displays the error screen with the message.
	 * @param error
	 */
	public static void showError(String error) {
		errorLogger.setText(error);
		cardLayout.show(cardPanel, ERROR_PANEL_ID);
		ClipSyncMain.getFrame().pack();
	}
	
	/**
	 * Logs a message the the application "console".
	 * @param message The message
	 */
	public static void logMessage(String message) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		if(console.getText().equals("")) {
			console.setText(timeStamp + " - " + message);
		} else {
			//append
			console.append(System.lineSeparator() + timeStamp + " - " + message);
		}
		//scroll to bottom
		JScrollBar scrollBar = scroller.getVerticalScrollBar();
		scrollBar.setValue(scrollBar.getMaximum());
		frame.pack();
	}
	
	/**
	 * @return Application window.
	 */
	public static JFrame getFrame() {
		return frame;
	}
}
