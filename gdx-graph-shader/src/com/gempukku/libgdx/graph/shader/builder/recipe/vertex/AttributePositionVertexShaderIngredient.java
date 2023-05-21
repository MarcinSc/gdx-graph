package com.gempukku.libgdx.graph.shader.builder.recipe.vertex;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public class AttributePositionVertexShaderIngredient implements GraphShaderRecipeIngredient {
    private String propertyName;

    public AttributePositionVertexShaderIngredient(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public void processIngredient(boolean designTime, GraphWithProperties graph, GraphShader graphShader, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        ShaderPropertySource shaderPropertySource = graphShader.getPropertySource(propertyName);
        ShaderFieldType shaderFieldType = shaderPropertySource.getShaderFieldType();
        GraphShaderNodeBuilder.FieldOutput positionField = shaderFieldType.addAsVertexAttribute(vertexShaderBuilder, null, shaderPropertySource);

        vertexShaderBuilder.addMainLine("vec3 positionWorld = " + positionField.getRepresentation() + ";");

        vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
        vertexShaderBuilder.addMainLine("v_position_world = positionWorld;");

        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = vec4((positionWorld.xy * 2.0 - 1.0), 0.0, 1.0);");
    }
}
