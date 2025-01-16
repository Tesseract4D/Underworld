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

    public static String getMagicName(int metadata) {
        return magic_names[metadata];
    }
}
