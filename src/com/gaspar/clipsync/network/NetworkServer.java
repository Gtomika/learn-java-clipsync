package com.gaspar.clipsync.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.gaspar.clipsync.Utils;
import com.gaspar.clipsync.bluetooth.BluetoothServer;

/**
 * This thread is the server for accepting incoming data on the local network.
 * @author Gáspár Tamás
 */
public class NetworkServer extends Thread {

	/**
	 * The port that the server will attemp to use. In the highly unlikely 
	 * event of another program already using this port, an exception will be thrown.
	 */
	public static final int PORT_NUMBER = 24480;
	/**
	 * The socket of the server.
	 */
	private ServerSocket serverSocket;
	/**
	 * Construct the server.
	 * @throws IOException If the port is already in use.
	 */
	public NetworkServer() throws IOException {
		serverSocket = new ServerSocket(PORT_NUMBER);
	}
	
	@Override
	public void run() {
		while(!isInterrupted()) { //go as long as not stopped
			System.out.println("Accepting connection over local network...");
			try(Socket client = serverSocket.accept()) { //blocks until connection
				System.out.println("Accepted client connection from the app!");
				try(InputStream inputStream = client.getInputStream()) { //open input stream
					try(Scanner scanner = new Scanner(inputStream, "UTF-8")) { //open scanner
						scanner.useDelimiter(BluetoothServer.DATA_DELIMITER);
						String input = scanner.next();
						System.out.println("Received data from the app!");
						if(!input.equals(BluetoothServer.HANDSHAKE_MESSAGE)) { //normal data, clear and set to clipboard
							Utils.setclipboard(input); 
							System.out.println("Copied data to clipboard!");
						}
					}
				}
			} catch(IOException e) {
				return; //thread was killed most likely
			} 
		}
	}
	
	/**
	 * Terminates the thread.
	 */
	public void kill() {
		interrupt();
		if(serverSocket != null) {
			try {
				serverSocket.close(); //accept method will stop with a SocketException
			} catch (IOException e) {
				e.printStackTrace(); //some kind of closing exception
			}
		}
	}
}
