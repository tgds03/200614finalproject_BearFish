import java.util.*;

public class Game implements Listener {

	//set game stage size {rows, cols}
	public static final int STAGE_ROWS = 16;
	public static final int STAGE_COLS = 32;

	public boolean isVictory = false, isGameOver = false;
	public int score = 20;
	List<GameObject> objects;
	Window window;
	Bear bear;
	int fishNum;

	public Game(Window window, int fishNum) {
		int x, y;
		objects = new ArrayList<GameObject>();
		bear = new Bear(0, 0);
		this.window = window;
		this.fishNum = fishNum;

		for(int i = 0; i < fishNum; i++) {
			x = (int)(Math.random() * STAGE_COLS);
			y = (int)(Math.random() * STAGE_ROWS);
			objects.add(new Fish(x, y));
		}
		objects.add(bear);
		
		bear.setUpdateListener(0, this);
		bear.setUpdateListener(1, window);

		window.setControlListener(bear.inputRespond);
	}

	//return gameObject list to draw these in gui
	public Iterable<GameObject> objectListIter() {
		return objects;
	}

	//calculate game procedure, execute by event from Bear
	public void update() {

		for (GameObject object: objects) {
			object.update();
		}
		score--;

		//collision check
		Iterator<GameObject> iter = objects.iterator();
		GameObject now;
		while(iter.hasNext()) {
			now = iter.next();
			if (now instanceof Fish && now.collide(bear)) {
				iter.remove();
				fishNum--;
				score += 15;
			}
		}

		//check victory
		if (fishNum <= 0) {
			isVictory = true;
			bear.updateEvent.receivers.remove(this);
			return;
		}
		
		//check loose
		if (score <= 0) {
		    isGameOver = true;
		    bear.updateEvent.receivers.remove(this);
		    return;
		}
	}
}