package cn.tesseract.underworld.hook;

import cn.tesseract.mycelium.asm.NodeTransformer;
import cn.tesseract.mycelium.asm.minecraft.HookLibPlugin;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Replace implements NodeTransformer {
    private final String method;
    private final String target;
    private final String transformed;

    public Replace(String method, String target, String transformed) {
        this.method = method;
        this.target = target;
        this.transformed = transformed;
    }

    @Override
    public void transform(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (HookLibPlugin.getMethodMcpName(method.name).equals(this.method))
                for (int i = 0; i < method.instructions.size(); i++) {
                    if (method.instructions.get(i) instanceof MethodInsnNode insn)
                        if (HookLibPlugin.getMethodMcpName(insn.name).equals(target)) {
                            insn.name = transformed;
                            break;
                        }
                }
        }
    }
}
