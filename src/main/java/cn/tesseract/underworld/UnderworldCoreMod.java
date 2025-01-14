package cn.tesseract.underworld;

import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import cn.tesseract.mycelium.asm.minecraft.HookLoader;
import cn.tesseract.underworld.hook.Accessor;
import cn.tesseract.underworld.hook.IPortalData;
import cn.tesseract.underworld.hook.IWorldData;
import cn.tesseract.underworld.hook.UnderworldHook;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class UnderworldCoreMod extends HookLoader {
    @Override
    protected void registerHooks() {
        registerHookContainer(UnderworldHook.class.getName());
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

        registerNodeTransformer("net.minecraft.entity.Entity", new Accessor(IPortalData.class.getName()));
        registerNodeTransformer("net.minecraft.world.World", new Accessor(IWorldData.class.getName()));

        /*NodeTransformer transformer = node -> {
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
        registerNodeTransformer("net.minecraft.entity.item.EntityMinecart", transformer);*/
    }
}
