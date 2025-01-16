package cn.tesseract.underworld.world;

import cn.tesseract.underworld.Underworld;
import cn.tesseract.underworld.hook.WorldData;
import cn.tesseract.underworld.util.RNG;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenDungeons;

import java.util.List;
import java.util.Random;

public final class ChunkProviderUnderworld implements IChunkProvider {
    private final Random hellRNG;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    public NoiseGeneratorOctaves netherNoiseGen6;
    public NoiseGeneratorOctaves netherNoiseGen7;
    private final World worldObj;
    private double[] noiseField;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_1a;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_1b;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_2;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_3;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_4;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_1a_bump;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_1b_bump;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_1c_bump;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_2_bump;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_3_bump;
    public static NoiseGeneratorOctaves noise_gen_bedrock_strata_4_bump;
    public static double[] bedrock_strata_1a_noise = new double[256];
    public static double[] bedrock_strata_1b_noise = new double[256];
    public static double[] bedrock_strata_2_noise = new double[256];
    public static double[] bedrock_strata_3_noise = new double[256];
    public static double[] bedrock_strata_4_noise = new double[256];
    public static double[] bedrock_strata_1a_bump_noise = new double[256];
    public static double[] bedrock_strata_1b_bump_noise = new double[256];
    public static double[] bedrock_strata_1c_bump_noise = new double[256];
    public static double[] bedrock_strata_2_bump_noise = new double[256];
    public static double[] bedrock_strata_3_bump_noise = new double[256];
    public static double[] bedrock_strata_4_bump_noise = new double[256];

    public ChunkProviderUnderworld(World par1World, long par2) {
        this.worldObj = par1World;
        this.hellRNG = new Random(par2);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        noise_gen_bedrock_strata_1a = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_1b = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_2 = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_3 = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_4 = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_1a_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_1b_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_1c_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_2_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_3_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
        noise_gen_bedrock_strata_4_bump = new NoiseGeneratorOctaves(this.hellRNG, 4);
    }

    public void generateNetherTerrain(int par1, int par2, Block[] blocks) {
        byte var4 = 4;
        byte var5 = 32;
        int var6 = var4 + 1;
        byte var7 = 17;
        int var8 = var4 + 1;
        this.noiseField = this.initializeNoiseField(this.noiseField, par1 * var4, 0, par2 * var4, var6, var7, var8);

        for (int var9 = 0; var9 < var4; ++var9) {
            for (int var10 = 0; var10 < var4; ++var10) {
                for (int var11 = 0; var11 < 16; ++var11) {
                    double var12 = 0.125;
                    int i = ((var9) * var8 + var10) * var7;
                    double var14 = this.noiseField[i + var11];
                    int i1 = ((var9) * var8 + var10 + 1) * var7;
                    double var16 = this.noiseField[i1 + var11];
                    int i2 = ((var9 + 1) * var8 + var10) * var7;
                    double var18 = this.noiseField[i2 + var11];
                    int i3 = ((var9 + 1) * var8 + var10 + 1) * var7;
                    double var20 = this.noiseField[i3 + var11];
                    double var22 = (this.noiseField[i + var11 + 1] - var14) * var12;
                    double var24 = (this.noiseField[i1 + var11 + 1] - var16) * var12;
                    double var26 = (this.noiseField[i2 + var11 + 1] - var18) * var12;
                    double var28 = (this.noiseField[i3 + var11 + 1] - var20) * var12;

                    for (int var30 = 0; var30 < 8; ++var30) {
                        double var31 = 0.25;
                        double var33 = var14;
                        double var35 = var16;
                        double var37 = (var18 - var14) * var31;
                        double var39 = (var20 - var16) * var31;
                        for (int var41 = 0; var41 < 4; ++var41) {
                            int var42 = var41 + (var9 << 2) << 11 | (var10 << 2) << 7 | (var11 << 3) + var30;
                            short var43 = 128;
                            double var44 = 0.25;
                            double var46 = var33;
                            double var48 = (var35 - var33) * var44;

                            for (int var50 = 0; var50 < 4; ++var50) {
                                Block var51 = null;
                                if ((var11 << 3) + var30 < var5 - 8) {
                                    var51 = Blocks.water;
                                }

                                if (var46 > 0.0) {
                                    var51 = Blocks.stone;
                                }

                                blocks[var42] = var51;
                                var42 += var43;
                                var46 += var48;
                            }

                            var33 += var37;
                            var35 += var39;
                        }

                        var14 += var22;
                        var16 += var24;
                        var18 += var26;
                        var20 += var28;
                    }
                }
            }
        }

    }

    public void replaceBlocksForBiome(int par1, int par2, Block[] par3ArrayOfByte) {

    }

    public Chunk loadChunk(int par1, int par2) {
        return this.provideChunk(par1, par2);
    }

    public Chunk provideChunk(int par1, int par2) {
        this.hellRNG.setSeed((long) par1 * 341873128712L + (long) par2 * 132897987541L);
        Block[] var3 = new Block[32768];
        this.generateNetherTerrain(par1, par2, var3);
        this.replaceBlocksForBiome(par1, par2, var3);
        placeRandomCobwebs(worldObj, var3, this.hellRNG);
        Chunk var4 = new Chunk(this.worldObj, var3, par1, par2);
        BiomeGenBase[] var5 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, par1 * 16, par2 * 16, 16, 16);
        byte[] var6 = var4.getBiomeArray();

        for (int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = (byte) var5[var7].biomeID;
        }

        var4.generateHeightMap();
        var4.resetRelightChecks();
        return var4;
    }

    public static void placeRandomCobwebs(World world, Block[] block_ids, Random rand) {
        int random_number_index = rand.nextInt();
        Block web_block_id = Blocks.web;
        Block lava_still_block_id = Blocks.lava;
        Block lava_moving_block_id = Blocks.flowing_lava;
        Block stone_block_id = Blocks.stone;
        int frequency = 128;
        RNG rng = ((WorldData) world).get_rng();

        for (int attempts = 0; attempts < frequency; ++attempts) {
            ++random_number_index;
            int x = rng.int_14_plus_1[random_number_index & 32767];
            ++random_number_index;
            int y = rng.int_126_plus_1[random_number_index & 32767];
            ++random_number_index;
            int z = rng.int_14_plus_1[random_number_index & 32767];
            int index = (((z << 4) + x) << 7) + y;
            if (block_ids[index] == null) {
                Block block_id_above = block_ids[index + 1];
                Block block_id_below = block_ids[index - 1];
                Block block_id_front = block_ids[index + 128];
                Block block_id_back = block_ids[index - 128];
                Block block_id_right = block_ids[index + 2048];
                Block block_id_left = block_ids[index - 2048];
                int solid_face_adjacent_blocks = 0;
                if (block_id_above != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (block_id_below != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (block_id_front != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (block_id_back != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (block_id_right != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (block_id_left != null) {
                    ++solid_face_adjacent_blocks;
                }

                if (solid_face_adjacent_blocks >= 4 && block_id_below != lava_still_block_id && block_id_below != lava_moving_block_id && (block_id_above == stone_block_id || block_id_below == stone_block_id || block_id_front == stone_block_id || block_id_back == stone_block_id || block_id_right == stone_block_id || block_id_left == stone_block_id)) {
                    block_ids[index] = web_block_id;
                    attempts -= frequency * 4;
                }
            }
        }
    }

    private double[] initializeNoiseField(double[] noise, int par2, int par3, int par4, int par5, int par6, int par7) {
        if (noise == null) {
            noise = new double[par5 * par6 * par7];
        }

        double var8 = 684.412;
        double var10 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, par2, par3, par4, par5, 1, par7, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, par2, par3, par4, par5, 1, par7, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, par2, par3, par4, par5, par6, par7, var8 / 80.0, var10 / 60.0, var8 / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, par2, par3, par4, par5, par6, par7, var8, var10, var8);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, par2, par3, par4, par5, par6, par7, var8, var10, var8);
        int var12 = 0;
        double[] var14 = new double[par6];

        int var15;
        for (var15 = 0; var15 < par6; ++var15) {
            var14[var15] = Math.cos((double) var15 * Math.PI * 6.0 / (double) par6) * 2.0;
            double var16 = var15;
            if (var15 > par6 / 2) {
                var16 = par6 - 1 - var15;
            }

            if (var16 < 4.0) {
                var16 = 4.0 - var16;
                var14[var15] -= var16 * var16 * var16 * 10.0;
            }
        }

        for (var15 = 0; var15 < par5; ++var15) {
            for (int var36 = 0; var36 < par7; ++var36) {

                for (int var23 = 0; var23 < par6; ++var23) {
                    double var24;
                    double var26 = var14[var23];
                    double var28 = this.noiseData2[var12] / 512.0;
                    double var30 = this.noiseData3[var12] / 512.0;
                    double var32 = (this.noiseData1[var12] / 10.0 + 1.0) / 2.0;
                    if (var32 < 0.0) {
                        var24 = var28;
                    } else if (var32 > 1.0) {
                        var24 = var30;
                    } else {
                        var24 = var28 + (var30 - var28) * var32;
                    }

                    var24 -= var26;
                    double var34;
                    if (var23 > par6 - 4) {
                        var34 = (float) (var23 - (par6 - 4)) / 3.0F;
                        var24 = var24 * (1.0 - var34) + -10.0 * var34;
                    }

                    noise[var12] = var24;
                    ++var12;
                }
            }
        }

        return noise;
    }

    public boolean chunkExists(int par1, int par2) {
        return true;
    }

    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3) {
        BlockFalling.fallInstantly = true;
        int var4 = par2 * 16;
        int var5 = par3 * 16;
        for (int i = 0; i < 24; ++i) {
            int x = var4 + this.hellRNG.nextInt(16) + 8;
            int y = this.hellRNG.nextInt(32) + 20 + Underworld.underworld_y_offset;
            int z = var5 + this.hellRNG.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(this.worldObj, this.hellRNG, x, y, z);
        }

        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
        biome.decorate(this.worldObj, this.worldObj.rand, var4, var5);
        BlockFalling.fallInstantly = false;
    }

    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate) {
        return true;
    }

    public void saveExtraData() {
    }

    public boolean unloadQueuedChunks() {
        return false;
    }

    public boolean canSave() {
        return true;
    }

    public String makeString() {
        return "UnderworldRandomLevelSource";
    }

    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4) {
        BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(par2, par4);
        return var5 == null ? null : var5.getSpawnableList(par1EnumCreatureType);
    }

    public ChunkPosition func_147416_a(World worldIn, String structureName, int x, int y, int z) //getNearestStructurePos
    {
        return null;
    }

    public int getLoadedChunkCount() {
        return 0;
    }

    public void recreateStructures(int par1, int par2) {
    }
}
