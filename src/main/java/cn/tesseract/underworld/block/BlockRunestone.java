package cn.tesseract.underworld.block;

import net.minecraft.block.BlockObsidian;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockRunestone extends BlockObsidian {
    private static final String[] magic_names = new String[]{"Nul", "Quas", "Por", "An", "Nox", "Flam", "Vas", "Des", "Ort", "Tym", "Corp", "Lor", "Mani", "Jux", "Ylem", "Sanct"};
    protected final IIcon[] IIconArray = new IIcon[16];
    public final String type;

    public BlockRunestone(String rune_metal) {
        super();
        this.type = rune_metal;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int metadata) {
        return side != 0 && side != 1 ? this.IIconArray[metadata] : this.blockIcon;
    }

    public void registerBlockIcons(IIconRegister par1IIconRegister) {
        super.registerBlockIcons(par1IIconRegister);

        for (int i = 0; i < this.IIconArray.length; ++i) {
            this.IIconArray[i] = par1IIconRegister.registerIcon("underworld:runestones/" + this.type + "/" + i);
        }
    }

    public void scheduleUpdatesForNearbyPortalBlocks(World world, int x, int y, int z) {
        int check_x;
        int check_y;
        if (world.getBlock(check_x = x - 1, check_y = y + 1, z) == Blocks.portal) {
            world.scheduleBlockUpdate(check_x, check_y, z, Blocks.portal, 1);
        }

        if (world.getBlock(check_x = x + 1, check_y = y + 1, z) == Blocks.portal) {
            world.scheduleBlockUpdate(check_x, check_y, z, Blocks.portal, 1);
        }

        if (world.getBlock(check_x = x - 1, check_y = y - 1, z) == Blocks.portal) {
            world.scheduleBlockUpdate(check_x, check_y, z, Blocks.portal, 1);
        }

        if (world.getBlock(check_x = x + 1, check_y = y - 1, z) == Blocks.portal) {
            world.scheduleBlockUpdate(check_x, check_y, z, Blocks.portal, 1);
        }

        int check_z;
        if (world.getBlock(x, check_y = y + 1, check_z = z - 1) == Blocks.portal) {
            world.scheduleBlockUpdate(x, check_y, check_z, Blocks.portal, 1);
        }

        if (world.getBlock(x, check_y = y + 1, check_z = z + 1) == Blocks.portal) {
            world.scheduleBlockUpdate(x, check_y, check_z, Blocks.portal, 1);
        }

        if (world.getBlock(x, check_y = y - 1, check_z = z - 1) == Blocks.portal) {
            world.scheduleBlockUpdate(x, check_y, check_z, Blocks.portal, 1);
        }

        if (world.getBlock(x, check_y = y - 1, check_z = z + 1) == Blocks.portal) {
            world.scheduleBlockUpdate(x, check_y, check_z, Blocks.portal, 1);
        }

    }

    public void onBlockAdded(World world, int x, int y, int z) {
        this.scheduleUpdatesForNearbyPortalBlocks(world, x, y, z);
    }

    public void onBlockPreDestroy(World world, int x, int y, int z, int metadata) {
        this.scheduleUpdatesForNearbyPortalBlocks(world, x, y, z);
    }

    public static String getMagicName(int metadata) {
        return magic_names[metadata];
    }
}
