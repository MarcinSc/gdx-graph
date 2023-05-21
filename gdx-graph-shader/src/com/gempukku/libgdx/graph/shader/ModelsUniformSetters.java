package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.math.Matrix4;

public class ModelsUniformSetters {
    public final static UniformRegistry.UniformSetter projViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(shaderContext.getRenderableModel().getWorldTransform()).mul(shaderContext.getCamera().combined));
        }
    };

    public final static UniformRegistry.UniformSetter normalProjViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(shaderContext.getRenderableModel().getWorldTransform()).mul(shaderContext.getCamera().combined).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter viewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(shaderContext.getRenderableModel().getWorldTransform()).mul(shaderContext.getCamera().view));
        }
    };

    public final static UniformRegistry.UniformSetter normalViewWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmp4 = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmp4.set(shaderContext.getRenderableModel().getWorldTransform()).mul(shaderContext.getCamera().view).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter worldTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getRenderableModel().getWorldTransform());
        }
    };

    public final static UniformRegistry.UniformSetter normalWorldTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(shaderContext.getRenderableModel().getWorldTransform()).toNormalMatrix());
        }
    };
}
