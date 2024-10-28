package mods.tesseract.underworld.biomes;

import mods.tesseract.underworld.world.WorldGenMinableUnderworld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

import java.util.Random;

public class UnderworldDecorator extends BiomeDecorator {
    public WorldGenMinableUnderworld[] oreGens = {};

    public UnderworldDecorator() {
        super();
        this.bigMushroomGen = new WorldGenBigMushroom(0);
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

    public void genDecorations(BiomeGenBase biome) {
        this.generateOres();
    }

    public void generateOres() {
        for (WorldGenMinableUnderworld g : oreGens) {
            int f = g.frequency;
            while (f-- > 0) {
                    int x = chunk_X + randomGenerator.nextInt(16);
                    int y = g.getRandomVeinHeight(currentWorld, randomGenerator);
                    int z = chunk_Z + randomGenerator.nextInt(16);
                    if (y >= 0) {
                        g.generate(currentWorld, randomGenerator, x, y, z);
                    }
            }
        }
    }
}
