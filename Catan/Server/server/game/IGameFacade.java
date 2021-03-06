package server.game;

import java.util.List;

import shared.ServerMethodRequests.AddAIRequest;
import shared.ServerMethodRequests.ResetGameRequest;
import shared.definitions.ServerModel;

/**
 * This interface defines a Facade containing the model, reset, commands, addAI, listAI commands
 *
 */
public interface IGameFacade {
	public ServerModel getGameModel(int id);
	public int resetGame(ResetGameRequest request);
	public int gameCommands();
	public int addAI(AddAIRequest request);
	public boolean validGameID(int id);
	public List<String> listAI();

}
