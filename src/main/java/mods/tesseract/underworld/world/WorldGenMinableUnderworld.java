package mods.tesseract.underworld.world;

import cpw.mods.fml.common.registry.GameRegistry;
import mods.tesseract.underworld.config.IConfigCSV;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMinableUnderworld extends WorldGenerator implements IConfigCSV {
    public static final String[] types = {"oreDict", "block", "blockMeta", "veinSize", "minY", "maxY", "blockToReplace","frequency", "sizeIncreasesWithDepth"};
    public String oreDict;
    public Block minableBlock;
    public int blockMetadata;
    public int veinSize;
    public int minY;
    public int maxY;
    public Block blockToReplace;
    public int frequency;
    public boolean sizeIncreasesWithDepth;

    @Override
    public String[] getTypes() {
        return types;
    }

    @Override
    public IConfigCSV parseCSV(String[] csv) {
        this.oreDict = csv[0];
        String[] b = IConfigCSV.split(csv[1], ':');
        this.minableBlock = GameRegistry.findBlock(b[0], b[1]);
        this.blockMetadata = Integer.parseInt(csv[2]);
        this.veinSize = Integer.parseInt(csv[3]);
        this.minY = Integer.parseInt(csv[4]);
        this.maxY = Integer.parseInt(csv[5]);
        b = IConfigCSV.split(csv[6], ':');
        this.blockToReplace = GameRegistry.findBlock(b[0], b[1]);
        this.frequency = Integer.parseInt(csv[7]);
        this.sizeIncreasesWithDepth = Boolean.parseBoolean(csv[8]);
        return this;
    }

    /*
    public WorldGenMinableUnderworld(Block block, int size, boolean increase) {
        this(block, 0, size, Blocks.stone, 255, 0, increase);
    }

    public WorldGenMinableUnderworld(Block block, int size) {
        this(block, size, false);
    }

    public WorldGenMinableUnderworld(Block block, int meta, int size, Block base, int max, int min, boolean increase) {
        this.minableBlock = block;
        this.blockMetadata = meta;
        this.veinSize = size;
        this.blockToReplace = base;
        this.maxY = max;
        this.minY = min;
        this.sizeIncreasesWithDepth = increase;
    }
*/

    public int growVein(World world, Random rand, int blocks_to_grow, int x, int y, int z, boolean must_be_supported) {
        if (blocks_to_grow >= 1 && world.blockExists(x, y, z) && world.getBlock(x, y, z) == this.blockToReplace) {
            if (must_be_supported && (y < 1 || world.getBlock(x, y - 1, z).getMaterial().isReplaceable())) {
                return 0;
            } else {
                    world.setBlock(x, y, z, this.minableBlock, this.blockMetadata, 2);
                int ore_blocks_grown = 1;

                for (int attempts = 0; attempts < 16; ++attempts) {
                    int dx = 0;
                    int dy = 0;
                    int dz = 0;
                    int axis = rand.nextInt(3);
                    if (axis == 0) {
                        dx = rand.nextInt(2) == 0 ? -1 : 1;
                    } else if (axis == 1) {
                        dy = rand.nextInt(2) == 0 ? -1 : 1;
                    } else {
                        dz = rand.nextInt(2) == 0 ? -1 : 1;
                    }

                    ore_blocks_grown += this.growVein(world, rand, blocks_to_grow - ore_blocks_grown, x + dx, y + dy, z + dz, must_be_supported);
                    if (ore_blocks_grown == blocks_to_grow) {
                        break;
                    }
                }

                return ore_blocks_grown;
            }
        } else {
            return 0;
        }
    }

    @Override
    public boolean generate(World world, Random rand, int x, int y, int z) {
        if (world.blockExists(x, y, z) && world.getBlock(x, y, z) == this.blockToReplace) {
            int vein_size = this.veinSize;

            float scale = 1.0F;
            while (rand.nextInt(2) == 0) {
                scale = (float) ((double) scale * ((double) rand.nextFloat() * 0.6 + 0.7));
            }

            scale = Math.min(scale, 4.0F);
            if (sizeIncreasesWithDepth) {
                float relative_height = (float) y / 256;
                scale *= 1.0F - relative_height + 0.5F;
            }

            if ((float) vein_size * scale <= 3.0F && rand.nextInt(2) == 0) {
                if (rand.nextInt(2) != 0) {
                    return true;
                }

                scale *= 2.0F;
            }

            vein_size = (int) ((float) vein_size * scale);
            if (vein_size < 1) {
                return true;
            } else {
                if (vein_size == 1) {
                    if (rand.nextInt(3) != 0) {
                        return true;
                    }

                    vein_size = 3;
                } else if (vein_size == 2) {
                    if (rand.nextInt(3) == 0) {
                        return true;
                    }

                    vein_size = 3;
                } else if (vein_size > 32) {
                    vein_size = 32;
                }

                boolean must_be_supported = this.minableBlock == Blocks.gravel;
                this.growVein(world, rand, vein_size, x, y, z, must_be_supported);
                return true;
            }
        } else {
            return false;
        }
    }


    public int getRandomVeinHeight(World world, Random rand) {
        int y = rand.nextInt(maxY - minY);
        return minY + rand.nextFloat() < 0.75f ? y / 2 : y;
    }
}
