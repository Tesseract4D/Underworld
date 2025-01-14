package cn.tesseract.underworld.util;

import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ChunkPostField {
    public static final int TYPE_FOG_DENSITY = 0;
    public static final int TYPE_UNDERWORLD_MYCELIUM = 1;
    private final Random random;
    private final int type;
    private final long hashed_type;
    private final long hashed_world_seed;
    private final int post_max_radius_of_effect;
    private final int post_max_radius_of_effect_sq;
    private final int field_chunk_range;
    private final int field_size_in_chunks;
    private final int field_num_chunks;
    private final float chance_of_post_per_chunk;
    private int last_post_field_generation_origin_chunk_x;
    private int last_post_field_generation_origin_chunk_z;
    private List post_list;

    public ChunkPostField(int type,long hashed_world_seed, int post_max_radius_of_effect, float chance_of_post_per_chunk) {
        this.random = new Random(type);
        this.type = type;
        this.random.nextInt();
        this.hashed_type = this.random.nextLong();
        this.hashed_world_seed = hashed_world_seed;
        this.post_max_radius_of_effect = post_max_radius_of_effect;
        this.post_max_radius_of_effect_sq = post_max_radius_of_effect * post_max_radius_of_effect;
        this.field_chunk_range = post_max_radius_of_effect < 1 ? 0 : (post_max_radius_of_effect - 1) / 16 + 1;
        this.field_size_in_chunks = this.field_chunk_range * 2 + 1;
        this.field_num_chunks = this.field_size_in_chunks * this.field_size_in_chunks;
        this.chance_of_post_per_chunk = chance_of_post_per_chunk;
    }

    private boolean doesPostFieldRequireRegeneration(int chunk_x, int chunk_z) {
        return this.post_list == null || this.last_post_field_generation_origin_chunk_x != chunk_x || this.last_post_field_generation_origin_chunk_z != chunk_z;
    }

    public ChunkPostField generate(int chunk_x, int chunk_z) {
        this.generateFieldIfRequired(chunk_x, chunk_z);
        return this;
    }

    public void generateFieldIfRequired(int chunk_x, int chunk_z) {
        if (this.doesPostFieldRequireRegeneration(chunk_x, chunk_z)) {
            this.last_post_field_generation_origin_chunk_x = chunk_x;
            this.last_post_field_generation_origin_chunk_z = chunk_z;
            if (this.post_list == null) {
                this.post_list = new ArrayList();
            } else {
                this.post_list.clear();
            }

            for (int chunk_dx = -this.field_chunk_range; chunk_dx <= this.field_chunk_range; ++chunk_dx) {
                for (int chunk_dz = -this.field_chunk_range; chunk_dz <= this.field_chunk_range; ++chunk_dz) {
                    chunk_x = this.last_post_field_generation_origin_chunk_x + chunk_dx;
                    chunk_z = this.last_post_field_generation_origin_chunk_z + chunk_dz;
                    long seed = (long) getIntPairHash(chunk_x, chunk_z) * this.hashed_type * this.hashed_world_seed;
                    this.random.setSeed(seed);
                    this.random.nextInt();
                    if (this.random.nextFloat() < this.chance_of_post_per_chunk) {
                        this.post_list.add(new ChunkPost(chunk_x, chunk_z, this.random.nextInt(16), this.random.nextInt(16), seed));
                    }
                }
            }

        }
    }

    public int getPostMaxRadiusOfEffect() {
        return this.post_max_radius_of_effect;
    }

    public int getPostMaxRadiusOfEffectSq() {
        return this.post_max_radius_of_effect_sq;
    }

    public List getNearbyPosts(int chunk_x, int chunk_z) {
        this.generateFieldIfRequired(chunk_x, chunk_z);
        return this.post_list;
    }

    public List getNearbyPostsForBlockCoords(int x, int z) {
        return this.getNearbyPosts(x >> 4, z >> 4);
    }

    public ChunkPost getNearestPostTo(double pos_x, double pos_z) {
        List posts = this.getNearbyPosts(MathHelper.floor_double(pos_x) >> 4, MathHelper.floor_double(pos_z) >> 4);
        ChunkPost nearest_post = null;
        double shortest_distance_sq_to_post = 0.0;
        Iterator i = posts.iterator();

        while (true) {
            ChunkPost post;
            double distance_sq;
            do {
                do {
                    if (!i.hasNext()) {
                        return nearest_post;
                    }

                    post = (ChunkPost) i.next();
                    distance_sq = post.getDistanceSqFromPosXZ(pos_x, pos_z);
                } while (distance_sq > (double) this.post_max_radius_of_effect_sq);
            } while (nearest_post != null && !(distance_sq < shortest_distance_sq_to_post));

            nearest_post = post;
            shortest_distance_sq_to_post = distance_sq;
        }
    }

    public double getDistanceToNearestPost(double pos_x, double pos_z) {
        ChunkPost nearest_post = this.getNearestPostTo(pos_x, pos_z);
        return nearest_post == null ? -1.0 : nearest_post.getDistanceFromPosXZ(pos_x, pos_z);
    }

    public static int getIntPairHash(int a, int b) {
        int hash = 17;
        hash = hash * 31 + a;
        hash = hash * 31 + b;
        return hash;
    }
}
