package com.gaspar.clipsync.bluetooth;

import java.io.IOException;
import java.util.Optional;

import com.gaspar.clipsync.ClipSyncMain;
import com.gaspar.clipsync.Mode;
import com.gaspar.clipsync.SelectorPaneController;
import com.gaspar.clipsync.Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller for the pane that displays when the user selected bluetooth mode.
 * @author Gáspár Tamás
 */
public class BluetoothPaneController {

	@FXML
	private Button backButton;
	
	@FXML
	private Text backendInfoText;
	
	/**
	 * Sets the central pane to the bluetooth pane.
	 */
	public static void showBluetoothPane() throws IOException {
		final FXMLLoader loader = new FXMLLoader(ClipSyncMain.class.getResource("/resources/BluetoothPane.fxml"));
		VBox bluetoothPane = loader.load();
		final BluetoothPaneController controller = loader.getController();
		
		//add properties programmatically
		BorderPane.setMargin(controller.backendInfoText, new Insets(10,10,10,10));
		BorderPane.setAlignment(controller.backendInfoText, Pos.CENTER);
		BorderPane.setMargin(controller.backButton, new Insets(10,10,10,10));
		BorderPane.setAlignment(controller.backButton, Pos.CENTER);
		ClipSyncMain.getRoot().setCenter(bluetoothPane);
	}
	
	/**
	 * This method is called when the user selects the back button (the mode select button).
	 * It will make the app "forget" the preferred mode and show the selection screen.
	 * @param event
	 */
	@FXML
	public void backButtonPressed(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Select mode");
		alert.setHeaderText("Are you sure you want to go back to mode selection?");
		alert.setContentText("This will stop any ClipSync until you select a mode again!");
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("/resources/icon.png").toString()));
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){ //got confrimation
			Utils.writePreferredMode(Mode.NOT_SET);
			SelectorPaneController.showSelectorPane();
		}
	}
	
}
