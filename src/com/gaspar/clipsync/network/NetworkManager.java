package com.gaspar.clipsync.network;

import java.io.IOException;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Lang;
import com.gaspar.clipsync.bluetooth.BluetoothManager;

/**
 * This class handles the network server, similarly to {@link BluetoothManager}, but for network mode.
 * @author Gáspár Tamás
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
		if(server != null && server.isAlive()) throw new RuntimeException(Lang.getTranslation("server_running"));
		ClipSyncMain.logMessage(Lang.getTranslation("network_starting"));
		server = new NetworkServer();
		server.start();
	}
	
	/**
	 * Terminates the server thread.
	 */
	public void stopServer() {
		ClipSyncMain.logMessage(Lang.getTranslation("network_stopping"));
		if(server != null) server.kill();
	}
}
