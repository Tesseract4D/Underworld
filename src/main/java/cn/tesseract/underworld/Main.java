package cn.tesseract.underworld;

import cn.tesseract.mycelium.asm.NodeTransformer;
import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import cn.tesseract.mycelium.asm.minecraft.HookLoader;
import cn.tesseract.underworld.biomes.BiomeGenUnderworld;
import cn.tesseract.underworld.blocks.BlockMantleOrCore;
import cn.tesseract.underworld.blocks.ItemBlockMantleOrCore;
import cn.tesseract.underworld.config.ConfigOreEntry;
import cn.tesseract.underworld.config.ConfigUnderWorld;
import cn.tesseract.underworld.world.WorldProviderUnderworld;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.util.ArrayList;

@Mod(modid = "underworld", acceptedMinecraftVersions = "[1.7.10]")
public class Main extends HookLoader {
    public static int underworld_y_offset = 120;
    public static Block mantleOrCore;
    public static ArrayList<ConfigOreEntry> oreEntries = new ArrayList<>();

    public static ConfigUnderWorld config = new ConfigUnderWorld();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        File oreConfigDir = new File(Launch.minecraftHome, "config/underworld");
        if (oreConfigDir.exists()) {
            File[] files = oreConfigDir.listFiles();
            if (files != null)
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".properties")) {
                        ConfigOreEntry entry = new ConfigOreEntry(file, "");
                        entry.read();
                        oreEntries.add(entry);
                    }
                }
        } else {
            oreConfigDir.mkdirs();
            String ores = """
                gravel,false,minecraft:gravel,0,32,0,255,minecraft:stone,30,true,false
                infested,false,minecraft:monster_egg,0,3,0,255,minecraft:stone,40,true,false
                oreIron,false,minecraft:iron_ore,0,6,0,255,minecraft:stone,48,false,true
                oreCoal,true,minecraft:coal_ore,0,16,0,255,minecraft:stone,40,false,true
                oreGold,false,minecraft:gold_ore,0,4,0,255,minecraft:stone,16,false,true
                oreRedstone,false,minecraft:redstone_ore,0,5,0,255,minecraft:stone,8,false,true
                oreDiamond,false,minecraft:diamond_ore,0,3,0,255,minecraft:stone,4,false,true
                oreLapis,false,minecraft:lapis_ore,0,3,0,255,minecraft:stone,4,false,true
                oreAdamatnium,false,*:*,0,3,0,127,minecraft:stone,1,true,true
                orePlatinum,false,*:*,0,3,0,127,minecraft:stone,1,true,true
                oreIridium,false,*:*,0,3,0,127,minecraft:stone,1,true,true
                oreDebris,false,*:*,0,3,0,127,minecraft:stone,1,true,true
                oreCopper,false,*:*,0,6,0,255,minecraft:stone,32,false,true
                oreTin,false,*:*,0,6,0,255,minecraft:stone,16,false,true
                oreLead,false,*:*,0,6,0,255,minecraft:stone,10,false,true
                oreNickel,false,*:*,0,4,0,255,minecraft:stone,4,false,true
                oreAluminum,false,*:*,0,6,0,255,minecraft:stone,16,false,true
                oreSilver,false,*:*,0,6,0,255,minecraft:stone,8,false,true
                oreMithril,false,*:*,0,3,0,255,minecraft:stone,8,false,true""";
            for (String line : ores.split("\n")) {
                String[] data = line.split(",");
                ConfigOreEntry entry = new ConfigOreEntry("underworld/" + data[0] + ".properties", data);
                entry.save(entry.toProperties());
                oreEntries.add(entry);
            }
        }
        config.read();
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

    @Override
    protected void registerHooks() {
        registerHookContainer("cn.tesseract.underworld.hook.UnderworldHook");
        registerNodeTransformer("net.minecraft.client.renderer.EntityRenderer", new NodeTransformer() {
            @Override
            public void transform(ClassNode node) {
                for (MethodNode method : node.methods) {
                    if (HookLibPlugin.getMethodMcpName(method.name).equals("updateFogColor"))
                        for (int i = 0; i < method.instructions.size(); i++) {
                            AbstractInsnNode insn = method.instructions.get(i);
                            if (insn instanceof MethodInsnNode minsn)
                                if (HookLibPlugin.getMethodMcpName(minsn.name).equals("getNightVisionBrightness")) {
                                    minsn.name = "getFogNightVisionBrightness";
                                }
                        }
                }
            }
        });
        registerNodeTransformer("net.minecraft.entity.Entity", new NodeTransformer() {
            @Override
            public void transform(ClassNode node) {
                for (MethodNode method : node.methods) {
                    if (HookLibPlugin.getMethodMcpName(method.name).equals("onEntityUpdate"))
                        for (int i = 0; i < method.instructions.size(); i++) {
                            AbstractInsnNode insn = method.instructions.get(i);
                            if (insn instanceof MethodInsnNode minsn)
                                if (HookLibPlugin.getMethodMcpName(minsn.name).equals("travelToDimension")) {
                                    minsn.name = "travelToDimensionUnderworld";
                                }
                        }
                }
            }
        });
    }
}
