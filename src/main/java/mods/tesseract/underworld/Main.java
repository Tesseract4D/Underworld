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
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends CustomLoadingPlugin {
    public static int underworld_y_offset = 120;
    public static Block mantleOrCore;
    public static Config oreEntries;

    public static Config config = new Config("Main.properties", IConfigProperties.toProperties(ConfigUnderworld.class));
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        Main.oreEntries = new Config("OreEntries.csv", """
            #oreDict，矿物词典，当找不到对应的方块时将选取该矿物词典中第一个方块来生成
            #block，要生成的方块
            #blockMeta，要生成的方块的元数据值
            #veinSize，矿脉最大尺寸
            #minY，生成的最低高度
            #maxY，生成的最高高度
            #blockToReplace，要替换的方块，一般为石头
            #frequency，一个区块尝试生成的次数，越大生成得越多
            #uniformDistribution，是否平均分布，否则75%的矿脉会生成在最低高度到最高高度之间的下半部分
            #sizeIncreasesWithDepth，矿脉尺寸是否随深度而增加

            oreDict,block,blockMeta,veinSize,minY,maxY,blockToReplace,frequency,uniformDistribution,sizeIncreasesWithDepth
            gravel,minecraft:gravel,0,32,0,255,minecraft:stone,30,true,false
            infested,minecraft:monster_egg,0,3,0,255,minecraft:stone,40,true,false
            oreIron,minecraft:iron_ore,0,6,0,255,minecraft:stone,48,false,true
            #默认不生成煤矿
            #oreCoal,minecraft:coal_ore,0,16,0,255,minecraft:stone,40,false,true
            oreGold,minecraft:gold_ore,0,4,0,255,minecraft:stone,16,false,true
            oreRedstone,minecraft:redstone_ore,0,5,0,255,minecraft:stone,8,false,true
            oreDiamond,minecraft:diamond_ore,0,3,0,255,minecraft:stone,4,false,true
            oreLapis,minecraft:lapis_ore,0,3,0,255,minecraft:stone,4,false,true
            #如果找不到对应的方块，将会尝试从矿物词典中查找方块，如果仍没有则该条将被忽略
            oreAdamatnium,modid:blockid,0,3,0,127,minecraft:stone,1,true,true
            orePlatinum,modid:blockid,0,3,0,127,minecraft:stone,1,true,true
            oreIridium,modid:blockid,0,3,0,127,minecraft:stone,1,true,true
            oreDebris,modid:blockid,0,3,0,127,minecraft:stone,1,true,true
            oreCopper,modid:blockid,0,6,0,255,minecraft:stone,32,false,true
            oreTin,modid:blockid,0,6,0,255,minecraft:stone,16,false,true
            oreLead,modid:blockid,0,6,0,255,minecraft:stone,10,false,true
            oreNickel,modid:blockid,0,4,0,255,minecraft:stone,4,false,true
            oreAluminum,modid:blockid,0,6,0,255,minecraft:stone,16,false,true
            oreSilver,modid:blockid,0,6,0,255,minecraft:stone,8,false,true
            oreMithril,modid:blockid,0,3,0,255,minecraft:stone,8,false,true""");
        Main.oreEntries.read();
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
