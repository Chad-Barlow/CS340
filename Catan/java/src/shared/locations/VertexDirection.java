package shared.locations;

import client.exceptions.ClientModelException;

/**
 * An enum which describes an Vertex Direction
 * <br><b>Domain:</b> West, NorthWEst, NorthEast, East, SouthEast, SouthWest
 *
 */
public enum VertexDirection
{
	West, NorthWest, NorthEast, East, SouthEast, SouthWest;
	
	private VertexDirection opposite;
	
	static
	{
		West.opposite = East;
		NorthWest.opposite = SouthEast;
		NorthEast.opposite = SouthWest;
		East.opposite = West;
		SouthEast.opposite = NorthWest;
		SouthWest.opposite = NorthEast;
	}
	
	/**
	 * Gets the opposite direction of the Vertex Direction
	 * @pre none
	 * @post Returns the opposite direction of the Vertex Direction
	 */
	public VertexDirection getOppositeDirection()
	{
		return opposite;
	}
	
	/**
	 * Determines which direction enum to return based off of the string given as a parameter
	 * @param dir
	 * @pre none
	 * @post Returns an enum assocaited to the specified direction
	 * @throws ClientModelException
	 */
	public static VertexDirection determineDirection(String dir) throws ClientModelException {
		switch(dir) {
		case "NorthWest":
			return VertexDirection.NorthWest;
		case "West":
			return VertexDirection.West;
		case "NorthEast":
			return VertexDirection.NorthEast;
		case "SouthEast":
			return VertexDirection.SouthEast;
		case "East":
			return VertexDirection.East;
		case "SouthWest":
			return VertexDirection.SouthWest;
		case "NW":
			return VertexDirection.NorthWest;
		case "W":
			return VertexDirection.West;
		case "NE":
			return VertexDirection.NorthEast;
		case "SE":
			return VertexDirection.SouthEast;
		case "E":
			return VertexDirection.East;
		case "SW":
			return VertexDirection.SouthWest;
		default:
			throw new ClientModelException("Invalid direction string given to static method VertexDirection determineDirection in VertexDirection class");
		}
	}
}

