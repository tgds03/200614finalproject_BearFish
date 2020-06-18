import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements Listener {

	public Game game;
	private Container contentPane;
	private JPanel gameStage, controlPanel, scorePanel;
	private JLabel stageLabels[][], scoreLabel;
	private JButton controlButtons[];
	private InputListener inputListener;	//inner class

	//convert ActionEvents to opcode
	public static String decodeAction(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			return ((JButton)e.getSource()).getText();
		} else {
			return null;
		}
	}

	public Window() {
		setSize(300, 400);
		setResizable(false);
		setFocusable(true);
		setTitle("BearFish");
		inputListener = new InputListener();

		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());

		//gameStage panel init
		gameStage = new JPanel();
		int width = Game.STAGE_COLS;
		int height = Game.STAGE_ROWS;
		gameStage.setLayout(new GridLayout(height, width));
		
		//elements in gameStage init
		//game objects will be represented by text
		stageLabels = new JLabel[height][width];
		for(int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				stageLabels[i][j] = new JLabel(".  ");
				gameStage.add(stageLabels[i][j]);
			}
		}

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
		
		contentPane.add(gameStage);
		contentPane.add(scorePanel);
		contentPane.add(controlPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//ready to receive keyEvent
		contentPane.addKeyListener(inputListener);
		contentPane.setFocusable(true);
		contentPane.requestFocus();
	}

	//update screen, will execute by event from Bear
	public void update() {
		if (game == null) return;
		Iterable<GameObject> iter = game.objectListIter(); 
		int x, y;
		screenClear();
		for(GameObject o: iter) {
			x = o.getX();
			y = o.getY();
			stageLabels[y][x].setText(o.getShape());
		}
		scoreLabel.setText("Score : "  + game.score);

		if (game.isGameOver) {
			String strWin = "You Win!";
			int len = strWin.length();
			int width = Game.STAGE_COLS;
			int height = Game.STAGE_ROWS;
			for(int i = 0; i < len; i++)
				stageLabels[height/2][width/2 - len/2 + i].setText(Character.toString(strWin.charAt(i)));
			game = null;
		}
	}

	public void screenClear() {
		for(int i = 0; i < Game.STAGE_ROWS; i++) {
			for(int j = 0; j < Game.STAGE_COLS; j++) {
				stageLabels[i][j].setText(".");
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
		update();
	}
	class InputListener implements ActionListener, KeyListener{

		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if ("Start".equals(b.getText())) {
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
