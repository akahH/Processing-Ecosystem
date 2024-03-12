package ecosystem;

import Physics.Body;
import ca.MajorityCell;
import ecosystem.WorldConstants.PatchType;
import processing.core.PApplet;

public class Patch extends MajorityCell {

	private long eatenTime;
	private int timeToGrow;
	private long aliveTime;
	private long breedTime;
	public boolean preyInPatch;
	private boolean stopGrowing;

	
	public Patch(Terrain terrain, PApplet p, int row, int col, int timeToGrow ) {
		super(terrain, p, row, col);
		this.timeToGrow = timeToGrow;
		eatenTime = System.currentTimeMillis();
		aliveTime = System.currentTimeMillis();
		breedTime = System.currentTimeMillis();

	}

	public void resumeGrowing(boolean stopGrowing) {
		if(stopGrowing) {
			this.stopGrowing = true;
		}
		else {
			this.stopGrowing = false;

		}
			
			
	}
	
	public void setFertile() {
		state = PatchType.FERTILE.ordinal();
		eatenTime = System.currentTimeMillis();
	}

	public void setBreedable() {
		state = PatchType.LAKE.ordinal();
		breedTime = System.currentTimeMillis();
	}

	public boolean regenerate() {
		boolean update = false;
		if(!stopGrowing) {
			if (state == PatchType.FERTILE.ordinal() && System.currentTimeMillis() > (eatenTime + timeToGrow)) {
				state = PatchType.FOOD.ordinal();
				aliveTime = System.currentTimeMillis();
				update = true;
			}
		}	
		return update;
	}
	//método que gera a fruta na planta
	//tempo de crescimento da fruta será superior ao tempo de crescimento da planta
	public boolean generateFruit() {
		boolean update = false;
		if(!stopGrowing) {
			if (state == PatchType.FOOD.ordinal() && System.currentTimeMillis() > (aliveTime + timeToGrow * 1.5)) {
				state = PatchType.FRUIT.ordinal();
				update = true;
			}
			
		}
		return update;
	}

	public boolean generateFish() {
		boolean update = false;
		if(!stopGrowing) {
			
			if (state == PatchType.LAKE.ordinal() && System.currentTimeMillis() > (breedTime + timeToGrow / 4)) {
				state = PatchType.LAKEFISH.ordinal();
				update = true;
			}
			
		}
		return update;
	}
	//método que recebe uma presa e verifica a sua posição no terreno
	//retorna true se algum dos patch das presas for equivalente ao patch actual
	public boolean preyInPatch(Body prey, Terrain terrain) {
		Patch patch = (Patch) terrain.world2Cell(prey.getPos().x, prey.getPos().y);
		if (this.equals(patch))
			return true;
		return false;

	}

}
