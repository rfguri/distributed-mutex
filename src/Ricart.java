
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
					this.setExecuted(false);
					t = pid;
					m.brodcastMessage(pid, p, cm, new Message(Message.Request, t));
				} 
				else if (m.getM().equals(Message.Request)) {
					if (pid != i) {
						if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d) [%d, %d, %d]\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT(), q[0], q[1], q[2]);
						if (m.getT() < t) m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
						else if (m.getT() == t && i < pid) m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
						else q[i-2] = 1;
					}
				}
				else if (m.getM().equals(Message.Acknowledge)) {
					int index = pid - 2;
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d) [%d, %d, %d] | acc: %d\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT(), q[0], q[1], q[2], acc);
					if (acc == 2) {
						if (DEBUG) for (int j = 0; j < 10; j++) {
							System.out.printf("[%d] (%d) I'm thread A%d\n", System.currentTimeMillis(), pid, index);
							//try { Thread.sleep(10); } catch (InterruptedException e) {}
						}
						if (!DEBUG) for (int j = 0; j < 10; j++) {
							System.out.printf("I'm thread A%d\n", index);
							//try { Thread.sleep(10); } catch (InterruptedException e) {}
						}
						for (int j = 0; j < q.length; j++) {
							if (q[j] == 1) {
								m.sendMessage(j + 2, pid, cm, new Message(Message.Acknowledge, t));
								q[j] = 0;
							}
						}
						cm.setM(1, pid, new Message(Message.Finish, t));
						if (DEBUG) System.out.printf("[%d] (%d,%d) Finished to 1\n", System.currentTimeMillis(), pid, t);
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
