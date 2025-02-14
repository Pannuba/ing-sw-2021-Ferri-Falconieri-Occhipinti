package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.util.Ping;

import java.io.*;
import java.util.*;

import java.net.Socket;

/**
 * Used by the client to send messages to a remote server through sockets.
 */

public class NetworkHandler extends MessageIO implements Runnable		/* Observed by CLI to send it the newest gamestate */
{
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private final View view;
	private final Timer heartbeat;
	private final TimerTask sendPing;
	private final String ip;
	private final int port;

	public NetworkHandler(View view, String ip, int port)
	{
		this.view = view;
		this.ip = ip;
		this.port = port;

		sendPing = new TimerTask() {
			public void run() {
				send(new Ping()); } };

		heartbeat = new Timer();
	}

	@Override
	public void run()
	{
		while (!clientSocket.isClosed())
		{
			try
			{
				Object inputObj = ois.readObject();

				if (!(inputObj instanceof Ping))		/* Don't care if it's a ping */
					view.update(inputObj);
			}
			catch (IOException | ClassNotFoundException e)
			{
				System.out.println("Lost connection to server!");
				e.printStackTrace();
				shutdown();
				break;
			}
		}
	}

	public void connect()
	{
		System.out.println("Connecting to server...");

		try
		{
			clientSocket = new Socket(ip, port);
			clientSocket.setSoTimeout(20000);								/* 20000 ms = 20 seconds */
			oos = new ObjectOutputStream(clientSocket.getOutputStream());	/* Send commands (list of strings) to server */
			ois = new ObjectInputStream(clientSocket.getInputStream());		/* Receive gamestate or messages from server */
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Connection failed to server");
			//shutdown();
		}

		heartbeat.scheduleAtFixedRate(sendPing, 5000, 10000);		/* Start heartbeat after 5 seconds, sends ping every timeout/2 seconds */
	}

	@Override
	public synchronized void send(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.reset();
		}
		catch (IOException e)
		{
			System.out.println("Couldn't send " + obj + " to server!");
			e.printStackTrace();
			shutdown();
		}
	}

	public void stop()
	{
		System.out.println("Shutting down...");

		try
		{
			sendPing.cancel();
			heartbeat.cancel();
			heartbeat.purge();

			ois.close();
			oos.flush();
			oos.close();

			clientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void shutdown()
	{
		stop();
		System.exit(0);
	}
}
