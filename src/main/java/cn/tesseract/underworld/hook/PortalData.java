package cn.tesseract.underworld.hook;

public interface PortalData {
    int get_portalType();

    void set_portalType(int type);

    int[] get_lastPortal();

    void set_lastPortal(int[] pos);
}
