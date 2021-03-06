package server.moves;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import client.exceptions.ClientModelException;
import server.cookie.CookieParams;
import shared.ServerMethodRequests.AcceptTradeRequest;
import shared.ServerMethodRequests.BuildCityRequest;
import shared.ServerMethodRequests.BuildRoadRequest;
import shared.ServerMethodRequests.BuildSettlementRequest;
import shared.ServerMethodRequests.BuyDevCardRequest;
import shared.ServerMethodRequests.DiscardCardsRequest;
import shared.ServerMethodRequests.FinishTurnRequest;
import shared.ServerMethodRequests.MaritimeTradeRequest;
import shared.ServerMethodRequests.MonopolyDevRequest;
import shared.ServerMethodRequests.MonumentDevRequest;
import shared.ServerMethodRequests.OfferTradeRequest;
import shared.ServerMethodRequests.RoadBuildingDevRequest;
import shared.ServerMethodRequests.RobPlayerRequest;
import shared.ServerMethodRequests.RollNumberRequest;
import shared.ServerMethodRequests.SendChatRequest;
import shared.ServerMethodRequests.SoldierDevRequest;
import shared.ServerMethodRequests.YearOfPlentyDevRequest;
import shared.definitions.RoadLocation;
import shared.definitions.ServerModel;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.City;
import shared.model.Hex;
import shared.model.Map;
import shared.model.Player;
import shared.model.Bank;
import shared.model.DevCards;
import shared.model.Road;
import shared.model.Settlement;
import shared.model.TradeOffer;

/**
 * This Facade implements the sendChat,
 * rollNumber, robPlayer, finishTurn, buyDevCard, Year_of_Plaenty, Road_Building, Soldier, 
 * Monopoly, Monument, buildRoad, buildSettlement, buildCity, 
 * offerTrade, acceptTrade, maritimeTrade, discardCards commands
 *
 */
public class MovesFacade implements IMovesFacade {

	private ArrayList<ServerModel> serverModels;
	
	public MovesFacade(ArrayList<ServerModel> serverModels){
		this.serverModels = serverModels;
	}
	
	@Override
	public ServerModel sendChat(SendChatRequest request,CookieParams cookie) throws InvalidMovesRequest{
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid send chat request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		int playerIndex = request.getPlayerIndex();
		String playerName = serverGameModel.getPlayers().get(playerIndex).getName();
		String message = request.getContent();
		
		serverGameModel.getChat().addMessage(playerName, message);
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel rollNumber(RollNumberRequest request,CookieParams cookie) throws InvalidMovesRequest{
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid roll number request");
		} 
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());

		int number=request.getNumber();
		
		if(number==7){
			int player1TotResources = serverGameModel.getPlayers().get(0).getResourceCount();
			int player2TotResources = serverGameModel.getPlayers().get(1).getResourceCount();
			int player3TotResources = serverGameModel.getPlayers().get(2).getResourceCount();
			int player4TotResources = serverGameModel.getPlayers().get(3).getResourceCount();
			
			if (player1TotResources > 7 || player2TotResources > 7 || player3TotResources > 7 || player4TotResources > 7) {
				if (player1TotResources <= 7) {
					serverGameModel.getPlayers().get(0).setDiscarded(true);
				}
				
				if (player2TotResources <= 7) {
					serverGameModel.getPlayers().get(1).setDiscarded(true);
				}
				
				if (player3TotResources <= 7) {
					serverGameModel.getPlayers().get(2).setDiscarded(true);
				}
				
				if (player4TotResources <= 7) {
					serverGameModel.getPlayers().get(3).setDiscarded(true);
				}
				
				serverGameModel.getTurnTracker().setStatus("Discarding");
			}
			else {
				serverGameModel.getTurnTracker().setStatus("Robbing");
			}
			
			serverGameModel.incrementVersion();
			return serverGameModel;
		}
		
		List<City> cities = serverGameModel.getMap().getCities();
		List<Settlement> settlements = serverGameModel.getMap().getSettlements();
		List<Hex> hexes = serverGameModel.getMap().getHexes();
		List<Hex> withNumber=new ArrayList<Hex>();
		VertexLocation NE,E,SE,SW,W,NW,loc;
		int owner;
		String resource;
		
		//execute
		for(int i=0;i<hexes.size();i++){
			if(hexes.get(i).getChit()==number && !serverGameModel.getMap().getRobber().getLocation().equals(hexes.get(i)))
				withNumber.add(hexes.get(i));
		}
		
		for(int i=0;i<withNumber.size();i++){
			NE=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.NorthEast).getNormalizedLocation();
			E=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.East).getNormalizedLocation();
			SE=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.SouthEast).getNormalizedLocation();
			SW=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.SouthWest).getNormalizedLocation();
			W=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.West).getNormalizedLocation();
			NW=new VertexLocation(withNumber.get(i).getLocation(),VertexDirection.NorthWest).getNormalizedLocation();
			resource=withNumber.get(i).getResourceType();
			for(int c=0;c<cities.size();c++){
				loc=cities.get(c).getLocation().getNormalizedLocation();
				owner=cities.get(c).getOwnerIndex();
				if(loc.equals(NE)){
					incrementResources(serverGameModel,owner,resource,2);
				}
				if(loc.equals(E)){
					incrementResources(serverGameModel,owner,resource,2);
				}
				if(loc.equals(SE)){
					incrementResources(serverGameModel,owner,resource,2);
				}
				if(loc.equals(SW)){
					incrementResources(serverGameModel,owner,resource,2);
				}
				if(loc.equals(W)){
					incrementResources(serverGameModel,owner,resource,2);
				}
				if(loc.equals(NW)){
					incrementResources(serverGameModel,owner,resource,2);
				}
			}
			for(int s=0;s<settlements.size();s++){
				loc=settlements.get(s).getLocation().getNormalizedLocation();
				owner=settlements.get(s).getOwnerIndex();
				if(loc.equals(NE)){
					incrementResources(serverGameModel,owner,resource,1);
				}
				if(loc.equals(E)){
					incrementResources(serverGameModel,owner,resource,1);
				}
				if(loc.equals(SE)){
					incrementResources(serverGameModel,owner,resource,1);
				}
				if(loc.equals(SW)){
					incrementResources(serverGameModel,owner,resource,1);
				}
				if(loc.equals(W)){
					incrementResources(serverGameModel,owner,resource,1);
				}
				if(loc.equals(NW)){
					incrementResources(serverGameModel,owner,resource,1);
				}
			}
		}
		
		serverGameModel.getTurnTracker().setStatus("Playing");
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel robPlayer(RobPlayerRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid rob player request");
		}
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		if (request.getVictimIndex() >= 0 && request.getVictimIndex() < 4) {
			
			Player player=serverGameModel.getPlayers().get(request.getPlayerIndex());
			Player target=serverGameModel.getPlayers().get(request.getVictimIndex());
			
			
			ArrayList<String> potentialLoot = new ArrayList<String>();
			
			if (target.getResourceCount() > 0) {
				if (target.getResources().brick > 0)
					potentialLoot.add("brick");
				if (target.getResources().ore > 0)
					potentialLoot.add("ore");
				if (target.getResources().sheep > 0)
					potentialLoot.add("sheep");
				if (target.getResources().wheat > 0)
					potentialLoot.add("wheat");
				if (target.getResources().wood > 0)
					potentialLoot.add("wood");
				
				Random randomGenerator = new Random();
				int lootIndex = randomGenerator.nextInt(potentialLoot.size());
				
				String loot=potentialLoot.get(lootIndex);
				if(loot.equals("wood")){
					player.setWood(player.getWood()+1);
					target.setWood(target.getWood()-1);
				}else if(loot.equals("wheat")){
					player.setWheat(player.getWheat()+1);
					target.setWheat(target.getWheat()-1);
				}else if(loot.equals("ore")){
					player.setOre(player.getOre()+1);
					target.setOre(target.getOre()-1);
				}else if(loot.equals("brick")){
					player.setBrick(player.getBrick()+1);
					target.setBrick(target.getBrick()-1);
				}else if(loot.equals("sheep")){
					player.setSheep(player.getSheep()+1);
					target.setSheep(target.getSheep()-1);
				}
			}
		}
		serverGameModel.incrementVersion();
		serverGameModel.getTurnTracker().setStatus("Playing");
		return serverGameModel;
	}

	@Override
	public ServerModel finishTurn(FinishTurnRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner=request.getPlayerIndex();
		Player player = serverGameModel.getPlayers().get(owner);
		
		DevCards newCards=player.getNewDevCards();
		DevCards oldCards=player.getOldDevCards();
		oldCards.setMonopoly(oldCards.getMonopoly()+newCards.getMonopoly());
//		oldCards.setMonument(oldCards.getMonument()+newCards.getMonument());
		oldCards.setRoadBuilding(oldCards.getRoadBuilding()+newCards.getRoadBuilding());
		oldCards.setSoldier(oldCards.getSoldier()+newCards.getSoldier());
		oldCards.setYearOfPlenty(oldCards.getYearOfPlenty()+newCards.getYearOfPlenty());
		newCards.reset();
		player.setPlayedDevCard(false);
		//player.setNewDevCards(newCards);
		//player.setOldDevCards(oldCards);
		
		serverGameModel.getTurnTracker().nextTurn();
		serverGameModel.incrementVersion();
		
		if ((serverGameModel.getTurnTracker().getStatus().equals("SecondRound") && request.getPlayerIndex() == 0) || 
				!serverGameModel.getTurnTracker().getStatus().equals("FirstRound") && !serverGameModel.getTurnTracker().getStatus().equals("SecondRound")) {
			serverGameModel.getTurnTracker().setStatus("Rolling");
		}
	
		return serverGameModel;
	}

	@Override
	public ServerModel buyDevCard(BuyDevCardRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid buy dev card request");
		} 
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner=request.getPlayerIndex();
		Player player = serverGameModel.getPlayers().get(owner);

		DevCards card=serverGameModel.getDeck();
		Bank bank = serverGameModel.getBank();
		Random rand=new Random();
		
		if(player.getSheep() > 0 && player.getWheat() > 0 && player.getOre() > 0 && 
				card.getTotalDevCardCount() > 0){
			int c=rand.nextInt(card.getTotalDevCardCount());
			if(c<card.getSoldier()){
				player.getNewDevCards().setSoldier(player.getNewDevCards().getSoldier()+1);
				card.setSoldier(card.getSoldier()-1);
			}else if(c<card.getSoldier()+card.getMonument()){
				player.getOldDevCards().setMonument(player.getOldDevCards().getMonument()+1);
				card.setMonument(card.getMonument()-1);
			}else if(c<card.getSoldier()+card.getMonument()+card.getMonopoly()){
				player.getNewDevCards().setMonopoly(player.getNewDevCards().getMonopoly()+1);
				card.setMonopoly(card.getMonopoly()-1);
			}else if(c<card.getSoldier()+card.getMonument()+card.getMonopoly()+card.getRoadBuilding()){
				player.getNewDevCards().setRoadBuilding(player.getNewDevCards().getRoadBuilding()+1);
				card.setRoadBuilding(card.getRoadBuilding()-1);
			}else{
				player.getNewDevCards().setYearOfPlenty(player.getNewDevCards().getYearOfPlenty()+1);
				card.setYearOfPlenty(card.getYearOfPlenty()-1);
			}
			
			player.setSheep(player.getSheep()-1);
			player.setWheat(player.getWheat()-1);
			player.setOre(player.getOre()-1);
			bank.setSheep(bank.getSheep()+1);
			bank.setWheat(bank.getWheat()+1);
			bank.setOre(bank.getOre()+1);
			
		}
		else{
			throw new InvalidMovesRequest("Error: invalid buy dev card request-not enought resources or not any monument cards in deck");
		}
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel yearOfPlenty(YearOfPlentyDevRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		Bank bank = serverGameModel.getBank();
		int owner = request.getPlayerIndex();
		Player player=serverGameModel.getPlayers().get(owner);
		
		if (!player.hasPlayedDevCard()) {
			DevCards cards = player.getOldDevCards();
			cards.setYearOfPlenty(cards.getYearOfPlenty()-1);
	
			String resource1 = request.getResource1();
			String resource2 = request.getResource2();
			List<String> resources = new ArrayList<String>();
			resources.add(resource1);
			resources.add(resource2);
			for(String resource : resources){	
				switch(resource.toLowerCase()){
				case "brick":
					if(bank.getBrick()>0){
						bank.setBrick(bank.getBrick()-1);
						player.setBrick(player.getBrick()+1);
					}
					else {
						//throw no more of that resource error
					}
					break;
				case "ore":
					if(bank.getOre()>0){
						bank.setOre(bank.getOre()-1);	
						player.setOre(player.getOre()+1);				
					}
					else {
						//throw no more of that resource error
					}	
					break;
				case "wood":
					if(bank.getWood()>0){
						bank.setWood(bank.getWood()-1);
						player.setWood(player.getWood()+1);					
					}
					else {
						//throw no more of that resource error
					}
					break;
				case "sheep":
					if(bank.getSheep()>0){
						bank.setSheep(bank.getSheep()-1);	
						player.setSheep(player.getSheep()+1);				
					}
					else {
						//throw no more of that resource error
					}
					break;
				case "wheat":
					if(bank.getWheat()>0){
						bank.setWheat(bank.getWheat()-1);		
						player.setWheat(player.getWheat()+1);			
					}
					else {
						//throw no more of that resource error
					}
					break;
				}
			}
			
			player.setPlayedDevCard(true);
		}
		//player.setOldDevCards(cards);
		//serverGameModel.setBank(bank);
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel roadBuilding(RoadBuildingDevRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner = request.getPlayerIndex();
		Player player=serverGameModel.getPlayers().get(owner);
	
		if (!player.hasPlayedDevCard()) {
			RoadLocation spot1 = new RoadLocation(request.getSpot1().getX(), request.getSpot1().getY(), EdgeDirection.valueOf(request.getSpot1().getDirection().name()));
			RoadLocation spot2 = new RoadLocation(request.getSpot2().getX(), request.getSpot2().getY(), EdgeDirection.valueOf(request.getSpot2().getDirection().name()));
			
			Map map = serverGameModel.getMap();
			map.getRoads().add(new Road(owner,spot1.getX(),spot1.getY(),spot1.getDirectionStr()));
			map.getRoads().add(new Road(owner,spot2.getX(),spot2.getY(),spot2.getDirectionStr()));
			
			player.setRoads(player.getRoads()-2);
			
			DevCards cards = player.getOldDevCards();
			cards.setRoadBuilding(cards.getRoadBuilding()-1);
			//serverGameModel.getPlayers().get(owner).setOldDevCards(cards);
			
			checkForLongestRoad(serverGameModel, owner);
			
			player.setPlayedDevCard(true);
		}
		//serverGameModel.setMap(map);
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel soldier(SoldierDevRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner = request.getPlayerIndex();
		Player player=serverGameModel.getPlayers().get(owner);		
		
		if (!player.hasPlayedDevCard()) {
			
			if (request.getVictimIndex() >= 0 && request.getVictimIndex() < 4) {
				
				Player target=serverGameModel.getPlayers().get(request.getVictimIndex());
				
				
				ArrayList<String> potentialLoot = new ArrayList<String>();
				
				if (target.getResourceCount() > 0) {
					if (target.getResources().brick > 0)
						potentialLoot.add("brick");
					if (target.getResources().ore > 0)
						potentialLoot.add("ore");
					if (target.getResources().sheep > 0)
						potentialLoot.add("sheep");
					if (target.getResources().wheat > 0)
						potentialLoot.add("wheat");
					if (target.getResources().wood > 0)
						potentialLoot.add("wood");
					
					Random randomGenerator = new Random();
					int lootIndex = randomGenerator.nextInt(potentialLoot.size());
					
					String loot=potentialLoot.get(lootIndex);
					if(loot.equals("wood")){
						player.setWood(player.getWood()+1);
						target.setWood(target.getWood()-1);
					}else if(loot.equals("wheat")){
						player.setWheat(player.getWheat()+1);
						target.setWheat(target.getWheat()-1);
					}else if(loot.equals("ore")){
						player.setOre(player.getOre()+1);
						target.setOre(target.getOre()-1);
					}else if(loot.equals("brick")){
						player.setBrick(player.getBrick()+1);
						target.setBrick(target.getBrick()-1);
					}else if(loot.equals("sheep")){
						player.setSheep(player.getSheep()+1);
						target.setSheep(target.getSheep()-1);
					}
				}
			}
			
			DevCards cards = player.getOldDevCards();
			cards.setSoldier(cards.getSoldier()-1);
			//player.setOldDevCards(cards);
			player.setSoldiers(player.getSoldiers()+1);
	
			checkForLargestArmy(serverGameModel, owner);
			
			player.setPlayedDevCard(true);
		}

		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel monopoly(MonopolyDevRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner = request.getPlayerIndex();
		Player player=serverGameModel.getPlayers().get(owner);
		Player other;
		
		if (!player.hasPlayedDevCard()) {
			String resource = request.getResource();
			int amount=0;
			for(int i=0;i<serverGameModel.getPlayers().size();i++){
				if(i!=owner){
					other=serverGameModel.getPlayers().get(i);
					switch(resource){
					case "ore":
						if(other.getOre()>0){
							amount=other.getOre();
							player.setOre(player.getOre()+amount);
							other.setOre(0);
						}
						break;
					case "sheep":
						if(other.getSheep()>0){
							amount=other.getSheep();
							player.setSheep(player.getSheep()+amount);
							other.setSheep(0);
						}
						break;
					case "wood":
						if(other.getWood()>0){
							amount=other.getWood();
							player.setWood(player.getWood()+amount);
							other.setWood(0);
						}
						break;
					case "wheat":
						if(other.getWheat()>0){
							amount=other.getWheat();
							player.setWheat(player.getWheat()+amount);
							other.setWheat(0);
						}
						break;
					case "brick":
						if(other.getBrick()>0){
							amount=other.getBrick();
							player.setBrick(player.getBrick()+amount);
							other.setBrick(0);
						}
						break;
					}
				}
			}
			player.setPlayedDevCard(true);
		}
		
		DevCards cards = player.getOldDevCards();
		cards.setMonopoly(cards.getMonopoly()-1);
		serverGameModel.getPlayers().get(owner).setOldDevCards(cards);
		
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel monument(MonumentDevRequest request, CookieParams cookie) {
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		int owner = request.getPlayerIndex();
		Player player=serverGameModel.getPlayers().get(owner);
		
		player.setVictoryPoints(player.getVictoryPoints()+1);
		
		DevCards cards = player.getOldDevCards();
		cards.setMonument(cards.getMonument()-1);
		player.setOldDevCards(cards);

		checkForWinner(serverGameModel,owner);
		
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel buildRoad(BuildRoadRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid build city request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		int playerIndex = request.getPlayerIndex();
		Player player = serverGameModel.getPlayers().get(playerIndex);
		int x = request.getRoadLocation().getX();
		int y = request.getRoadLocation().getY(); 
		String direction = request.getRoadLocation().getDirectionStr();
		Road road = new Road(playerIndex, x, y , direction);
		serverGameModel.getMap().getRoads().add(road);
		serverGameModel.incrementVersion();
		
		player.decrementRoads();
		
		if (!request.isFree()) {
			player.decrementBrick();
			player.decrementWood();
			serverGameModel.getBank().brick += 1;
			serverGameModel.getBank().wood += 1;
		}

		checkForLongestRoad(serverGameModel, playerIndex);
		
		return serverGameModel;
	}

	@Override
	public ServerModel buildSettlement(BuildSettlementRequest request, CookieParams cookie) throws InvalidMovesRequest, ClientModelException {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid build city request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		int playerIndex = request.getPlayerIndex();
		Player player = serverGameModel.getPlayers().get(playerIndex);
		int x = request.getVertexLocation().getX();
		int y = request.getVertexLocation().getY(); 
		String direction = request.getVertexLocation().getDirectionStr();
		Settlement settlement = new Settlement(playerIndex, x, y , direction);
		serverGameModel.getMap().getSettlements().add(settlement);
		serverGameModel.incrementVersion();
		player.decrementSettlements();
		player.incrementVictoryPoints();
		
		if (!request.isFree()) {
			player.decrementBrick();
			player.decrementSheep();
			player.decrementWheat();
			player.decrementWood();
			serverGameModel.getBank().brick += 1;
			serverGameModel.getBank().sheep += 1;
			serverGameModel.getBank().wheat += 1;
			serverGameModel.getBank().wood += 1;
			serverGameModel.getBank().ore += 1;
		}
		
		if (serverGameModel.getTurnTracker().getStatus().equals("SecondRound")) {
			
			HexLocation hexLoc = new HexLocation(x, y);
			HexLocation neighbor1 = null;
			HexLocation neighbor2 = null;
			
			switch(direction) {
			case "NW":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.NorthWest);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.North);
				break;
			case "NE":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.NorthEast);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.North);
				break;
			case "E":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.NorthEast);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.SouthEast);
				break;
			case "SE":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.SouthEast);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.South);
				break;
			case "SW":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.SouthWest);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.South);
				break;
			case "W":
				neighbor1 = hexLoc.getNeighborLoc(EdgeDirection.NorthWest);
				neighbor2 = hexLoc.getNeighborLoc(EdgeDirection.SouthWest);
				break;
			}
			
			for (Hex hex : serverGameModel.getMap().getHexes()) {
				int hexX = hex.getLocation().getX();
				int hexY = hex.getLocation().getY();
				
				if ((hexX == hexLoc.getX() && hexY == hexLoc.getY()) ||
						(hexX == neighbor1.getX() && hexY == neighbor1.getY()) ||
						(hexX == neighbor2.getX() && hexY == neighbor2.getY())) {
					
					String resource = hex.getResourceType();
					
					if (resource != null) {
					
						switch (resource) {
						case "brick":
								player.setBrick(player.getBrick()+1);
								serverGameModel.getBank().brick--;
							break;
						case "ore":
							player.setOre(player.getOre()+1);
							serverGameModel.getBank().ore--;
							break;
						case "sheep":
								player.setSheep(player.getSheep()+1);
								serverGameModel.getBank().sheep--;
							break;
						case "wheat":
								player.setWheat(player.getWheat()+1);
								serverGameModel.getBank().wheat--;
							break;
						case "wood":
								player.setWood(player.getWood()+1);
								serverGameModel.getBank().wood--;
							break;
						}
					}
				}
			}
			
		}
		
		checkForWinner(serverGameModel,playerIndex);
		
		return serverGameModel;
	}

	@Override
	public ServerModel buildCity(BuildCityRequest request, CookieParams cookie) throws InvalidMovesRequest, ClientModelException {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid build city request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		int playerIndex = request.getPlayerIndex();
		Player player = serverGameModel.getPlayers().get(playerIndex);
		int x = request.getCityLocation().getX();
		int y = request.getCityLocation().getY(); 
		String direction = request.getCityLocation().getDirectionStr();
		City city = new City(playerIndex, x, y , direction);
		serverGameModel.getMap().getCities().add(city);
		serverGameModel.incrementVersion();
		
		ArrayList<Settlement> settlements = serverGameModel.getMap().getSettlements();
		
		for (int i = 0; i < settlements.size(); i++) {
			VertexLocation loc = settlements.get(i).getLocation().getNormalizedLocation();
			if (settlements.get(i).getLocation().getHexLoc().getX() == x &&
					settlements.get(i).getLocation().getHexLoc().getY() == y &&
					settlements.get(i).getLocation().getDir().getDirectionStr().equals(direction)) {
				settlements.remove(i);
				break;
			}
			
			if (loc.getHexLoc().getX() == x &&
					loc.getHexLoc().getY() == y &&
					loc.getDir().getDirectionStr().equals(direction)) {
				settlements.remove(i);
				break;
			}
				
		}
		
		player.decrementCities();
		player.incrementVictoryPoints();
		player.incrementSettlements();
		//3 ore 2wheat
		int newOre = player.getResources().getOre() - 3;
		int newWheat = player.getResources().getWheat() - 2;
		player.getResources().setOre(newOre);
		player.getResources().setWheat(newWheat);
		serverGameModel.getBank().ore += 3;
		serverGameModel.getBank().wheat += 2;
		
		checkForWinner(serverGameModel,playerIndex);
		
		return serverGameModel;
	}

	@Override
	public ServerModel offerTrade(OfferTradeRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid offer trade request");
		} 
		
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		int brick = request.getOffer().getBrick();
		int ore = request.getOffer().getOre();
		int sheep = request.getOffer().getSheep();
		int wheat = request.getOffer().getWheat();
		int wood = request.getOffer().getWood();
		
		TradeOffer tradeOffer = new TradeOffer(request.getPlayerIndex(), request.getReceiver(), brick, ore, sheep, wheat, wood);
		serverGameModel.setTradeOffer(tradeOffer);
		serverGameModel.incrementVersion();
		return serverGameModel;
	}

	@Override
	public ServerModel acceptTrade(AcceptTradeRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid accept trade request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		if (request.isWillAccept()) {

			//execute
			int sender = serverGameModel.getTradeOffer().getSender();
			int receiver = serverGameModel.getTradeOffer().getReceiver();
			int brick = serverGameModel.getTradeOffer().getOffer().getBrick();
			int ore = serverGameModel.getTradeOffer().getOffer().getOre();
			int sheep = serverGameModel.getTradeOffer().getOffer().getSheep();
			int wheat = serverGameModel.getTradeOffer().getOffer().getWheat();
			int wood = serverGameModel.getTradeOffer().getOffer().getWood();
			
			int senderBrick = serverGameModel.getPlayers().get(sender).getBrick() - brick;
			int senderOre = serverGameModel.getPlayers().get(sender).getOre() - ore;
			int senderSheep = serverGameModel.getPlayers().get(sender).getSheep() - sheep;
			int senderWheat = serverGameModel.getPlayers().get(sender).getWheat() - wheat;
			int senderWood = serverGameModel.getPlayers().get(sender).getWood() - wood;
			
			serverGameModel.getPlayers().get(sender).setBrick(senderBrick);
			serverGameModel.getPlayers().get(sender).setOre(senderOre);
			serverGameModel.getPlayers().get(sender).setSheep(senderSheep);
			serverGameModel.getPlayers().get(sender).setWheat(senderWheat);
			serverGameModel.getPlayers().get(sender).setWood(senderWood);
			
			int receiverBrick = serverGameModel.getPlayers().get(receiver).getBrick() + brick;
			int receiverOre = serverGameModel.getPlayers().get(receiver).getOre() + ore;
			int receiverSheep = serverGameModel.getPlayers().get(receiver).getSheep() + sheep;
			int receiverWheat = serverGameModel.getPlayers().get(receiver).getWheat() + wheat;
			int receiverWood = serverGameModel.getPlayers().get(receiver).getWood() + wood;
			
			serverGameModel.getPlayers().get(receiver).setBrick(receiverBrick);	
			serverGameModel.getPlayers().get(receiver).setOre(receiverOre);
			serverGameModel.getPlayers().get(receiver).setSheep(receiverSheep);
			serverGameModel.getPlayers().get(receiver).setWheat(receiverWheat);
			serverGameModel.getPlayers().get(receiver).setWood(receiverWood);
		
		}
		serverGameModel.incrementVersion();
		serverGameModel.setTradeOffer(null);
		return serverGameModel;
	}

	@Override
	public ServerModel maritimeTrade(MaritimeTradeRequest request, CookieParams cookie) throws InvalidMovesRequest {
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid maritime trade request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		//execute
		Player player = serverGameModel.getPlayers().get(request.getPlayerIndex());
		int ratio = request.getRatio();
		String inputResource = request.getInputResource().toLowerCase();
		String outputResource = request.getOutputResource().toLowerCase();
		
		switch (inputResource) {
		case "brick":
			int playerBrick = player.getBrick() - ratio;
			player.setBrick(playerBrick);
			serverGameModel.getBank().brick += ratio;
			break;
		case "ore":
			int playerOre = player.getOre() - ratio;
			player.setOre(playerOre);
			serverGameModel.getBank().ore += ratio;
			break;
		case "sheep":
			int playerSheep = player.getSheep() - ratio;
			player.setSheep(playerSheep);
			serverGameModel.getBank().sheep += ratio;
			break;
		case "wheat":
			int playerWheat = player.getWheat() - ratio;
			player.setWheat(playerWheat);
			serverGameModel.getBank().wheat += ratio;
			break;
		case "wood":
			int playerWood = player.getWood() - ratio;
			player.setWood(playerWood);
			serverGameModel.getBank().wood += ratio;
			break;
		}
		
		switch (outputResource) {
		case "brick":
			int playerBrick = player.getBrick() + 1;
			player.setBrick(playerBrick);
			serverGameModel.getBank().brick -= 1;
			break;
		case "ore":
			int playerOre = player.getOre() + 1;
			player.setOre(playerOre);
			serverGameModel.getBank().ore -= 1;
			break;
		case "sheep":
			int playerSheep = player.getSheep() + 1;
			player.setSheep(playerSheep);
			serverGameModel.getBank().sheep -= 1;
			break;
		case "wheat":
			int playerWheat = player.getWheat() + 1;
			player.setWheat(playerWheat);
			serverGameModel.getBank().wheat -= 1;
			break;
		case "wood":
			int playerWood = player.getWood() + 1;
			player.setWood(playerWood);
			serverGameModel.getBank().wood -= 1;
			break;
		}
		
		return serverGameModel;
	}

	@Override
	public ServerModel discardCards(DiscardCardsRequest request, CookieParams cookie) throws InvalidMovesRequest{
		if(request == null) {
			throw new InvalidMovesRequest("Error: invalid discard cards request");
		} 
		
		ServerModel serverGameModel = serverModels.get(cookie.getGameID());
		
		serverGameModel.getBank().brick += request.getDiscardedCards().getBrick();
		serverGameModel.getBank().ore += request.getDiscardedCards().getOre();
		serverGameModel.getBank().sheep += request.getDiscardedCards().getSheep();
		serverGameModel.getBank().wheat += request.getDiscardedCards().getWheat();
		serverGameModel.getBank().wood += request.getDiscardedCards().getWood();
		
		
		Player player = serverGameModel.getPlayers().get(request.getPlayerIndex());
		int playerBrick = player.getBrick() - request.getDiscardedCards().getBrick();
		int playerOre = player.getOre() - request.getDiscardedCards().getOre();
		int playerSheep = player.getSheep() - request.getDiscardedCards().getSheep();
		int playerWheat = player.getWheat() - request.getDiscardedCards().getWheat();
		int playerWood = player.getWood() - request.getDiscardedCards().getWood();
		
		player.setBrick(playerBrick);
		player.setOre(playerOre);
		player.setSheep(playerSheep);
		player.setWheat(playerWheat);
		player.setWood(playerWood);

		
		if (!serverGameModel.getPlayers().get(player.getPlayerIndex()).isDiscarded()) {
			serverGameModel.getPlayers().get(player.getPlayerIndex()).setDiscarded(true);
		}
		
		if (serverGameModel.getPlayers().get(0).isDiscarded() && 
				serverGameModel.getPlayers().get(1).isDiscarded() && 
				serverGameModel.getPlayers().get(2).isDiscarded() && 
				serverGameModel.getPlayers().get(3).isDiscarded()) {
			
			serverGameModel.getTurnTracker().setStatus("Robbing");
			serverGameModel.getPlayers().get(0).setDiscarded(false);
			serverGameModel.getPlayers().get(1).setDiscarded(false);
			serverGameModel.getPlayers().get(2).setDiscarded(false);
			serverGameModel.getPlayers().get(3).setDiscarded(false);
		}
		
		serverGameModel.incrementVersion();
		
		return serverGameModel;
	}
	
	private void checkForLongestRoad(ServerModel game, int currentPlayerIndex){

		int longest = 4;

		Player currentPlayer = game.getPlayers().get(currentPlayerIndex);

		int playerWith=game.getTurnTracker().getLongestRoad();

		
		
		if(playerWith == -1 && 15-currentPlayer.getRoads() > longest) { // no one has it yet

			playerWith = currentPlayerIndex;
			currentPlayer.incrementVictoryPoints();
			currentPlayer.incrementVictoryPoints();
		}

		else if(playerWith > -1 && playerWith < game.getPlayers().size() && 15-currentPlayer.getRoads() > 15-game.getPlayers().get(playerWith).getRoads()) { // take it from who has it
			game.getPlayers().get(playerWith).decrementVictoryPoints();
       	 	game.getPlayers().get(playerWith).decrementVictoryPoints();
       	 	playerWith = currentPlayerIndex;
       	 	currentPlayer.incrementVictoryPoints();
       	 	currentPlayer.incrementVictoryPoints();

		}

		if(playerWith!=-1) {

			game.getTurnTracker().setLongestRoad(playerWith);

		}

	}
	
	private void checkForLargestArmy(ServerModel game, int currentPlayerIndex){
		 Player currentPlayer = game.getPlayers().get(currentPlayerIndex);

         int largest=2;

         int playerWith=game.getTurnTracker().getLargestArmy();

         if(playerWith == -1 && currentPlayer.getSoldiers() > largest) { // no one has it yet

                 playerWith = currentPlayerIndex;
                 currentPlayer.incrementVictoryPoints();
                 currentPlayer.incrementVictoryPoints();

         }

         else if(playerWith > -1 && playerWith < game.getPlayers().size() && currentPlayer.getSoldiers() > game.getPlayers().get(playerWith).getSoldiers()) { // take it from who has it
        	 game.getPlayers().get(playerWith).decrementVictoryPoints();
        	 game.getPlayers().get(playerWith).decrementVictoryPoints();
             playerWith = currentPlayerIndex;
             currentPlayer.incrementVictoryPoints();
             currentPlayer.incrementVictoryPoints();

         }

         if(playerWith!=-1) {

                 game.getTurnTracker().setLargestArmy(playerWith);

         }
	}
	
	private void checkForWinner(ServerModel game,int owner){
		int points=game.getPlayers().get(owner).getVictoryPoints();
		if(points>=10)
			game.setWinner(owner);
	}
	
	private void incrementResources(ServerModel game,int owner,String resource,int amount){
		if(resource.equals("wood") && game.getBank().wood > 0){
			game.getPlayers().get(owner).setWood(game.getPlayers().get(owner).getWood()+amount);
			game.getBank().wood -= amount;
		}else if(resource.equals("sheep") && game.getBank().sheep > 0){
			game.getPlayers().get(owner).setSheep(game.getPlayers().get(owner).getSheep()+amount);
			game.getBank().sheep -= amount;
		}else if(resource.equals("ore") && game.getBank().ore > 0){
			game.getPlayers().get(owner).setOre(game.getPlayers().get(owner).getOre()+amount);
			game.getBank().ore -= amount;
		}else if(resource.equals("wheat")  && game.getBank().wheat > 0){
			game.getPlayers().get(owner).setWheat(game.getPlayers().get(owner).getWheat()+amount);
			game.getBank().wheat -= amount;
		}else if(resource.equals("brick")  && game.getBank().brick > 0){
			game.getPlayers().get(owner).setBrick(game.getPlayers().get(owner).getBrick()+amount);
			game.getBank().brick -= amount;
		}
	}

	@Override
	public Serializable getModelsList() {
		return this.serverModels;
	}
}
