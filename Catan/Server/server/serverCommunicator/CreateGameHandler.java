package server.serverCommunicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import proxy.ITranslator;
import server.commands.games.CreateCommand;
import server.commands.games.IGamesCommandLog;
import server.games.IGamesFacade;
import server.games.InvalidGamesRequest;
import shared.ServerMethodRequests.CreateGameRequest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler for create game command
 *
 */
public class CreateGameHandler implements HttpHandler {

	private ITranslator translator;
	private IGamesFacade gamesFacade;
	private IGamesCommandLog gamesLog;

	public CreateGameHandler(ITranslator translator, IGamesFacade gamesFacade, IGamesCommandLog gamesLog) {
		this.translator = translator;
		this.gamesFacade = gamesFacade;
		this.gamesLog = gamesLog;
	}

	/**
	 * Handles the Create Game. 
	 * @param exchange: the exchange to be handled. 
	 * @pre The handler will be given the proper values to carry out the exchange.
	 * @post no post as there is no return value. 
	 */
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		System.out.println("In create game handler");
		
		String responseMessage = "";
		
		if(exchange.getRequestMethod().toLowerCase().equals("post")) {
			try {  // check user login cookie and if valid get params
				//String unvalidatedCookie = exchange.getRequestHeaders().get("Cookie").get(0);
				//Cookie.verifyCookie(unvalidatedCookie, translator);

				exchange.getResponseHeaders().set("Content-Type", "appliction/json");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
				String inputLine;
				StringBuffer requestJson = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					requestJson.append(inputLine);
				}
				in.close();
				
				System.out.println(requestJson.toString());
				CreateGameRequest request = (CreateGameRequest) translator.translateFrom(requestJson.toString(), CreateGameRequest.class);
				exchange.getRequestBody().close();
				System.out.println("About to translate.");
				responseMessage = this.translator.translateTo(this.gamesFacade.createGame(request));
				System.out.println("Finished translating");
				// TODO Create empty game model and add to gameModels list
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				gamesLog.Store(new CreateCommand(gamesFacade, request));
				
			} catch (/*InvalidCookieException |*/ InvalidGamesRequest e) { // else send error message
				responseMessage = e.getMessage();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
			}
		}
		else {
			// unsupported request method
			responseMessage = "Error: \"" + exchange.getRequestMethod() + "\" is not supported!";
			System.out.println("Invalid request: " + exchange.getRequestMethod());
		}
		
		System.out.println(responseMessage + "\n\n");
		
		OutputStreamWriter writer = new OutputStreamWriter(
				exchange.getResponseBody());
		writer.write(responseMessage);
		writer.flush();
		writer.close();
		
		exchange.getResponseBody().close();
		System.out.println("Closed exchange response body.");
	}

}
