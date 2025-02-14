package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.client.view.MessageExecutor;

/**
 * @author Giulio Occhipinti
 */

public class MatchStartMessage implements Message
{

	@Override
	public void process(MessageExecutor action)
	{
		action.startMatch();
	}
}
