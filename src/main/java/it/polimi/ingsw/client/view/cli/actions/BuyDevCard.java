package it.polimi.ingsw.client.view.cli.actions;

import it.polimi.ingsw.client.view.cli.CLI;
import it.polimi.ingsw.client.view.cli.PrintMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BuyDevCard
{
	private Scanner input;
	private CLI cli;

	public BuyDevCard(Scanner input, CLI cli)
	{
		this.input = input;
		this.cli = cli;
	}

	public List<String> run()
	{
		List<String> command = new ArrayList<>();
		int devCardAreaIndex, targetAreaLayer;
		String choice, cardToBuyNum;

		System.out.print("These are your current dev card areas:\n\n");
		PrintMethods.printDevCardAreas(cli.getGameState().getPlayerByName(cli.getUsername()).getDashboard().getDevCardAreas());
		System.out.print("Insert the dev card area # you want to put the new card in (1/2/3) (Q = choose another action) ");
		choice = input.nextLine();

		if (!choice.equals("1") && !choice.equals("2") && !choice.equals("3"))
		{
			cli.chooseAction();
			return null;
		}

		devCardAreaIndex = Integer.parseInt(choice);											/* - 1 because arrays are zero-indexed */
		targetAreaLayer = cli.getGameState().getPlayerByName(cli.getUsername()).getDashboard().getDevCardAreas()[devCardAreaIndex - 1].getLayer();

		while (targetAreaLayer == 3)
		{
			System.out.print("This dev card area already has three cards!\nWhich dev card area do you want to put the new card in? ");
			devCardAreaIndex = Integer.parseInt(input.nextLine());		/* 1, 2 or 3			- 1 because arrays are zero-indexed */
			targetAreaLayer = cli.getGameState().getPlayerByName(cli.getUsername()).getDashboard().getDevCardAreas()[devCardAreaIndex - 1].getLayer();
		}

		/* Print devcards of level targetAreaLayer + 1 (layer = level of top devcard. New card has to have a higher level) */
		PrintMethods.printDevCardsMarket(cli.getGameState().getCurrDevCardsMarket());

		System.out.print("Insert the card number you want to buy: ");
		cardToBuyNum = input.nextLine();

		command.add("BUY_DEVCARD");
		command.add(cardToBuyNum);
		command.add(Integer.toString(devCardAreaIndex));
		return command;
	}
}
