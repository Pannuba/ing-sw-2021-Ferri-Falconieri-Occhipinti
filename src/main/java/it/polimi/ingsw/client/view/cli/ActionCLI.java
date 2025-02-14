package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.MessageIO;
import it.polimi.ingsw.client.view.MessageExecutor;
import it.polimi.ingsw.client.view.cli.actions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resource;
import it.polimi.ingsw.model.cards.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ActionCLI extends MessageExecutor    /* Has methods that perform actions such as buying resources, to avoid cluttering the CLI. Interface? */
{
	private final Scanner input;
	private final CLI cli;
	private final MessageIO messageHandler;
	private final List<String> command;

	public ActionCLI(CLI cli)
	{
		this.cli = cli;
		this.input = cli.getInput();
		this.messageHandler = cli.getMessageHandler();
		command = new ArrayList<>();
	}

	@Override
	public void loginFailed(String message)
	{
		System.out.print(message);		/* Switch/case for other error types? */
		messageHandler.send(input.nextLine());
	}

	@Override
	public void firstPlayer(boolean isFirstPlayer)
	{
		if (isFirstPlayer)
		{
			System.out.print("Total players: ");
			messageHandler.send(input.nextLine());
		}
	}

	@Override
	public void startMatch()		/* Simple actions like this one don't need their own class */
	{
		System.out.println("Starting match\n\nMasters of Renaissance!");
	}

	@Override
	public void startRecoveredMatch()
	{
		System.out.println("Restored saved match!");
	}

	@Override
	public void chooseLeaderCards(List<LeaderCard> fourLeaderCards)
	{
		new ChooseLeaderCards(fourLeaderCards, input, command, messageHandler);
	}

	@Override
	public void productionResult(Player player)
	{
		PrintMethods.printStorage(player.getDashboard().getStorage());
		System.out.print("Choose another production: default production (0), devcard (1), SkillProduction leader card (2), stop production (4): ");

		List<String> command;

		switch (input.nextLine())
		{
			case "0":
				command = new ActivateProduction(cli.getInput(), cli).defaultProduction();
				break;

			case "1":
				command = new ActivateProduction(cli.getInput(), cli).devCardProduction();
				break;

			case "3":
				command = new ActivateProduction(cli.getInput(), cli).leaderProduction();
				break;

			default:
				messageHandler.send(Arrays.asList("STOP_PRODUCTION"));
				return;		/* Otherwise it will send the command twice */
		}

		if (command != null)
			messageHandler.send(command);

		else	/* If the command has failed */
			messageHandler.send(Arrays.asList("STOP_PRODUCTION"));
	}

	@Override
	public void initialResources(int playerID)			/* 1st player: nothing; 2nd: 1 resource; 3rd: 1 resource + 1 faithPoint; 4th: 2 resources + 1 faithPoint */
	{
		new InitialResources(playerID, input, command, messageHandler);
	}

	@Override
	public void getBoughtResources(List<Resource> boughtResources)
	{
		System.out.print("Received the following resources: " + PrintMethods.convertResListToString(boughtResources) + "\n");
	}

	@Override
	public void getDiscardedResources(int discardedResNum, String playerWhoDiscarded)
	{
		if (!playerWhoDiscarded.equals(cli.getUsername()))
			System.out.println(playerWhoDiscarded + " discarded " + discardedResNum + " resources, so you gained " + discardedResNum + " faith points!");

		else
			System.out.println(discardedResNum + " resources couldn't fit in the storage, so they have been discarded");
	}

	@Override
	public void getOperationResultMessage(String message, boolean isFailed)
	{
		System.out.println(message);	/* If the default production fails there's a desync problem because the CLI receives an OperationResultMessage and a ProductionResultMessage */

		if (isFailed)
			cli.chooseAction();		/* Client can repeat the round for failed actions */
	}

	@Override
	public void getActionToken(ActionToken token)
	{
		System.out.print("Received action token: ");
		PrintMethods.printActionToken(token);
	}

	@Override
	public void vaticanReport(int popeBoxNum, List<Player> players)
	{
		new VaticanReport(popeBoxNum, players, cli);
	}

	@Override
	public void singlePlayerGameOver(String message)		/* When the player has lost, print the game over message and close the client */
	{
		System.out.println(message);

		System.out.print("Do you want to play again? (Y/N) ");

		if (input.nextLine().equalsIgnoreCase("Y"))
		{
			messageHandler.stop();
			new CLI();
		}

		else
			messageHandler.shutdown();
	}

	@Override
	public void matchOver(String winnerName, List<Player> players)
	{
		if (cli.getUsername().equals(winnerName))
			System.out.println("You win!");

		if (winnerName.isEmpty())
			System.out.println("Tie!");

		else
			System.out.println("You lose! The winner is " + winnerName);

		for (int i = 0; i < players.size(); i++)
			System.out.println(players.get(i).getUsername() + ": " + players.get(i).getVictoryPoints() + " points");

		System.out.print("Do you want to play again? (Y/N) ");

		if (input.nextLine().equalsIgnoreCase("Y"))
		{
			messageHandler.stop();
			new CLI();
		}

		else
			messageHandler.shutdown();
	}
}
