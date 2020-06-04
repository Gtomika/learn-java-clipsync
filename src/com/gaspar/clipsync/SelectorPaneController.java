package com.gaspar.clipsync;

import java.io.IOException;

import com.gaspar.clipsync.bluetooth.BluetoothPaneController;
import com.gaspar.clipsync.network.NetworkPaneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * Acts as the controller for the mode selector pane.
 * @author Gáspár Tamás
 */
public class SelectorPaneController {
	
	/**
	 * This layout holds the radio buttons used for mode selection.
	 */
	@FXML
	private VBox buttonHolder;
	
	/**
	 * Grouping of the radio buttons.
	 */
	private ToggleGroup toggleGroup;

	/**
	 * Checkbox that the user can tick if they want the selected mode to be remembered.
	 */
	@FXML
	private CheckBox rememberCheckBox;
	
	/**
	 * Button that selects a mode.
	 */
	@FXML
	private Button nextButton;
	
	/**
	 * This method is called when the user selects a preferred mode.
	 * @param event
	 */
	@FXML
	private void nextButtonPressed(ActionEvent event) throws IOException {
		RadioButton selected = (RadioButton)toggleGroup.getSelectedToggle();
		String selectedText = selected.getText();
		Mode mode = null;
		if(selectedText.equals("Bluetooth")) {
			mode = Mode.BLUETOOTH;
			BluetoothPaneController.showBluetoothPane();
		} else { //network
			mode = Mode.NETWORK;
			NetworkPaneController.showNetworkPane();
		}
		if(rememberCheckBox.selectedProperty().get()) { //save
			Utils.writePreferredMode(mode);
		}
	}
	
	/**
	 * Shows the selector pane where the user can select from bluetooth and network mode.
	 */
	public static void showSelectorPane() throws IOException {
		final FXMLLoader loader = new FXMLLoader(ClipSyncMain.class.getResource("/resources/ModeSelector.fxml"));
		VBox selectorPane = loader.load();
		final SelectorPaneController controller = loader.getController();
		controller.toggleGroup = new ToggleGroup();
		//load radio buttons
		RadioButton bluetoothButton = new RadioButton("Bluetooth");
		bluetoothButton.setStyle("-fx-font-size:20px");
		RadioButton networkButton = new RadioButton("Local network");
		networkButton.setStyle("-fx-font-size:20px");
		controller.toggleGroup.getToggles().addAll(bluetoothButton, networkButton);
		controller.toggleGroup.selectToggle(bluetoothButton);
		controller.buttonHolder.getChildren().addAll(bluetoothButton, networkButton);
		
		//add properties programmatically
		BorderPane.setMargin(controller.rememberCheckBox, new Insets(10,10,10,10));
		BorderPane.setAlignment(controller.rememberCheckBox, Pos.CENTER);
		BorderPane.setMargin(controller.nextButton, new Insets(10,10,10,10));
		BorderPane.setAlignment(controller.nextButton, Pos.CENTER);
		ClipSyncMain.getRoot().setCenter(selectorPane);
	}
}
