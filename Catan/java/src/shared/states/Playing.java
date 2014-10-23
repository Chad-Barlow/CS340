package shared.states;

import shared.ServerMethodResponses.GetGameModelResponse;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import client.presenter.IPresenter;

public class Playing extends GamePlay {
	public Playing() {
		super("Playing");
	}
	
	@Override 
	public GetGameModelResponse getGameModel(IPresenter presenter) {
		return presenter.getProxy().getGameModel(presenter.getVersion(), presenter.getCookie());
	}
	
	@Override
	public void buildRoad(IPresenter presenter, EdgeLocation roadLocation) {
		presenter.getProxy().buildRoad(presenter.getPlayerInfo().getIndex(), roadLocation, false, presenter.getCookie());
	}
	
	@Override
	public void buildSettlement(IPresenter presenter, VertexLocation vertLoc) {
		presenter.getProxy().buildSettlement(presenter.getPlayerInfo().getIndex(), vertLoc, false, presenter.getCookie());
	}
}
