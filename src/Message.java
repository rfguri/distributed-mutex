
public class Message {

	public static final String Start = "Start";
	public static final String Token = "Token";
	public static final String Request = "Request";
	public static final String Acknowledge = "Acknowledge";
	public static final String Release = "Release";
	public static final String Finish = "Finish";
	public static final String Test = "Test";
	
	private String m;
	private int t;
	
	public Message(String m, int t) {
		this.m = m;
		this.t = t;
	}

	public String getM() {
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}
	
	public void brodcastMessage(int pid, int p, CommMatrix cm, Message m) {
		int start = (p == Protocol.Ricart) ? 2 : (cm.getSize() / 2) + 1;
		int end = (p == Protocol.Ricart) ? (cm.getSize() / 2) + 1 : cm.getSize();
		for (int i = start; i < end; i++) {
			this.sendMessage(pid, i, cm, m);
		}
	}
	
	public void sendMessage(int pid, int i, CommMatrix cm, Message m) {
		cm.setM(pid, i, m);
	}
	
}
