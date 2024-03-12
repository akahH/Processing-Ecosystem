package ecosystem;


import processing.core.PApplet;
import processing.core.PFont;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import processing.core.PImage;
import processing.core.PVector;
import setup.iProcessingApp;
import simple_subplot.SubPlot;

public class EcosystemMSSN implements iProcessingApp {

	private float[] viewport = { 0f, 0.4f, 1f, 0.6f };
	private float[] viewport2 = { 0f, 0.1f, 1f, 0.30f };
	private float[] viewport3 = { 0f, 0.0f, 1f, 0.10f };
	private SubPlot plt;
	private SubPlot pltCounter;

	private AnimalCounter ac;
	private Terrain terrain;
	private Population population;

	private PVector vd;
	private SubPlot pltPlayerBar;
	private PlayerEnergyBar playerBar;
	private PImage treeLine;
	private PImage treeLineVert;
	private boolean start = false;
	private boolean controlMenu = false;
	private boolean agentsMenu = false;
	private PImage startBackground ;
	private PImage ball ;
	private boolean tilesMenu = false;
	static Clip clip;
	private boolean musicStart;
	private boolean checkAgents = false;
	private boolean checkTiles;
	private PImage grass;
	private PImage plant;
	private PImage obstacle;
	private PImage fruit;
	private PImage lake;
	private PImage lakefish;
	
	@Override
	public void setup(PApplet p) {

		plt = new SubPlot(WorldConstants.WINDDOW, viewport, p.width, p.height);
		pltCounter = new SubPlot(WorldConstants.WINDDOW_COUNTER, viewport2, p.width, p.height);
		pltPlayerBar = new SubPlot(WorldConstants.WINDDOW_COUNTER, viewport3, p.width, p.height);
		ac = new AnimalCounter(pltCounter, p);
		playerBar = new PlayerEnergyBar(pltPlayerBar);
		terrain = new Terrain(p, plt, WorldConstants.IMGPATCH);
		terrain.setStateColors(getColors(p));
		;
		terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);

		for (int i = 0; i < 2; i++)
			terrain.majorityRule();
		population = new Population(p, plt, terrain);
		ac.animalCount(population.getPreyNumber(), 0, population.getPredatorNumber(), population.getMutateNumber());
		treeLine = p.loadImage("../sprites/treeLine.png");
		treeLine.resize(800, 20);
		treeLineVert = p.loadImage("../sprites/treeLineVert.png");
		treeLineVert.resize(20, 360);
		vd = new PVector();
		startBackground = p.loadImage("../sprites/startBackground.png");
		startBackground.resize(p.width, p.height);
		ball = p.loadImage("../sprites/ball.png");
		ball.resize(20, 20);
		musicStart = false;
		 grass = p.loadImage("../sprites/grass_1.png");
		plant = p.loadImage("../sprites/grass_full_v2.png");
		obstacle = p.loadImage("../sprites/obstacle.png");;
		fruit = p.loadImage("../sprites/grass_fruit.png");;
		lake = p.loadImage("../sprites/lake.png");;
		lakefish = p.loadImage("../sprites/lakeFish.png");;
		


	}

	@Override
	public void draw(PApplet p, float dt) {
			if(musicStart) {
				
				playSound("./sprites/app_music.wav");
			
				musicStart = false;
			}
				
	
			if(!start) {
				p.image(startBackground, 0, 0);
				p.pushStyle();
				PFont font = p.createFont("../fonts/CartooNature.ttf",20);
				p.textFont(font);
				p.fill(255);
				p.textSize(32);
				p.textAlign(PApplet.CENTER);
				p.text("Press S to start", p.width/2, 500);
				p.popStyle();
			}else {
				if(!checkAgents) {
					PImage background = p.loadImage("../sprites/background.png");
					background.resize(p.width, p.height);
					p.background(background);
					agentsMenu(p);

				}
				else if(checkAgents && !checkTiles) {
					PImage background = p.loadImage("../sprites/background.png");
					background.resize(p.width, p.height);
					p.background(background);
					tilesMenu(p);
				}
				else if(checkAgents && checkTiles) {
					ac.animalCount(population.getPreyNumber(), population.getFlockNumber(), population.getPredatorNumber(),
							population.getMutateNumber());
					ac.display(p, pltCounter);
					if (population.hunter != null)
						playerBar.display(dt, population.hunter.energy);
					terrain.regenerate(p, false);
					terrain.generateFruits(p, false);
					terrain.generateFish(p, false);
					population.update(dt, terrain);
					terrain.display(p);
					if (population.hunter != null)
						population.hunter.move(dt, vd);
					population.display(p, plt);
					p.image(treeLine, 0, 0);
					p.image(treeLineVert, 0, 0);
					p.image(treeLineVert, p.width-20, 0);
					p.image(treeLine, 0, 355);
					if(controlMenu)
						controlMenu(p);
					if(agentsMenu)
						agentsMenu(p);
					if(tilesMenu )
						tilesMenu(p);
				}

			}

		


	}

	@Override
	public void mousePressed(PApplet p) {
		float x = p.mouseX;
		float y = p.mouseY;
		if (population.hunter != null)
			vd = population.hunter.getTarget(x, y);
		float[] pp = pltCounter.getPixelCoord(-8.5, 1);
		if(x >= pp[0] && x <= pp[0]+30 && y >= pp[1] && y <= pp[1]+30) {
			population.reporduceNowPrey();
		}
		
		pp = pltCounter.getPixelCoord(-3.4, 1);
		if(x >= pp[0] && x <= pp[0]+30 && y >= pp[1] && y <= pp[1]+30) {
			population.reporduceNowMutatedPrey();
		}
		
		pp = pltCounter.getPixelCoord(2.5, 1);
		if(x >= pp[0] && x <= pp[0]+30 && y >= pp[1] && y <= pp[1]+30) {
			population.flock.getAnimals().add(population.flock.addAnimal());
		}
		
		pp = pltCounter.getPixelCoord(7.5, 1);
		if(x >= pp[0] && x <= pp[0]+30 && y >= pp[1] && y <= pp[1]+30) {
			population.reproduceNowPredator();
		}

	}

	@Override
	public void keyPressed(PApplet p) {
		if (p.key == 'p')
			population.iniHunter(p, terrain, plt);
		if(p.key == 's') {
				
			if(!start) {

				start = !start;
				musicStart = true;
			}
				
			

		}

		if(p.key == 'r') {
			clip.stop();
			reset(p);
			
		}
			
		if(p.key == 'm') {
			if(start)
			controlMenu = !controlMenu;
		}
		
		if(p.key == 'c') {
			if(start)
				agentsMenu = !agentsMenu;
		}
		
		if(p.key == 't') {
			if(start)
				tilesMenu = !tilesMenu;
		}
		
		if(p.keyCode == PApplet.ENTER) {
			if(start) {
				if(checkAgents) {
					checkTiles = true;
					setup(p);
				}
				checkAgents = true;

			}

		}

	}

	@Override
	public void mouseReleased(PApplet p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(PApplet p) {
		// TODO Auto-generated method stub

	}

	private int[] getColors(PApplet p) {
		int[] colors = new int[WorldConstants.NSTATES];
		for (int i = 0; i < WorldConstants.NSTATES; i++) {
			colors[i] = p.color(WorldConstants.TERRAIN_COLORS[i][0], WorldConstants.TERRAIN_COLORS[i][1],
					WorldConstants.TERRAIN_COLORS[i][2]);
		}
		return colors;
	}
	
	private void reset(PApplet p) {
		terrain = new Terrain(p, plt, WorldConstants.IMGPATCH);
		terrain.setStateColors(getColors(p));
		;
		terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);

		for (int i = 0; i < 2; i++)
			terrain.majorityRule();
		population = new Population(p, plt, terrain);
		PImage startBackground = p.loadImage("../sprites/startBackground.png");
		startBackground.resize(p.width, p.height);
		p.image(startBackground, 0, 0);
		start = false;
		checkAgents = false;
		checkTiles = false;
		grass = p.loadImage("../sprites/grass_1.png");
		plant = p.loadImage("../sprites/grass_full_v2.png");
		obstacle = p.loadImage("../sprites/obstacle.png");;
		fruit = p.loadImage("../sprites/grass_fruit.png");;
		lake = p.loadImage("../sprites/lake.png");;
		lakefish = p.loadImage("../sprites/lakeFish.png");;
	}
	
	public void controlMenu(PApplet p) {
		p.pushStyle();
		PFont font = p.createFont("../fonts/CartooNature.ttf",20);
		p.textFont(font);
		p.fill(0);
		p.textSize(20);
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(200, 100, p.width/2, 180);
		p.fill(255);
		p.image(ball, 200, 100);
		p.image(ball, 200+p.width/2-20, 100);
		p.image(ball, 200+p.width/2 -20, 100+160);
		p.image(ball, 200, 100+160);
		p.text("CONTROLS", 360, 130);
		p.textAlign(PApplet.CENTER);
		p.text("R - Restart Simulation", 200 + p.width/4, 170);
		p.text("P - Spawn a playable agent", 200 + p.width/4, 200);
		p.text("C - Agent details menu", 200 + p.width/4, 230);
		p.text("T - Tiles details menu", 200 + p.width/4, 260);
		

		p.popStyle();
	}
	
	
	public void agentsMenu(PApplet p) {
		p.pushStyle();
		PFont font = p.createFont("../fonts/CartooNature.ttf",20);
		p.textFont(font);
		p.fill(0);
		p.textSize(20);
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 50, 600, 100);
		PImage img = p.loadImage("../sprites/eevee.png");
		img.resize(0, 40);
		p.image(img, 120, 80);
		p.fill(255);
		p.text("Prey : ", 170, 80);
		p.text("- A small prey that just wanders around the field", 170, 100);
		p.text("- Feeds on plants and fruits that it finds", 170, 120);
		p.text("- Has vision to avoid obstacles such as rocks and lakes", 170, 140);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 180, 600, 100);
		img = p.loadImage("../sprites/flareon.png");
		img.resize(0, 40);
		p.image(img, 120, 210);
		p.fill(255);
		p.text("Mutated Prey : ", 170, 210);
		p.text("- A prey that mutated after eating fruit", 170, 230);
		p.text("- Is faster than a normal prey", 170, 250);
		p.text("- Has vision to avoid obstacles such as rocks and lakes", 170, 270);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 310, 600, 100);
		img = p.loadImage("../sprites/wingull.png");
		img.resize(0, 40);
		p.image(img, 110, 340);
		p.fill(255);
		p.text("Flock Prey : ", 170, 340);
		p.text("- A flying prey that can travel in a flock", 170, 360);
		p.text("- Has no vision and ignores obstacles", 170, 380);
		p.text("- Feeds on fruit and fishes from the lake", 170, 400);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 440, 600, 100);
		img = p.loadImage("../sprites/luxray.png");
		img.resize(0, 40);
		p.image(img, 120, 470);
		p.fill(255);
		p.text("Predator : ", 170, 470);
		p.text("- Hunts preys and mutated preys", 170, 490);
		p.text("- Can walk over obstacles but is slowed", 170, 510);
		p.text("- Can be hunted by a human hunter", 170, 530);

		p.textAlign(PApplet.CENTER);
		if(!checkAgents)
			p.text("Press Enter to continue", p.width/2, 580);
		p.popStyle();
	}
	
	public void tilesMenu(PApplet p) {
		p.pushStyle();
		PFont font = p.createFont("../fonts/CartooNature.ttf",20);
		p.textFont(font);
		p.fill(0);
		p.textSize(20);
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 50, 280, 100);
		grass.resize(0, 40);
		p.image(grass, 120, 80);
		p.noFill();
		p.strokeWeight(5);
		p.rect(120, 80, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Grass Tile : ", 170, 80);
		p.text("- Standard Map Tile", 170, 100);
		p.text("- Can stay empty or generate plants", 170, 120);

		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(400, 50, 280, 100);
		plant.resize(0, 40);
		p.image(plant, 420, 80);
		p.noFill();
		p.strokeWeight(5);
		p.rect(420, 80, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Plant Tile : ", 470, 80);
		p.text("- Can be consumed by preys", 470, 100);
		p.text("- Regenerates over time", 470, 120);
		

		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 180, 280, 100);
		fruit.resize(0, 40);
		p.image(fruit, 120, 220);
		p.noFill();
		p.strokeWeight(5);
		p.rect(120, 220, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Fruit Tile : ", 170, 220);
		p.text("- Can be consumed by preys", 170, 240);
		p.text("- Mutates preys if eaten by them", 170, 260);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(400, 180, 280, 100);
		obstacle.resize(0, 40);
		p.image(obstacle, 420, 220);
		p.noFill();
		p.strokeWeight(5);
		p.rect(420, 220, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Obstacle Tile : ", 470, 220);
		p.text("- Does not change throughout time", 470, 240);
		p.text("- Can be crossed by predators", 470, 260);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(100, 310, 280, 100);
		lake.resize(0, 38);
		p.image(lake, 120, 340);
		p.noFill();
		p.strokeWeight(5);
		p.rect(120, 340, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Lake Tile : ", 170, 340);
		p.text("- Can stay empty or generate Fish", 170, 360);
		p.text("- Slows predators when they cross", 170, 380);
		
		p.fill(p.color(190,145,120));
		p.stroke(255);
		p.strokeWeight(10);
		p.rect(400, 310, 280, 100);
		lakefish.resize(0, 38);
		p.image(lakefish, 420, 340);
		p.noFill();
		p.strokeWeight(5);
		p.rect(420, 340, 40, 40);
		p.fill(255);
		p.textSize(12);
		p.text("Fish Tile : ", 470, 340);
		p.text("- Consumed by flock prey", 470, 360);
		p.text("- Regenerates over time ", 470, 380);
		
		
		p.textSize(20);
		p.textAlign(PApplet.CENTER);
		if(!checkTiles)
			p.text("Press Enter to continue", p.width/2, 580);

		p.popStyle();
	}
	
	public void step(PApplet p) {
		ac.animalCount(population.getPreyNumber(), population.getFlockNumber(), population.getPredatorNumber(),
				population.getMutateNumber());
		ac.display(p, pltCounter);
		terrain.display(p);
		population.display(p, plt);
		p.image(treeLine, 0, 0);
		p.image(treeLineVert, 0, 0);
		p.image(treeLineVert, p.width-20, 0);
		p.image(treeLine, 0, 355);
		musicStart = false;
	}
	
	public static synchronized void playSound(final String url) {
		  new Thread(new Runnable() {
		  // The wrapper thread is unnecessary, unless it blocks on the
		  // Clip finishing; see comments.
		    public void run() {
		      try {
		    	clip  = AudioSystem.getClip();
		        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url).getAbsoluteFile());
		        clip.open(inputStream);
		        clip.start();
		        
		      } catch (Exception e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }).start();
		}


}
