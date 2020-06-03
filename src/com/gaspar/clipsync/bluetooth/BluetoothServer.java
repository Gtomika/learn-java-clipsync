package com.gaspar.clipsync.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.gaspar.clipsync.Utils;

/**
 * A background thread that accepts incoming bluetooth connections.
 * @author Gáspár Tamás
 */
public class BluetoothServer implements Runnable {
	
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
	private static final String HANDSHAKE_MESSAGE = "handshake";
	/**
	 * This marks the end of the input from the client. Should not be used in code samples.
	 */
	private static final String DATA_DELIMITER = "DATA_DELIMITER";
	
	/**
	 * This represents the local bluetooth device.
	 */
	private LocalDevice localDevice;
	
	public BluetoothServer() throws BluetoothStateException {
		this.localDevice = LocalDevice.getLocalDevice();
		boolean isDiscoverable = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
		if(!isDiscoverable) {
			throw new BluetoothStateException("Could not be made discoverable!");
		}
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		StreamConnection connection = null;
		try {
			System.out.println("Starting server...");
			StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(serverURL);
			while(true) { //go as long as not terminated
				System.out.println("Accepting connection from the app...");
				connection = service.acceptAndOpen(); //blocks until client connects
				System.out.println("Accepted client connection from the app!");
				InputStream inputStream = connection.openInputStream();
				Scanner scanner = new Scanner(inputStream, "UTF-8");
				scanner.useDelimiter(DATA_DELIMITER);
				String input = scanner.next();
				System.out.println("Received data from the app!");
				if(!input.equals(HANDSHAKE_MESSAGE)) { //normal data, clear and set to clipboard
					Utils.setclipboard(input); 
					System.out.println("Copied data to clipboard!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					if(connection!=null) connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
