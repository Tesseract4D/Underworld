package mods.tesseract.underworld.biomes;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.Random;

public class UnderworldDecorator extends BiomeDecorator {
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
        if (this.randomGenerator.nextInt(6) == 0) {
            int x = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int y = this.randomGenerator.nextInt(128);
            int z = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, x, y, z);
        }

        if (this.randomGenerator.nextInt(4) == 0) {
            int x = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int y = this.randomGenerator.nextInt(128);
            int z = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, x, y, z);
        }
    }
}
