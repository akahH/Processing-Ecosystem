package ca;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import simple_subplot.CustomRandomGenerator;
import simple_subplot.SubPlot;

public class CellularAutomata {
	
	public int nrows;
	public int ncols;
	protected int nStates;
	private int radiusNeigh;
	protected Cell[][] cells;
	private int[] colors;
	protected float cellWidth, cellHeight;
	protected float xmin, ymin;
	private SubPlot plt;
	protected String[] paths;
	protected HashMap<Integer, PImage> imageHash;
	
	public CellularAutomata(PApplet p, SubPlot plt,  int nrows, int ncols,int nStates,int radiusNeigh, String[] paths) {
		this.nStates = nStates;
		this.nrows = nrows;
		this.ncols = ncols;
		this.radiusNeigh = radiusNeigh;
		this.plt = plt;
		float[] bb = plt.getBoundingBox();
		xmin = bb[0];
		ymin = bb[1];
		cells = new Cell[nrows][ncols];
		colors = new int[nStates];
		cellHeight = bb[3]/nrows;
		cellWidth = bb[2]/ncols;
		this.paths = paths;
		imageHash = new HashMap<Integer, PImage>();
		imageHash(p);
		createCells(p);
	}
	
	public void setStateColors(PApplet p) {
		
		colors[0] = p.color(0,120,0); // morta
		colors[1] = p.color(0,255,0); //célula que nasce com 3 vizinhos
		colors[2] = p.color(0,0,255); // célula que nasce com 6 vizinhos
		colors[3] = p.color(0,128,255); // célula viva com dois vizinhos
		colors[4] = p.color(128,0,255);  // célula viva com 3 vizinhos
		
	}
	
	
	public void setStateColors(int[] colors) {
		this.colors = colors;
	}
	
	public void setImagePath(String[]paths) {
		this.paths = paths;
	}
	
	public String[] getPaths() {
		return paths;
	}
	
	public void imageHash(PApplet p){
		int aux = 0;
		for(String path : paths) {
			PImage img = p.loadImage(path);
			imageHash.put(aux, img);
			aux++;
		}
	}
	
	public int[] getStateColors() {
		return colors;
	}
	
	protected void createCells(PApplet p) {
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j] =  new Cell(this, p, i, j);
			}
		}
		
		setMooreNeighbours();
	}
	
	public void initRandom() {
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j].setState((int)((nStates) * Math.random()));
			}
		}
	}
	
	public void initRandomCustom(double[] pmf) {
		CustomRandomGenerator crg = new CustomRandomGenerator(pmf);
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j].setState(crg.getRandomClass());
			}
		}
	}
	
	public void initCustom() {
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j].setState(0);
			}
		}
	}
	
	
	public PVector getCenterCell(int row, int col) {
		float x = (col + 0.5f) * cellWidth;
		float y = (row + 0.5f) * cellHeight;
		double[] w = plt.getWorldCoord(x, y);
		return new PVector((float)w[0], (float)w[1]);
	}
	
	
	public Cell world2Cell(double x, double y) {
		float[] xy = plt.getPixelCoord(x, y);
		return pixel2Cell(xy[0], xy[1]);
	}
	
	
	public Cell pixel2Cell(float x, float y) {
		int row = (int) ((y - ymin) / cellHeight);
		int col = (int) ((x - xmin) / cellWidth);
		if(row >= nrows) row = nrows-1;
		if(col >= ncols) col = ncols-1;
		if(row < 0)  row = 0;
		if(col < 0) col = 0;
		return cells[row][col];
	}
	
	protected void setMooreNeighbours() {
		
		int NN = (int) Math.pow(2 * radiusNeigh+1, 2);
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
					Cell[] neigh = new Cell[NN];
					int n = 0;
					for(int ii = -this.radiusNeigh; ii <= this.radiusNeigh;ii++) {
						int row = (i + ii + this.nrows) % this.nrows;
						for(int jj = -this.radiusNeigh; jj <= this.radiusNeigh; jj++) {
								int col  = (j + jj + this.ncols) % this.ncols;
								neigh[n++] = cells[row][col];
								}
							}
						cells[i][j].setNeighbours(neigh);
					}
			}
	}
	
	public Cell[][] getCells() {
		return this.cells;
	}
	
	
	public void display(PApplet p) {
		for(int i = 0; i < nrows;i++) {
			for(int j = 0; j < ncols; j++) {
				cells[i][j].display(p, imageHash.get(cells[i][j].getState()));
			}
		}
		
	}
}
