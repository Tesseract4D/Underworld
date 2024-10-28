package mods.tesseract.underworld;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.tesseract.underworld.biomes.BiomeGenUnderworld;
import mods.tesseract.underworld.blocks.BlockMantleOrCore;
import mods.tesseract.underworld.blocks.ItemBlockMantleOrCore;
import mods.tesseract.underworld.config.Config;
import mods.tesseract.underworld.config.ConfigUnderworld;
import mods.tesseract.underworld.config.IConfigCSV;
import mods.tesseract.underworld.config.IConfigProperties;
import mods.tesseract.underworld.fix.ReplaceMethodVisitor;
import mods.tesseract.underworld.world.WorldGenMinableUnderworld;
import mods.tesseract.underworld.world.WorldProviderUnderworld;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

import java.util.ArrayList;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends CustomLoadingPlugin {
    public static int underworld_y_offset = 120;
    public static Block mantleOrCore;
    public static Config oreEntries;

    public static Config config = new Config("Main.properties", IConfigProperties.toProperties(ConfigUnderworld.class));
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        config.read();
        IConfigProperties.loadProperties(config.config, ConfigUnderworld.class);
        config.config = IConfigProperties.toProperties(ConfigUnderworld.class);
        config.save();
        BiomeGenUnderworld.biome = (new BiomeGenUnderworld(26)).setColor(16711680).setBiomeName("Underworld").setDisableRain().setTemperatureRainfall(1.0F, 0.0F);
        mantleOrCore = registerBlock(new BlockMantleOrCore().setBlockName("mantleOrCore"), ItemBlockMantleOrCore.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        DimensionManager.registerProviderType(-2, WorldProviderUnderworld.class, false);
        DimensionManager.registerDimension(-2, -2);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        String s = "";
        ArrayList<ItemStack> od = OreDictionary.getOres("oreCopper");
        if (!od.isEmpty())
            s += "oreCopper," + od.get(0).getUnlocalizedName() + "," + od.get(0).getItemDamage() + ",6,0,255,minecraft:stone,32,true\n";
        oreEntries = new Config("OreEntries.csv", s + """
            oreDict,block,blockMeta,veinSize,minY,maxY,blockToReplace,frequency,sizeIncreasesWithDepth
            gravel,minecraft:gravel,0,32,0,255,minecraft:stone,30,false
            infested,minecraft:monster_egg,0,3,0,255,minecraft:stone,40,false
            oreIron,minecraft:iron_ore,0,6,0,255,minecraft:stone,48,true
            oreGold,minecraft:gold_ore,0,4,0,255,minecraft:stone,16,true
            #oreSilver,modid:blockid,0,6,0,255,minecraft:stone,8,true
            #oreMithril,modid:blockid,0,3,0,255,minecraft:stone,8,true
            oreRedstone,minecraft:redstone_ore,0,5,0,255,minecraft:stone,8,true
            oreDiamond,minecraft:diamond_ore,0,3,0,255,minecraft:stone,4,true
            oreLapis,minecraft:lapis_ore,0,3,0,255,minecraft:stone,4,true""");
        oreEntries.read();
        ArrayList<IConfigCSV> ores;
        try {
            ores = IConfigCSV.parseCSV(Main.oreEntries.config, WorldGenMinableUnderworld.class);
        } catch (IllegalArgumentException f) {
            Main.oreEntries.reset();
            ores = IConfigCSV.parseCSV(Main.oreEntries.defaultConfig, WorldGenMinableUnderworld.class);
        }
        BiomeGenUnderworld.decorator.oreGens = ores.toArray(new WorldGenMinableUnderworld[0]);
        for (WorldGenMinableUnderworld g : BiomeGenUnderworld.decorator.oreGens) {
            if (!g.oreDict.equals("gravel"))
                g.frequency = (int) (g.frequency * ConfigUnderworld.ore_mutiplier);
        }
        System.out.println(7777);
        System.out.println(BiomeGenUnderworld.decorator.oreGens[0].minableBlock.getUnlocalizedName());
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
