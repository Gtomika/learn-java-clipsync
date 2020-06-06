package com.gaspar.clipsync.network;

import java.io.IOException;

import com.gaspar.clipsync.bluetooth.BluetoothManager;

/**
 * This class handles the network server, similarly to {@link BluetoothManager}, but for network mode.
 * @author G�sp�r Tam�s
 */
public class NetworkManager {
	/**
	 * Singleton instance.
	 */
	private static NetworkManager instance;
	/**
	 * The background thread that accepts connections.
	 */
	private NetworkServer server;
	/**
	 * Private contructor.
	 */
	private NetworkManager() {}
	/**
	 * Access (and create, if needed) the instance.
	 * @return The instance.
	 */
	public static synchronized NetworkManager instance() {
		if(instance == null) instance = new NetworkManager();
		return instance;
	}
	
	/**
	 * Starts the thread listening for incoming data on the local network.
	 * @throws IOException If another program is using the port of app's port number.
	 */
	public void startServer() throws IOException {
		if(server != null && server.isAlive()) throw new RuntimeException("Server already running");
		System.out.println("Starting network server...");
		server = new NetworkServer();
		server.start();
	}
	
	/**
	 * Terminates the server thread.
	 */
	public void stopServer() {
		System.out.println("Stopping network server...");
		if(server != null) server.kill();
	}
}