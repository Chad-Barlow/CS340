package server.serverCommunicator;

import java.io.IOException;

import proxy.ITranslator;
import server.moves.IMovesFacade;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RollNumberHandler implements HttpHandler {

	public RollNumberHandler(ITranslator translator, IMovesFacade movesFacade) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub

	}

}