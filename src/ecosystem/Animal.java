package ecosystem;

import aa.Behavior;
import aa.Boid;
import aa.DNA;
import aa.Eye;
import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Animal extends Boid implements IAnimal {
	
	protected float energy;

	protected Animal(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, mass, radius, color, p, plt);
	}

	protected Animal(Animal a, PApplet p, SubPlot plt) {
		super(a.pos, a.mass, a.radius, a.color, p, plt);
		for(Behavior b : a.behaviors) {
			this.addBehaviour(b);
		}
		if(a.eye != null) {
			eye = new Eye(this, a.eye);
		}
		dna = new DNA(a.dna);
	}
	


	@Override
	public void energy_consumption(float dt, Terrain terrain) {
		energy -= dt;
		
	}

	@Override
	public boolean die() {
		return(energy < 0);
	}

	@Override
	public Animal reproduce() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eat(Terrain terrain) {
		// TODO Auto-generated method stub
		
	}

}
