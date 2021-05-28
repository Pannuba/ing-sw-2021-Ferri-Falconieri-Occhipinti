package it.polimi.ingsw.client;

import it.polimi.ingsw.util.Ping;

import java.io.*;
import java.util.*;

import java.net.Socket;

public class NetworkHandler extends Observable implements Runnable		/* Observes CLI to get input to send to server, observed by CLI to send it the newest gamestate */
{
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private final Timer heartbeat;
	private final TimerTask sendPing;
	private final String ip;
	private final int port;

	public NetworkHandler(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
		heartbeat = new Timer();

		sendPing = new TimerTask() {
			public void run() {
				send(new Ping()); } };
	}

	public void run()
	{
		while (!clientSocket.isClosed())
		{
			try
			{
				System.out.println("Waiting for new object from server");
				Object inputObj = ois.readObject();
				System.out.println("Received " + inputObj.getClass().getSimpleName());

				if (!(inputObj instanceof Ping))		/* Don't care if it's a ping */
				{
					setChanged();
					notifyObservers(inputObj);
				}
			}
			catch (IOException | ClassNotFoundException e)
			{
				System.out.println("Lost connection to server!");
				e.printStackTrace();
				break;
			}
		}

		shutdown();
	}

	public void connect()
	{
		System.out.println("Connecting to server...");

		try
		{
			clientSocket = new Socket(ip, port);
			clientSocket.setSoTimeout(20000);								/* 20000 ms = 20 seconds. shutdown() after 3 failed pings? */
			oos = new ObjectOutputStream(clientSocket.getOutputStream());	/* Send commands (list of strings) to server */
			ois = new ObjectInputStream(clientSocket.getInputStream());		/* Receive gamestate or messages from server */
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Connection failed to server");
			shutdown();
		}

		heartbeat.scheduleAtFixedRate(sendPing, 5000, 10000);		/* Start heartbeat after 5 seconds, sends ping every timeout/2 seconds */
	}

	public void send(Object obj)
	{
		try
		{
			oos.writeObject(obj);
			oos.reset();						/* omg if this works i swear to god (it did kinda) */
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void shutdown()
	{
		System.out.println("Shutting down...");

		try
		{
			oos.flush();
			oos.close();

			heartbeat.cancel();
			heartbeat.purge();

			clientSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.exit(0);
	}
}
