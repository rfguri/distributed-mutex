
public class Process extends Thread {

	protected int pid;
	protected int t;
	protected int p;
	protected CommMatrix cm;
	
	public Process(int pid, int t, CommMatrix cm, int p) {
		this.pid = pid;
		this.setT(t);
		this.cm = cm;
		this.setP(p);
	}
	
	public void run() {
		Token token = null;
		Ricart ricart = null;
		Lamport lamport = null;
		if (p == Protocol.Token) token = new Token(pid, t, cm, p);
		else if (p == Protocol.Ricart) ricart = new Ricart(pid, t, cm, p);
		else lamport = new Lamport(pid, t, cm, p);
		while (true) {
			if (p == Protocol.Token) token.handleMessage();
			else if (p == Protocol.Ricart) ricart.handleMessage();
			else lamport.handleMessage();
		}
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}
	
	public CommMatrix getCm() {
		return cm;
	}

	public void setCm(CommMatrix cm) {
		this.cm = cm;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}
	
}
