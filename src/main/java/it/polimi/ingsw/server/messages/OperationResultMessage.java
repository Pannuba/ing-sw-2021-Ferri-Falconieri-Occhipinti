package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.client.view.MessageExecutor;

/**
 * This message is sent after (nearly?) every action the client performs
 * The boolean is true if the action has failed, so the controller doesn't progress the game by sending a new gamestate and choosing the next player
 * (and flipping the next actiontoken for singleplayer matches) so that the client can perform another action in the same round
 * @author Giulio Occhipinti
 */

public class OperationResultMessage implements Message
{
	private final String message;
	private final boolean isFailed;

	public OperationResultMessage(String message, boolean isFailed)
	{
		this.message = message;
		this.isFailed = isFailed;
	}

	@Override
	public void process(MessageExecutor action)
	{
		action.getOperationResultMessage(message, isFailed);
	}
}
