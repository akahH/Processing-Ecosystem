package ecosystem;

import java.util.ArrayList;
import java.util.List;

import Physics.Body;
import aa.AvoidObstacle;
import aa.Behavior;
import aa.Eye;
import aa.Pursuit;
import aa.Seek;
import aa.Separate;
import aa.Wander;
import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Population {

	private List<Prey> allPreys;
	protected List<Predator> allPredators;
	protected int mutateN = 0;
	private List<Body> targets;
	private double[] window;
	protected List<Body> obstacles;
	private List<Body> fruitsFishes;
	public FlockPrey flock;
	protected Human hunter;
	private Terrain terrain;
	private Behavior pursuit;
	private Behavior avoid;
	private Behavior avoidStrong;
	private Behavior wander;
	private Behavior seek;
	private Behavior separateStrong;

	public Population(PApplet parent, SubPlot plt, Terrain terrain) {
		window = plt.getWindow();
		allPreys = new ArrayList<Prey>();
		allPredators = new ArrayList<Predator>();
		targets = new ArrayList<Body>();
		iniPreys(parent, terrain, plt);
		iniPredators(parent, terrain, plt);
		iniFlockPrey(parent, terrain, plt);
		this.terrain = terrain;
	}

	
	//método que instancia um caçador
	public void iniHunter(PApplet parent, Terrain terrain, SubPlot plt) {
		if (hunter == null) {
			PVector pos = new PVector();
			Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
			//como morre quando em contacto com um obstáculo, a usa poisção de origem não pode ser num destes patches
			while (patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()
					|| patch.getState() == WorldConstants.PatchType.LAKE.ordinal()
					|| patch.getState() == WorldConstants.PatchType.LAKEFISH.ordinal()
					|| patch.getState() == WorldConstants.PatchType.EMPTYLAKE.ordinal()) {
				pos = new PVector(parent.random((float) window[0], (float) window[1]),
						parent.random((float) window[2], (float) window[3]));
				patch = (Patch) terrain.world2Cell(pos.x, pos.y);
			}
			hunter = new Human(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, parent.color(0), parent, plt);
			hunter.getDNA().maxSpeed = hunter.getDNA().maxSpeed * 2;
		}

	}

	public void iniPreys(PApplet parent, Terrain terrain, SubPlot plt) {
		obstacles = terrain.getObstacles();
		for (int i = 0; i < WorldConstants.INI_PREY_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float) window[0], (float) window[1]),
					parent.random((float) window[2], (float) window[3]));
			int color = parent.color(WorldConstants.PREY_COLOR[0], WorldConstants.PREY_COLOR[1],
					WorldConstants.PREY_COLOR[2]);
			Prey a = new Prey(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, color, parent, plt, terrain,
					window);
			avoid = new AvoidObstacle(10);
			wander = new Wander(1);
			avoidStrong = new AvoidObstacle(100);
			a.addBehaviour(avoid);
			a.addBehaviour(wander);
			Eye eye = new Eye(a, obstacles);
			a.setEye(eye);
			allPreys.add(a);
			targets.add(a);
		}
	}

	public void iniPredators(PApplet parent, Terrain terrain, SubPlot plt) {
		for (int i = 0; i < WorldConstants.INI_PREDATOR_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float) window[0], (float) window[1]),
					parent.random((float) window[2], (float) window[3]));
			int color = parent.color(WorldConstants.PREDATOR_COLOR[0], WorldConstants.PREDATOR_COLOR[1],
					WorldConstants.PREDATOR_COLOR[2]);
			Predator a = new Predator(pos, WorldConstants.PREY_MASS, (float) (WorldConstants.PREY_SIZE * 2), color,
					parent, plt, window);
			Eye eye = new Eye(a, targets);
			a.setEye(eye);
			pursuit = new Pursuit(1);
			a.addBehaviour(pursuit);
			allPredators.add(a);
			// obstacles.add(a);
		}
	}

	public void iniFlockPrey(PApplet parent, Terrain terrain, SubPlot plt) {
		int color = parent.color(WorldConstants.FLOCK_COLOR[0], WorldConstants.FLOCK_COLOR[1],
				WorldConstants.FLOCK_COLOR[2]);
		flock = new FlockPrey(WorldConstants.INI_FLOCK_SIZE, WorldConstants.PREY_MASS / 2, WorldConstants.PREY_SIZE / 2,
				color, WorldConstants.SACWEIGHTS, parent, plt);
		separateStrong = new Separate(10);
		seek = new Seek(4);
	}

	public void update(float dt, Terrain terrain) {
		fruitsFishes = terrain.getFruitsFish();
		move(terrain, dt);
		eat(terrain);
		energy_consumption(dt, terrain);
		reproduce();
		die();
	}

	private void move(Terrain terrain, float dt) {
		for (Prey a : allPreys) {

			a.getEye().look();

			if (!a.getEye().getNearSight().isEmpty()) {
				a.removeBehaviour(avoid);
				a.addBehaviour(avoidStrong);

			}
			a.removeBehaviour(avoidStrong);
			a.addBehaviour(avoid);

			a.applyBehaviors(dt);
		}

		for (Predator a : allPredators) {
			a.slowed(terrain);
			a.getEye().look();
			boolean eyeChanged = false;
			if (!a.getEye().getFarSight().isEmpty()) {
				if (!a.getEye().getNearSight().isEmpty()) {

					a.setEye(new Eye(a, a.getEye().getNearSight()));
					eyeChanged = true;
				}
				if (!eyeChanged)
					a.setEye(new Eye(a, a.getEye().getFarSight()));
			}

			if (targets.isEmpty()) {
				a.removeBehaviour(pursuit);
				a.addBehaviour(new Wander(1));
			}

			a.applyBehaviors(dt);

		}

		if (fruitsFishes != null && fruitsFishes.size() > 0) {
			for (int i = 0; i < flock.getAnimals().size(); i++) {
				Animal a = flock.getAnimals().get(i);
				a.addBehaviour(separateStrong);
				a.addBehaviour(seek);
				a.setEye(new Eye(a, fruitsFishes));
				a.getEye().look();
				boolean eyeChanged = false;
				if (!a.getEye().getFarSight().isEmpty()) {
					if (!a.getEye().getNearSight().isEmpty()) {

						a.setEye(new Eye(a, a.getEye().getNearSight()));
						eyeChanged = true;
					}
					if (!eyeChanged)
						a.setEye(new Eye(a, a.getEye().getFarSight()));
				}

			}
			flock.applyBehavior(dt);

		} else {
			for (int i = 0; i < flock.getAnimals().size(); i++) {
				Animal a = flock.getAnimals().get(i);
				a.removeBehaviour(seek);
				a.removeBehaviour(separateStrong);
				a.setEye(new Eye(a, flock.animalList2BodyList(flock.getAnimals())));
			}
			flock.applyBehavior(dt);
		}

	}

	private void eat(Terrain terrain) {
		for (Prey a : allPreys) {
			a.eat(terrain);

		}

		for (Predator a : allPredators) {
			if (!targets.isEmpty())
				a.eat((Prey) a.getEye().target, terrain, this);
		}

		flock.eat(terrain);

		if (hunter != null)
			hunter.eat(allPredators, terrain, this);

	}

	private void energy_consumption(float dt, Terrain terrain) {
		for (Animal a : allPreys)
			a.energy_consumption(dt, terrain);
		for (Animal a : allPredators) {
			a.energy_consumption(dt, terrain);
		}
		for (Animal a : flock.getAnimals()) {
			a.energy_consumption(dt, terrain);
		}
		if (hunter != null)
			hunter.energy_consumption(dt, terrain);

	}

	private void reproduce() {
		for (int i = allPreys.size() - 1; i >= 0; i--) {
			Prey a = allPreys.get(i);
			Prey child = a.reproduce();
			if (child != null) {
				allPreys.add(child);
				targets.add(child);
				for (Animal p : allPredators) {
					p.setEye(new Eye(p, targets));

				}
			}

		}

		for (int i = allPredators.size() - 1; i >= 0; i--) {
			Predator a = allPredators.get(i);
			Predator child = a.reproduce();
			if (child != null)
				allPredators.add(child);
		}

		flock.reproduce();
	}

	//Métodos para incrementar os agentes na aplicação
	public void reporduceNowPrey() {
		Prey child = allPreys.get(0).reproduceNowPrey();
		allPreys.add(child);
		targets.add(child);
		for (Animal p : allPredators) {
			p.setEye(new Eye(p, targets));
		}
	}

	public void reporduceNowMutatedPrey() {
		Prey child = allPreys.get(0).reproduceNowMutatedPrey();
		allPreys.add(child);
		targets.add(child);
		for (Animal p : allPredators) {
			p.setEye(new Eye(p, targets));
		}
	}

	public void reproduceNowPredator() {

		Predator child = allPredators.get(0).reproduceNow();

		allPredators.add(child);

	}

	private void die() {
		for (int i = allPreys.size() - 1; i >= 0; i--) {
			Prey a = allPreys.get(i);
			if (a.die()) {
				allPreys.remove(i);
				targets.remove(a);
			}

		}

		for (int i = allPredators.size() - 1; i >= 0; i--) {
			Animal a = allPredators.get(i);
			if (a.die())
				allPredators.remove(i);
		}

		if (hunter != null)
			if (hunter.die(terrain))
				hunter = null;

		for (int i = flock.getAnimals().size() - 1; i >= 0; i--) {
			Animal a = flock.getAnimals().get(i);
			if (a.die())
				flock.getAnimals().remove(i);
		}
	}
	//método auxiliar chamado em Predator quando este consome uma presa
	//remove a presa do terreno
	public void preyEaten(Prey prey) {
		allPreys.remove(prey);
		targets.remove(prey);
		for (Animal a : allPredators)
			a.setEye(new Eye(a, targets));

	}

	public void predatorHunted(Predator p) {
		allPredators.remove(p);
	}

	public void display(PApplet p, SubPlot plt) {
		for (Animal a : allPreys)
			a.display(p, plt);
		for (Animal a : allPredators)
			a.display(p, plt);
		flock.display(p, plt);
		if (hunter != null)
			hunter.display(p, plt);
	}

	
	//Métodos auxiliares de contagem
	
	public int getNumAnimals() {
		return allPreys.size();
	}

	public int getPreyNumber() {
		int aux = 0;
		for (Prey a : allPreys)
			if (!a.mutate)
				aux++;
		return aux;
	}

	public int getPredatorNumber() {
		return allPredators.size();
	}

	public int getFlockNumber() {
		return flock.getAnimals().size();
	}

	public int getMutateNumber() {
		mutateN = 0;
		for (Prey a : allPreys) {
			if (a.mutate)
				mutateN++;
		}
		return mutateN;
	}

}
