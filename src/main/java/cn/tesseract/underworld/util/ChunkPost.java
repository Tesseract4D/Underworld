package cn.tesseract.underworld.util;

import net.minecraft.util.MathHelper;

public final class ChunkPost {
    private final int chunk_x;
    private final int chunk_z;
    private final int chunk_base_x;
    private final int chunk_base_z;
    private final int local_x;
    private final int local_z;
    private final int x;
    private final int z;
    private final double pos_x;
    private final double pos_z;
    private final long seed;

    public ChunkPost(int chunk_x, int chunk_z, int local_x, int local_z, long seed) {
        this.chunk_x = chunk_x;
        this.chunk_z = chunk_z;
        this.chunk_base_x = chunk_x * 16;
        this.chunk_base_z = chunk_z * 16;
        this.local_x = local_x;
        this.local_z = local_z;
        this.x = this.chunk_base_x + local_x;
        this.z = this.chunk_base_z + local_z;
        this.pos_x = (double) this.x + 0.5;
        this.pos_z = (double) this.z + 0.5;
        this.seed = seed;
    }

    public int getChunkX() {
        return this.chunk_x;
    }

    public int getChunkZ() {
        return this.chunk_z;
    }

    public int getNonLocalBlockPosX() {
        return this.x;
    }

    public int getNonLocalBlockPosZ() {
        return this.z;
    }

    public double getNonLocalPosX() {
        return this.pos_x;
    }

    public double getNonLocalPosZ() {
        return this.pos_z;
    }

    public long getSeed() {
        return this.seed;
    }

    public double getDistanceSqFromBlockCoords(int x, int z) {
        return getDistanceSqFromDeltas(this.x - x, this.z - z);
    }

    public double getDistanceSqFromPosXZ(double pos_x, double pos_z) {
        return getDistanceSqFromDeltas(this.pos_x - pos_x, this.pos_z - pos_z);
    }

    public double getDistanceFromBlockCoords(int x, int z) {
        return getDistanceFromDeltas(this.x - x, this.z - z);
    }

    public double getDistanceFromPosXZ(double pos_x, double pos_z) {
        return getDistanceFromDeltas(this.pos_x - pos_x, this.pos_z - pos_z);
    }

    public String toString() {
        return "[" + this.chunk_x + "," + this.chunk_z + "]";
    }

    public static float getDistanceFromDeltas(double dx, double dy, double dz) {
        return MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    }

    public static double getDistanceSqFromDeltas(double dx, double dy, double dz) {
        return dx * dx + dy * dy + dz * dz;
    }

    public static double getDistanceSqFromDeltas(float dx, float dy, float dz) {
        return dx * dx + dy * dy + dz * dz;
    }

    public static double getDistanceSqFromDeltas(double dx, double dz) {
        return dx * dx + dz * dz;
    }

    public static double getDistanceFromDeltas(double dx, double dz) {
        return MathHelper.sqrt_double(getDistanceSqFromDeltas(dx, dz));
    }
}
