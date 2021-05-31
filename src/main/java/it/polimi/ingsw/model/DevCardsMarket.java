package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DevCard;
import it.polimi.ingsw.model.cards.DevCardColor;
import it.polimi.ingsw.util.XML_Serialization;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevCardsMarket implements Serializable
{
	private List<List<DevCard>> devCardStacks;

	public DevCardsMarket()
	{
		System.out.println("Creating devcards...");
		List<DevCard> allDevCards = new ArrayList<>();
		devCardStacks = new ArrayList<>();

		for (int i = 0; i < 48; i++)
		{
			try
			{
				allDevCards.add((DevCard) XML_Serialization.deserialize("src/main/resources/xml/devcards/devcard" + (i + 1) + ".xml"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		List<DevCard> tempList;				/* For each devcard color, make 3 stacks of devcards for a total of 12 stacks */

		for (int j = 0; j < 3; j++)
		{
			tempList = new ArrayList<>();

			for (int i = 0; i < 48; i++)
			{																	/* j + 1 because level 0 devcards don't exist */
				if (allDevCards.get(i).getColor() == DevCardColor.GREEN && allDevCards.get(i).getLevel() == (j + 1))
					tempList.add(allDevCards.get(i));
			}

			devCardStacks.add(tempList);
		}

		for (int j = 0; j < 3; j++)
		{
			tempList = new ArrayList<>();

			for (int i = 0; i < 48; i++)
			{
				if (allDevCards.get(i).getColor() == DevCardColor.BLUE && allDevCards.get(i).getLevel() == (j + 1))
					tempList.add(allDevCards.get(i));
			}

			devCardStacks.add(tempList);
		}

		for (int j = 0; j < 3; j++)
		{
			tempList = new ArrayList<>();

			for (int i = 0; i < 48; i++)
			{
				if (allDevCards.get(i).getColor() == DevCardColor.PURPLE && allDevCards.get(i).getLevel() == (j + 1))
					tempList.add(allDevCards.get(i));
			}

			devCardStacks.add(tempList);
		}

		for (int j = 0; j < 3; j++)
		{
			tempList = new ArrayList<>();

			for (int i = 0; i < 48; i++)
			{
				if (allDevCards.get(i).getColor() == DevCardColor.YELLOW && allDevCards.get(i).getLevel() == (j + 1))
					tempList.add(allDevCards.get(i));
			}

			devCardStacks.add(tempList);
		}

		for (int i = 0; i < devCardStacks.size(); i++)			/* Shuffle each stack */
			Collections.shuffle(devCardStacks.get(i));
	}

	public DevCard buyCardFromMarket(int boughtCardNum)
	{
		System.out.println("DevCardMarket: buying card #" + boughtCardNum);

		DevCard boughtCard = getDevCardByNumber(boughtCardNum);

		for (int i = 0; i < devCardStacks.size(); i++)			/* So many for loops, not very efficient... */
		{
			for (int j = 0; j < devCardStacks.get(i).size(); j++)
			{
				if (devCardStacks.get(i).get(j) == boughtCard)
				{
					devCardStacks.get(i).remove(j);
					j--;		/* Not really necessary but prevents the warning from IntelliJ */
				}
			}
		}

		return boughtCard;
	}

	public DevCard getDevCardByNumber(int cardNumber)
	{
		for (int i = 0; i < devCardStacks.size(); i++)				/* Always 12 */
		{
			for (int j = 0; j < devCardStacks.get(i).size(); j++)		/* Not always 4 */
			{
				if (devCardStacks.get(i).get(j).getCardNumber() == cardNumber)
					return devCardStacks.get(i).get(j);
			}
		}

		return null;
	}

	public List<List<DevCard>> getDevCardStacks()
	{
		return devCardStacks;
	}

	public void setDevCardStacks(List<List<DevCard>> devCardStacks)
	{
		this.devCardStacks = devCardStacks;
	}
}
