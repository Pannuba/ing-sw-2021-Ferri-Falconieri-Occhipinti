package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.MessageExecutor;
import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.ActionToken;
import it.polimi.ingsw.model.cards.LeaderCard;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class ActionGUI extends MessageExecutor
{
	private final GUI gui;
	private final Stage mainStage;
	private final Scene gameStartScene;
	private final Scene mainViewScene;
	private final Scene endGameScene;
	private final LoginController lc;
	private final GameStartController gsc;
	private final EndGameController egc;
	private final LeaderCardsController lcc;
	private final MainViewController mvc;						/* To update the scenes when a new gamestate is received */
	private final MarketsController mc;

	public ActionGUI(GUI gui, Scene gameStartScene, Scene mainViewScene, Scene endGameScene, LoginController lc,
					 GameStartController gsc, EndGameController egc, LeaderCardsController lcc, MainViewController mvc, MarketsController mc)
	{
		this.gui = gui;
		mainStage = gui.getMainStage();
		this.gameStartScene = gameStartScene;
		this.mainViewScene = mainViewScene;
		this.endGameScene = endGameScene;
		this.lc = lc;
		this.gsc = gsc;
		this.egc = egc;
		this.lcc = lcc;
		this.mvc = mvc;		/* Pack all loaders in a hashmap? */
		this.mc = mc;
	}

	public void updateView(GameState gameState)
	{
		Platform.runLater(() -> {
			mvc.update(gameState, gui.getUsername());
			lcc.update(gameState.getPlayerByName(gui.getUsername()).getLeaderCards(), gameState.getPlayerByName(gui.getUsername()).isMyTurn());
			mc.updateMarket(gameState.getCurrMarblesMarket(), gameState.getCurrDevCardsMarket(), gameState.getPlayerByName(gui.getUsername()));
		});
	}

	@Override
	public void loginFailed(String message)
	{
		Platform.runLater(() -> {
			lc.getErrorLabel().setText(message);
			lc.getMessageHandler().stop();
		});
	}

	@Override
	public void firstPlayer(boolean isFirstPlayer)
	{
		if (isFirstPlayer)
			lc.firstPlayer();
	}

	@Override
	public void startMatch()
	{
		Platform.runLater(() -> {
			mainStage.setTitle("Masters of Renaissance - Setup");
			mainStage.setScene(gameStartScene);
			mainStage.centerOnScreen();
			mainStage.show();
		});
	}

	@Override
	public void startRecoveredMatch()
	{
		Platform.runLater(() -> {
			mvc.printToConsole("Restored saved match!");
			mainStage.setTitle("Masters of Renaissance");
			mainStage.setScene(mainViewScene);
			mainStage.centerOnScreen();
			mainStage.show();
		});
	}

	@Override
	public void initialResources(int playerID)
	{
		gsc.showResourcesMessage(playerID);
	}

	@Override
	public void vaticanReport(int popeBoxNum, List<Player> players)
	{
		mvc.getConsole().setText("Vatican report on box #" + popeBoxNum + "!");
	}

	@Override
	public void chooseLeaderCards(List<LeaderCard> leaderCards)
	{
		gsc.setLeaderCards(leaderCards);
	}

	@Override
	public void productionResult(Player player)
	{
		Platform.runLater(() -> {
			mvc.enableButtons();
			mvc.updateStorage(player.getDashboard().getStorage());
			lcc.update(player.getLeaderCards(), false);		/* isMyTurn = true because it's my turn when I'm doing the production */
		});
	}

	@Override
	public void getBoughtResources(List<Resource> boughtResources)
	{
		mvc.printToConsole("Received " + boughtResources.size() + " resources");
	}

	@Override
	public void getDiscardedResources(int discardedResNum, String playerWhoDiscarded)
	{
		if (!playerWhoDiscarded.equals(gui.getUsername()))
			mvc.printToConsole(playerWhoDiscarded + " discarded " + discardedResNum + " resources, so you gained " + discardedResNum + " faith points!");

		else
			mvc.printToConsole(discardedResNum + " resources couldn't fit in the storage, so they have been discarded");
	}

	@Override
	public void getOperationResultMessage(String message, boolean isFailed)
	{
		mvc.printToConsole(message);

		if (!isFailed)		/* If the action fails the client doesn't receive a gamestate (except for leadercard actions, but a GameState is immediately sent afterwards so it's ok) */
			mvc.disableButtons();
	}

	@Override
	public void getActionToken(ActionToken token)
	{
		mvc.printToConsole("Flipped next action token");
		mvc.updateActionToken(token);
	}

	@Override
	public void matchOver(String winnerName, List<Player> players)
	{
		egc.setup(winnerName, players, gui.getUsername(), mainStage);
		Platform.runLater(() -> {
			mainStage.setTitle("Masters of Renaissance - Game Over");
			mainStage.setScene(endGameScene);
			mainStage.sizeToScene();
			mainStage.show();
		});
	}

	@Override
	public void singlePlayerGameOver(String message)
	{
		egc.setupSingle(message, mainStage);
		Platform.runLater(() -> {
			mainStage.setTitle("Masters of Renaissance - Game Over");
			mainStage.setScene(endGameScene);
			mainStage.sizeToScene();
			mainStage.centerOnScreen();
			mainStage.show();
		});
	}
}
