package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.Resource;
import tools.Logger;

import java.io.IOException;

public class Storage
{
	private Resource shelfOne;
	private Resource shelfTwo;
	private Resource shelfThree;

	/* Logic methods to check resources in shelf here? Or in controller?? */

	public Storage()
	{
		shelfOne = new Resource();
		shelfTwo = new Resource();
		shelfThree = new Resource();

		shelfOne.setQuantity(0);
		shelfTwo.setQuantity(0);
		shelfThree.setQuantity(0);
	}

	public boolean checkShelves() throws IOException
	{
		if (shelfOne.getQuantity() > 1 || shelfTwo.getQuantity() > 2 || shelfThree.getQuantity() > 3)
		{
			System.out.println("Shelf has incorrect amount of resources");
			return false;
		}

		if (shelfOne.getResourceType() == shelfTwo.getResourceType() || shelfTwo.getResourceType() == shelfThree.getResourceType() || shelfOne.getResourceType() == shelfThree.getResourceType())
		{
			System.out.println("Shelf has the same type of resource of another shelf");
			return false;
		}

		else
			return true;
	}

	public void moveResources()
	{

	}

	public int getTotalResources()
	{
		int totalResources = 0;

		totalResources += shelfOne.getQuantity();
		totalResources += shelfTwo.getQuantity();
		totalResources += shelfThree.getQuantity();

		return totalResources;
	}

	public Resource getShelfOne()
	{
		return shelfOne;
	}

	public void setShelfOne(Resource shelfOne)
	{
		this.shelfOne = shelfOne;
	}

	public Resource getShelfTwo()
	{
		return shelfTwo;
	}

	public void setShelfTwo(Resource shelfTwo)
	{
		this.shelfTwo = shelfTwo;
	}

	public Resource getShelfThree()
	{
		return shelfThree;
	}

	public void setShelfThree(Resource shelfThree)
	{
		this.shelfThree = shelfThree;
	}
}
