package mods.tesseract.underworld.fix;

import mods.tesseract.underworld.util.RNG;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

import java.util.Random;

public class FixesUnderworld {
    @Fix(targetMethod = "<init>", insertOnExit = true)
    public static void World(World c, ISaveHandler p_i45369_1_, String p_i45369_2_, WorldSettings p_i45369_3_, WorldProvider p_i45369_4_, Profiler p_i45369_5_) {
        System.out.println("&" + c.getSeed());
        Random rand = new Random(c.getSeed());
        rand.nextInt();
        ((IWorld) c).mycelium_posts.setHashedWorldSeed(rand.nextLong());
        RNG.init(c);
    }

    @Fix(targetMethod = "<init>", insertOnExit = true)
    public static void World(World c, ISaveHandler p_i45368_1_, String p_i45368_2_, WorldProvider p_i45368_3_, WorldSettings p_i45368_4_, Profiler p_i45368_5_) {
        //World(c, p_i45368_1_, p_i45368_2_, p_i45368_4_, p_i45368_3_, p_i45368_5_);
    }
}
