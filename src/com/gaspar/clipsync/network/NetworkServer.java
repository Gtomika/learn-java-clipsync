package com.gaspar.clipsync.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Utils;
import com.gaspar.clipsync.bluetooth.BluetoothServer;

/**
 * This thread is the server for accepting incoming data on the local network. Uses UDP protocol
 * @author Gáspár Tamás
 */
public class NetworkServer extends Thread {
	
	/**
	 * The server sends this back to the app after receiving a message.
	 */
	private static final String CONFIRMATION_MESSAGE = "rcvd";
	
	/**
	 * The port number that the android application is using.
	 */
	public static final int APP_PORT_NUMBER = 24480;
	
	/**
	 * The port that the server will attemp to use. In the highly unlikely 
	 * event of another program already using this port, an exception will be thrown.
	 */
	public static final int SERVER_PORT_NUMBER = 24481;
	
	/**
	 * The socket of the server.
	 */
	private DatagramSocket serverSocket;
	
	/**
	 * Construct the server.
	 * @throws IOException If the port is already in use.
	 */
	public NetworkServer() throws IOException {
		serverSocket = new DatagramSocket(SERVER_PORT_NUMBER, InetAddress.getByName("0.0.0.0"));
		serverSocket.setBroadcast(true);
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) { //go as long as not stopped
			try {
				ClipSyncMain.logMessage("Accepting broadcasts over local network...");
				byte[] buffer = new byte[2048];
				DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
				serverSocket.receive(datagram); //blocks until data received
				ClipSyncMain.logMessage("Received something from " + datagram.getAddress().getHostAddress());
				
				String input = new String(datagram.getData(), 0, datagram.getLength(), StandardCharsets.UTF_8);
				if(datagram.getPort() == APP_PORT_NUMBER && input.endsWith(BluetoothServer.DATA_DELIMITER) ) {
					//this means that it's from the app
					ClipSyncMain.logMessage("Received data from the app!");
					int endIndex = input.length() - BluetoothServer.DATA_DELIMITER.length();
					Utils.setclipboard(input.substring(0, endIndex)); //cut off delimiter 
					ClipSyncMain.logMessage("Copied data to clipboard!");
					//send back a confirmation message
					InetAddress appAddress = datagram.getAddress();
					byte[] responseBuffer = CONFIRMATION_MESSAGE.getBytes(StandardCharsets.UTF_8);
					DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length, appAddress, APP_PORT_NUMBER);
					serverSocket.send(response);
					ClipSyncMain.logMessage("Sent response message to the app!");
				} else {
					ClipSyncMain.logMessage("This message is not from the app.");
				}
			} catch( IOException e) {} //if the socket was closed by thread kill
		}
	}
	
	/**
	 * Terminates the thread.
	 */
	public void kill() {
		interrupt();
		if(serverSocket != null) {
			serverSocket.close(); //receive method will stop with a IOException
		}
	}
}
