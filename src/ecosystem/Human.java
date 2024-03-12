package ecosystem;

import java.util.List;

import aa.Boid;
import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Human extends Boid {

	private SubPlot plt;
	protected float energy;
	
	public Human(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, mass, radius, color, p, plt);
		this.plt = plt;
		energy = WorldConstants.INI_PREY_ENERGY;
		sprite = p.loadImage("../sprites/trainer.png");
		sprite.resize(0, 20);
		
	}

	public PVector getTarget(float x, float y) {
		double[] pp = plt.getWorldCoord(x, y);
		PVector vd = PVector.sub(new PVector((float)pp[0], (float)pp[1]), new PVector(this.getPos().x,this.getPos().y));
		return vd;
	}
	
	public void eat(List<Predator> predators, Terrain terrain, Population population) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		Predator hunted = null;
		for(Predator p : predators)
		{
			if(patch.preyInPatch(p, terrain)) {
				hunted = p;
				energy = 10f;
			}
		}
		if(hunted!=null)
			population.predatorHunted(hunted);

	}
	
	public boolean die(Terrain terrain) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		
		for(int i = 0; i < terrain.nrows; i++) {
			for(int j = 0; j < terrain.ncols; j++) {
					if(terrain.getCells()[i][j].getState() == WorldConstants.PatchType.OBSTACLE.ordinal() ||
							terrain.getCells()[i][j].getState() == WorldConstants.PatchType.LAKE.ordinal() ||
							terrain.getCells()[i][j].getState() == WorldConstants.PatchType.LAKEFISH.ordinal()||
							terrain.getCells()[i][j].getState() == WorldConstants.PatchType.EMPTYLAKE.ordinal())
						if(terrain.getCells()[i][j].equals(patch))
							return true;
				}
			}
		if(energy < 0)
			return true;
		return false;
	}
	
	public void energy_consumption(float dt, Terrain terrain) {
		energy -= dt;
		
	}
	
}
