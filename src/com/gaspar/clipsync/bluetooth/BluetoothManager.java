package com.gaspar.clipsync.bluetooth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.bluetooth.BluetoothStateException;

import com.intel.bluetooth.BlueCoveImpl;
import com.intel.bluetooth.BluetoothStack;

public class BluetoothManager {

	private static BluetoothManager instance;
	
	/**
	 * This executor service runs the server process in the background.
	 */
	private ExecutorService worker;
	
	private BluetoothManager() {
		worker = Executors.newSingleThreadExecutor();
	}
	
	public static synchronized BluetoothManager instance() {
		if(instance == null) instance = new BluetoothManager();
		return instance;
	}
	
	/**
	 * Creates the background thread that listens to incoming bluetooth connections from the android app.
	 * @return True if the initialization was successful, false otherwise.
	 */
	public void createServer() throws BluetoothStateException {
		worker.execute(new BluetoothServer());
	}
	
	/**
	 * Stops the background server process.
	 */
	public void stopServer() {
		worker.shutdownNow();
	}
}
