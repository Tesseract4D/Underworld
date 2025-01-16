package cn.tesseract.underworld.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockMantleOrCore extends Block {
    private static final String[] types = {"mantle", "core"};

    private IIcon[] textures;

    public BlockMantleOrCore() {
        super(Material.rock);
        setBlockUnbreakable();
        setResistance(6000000);
        setLightLevel(0.9f);
        setCreativeTab(CreativeTabs.tabBlock);
        setStepSound(Block.soundTypeStone);
        setTickRandomly(true);
    }

    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textures = new IIcon[types.length];
        for (int i = 0; i < types.length; i++)
            this.textures[i] = iconRegister.registerIcon("underworld:" + types[i]);
    }

    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta >= this.textures.length)
            meta = 0;
        return this.textures[meta];
    }

    public void getSubBlocks(Item itemIn, CreativeTabs creativeTabs, List<ItemStack> list) {
        for (int i = 0; i < types.length; i++)
            list.add(new ItemStack(itemIn, 1, i));
    }

    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public void updateTick(World world, int x, int y, int z, Random rand) {
        int n = rand.nextInt(3);
        Block block;
        for (int i = 0; i < n; ++i) {
            x += rand.nextInt(3) - 1;
            ++y;
            z += rand.nextInt(3) - 1;
            block = world.getBlock(x, y, z);
            if (block == Blocks.air) {
                if (world.getBlock(x - 1, y, z).getMaterial().getCanBurn() || world.getBlock(x + 1, y, z).getMaterial().getCanBurn() || world.getBlock(x, y - 1, z).getMaterial().getCanBurn() || world.getBlock(x, y + 1, z).getMaterial().getCanBurn() || world.getBlock(x, y, z - 1).getMaterial().getCanBurn() || world.getBlock(x, y, z + 1).getMaterial().getCanBurn()) {
                    world.setBlock(x, y, z, Blocks.fire);
                    return;
                }
            } else if (block.getMaterial().isSolid()) {
                return;
            }
        }

        if (n == 0) {
            for (int i = 0; i < 3; ++i) {
                x += rand.nextInt(3) - 1;
                z += rand.nextInt(3) - 1;
                if (world.isAirBlock(x, y + 1, z) && world.getBlock(x, y, z).getMaterial().getCanBurn()) {
                    world.setBlock(x, y + 1, z, Blocks.fire);
                }
            }
        }
    }
}
