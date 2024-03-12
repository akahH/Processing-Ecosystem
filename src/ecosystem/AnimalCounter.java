package ecosystem;

import java.util.HashMap;


import processing.core.PApplet;

import processing.core.PFont;
import processing.core.PImage;

import simple_subplot.SubPlot;

public class AnimalCounter {

	private HashMap<String, Integer> hash;
	private PImage background;
	
	public AnimalCounter(SubPlot plt, PApplet p) {
		hash = new HashMap<String, Integer>();
		background = p.loadImage("../sprites/background.png");
		background.resize(p.width, p.height);

		
	}
	
	public void animalCount(int prey, int flock, int predator, int mutate){
		hash.put("prey", prey);
		hash.put("flock", flock);
		hash.put("predator", predator);
		hash.put("mutate", mutate);
	}
	
	public void display(PApplet p, SubPlot plt) {
		
		p.pushStyle();
		p.background(background);
		
		PFont font = p.createFont("../fonts/CartooNature.ttf",20);
		p.textFont(font);
		p.fill(0);
		p.textSize(20);
		PImage img = p.loadImage("../sprites/eevee.png");
		img.resize(0, 30);
		
		float[] pp = plt.getPixelCoord(-8.5, 1);
		p.image(img,pp[0], pp[1]);
		pp = plt.getPixelCoord(-8.2, -2);
		p.text(hash.get("prey"), pp[0], pp[1]);
		pp = plt.getPixelCoord(-8.7, 2);
		p.text("Preys", pp[0], pp[1]);
		
		
		
		img = p.loadImage("../sprites/flareon.png");
		img.resize(0, 30);
		pp = plt.getPixelCoord(-3.4, 1);
		p.image(img,pp[0], pp[1]);
		pp = plt.getPixelCoord(-3.2, -2);
		p.text(hash.get("mutate"), pp[0], pp[1]);
		pp = plt.getPixelCoord(-4.4, 2);
		p.text("Mutated Preys", pp[0], pp[1]);
		
		
		
		img = p.loadImage("../sprites/wingull.png");
		img.resize(0, 30);
		pp = plt.getPixelCoord(2.5, 1);
		p.image(img,pp[0], pp[1]);
		pp = plt.getPixelCoord(2.8, -2);
		p.text(hash.get("flock"), pp[0], pp[1]);
		pp = plt.getPixelCoord(2., 2);
		p.text("Flock Preys", pp[0], pp[1]);
		
		img = p.loadImage("../sprites/luxray.png");
		img.resize(0, 30);
		pp = plt.getPixelCoord(7.5, 1);
		p.image(img,pp[0], pp[1]);
		pp = plt.getPixelCoord(7.8, -2);
		p.text(hash.get("predator"), pp[0], pp[1]);
		pp = plt.getPixelCoord(7., 2);
		p.text("Predators", pp[0], pp[1]);
		
		pp = plt.getPixelCoord(0, -4);
		p.textAlign(PApplet.CENTER);
		p.text("Press M to open the Controls Menu || Click on the Agents to add more", pp[0], pp[1]);
		
		

		p.popStyle();
		
	}
	
	
}
