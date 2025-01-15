package cn.tesseract.underworld.hook;

import net.minecraft.util.ChunkCoordinates;

public interface IPortalData {
    int get_portalType();

    void set_portalType(int type);

    ChunkCoordinates get_lastPortalPos();

    void set_lastPortalPos(ChunkCoordinates pos);
}
