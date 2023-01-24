package com.gempukku.libgdx.graph.shader.common.texture;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.texture.TextureSizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class TextureSizeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public TextureSizeShaderNodeBuilder() {
        super(new TextureSizeShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        TextureFieldOutput textureValue = (TextureFieldOutput) inputs.get("texture");
        boolean invertX = data.getBoolean("invertX");
        boolean invertY = data.getBoolean("invertY");

        String sizeResultName = "size_" + nodeId;
        String widthResultName = "width_" + nodeId;
        String heightResultName = "height_" + nodeId;

        String sizeRepresentation = textureValue.getSizeRepresentation();
        String x = invertX ? "1.0 / " + sizeRepresentation + ".x" : sizeRepresentation + ".x";
        String y = invertY ? "1.0 / " + sizeRepresentation + ".y" : sizeRepresentation + ".y";

        ObjectMap<String, FieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("size")) {
            commonShaderBuilder.addMainLine("vec2 " + sizeResultName + " = vec2(" + x + ", " + y + ");");
            result.put("size", new DefaultFieldOutput(ShaderFieldType.Vector2, sizeResultName));
        }
        if (producedOutputs.contains("width")) {
            commonShaderBuilder.addMainLine("float " + widthResultName + " = " + x + ";");
            result.put("width", new DefaultFieldOutput(ShaderFieldType.Float, widthResultName));
        }
        if (producedOutputs.contains("height")) {
            commonShaderBuilder.addMainLine("float " + heightResultName + " = " + y + ";");
            result.put("height", new DefaultFieldOutput(ShaderFieldType.Float, heightResultName));
        }

        return result;
    }
}
