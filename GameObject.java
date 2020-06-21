import java.awt.event.*;

abstract class GameObject {
	private int x, y, dx, dy;
	String shape;

	public GameObject(int startX, int startY) {
		this.x = startX;
		this.y = startY;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public boolean collide(GameObject p) {
		if (this.x == p.getX() && this.y == p.getY()) {
			return true;
		} else {
			return false;
		}
	}

	public String getShape() {
		return shape;
	}
	
	public void update() {
		move(dx, dy);
	};

	public void setMove(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	private void move(int dx, int dy) {
		 if (0 <= this.x + dx && this.x + dx < Game.STAGE_COLS)
		 	this.x += dx;
		 if (0 <= this.y + dy && this.y + dy < Game.STAGE_ROWS)
		 	this.y += dy;
	}
}

class Bear extends GameObject {

	public InputRespond inputRespond;
	public Event updateEvent;

	public Bear(int startX, int startY) {
		super(startX, startY);
		super.shape = "B";
		inputRespond = new InputRespond();
		updateEvent = new Event();
	}

	public void update() {
		super.update();
	}

	//receive inputs, emit events to which should update
	private class InputRespond implements ActionListener, KeyListener{

		public void actionPerformed(ActionEvent e) {
			String actName = Window.decodeAction(e);
			if ("Up".equals(actName)) {
				setMove(0, -1);
			} else if ("Down".equals(actName)) {
				setMove(0, 1);
			} else if ("Left".equals(actName)) {
				setMove(-1, 0);
			} else if ("Right".equals(actName)) {
				setMove(1, 0);
			} else {
				return;
			}
			updateEvent.sayUpdate();
		}

		public void keyPressed(KeyEvent e) {
			char keycode = (char)e.getKeyChar();
			if (keycode == 'w') {
				setMove(0, -1);
			} else if (keycode == 's') {
				setMove(0, 1);
			} else if (keycode == 'a') {
				setMove(-1, 0);
			} else if (keycode == 'd') {
				setMove(1, 0);
			} else {
				return;
			}
			updateEvent.sayUpdate();
		}
		
		public void keyReleased(KeyEvent e) {};
		public void keyTyped(KeyEvent e) {};

	}
	
	public void setUpdateListener(int idx, Listener o) {updateEvent.setListener(idx, o);}

}

class Fish extends GameObject {

	private int distance = 2;
	public Fish(int startX, int startY) {
		super(startX, startY);
		int dx, dy;
		dx = (int)(signRandom() * distance);
		dy = (int)(signRandom() * distance);
		setMove(dx, dy);
		super.shape = "F";
	}

	public void update() {
		super.update();
		if (Math.random() > 0.5) {
			int dx, dy;
			dx = (int)(signRandom() * distance);
			dy = (int)(signRandom() * distance);
			setMove(dx, dy);
		}

	}

	//return random value in [-1, 1]
	private double signRandom() {
		return 2*Math.random()-1;
	}
}