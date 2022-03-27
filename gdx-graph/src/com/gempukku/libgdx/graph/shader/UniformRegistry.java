package com.gempukku.libgdx.graph.shader;

public interface UniformRegistry {
    void registerAttribute(final String alias);

    void registerGlobalUniform(final String alias, final UniformSetter setter);

    void registerLocalUniform(final String alias, final UniformSetter setter);

    void registerGlobalStructArrayUniform(final String alias, String[] fieldNames, StructArrayUniformSetter setter);

    void registerLocalStructArrayUniform(final String alias, String[] fieldNames, StructArrayUniformSetter setter);

    interface UniformSetter {
        void set(final BasicShader shader, final int location, ShaderContext shaderContext);
    }

    interface StructArrayUniformSetter {
        void set(final BasicShader shader, final int startingLocation, int[] fieldOffsets, int structSize, ShaderContext shaderContext);
    }
}
