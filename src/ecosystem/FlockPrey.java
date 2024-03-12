package ecosystem;

import processing.core.PApplet;
import simple_subplot.SubPlot;

public class FlockPrey extends AnimalFlock {

	public FlockPrey(int nboids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt) {
		super(nboids, mass, radius, color, sacWeights, p, plt);

	}

	@Override
	public void eat(Terrain terrain) {
		for (Animal a : animals) {
			Patch patch = (Patch) terrain.world2Cell(a.getPos().x, a.getPos().y);
			if (patch.getState() == WorldConstants.PatchType.FRUIT.ordinal()) {
				a.energy += WorldConstants.ENERGY_FROM_FISH;
				patch.setFertile();
			}
			if (patch.getState() == WorldConstants.PatchType.LAKEFISH.ordinal()) {
				a.energy += WorldConstants.ENERGY_FROM_FISH;
				patch.setBreedable();
			}

		}

	}

	@Override
	public Animal reproduce() {
		Animal child = null;
		int aux = 0;
		for (Animal a : this.getAnimals()) {
			if (a.energy > WorldConstants.FLOCK_ENERGY_TO_REPRODUCE) {
				a.energy -= WorldConstants.INI_FLOCK_ENERGY;
				aux++;
			}

		}
		for (int i = 0; i < aux; i++) {
			this.getAnimals().add(this.addAnimal());
		}
		return child;
	}

}
