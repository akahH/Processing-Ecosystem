package ecosystem;

public class WorldConstants {

	public final static double[] WINDDOW = { -10, 10, -10, 10 };
	public final static double[] WINDDOW_COUNTER = { -10, 10, -5, 5 };

	public final static int NROWS = 20;
	public final static int NCOLS = 40;

	public static enum PatchType {
		EMPTY, OBSTACLE, FERTILE, FOOD, FRUIT, LAKE, LAKEFISH, EMPTYLAKE
	}

	public final static double[] PATCH_TYPE_PROB = { 0.1f, 0.1f, 0.2f, 0.3f, 0.0f, 0.05f, 0.1f, 0.15f };
	public final static String[] IMGPATCH = { "../sprites/grass_1.png", "../sprites/obstacle.png",
			"../sprites/grass_1.png", "../sprites/grass_full_v2.png", "../sprites/grass_fruit.png",
			"../sprites/lake.png", "../sprites/lakeFish.png", "../sprites/lake.png" };
	public final static int NSTATES = PatchType.values().length;
	public static int[][] TERRAIN_COLORS = { { 200, 200, 60 }, { 160, 30, 70 }, { 200, 200, 60 }, { 40, 200, 20 },
			{ 0, 150, 150 }, { 0, 150, 150 }, { 0, 150, 150 }, { 0, 150, 150 } };
	public final static float[] REGENERATION_TIME = { 10.f, 20.f };

	public final static float PREY_SIZE = 0.2f;
	public final static float PREY_MASS = 1f;

	public final static int INI_PREY_POPULATION = 3;
	public final static int INI_PREDATOR_POPULATION = 1;

	public final static float INI_PREY_ENERGY = 10f;
	public final static float INI_FLOCK_ENERGY = 15f;
	public final static float FLOCK_WANDER_ENERGY = 26f;
	public final static float PREY_ENERGY_TO_REPRODUCE = 25f;
	public final static float FLOCK_ENERGY_TO_REPRODUCE = 20f;

	public final static float INI_PREDATOR_ENERGY = 20f;
	public final static float PREDATOR_ENERGY_TO_REPRODUCE = 30f;

	public final static float ENERGY_FROM_PLANT = 2f;
	public final static float ENERGY_FROM_PREY = 5f;
	public final static float ENERGY_FROM_FISH = 5f;

	public final static int[] PREY_COLOR = { 80, 100, 220 };
	public final static int[] PREY_MUTATE_COLOR = { 80, 220, 220 };
	public final static int[] PREDATOR_COLOR = { 255, 0, 0 };

	// Flock
	public final static int INI_FLOCK_SIZE = 20;
	public final static int[] FLOCK_COLOR = { 120, 0, 120 };
	public final static float[] SACWEIGHTS = { 1f, 1f, 1f };

}
