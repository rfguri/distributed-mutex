
public class Token extends Process {

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
					System.out.println();
					if (pid == 1) for (int j = (cm.getSize() / 2) + 1; j < cm.getSize(); j++) m.sendMessage(j, pid, cm, new Message(Message.Start, t));
					if (pid == 0) for (int j = 2; j < (cm.getSize() / 2) + 1; j++) m.sendMessage(j, pid, cm, new Message(Message.Start, t));
					//try { Thread.sleep(500); } catch (InterruptedException e) {}
				} 
				else if (m.getM().equals(Message.Finish)) {
					if (acc == 3) {
						acc = 1;
						if (pid == 1) cm.setM(1, pid, new Message(Message.Token, t));
						else if (pid == 0) cm.setM(0, pid, new Message(Message.Token, t));
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
