package setup;


import ecosystem.EcosystemMSSN;
import processing.core.PApplet;


public class ProcessingSetup extends PApplet {

	public static iProcessingApp app;
	private int lastUpdate;
	private int now;
	public boolean stop = false;

	
	@Override
	public void settings() {
		size(800,600);
	}
	
	@Override
	public void setup() {
		app.setup(this);
		lastUpdate = millis();
	}
	
	@Override
	public void draw() {
		if(!stop) {
			now = millis();
			float dt = (now - lastUpdate)/1000f;
			lastUpdate = now;
			app.draw(this, dt);}
	}
	
	
	@Override
	public void mousePressed() {
		app.mousePressed(this);
	}
	
	@Override
	public void mouseReleased() {
		app.mouseReleased(this);
	}
	
	@Override
	public void mouseDragged() {
		app.mouseDragged(this);
	}
	
	
	@Override
	public void keyPressed() {
		app.keyPressed(this);
	}
	
	@Override
	public void noLoop() {
		
		stop = true;
	}
	
	@Override
	public void loop() {
		lastUpdate = millis();
		stop = false;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		app = new EcosystemMSSN();
		PApplet.main(ProcessingSetup.class);
	}
	
	
}
