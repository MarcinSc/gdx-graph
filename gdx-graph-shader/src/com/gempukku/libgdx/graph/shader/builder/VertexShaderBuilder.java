package com.gempukku.libgdx.graph.shader.builder;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.gempukku.libgdx.graph.pipeline.util.GdxCompatibilityUtils;
import com.gempukku.libgdx.graph.shader.UniformRegistry;

public class VertexShaderBuilder extends CommonShaderBuilder {
    private final ObjectMap<String, String> attributeComments = new OrderedMap<>();
    private final ObjectMap<String, String> attributeVariables = new OrderedMap<>();

    public VertexShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public void addAttributeVariable(VertexAttribute vertexAttribute, String type, String comment) {
        addAttributeVariable(vertexAttribute.alias, vertexAttribute.numComponents, type, comment);
    }

    public void addAttributeVariable(String name, int componentCount, String type, String comment) {
        String existingType = attributeVariables.get(name);
        if (existingType != null && !existingType.equals(type))
            throw new IllegalStateException("Already contains vertex attribute of that name with different type");
        if (existingType == null) {
            uniformRegistry.registerAttribute(name, componentCount);
            attributeVariables.put(name, type);
            attributeComments.put(name, comment);
        }
    }

    private void appendAttributeVariables(StringBuilder stringBuilder) {
        for (ObjectMap.Entry<String, String> uniformDefinition : attributeVariables.entries()) {
            String comment = attributeComments.get(uniformDefinition.key);
            if (comment != null)
                stringBuilder.append("// " + comment + "\n");
            stringBuilder.append("attribute " + uniformDefinition.value + " " + uniformDefinition.key + ";\n");
        }
        if (!attributeVariables.isEmpty())
            stringBuilder.append("\n");
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        result.append(GdxCompatibilityUtils.getShaderVersionCode());
        appendInitial(result);
        appendStructures(result);
        appendAttributeVariables(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }
}
