package server.serverCommunicator;

import java.io.IOException;

import proxy.ITranslator;
import server.game.IGameFacade;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for various game commands
 * @author Chad
 *
 */
public class GameCommandsHandler implements HttpHandler {

	private IGameFacade gameFacade;
	private ITranslator translator;

	public GameCommandsHandler(ITranslator translator, IGameFacade gameFacade) {
		this.translator = translator;
		this.gameFacade = gameFacade;
	}

	/**
	 * Handles Game Commands. 
	 * @param exchange: the exchange to be handled. 
	 * @pre The handler will be given the proper values to carry out the exchange.
	 * @post no post as there is no return value. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub

	}

}
