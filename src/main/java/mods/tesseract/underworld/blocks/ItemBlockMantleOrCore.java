package mods.tesseract.underworld.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMantleOrCore extends ItemBlock {
    private static final String[] types =  {"mantle", "core"};

    public ItemBlockMantleOrCore(Block b) {
        super(b);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMetadata(int meta) {
        return meta & 0xF;
    }

    public String getUnlocalizedName(ItemStack itemstack) {
        int meta = itemstack.getItemDamage();
        if (meta < 0 || meta >= types.length)
            meta = 0;
        return getUnlocalizedName() + "." + types[meta];
    }
}
