package com.gaspar.clipsync.bluetooth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.bluetooth.BluetoothStateException;
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
	
	private static final String serverURL = "btspp://localhost:" + serverUUID.toString() + ";name=LearnJavaServer";
	
	/**
	 * This represents the local bluetooth device.
	 */
	@SuppressWarnings("unused") //unused for now
	private LocalDevice localDevice;
	
	public BluetoothServer() throws BluetoothStateException {
		this.localDevice = LocalDevice.getLocalDevice();
	}
	
	@Override
	public void run() {
		try {
			StreamConnectionNotifier service = (StreamConnectionNotifier) Connector.open(serverURL);
			while(true) { //go as long as not terminated
				StreamConnection connection = service.acceptAndOpen(); //blocks until client connects
				try(InputStream inputStream = connection.openInputStream()) {
					String input = new BufferedReader(new InputStreamReader(inputStream))
							  .lines().collect(Collectors.joining("\n"));
					Utils.setclipboard(input); //set clipboard
				}
				try(OutputStream outputStream = connection.openOutputStream()) { //send confirmation back
					outputStream.write("received".getBytes(StandardCharsets.UTF_8));
				}
				connection.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
