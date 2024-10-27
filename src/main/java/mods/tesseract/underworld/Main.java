package mods.tesseract.underworld;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.tesseract.underworld.biomes.BiomeGenUnderworld;
import mods.tesseract.underworld.blocks.BlockMantleOrCore;
import mods.tesseract.underworld.blocks.ItemBlockMantleOrCore;
import mods.tesseract.underworld.config.Config;
import mods.tesseract.underworld.config.ConfigUnderworld;
import mods.tesseract.underworld.config.IConfigProperties;
import mods.tesseract.underworld.fix.ReplaceMethodVisitor;
import mods.tesseract.underworld.world.WorldProviderUnderworld;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;
import net.tclproject.mysteriumlib.asm.core.MetaReader;
import net.tclproject.mysteriumlib.asm.core.MiscUtils;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends CustomLoadingPlugin {
    public static int underworld_y_offset = 120;
    public static Block mantleOrCore;
    public static Config oreEntries = new Config("OreEntries.csv", """
        oreDict,block,blockMeta,veinSize,minY,maxY,blockToReplace,frequency,sizeIncreasesWithDepth
        gravel,minecraft:gravel,0,32,0,255,minecraft:stone,300,false
        infested,minecraft:monster_egg,0,3,0,255,minecraft:stone,400,false
        oreIron,minecraft:iron_ore,0,6,0,255,minecraft:stone,480,true
        #oreCopper,modid:blockid,0,6,0,255,minecraft:stone,320,true
        oreGold,minecraft:gold_ore,0,4,0,255,minecraft:stone,160,true
        #oreSilver,modid:blockid,0,6,0,255,minecraft:stone,80,true
        #oreMithril,modid:blockid,0,3,0,255,minecraft:stone,80,true
        oreRedstone,minecraft:redstone_ore,0,5,0,255,minecraft:stone,80,true
        oreDiamond,minecraft:diamond_ore,0,3,0,255,minecraft:stone,40,true
        oreLapis,minecraft:lapis_ore,0,3,0,255,minecraft:stone,40,true""");

    public static Config config = new Config("Main.properties", IConfigProperties.toProperties(ConfigUnderworld.class));
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        oreEntries.read();
        config.read();
        IConfigProperties.loadProperties(config.config, ConfigUnderworld.class);
        config.config = IConfigProperties.toProperties(ConfigUnderworld.class);
        config.save();
        try {
            System.out.println("&" + new MetaReader().getLocalVariables(EntityRenderer.class.getDeclaredMethod("updateFogColor", float.class)));
            System.out.println("&" + MiscUtils.getMemberInfo(EntityRenderer.class.getDeclaredMethod("getNightVisionBrightness", EntityPlayer.class, float.class)));
        } catch (Exception ex) {
        }
        System.out.println(config.defaultConfig);
        BiomeGenUnderworld.biome = (new BiomeGenUnderworld(26)).setColor(16711680).setBiomeName("Underworld").setDisableRain().setTemperatureRainfall(1.0F, 0.0F);
        mantleOrCore = registerBlock(new BlockMantleOrCore().setBlockName("mantleOrCore"), ItemBlockMantleOrCore.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DimensionManager.registerProviderType(-2, WorldProviderUnderworld.class, false);
        DimensionManager.registerDimension(-2, -2);
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

    public String[] getASMTransformerClass() {
        return new String[]{FirstClassTransformer.class.getName()};
    }

    public void registerFixes() {
        registerClassWithFixes("mods.tesseract.underworld.fix.FixesUnderworld");
        registerImplementation("net.minecraft.world.World","mods.tesseract.underworld.fix.IWorld");
        registerClassVisitor("net.minecraft.client.renderer.EntityRenderer", new ReplaceMethodVisitor("updateFogColor", "getNightVisionBrightness", "getFogNightVisionBrightness"));
    }
}
