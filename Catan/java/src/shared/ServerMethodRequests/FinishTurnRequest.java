package shared.ServerMethodRequests;

public class FinishTurnRequest {
	private String type;
	private int playerIndex;
	
	public FinishTurnRequest(int playerIndex) {
		this.type = "finishTurn";
		this.playerIndex = playerIndex;
	}

	public String getType() {
		return type;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	
}
