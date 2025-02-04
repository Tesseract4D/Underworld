package cn.tesseract.underworld.hook;

import cn.tesseract.mycelium.asm.Hook;
import cn.tesseract.mycelium.asm.ReturnCondition;
import cn.tesseract.underworld.Underworld;
import cn.tesseract.underworld.util.ChunkPostField;
import cn.tesseract.underworld.util.RNG;
import cn.tesseract.underworld.world.ChunkProviderUnderworld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.storage.ISaveHandler;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SuppressWarnings("unused")
public class UnderworldHook {
    @Hook(createMethod = true)
    public static float getFogNightVisionBrightness(EntityRenderer c, EntityPlayer player, float d) {
        return player.worldObj.provider.dimensionId == -2 ? 0 : c.getNightVisionBrightness(player, d);
    }

    @Hook(createMethod = true)
    public static boolean setPortalBlock(WorldServer c, int x, int y, int z, Block blockIn, int metadataIn, int flags) {
        if (blockIn == Blocks.portal) {
            return c.setBlock(x, y, z, blockIn, c.provider.dimensionId == -2 ? 0 : 4, flags);
        } else
            return c.setBlock(x, y, z, blockIn, metadataIn, flags);
    }

    @Hook(createMethod = true)
    public static void travelToDimensionUnderworld(Entity c, int dimensionId) {
        if (c.inPortal) {
            int type = ((PortalData) c).get_portalType(), dim = c.dimension;
            if (type == 0) {
                //int[] portal = ((PortalData) c).get_lastPortal();
                //BlockPortalUnderworld.Size size = new BlockPortalUnderworld.Size(c.worldObj, portal[0], portal[1], portal[2], portal[3]);
                //TODO
                if (dim == 0) {
                    ChunkCoordinates pos = c.worldObj.getSpawnPoint();
                    doTeleport(c, pos.posX + 0.5, c.worldObj.getTopSolidOrLiquidBlock(pos.posX, pos.posZ) + 2, pos.posZ + 0.5);
                } else if (dim == -2)
                    c.travelToDimension(0);
            } else if (type == 1) {
                if (dim == 0)
                    c.travelToDimension(-2);
                else if (dim == -1)
                    c.travelToDimension(-2);
            } else if (type == 2 && dim == -2)
                c.travelToDimension(-1);
        } else
            c.travelToDimension(dimensionId);
    }

    public static void doTeleport(Entity e, double x, double y, double z) {
        if (e instanceof EntityPlayerMP p)
            p.setPositionAndUpdate(x, y, z);
        else
            e.setPosition(x, y, z);
    }

    @Hook(createMethod = true, returnCondition = ReturnCondition.ALWAYS)
    public static IIcon getOverlayIcon(BlockPortal c, int i) {
        return c.getIcon(i, ((PortalData) Minecraft.getMinecraft().thePlayer).get_portalType() << 2);
    }

    @Hook(returnCondition = ReturnCondition.ON_TRUE)
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

    @Hook(injectOnExit = true, returnCondition = ReturnCondition.ALWAYS)
    public static boolean handleLavaMovement(Entity c, @Hook.ReturnValue boolean b) {
        return b || doesBoundingBoxContainBlock(c.worldObj, c.boundingBox.expand(0.001, 0.001, 0.001), Underworld.mantleOrCore);
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

    @Hook(injectOnExit = true, targetMethod = "<init>")
    public static void init(World c, ISaveHandler p_i45369_1_, String p_i45369_2_, WorldSettings p_i45369_3_, WorldProvider p_i45369_4_, Profiler p_i45369_5_) {
        ((WorldData) c).set_mycelium_posts(new ChunkPostField(1, c.getSeed(), 24, 0.0625F));
        ((WorldData) c).set_rng(new RNG(c));
    }

    @Hook(injectOnExit = true, targetMethod = "<init>")
    public static void init(Entity c, World worldIn) {
        ((PortalData) c).set_lastPortal(new int[4]);
    }

    @Hook(injectOnExit = true)
    public static void replaceBiomeBlocks(ChunkProviderHell c, int cx, int cz, Block[] blocks, byte[] meta, BiomeGenBase[] biomes) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int y, i;
                for (y = 127; y > 122; y--) {
                    i = ((z << 4) | x) << 7 | y;
                    if (blocks[i] == Blocks.bedrock)
                        blocks[i] = Underworld.mantleOrCore;
                }
                for (y = 0; y < 5; y++) {
                    i = ((z << 4) | x) << 7 | y;
                    if (blocks[i] == Blocks.bedrock) {
                        blocks[i] = Underworld.mantleOrCore;
                        meta[i] = 1;
                    }
                }
            }
        }
    }

    @Hook(targetMethod = "<init>", injectOnLine = 1, returnCondition = ReturnCondition.ON_TRUE)
    public static boolean init(Chunk c, World world, Block[] blocks, int cx, int xz) {
        if (world.provider.dimensionId == -2) {
            int maxY = blocks.length / 256;
            int base_x = c.xPosition << 4;
            int base_z = c.zPosition << 4;
            RNG rng = ((WorldData) world).get_rng();
            Random random = new Random(world.getSeed() * (long) ChunkPostField.getIntPairHash(c.xPosition, c.zPosition));
            int y_offset = Underworld.underworld_y_offset;
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
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    int num_bedrock_blocks = random.nextInt(3) + 1;
                    for (int y = -y_offset; y < 256 - y_offset; ++y) {
                        Block block;
                        if (y < 0 || y > 127) {
                            if ((y += y_offset) < num_bedrock_blocks) {
                                block = Underworld.mantleOrCore;
                            } else if (y > 255 - num_bedrock_blocks) {
                                block = Blocks.bedrock;
                            } else {
                                block = Blocks.stone;
                                Block block_bedrock_id = Underworld.bedrockLayerBlock;
                                int local_xz_index = z + x * 16;
                                double bedrock_noise = Math.max(ChunkProviderUnderworld.bedrock_strata_1a_noise[local_xz_index], ChunkProviderUnderworld.bedrock_strata_1b_noise[local_xz_index]);
                                int dy = y - 3;
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
                                    block = Underworld.bedrockLayerBlock;
                                }
                                if (block != block_bedrock_id) {
                                    dy = y - 32;
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
                                            block = Underworld.bedrockLayerBlock;
                                        }
                                    }
                                }
                                if (block != block_bedrock_id) {
                                    dy = y - 72;
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
                                            block = Underworld.bedrockLayerBlock;
                                        }
                                    }
                                }
                                if (block != block_bedrock_id) {
                                    dy = y - 96;
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
                                            block = Underworld.bedrockLayerBlock;
                                        }
                                    }
                                }
                            }
                        } else {
                            int index = x << 11 | z << 7 | y;
                            block = index < blocks.length ? blocks[index] : Blocks.stone;
                            y += y_offset;
                        }
                        if (block != null) {
                            int section = y >> 4;
                            if (c.storageArrays[section] == null) {
                                c.storageArrays[section] = new ExtendedBlockStorage(section << 4, !world.provider.hasNoSky);
                            }
                            c.storageArrays[section].func_150818_a(x, y & 0xF, z, block);
                            /*if (block.getLightValue() > 0) {
                                c.storageArrays[section].setExtBlocklightValue(index, y & 15, z, block.getLightValue());
                                if (blocks[index + 1] == null)
                                    this.addPendingBlocklightUpdate(base_x + x, y, base_z + z);
                            }*/
                        }
                        y -= y_offset;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
