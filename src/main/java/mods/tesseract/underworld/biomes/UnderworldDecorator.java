package mods.tesseract.underworld.biomes;

import mods.tesseract.underworld.Main;
import mods.tesseract.underworld.config.ConfigUnderworld;
import mods.tesseract.underworld.config.IConfigCSV;
import mods.tesseract.underworld.world.WorldGenMinableUnderworld;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

import java.util.ArrayList;
import java.util.Random;

public class UnderworldDecorator extends BiomeDecorator {
    public final WorldGenMinableUnderworld[] oreGens;

    public UnderworldDecorator() {
        super();
        ArrayList<IConfigCSV> ores;
        try {
            ores = IConfigCSV.parseCSV(Main.oreEntries.config, WorldGenMinableUnderworld.class);
        } catch (IllegalArgumentException e) {
            Main.oreEntries.reset();
            ores = IConfigCSV.parseCSV(Main.oreEntries.defaultConfig, WorldGenMinableUnderworld.class);
        }
        this.oreGens = ores.toArray(new WorldGenMinableUnderworld[0]);
        this.bigMushroomGen = new WorldGenBigMushroom(0);
    }

    public void genMinable(int frequency, WorldGenMinableUnderworld world_gen_minable) {
        while(frequency-- > 0) {
            if (this.randomGenerator.nextInt(10) == 0) {
                int x = this.chunk_X + this.randomGenerator.nextInt(16);
                int y = world_gen_minable.getRandomVeinHeight(currentWorld, randomGenerator);
                int z = this.chunk_Z + this.randomGenerator.nextInt(16);
                if (y >= 0) {
                    world_gen_minable.generate(this.currentWorld, this.randomGenerator, x, y, z);
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

    public void genDecorations(BiomeGenBase biome) {
        this.generateOres();
    }

    public void generateOres() {
        for (WorldGenMinableUnderworld g : oreGens) {
            int f = g.frequency;
            while (f-- > 0) {
                if (this.randomGenerator.nextInt(ConfigUnderworld.ore_mutiplier) == 0) {
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
}
