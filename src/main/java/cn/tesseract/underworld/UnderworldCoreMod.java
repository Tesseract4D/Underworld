package cn.tesseract.underworld;

import cn.tesseract.mycelium.asm.Accessor;
import cn.tesseract.mycelium.asm.NodeTransformer;
import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import cn.tesseract.mycelium.asm.minecraft.HookLoader;
import cn.tesseract.underworld.hook.PortalData;
import cn.tesseract.underworld.hook.WorldData;
import cn.tesseract.underworld.hook.Replace;
import cn.tesseract.underworld.hook.UnderworldHook;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class UnderworldCoreMod extends HookLoader {
    @Override
    protected void registerHooks() {
        //MyceliumCoreMod.dumpTransformedClass = true;
        registerHookContainer(UnderworldHook.class.getName());
        registerNodeTransformer("net.minecraft.block.Block", node -> {
            for (MethodNode method : node.methods) {
                if (HookLibPlugin.getMethodMcpName(method.name).equals("registerBlocks"))
                    for (int i = 0; i < method.instructions.size(); i++) {
                        AbstractInsnNode insn = method.instructions.get(i);
                        if (insn instanceof MethodInsnNode minsn) {
                            if (minsn.name.equals("<init>") && minsn.owner.equals("net/minecraft/block/BlockPortal")) {
                                minsn.owner = ((TypeInsnNode) method.instructions.get(i - 2)).desc = "cn/tesseract/underworld/block/BlockPortalUnderworld";
                                break;
                            }
                        }
                    }
            }
        });

        registerNodeTransformer("net.minecraft.entity.Entity", new Accessor(PortalData.class.getName()));
        registerNodeTransformer("net.minecraft.world.World", new Accessor(WorldData.class.getName()));

        registerNodeTransformer("net.minecraft.client.renderer.EntityRenderer", new Replace("updateFogColor", "getNightVisionBrightness", "getFogNightVisionBrightness"));
        registerNodeTransformer("net.minecraft.client.gui.GuiIngame", new Replace("func_130015_b", "getBlockTextureFromSide", "getOverlayIcon"));
        registerNodeTransformer("net.minecraft.world.Teleporter", new Replace("makePortal", "setBlock", "setPortalBlock"));

        NodeTransformer transformer = new Replace("onEntityUpdate", "travelToDimension", "travelToDimensionUnderworld");
        registerNodeTransformer("net.minecraft.entity.Entity", transformer);
        registerNodeTransformer("net.minecraft.entity.item.EntityMinecart", transformer);
    }
}
