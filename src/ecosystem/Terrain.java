package ecosystem;

import java.util.ArrayList;
import java.util.List;

import Physics.Body;
import ca.MajorityCA;
import processing.core.PApplet;
import simple_subplot.SubPlot;

public class Terrain extends MajorityCA {

	public Terrain(PApplet p, SubPlot plt, String[] paths) {
		super(p, plt, WorldConstants.NROWS, WorldConstants.NCOLS, WorldConstants.NSTATES, 1, paths);

	}

	@Override
	protected void createCells(PApplet p) {
		int minRT = (int) (WorldConstants.REGENERATION_TIME[0] * 1000);
		int maxRT = (int) (WorldConstants.REGENERATION_TIME[1] * 1000);
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				int timeToGrow = (int) (minRT + (maxRT - minRT) * Math.random());
				cells[i][j] = new Patch(this, p, i, j, timeToGrow);
			}
		}
		setMooreNeighbours();
	}

	public List<Body> getObstacles() {
		List<Body> bodies = new ArrayList<Body>();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (cells[i][j].getState() == WorldConstants.PatchType.OBSTACLE.ordinal()
						|| cells[i][j].getState() == WorldConstants.PatchType.LAKE.ordinal()
						|| cells[i][j].getState() == WorldConstants.PatchType.LAKEFISH.ordinal()
						|| cells[i][j].getState() == WorldConstants.PatchType.EMPTYLAKE.ordinal()) {
					Body b = new Body(this.getCenterCell(i, j));
					bodies.add(b);
				}
			}
		}
		return bodies;
	}

	public List<Body> getFruitsFish() {
		List<Body> bodies = new ArrayList<Body>();
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				if (cells[i][j].getState() == WorldConstants.PatchType.FRUIT.ordinal()
						|| cells[i][j].getState() == WorldConstants.PatchType.LAKEFISH.ordinal()) {
					Body b = new Body(this.getCenterCell(i, j));
					bodies.add(b);
				}
			}
		}
		return bodies;
	}

	public void regenerate(PApplet p, boolean stopGrowing) {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				((Patch) cells[i][j]).resumeGrowing(stopGrowing);
				if (((Patch) cells[i][j]).regenerate())
					cells[i][j].display(p, this.imageHash.get(cells[i][j].getState()));
				;
			}
		}
	}

	public void generateFruits(PApplet p, boolean stopGrowing) {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				((Patch) cells[i][j]).resumeGrowing(stopGrowing);
				if (((Patch) cells[i][j]).generateFruit())
					cells[i][j].display(p, this.imageHash.get(cells[i][j].getState()));
				;
			}
		}
	}

	public void generateFish(PApplet p,boolean stopGrowing) {
		for (int i = 0; i < nrows; i++) {
			for (int j = 0; j < ncols; j++) {
				((Patch) cells[i][j]).resumeGrowing(stopGrowing);
				if (((Patch) cells[i][j]).generateFish())
					cells[i][j].display(p, this.imageHash.get(cells[i][j].getState()));
				;
			}
		}
	}

	public void checkPreyInPatches(List<Animal> allAnimals) {
		for (Animal a : allAnimals) {
			for (int i = 0; i < nrows; i++) {
				for (int j = 0; j < ncols; j++) {
					((Patch) cells[i][j]).preyInPatch((Prey) a, this);

				}
			}
		}

	}
}
