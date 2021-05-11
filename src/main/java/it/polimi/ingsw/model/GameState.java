package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Dashboard;
import it.polimi.ingsw.model.board.Track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*	Every turn, the server sends a GameState object to all clients
	So the client view reads it and updates everything
	TODO: translate GameState object in client
 */

public class GameState implements Serializable
{
	private List<Player> currPlayers;
	private String currPlayerName;		/* Player who has the current turn (choose action). ID? */
	private Track currTrack;
	private MarblesMarket currMarblesMarket;
	private DevCardsMarket currDevCardsMarket;
	private int round;

	public GameState(List<Player> currPlayers, Track currTrack, MarblesMarket currMarblesMarket, DevCardsMarket currDevCardsMarket)
	{
		//this.round = round;
		this.currPlayers = currPlayers;
		this.currTrack = currTrack;
		this.currMarblesMarket = currMarblesMarket;
		this.currDevCardsMarket = currDevCardsMarket;

		/*currBoard = currPlayers.getDashboard();		Need to get all dashboards from player. for loop?
		currPlayerName = currPlayers.getUsername();*/
	}

	public Player getPlayerByName(String name)
	{
		Player player = new Player();

		for (int i = 0; i < currPlayers.size(); i++)
		{
			System.out.println("currPlayers " + i + " name: " + currPlayers.get(i).getUsername());

			if (currPlayers.get(i).getUsername().equals(name))
			{
				return currPlayers.get(i);
			}
		}

		System.out.println("Player \"" + name + "\" not found");
		return null;
	}

	public List<Player> getCurrPlayers()
	{
		return currPlayers;
	}

	public void setCurrPlayers(List<Player> currPlayers)
	{
		this.currPlayers = currPlayers;
	}

	public String getCurrPlayerName()
	{
		return currPlayerName;
	}

	public void setCurrPlayerName(String currPlayerName)
	{
		this.currPlayerName = currPlayerName;
	}

	public Track getCurrTrack()
	{
		return currTrack;
	}

	public void setCurrTrack(Track currTrack)
	{
		this.currTrack = currTrack;
	}

	public MarblesMarket getCurrMarblesMarket()
	{
		return currMarblesMarket;
	}

	public void setCurrMarblesMarket(MarblesMarket currMarblesMarket)
	{
		this.currMarblesMarket = currMarblesMarket;
	}

	public DevCardsMarket getCurrDevCardsMarket()
	{
		return currDevCardsMarket;
	}

	public void setCurrDevCardsMarket(DevCardsMarket currDevCardsMarket)
	{
		this.currDevCardsMarket = currDevCardsMarket;
	}

	public int getRound()
	{
		return round;
	}

	public void setRound(int round)
	{
		this.round = round;
	}
}
