package cn.tesseract.underworld;

import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import cn.tesseract.underworld.biome.BiomeGenUnderworld;
import cn.tesseract.underworld.block.BlockMantleOrCore;
import cn.tesseract.underworld.block.BlockRunestone;
import cn.tesseract.underworld.block.ItemBlockMantleOrCore;
import cn.tesseract.underworld.config.ConfigOreEntries;
import cn.tesseract.underworld.config.ConfigUnderWorld;
import cn.tesseract.underworld.config.OreEntries;
import cn.tesseract.underworld.world.WorldProviderUnderworld;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Underworld {
    public static final int underworld_y_offset = 120;
    public static Block bedrockLayerBlock;
    public static Block mantleOrCore;
    public static BlockRunestone runestoneMithril;
    public static BlockRunestone runestoneAdamantium;
    public static final ConfigUnderWorld config = new ConfigUnderWorld();
    public static ConfigOreEntries ores = new ConfigOreEntries("underworld_ores", OreEntries.class);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) throws IOException {
        config.read();

        if (!ores.file.exists())
            FileUtils.copyInputStreamToFile(HookLibPlugin.class.getResourceAsStream("/assets/underworld/underworld_ores.json"), ores.file);
        ores.read();

        String[] block = config.bedrock_layer_block.split(":", 2);
        if ((bedrockLayerBlock = GameRegistry.findBlock(block[0], block[1])) == null)
            bedrockLayerBlock = Blocks.bedrock;

        BiomeGenUnderworld.biome = (new BiomeGenUnderworld(26)).setColor(16711680).setBiomeName("Underworld").setDisableRain().setTemperatureRainfall(1.0F, 0.0F);
        mantleOrCore = registerBlock(new BlockMantleOrCore().setBlockName("mantleOrCore"), ItemBlockMantleOrCore.class);
        runestoneMithril = (BlockRunestone) registerBlock(new BlockRunestone("mithril").setHardness(2.4F).setResistance(20.0F).setStepSound(Block.soundTypeStone).setBlockName("runestone_mithril").setBlockTextureName("obsidian"));
        runestoneAdamantium = (BlockRunestone) registerBlock(new BlockRunestone("adamantium").setHardness(2.4F).setResistance(20.0F).setStepSound(Block.soundTypeStone).setBlockName("runestone_adamantium").setBlockTextureName("obsidian"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DimensionManager.registerProviderType(-2, WorldProviderUnderworld.class, false);
        DimensionManager.registerDimension(-2, -2);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (entity.worldObj == null) return;
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;
        if (!entity.isImmuneToFire() && !entity.isSneaking() && entity.onGround && entity.worldObj.getBlock(MathHelper.floor_double(x), (int) (y - .45D), MathHelper.floor_double(z)) == mantleOrCore) {
            entity.attackEntityFrom(DamageSource.lava, 2);
        }
    }

    public static Block registerBlock(Block block, Class<? extends ItemBlock> itemBlock) {
        return GameRegistry.registerBlock(block, itemBlock, block.getUnlocalizedName().replace("tile.", ""));
    }

    public static Block registerBlock(Block block) {
        return GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
    }
}
