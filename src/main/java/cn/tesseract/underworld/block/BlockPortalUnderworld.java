package cn.tesseract.underworld.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockPortalUnderworld extends BlockPortal {
    private IIcon runegate_icon;
    private IIcon nether_portal_icon;

    public BlockPortalUnderworld() {
        super();
    }

    public void registerBlockIcons(IIconRegister reg) {
        super.registerBlockIcons(reg);
        runegate_icon = reg.registerIcon("underworld:runegate");
        nether_portal_icon = reg.registerIcon("underworld:portal_nether");
    }

    @Override
    public boolean func_150000_e(World world, int x, int y, int z) {
        Size size = new Size(world, x, y, z, 1);

        if (size.isValid() && size.portalBlockCount == 0) {
            size.placePortalBlocks();
            return true;
        } else {
            size = new Size(world, x, y, z, 2);

            if (size.isValid() && size.portalBlockCount == 0) {
                size.placePortalBlocks();
                return true;
            } else {
                return false;
            }
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null) {
            entityIn.setInPortal();

        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {
        Size size = new Size(worldIn, x, y, z, 1);
        Size size1 = new Size(worldIn, x, y, z, 2);
        if (!((size.isValid() && size.portalBlockCount == size.width * size.height) || (size1.isValid() && size1.portalBlockCount == size1.width * size1.height)))
            worldIn.setBlock(x, y, z, Blocks.air);
    }

    public IIcon getIcon(int side, int meta) {
        return switch (meta) {
            case 1 -> blockIcon;
            case 2 -> nether_portal_icon;
            default -> runegate_icon;
        };
    }

    public static class Size {
        public final World world;
        public final int axis;
        public final int leftDir;
        public final int rightDir;
        public ChunkCoordinates bottomLeft;
        public int portalBlockCount;
        public int height;
        public int width;

        public Size(World world, int x, int y, int z, int axis) {
            this.world = world;
            this.axis = axis;
            rightDir = BlockPortal.field_150001_a[axis][0];
            leftDir = BlockPortal.field_150001_a[axis][1];

            int i = y;
            while (y > i - 21 && y > 0 && isEmptyBlock(world.getBlock(x, y - 1, z))) {
                --y;
            }

            int j = getDistanceUntilEdge(x, y, z, rightDir) - 1;

            if (j >= 0) {
                bottomLeft = new ChunkCoordinates(x + j * Direction.offsetX[rightDir], y, z + j * Direction.offsetZ[rightDir]);
                width = getDistanceUntilEdge(bottomLeft.posX, bottomLeft.posY, bottomLeft.posZ, leftDir);

                if (width < 2 || width > 21) {
                    bottomLeft = null;
                    width = 0;
                }
            }

            if (bottomLeft != null) {
                height = calculatePortalHeight();
            }
        }

        protected int getDistanceUntilEdge(int x, int y, int z, int dir) {
            int j = Direction.offsetX[dir];
            int k = Direction.offsetZ[dir];
            int i;
            Block block;

            for (i = 0; i < 22; ++i) {
                block = world.getBlock(x + j * i, y, z + k * i);

                if (!isEmptyBlock(block)) {
                    break;
                }

                Block block1 = world.getBlock(x + j * i, y - 1, z + k * i);

                if (block1 != Blocks.obsidian) {
                    break;
                }
            }

            block = world.getBlock(x + j * i, y, z + k * i);
            return block == Blocks.obsidian ? i : 0;
        }

        protected int calculatePortalHeight() {
            int i;
            int j;
            int k;
            int l;
            label56:

            for (height = 0; height < 21; ++height) {
                i = bottomLeft.posY + height;

                for (j = 0; j < width; ++j) {
                    k = bottomLeft.posX + j * Direction.offsetX[BlockPortal.field_150001_a[axis][1]];
                    l = bottomLeft.posZ + j * Direction.offsetZ[BlockPortal.field_150001_a[axis][1]];
                    Block block = world.getBlock(k, i, l);

                    if (!isEmptyBlock(block)) {
                        break label56;
                    }

                    if (block == Blocks.portal) {
                        ++portalBlockCount;
                    }

                    if (j == 0) {
                        block = world.getBlock(k + Direction.offsetX[BlockPortal.field_150001_a[axis][0]], i, l + Direction.offsetZ[BlockPortal.field_150001_a[axis][0]]);

                        if (block != Blocks.obsidian) {
                            break label56;
                        }
                    } else if (j == width - 1) {
                        block = world.getBlock(k + Direction.offsetX[BlockPortal.field_150001_a[axis][1]], i, l + Direction.offsetZ[BlockPortal.field_150001_a[axis][1]]);

                        if (block != Blocks.obsidian) {
                            break label56;
                        }
                    }
                }
            }

            for (i = 0; i < width; ++i) {
                j = bottomLeft.posX + i * Direction.offsetX[BlockPortal.field_150001_a[axis][1]];
                k = bottomLeft.posY + height;
                l = bottomLeft.posZ + i * Direction.offsetZ[BlockPortal.field_150001_a[axis][1]];

                if (world.getBlock(j, k, l) != Blocks.obsidian) {
                    height = 0;
                    break;
                }
            }

            if (height <= 21 && height >= 3) {
                return height;
            } else {
                bottomLeft = null;
                width = 0;
                height = 0;
                return 0;
            }
        }

        protected boolean isEmptyBlock(Block block) {
            return block.getMaterial() == Material.air || block == Blocks.fire || block == Blocks.portal;
        }

        public boolean isValid() {
            return bottomLeft != null && width >= 2 && width <= 21 && height >= 3 && height <= 21;
        }

        public final boolean isBottomBlock(World world, int x, int y, int z) {
            return world.getBlock(x, y, z) == Blocks.bedrock;
        }

        public void placePortalBlocks() {
            int dirX = Direction.offsetX[leftDir], dirZ = Direction.offsetZ[leftDir];
            boolean f;

            System.out.println(world.getBlock(bottomLeft.posX - dirX, bottomLeft.posY - 1, bottomLeft.posZ - dirZ));
            System.out.println(world.getBlock(bottomLeft.posX - dirX * width - 1, bottomLeft.posY - 1, bottomLeft.posZ - dirZ * width - 1));
            System.out.println(world.getBlock(bottomLeft.posX - dirX, bottomLeft.posY + height, bottomLeft.posZ - dirZ));
            System.out.println(world.getBlock(bottomLeft.posX - dirX * width - 1, bottomLeft.posY + height, bottomLeft.posZ - dirZ * width - 1));

            if (bottomLeft.posY < 8)
                for (int i = -1; i < width + 1; ++i) {
                    int j = bottomLeft.posX + dirX * i;
                    int k = bottomLeft.posZ + dirZ * i;
                    if (isBottomBlock(world, j, bottomLeft.posY - 2, k)) {
                        f = true;
                        break;
                    }
                }

            for (int i = 0; i < width; ++i) {
                int j = bottomLeft.posX + Direction.offsetX[leftDir] * i;
                int k = bottomLeft.posZ + Direction.offsetZ[leftDir] * i;

                for (int l = 0; l < height; ++l) {
                    int i1 = bottomLeft.posY + l;
                    world.setBlock(j, i1, k, Blocks.portal, 0, 2);
                }
            }
        }
    }
}
