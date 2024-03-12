package ecosystem;

import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Predator extends Animal {
	
	private PApplet parent;
	private SubPlot plt;
	private double [] window;
	private boolean slowed = false;
	private float maxSpeed;

	protected Predator(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt ,double [] window) {
		super(pos, mass, radius, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = WorldConstants.INI_PREDATOR_ENERGY;
		this.window = window;
		sprite = parent.loadImage("../sprites/luxray.png");
		sprite.resize(0, 30);
		getDNA().maxSpeed = getDNA().maxSpeed*1.5f;
		maxSpeed = getDNA().maxSpeed;

	}
	
	protected Predator(Predator prey, PApplet parent, SubPlot plt, double [] window) {
		super(prey, parent, plt);
		this.parent = parent;
		this.plt = plt;
		this.window = window;
		pos = new PVector(parent.random((float) window[0], (float) window[1]),
				parent.random((float) window[2], (float) window[3]));
		energy = WorldConstants.INI_PREDATOR_ENERGY;
		sprite = parent.loadImage("../sprites/luxray.png");
		sprite.resize(0, 30);
		maxSpeed = getDNA().maxSpeed;

	}
	
	
	public void eat(Prey prey, Terrain terrain, Population population) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		if(patch.preyInPatch(prey, terrain)) {
			energy +=  WorldConstants.ENERGY_FROM_PREY;
			population.preyEaten(prey);
		}
	}
	
	@Override
	public Predator reproduce() {
		Predator child = null;
		if(energy > WorldConstants.PREDATOR_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.INI_PREDATOR_ENERGY;
			child = new Predator(this, parent, plt, window);
		}
		return child;
	}
	
	public Predator reproduceNow() {
		Predator child = new Predator(this, parent, plt, window);
		return child;
	}
	
	public void slowed(Terrain terrain) {
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		if(!slowed && patch.getState() == WorldConstants.PatchType.LAKE.ordinal() || 
				patch.getState() == WorldConstants.PatchType.LAKEFISH.ordinal()) {
			getDNA().maxSpeed = (float) PApplet.constrain(((float)getDNA().maxSpeed * 0.8f), ((float)maxSpeed*0.8f), maxSpeed);
			slowed = true;
		}
		if(slowed && patch.getState() != WorldConstants.PatchType.LAKE.ordinal() && 
				patch.getState() != WorldConstants.PatchType.LAKEFISH.ordinal() ) {
			getDNA().maxSpeed = (float) (getDNA().maxSpeed * 1.2);
			slowed = false;
		}
		

	}

}
