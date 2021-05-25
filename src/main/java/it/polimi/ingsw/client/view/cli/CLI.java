package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.model.GameState;
import it.polimi.ingsw.model.ResourceType;
import it.polimi.ingsw.server.messages.Message;

import java.util.*;

public class CLI extends Observable implements Observer		/* FIXME: CLI gets old information from gamestates. executors/threadpools, or send less gamestates? */
{
	private final Scanner input;
	private final ActionExecutor action;
	private NetworkHandler networkHandler;
	private GameState gameState;
	private String username;

	public CLI()
	{
		input = new Scanner(System.in);
		action = new ActionExecutor(this);
		gameStart();						/* Will probably have to start the networkHandler immediately and use messages also for setup to avoid sync problems */
		action.chooseLeaderCards();
		action.chooseResources();
		new Thread(networkHandler).start();		/* Start listening for gamestate updates from server. Put this after game setup? Maybe. */

	}

	private void gameStart()
	{
		System.out.print("Username: ");
		username = input.nextLine();
		System.out.print("Server IP: ");
		//String ip = input.nextLine();		/* For debugging */
		String ip = "127.0.0.1";
		System.out.print("Server port: ");
		//int port = Integer.parseInt(input.nextLine());
		int port = 2000;

		networkHandler = new NetworkHandler(ip, port);
		networkHandler.addObserver(this);		/* CLI observes the networkHandler to get the new gamestate */
		networkHandler.connect();

		System.out.println("Sending username to server...");
		networkHandler.sendString(username);

		if (networkHandler.isFirstPlayer())
		{
			System.out.print("Total players: ");
			networkHandler.sendString(input.nextLine());
		}

		System.out.println("Created network handler");
		networkHandler.waitForPlayers();
		System.out.println("Starting match\n\nMasters of the Renaissance!");
	}
	
	private void chooseAction(int choice)			/* Actions class? */
	{
		switch(choice)
		{
			case 0:
				action.buyResources();
				break;

			case 1:
				action.buyDevCard();
				break;

			case 2:
				//command.add("ACTIVATE_PRODUCTION");
				break;

			case 3:
				PrintMethods.printPlayerLeaderCards(gameState.getPlayerByName(username).getLeaderCards());
				PrintMethods.printPlayerDevCards(gameState.getPlayerByName(username).getDashboard().getAllDevCards());
				break;

			case 4:
				PrintMethods.printTrack(gameState.getCurrTrack(), gameState.getCurrPlayers());
				PrintMethods.printBoard(gameState.getPlayerByName(username).getDashboard());
				break;

			case 5:
				PrintMethods.printDevCardsMarket(gameState.getCurrDevCardsMarket());
				PrintMethods.printMarblesMarket(gameState.getCurrMarblesMarket());
				break;

			default:
				System.out.println("Invalid action number");

		}

		if (choice == 3 || choice == 4 || choice == 5)			/* Player can choose again after viewing things */
		{
			System.out.print("What do you want to do now?\nBuy from market (0), buy devcards (1), activate production (2), view cards (3), view board (4), view markets (5): ");
			chooseAction(Integer.parseInt(input.nextLine()));
		}
	}

	@Override
	public void update(Observable obs, Object obj)
	{
		if (obj instanceof Message)
			((Message) obj).process(this);		/* Calls method in cli specified in the message */

		if (obj instanceof GameState)		/* TODO: check if match is over, or make server send "match over" message */
		{
			System.out.println("Received gamestate");
			this.gameState = (GameState) obj;        /* Gamestate is needed in game loop, not during setup */
			System.out.println(gameState.getCurrPlayers().get(0).getUsername() + " is active? " + gameState.getCurrPlayers().get(0).isMyTurn());
			System.out.println(gameState.getCurrPlayers().get(1).getUsername() + " is active? " + gameState.getCurrPlayers().get(1).isMyTurn());

			if (gameState.getPlayerByName(username).isMyTurn())
			{
				System.out.println("It's your turn!");
				System.out.print("What do you want to do?\nBuy from market (0), buy devcards (1), activate production (2), view cards (3), view board (4), view markets (5): ");
				chooseAction(Integer.parseInt(input.nextLine()));
			}

			else
				System.out.println("It's " + gameState.getCurrPlayerName() + "'s turn!");
		}
	}

	public void getBoughtResources(List<ResourceType> boughtResources)
	{
		System.out.println("Received the following resources: " + boughtResources);
	}

	public NetworkHandler getNetworkHandler()
	{
		return networkHandler;
	}

	public Scanner getInput()
	{
		return input;
	}

	public GameState getGameState()
	{
		return gameState;
	}

	public String getUsername()
	{
		return username;
	}
}
