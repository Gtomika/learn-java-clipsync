package com.gaspar.clipsync.bluetooth;

import java.io.IOException;
import java.util.Optional;

import javax.bluetooth.BluetoothStateException;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The controller for the pane that displays when the user selected bluetooth mode.
 * @author Gáspár Tamás
 */
public class BluetoothPaneController {

	/**
	 * This button allows the user to go back to mode selection.
	 * A confirmation prompt is shown before.
	 */
	@FXML
	private Button backButton;
	
	/**
	 * Displays bluetooth API implementor BlueCove.
	 */
	@FXML
	private Text backendInfoText;
	
	/**
	 * Displays the result of the bluetooth initialization.
	 */
	@FXML
	private HBox resultHolder;
	
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
		
		try { //attempt to start bluetooth server
			BluetoothManager.instance().createServer();
			controller.buildResultPane(true);
		} catch (BluetoothStateException e) {
			controller.buildResultPane(false);
		}
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
	
	/**
	 * Creates a text view and an icon to show the result of the bluetooth initialization.
	 * @param success If the initialization succeeded.
	 */
	private void buildResultPane(boolean success) {
		Text text = new Text();
		ImageView icon = new ImageView();
		if(success) {
			text.setText("Ready for ClipSync!");
			icon.setImage(new Image("/resources/success.png"));
		} else { //fail
			text.setText("Initialization failed!\nMake sure your computer has bluetooth!");
			icon.setImage(new Image("/resources/fail.png"));
		}
		text.setStyle("-fx-font-size: 20px");
		icon.fitWidthProperty().set(50);
		icon.fitHeightProperty().set(50);
		resultHolder.getChildren().addAll(text, icon);
	}
}
