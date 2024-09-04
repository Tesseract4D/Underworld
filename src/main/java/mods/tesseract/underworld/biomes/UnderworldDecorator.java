package mods.tesseract.underworld.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

import static net.minecraft.world.EnumSkyBlock.Block;

public class UnderworldDecorator extends BiomeDecorator {
    private final WorldGenMinable silverfishGen;

    public UnderworldDecorator() {
        super();
        this.bigMushroomGen = new WorldGenBigMushroom(0);
        this.dirtGen = new WorldGenMinable(Blocks.dirt, 32);
        this.gravelGen = new WorldGenMinable(Blocks.gravel, 32);
        this.coalGen = new WorldGenMinable(Blocks.coal_ore, 16);
        //this.copperGen = new WorldGenMinable(Blocks.oreCopper, 6);
        //this.silverGen = new WorldGenMinable(Blocks.oreSilver, 6);
        this.goldGen = new WorldGenMinable(Blocks.gold_ore, 4);
        this.ironGen = new WorldGenMinable(Blocks.iron_ore, 6);
        //this.mithrilGen = new WorldGenMinable(Blocks.oreMithril, 3);
        //this.adamantiteGen = new WorldGenMinable(Blocks.oreAdamantium, 3);
        this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore, 5);
        this.diamondGen = new WorldGenMinable(Blocks.diamond_ore, 3);
        this.lapisGen = new WorldGenMinable(Blocks.lapis_ore, 3);
        this.silverfishGen = new WorldGenMinable(Blocks.monster_egg, 3);
    }

    protected void genMinable(int frequency, WorldGenMinable world_gen_minable, boolean vein_size_increases_with_depth) {
        while(frequency-- > 0) {
            if (this.randomGenerator.nextInt(10) == 0) {
                int x = this.chunk_X + this.randomGenerator.nextInt(16);
                int y = world_gen_minable.getRandomVeinHeight(this.currentWorld, this.randomGenerator);
                int z = this.chunk_Z + this.randomGenerator.nextInt(16);
                if (y >= 0) {
                    world_gen_minable.generate(this.currentWorld, this.randomGenerator, x, y, z, vein_size_increases_with_depth);
                }
            }
        }

    }

    @Override
    public void decorateChunk(World worldIn, Random random, BiomeGenBase biome, int x, int z) {
        if (this.currentWorld != null) {
            System.out.println("Already decorating");
        } else {
            this.currentWorld = worldIn;
            this.randomGenerator = random;
            this.chunk_X = x;
            this.chunk_Z = z;
            this.genDecorations(biome);
            this.currentWorld = null;
            this.randomGenerator = null;
        }
    }

    @Override
    protected void genDecorations(BiomeGenBase biome) {

    }
}
