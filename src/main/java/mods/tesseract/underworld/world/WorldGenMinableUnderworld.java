package mods.tesseract.underworld.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMinableUnderworld extends WorldGenerator {
    public Block minableBlock;
    public int blockMetadata;
    public int numberOfBlocks;
    public int maxY;
    public int minY;
    public Block blockToReplace;
    public boolean sizeIncreasesWithDepth;

    public WorldGenMinableUnderworld(Block block, int size, boolean increase) {
        this(block, 0, size, Blocks.stone, 255, 0, increase);
    }

    public WorldGenMinableUnderworld(Block block, int size) {
        this(block, size, false);
    }

    public WorldGenMinableUnderworld(Block block, int meta, int size, Block base, int max, int min, boolean increase) {
        this.minableBlock = block;
        this.blockMetadata = meta;
        this.numberOfBlocks = size;
        this.blockToReplace = base;
        this.maxY = max;
        this.minY = min;
        this.sizeIncreasesWithDepth = increase;
    }

    public int growVein(World world, Random rand, int blocks_to_grow, int x, int y, int z, boolean must_be_supported, boolean is_dirt) {
        if (blocks_to_grow >= 1 && world.blockExists(x, y, z) && world.getBlock(x, y, z) == this.blockToReplace) {
            if (must_be_supported && (y < 1 || world.getBlock(x, y - 1, z).getMaterial().isReplaceable())) {
                return 0;
            } else {
                if (is_dirt && world.canBlockSeeTheSky(x, y + 1, z)) {
                    BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
                    world.setBlock(x, y, z, biome != BiomeGenBase.desert && biome != BiomeGenBase.desertHills ? Blocks.grass : Blocks.sand, 0, 2);
                } else {
                    world.setBlock(x, y, z, this.minableBlock, this.blockMetadata, 2);
                }

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

                    ore_blocks_grown += this.growVein(world, rand, blocks_to_grow - ore_blocks_grown, x + dx, y + dy, z + dz, must_be_supported, is_dirt);
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
            int vein_size = this.numberOfBlocks;

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
                boolean is_dirt = this.minableBlock == Blocks.dirt;
                this.growVein(world, rand, vein_size, x, y, z, must_be_supported, is_dirt);
                return true;
            }
        } else {
            return false;
        }
    }


    public int getRandomVeinHeight(World world, Random rand) {
        int y = minY + rand.nextInt(maxY);
        return rand.nextFloat() < 0.75f ? y / 2 : y;
    }
}
