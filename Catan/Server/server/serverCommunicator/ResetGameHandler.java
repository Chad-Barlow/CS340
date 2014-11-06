package server.serverCommunicator;

import java.io.IOException;

import proxy.ITranslator;
import server.game.IGameFacade;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for RestGame command
 * @author Chad
 *
 */
public class ResetGameHandler implements HttpHandler {

	public ResetGameHandler(ITranslator translator, IGameFacade gameFacade) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Handles Reset Game. 
	 * @param exchange: the exchange to be handled. 
	 * @pre The handler will be given the proper values to carry out the exchange.
	 * @post no post as there is no return value. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub

	}

}