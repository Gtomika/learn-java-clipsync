package com.gaspar.clipsync.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Lang;
import com.gaspar.clipsync.Utils;

/**
 * A background thread that accepts incoming bluetooth connections.
 * @author Gáspár Tamás
 */
public class BluetoothServer extends Thread {
	
	/**
	 * The unique identifier for the bluetooth service.
	 */
	private static final UUID serverUUID = new UUID("2e33e9bcc2834347b642bfa1f48cdf72", false);
	/**
	 * URL on which the server is found.
	 */
	private static final String serverURL = "btspp://localhost:" + serverUUID.toString() + ";name=LearnJavaServer";
	/**
	 * A message that is just for initiating connection, and pairing. Is not copied to clipboard.
	 */
	public static final String HANDSHAKE_MESSAGE = "handshake";
	/**
	 * This marks the end of the input from the client. Should not be used in code samples.
	 */
	public static final String DATA_DELIMITER = "DATA_DELIMITER";
	/**
	 * The serivce broadcasted by this device.
	 */
	private StreamConnectionNotifier service;
	/**
	 * The server loop. Use the kill method to stop the server.
	 */
	@Override
	public void run() {
		StreamConnection connection = null;
		try {
			service = (StreamConnectionNotifier) Connector.open(serverURL);
			while(!isInterrupted()) { //go as long as not terminated
				ClipSyncMain.logMessage(Lang.getTranslation("bluetooth_accepting"));
				connection = service.acceptAndOpen(); //blocks until client connects
				ClipSyncMain.logMessage(Lang.getTranslation("bluetooth_accepted"));
				try(InputStream inputStream = connection.openInputStream()) { //open input stream
					try(Scanner scanner = new Scanner(inputStream, "UTF-8")) { //open scanner
						scanner.useDelimiter(DATA_DELIMITER);
						String input = scanner.next();
						ClipSyncMain.logMessage(Lang.getTranslation("data_received"));
						if(!input.equals(HANDSHAKE_MESSAGE)) { //normal data, clear and set to clipboard
							Utils.setclipboard(input); 
							ClipSyncMain.logMessage(Lang.getTranslation("data_copied"));
						}
					}
				}
			}
		} catch (IOException e) {
			return; //this means that the thread was stopped using the kill method
		} finally {
				try {
					if(connection!=null) {
						connection.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Stops the server from accepting requests.
	 */
	public void kill() {
		interrupt(); 
		try {
			if(service != null) { //this will cause the acceptAndOpen method to stop waiting with IOException
				service.close();
			}
		} catch(Exception e) {
			e.printStackTrace(); //some kind of closing exception
		}
	}
}
