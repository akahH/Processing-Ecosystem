package ca;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Cell {
	private int row, col;
	protected int state;
	private Cell[] neighbours;
	protected CellularAutomata ca;
	protected PImage img;

	
	public Cell(CellularAutomata ca, PApplet p,  int row, int col) {
		this.ca = ca;
		this.row = row;
		this.col = col;
		this.state = 0;
		this.neighbours = null;
		
		//img = p.loadImage(ca.getPaths()[state]);
		
	}
	
	public void setNeighbours(Cell[] neigh) {
		this.neighbours = neigh;
	}
	
	public Cell[] getNeighbours() {
		return this.neighbours;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return this.state;
	}
	
	public int getAliveNeighbours() {
		int alive = 0;
		for(Cell neighbour: this.getNeighbours()) {
			if (neighbour.getState() != 0)
				alive++;
		}
		if(state != 0)
			alive--;
		return alive;
	}
	

	
	public PVector cellCenter() {
		return new PVector((ca.xmin + col * ca.cellWidth) + ca.cellWidth/2 , (ca.ymin  + row * ca.cellHeight)- ca.cellHeight/2);
	}
	
	public void display(PApplet p, PImage img) {
		

		p.image(img, ca.xmin + col * ca.cellWidth , ca.ymin  + row * ca.cellHeight, ca.cellWidth, ca.cellHeight);
		
	}
	
}
