package com.gempukku.libgdx.graph.shader.builder.recipe.fragment;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class ColorAlphaFragmentIngredient implements GraphShaderRecipeIngredient {
    private String colorNodeId;
    private String colorProperty;
    private String alphaNodeId;
    private String alphaProperty;

    public ColorAlphaFragmentIngredient(String colorNodeId, String colorProperty, String alphaNodeId, String alphaProperty) {
        this.colorNodeId = colorNodeId;
        this.colorProperty = colorProperty;
        this.alphaNodeId = alphaNodeId;
        this.alphaProperty = alphaProperty;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        GraphShaderNodeBuilder.FieldOutput colorField = outputResolver.getSingleOutputForInput(colorNodeId, colorProperty);
        GraphShaderNodeBuilder.FieldOutput alphaField = outputResolver.getSingleOutputForInput(alphaNodeId, alphaProperty);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";

        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector4)) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector3)) {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        } else if (colorField.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            color = "vec4(" + colorField.getRepresentation() + ", 0.0, " + alpha + ")";
        } else {
            color = "vec4(vec3(" + colorField.getRepresentation() + "), " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");
    }
}
