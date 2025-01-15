package cn.tesseract.underworld.world;


import cn.tesseract.underworld.biome.BiomeGenUnderworld;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderUnderworld extends WorldProvider {

    public void registerWorldChunkManager() {
        this.hasNoSky = true;
        this.dimensionId = -2;
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenUnderworld.biome, 0.0F);
    }

    @Override
    public String getDimensionName() {
        return "Underworld";
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float par1, float par2) {
        int day_of_cycle = (int) ((worldObj.getWorldTime() / 24000L) % 32);
        int distance_from_peak = Math.abs(day_of_cycle - 16);
        float grayscale = distance_from_peak * distance_from_peak * distance_from_peak / 64000f;
        return Vec3.createVectorHelper(grayscale, grayscale, grayscale);
    }

    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderUnderworld(this.worldObj, this.worldObj.getSeed());
    }

    public boolean canCoordinateBeSpawn(int par1, int par2) {
        return false;
    }

    public float calculateCelestialAngle(long par1, float par3) {
        return 0.5F;
    }

    public boolean canRespawnHere() {
        return false;
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    public int getActualHeight() {
        return 256;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 8.0F;
    }

    public boolean doesXZShowFog(int x, int z) {
        return true;
    }
}
