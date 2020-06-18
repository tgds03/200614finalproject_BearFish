import java.util.ArrayList;

interface Listener {
	void update();
}

class Event {
	ArrayList<Listener> receivers;

	public Event() {
		receivers = new ArrayList<Listener>();
	}

	public void sayUpdate() {
		int size = receivers.size();
		for(int i = 0; i < size; i++) {
			receivers.get(i).update();
			if (size != receivers.size()) {
				size = receivers.size();
				i--;
			}
		}
	}

	public void setListener(Listener listener) {
		receivers.add(listener);
	}

	public void setListener(int idx, Listener listener) {
		receivers.add(idx, listener);
	}

	public void releaseListener(Listener listener) {
		receivers.remove(listener);
	}

}