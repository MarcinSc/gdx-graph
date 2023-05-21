package com.gempukku.libgdx.graph.shader.builder;


import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.util.GdxCompatibilityUtils;

public class FragmentShaderBuilder extends CommonShaderBuilder {
    public FragmentShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        result.append(GdxCompatibilityUtils.getShaderVersionCode());
        appendInitial(result);
        appendStructures(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }
}
