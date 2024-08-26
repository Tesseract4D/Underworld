package mods.tesseract.underworld.world;


import mods.tesseract.underworld.biomes.BiomeGenUnderworld;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderUnderworld extends WorldProvider {

    public void registerWorldChunkManager() {
        //this.isHellWorld = true;
        this.hasNoSky = true;
        this.dimensionId = -2;
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenUnderworld.biome, 0.0F);
    }

    @Override
    public String getDimensionName() {
        return "Underworld";
    }

    public Vec3 getFogColor(float par1, float par2) {
        int day_of_cycle = (int) ((this.worldObj.getTotalWorldTime() / 24000L) % 32);
        int distance_from_peak = Math.abs(day_of_cycle - 16);
        float grayscale = (float) distance_from_peak * 0.004F;
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

    public boolean doesXZShowFog(int x, int y, int z) {
        return false;
    }
}
