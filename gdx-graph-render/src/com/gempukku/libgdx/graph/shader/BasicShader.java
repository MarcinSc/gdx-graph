package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.libgdx.context.OpenGLContext;
import com.gempukku.libgdx.graph.shader.setting.BlendingFactor;
import com.gempukku.libgdx.graph.shader.setting.Culling;
import com.gempukku.libgdx.graph.shader.setting.DepthTesting;

public abstract class BasicShader implements UniformRegistry, Disposable {
    protected final ObjectMap<String, Attribute> attributes = new ObjectMap<>();
    protected final ObjectMap<String, Uniform> globalUniforms = new ObjectMap<>();
    protected final ObjectMap<String, Uniform> localUniforms = new ObjectMap<>();
    protected final ObjectMap<String, StructArrayUniform> globalStructArrayUniforms = new ObjectMap<>();
    protected final ObjectMap<String, StructArrayUniform> localStructArrayUniforms = new ObjectMap<>();

    protected ShaderProgram program;
    private OpenGLContext context;
    private final String tag;
    private Culling culling = Culling.back;
    private boolean blending = false;
    private BlendingFactor blendingSourceFactor = BlendingFactor.source_alpha;
    private BlendingFactor blendingDestinationFactor = BlendingFactor.one_minus_source_alpha;
    private DepthTesting depthTesting = DepthTesting.less;
    private boolean depthWriting = true;

    private boolean usingDepthTexture;
    private boolean usingColorTexture;

    private boolean initialized = false;

    public BasicShader(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public boolean isUsingDepthTexture() {
        return usingDepthTexture;
    }

    public void setUsingDepthTexture(boolean usingDepthTexture) {
        this.usingDepthTexture = usingDepthTexture;
    }

    public boolean isUsingColorTexture() {
        return usingColorTexture;
    }

    public void setUsingColorTexture(boolean usingColorTexture) {
        this.usingColorTexture = usingColorTexture;
    }

    @Override
    public void registerAttribute(String alias, int componentCount) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        validateNewAttribute(alias);
        attributes.put(alias, new Attribute(alias, componentCount));
    }

    @Override
    public void registerGlobalUniform(final String alias, final UniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        //validateNewUniform(alias, true, setter);
        globalUniforms.put(alias, new Uniform(alias, setter));
    }

    @Override
    public void registerLocalUniform(final String alias, final UniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        //validateNewUniform(alias, false, setter);
        localUniforms.put(alias, new Uniform(alias, setter));
    }

    @Override
    public void registerGlobalStructArrayUniform(final String alias, String[] fieldNames, StructArrayUniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        //validateNewStructArrayUniform(alias, true, setter);
        globalStructArrayUniforms.put(alias, new StructArrayUniform(alias, fieldNames, setter));
    }

    @Override
    public void registerLocalStructArrayUniform(final String alias, String[] fieldNames, StructArrayUniformSetter setter) {
        if (initialized) throw new GdxRuntimeException("Cannot register an uniform after initialization");
        //validateNewStructArrayUniform(alias, false, setter);
        localStructArrayUniforms.put(alias, new StructArrayUniform(alias, fieldNames, setter));
    }

    public OpenGLContext getContext() {
        return context;
    }

    private void validateNewAttribute(String alias) {
        if (attributes.containsKey(alias))
            throw new GdxRuntimeException("Attribute already registered");
    }

    private void validateNewStructArrayUniform(String alias, boolean global, StructArrayUniformSetter setter) {
        if (globalStructArrayUniforms.containsKey(alias)) {
            if (!global)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
            StructArrayUniform uniform = globalStructArrayUniforms.get(alias);
            if (uniform.setter != setter)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
        } else if (localStructArrayUniforms.containsKey(alias)) {
            if (global)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
            StructArrayUniform uniform = localStructArrayUniforms.get(alias);
            if (uniform.setter != setter)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
        }
    }

    private void validateNewUniform(String alias, boolean global, UniformSetter setter) {
        if (globalUniforms.containsKey(alias)) {
            if (!global)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
            Uniform uniform = globalUniforms.get(alias);
            if (uniform.setter != setter)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
        } else if (localUniforms.containsKey(alias)) {
            if (global)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
            Uniform uniform = localUniforms.get(alias);
            if (uniform.setter != setter)
                throw new IllegalStateException("Already contains uniform of that name with a different global flag, or setter");
        }
    }

    /**
     * Initialize this shader, causing all registered uniforms/attributes to be fetched.
     *
     * @param program ShaderProgram to initialize
     */
    protected void init(final ShaderProgram program) {
        if (initialized) throw new GdxRuntimeException("Already initialized");
        if (!program.isCompiled()) throw new GdxRuntimeException(program.getLog());
        this.program = program;

        for (Attribute attribute : attributes.values()) {
            final int location = program.getAttributeLocation(attribute.alias);
            if (location >= 0)
                attribute.setLocation(location);
        }

        for (Uniform uniform : globalUniforms.values()) {
            String alias = uniform.alias;
            int location = getUniformLocation(program, alias);
            uniform.setUniformLocation(location);
        }
        for (Uniform uniform : localUniforms.values()) {
            String alias = uniform.alias;
            int location = getUniformLocation(program, alias);
            uniform.setUniformLocation(location);
        }

        for (StructArrayUniform uniform : globalStructArrayUniforms.values()) {
            int startIndex = getUniformLocation(program, uniform.alias + "[0]." + uniform.fieldNames[0]);
            int size = program.fetchUniformLocation(uniform.alias + "[1]." + uniform.fieldNames[0], false) - startIndex;
            int[] fieldOffsets = new int[uniform.fieldNames.length];
            // Starting at 1, as first field offset is 0 by default
            for (int i = 1; i < uniform.fieldNames.length; i++) {
                fieldOffsets[i] = getUniformLocation(program, uniform.alias + "[0]." + uniform.fieldNames[i]) - startIndex;
            }
            uniform.setUniformLocations(startIndex, size, fieldOffsets);
        }
        for (StructArrayUniform uniform : localStructArrayUniforms.values()) {
            int startIndex = getUniformLocation(program, uniform.alias + "[0]." + uniform.fieldNames[0]);
            int size = program.fetchUniformLocation(uniform.alias + "[1]." + uniform.fieldNames[0], false) - startIndex;
            int[] fieldOffsets = new int[uniform.fieldNames.length];
            // Starting at 1, as first field offset is 0 by default
            for (int i = 1; i < uniform.fieldNames.length; i++) {
                fieldOffsets[i] = getUniformLocation(program, uniform.alias + "[0]." + uniform.fieldNames[i]) - startIndex;
            }
            uniform.setUniformLocations(startIndex, size, fieldOffsets);
        }
        initialized = true;
    }

    private int getUniformLocation(ShaderProgram program, String alias) {
        return program.fetchUniformLocation(alias, false);
    }

    public void setCulling(Culling culling) {
        this.culling = culling;
    }

    public void setDepthTesting(DepthTesting depthTesting) {
        this.depthTesting = depthTesting;
    }

    public boolean isDepthWriting() {
        return depthWriting;
    }

    public void setDepthWriting(boolean depthWriting) {
        this.depthWriting = depthWriting;
    }

    public void setBlending(boolean blending) {
        this.blending = blending;
    }

    public void setBlendingSourceFactor(BlendingFactor blendingSourceFactor) {
        this.blendingSourceFactor = blendingSourceFactor;
    }

    public void setBlendingDestinationFactor(BlendingFactor blendingDestinationFactor) {
        this.blendingDestinationFactor = blendingDestinationFactor;
    }

    public void begin(ShaderContext shaderContext, OpenGLContext context) {
        this.context = context;
        program.begin();

        // Set depth mask/testing
        context.setDepthMask(depthWriting);
        depthTesting.setDepthTest(context, 0f, 1f);
        culling.setCullFace(context);
        context.setBlending(blending, blendingSourceFactor.getFactor(), blendingDestinationFactor.getFactor());

        for (Uniform uniform : globalUniforms.values()) {
            if (uniform.location != -1)
                uniform.setter.set(this, uniform.location, shaderContext);
        }
        for (StructArrayUniform uniform : globalStructArrayUniforms.values()) {
            if (uniform.startIndex != -1)
                uniform.setter.set(this, uniform.startIndex, uniform.fieldOffsets, uniform.size, shaderContext);
        }
    }

    public void end() {
        program.end();
    }

    @Override
    public void dispose() {
        program = null;
        globalUniforms.clear();
        localUniforms.clear();
        globalStructArrayUniforms.clear();
        localStructArrayUniforms.clear();
        attributes.clear();
    }

    public void setUniform(final int location, final Matrix4 value) {
        program.setUniformMatrix(location, value);
    }

    public void setUniform(final int location, final Matrix3 value) {
        program.setUniformMatrix(location, value);
    }

    public void setUniform(final int location, final Vector3 value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final Vector2 value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final Color value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final float value) {
        program.setUniformf(location, value);
    }

    public void setUniform(final int location, final float v1, final float v2) {
        program.setUniformf(location, v1, v2);
    }

    public void setUniform(final int location, final float v1, final float v2, final float v3) {
        program.setUniformf(location, v1, v2, v3);
    }

    public void setUniform(final int location, final float v1, final float v2, final float v3, final float v4) {
        program.setUniformf(location, v1, v2, v3, v4);
    }

    public void setUniform(final int location, final int value) {
        program.setUniformi(location, value);
    }

    public void setUniform(final int location, final int v1, final int v2) {
        program.setUniformi(location, v1, v2);
    }

    public void setUniform(final int location, final int v1, final int v2, final int v3) {
        program.setUniformi(location, v1, v2, v3);
    }

    public void setUniform(final int location, final int v1, final int v2, final int v3, final int v4) {
        program.setUniformi(location, v1, v2, v3, v4);
    }

    public void setUniform(final int location, final TextureDescriptor textureDesc) {
        program.setUniformi(location, context.bindTexture(textureDesc));
    }

    public void setUniform(final int location, final GLTexture texture) {
        program.setUniformi(location, context.bindTexture(texture));
    }

    public void setUniformMatrix4Array(final int location, float[] values) {
        program.setUniformMatrix4fv(location, values, 0, values.length);
    }

    public void setUniformFloatArray(final int location, float[] values) {
        program.setUniform1fv(location, values, 0, values.length);
    }

    public void setUniformVector2Array(final int location, float[] values) {
        program.setUniform2fv(location, values, 0, values.length);
    }

    public void setUniformVector3Array(final int location, float[] values) {
        program.setUniform3fv(location, values, 0, values.length);
    }

    public static class Attribute {
        private final String alias;
        private final int componentCount;
        private int location = -1;

        public Attribute(String alias, int componentCount) {
            this.alias = alias;
            this.componentCount = componentCount;
        }

        private void setLocation(int location) {
            this.location = location;
        }

        public int getLocation() {
            return location;
        }

        public int getComponentCount() {
            return componentCount;
        }
    }

    protected static class Uniform {
        private final String alias;
        private final UniformSetter setter;
        private int location = -1;

        public Uniform(String alias, UniformSetter setter) {
            this.alias = alias;
            this.setter = setter;
        }

        private void setUniformLocation(int location) {
            this.location = location;
        }

        public UniformSetter getSetter() {
            return setter;
        }

        public int getLocation() {
            return location;
        }
    }

    protected static class StructArrayUniform {
        private final String alias;
        private final String[] fieldNames;
        private final StructArrayUniformSetter setter;
        private int startIndex;
        private int size;
        private int[] fieldOffsets;

        public StructArrayUniform(String alias, String[] fieldNames, StructArrayUniformSetter setter) {
            this.alias = alias;
            this.fieldNames = new String[fieldNames.length];
            System.arraycopy(fieldNames, 0, this.fieldNames, 0, fieldNames.length);
            this.setter = setter;
        }

        private void setUniformLocations(int startIndex, int size, int[] fieldOffsets) {
            this.startIndex = startIndex;
            this.size = size;
            this.fieldOffsets = fieldOffsets;
        }

        public StructArrayUniformSetter getSetter() {
            return setter;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getSize() {
            return size;
        }

        public int[] getFieldOffsets() {
            return fieldOffsets;
        }
    }
}
