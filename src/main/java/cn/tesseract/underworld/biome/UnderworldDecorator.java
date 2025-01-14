package cn.tesseract.underworld.biome;

import cn.tesseract.underworld.Underworld;
import cn.tesseract.underworld.config.ConfigUnderWorld;
import cn.tesseract.underworld.world.WorldGenMinableUnderworld;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenLiquids;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
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
            (new WorldGenLiquids(Blocks.flowing_water)).generate(this.currentWorld, this.randomGenerator, k, l + Underworld.underworld_y_offset, i1);
        }

        for (int j = 0; j < 10; ++j) {
            int k = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int l = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
            int i1 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            (new WorldGenLiquids(Blocks.flowing_lava)).generate(this.currentWorld, this.randomGenerator, k, l + Underworld.underworld_y_offset, i1);
        }
        this.generateOres();
    }

    public void generateOres() {
        if (!initialized) {
            initialized = true;
            ArrayList<WorldGenMinableUnderworld> ores = new ArrayList<>();
            Underworld.oreEntries.forEach(a -> {
                if (a.disable)
                    return;
                String[] b = a.block.split(":");
                Block block = GameRegistry.findBlock(b[0], b[1]);
                int meta = a.blockMeta;
                if (block == null) {
                    ArrayList<ItemStack> o = OreDictionary.getOres(a.oreDict);
                    for (ItemStack k : o) {
                        if (k.getItem() instanceof ItemBlock l) {
                            block = l.field_150939_a;
                            meta = k.getItemDamage();
                            break;
                        }
                    }
                }
                if (block == null) {
                    System.out.println("[Warning] Cannot find block for \"" + a.file.getName() + "\", ignored.");
                    return;
                }
                b = a.blockToReplace.split(":");
                Block replace = GameRegistry.findBlock(b[0], b[1]);
                if (replace == null)
                    replace = Blocks.stone;
                ores.add(new WorldGenMinableUnderworld(block, meta, replace, a.veinSize, a.minY, a.maxY, a.oreDict.equals("gravel") ? a.frequency : (int) (a.frequency * ConfigUnderWorld.ore_mutiplier), a.uniformDistribution, a.sizeIncreasesWithDepth));
            });
            oreGens = ores.toArray(new WorldGenMinableUnderworld[0]);
            Underworld.oreEntries = null;
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
