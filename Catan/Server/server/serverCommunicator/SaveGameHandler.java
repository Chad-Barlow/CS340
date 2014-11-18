package server.serverCommunicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import proxy.ITranslator;
import server.games.GamesFacade;
import server.games.IGamesFacade;
import shared.ServerMethodRequests.JoinGameRequest;
import shared.ServerMethodRequests.SaveGameRequest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for SaveGame command
 * @author Chad
 *
 */
public class SaveGameHandler implements HttpHandler {

	private ITranslator translator;
	private IGamesFacade gamesFacade;

	public SaveGameHandler(ITranslator translator, IGamesFacade gamesFacade) {
		this.translator = translator;
		this.gamesFacade = gamesFacade;
	}

	/**
	 * Handles Save Game. 
	 * @param exchange: the exchange to be handled. 
	 * @pre The handler will be given the proper values to carry out the exchange.
	 * @post no post as there is no return value. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("In Save Game handler.");
		
		String responseMessage = "";
		
		if(exchange.getRequestMethod().toLowerCase().equals("post")) {
			BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
			String inputLine;
			StringBuffer requestJson = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				requestJson.append(inputLine);
			}
			in.close();
			
			System.out.println(requestJson);
			
			SaveGameRequest request = (SaveGameRequest) translator.translateFrom(requestJson.toString(), SaveGameRequest.class);
			exchange.getRequestBody().close();
			
			if(gamesFacade.validateGameID(request.getId())){
				try{
					gamesFacade.saveGame(request.getId(), request.getName());
					responseMessage = "Successfully wrote game to file: " + request.getId();
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				}catch(IOException e){
					System.out.println("Error writing to file.");
					responseMessage = e.getMessage();
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
				}
			}
		}

	}

}
