package mods.tesseract.underworld.fix;

import mods.tesseract.underworld.util.ChunkPostField;
import mods.tesseract.underworld.util.RNG;

public interface IWorld {
    ChunkPostField mycelium_posts=new ChunkPostField(1,  24, 0.0625F);
    RNG rng = new RNG();
}
