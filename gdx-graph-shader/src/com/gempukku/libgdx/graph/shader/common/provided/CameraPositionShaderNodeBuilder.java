package com.gempukku.libgdx.graph.shader.common.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.provided.CameraPositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class CameraPositionShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public CameraPositionShaderNodeBuilder() {
        super(new CameraPositionShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        commonShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");
        return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "u_cameraPosition"));
    }
}
