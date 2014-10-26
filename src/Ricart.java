
public class Ricart extends Process {

	private static final boolean DEBUG = true;
	
	private int acc;
	private int[] q;
	private boolean executed;
	
	public Ricart(int pid, int t, CommMatrix cm, int p) {
		super(pid, t, cm, p);
		this.setAcc(1);
		this.setQ(new int[3]);
		this.setExecuted(false);
	}
	
	public void handleMessage() {
		for (int i = 0; i < 8; i++) {
			Message m = cm.getM(pid, i);
			if (m != null) {
				if (m.getM().equals(Message.Start)) {
					m.brodcastMessage(pid, p, cm, new Message(Message.Request, m.getT()));
				} 
				else if (m.getM().equals(Message.Request)) {
					if (pid != i) {
						if (DEBUG) System.out.printf("[%d] (%d) %s [%d, %d, %d]\n", System.currentTimeMillis(), pid, m.getM(), q[0], q[1], q[2]);
						if (!this.isExecuted()) {
							if (m.getT() < t) m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
							else if (m.getT() == t && i < pid) m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
							else q[i - 2] = 1;
						} else m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
					}
				}
				else if (m.getM().equals(Message.Acknowledge)) {
					if (DEBUG) System.out.printf("[%d] (%d) %s [%d, %d, %d]\n", System.currentTimeMillis(), pid, m.getM(), q[0], q[1], q[2]);
					if (acc == 2) {
						acc = 1;
						if (DEBUG) for (int j = 0; j < 10; j++) System.out.printf("[%d] (%d) I'm thread A%d\n", System.currentTimeMillis(), pid, pid - 2);
						if (!DEBUG) for (int j = 0; j < 10; j++) System.out.printf("I'm thread A%d\n", pid - 2);
						executed = true;
						cm.setM(1, pid, new Message(Message.Finish, t));
						for (int j = 0; j < q.length; j++) {
							if (q[j] == 1) {
								m.sendMessage(j + 2, pid, cm, new Message(Message.Acknowledge, t));
								q[j] = 0;
							}	
						}
					} 
					else {
						acc++;
					}
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
	
	public int[] getQ() {
		return q;
	}

	public void setQ(int[] q) {
		this.q = q;
		for (int i = 0; i < q.length; i++) this.q[i] = 0;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
	
}
