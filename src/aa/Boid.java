package aa;

import java.util.ArrayList;
import java.util.List;

import Physics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import simple_subplot.SubPlot;

public class Boid extends Body {


	private PShape shape;
	protected DNA dna;
	protected Eye eye;
	protected List<Behavior> behaviors;
	protected float phiWander;
	private double[] window;
	private float sumWeights;
	public PImage sprite;
	
	public Boid(PVector pos, float mass, float radius, int color
			,PApplet p, SubPlot plt) {
		super(pos, new PVector(), mass, radius, color);
		dna = new DNA();
		behaviors = new ArrayList<Behavior>();
		window = plt.getWindow();
		setShape(p, plt);
		
		
	}
	
	public void setEye(Eye eye) {
		this.eye = eye;
	}
	
	public Eye getEye() {
		return eye;
	}
	
	public DNA getDNA() {
		return dna;
	}

	public void setShape(PApplet p, SubPlot plt) {
		 float[] rr = plt.getVectorCoord(radius, radius);
		 shape = p.createShape();
		 shape.beginShape();
		 shape.vertex(-rr[0], rr[0]/2);
		 shape.vertex(rr[0], 0);
		 shape.vertex(-rr[0], -rr[0]/2);
		 shape.vertex(-rr[0]/2, 0);
		 shape.fill(color);
		 shape.noStroke();
		 shape.endShape(PConstants.CLOSE);
	}
	
	private void updateSumWeights() {
		sumWeights = 0;
		for(Behavior beh : behaviors)
			sumWeights += beh.getWeight();
	}
	
	public void addBehaviour(Behavior behavior) {
		behaviors.add(behavior);
		updateSumWeights();
	}

	public void removeBehaviour(Behavior behavior) {
		
		if (behaviors.contains(behavior))
			behaviors.remove(behavior);
		updateSumWeights();
	}
	
	public void applyBehavior(int i, float dt) {
		if(eye!= null) eye.look();
		Behavior behavior = behaviors.get(i);
		PVector vd = behavior.getDesiredVelocity(this);
		move(dt, vd);
	}
	
	public void applyBehaviors(float dt) {
		if(eye!= null) eye.look();
		
		PVector vd = new PVector();
		for (Behavior behavior : behaviors) {
			PVector vdd = behavior.getDesiredVelocity(this);
			vdd.mult(behavior.getWeight()/sumWeights);
			vd.add(vdd);
		}
		
		move(dt, vd);
	}
	
	public void move(float dt, PVector vd) {
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, vel);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);
		if(pos.x < window[0])
			pos.x += window[1] - window[0];
		if(pos.y < window[2])
			pos.y += window[3] - window[2];
		if(pos.x >= window[1])
			pos.x -= window[1] - window[0];
		if(pos.y >= window[3])
			pos.y -= window[3] - window[2];
	}
	
	
	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushMatrix();
		
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		p.translate(pp[0], pp[1]);
		p.image(sprite, pos.x, pos.y);
		
		p.popMatrix();
	}
	
	
}
