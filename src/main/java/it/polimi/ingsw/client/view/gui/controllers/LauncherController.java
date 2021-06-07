package it.polimi.ingsw.client.view.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LauncherController
{

	@FXML
	private ImageView playOnline;

	@FXML
	private ImageView playLocal;

	private Scene launcherScene;
	private LoginController lc;

	@FXML
	void playLocal(MouseEvent event) throws IOException
	{
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		lc.getIpField().setVisible(false);
		lc.getPortField().setVisible(false);
		mainStage.setTitle("Masters of Renaissance - Local Game");
		mainStage.setScene(launcherScene);
		mainStage.sizeToScene();
		mainStage.show();
	}

	@FXML
	void playOnline(MouseEvent event) throws IOException
	{
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		mainStage.setTitle("Masters of Renaissance - Online Game");
		mainStage.setScene(launcherScene);
		mainStage.sizeToScene();
		mainStage.show();

	}

	public void setup() throws IOException {
		FXMLLoader launcherLoader = new FXMLLoader();
		launcherLoader.setLocation(getClass().getResource("/scenes/login.fxml"));
		Parent launcherRoot = launcherLoader.load();
		launcherScene = new Scene(launcherRoot);

		lc = launcherLoader.getController();

	}
}
