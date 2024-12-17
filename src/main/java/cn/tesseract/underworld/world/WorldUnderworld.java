package cn.tesseract.underworld.world;

import cn.tesseract.underworld.util.ChunkPostField;
import cn.tesseract.underworld.util.RNG;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Random;

public class WorldUnderworld {

    private final static HashMap<World, WorldUnderworld> worldUnderworld = new HashMap<>();
    public final ChunkPostField mycelium_posts = new ChunkPostField(1, 24, 0.0625F);
    public final RNG rng = new RNG();
    public final World world;

    public WorldUnderworld(World world) {
        rng.init(world);
        if (world.provider.dimensionId == -2) {
            Random rand = new Random(world.getSeed());
            rand.nextInt();
            mycelium_posts.setHashedWorldSeed(rand.nextLong());
        }
        this.world = world;
    }

    public static WorldUnderworld get(World world) {
        if (!worldUnderworld.containsKey(world))
            worldUnderworld.put(world, new WorldUnderworld(world));
        return worldUnderworld.get(world);
    }
}
