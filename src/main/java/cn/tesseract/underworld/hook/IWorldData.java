package cn.tesseract.underworld.hook;

import cn.tesseract.underworld.util.ChunkPostField;
import cn.tesseract.underworld.util.RNG;

public interface IWorldData {
    ChunkPostField get_mycelium_posts();

    void set_mycelium_posts(ChunkPostField field);

    RNG get_rng();

    void set_rng(RNG rng);
}
