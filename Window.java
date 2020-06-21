import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements Listener {

	public static final int objectWidth = 300/Game.STAGE_COLS;
	public static final int objectHeight = 300/Game.STAGE_ROWS;

	public Game game;
	private Container contentPane;
	private JPanel controlPanel, scorePanel;
	private JLabel winlooseLabel, scoreLabel;
	private JButton controlButtons[];
	private InputListener inputListener;	//inner class
	private Screen screen; //ineer class
	private String stageData[][];

	//convert ActionEvents to opcode
	public static String decodeAction(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			return ((JButton)e.getSource()).getText();
		} else {
			return null;
		}
	}

	public Window() {
		setSize(320, 480);
		setResizable(false);
		setFocusable(true);
		setTitle("BearFish");
		inputListener = new InputListener();

		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		int width = Game.STAGE_COLS;
		int height = Game.STAGE_ROWS;
		
		//init screen and game stage
		//game objects will be stored in Character form
		screen = new Screen();
		screen.setLayout(new FlowLayout(FlowLayout.CENTER, 120, 120));
		screen.setPreferredSize(new Dimension(300, 300));
		stageData = new String[height][width];
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++)
				stageData[i][j] = "";
		winlooseLabel = new JLabel("");
		screen.add(winlooseLabel);

		//control panel init
		controlPanel = new JPanel();
		controlPanel.setLayout(new BorderLayout());
		controlPanel.setFocusable(false);

		//control buttons init
		controlButtons = new JButton[5];
		controlButtons[0] = new JButton("Up"); controlPanel.add("North", controlButtons[0]);
		controlButtons[1] = new JButton("Down"); controlPanel.add("South", controlButtons[1]);
		controlButtons[2] = new JButton("Right"); controlPanel.add("East", controlButtons[2]);
		controlButtons[3] = new JButton("Left"); controlPanel.add("West", controlButtons[3]);
		controlButtons[4] = new JButton("Start!"); controlPanel.add("Center", controlButtons[4]);
		controlButtons[4].addActionListener(inputListener);
		for(int i = 0; i < 5; i++) controlButtons[i].setFocusable(false);

		//panel of score, etc...
		scorePanel = new JPanel();
		scorePanel.add(scoreLabel = new JLabel("Score : 0"));
		
		contentPane.add(screen);
		contentPane.add(scorePanel);
		contentPane.add(controlPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//ready to receive keyEvent
		contentPane.addKeyListener(inputListener);
		contentPane.setFocusable(true);
		contentPane.requestFocus();

	}

	class Screen extends JPanel {
		//draw screen
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 300, 300);
			// System.out.println(stageData);
			// if (stageData == null) return;
			for(int i = 0; i < Game.STAGE_ROWS; i++) {
				for(int j = 0; j < Game.STAGE_COLS; j++) {
					switch(stageData[i][j]) {
						case "B":
							g.setColor(Color.ORANGE);
							g.fillRect(j*objectWidth, i*objectHeight, objectWidth, objectHeight);
							break;
						case "F":
							g.setColor(Color.CYAN);
							g.fillRect(j*objectWidth, i*objectHeight, objectWidth, objectHeight);
							break;
						default: break;
					}
				}
			}
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, 300, 300);
			

		}
	}

	//update screen, will execute by event from Bear
	public void update() {
		if (game == null) return;
		Iterable<GameObject> iter = game.objectListIter(); 
		int x, y;
		stageClear();
		for(GameObject o: iter) {
			x = o.getX();
			y = o.getY();
			stageData[y][x] = o.getShape();
		}
		screen.repaint();
		scoreLabel.setText("Score : "  + game.score);

		//when Bear ate every fish
		if (game.isVictory) {
			winlooseLabel.setText("You Win!");
			game = null;
			return;
		}
		
		//when score is 0
		if (game.isGameOver) {
			winlooseLabel.setText("Game Over");
			game = null;
			return;
		}
	}

	//clear stagedata
	public void stageClear() {
		for(int i = 0; i < Game.STAGE_ROWS; i++) {
			for(int j = 0; j < Game.STAGE_COLS; j++) {
				stageData[i][j] = "";
			}
		}
	}

	//mapping gui input listener
	public <T extends ActionListener & KeyListener> void setControlListener(T o) {
		for(int i = 0; i < 4; i++)
			controlButtons[i].addActionListener(o);
		contentPane.addKeyListener(o);
	}

	//start button (or space key)
	private void newGame() {
		game = new Game(this, (int)(7 + Math.random()*7)); 
		winlooseLabel.setText("");
		update();
	}
	class InputListener implements ActionListener, KeyListener{

		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if ("Start!".equals(b.getText())) {
				newGame();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == ' ') {
				newGame();
			}
		}
		public void keyReleased(KeyEvent e) {};
		public void keyTyped(KeyEvent e) {};
	}

	

}
