
public class Token extends Process {

	private static final boolean DEBUG = true;
	
	private int acc;
	
	public Token(int pid, int t, CommMatrix cm, int p) {
		super(pid, t, cm, p);
		this.setAcc(1);
	}
	
	public void handleMessage() {
		for (int i = 0; i < cm.getSize(); i++) {
			Message m = cm.getM(pid, i);
			if (m != null) {
				if (m.getM().equals(Message.Token)) {
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d)\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT());
					System.out.println();
					if (pid == 1) for (int j = (cm.getSize() / 2) + 1; j < cm.getSize(); j++) m.sendMessage(j, pid, cm, new Message(Message.Start, t));
					if (pid == 0) for (int j = 2; j < (cm.getSize() / 2) + 1; j++) m.sendMessage(j, pid, cm, new Message(Message.Start, t));
				} 
				else if (m.getM().equals(Message.Finish)) {
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d)\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT());
					if (acc == 3) {
						if (pid == 1) cm.setM(pid, 0, new Message(Message.Token, t));
						else if (pid == 0) cm.setM(pid, 1, new Message(Message.Token, t));
						acc = 1;
					} else acc++;
				}
			}
		}
	}

	public int getAcc() {
		return acc;
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}
	
}
