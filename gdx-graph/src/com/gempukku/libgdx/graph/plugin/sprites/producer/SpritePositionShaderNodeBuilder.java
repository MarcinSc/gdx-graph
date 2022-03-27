package com.gempukku.libgdx.graph.plugin.sprites.producer;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.sprites.config.SpritePositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class SpritePositionShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SpritePositionShaderNodeBuilder() {
        super(new SpritePositionShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(new VertexAttribute(512, 3, ShaderProgram.POSITION_ATTRIBUTE), "vec3");

        return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, ShaderProgram.POSITION_ATTRIBUTE));
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        VertexAttribute attribute = new VertexAttribute(512, 3, ShaderProgram.POSITION_ATTRIBUTE);
        copyAttributeToFragmentShader(attribute, "v_position", vertexShaderBuilder, fragmentShaderBuilder);
        return LibGDXCollections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position"));
    }
}
