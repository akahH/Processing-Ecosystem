package ecosystem;

import processing.core.PApplet;
import simple_subplot.SubPlot;

public class PlayerEnergyBar {

	private static PApplet p;
	private SubPlot plt;
	
	public PlayerEnergyBar(SubPlot plt){

		this.plt = plt;
	}
	
	
	public void display(float dt, float currentEnergy) {
		
		float[] pp = plt.getPixelCoord(-8.8, 1);
		float[] pp2 = plt.getPixelCoord(0, 3);
		
		p.pushStyle();
		p.textAlign(PApplet.CENTER);
		p.text("Player's energy", pp2[0], pp2[1]);
		p.fill(p.color(0, 255, 0));
		float value = PApplet.map(currentEnergy, 0, 10, 0, p.width-100);
		p.rect(pp[0], pp[1], value, 20);
		
		
		p.popStyle();


	}



}
