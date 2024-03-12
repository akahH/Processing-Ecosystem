package ecosystem;

import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class LakePrey extends Animal {

	private PApplet parent;
	private SubPlot plt;
	public boolean mutate = false;


	protected LakePrey(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt, Terrain terrain, double [] window) {
		super(pos, mass, radius, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		dna.visionSafeDistance = 0.9f * dna.visionDistance;
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		while(patch.getState() != WorldConstants.PatchType.LAKE.ordinal()) {
			pos = new PVector(parent.random((float) window[0], (float) window[1]),
					parent.random((float) window[2], (float) window[3]));
			patch = (Patch) terrain.world2Cell(pos.x, pos.y);
			
		}
		double[] newPos = plt.getWorldCoord(patch.cellCenter().x, patch.cellCenter().y);
		this.pos = new PVector((float) newPos[0],(float) newPos[1]);
		this.getDNA().maxSpeed = (float) (this.getDNA().maxSpeed * 0.2);
		sprite = parent.loadImage("../sprites/magikarp.png");
		sprite.resize(0, 20);
		

	}
	
	protected LakePrey(LakePrey prey, PApplet parent, SubPlot plt) {
		super(prey, parent, plt);
		this.parent = parent;
		this.plt = plt;

	}
	
	@Override
	public void eat(Terrain terrain) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		if(patch.getState() == WorldConstants.PatchType.FOOD.ordinal()) {
			energy +=  WorldConstants.ENERGY_FROM_PLANT;
			patch.setFertile();
		}
		if(patch.getState() == WorldConstants.PatchType.FRUIT.ordinal()) {
			energy +=  WorldConstants.ENERGY_FROM_PLANT;
			if(!mutate)
				{
				dna.dnaMutate();
				mutate = true;
				color = parent.color( WorldConstants.PREY_MUTATE_COLOR[0],
						WorldConstants.PREY_MUTATE_COLOR[1],
						WorldConstants.PREY_MUTATE_COLOR[2]);
				sprite = parent.loadImage("../sprites/flareon.png");
				sprite.resize(0, 20);
				}

			patch.setFertile();
			
		}
	}
	
	@Override
	public LakePrey reproduce() {
		LakePrey child =  new LakePrey(this, parent, plt);

			
		return child;
	}
	
}
