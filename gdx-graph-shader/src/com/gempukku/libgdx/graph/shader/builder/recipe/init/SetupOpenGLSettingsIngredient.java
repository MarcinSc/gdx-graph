package com.gempukku.libgdx.graph.shader.builder.recipe.init;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.setting.BlendingFactor;
import com.gempukku.libgdx.graph.shader.setting.Culling;
import com.gempukku.libgdx.graph.shader.setting.DepthTesting;
import com.gempukku.libgdx.ui.graph.data.GraphNode;

public class SetupOpenGLSettingsIngredient implements GraphShaderRecipeIngredient {
    private final String nodeId;

    public SetupOpenGLSettingsIngredient(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderOutputResolver outputResolver, PipelineRendererConfiguration configuration) {
        GraphNode endNode = graph.getNodeById(nodeId);
        JsonValue data = endNode.getData();

        String cullingValue = data.getString("culling", null);
        if (cullingValue != null)
            graphShader.setCulling(Culling.valueOf(cullingValue));

        String depthTest = data.getString("depthTest", null);
        if (depthTest != null)
            graphShader.setDepthTesting(DepthTesting.valueOf(depthTest.replace(' ', '_')));

        boolean depthWrite = data.getBoolean("depthWrite", false);
        graphShader.setDepthWriting(depthWrite);

        boolean blending = data.getBoolean("blending", false);
        graphShader.setBlending(blending);

        String blendingSourceFactor = data.getString("blendingSourceFactor", null);
        if (blendingSourceFactor != null)
            graphShader.setBlendingSourceFactor(BlendingFactor.valueOf(blendingSourceFactor.replace(' ', '_')));

        String blendingDestinationFactor = data.getString("blendingDestinationFactor", null);
        if (blendingDestinationFactor != null)
            graphShader.setBlendingDestinationFactor(BlendingFactor.valueOf(blendingDestinationFactor.replace(' ', '_')));

    }
}
