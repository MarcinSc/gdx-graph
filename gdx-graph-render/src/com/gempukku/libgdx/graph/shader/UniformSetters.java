package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;

public class UniformSetters {
    private UniformSetters() {
    }

    public final static UniformRegistry.UniformSetter projViewTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getCamera().combined);
        }
    };

    public final static UniformRegistry.UniformSetter normalProjViewTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(shaderContext.getCamera().combined).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter viewTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getCamera().view);
        }
    };

    public final static UniformRegistry.UniformSetter normalViewTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(shaderContext.getCamera().view).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter projTrans = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getCamera().projection);
        }
    };

    public final static UniformRegistry.UniformSetter normalProjTrans = new UniformRegistry.UniformSetter() {
        private final Matrix4 tmpM = new Matrix4();

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, tmpM.set(shaderContext.getCamera().projection).toNormalMatrix());
        }
    };

    public final static UniformRegistry.UniformSetter cameraPosition = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Camera camera = shaderContext.getCamera();
            shader.setUniform(location, camera.position.x, camera.position.y, camera.position.z);
        }
    };
    public final static UniformRegistry.UniformSetter cameraViewportSize = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Camera camera = shaderContext.getCamera();
            shader.setUniform(location, camera.viewportWidth, camera.viewportHeight);
        }
    };
    public final static UniformRegistry.UniformSetter cameraDirection = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getCamera().direction);
        }
    };
    public final static UniformRegistry.UniformSetter cameraUp = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getCamera().up);
        }
    };
    public final static UniformRegistry.UniformSetter cameraClipping = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Camera camera = shaderContext.getCamera();
            shader.setUniform(location, camera.near, camera.far);
        }
    };
    public final static UniformRegistry.UniformSetter viewportSize = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, 1f * shaderContext.getRenderWidth(), 1f * shaderContext.getRenderHeight());
        }
    };
    public final static UniformRegistry.UniformSetter pixelSize = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, 1f / shaderContext.getRenderWidth(), 1f / shaderContext.getRenderHeight());
        }
    };
    public final static UniformRegistry.UniformSetter time = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getTimeProvider().getTime());
        }
    };

    public static class SinTime implements UniformRegistry.UniformSetter {
        private final float multiplier;

        public SinTime(float multiplier) {
            this.multiplier = multiplier;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, MathUtils.sin(shaderContext.getTimeProvider().getTime() * multiplier));
        }
    }

    public static class CosTime implements UniformRegistry.UniformSetter {
        private final float multiplier;

        public CosTime(float multiplier) {
            this.multiplier = multiplier;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, MathUtils.cos(shaderContext.getTimeProvider().getTime() * multiplier));
        }
    }

    public static class DeltaTime implements UniformRegistry.UniformSetter {
        private final float multiplier;

        public DeltaTime(float multiplier) {
            this.multiplier = multiplier;
        }

        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            shader.setUniform(location, shaderContext.getTimeProvider().getDelta() * multiplier);
        }
    }

    public final static UniformRegistry.UniformSetter depthTexture = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Texture depthTexture = shaderContext.getDepthTexture();
            final int unit = shader.getContext().bindTexture(depthTexture);
            shader.setUniform(location, unit);
        }
    };

    public final static UniformRegistry.UniformSetter colorTexture = new UniformRegistry.UniformSetter() {
        @Override
        public void set(BasicShader shader, int location, ShaderContext shaderContext) {
            Texture colorTexture = shaderContext.getColorTexture();
            final int unit = shader.getContext().bindTexture(colorTexture);
            shader.setUniform(location, unit);
        }
    };
}
