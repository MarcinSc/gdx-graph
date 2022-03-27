package com.gempukku.libgdx.graph.plugin.models;

import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderContext;
import com.gempukku.libgdx.graph.shader.UniformRegistry;

public class ModelsUniformSetters {
    public final static UniformRegistry.UniformSetter projViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag())).mul(shaderContext.getCamera().combined));
        }
    };

    public final static UniformRegistry.UniformSetter normalProjViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag())).mul(shaderContext.getCamera().combined).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter viewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag())).mul(shaderContext.getCamera().view));
        }
    };

    public final static UniformRegistry.UniformSetter normalViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag())).mul(shaderContext.getCamera().view).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, ((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag()));
        }
    };

    public final static UniformRegistry.UniformSetter normalWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(((ModelShaderContext) shaderContext).getRenderableModel().getWorldTransform(shader.getTag())).toNormalMatrix());
        }
    };

    public static class Bones implements UniformRegistry.UniformSetter {
        private final static Matrix4 idtMatrix = new Matrix4();
        public final float[] bones;

        public Bones(final int numBones) {
            this.bones = new float[numBones * 16];
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Matrix4[] modelBones = ((ModelShaderContext) shaderContext).getRenderableModel().getBones(shader.getTag());
            for (int i = 0; i < bones.length; i += 16) {
                final int idx = i / 16;
                if (modelBones == null || idx >= modelBones.length || modelBones[idx] == null)
                    System.arraycopy(idtMatrix.val, 0, bones, i, 16);
                else
                    System.arraycopy(modelBones[idx].val, 0, bones, i, 16);
            }
            shader.setUniformMatrix4Array(location, bones);
        }
    }
}
