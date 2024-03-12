package ca;

import processing.core.PApplet;
import simple_subplot.SubPlot;

public class MajorityCA extends CellularAutomata {

	public MajorityCA(PApplet p, SubPlot plt, int nrows, int ncols, int nStates, int radiusNeigh, String[] paths) {
		super(p, plt, nrows, ncols, nStates, radiusNeigh, paths);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void createCells(PApplet p) {
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j] =  new MajorityCell(this,p, i, j);
			}
		}
		
		setMooreNeighbours();
	}
	
	public boolean majorityRule() {
		for(int i = 0; i < nrows; i++) {
			for(int j = 0; j < ncols; j++) {
				((MajorityCell) cells[i][j]).computeHistogram();
			}
		}
		
		
		boolean anyChanged = false;
		for(int i = 0; i < nrows; i++) {
			for(int j = 0; j < ncols; j++) {
				boolean changed = ((MajorityCell)cells[i][j]).applyMajorityRule();
				if(changed) anyChanged = true;
			}	
		}
		
		return anyChanged;
	}
	
	

}
