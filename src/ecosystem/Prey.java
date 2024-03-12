package ecosystem;

import processing.core.PApplet;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Prey extends Animal {

	private PApplet parent;
	private SubPlot plt;
	public boolean mutate = false;

	protected Prey(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt, Terrain terrain,
			double[] window) {
		super(pos, mass, radius, color, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = WorldConstants.INI_PREY_ENERGY;
		// ampliação da visão de segurança para evitar obstáculos
		dna.visionSafeDistance = 1.5f * dna.visionDistance;
		// verificação do patch e geração de uma nova posição até o patch não ser
		// obstáculo
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		while (patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()
				|| patch.getState() == WorldConstants.PatchType.LAKE.ordinal()) {
			pos = new PVector(parent.random((float) window[0], (float) window[1]),
					parent.random((float) window[2], (float) window[3]));
			patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		}
		this.pos = pos;
		sprite = parent.loadImage("../sprites/eevee.png");
		sprite.resize(0, 20);
	}

	protected Prey(Prey prey, PApplet parent, SubPlot plt) {
		super(prey, parent, plt);
		this.parent = parent;
		this.plt = plt;
		energy = WorldConstants.INI_PREY_ENERGY;
		sprite = parent.loadImage("../sprites/eevee.png");
		sprite.resize(0, 20);
	}

	@Override
	public void eat(Terrain terrain) {
		// verificar o tipo de terreno para ver se é válido para ser consumido
		Patch patch = (Patch) terrain.world2Cell(pos.x, pos.y);
		if (patch.getState() == WorldConstants.PatchType.FOOD.ordinal()) {
			energy += WorldConstants.ENERGY_FROM_PLANT;
			patch.setFertile();
		}
		if (patch.getState() == WorldConstants.PatchType.FRUIT.ordinal()) {
			energy += WorldConstants.ENERGY_FROM_PLANT;
			// só pode mutar uma unica vez
			if (!mutate) {
				// efectuada a mutação e alteração visual do agente
				dna.dnaMutate();
				mutate = true;
				color = parent.color(WorldConstants.PREY_MUTATE_COLOR[0], WorldConstants.PREY_MUTATE_COLOR[1],
						WorldConstants.PREY_MUTATE_COLOR[2]);
				sprite = parent.loadImage("../sprites/flareon.png");
				sprite.resize(0, 20);
			}

			patch.setFertile();

		}
	}
	//método usado na aplicação para adicionar instantaneamente mais presas
	public Prey reproduceNowPrey() {
		Prey child = new Prey(this, parent, plt);
		return child;

	}

	//método usado na aplicação para adicionar instantaneamente mais presas mutadas
	public Prey reproduceNowMutatedPrey() {
		Prey child = new Prey(this, parent, plt);
		child.sprite = parent.loadImage("../sprites/flareon.png");
		child.sprite.resize(0, 20);
		child.mutate = true;
		return child;

	}

	@Override
	public Prey reproduce() {
		Prey child = null;
		if (energy > WorldConstants.PREY_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.INI_PREY_ENERGY;
			child = new Prey(this, parent, plt);
			// se esta presa for mutada, a sua cria também o será
			if (this.mutate) {
				child.sprite = parent.loadImage("../sprites/flareon.png");
				child.sprite.resize(0, 20);
				child.mutate = true;
			}

		}
		return child;
	}

}
