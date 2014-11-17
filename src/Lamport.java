import java.util.Arrays;


public class Lamport extends Process {

	private static final boolean DEBUG = true;
	
	private int acc;
	private int[] q;
	
	public Lamport(int pid, int t, CommMatrix cm, int p) {
		super(pid, t, cm, p);
		this.setAcc(1);
		this.setQ(new int[(cm.getSize() / 2) - 1]);
	}
	
	public void handleMessage() {
		for (int i = 0; i < cm.getSize(); i++) {
			Message m = cm.getM(pid, i);
			if (m != null) {
				if (m.getM().equals(Message.Start)) {
					int pos = pid - ((cm.getSize() / 2) + 1);
					q[pos] = pid;
					m.brodcastMessage(pid, p, cm, new Message(Message.Request, pid));
				} else if (m.getM().equals(Message.Request)) {
					int pos = i - ((cm.getSize() / 2) + 1);
					q[pos] = m.getT();
					t = (m.getT() > t) ? m.getT() + 1 : t + 1;
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d) [%d, %d, %d]\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT(), q[0], q[1], q[2]);
					m.sendMessage(i, pid, cm, new Message(Message.Acknowledge, t));
				} else if (m.getM().equals(Message.Acknowledge)) {
					t = (m.getT() > t) ? m.getT() + 1 : t + 1;
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d) [%d, %d, %d]\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT(), q[0], q[1], q[2]);
					if (acc == 3) {
						boolean allowed = true;
						int index = pid - ((cm.getSize() / 2) + 1);
						if (index != this.getMinValueIndex(q)) allowed = false;
						else if (q[index] < this.getMinValue(q)) allowed = false;
						else if (q[index] == this.getMinValue(q) && index < this.getMinValueIndex(q)) allowed = false;
						if (allowed) {
							if (DEBUG) for (int j = 0; j < 10; j++) {
								System.out.printf("[%d] (%d) I'm thread B%d\n", System.currentTimeMillis(), pid, index);
								//try { Thread.sleep(10); } catch (InterruptedException e) {}
							}
							if (!DEBUG) for (int j = 0; j < 10; j++) {
								System.out.printf("I'm thread B%d\n", index);
								//try { Thread.sleep(10); } catch (InterruptedException e) {}
							}
							acc = 1;
							m.brodcastMessage(pid, p, cm, new Message(Message.Release, t));
						}
					} else acc++;
				} else if (m.getM().equals(Message.Release)) {
					int pos = i - ((cm.getSize() / 2) + 1);
					q[pos] = 9999;
					if (DEBUG) System.out.printf("[%d] (%d,%d) %s from (%d,%d) [%d, %d, %d] | acc: %d\n", System.currentTimeMillis(), pid, t, m.getM(), i, m.getT(), q[0], q[1], q[2], acc);
					boolean allowed = true;
					int index = pid - ((cm.getSize() / 2) + 1);
					if (index != this.getMinValueIndex(q) || q[index] == 9999) allowed = false;
					else if (q[index] < this.getMinValue(q)) allowed = false;
					else if (q[index] == this.getMinValue(q) && index < this.getMinValueIndex(q)) allowed = false;
					if (allowed) {
						if (DEBUG) for (int j = 0; j < 10; j++) {
							System.out.printf("[%d] (%d) I'm thread B%d\n", System.currentTimeMillis(), pid, index);
							//try { Thread.sleep(10); } catch (InterruptedException e) {}
						}
						if (!DEBUG) for (int j = 0; j < 10; j++) {
							System.out.printf("I'm thread B%d\n", index);
							//try { Thread.sleep(10); } catch (InterruptedException e) {}
						}
						acc = 1;
						m.brodcastMessage(pid, p, cm, new Message(Message.Release, t));
					}
					if (acc == 3) {	
						cm.setM(0, pid, new Message(Message.Finish, t));
						acc = 1;
						if (DEBUG) System.out.printf("[%d] (%d,%d) Finished to 1\n", System.currentTimeMillis(), pid, t);
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
		for (int i = 0; i < q.length; i++) this.q[i] = 9999;
	}
	
	private int getMinValue(int[] a) {
		int[] b = a.clone();
		Arrays.sort(b);
		return b[0];
	}
	
	private int getMinValueIndex(int[] a) {
		int min = 9999;
		int index = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min) {
				min = a[i];
				index = i;
			}
		}
		return index;
	}
	
}
