package cn.tesseract.underworld;

import cn.tesseract.mycelium.asm.NodeTransformer;
import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import cn.tesseract.mycelium.asm.minecraft.HookLoader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class UnderworldCoreMod  extends HookLoader {
    @Override
    protected void registerHooks() {
        registerHookContainer("cn.tesseract.underworld.hook.UnderworldHook");
        registerNodeTransformer("net.minecraft.client.renderer.EntityRenderer", node -> {
            for (MethodNode method : node.methods) {
                if (HookLibPlugin.getMethodMcpName(method.name).equals("updateFogColor"))
                    for (int i = 0; i < method.instructions.size(); i++) {
                        AbstractInsnNode insn = method.instructions.get(i);
                        if (insn instanceof MethodInsnNode minsn)
                            if (HookLibPlugin.getMethodMcpName(minsn.name).equals("getNightVisionBrightness")) {
                                minsn.name = "getFogNightVisionBrightness";
                            }
                    }
            }
        });

        NodeTransformer transformer = node -> {
            for (MethodNode method : node.methods) {
                if (HookLibPlugin.getMethodMcpName(method.name).equals("onEntityUpdate"))
                    for (int i = 0; i < method.instructions.size(); i++) {
                        AbstractInsnNode insn = method.instructions.get(i);
                        if (insn instanceof MethodInsnNode minsn)
                            if (HookLibPlugin.getMethodMcpName(minsn.name).equals("travelToDimension")) {
                                minsn.name = "travelToDimensionUnderworld";
                            }
                    }
            }
        };
        registerNodeTransformer("net.minecraft.entity.Entity", transformer);
        registerNodeTransformer("net.minecraft.entity.item.EntityMinecart", transformer);
    }
}
