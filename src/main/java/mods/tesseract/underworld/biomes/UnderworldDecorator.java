package mods.tesseract.underworld.biomes;

import mods.tesseract.underworld.world.WorldGenMinableUnderworld;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class UnderworldDecorator extends BiomeDecorator {
    private final WorldGenerator silverfishGen;

    public UnderworldDecorator() {
        super();
        this.bigMushroomGen = new WorldGenBigMushroom(0);
        this.dirtGen = new WorldGenMinableUnderworld(Blocks.dirt, 32);
        this.gravelGen = new WorldGenMinableUnderworld(Blocks.gravel, 32);
        //this.copperGen = new WorldGenMinable(Blocks.oreCopper, 6);
        //this.silverGen = new WorldGenMinable(Blocks.oreSilver, 6);
        this.goldGen = new WorldGenMinableUnderworld(Blocks.gold_ore, 4, true);
        this.ironGen = new WorldGenMinableUnderworld(Blocks.iron_ore, 6, true);
        this.redstoneGen = new WorldGenMinableUnderworld(Blocks.redstone_ore, 5);
        this.diamondGen = new WorldGenMinableUnderworld(Blocks.diamond_ore, 3);
        this.lapisGen = new WorldGenMinableUnderworld(Blocks.lapis_ore, 3);
        this.silverfishGen = new WorldGenMinableUnderworld(Blocks.monster_egg, 3);
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
        this.genMinable(300, (WorldGenMinableUnderworld) this.gravelGen);
        //this.genMinable(40, this.copperGen, true);
        //this.genMinable(10, this.silverGen, true);
        this.genMinable(160, (WorldGenMinableUnderworld) this.goldGen);
        this.genMinable(480, (WorldGenMinableUnderworld) this.ironGen);
        //this.genMinable(10, this.mithrilGen, true);
        //this.genMinable(5, this.adamantiteGen, true);
        this.genMinable(80, (WorldGenMinableUnderworld) this.redstoneGen);
        this.genMinable(40, (WorldGenMinableUnderworld) this.diamondGen);
        this.genMinable(40, (WorldGenMinableUnderworld) this.lapisGen);
        this.genMinable(400, (WorldGenMinableUnderworld) this.silverfishGen);
    }
}
