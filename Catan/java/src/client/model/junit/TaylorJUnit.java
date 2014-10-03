package client.model.junit;

import static org.junit.Assert.*;

import org.junit.*;

import proxy.*;
import shared.definitions.*;
import client.model.*;

public class TaylorJUnit{
	private ClientModel clientModel;
	
	@Before 
	public void setUp() { 
		//Creates a ServerModel object from a test JSON string. Then creates a ClientModel from ServerModel.
		ITranslator translator = new TranslatorJSON();
		clientModel = new ClientModel((ServerModel)translator.translateFrom(gameJSON, ServerModel.class));
	}
	
//	@Test
//	public void testUpdateServerModel(){
//		
//	}
	
//	@Test
//	public void testCanAcceptTrade(){
//		//Trade offer should be null
//		assertEquals("Client Model should not have a trade offer and fail", 
//				false, clientModel.canAcceptTrade());
//		
//		//Create invalid trade offer from player 0 to player 1 for 1 wood
//		clientModel.getServerModel().setTradeOffer(new TradeOffer(0,1,0,0,0,0,1));
//		assertEquals("Client Model should not have a valid trade offer and fail",
//				false, clientModel.canAcceptTrade());
//		
//		//Create valid trade offer from player 0 to player 1 for 1 brick
//		clientModel.getServerModel().setTradeOffer(new TradeOffer(0,1,1,0,0,0,0));
//		assertEquals("Client Model should have a valid trade offer and pass",
//				true, clientModel.canAcceptTrade());
//	}
	
//	@Test
//	public void testCanDiscardCards(){
//		//Status should be rolling, not discarding
//		assertEquals("Client Model's status should be Rolling and fail", false, 
//				clientModel.canDiscardCards(0, new ResourceHand(0,0,0,0,0)));
//		
//		//Change status to discarding, still fail because bad resource hand
//		clientModel.getServerModel().getTurnTracker().setStatus("Discarding");
//		assertEquals("Client Model's status should be Discarding and ResourceHand all 0s and fail", false,
//				clientModel.canDiscardCards(0, new ResourceHand(0,0,0,0,0)));
//		
//		//Set resources and pass in a good resource hand
//		clientModel.getServerModel().getPlayers().get(0).setResources(new Resources(3,3,2,2,2));
//		assertEquals("Client Model's status should be Discarding and ResourceHand all 1's and pass", true,
//				clientModel.canDiscardCards(0, new ResourceHand(1,1,1,1,1)));
//		
//	}
//	
//	@Test
//	public void testCanBuildRoad(){
//		
//	}
//	
//	@Test
//	public void testCanBuildSettlement(){
//		
//	}
//	
//	@Test
//	public void testCanBuildCity(){
//		
//	}
//	
//	@Test
//	public void testCanOfferTrade(){
//		
//	}
//	
//	@Test
//	public void testCanMaritimeTrade(){
//		
//	}
//	
//	@Test
//	public void testFinishTurn(){
//		
//	}
//	
//	@Test
//	public void testCanBuyDevCard(){
//		
//	}
//	
//	@Test
//	public void testCanPlayYearOfPlenty(){
//		
//	}
//	
//	@Test
//	public void testCanPlayRoadBuilding(){
//		
//	}
	
	@Test
	public void testCanPlaySoldier(){
		//Status should be playing, not rolling
		DevCards cards = new DevCards();
		cards.updateCards(1, 0, 0, 0, 0);
		clientModel.getServerModel().getTurnTracker().setStatus("rolling");
		clientModel.getServerModel().getTurnTracker().setCurrentTurn(0);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		
		assertEquals("Client Model's status should be Rolling and fail", false, 
				clientModel.canPlayMonopoly(0));
		
		//Change status to discarding, still fail because not the player 1's turn
		clientModel.getServerModel().getTurnTracker().setStatus("Playing");
		assertEquals("Client Model's status should Playing and fail", false,
				clientModel.canPlayMonopoly(1));
		
		//Now checking player 0, whose turn it is, but still fail because player 0 does not have a monopoly card
		cards.updateCards(0, 0, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		assertEquals("Client Model's status should be Playing and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonopoly(0));
		
		//Player 0 now has monopoly card but still fail because player 0 flag for already played devCard is set.
		cards.updateCards(1, 0, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(true);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonopoly(0));
		
		//All preconditions are set so test should be successful
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 1's and pass", true,
				clientModel.canPlayMonopoly(0));
	}
	
	@Test
	public void testCanPlayMonopoly(){
		//Status should be playing, not rolling
		DevCards cards = new DevCards();
		cards.updateCards(1, 0, 0, 0, 0);
		clientModel.getServerModel().getTurnTracker().setStatus("rolling");
		clientModel.getServerModel().getTurnTracker().setCurrentTurn(0);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		
		assertEquals("Client Model's status should be Rolling and fail", false, 
				clientModel.canPlayMonopoly(0));
		
		//Change status to discarding, still fail because not the player 1's turn
		clientModel.getServerModel().getTurnTracker().setStatus("Playing");
		assertEquals("Client Model's status should Playing and fail", false,
				clientModel.canPlayMonopoly(1));
		
		//Now checking player 0, whose turn it is, but still fail because player 0 does not have a monopoly card
		cards.updateCards(0, 0, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		assertEquals("Client Model's status should be Playing and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonopoly(0));
		
		//Player 0 now has monopoly card but still fail because player 0 flag for already played devCard is set.
		cards.updateCards(1, 0, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(true);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonopoly(0));
		
		//All preconditions are set so test should be successful
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 1's and pass", true,
				clientModel.canPlayMonopoly(0));
	}
	
	@Test
	public void testCanPlayMonument(){
		//Status should be playing, not rolling
		DevCards cards = new DevCards();
		cards.updateCards(0, 1, 0, 0, 0);
		clientModel.getServerModel().getTurnTracker().setStatus("rolling");
		clientModel.getServerModel().getTurnTracker().setCurrentTurn(0);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		
		assertEquals("Client Model's status should be Rolling and fail", false, 
				clientModel.canPlayMonument(0));
		
		//Change status to discarding, still fail because not the player 1's turn
		clientModel.getServerModel().getTurnTracker().setStatus("Playing");
		assertEquals("Client Model's status should Playing and fail", false,
				clientModel.canPlayMonument(1));
		
		//Now checking player 0, whose turn it is, but still fail because player 0 does not have a monument card
		cards.updateCards(0, 0, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		assertEquals("Client Model's status should be Playing and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonument(0));
		
		//Player 0 now has monument card but still fail because player 0 flag for already played devCard is set.
		cards.updateCards(0, 1, 0, 0, 0);
		clientModel.getServerModel().getPlayers().get(0).setOldDevCards(cards);
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(true);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 0s and fail", false,
				clientModel.canPlayMonument(0));
		
		//All preconditions are set so test should be successful
		clientModel.getServerModel().getPlayers().get(0).setPlayedDevCard(false);
		assertEquals("Client Model's status should be Discarding and ResourceHand all 1's and pass", true,
				clientModel.canPlayMonument(0));
	}
	
	public static void main(String[] args) 
	{
		String[] testClasses = new String[] 
		{
				"client.TaylorJUnit"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
	final String gameJSON = "{\n" + 
			"  \"deck\": {\n" + 
			"    \"yearOfPlenty\": 2,\n" + 
			"    \"monopoly\": 2,\n" + 
			"    \"soldier\": 14,\n" + 
			"    \"roadBuilding\": 2,\n" + 
			"    \"monument\": 5\n" + 
			"  },\n" + 
			"  \"map\": {\n" + 
			"    \"hexes\": [\n" + 
			"      {\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": -2\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"brick\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -2\n" + 
			"        },\n" + 
			"        \"number\": 4\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wood\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": -2\n" + 
			"        },\n" + 
			"        \"number\": 11\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"brick\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": -1\n" + 
			"        },\n" + 
			"        \"number\": 8\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wood\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": -1\n" + 
			"        },\n" + 
			"        \"number\": 3\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"ore\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -1\n" + 
			"        },\n" + 
			"        \"number\": 9\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"sheep\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": -1\n" + 
			"        },\n" + 
			"        \"number\": 12\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"ore\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 0\n" + 
			"        },\n" + 
			"        \"number\": 5\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"sheep\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": 0\n" + 
			"        },\n" + 
			"        \"number\": 10\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wheat\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 0\n" + 
			"        },\n" + 
			"        \"number\": 11\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"brick\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": 0\n" + 
			"        },\n" + 
			"        \"number\": 5\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wheat\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": 0\n" + 
			"        },\n" + 
			"        \"number\": 6\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wheat\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 1\n" + 
			"        },\n" + 
			"        \"number\": 2\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"sheep\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": 1\n" + 
			"        },\n" + 
			"        \"number\": 9\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wood\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 1\n" + 
			"        },\n" + 
			"        \"number\": 4\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"sheep\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": 1\n" + 
			"        },\n" + 
			"        \"number\": 10\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wood\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 2\n" + 
			"        },\n" + 
			"        \"number\": 6\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"ore\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": 2\n" + 
			"        },\n" + 
			"        \"number\": 3\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"resource\": \"wheat\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 2\n" + 
			"        },\n" + 
			"        \"number\": 8\n" + 
			"      }\n" + 
			"    ],\n" + 
			"    \"roads\": [\n" + 
			"      {\n" + 
			"        \"owner\": 2,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"S\",\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 3,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": -2\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 0,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"S\",\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 1,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 2,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"S\",\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 0\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 0,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": 0\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 1,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"S\",\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": -1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 3,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      }\n" + 
			"    ],\n" + 
			"    \"cities\": [],\n" + 
			"    \"settlements\": [\n" + 
			"      {\n" + 
			"        \"owner\": 3,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SE\",\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -2\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 2,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 0\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 2,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 1,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": -1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 0,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SE\",\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 1,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 0,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": 0\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"owner\": 3,\n" + 
			"        \"location\": {\n" + 
			"          \"direction\": \"SW\",\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      }\n" + 
			"    ],\n" + 
			"    \"radius\": 3,\n" + 
			"    \"ports\": [\n" + 
			"      {\n" + 
			"        \"ratio\": 2,\n" + 
			"        \"resource\": \"sheep\",\n" + 
			"        \"direction\": \"NW\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 3,\n" + 
			"          \"y\": -1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 2,\n" + 
			"        \"resource\": \"wheat\",\n" + 
			"        \"direction\": \"S\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -1,\n" + 
			"          \"y\": -2\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 2,\n" + 
			"        \"resource\": \"wood\",\n" + 
			"        \"direction\": \"NE\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -3,\n" + 
			"          \"y\": 2\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 3,\n" + 
			"        \"direction\": \"NW\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 2,\n" + 
			"          \"y\": 1\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 3,\n" + 
			"        \"direction\": \"SW\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 3,\n" + 
			"          \"y\": -3\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 3,\n" + 
			"        \"direction\": \"SE\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -3,\n" + 
			"          \"y\": 0\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 2,\n" + 
			"        \"resource\": \"brick\",\n" + 
			"        \"direction\": \"NE\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": -2,\n" + 
			"          \"y\": 3\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 3,\n" + 
			"        \"direction\": \"N\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 0,\n" + 
			"          \"y\": 3\n" + 
			"        }\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"ratio\": 2,\n" + 
			"        \"resource\": \"ore\",\n" + 
			"        \"direction\": \"S\",\n" + 
			"        \"location\": {\n" + 
			"          \"x\": 1,\n" + 
			"          \"y\": -3\n" + 
			"        }\n" + 
			"      }\n" + 
			"    ],\n" + 
			"    \"robber\": {\n" + 
			"      \"x\": 0,\n" + 
			"      \"y\": -2\n" + 
			"    }\n" + 
			"  },\n" + 
			"  \"players\": [\n" + 
			"    {\n" + 
			"      \"resources\": {\n" + 
			"        \"brick\": 0,\n" + 
			"        \"wood\": 1,\n" + 
			"        \"sheep\": 1,\n" + 
			"        \"wheat\": 1,\n" + 
			"        \"ore\": 0\n" + 
			"      },\n" + 
			"      \"oldDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"newDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"roads\": 13,\n" + 
			"      \"cities\": 4,\n" + 
			"      \"settlements\": 3,\n" + 
			"      \"soldiers\": 0,\n" + 
			"      \"victoryPoints\": 2,\n" + 
			"      \"monuments\": 0,\n" + 
			"      \"playedDevCard\": false,\n" + 
			"      \"discarded\": false,\n" + 
			"      \"playerID\": 0,\n" + 
			"      \"playerIndex\": 0,\n" + 
			"      \"name\": \"Sam\",\n" + 
			"      \"color\": \"orange\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"resources\": {\n" + 
			"        \"brick\": 1,\n" + 
			"        \"wood\": 0,\n" + 
			"        \"sheep\": 1,\n" + 
			"        \"wheat\": 0,\n" + 
			"        \"ore\": 1\n" + 
			"      },\n" + 
			"      \"oldDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"newDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"roads\": 13,\n" + 
			"      \"cities\": 4,\n" + 
			"      \"settlements\": 3,\n" + 
			"      \"soldiers\": 0,\n" + 
			"      \"victoryPoints\": 2,\n" + 
			"      \"monuments\": 0,\n" + 
			"      \"playedDevCard\": false,\n" + 
			"      \"discarded\": false,\n" + 
			"      \"playerID\": 1,\n" + 
			"      \"playerIndex\": 1,\n" + 
			"      \"name\": \"Brooke\",\n" + 
			"      \"color\": \"red\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"resources\": {\n" + 
			"        \"brick\": 0,\n" + 
			"        \"wood\": 1,\n" + 
			"        \"sheep\": 1,\n" + 
			"        \"wheat\": 1,\n" + 
			"        \"ore\": 0\n" + 
			"      },\n" + 
			"      \"oldDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"newDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"roads\": 13,\n" + 
			"      \"cities\": 4,\n" + 
			"      \"settlements\": 3,\n" + 
			"      \"soldiers\": 0,\n" + 
			"      \"victoryPoints\": 2,\n" + 
			"      \"monuments\": 0,\n" + 
			"      \"playedDevCard\": false,\n" + 
			"      \"discarded\": false,\n" + 
			"      \"playerID\": 10,\n" + 
			"      \"playerIndex\": 2,\n" + 
			"      \"name\": \"Pete\",\n" + 
			"      \"color\": \"red\"\n" + 
			"    },\n" + 
			"    {\n" + 
			"      \"resources\": {\n" + 
			"        \"brick\": 0,\n" + 
			"        \"wood\": 1,\n" + 
			"        \"sheep\": 1,\n" + 
			"        \"wheat\": 0,\n" + 
			"        \"ore\": 1\n" + 
			"      },\n" + 
			"      \"oldDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"newDevCards\": {\n" + 
			"        \"yearOfPlenty\": 0,\n" + 
			"        \"monopoly\": 0,\n" + 
			"        \"soldier\": 0,\n" + 
			"        \"roadBuilding\": 0,\n" + 
			"        \"monument\": 0\n" + 
			"      },\n" + 
			"      \"roads\": 13,\n" + 
			"      \"cities\": 4,\n" + 
			"      \"settlements\": 3,\n" + 
			"      \"soldiers\": 0,\n" + 
			"      \"victoryPoints\": 2,\n" + 
			"      \"monuments\": 0,\n" + 
			"      \"playedDevCard\": false,\n" + 
			"      \"discarded\": false,\n" + 
			"      \"playerID\": 11,\n" + 
			"      \"playerIndex\": 3,\n" + 
			"      \"name\": \"Mark\",\n" + 
			"      \"color\": \"green\"\n" + 
			"    }\n" + 
			"  ],\n" + 
			"  \"log\": {\n" + 
			"    \"lines\": [\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Mark\",\n" + 
			"        \"message\": \"Mark's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Pete\",\n" + 
			"        \"message\": \"Pete's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Brooke\",\n" + 
			"        \"message\": \"Brooke's turn just ended\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam built a road\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam built a settlement\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"source\": \"Sam\",\n" + 
			"        \"message\": \"Sam's turn just ended\"\n" + 
			"      }\n" + 
			"    ]\n" + 
			"  },\n" + 
			"  \"chat\": {\n" + 
			"    \"lines\": []\n" + 
			"  },\n" + 
			"  \"bank\": {\n" + 
			"    \"brick\": 23,\n" + 
			"    \"wood\": 21,\n" + 
			"    \"sheep\": 20,\n" + 
			"    \"wheat\": 22,\n" + 
			"    \"ore\": 22\n" + 
			"  },\n" + 
			"  \"turnTracker\": {\n" + 
			"    \"status\": \"Rolling\",\n" + 
			"    \"currentTurn\": 0,\n" + 
			"    \"longestRoad\": -1,\n" + 
			"    \"largestArmy\": -1\n" + 
			"  },\n" + 
			"  \"winner\": -1,\n" + 
			"  \"version\": 0\n" + 
			"}";
}


