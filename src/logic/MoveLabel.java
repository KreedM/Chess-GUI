package logic;

import javax.swing.JLabel;

public class MoveLabel extends JLabel {
	private int limit;
	
	public MoveLabel(int limit, String label) {
		super(label);
		this.limit = limit;
	}
	
	public int getLimit() {
		return limit;
	}
}
