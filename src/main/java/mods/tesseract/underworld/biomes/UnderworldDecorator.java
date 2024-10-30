package mods.tesseract.underworld.biomes;

import mods.tesseract.underworld.Main;
import mods.tesseract.underworld.config.ConfigUnderworld;
import mods.tesseract.underworld.config.IConfigCSV;
import mods.tesseract.underworld.world.WorldGenMinableUnderworld;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenLiquids;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class UnderworldDecorator extends BiomeDecorator {
    public WorldGenMinableUnderworld[] oreGens = {};
    private boolean initialized;

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
        for (int j = 0; j < 25; ++j) {
            int k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int l = this.randomGenerator.nextInt(this.randomGenerator.nextInt(248) + 8);
            int i1 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, k, l+ Main.underworld_y_offset, i1);
        }

        for (int j = 0; j < 10; ++j) {
            int k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int l = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
            int i1 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, k, l + Main.underworld_y_offset, i1);
        }
        this.generateOres();
    }

    public void generateOres() {
        if (!initialized) {
            initialized = true;
            ArrayList<IConfigCSV> ores;
            try {
                ores = IConfigCSV.parseCSV(Main.oreEntries.config, WorldGenMinableUnderworld.class);
            } catch (IllegalArgumentException f) {
                Main.oreEntries.reset();
                ores = IConfigCSV.parseCSV(Main.oreEntries.defaultConfig, WorldGenMinableUnderworld.class);
            }
            ListIterator<IConfigCSV> t = ores.listIterator();
            while (t.hasNext()) {
                WorldGenMinableUnderworld g = (WorldGenMinableUnderworld) t.next();
                if (!g.oreDict.equals("gravel"))
                    g.frequency = (int) (g.frequency * ConfigUnderworld.ore_mutiplier);
                if (g.minableBlock == null || g.blockToReplace == null) {
                    t.remove();
                }
            }
            oreGens = ores.toArray(new WorldGenMinableUnderworld[0]);
        }
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
