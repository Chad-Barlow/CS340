package server.serverCommunicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import proxy.ITranslator;
import server.commands.games.IGamesCommandLog;
import server.commands.games.LoadCommand;
import server.games.IGamesFacade;
import shared.ServerMethodRequests.LoadGameRequest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for the LoadGame command
 * @author Chad
 *
 */
public class LoadGameHandler implements HttpHandler {

	private ITranslator translator;
	private IGamesFacade gamesFacade;
	private IGamesCommandLog log;

	public LoadGameHandler(ITranslator translator, IGamesFacade gamesFacade, IGamesCommandLog log) {
		this.translator = translator;
		this.gamesFacade = gamesFacade;
		this.log = log;
	}

	/**
	 * Handles Load Game. 
	 * @param exchange: the exchange to be handled. 
	 * @pre The handler will be given the proper values to carry out the exchange.
	 * @post no post as there is no return value. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("In Load Game handler.");
		
		String responseMessage = "";
		
		if(exchange.getRequestMethod().toLowerCase().equals("post")) {
			exchange.getResponseHeaders().set("Content-Type", "appliction/json");
			BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
			String inputLine;
			StringBuffer requestJson = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				requestJson.append(inputLine);
			}
			in.close();
			
			System.out.println(requestJson);
			
			LoadGameRequest request = (LoadGameRequest) translator.translateFrom(requestJson.toString(), LoadGameRequest.class);
			exchange.getRequestBody().close();
				try{
					int id = gamesFacade.loadGame(request.getName());
					responseMessage = "Successfully loaded game: " + request.getName() + " with id " + id;
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					log.Store(new LoadCommand(gamesFacade, request.getName()));
				}catch(IOException e){
					System.out.println("Error reading file. IOException");
					responseMessage = e.getMessage();
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
				}
		}
		else {
			// unsupported request method
			responseMessage = "Error: \"" + exchange.getRequestMethod() + "\" is not supported!";
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
		}		
				if (!responseMessage.isEmpty()) {
					//send failure response message
					OutputStreamWriter writer = new OutputStreamWriter(
							exchange.getResponseBody());
					writer.write(responseMessage);
					writer.flush();
					writer.close();
				}
				exchange.getResponseBody().close();
	}
}
