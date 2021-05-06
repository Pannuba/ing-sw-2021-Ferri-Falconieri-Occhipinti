package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.MarblesMarket;
import it.polimi.ingsw.model.cards.LeaderCard;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

public class CLI extends Observable
{
	private final Scanner input;
	private NetworkHandler networkHandler;
	private final Controller controller;

	public CLI() throws IOException
	{
		controller = new Controller();
		input = new Scanner(System.in);

		NetworkHandler networkHandler = new NetworkHandler(new Socket("127.0.0.1", 2000));
		new Thread(networkHandler).start();

		System.out.println("Masters of the Renaissance!");

		networkHandler.send("ping");
		gameSetup();
	}

	public void gameSetup() throws IOException
	{
		System.out.println("Insert username: ");
		networkHandler.send(input.nextLine());

		/* Client directly asks for code, numPlayers... No reason to make the server ask because it's a standard process */
		/* ALWAYS use writeUTF(), never writeInt() or anything else */

		/* Server tells clients info about the four leadercards */
		System.out.println("Choose leader card 1, 2, 3 or 4: ");
		networkHandler.send(input.nextLine());
	}

	public void printBoard()
	{
		System.out.println("[ | | | | | | | | | | | | | | | | | | | | | | | | | ]");		/* wow */
	}

	public void printPlayerLeaderCard(Object card)		/* Can't access skill variables from LeaderCard because it's abstract */
	{
		System.out.println("Points: " + ((LeaderCard)card).getPoints());
		System.out.println("Discarded: " + ((LeaderCard)card).isDiscarded());

		if (card.getClass().getSimpleName() == "SkillDiscount")
		{
			// ...
		}

	}

	public void printPlayerDevCards() 		/* Model should be in client in order to decode the DevCard objects, unless the server decodes them before sending them */
	{

	}

	public void printDevCardsMarket()
	{

	}

	public void printMarblesMarket(MarblesMarket market)		/* private? */
	{
		Marble[][] board = market.getMarblesBoard();

		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				System.out.print(convertMarbleToString(board[i][j]) + "  ");
			}
			System.out.print(Color.RESET + "\n");
		}
	}

	public String convertMarbleToString(Marble marble)
	{
		final String marbleSymbol = "\u2B24";

		switch (marble.getMarbleType())
		{
			case YELLOW:
				return Color.YELLOW + marbleSymbol + Color.RESET;

			case PURPLE:
				return Color.PURPLE + marbleSymbol + Color.RESET;

			case WHITE:
				return Color.WHITE + marbleSymbol + Color.RESET;

			case GREY:
				return Color.GREY + marbleSymbol + Color.RESET;

			case BLUE:
				return Color.BLUE + marbleSymbol + Color.RESET;

			case RED:
				return Color.RED + marbleSymbol + Color.RESET;
		}

		return null;
	}



}