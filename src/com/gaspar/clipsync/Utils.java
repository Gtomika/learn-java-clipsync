package com.gaspar.clipsync;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Utility method collection that is used by both bluetooth and network ClipSync.
 * @author Gáspár Tamás
 */
public abstract class Utils {
	
	/**
	 * Sets a new value to the clipboard.
	 * @param content The string that will be set.
	 */
	public static void setclipboard(String content) {
		StringSelection selection = new StringSelection(content);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
	}

	/**
	 * File name for the preference storage file.
	 */
	private static final String PREF_FILE_NAME = "learnjava.pref";
	
	/**
	 * Writes the user's preferred clip sync mode to file.
	 * @param mode
	 */
	public static void writePreferredMode(Mode mode) {
		try {
			//current working directory
			String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
			Path path = Paths.get(currentDir + "/" + PREF_FILE_NAME);
			Files.write(path, Arrays.asList(mode.toString()));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the user's preferred clip sync mode from file.
	 * @return The mode.
	 */
	public static Mode getPreferredMode() {
		try {
			//current working directory
			String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
			Path path = Paths.get(currentDir + "/" + PREF_FILE_NAME);
			List<String> lines = Files.readAllLines(path);
			return Mode.valueOf(lines.get(0)); 
		} catch(Exception e) {
			return Mode.NOT_SET;
		}
	}
	
	/**
	 * Writes an exception stack trace to a file.
	 */
	public static void logException(Exception e) {
		File logFile = new File(System.getProperty("user.dir") + "/.log");
		try(PrintWriter writer = new PrintWriter(new FileOutputStream(logFile, true))) {
			e.printStackTrace(writer);
		} catch(Exception exc) {
			e.printStackTrace();
		}
	}
}
