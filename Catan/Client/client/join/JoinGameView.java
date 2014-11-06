package client.join;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import shared.definitions.GameDescription;
import shared.definitions.PlayerDescription;
import client.base.OverlayView;

/**
 * Implementation for the join game view, which lets the user select a game to
 * join
 */
@SuppressWarnings("serial")
public class JoinGameView extends OverlayView implements IJoinGameView
{

	private final int LABEL_TEXT_SIZE = 40;
	private final int PANEL_TEXT_SIZE = 14;
	private final int BUTTON_TEXT_SIZE = 28;
	private final int BORDER_WIDTH = 10;

	private JLabel label;
	private JLabel subLabel;

	private JLabel hash;
	private JLabel name;
	private JLabel currentPlayer;
	private JLabel join;

	private JButton createButton;
	private JButton tempJoinButton;

	private JPanel labelPanel;
	private JPanel gamePanel;
	private JPanel buttonPanel;

	//private GameInfo[] games;
	private List<GameDescription> games;
	private PlayerDescription localPlayer;

	public JoinGameView()
	{
		this.initialize();
	}

	private void initialize()
	{
		this.initializeView();
	}

	private void initializeView()
	{
		this.setOpaque(true);
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

		label = new JLabel("Welcome to the game hub");
		Font labelFont = label.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE);
		label.setFont(labelFont);
		subLabel = new JLabel("Join an existing game, or create your own");
		labelFont = subLabel.getFont();
		labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE * 2 / 3);
		subLabel.setFont(labelFont);

		labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		labelPanel.add(label);
		labelPanel.add(subLabel);
		this.add(labelPanel, BorderLayout.NORTH);


		// This is the header layout
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(0, 4));
		hash = new JLabel("#");
		labelFont = new Font(labelFont.getFontName(), Font.BOLD, PANEL_TEXT_SIZE);
		hash.setFont(labelFont);
		name = new JLabel("Name");
		name.setFont(labelFont);
		currentPlayer = new JLabel("Current Players");
		currentPlayer.setFont(labelFont);
		join = new JLabel("Join");
		join.setFont(labelFont);

		gamePanel.add(hash);
		gamePanel.add(name);
		gamePanel.add(currentPlayer);
		gamePanel.add(join);

		// This is the looped layout
		if (games != null && games.size() > 0)
		{
			labelFont = labelFont.deriveFont(labelFont.getStyle(), PANEL_TEXT_SIZE);
			for (GameDescription game : games)
			{
				JLabel tmp1 = new JLabel(String.valueOf(game.getId()));
				tmp1.setFont(labelFont);
				gamePanel.add(tmp1);
				JLabel tmp2 = new JLabel(game.getTitle());
				tmp2.setFont(labelFont);
				gamePanel.add(tmp2);
				List<PlayerDescription> playersList = game.getPlayerDescriptions();
				String players = String.valueOf(playersList.size())+ "/4 : ";
				//String players = String.valueOf(game.getPlayers().size()) + "/4 : ";
				for (int j = 0; j < playersList.size(); j++) {
					if (j < playersList.size() - 1) {
						players = players + playersList.get(j).getName() + ", ";
					} else {
						players = players + playersList.get(j).getName();
					}
				}
				JLabel tmp3 = new JLabel(players);
				tmp3.setFont(labelFont);
				gamePanel.add(tmp3);
				JButton joinButton;
				
				if (playersList.contains(localPlayer))
				{
					joinButton = new JButton("Re-Join");
				}
				else if (playersList.size() >= 4)
				{
					joinButton = new JButton("Full");
					joinButton.setEnabled(false);
				}
				else
				{
					joinButton = new JButton("Join");
				}
				joinButton.setActionCommand("" + game.getId());
				joinButton.addActionListener(actionListener);
				gamePanel.add(joinButton);
			}
		}

		//Add all the above
		this.add(gamePanel, BorderLayout.CENTER);

		tempJoinButton = new JButton("Temporary Join Button");
		tempJoinButton.addActionListener(actionListener);
		Font buttonFont = tempJoinButton.getFont();
		buttonFont = buttonFont.deriveFont(buttonFont.getStyle(), BUTTON_TEXT_SIZE);
		tempJoinButton.setFont(buttonFont);

		createButton = new JButton("Create Game");
		createButton.addActionListener(actionListener);
		createButton.setFont(buttonFont);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(createButton);
		buttonPanel.add(tempJoinButton);		
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	@Override
	public IJoinGameController getController()
	{
		return (IJoinGameController) super.getController();
	}

	@Override
	public void setGames(List<GameDescription> games, PlayerDescription localPlayer)
	{
		this.games = games;
		this.localPlayer = localPlayer;
		this.removeAll();
		this.initialize();
		this.repaint();
	}
	
	private ActionListener actionListener = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == createButton)
			{
				getController().startCreateNewGame();
			}
			else if (e.getSource() == tempJoinButton)
			{
				getController().startJoinGame(null);
			}
			else
			{
				try
				{
					//System.out.println(e.getActionCommand());
					int gameId = Integer.parseInt(e.getActionCommand());
					GameDescription game = null;
					for (GameDescription g : games)
					{
						if (g.getId() == gameId)
						{
							game = g;
							break;
						}
					}
					getController().startJoinGame(game);
				}
				catch (NumberFormatException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	};
}

