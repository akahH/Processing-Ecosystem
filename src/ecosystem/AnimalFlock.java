package ecosystem;

import java.util.ArrayList;
import java.util.List;

import Physics.Body;
import aa.Align;
import aa.Behavior;
import aa.Boid;
import aa.Cohesion;
import aa.Eye;
import aa.Separate;
import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class AnimalFlock implements IAnimal {

	protected float energy;
	protected List<Animal> animals;
	protected float mass;
	protected float radius;
	protected int color;
	protected float[] sacWeights;
	protected PApplet p;
	protected SubPlot plt;

	public AnimalFlock(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt) {
		double[] w = plt.getWindow();
		float maxSpeed = p.random(2, 4);
		animals = new ArrayList<Animal>();
		for (int i = 0; i < nboids; i++) {
			float x = p.random((float) w[0], (float) w[1]);
			float y = p.random((float) w[2], (float) w[3]);
			Animal b = new Animal(new PVector(x, y), mass, radius, color, p, plt);
			b.sprite = p.loadImage("../sprites/wingull.png");
			b.sprite.resize(0, 10);
			b.getDNA().visionDistance = p.random(10, 10);
			b.getDNA().maxSpeed = maxSpeed;
			b.addBehaviour(new Separate(sacWeights[0]));
			b.addBehaviour(new Align(sacWeights[1]));
			b.addBehaviour(new Cohesion(sacWeights[2]));
			b.energy = WorldConstants.INI_FLOCK_ENERGY;
			animals.add(b);
		}

		for (Animal b : animals)
			b.setEye(new Eye(b, animalList2BodyList(animals)));
		this.color = color;
		this.mass = mass;
		this.radius = radius;
		this.p = p;
		this.plt = plt;
		this.sacWeights = sacWeights;
	}

	public List<Body> animalList2BodyList(List<Animal> animals) {
		List<Body> bodies = new ArrayList<Body>();
		for (Animal b : animals)
			bodies.add(b);
		return bodies;
	}

	public void addBehavior(Behavior b) {
		for (Animal a : animals)
			a.addBehaviour(b);
	}

	public void removeBehavior(Behavior b) {
		for (Animal a : animals)
			a.removeBehaviour(b);
		;
	}

	public void setEye(Eye eye) {
		for (Animal a : animals)
			a.setEye(eye);
		;
	}

	//gerar um novo animal 
	public Animal addAnimal() {

		double[] w = plt.getWindow();
		float maxSpeed = p.random(2, 4);
		float x = p.random((float) w[0], (float) w[1]);
		float y = p.random((float) w[2], (float) w[3]);
		Animal b = new Animal(new PVector(x, y), mass, radius, color, p, plt);
		b.sprite = p.loadImage("../sprites/wingull.png");
		b.sprite.resize(0, 10);
		b.getDNA().visionDistance = p.random(10, 10);
		b.getDNA().maxSpeed = maxSpeed;
		b.addBehaviour(new Separate(sacWeights[0]));
		b.addBehaviour(new Align(sacWeights[1]));
		b.addBehaviour(new Cohesion(sacWeights[2]));
		b.energy = WorldConstants.INI_FLOCK_ENERGY;
		return b;
	}

	public List<Animal> getAnimals() {
		return animals;
	}

	public void applyBehavior(float dt) {
		for (Animal b : animals)
			b.applyBehaviors(dt);
	}

	public void display(PApplet p, SubPlot plt) {
		for (Boid b : animals)
			b.display(p, plt);
	}

	@Override
	public void eat(Terrain terrain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void energy_consumption(float dt, Terrain terrain) {
		energy -= dt;

	}

	@Override
	public boolean die() {
		return (energy < 0);
	}

	@Override
	public Animal reproduce() {
		Animal child = null;
		if (energy > WorldConstants.FLOCK_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.INI_FLOCK_ENERGY;
			child = this.addAnimal();
		}
		return child;
	}

}
