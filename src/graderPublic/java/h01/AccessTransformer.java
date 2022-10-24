package h01;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class AccessTransformer implements ClassTransformer {

    private static int makePublic(int access) {
        return access & ~Opcodes.ACC_FINAL;
    }

    @Override
    public String getName() {
        return "AccessTransformer";
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        reader.accept(new CV(Opcodes.ASM9, writer), 0);
    }

    private static class CV extends ClassVisitor {
        public CV(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, makePublic(access), name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            return super.visitMethod(makePublic(access), name, descriptor, signature, exceptions);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
            return super.visitField(makePublic(access), name, descriptor, signature, value);
        }
    }
}
