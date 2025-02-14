package it.polimi.ingsw.client.view.gui.controllers;

import it.polimi.ingsw.client.MessageIO;
import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.client.view.gui.GUI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Scene controller for the login, allows you to enter username and the number of players
 * If it is an online game, it allows you to enter the ip address and port
 * @author Giulio Occhipinti
 * @author Chiara Falconieri
 */

public class LoginController
{
	private boolean isLocalMatch;
	private MessageIO messageHandler;
	private Scene launcherScene;

	@FXML private TextField nameField;
	@FXML private TextField ipField;
	@FXML private TextField portField;
	@FXML private TextField numPlayersField;
	@FXML private Label errorLabel;

	@FXML private ImageView startButton;
	@FXML private ImageView indicator;

	@FXML
	void backToLauncher(MouseEvent event)
	{
		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		ipField.setVisible(true);
		portField.setVisible(true);
		isLocalMatch = false;

		mainStage.setTitle("Masters of Renaissance Launcher");
		mainStage.setScene(launcherScene);
		mainStage.sizeToScene();
		mainStage.show();
	}

	@FXML
	void connectToServerMouse(MouseEvent event) throws IOException
	{
		connectToServer(event);
	}

	@FXML
	public void connectToServerEnter(ActionEvent event) throws IOException
	{
		connectToServer(event);
	}

	public void connectToServer(Event event) throws IOException
	{
		if (numPlayersField.isVisible())		/* Temporary solution, but what do? */
		{
			numPlayersField.getText();

			if (Integer.parseInt(numPlayersField.getText()) > 0 && Integer.parseInt(numPlayersField.getText()) < 5)
			{
				errorLabel.setText("Waiting for other players to connect...");
				messageHandler.send(numPlayersField.getText());
			}

			else
				errorLabel.setText("Wrong number of players: re-enter the number");

			return;
		}

		Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		GUI gui = new GUI(nameField.getText(), this, mainStage);

		if (!isLocalMatch)
		{
			messageHandler = new NetworkHandler(gui, ipField.getText(), Integer.parseInt(portField.getText()));
			messageHandler.connect();
			messageHandler.send(nameField.getText());
		}

		gui.setMessageHandler(messageHandler);
		gui.createScenes();
	}

	public void firstPlayer()
	{
		numPlayersField.setVisible(true);
	}

	public TextField getIpField()
	{
		return ipField;
	}

	public TextField getPortField()
	{
		return portField;
	}

	public Label getErrorLabel()
	{
		return errorLabel;
	}

	public boolean isLocalMatch()
	{
		return isLocalMatch;
	}

	public void setLocalMatch(boolean localMatch)
	{
		isLocalMatch = localMatch;
	}

	public MessageIO getMessageHandler()
	{
		return messageHandler;
	}

	public void setup(Scene launcherScene)        /* Get GUI from launcher, set username and messageHandler in startMatch? Using getters */
	{
		this.launcherScene = launcherScene;
	}
}
