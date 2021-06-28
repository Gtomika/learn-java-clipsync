package com.gaspar.clipsync.bluetooth;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Lang;

/**
 * The class responsible for managing the bluetooth server.
 * @author Gáspár Tamás
 */
public class BluetoothManager {
	/**
	 * Singleton instance.
	 */
	private static BluetoothManager instance;
	/**
	 * The background thread that accepts connections.
	 */
	private BluetoothServer server;
	/**
	 * Represents the the bluetooth adapter of this device.
	 */
	private LocalDevice localDevice;
	/**
	 * Private contructor.
	 */
	private BluetoothManager() {}
	/**
	 * Access (and create, if needed) the instance.
	 * @return The instance.
	 */
	public static synchronized BluetoothManager instance() {
		if(instance == null) instance = new BluetoothManager();
		return instance;
	}
	
	/**
	 * Creates and starts the background thread that listens to incoming bluetooth connections from the android app.
	 * @throws BluetoothStateException If bluetooth is not available or the device could not be made discoverable.
	 */
	public void startServer() throws BluetoothStateException {
		if(localDevice == null) { //check availability, only once
			this.localDevice = LocalDevice.getLocalDevice(); //checks bluetooth
			boolean isDiscoverable = localDevice.setDiscoverable(DiscoveryAgent.GIAC);
			if(!isDiscoverable) {
				throw new BluetoothStateException(Lang.getTranslation("bluetooth_fail_disc"));
			}
		}
		if(server != null && server.isAlive()) throw new RuntimeException(Lang.getTranslation("server_running"));
		ClipSyncMain.logMessage(Lang.getTranslation("bluetooth_starting"));
		
		server = new BluetoothServer();
		server.start();
	}
	
	/**
	 * Stops the background server process.
	 */
	public void stopServer() {
		ClipSyncMain.logMessage(Lang.getTranslation("bluetooth_stopping"));
		if(server != null) {
			server.kill();
		}
	}
}
