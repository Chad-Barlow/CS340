package client.junit;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import proxy.ClientCommunicator;
import proxy.CommandResponse;
import proxy.Pair;
import proxy.RequestType;
import proxy.TranslatorJSON;
import shared.ServerMethodRequests.AcceptTradeRequest;
import shared.ServerMethodRequests.AddAIRequest;
import shared.ServerMethodRequests.BuildCityRequest;
import shared.ServerMethodRequests.BuildRoadRequest;
import shared.ServerMethodRequests.BuildSettlementRequest;
import shared.ServerMethodRequests.BuyDevCardRequest;
import shared.ServerMethodRequests.ChangeLogLevelRequest;
import shared.ServerMethodRequests.CreateGameRequest;
import shared.ServerMethodRequests.DiscardCardsRequest;
import shared.ServerMethodRequests.FinishTurnRequest;
import shared.ServerMethodRequests.JoinGameRequest;
import shared.ServerMethodRequests.MaritimeTradeRequest;
import shared.ServerMethodRequests.MonopolyDevRequest;
import shared.ServerMethodRequests.MonumentDevRequest;
import shared.ServerMethodRequests.OfferTradeRequest;
import shared.ServerMethodRequests.ResetGameRequest;
import shared.ServerMethodRequests.RoadBuildingDevRequest;
import shared.ServerMethodRequests.RollNumberRequest;
import shared.ServerMethodRequests.SendChatRequest;
import shared.ServerMethodRequests.SoldierDevRequest;
import shared.ServerMethodRequests.YearOfPlentyDevRequest;
import shared.definitions.GameDescription;
import shared.definitions.ResourceHand;
import shared.definitions.RoadLocation;
import shared.definitions.ServerModel;
import shared.definitions.User;
import shared.definitions.VertexLocationRequest;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.LogEntry;

/**
 * @author Chad
 *
 */
public class ClientCommunicatorTest {

	ClientCommunicator CCTestor;
	TranslatorJSON jsonTrans;
	
	List<Pair<String,String>> headers;
	User mockUser;
	String mockCommandName;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		jsonTrans = new TranslatorJSON();
		CCTestor = new ClientCommunicator("localhost", 8081, jsonTrans);
		headers = new ArrayList<Pair<String,String>>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for {@link proxy.ClientCommunicator#executeCommand(proxy.RequestType, java.util.List, java.lang.String, java.lang.Object, java.lang.Class)}.
	 */
	@Test
	public void connectUserLoginTest() {
		//Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		//headers.add(mockPair);
		mockUser = new User("Brooke", "brooke");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "user/login", mockUser, null);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testRegisterUser() {
		//Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Bob%22%2C%22password%22%3A%22bob%22%2C%22playerID%22%3A0%7D");
		//headers.add(mockPair);
		mockUser = new User("Test", "TEST");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "user/register", mockUser, null);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testGetGamesList() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		mockUser = new User("Brooke", "brooke");
		CommandResponse response = CCTestor.executeCommand(RequestType.GET, headers, "games/list", mockUser, GameDescription[].class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testCreateGame() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		CreateGameRequest data = new CreateGameRequest(true,true,true, "GAME OF DOOM!!");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "games/create", data, GameDescription.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	//@Test //This test causes problems Dont run until fixed
	public void testJoinGame() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		JoinGameRequest data = new JoinGameRequest(4, "green");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "games/join", data, RequestType.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	//@Test
	public void testGetGameModel() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		mockUser = new User("Brooke", "brooke");
		CommandResponse response = CCTestor.executeCommand(RequestType.GET, headers, "game/model", mockUser, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testGameReset() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D; catan.game=0");
		//Pair<String, String> mockPair2 = new Pair<String, String>("Cookie", "catan.game=0");		
		headers.add(mockPair);
		//headers.add(mockPair2);
		ResetGameRequest data = null; //new ResetGameRequest();
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "game/reset", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testGetGameCommands() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> mockPair2 = new Pair<String, String>("Cookie", "catan.game=0");
		headers.add(mockPair);
		headers.add(mockPair2);
		mockUser = new User("Brooke", "brooke");
		CommandResponse response = CCTestor.executeCommand(RequestType.GET, headers, "game/commands", null, LogEntry[].class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testPostGameCommands() {
		//Can be used for various game command calls
	}
	
	@Test
	public void testGetGameListAI() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		mockUser = new User("Brooke", "brooke");
		CommandResponse response = CCTestor.executeCommand(RequestType.GET, headers, "game/listAI", mockUser, String[].class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testAddAI() { 
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		AddAIRequest data = new AddAIRequest("LARGEST_ARMY");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "game/addAI", data, null);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testUtilChangeLogLevel() {
		Pair<String, String> mockPair = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		headers.add(mockPair);
		ChangeLogLevelRequest data = new ChangeLogLevelRequest("SEVERE");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "util/changeLogLevel", data, null);
		assertTrue(response.getResponseCode() == 200);
	}
	
	////Move Command Tests*****************
	
	@Test
	public void testSendChat() {
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		SendChatRequest mockChatRequest = new SendChatRequest(1, "Sup duuuuuuuude!!!!");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/sendChat", mockChatRequest, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testRollNumber(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		RollNumberRequest data = new RollNumberRequest(5, 1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/rollNumber", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test 
	public void testRobPlayer(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		SoldierDevRequest data = new SoldierDevRequest(1,2, new HexLocation(5,5));
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/robPlayer", data, RequestType.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testFinishTurn(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		FinishTurnRequest data = new FinishTurnRequest(1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/finishTurn", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testBuyDevCard(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		BuyDevCardRequest data = new BuyDevCardRequest(1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/buyDevCard", data, RequestType.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testYearOfPlenty(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		YearOfPlentyDevRequest data = new YearOfPlentyDevRequest(1, "sheep", "wood");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/Year_of_Plenty", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testRoadBuilding(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		RoadBuildingDevRequest data = new RoadBuildingDevRequest(1, null, null);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/Road_Building", data, RequestType.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testSoldier(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		SoldierDevRequest data = new SoldierDevRequest(1,2, new HexLocation(5,5));
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/Soldier", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testMonopoly(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		MonopolyDevRequest data = new MonopolyDevRequest(1, "sheep");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/Monopoly", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testMonument() {
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		MonumentDevRequest data = new MonumentDevRequest(1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/Monument", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testBuildRoad(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		BuildRoadRequest data = new BuildRoadRequest(1, new RoadLocation(0, 0, EdgeDirection.SouthEast), true);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/buildRoad", data, RequestType.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testBuildSettlement(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=0");
		headers.add(user);
		headers.add(game);		
		VertexLocationRequest vertex = new VertexLocationRequest(1,1,VertexDirection.NorthWest);
		BuildSettlementRequest data1 = new BuildSettlementRequest(1 ,vertex, true);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/buildSettlement", data1, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testBuildCity(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=0");
		headers.add(user);
		headers.add(game);
		VertexLocationRequest vertex = new VertexLocationRequest(3,4,VertexDirection.NorthWest);

		BuildCityRequest data = new BuildCityRequest(1, vertex);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/buildCity", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testOfferTrade(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		OfferTradeRequest data = new OfferTradeRequest(11, new ResourceHand(0,0,0,0,0), 1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/offerTrade", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
//	@Test
	public void testAcceptTrade(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		AcceptTradeRequest data = new AcceptTradeRequest(1, true);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/acceptTrade", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testMaritimeTrade(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		MaritimeTradeRequest data = new MaritimeTradeRequest(1,2,"sheep", "wood");
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/maritimeTrade", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
	
	@Test
	public void testDiscardCards(){
		Pair<String, String> user = new Pair<String, String>("Cookie", "catan.user=%7B%22name%22%3A%22Brooke%22%2C%22password%22%3A%22brooke%22%2C%22playerID%22%3A0%7D");
		Pair<String, String> game = new Pair<String, String>("Cookie", "catan.game=2");
		headers.add(user);
		headers.add(game);
		DiscardCardsRequest data = new DiscardCardsRequest(new ResourceHand(0,-1,-1,0,0),1);
		CommandResponse response = CCTestor.executeCommand(RequestType.POST, headers, "moves/discardCards", data, ServerModel.class);
		assertTrue(response.getResponseCode() == 200);
	}
}