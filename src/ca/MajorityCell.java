package ca;

import processing.core.PApplet;
import simple_subplot.Histogram;

public class MajorityCell extends Cell {
	
	private Histogram hist;

	public MajorityCell(CellularAutomata ca, PApplet p, int row, int col) {
		super(ca, p, row, col);
	}
	
	public void computeHistogram() {
		Cell[] neighbours = getNeighbours();
		int[] data = new int[neighbours.length];
		for(int i = 0; i < neighbours.length; i++)
			data[i] = neighbours[i].getState();
		hist = new Histogram(data, ca.nStates);
	}
	
	public boolean applyMajorityRule() {
		int mode = hist.getMode(0);
		boolean changed = false;
		if(getState() != mode) {
			setState(mode);
			changed = true;
		}
		return changed;
		
	}

}
