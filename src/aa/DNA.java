package aa;

public class DNA {
	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;
	
	public DNA() {
		
		//Physics
		maxSpeed = random(1,3);
		maxForce = random(2,5);
		//Vision
		visionDistance = random(1.5f,2.5f);
		visionSafeDistance = 0.25f * visionDistance;
		visionAngle = (float) Math.PI*0.3f;
		//Pursuit
		deltaTPursuit = random(0.5f, 1);
		//Arrive
		radiusArrive = random(3, 5);
		//Wander
		deltaTWander = random(1f, 1f);
		radiusWander = random(3f,3f);
		deltaPhiWander = (float) Math.PI/8;
	}
	
	public DNA(DNA dna) {
		//Physics
		maxSpeed = dna.maxSpeed;
		maxForce = dna.maxForce;
		//Vision
		visionDistance = dna.visionDistance;
		visionSafeDistance = dna.visionSafeDistance;
		visionAngle = dna.visionAngle;
		//Pursuit
		deltaTPursuit = dna.deltaTPursuit;
		//Arrive
		radiusArrive = dna.radiusArrive;
		//Wander
		deltaTWander = dna.deltaTWander;
		radiusWander = dna.radiusWander;
		deltaPhiWander = dna.deltaPhiWander;
	}

	public void dnaMutate() {
		maxSpeed += random(0.3f,0.3f);
	}
	
	public static float random(float min, float max) {
		return (float) (min  + (max - min)*Math.random());
	}


}
