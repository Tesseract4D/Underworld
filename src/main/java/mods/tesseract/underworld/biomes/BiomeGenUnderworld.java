package mods.tesseract.underworld.biomes;

import mods.tesseract.underworld.fix.IWorld;
import mods.tesseract.underworld.util.ChunkPost;
import mods.tesseract.underworld.util.ChunkPostField;
import net.minecraft.block.Block;
import net.minecraft.block.BlockMushroom;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.List;
import java.util.Random;

public class BiomeGenUnderworld extends BiomeGenBase {
    public BiomeGenUnderworld(int id) {
        super(id);
        this.spawnableMonsterList.add(new SpawnListEntry(EntityCaveSpider.class, 40, 1, 2));
    }

    public void decorate(World world, Random random, int chunk_origin_x, int chunk_origin_z) {
        this.placeMycelium(world, chunk_origin_x, chunk_origin_z);
        super.decorate(world, random, chunk_origin_x, chunk_origin_z);
    }

    private void placeMycelium(World world, int chunk_origin_x, int chunk_origin_z) {
        ChunkPostField mycelium_posts = ((IWorld) world).mycelium_posts;
        Random random = new Random();

        for (int x = chunk_origin_x; x < chunk_origin_x + 16; ++x) {
            for (int z = chunk_origin_z; z < chunk_origin_z + 16; ++z) {
                List posts = mycelium_posts.getNearbyPostsForBlockCoords(x, z);

                for (int i = 0; i < posts.size(); ++i) {
                    ChunkPost post = (ChunkPost) posts.get(i);
                    if (!(post.getDistanceSqFromBlockCoords(x, z) > (double) (mycelium_posts.getPostMaxRadiusOfEffectSq() + 4))) {
                        random.setSeed(post.getSeed());
                        random.nextInt();
                        int y = random.nextInt(random.nextBoolean() ? 16 : 72) + 24;
                        //y += world.underworld_y_offset;
                        y += 120;
                        int height = random.nextInt(5) + 1;

                        for (int dy = 0; dy < height; ++dy) {
                            if (world.isAirBlock(x, y + 1, z)) {
                                Block block = world.getBlock(x, y, z);
                                if (block != null && block.isNormalCube() && block.isOpaqueCube() && !(block instanceof BlockMushroom)) {
                                    block = world.getBlock(x, y - 1, z);
                                    if (block != null && block.isNormalCube() && block.isOpaqueCube() && world.setBlock(x, y, z, Blocks.mycelium, 0, 2)) {
                                        //world.getChunkFromBlockCoords(x, z).setHadNaturallyOccurringMycelium();
                                        random.setSeed(post.getSeed() + (long) ChunkPostField.getIntPairHash(x, z));
                                        random.nextInt();
                                        if (random.nextInt(16) == 0 && !this.theBiomeDecorator.bigMushroomGen.generate(world, random, x, y + 1, z)) {
                                            world.setBlock(x, y + 1, z, Blocks.brown_mushroom, 0, 2);
                                        }
                                    }
                                }
                                break;
                            }

                            ++y;
                        }
                    }
                }
            }
        }

    }
}
