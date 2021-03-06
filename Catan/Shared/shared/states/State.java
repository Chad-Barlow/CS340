package shared.states;

import java.util.Collections;
import java.util.List;

import shared.ServerMethodResponses.AddAIResponse;
import shared.ServerMethodResponses.ChangeLogLevelResponse;
import shared.ServerMethodResponses.CreateGameResponse;
import shared.ServerMethodResponses.GetGameCommandsResponse;
import shared.ServerMethodResponses.GetGameModelResponse;
import shared.ServerMethodResponses.ListAIResponse;
import shared.ServerMethodResponses.ListGamesResponse;
import shared.ServerMethodResponses.LoginUserResponse;
import shared.ServerMethodResponses.PostGameCommandsResponse;
import shared.ServerMethodResponses.RegisterUserResponse;
import shared.ServerMethodResponses.ResetGameResponse;
import shared.definitions.CatanColor;
import shared.definitions.ResourceHand;
import shared.definitions.ResourceType;
import shared.definitions.ServerLogLevel;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.Log;
import shared.model.Player;
import client.base.OverlayView;
import client.presenter.IPresenter;

public abstract class State implements IState {
	private String status;
	
	public State(String status) {
		this.status = status;
	}

	@Override
	public LoginUserResponse login(IPresenter presenter, String user, String pass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListGamesResponse listGames(IPresenter presenter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegisterUserResponse register(IPresenter presenter, String user, String pass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateGameResponse createGame(IPresenter presenter, boolean randTiles, boolean randNums,
			boolean randPorts, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void joinGame(IPresenter presenter, CatanColor color, int gameID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] listAIChoices(IPresenter presenter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildRoad(IPresenter presenter, EdgeLocation roadLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSettlement(IPresenter presenter, VertexLocation vertLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildCity(IPresenter presenter, VertexLocation vertLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollNumber(IPresenter presenter, int diceRoll) {
		// TODO Auto-generated method stub
		
	}
	
	public GetGameModelResponse getGameModel(IPresenter presenter) {
		// TODO Auto-generated method stub
		GetGameModelResponse response = presenter.getProxy().getGameModel(presenter.getClientModel().getServerModel().getVersion(), presenter.getCookie());
		//GetGameModelResponse response = presenter.getProxy().getGameModel(-1, presenter.getCookie());
		return response;
	}
	
	public String getStatus() {
		return status;
	}

	@Override
	public ResetGameResponse resetGame(IPresenter presenter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetGameCommandsResponse getGameCommands(IPresenter presenter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostGameCommandsResponse postGameCommands(IPresenter presenter,
			Log commands) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAIResponse listAI(IPresenter presenter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAI(IPresenter presenter, String aiToAdd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChangeLogLevelResponse changeLogLevel(IPresenter presenter,
			ServerLogLevel logLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendChat(IPresenter presenter, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptTrade(IPresenter presenter, boolean willAccept) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discardCards(IPresenter presenter, ResourceHand resourceHand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildRoad(IPresenter presenter, EdgeLocation roadLocation,
			boolean free) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildSettlement(IPresenter presenter,
			VertexLocation vertexLocation, boolean free) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offerTrade(IPresenter presenter, ResourceHand offer,
			int receiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maritimeTrade(IPresenter presenter, int ratio,
			ResourceType inputResource, ResourceType outputResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishTurn(IPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buyDevCard(IPresenter presenter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playYearOfPlentyCard(IPresenter presenter,
			ResourceType resource1, ResourceType resource2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playRoadBuildingCard(IPresenter presenter, EdgeLocation spot1,
			EdgeLocation spot2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playMonopolyCard(IPresenter presenter, ResourceType resource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSoldierCard(IPresenter presenter, int playerIndex, int victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void robPlayer(IPresenter presenter, int playerIndex, int victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playMonumentCard(IPresenter presenter) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isInAnyPlayingState() {
		return (status.equals("FirstRound") || 
				status.equals("SecondRound") || 
				status.equals("Rolling") || 
				status.equals("Playing") || 
				status.equals("Discarding") ||
				status.equals("Robbing"));
	}
	
	@Override
	public void update(IPresenter presenter) {
		
		GetGameModelResponse response = getGameModel(presenter);
		if(response != null && response.isSuccessful()) {
			if(response.isNeedToUpdate()) {
				System.out.println("UPDATING MODEL");
				
				List<Player> players = response.getGameModel().getPlayers();
				players.removeAll(Collections.singleton(null));
				if(players.size() == 4) {
					if(presenter.getState().getStatus().equals("PlayerWaiting")){
						OverlayView.closePlayerWaitingModal();
					}
					presenter.setStateBasedOffString(response.getGameModel().getTurnTracker().getStatus());
				}
				
				presenter.setVersion(response.getGameModel().getVersion());
				presenter.getClientModel().updateServerModel(response.getGameModel());
			}
		}
		else {
			System.err.println("Error: Unable to process update game model request!");
		}
	}
}
