package com.gaspar.clipsync;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;

import com.gaspar.clipsync.bluetooth.BluetoothPaneController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main class for the JavaFx app.
 * @author G�sp�r Tam�s
 */
public class ClipSyncMain extends Application {

	/**
	 * The root pane of the application
	 */
	private static BorderPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getResource("/resources/RootLayout.fxml"));
			final Scene scene = new Scene(root,400,200);
			scene.getStylesheets().add(getClass().getResource("/resources/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			//set app icon
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icon.png")));
			primaryStage.setTitle("Learn Java ClipSync");
			primaryStage.show();
			
			setPreferredModePane(); //initialize the screen
		} catch(BluetoothStateException e) { //errors related to bluetooth
			//TODO
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes the mode (bluetooth/network) pane visible, if there is a preferred mode. 
	 * If there isn't, shows a selector screen.
	 */
	private void setPreferredModePane() throws BluetoothStateException, IOException {
		Mode prefMode = Utils.getPreferredMode();
		switch (prefMode) {
		case BLUETOOTH:
			BluetoothPaneController.showBluetoothPane();
			break;
		case NETWORK:
			
			break;
		default: //not set
			SelectorPaneController.showSelectorPane();
			break;
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static BorderPane getRoot() {
		return root;
	}
}