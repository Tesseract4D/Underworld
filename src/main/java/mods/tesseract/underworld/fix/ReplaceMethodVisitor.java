package mods.tesseract.underworld.fix;

import net.tclproject.mysteriumlib.asm.common.MinecraftMetaReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ReplaceMethodVisitor extends ClassVisitor {
    String inMethod;
    String targetMethod;
    String transformedMethod;

    public ReplaceMethodVisitor(String inMethod, String targetMethod, String transformedMethod) {
        super(Opcodes.ASM5);
        this.inMethod = inMethod;
        this.targetMethod = targetMethod;
        this.transformedMethod = transformedMethod;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor old = super.visitMethod(access, name, desc, signature, exceptions);
        return MinecraftMetaReader.checkSameMethod(name, inMethod) ? new MethodVisitor(Opcodes.ASM5, old) {
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
                if (MinecraftMetaReader.checkSameMethod(name, targetMethod))
                    super.visitMethodInsn(opcode, owner, transformedMethod, desc, itf);
                else
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        } : old;
    }
}
