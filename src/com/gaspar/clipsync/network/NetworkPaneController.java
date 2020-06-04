package com.gaspar.clipsync.network;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the controller for the network clip sync mode.
 * @author Gáspár Tamás
 */
public class NetworkPaneController {
	/**
	 * Displays the result of the network initialization.
	 */
	@FXML
	private HBox resultHolder;
	/**
	 * This button allows the user to go back to mode selection.
	 * A confirmation prompt is shown before.
	 */
	@FXML
	private Button backButton;
	/**
	 * Sets the central pane to the bluetooth pane. Starts the network server.
	 * @throws IOException On FXML load error.
	 */
	public static void showNetworkPane() throws IOException {
		final FXMLLoader loader = new FXMLLoader(ClipSyncMain.class.getResource("/resources/NetworkPane.fxml"));
		VBox networkPane = loader.load();
		final NetworkPaneController controller = loader.getController();
		//add properties programmatically
		VBox.setMargin(controller.backButton, new Insets(10,10,10,10));
		ClipSyncMain.getRoot().setCenter(networkPane);
		
		try { //attempt to start server
			NetworkManager.instance().startServer();
			controller.buildResultPane(true);
		} catch(IOException e) { //start error, for example port in use
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
			NetworkManager.instance().stopServer(); //stop server
			Utils.writePreferredMode(Mode.NOT_SET); //forget preference
			SelectorPaneController.showSelectorPane(); //show main menu
		}
	}
	/**
	 * Creates a text view and an icon to show the result of the network server initialization.
	 * @param success If the initialization succeeded.
	 */
	private void buildResultPane(boolean success) {
		Text text = new Text();
		ImageView icon = new ImageView();
		if(success) {
			text.setText("Ready for ClipSync!");
			icon.setImage(new Image("/resources/success.png"));
		} else { //fail
			text.setText("Server failed: port " + NetworkServer.PORT_NUMBER + " in use!");
			icon.setImage(new Image("/resources/fail.png"));
		}
		text.setStyle("-fx-font-size: 20px");
		icon.fitWidthProperty().set(50);
		icon.fitHeightProperty().set(50);
		resultHolder.getChildren().addAll(text, icon);
	}
}
