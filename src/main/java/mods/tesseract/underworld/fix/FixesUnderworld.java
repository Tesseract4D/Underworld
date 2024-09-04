package mods.tesseract.underworld.fix;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.tesseract.underworld.Main;
import mods.tesseract.underworld.util.ChunkPostField;
import mods.tesseract.underworld.util.RNG;
import mods.tesseract.underworld.world.ChunkProviderUnderworld;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.DimensionManager;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;
import net.tclproject.mysteriumlib.asm.annotations.ReturnedValue;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SuppressWarnings("unused")
public class FixesUnderworld {
    @Fix
    public static void setWorld(DimensionManager c, int id, WorldServer world) {
        ((IWorld) world).rng.init(world);
        if (id == -2 && world != null) {
            Random rand = new Random(world.getSeed());
            rand.nextInt();
            ((IWorld) world).mycelium_posts.setHashedWorldSeed(rand.nextLong());
        }
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, nullReturned = true)
    @SideOnly(Side.CLIENT)
    public static boolean renderVignette(GuiIngame c, float x, int y, int z) {
        if (c.mc.theWorld.provider.dimensionId == -2) {
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBlendFunc(770, 771);
            return true;
        }
        return false;
    }

    @Fix(createNewMethod = true)
    public static float getFogNightVisionBrightness(EntityRenderer c, EntityPlayer player, float d) {
        return player.worldObj.provider.dimensionId == -2 ? 0 : c.getNightVisionBrightness(player, d);
    }


    @Fix(insertOnExit = true, returnSetting = EnumReturnSetting.ALWAYS)
    public static boolean handleLavaMovement(Entity c, @ReturnedValue boolean b) {
        return b || doesBoundingBoxContainBlock(c.worldObj, c.boundingBox.expand(0.001, 0.001, 0.001), Main.mantleOrCore);
    }

    public static boolean doesBoundingBoxContainBlock(World world, AxisAlignedBB box, Block b) {
        int i = MathHelper.floor_double(box.minX);
        int j = MathHelper.floor_double(box.maxX + 1.0D);
        int k = MathHelper.floor_double(box.minY);
        int l = MathHelper.floor_double(box.maxY + 1.0D);
        int i1 = MathHelper.floor_double(box.minZ);
        int j1 = MathHelper.floor_double(box.maxZ + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    if (world.getBlock(k1, l1, i2) == b) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Fix(insertOnExit = true)
    public static void applyEntityAttributes(EntitySkeleton c) {
        c.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(5);
    }

    @Fix(targetMethod = "<init>", insertOnLine = 1, returnSetting = EnumReturnSetting.ON_TRUE, nullReturned = true)
    public static boolean Chunk(Chunk c, World world, Block[] blocks, int cx, int xz) {
        int maxY = blocks.length / 256;
        int base_x = c.xPosition << 4;
        int base_z = c.zPosition << 4;
        if (world.provider.dimensionId == -2) {
            RNG rng = ((IWorld) world).rng;
            Random random = new Random(world.getSeed() * (long) ChunkPostField.getIntPairHash(c.xPosition, c.zPosition));
            int y_offset = Main.underworld_y_offset;
            double scale_xz = 0.015625;
            double scale_y = 0.03125;
            ChunkProviderUnderworld.bedrock_strata_1a_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_1a.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_1a_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 2.0, scale_y * 2.0, scale_xz * 2.0);
            ChunkProviderUnderworld.bedrock_strata_1b_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_1b.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_1b_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 2.0, scale_y * 2.0, scale_xz * 2.0);
            ChunkProviderUnderworld.bedrock_strata_2_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_2.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_2_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            ChunkProviderUnderworld.bedrock_strata_3_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_3.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_3_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            ChunkProviderUnderworld.bedrock_strata_4_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_4.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_4_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            scale_xz = 0.25;
            ChunkProviderUnderworld.bedrock_strata_1a_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_1a_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_1a_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 0.5, scale_y * 2.0, scale_xz * 0.5);
            ChunkProviderUnderworld.bedrock_strata_1b_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_1b_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_1b_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz, scale_y * 2.0, scale_xz);
            ChunkProviderUnderworld.bedrock_strata_1c_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_1c_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_1c_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 2.0, scale_y * 2.0, scale_xz * 2.0);
            ChunkProviderUnderworld.bedrock_strata_2_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_2_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_2_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            ChunkProviderUnderworld.bedrock_strata_3_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_3_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_3_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            ChunkProviderUnderworld.bedrock_strata_4_bump_noise = ChunkProviderUnderworld.noise_gen_bedrock_strata_4_bump.generateNoiseOctaves(ChunkProviderUnderworld.bedrock_strata_4_bump_noise, base_x, 0, base_z, 16, 1, 16, scale_xz * 4.0, scale_y * 2.0, scale_xz * 4.0);
            int rng_index = c.xPosition * 2653 + c.zPosition * 6714631;
            for (int var6 = 0; var6 < 16; ++var6) {
                for (int var7 = 0; var7 < 16; ++var7) {
                    int num_bedrock_blocks = random.nextInt(3) + 1;
                    for (int var8 = -y_offset; var8 < 256 - y_offset; ++var8) {
                        Block var9;
                        int index;
                        if (var8 < 0 || var8 > 127) {
                            index = -1;
                            if ((var8 += y_offset) < num_bedrock_blocks) {
                                var9 = Main.mantleOrCore;
                            } else if (var8 > 255 - num_bedrock_blocks) {
                                var9 = Blocks.bedrock;
                            } else {
                                var9 = Blocks.stone;
                                Block block_bedrock_id = Blocks.bedrock;
                                int local_xz_index = var7 + var6 * 16;
                                double bedrock_noise = Math.max(ChunkProviderUnderworld.bedrock_strata_1a_noise[local_xz_index], ChunkProviderUnderworld.bedrock_strata_1b_noise[local_xz_index]);
                                int dy = var8 - 3;
                                double bump_noise = ChunkProviderUnderworld.bedrock_strata_1a_bump_noise[local_xz_index];
                                if (bump_noise > 0.0) {
                                    bedrock_noise += bump_noise * 0.25;
                                }
                                if ((bump_noise = ChunkProviderUnderworld.bedrock_strata_1b_bump_noise[local_xz_index]) > 0.0) {
                                    bedrock_noise += bump_noise * 0.125;
                                }
                                if ((bump_noise = ChunkProviderUnderworld.bedrock_strata_1c_bump_noise[local_xz_index]) > 0.0) {
                                    bedrock_noise += bump_noise * 0.125;
                                }
                                if ((bump_noise = ChunkProviderUnderworld.bedrock_strata_4_bump_noise[local_xz_index]) > 0.0) {
                                    bedrock_noise += bump_noise * 0.09375 + 0.125;
                                }
                                if (bedrock_noise > 0.0 && (double) dy <= bedrock_noise * 7.0) {
                                    var9 = Blocks.bedrock;
                                }
                                if (var9 != block_bedrock_id) {
                                    dy = var8 - 32;
                                    if ((bedrock_noise = ChunkProviderUnderworld.bedrock_strata_2_noise[local_xz_index] - bedrock_noise * 1.5) > 0.0) {
                                        if (dy > 0 && (bump_noise = ChunkProviderUnderworld.bedrock_strata_2_bump_noise[local_xz_index]) > 0.0) {
                                            bedrock_noise += bump_noise * 0.25 + 0.25;
                                        }
                                        if (dy < 0) {
                                            if (rng.chance_in_2[++rng_index & Short.MAX_VALUE]) {
                                                ++dy;
                                            }
                                            dy = -dy;
                                        }
                                        if ((double) dy <= bedrock_noise * 2.0) {
                                            var9 = Blocks.bedrock;
                                        }
                                    }
                                }
                                if (var9 != block_bedrock_id) {
                                    dy = var8 - 72;
                                    bedrock_noise = ChunkProviderUnderworld.bedrock_strata_3_noise[local_xz_index] - ChunkProviderUnderworld.bedrock_strata_4_noise[local_xz_index] * 0.375;
                                    if ((bedrock_noise += 0.5) > 0.0) {
                                        if (dy > 0 && (bump_noise = ChunkProviderUnderworld.bedrock_strata_3_bump_noise[local_xz_index]) > 0.0) {
                                            bedrock_noise += bump_noise * 0.25 + 0.25;
                                        }
                                        if (dy < 0) {
                                            if (rng.chance_in_2[++rng_index & Short.MAX_VALUE]) {
                                                ++dy;
                                            }
                                            dy = -dy;
                                        }
                                        if ((double) dy <= bedrock_noise * 2.0) {
                                            var9 = Blocks.bedrock;
                                        }
                                    }
                                }
                                if (var9 != block_bedrock_id) {
                                    dy = var8 - 96;
                                    bedrock_noise = ChunkProviderUnderworld.bedrock_strata_4_noise[local_xz_index] - ChunkProviderUnderworld.bedrock_strata_3_noise[local_xz_index] * 0.375;
                                    if ((bedrock_noise += 0.5) > 0.0) {
                                        if (dy > 0 && (bump_noise = ChunkProviderUnderworld.bedrock_strata_4_bump_noise[local_xz_index]) > 0.0) {
                                            bedrock_noise += bump_noise * 0.25 + 0.25;
                                        }
                                        if (dy < 0) {
                                            if (rng.chance_in_2[++rng_index & Short.MAX_VALUE]) {
                                                ++dy;
                                            }
                                            dy = -dy;
                                        }
                                        if ((double) dy <= bedrock_noise * 2.0) {
                                            var9 = Blocks.bedrock;
                                        }
                                    }
                                }
                            }
                        } else {
                            index = var6 << 11 | var7 << 7 | var8;
                            var9 = index < blocks.length ? blocks[index] : Blocks.stone;
                            var8 += y_offset;
                        }
                        if (var9 != null) {
                            int var10 = var8 >> 4;
                            if (c.storageArrays[var10] == null) {
                                c.storageArrays[var10] = new ExtendedBlockStorage(var10 << 4, !world.provider.hasNoSky);
                            }
                            c.storageArrays[var10].func_150818_a(var6, var8 & 0xF, var7, var9);
                            if (var9.getLightValue() > 0) {
                                c.storageArrays[var10].setExtBlocklightValue(index, var8 & 15, var7, var9.getLightValue());
                                //if (blocks[index + 1] == null)
                                    //this.addPendingBlocklightUpdate(base_x + var6, var8, base_z + var7);
                            }
                        }
                        var8 -= y_offset;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
