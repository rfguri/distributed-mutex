import java.util.ArrayList;
import java.util.List;

public class CommMatrix {
	
	private List<List<List<Message>>> cm;
	private int s;
	
	public CommMatrix(int size) {
		this.setSize(size);
		this.init();
	}

	public List<List<List<Message>>> getCM() {
		return cm;
	}

	public void setCM(List<List<List<Message>>> cm) {
		this.cm = cm;
	}
	
	public void setSize(int s) {
		this.s = s;
	}
	
	public int getSize() {
		return s;
	}

	public Message getM(int c, int r) {
		Message m = null;
		if (!cm.get(c).get(r).isEmpty()) {
			m = cm.get(c).get(r).get(0);
			this.cm.get(c).get(r).remove(0);
		}
		return m;
	}

	public void setM(int c, int r, Message m) {
		this.cm.get(c).get(r).add(m);
	}
	
	private void init() {
		this.cm = new ArrayList<List<List<Message>>>(8);
		for (int i = 0; i < s; i++) {
			List<List<Message>> c = new ArrayList<List<Message>>(8);
			for(int j = 0; j < s; j++) c.add(new ArrayList<Message>());
			this.cm.add(c);
		}
	}
	
}
