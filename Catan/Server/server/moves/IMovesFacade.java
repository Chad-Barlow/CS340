package server.moves;

import java.io.Serializable;

import client.exceptions.ClientModelException;
import server.cookie.CookieParams;
import shared.ServerMethodRequests.AcceptTradeRequest;
import shared.ServerMethodRequests.BuildCityRequest;
import shared.ServerMethodRequests.BuildRoadRequest;
import shared.ServerMethodRequests.BuildSettlementRequest;
import shared.ServerMethodRequests.BuyDevCardRequest;
import shared.ServerMethodRequests.DiscardCardsRequest;
import shared.ServerMethodRequests.FinishTurnRequest;
import shared.ServerMethodRequests.JoinGameRequest;
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
import shared.definitions.ServerModel;

/**
 * This interface defines a Facade containing the sendChat,
 * rollNumber, robPlayer, finishTurn, buyDevCard, Year_of_Plaenty, Road_Building, Soldier, 
 * Monopoly, Monument, buildRoad, buildSettlement, buildCity, 
 * offerTrade, acceptTrade, maritimeTrade, discardCards commands
 *
 */
public interface IMovesFacade {

	public ServerModel sendChat(SendChatRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel rollNumber(RollNumberRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel robPlayer(RobPlayerRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel finishTurn(FinishTurnRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel buyDevCard(BuyDevCardRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel yearOfPlenty(YearOfPlentyDevRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel roadBuilding(RoadBuildingDevRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel soldier(SoldierDevRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel monopoly(MonopolyDevRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel monument(MonumentDevRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel buildRoad(BuildRoadRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel buildSettlement(BuildSettlementRequest request, CookieParams cookie) throws InvalidMovesRequest, ClientModelException;
	public ServerModel buildCity(BuildCityRequest request, CookieParams cookie) throws InvalidMovesRequest, ClientModelException; 
	public ServerModel offerTrade(OfferTradeRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel acceptTrade(AcceptTradeRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel maritimeTrade(MaritimeTradeRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public ServerModel discardCards(DiscardCardsRequest request, CookieParams cookie) throws InvalidMovesRequest;
	public Serializable getModelsList();
}
