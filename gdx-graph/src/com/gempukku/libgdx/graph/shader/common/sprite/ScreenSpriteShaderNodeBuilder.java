package com.gempukku.libgdx.graph.shader.common.sprite;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import com.gempukku.libgdx.graph.util.LibGDXCollections;

public class ScreenSpriteShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ScreenSpriteShaderNodeBuilder() {
        super(new ScreenSpriteShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput positionField = inputs.get("position");
        FieldOutput uvField = inputs.get("uv");
        FieldOutput anchorField = inputs.get("anchor");
        FieldOutput sizeField = inputs.get("size");
        FieldOutput rotationField = inputs.get("rotation");

        loadFragmentIfNotDefined(commonShaderBuilder, "screenSprite");

        String position = positionField.getRepresentation();
        String uv = uvField.getRepresentation();
        String size = resolveSize(sizeField);
        String anchor = resolveAnchor(anchorField);
        String rotation = (rotationField != null) ? rotationField.getRepresentation() : "0.0";

        String name = "result_" + nodeId;

        commonShaderBuilder.addMainLine("vec3 " + name + " = screenSprite(" + position + ", " + uv + ", " + size + ", " + anchor + ", " + rotation + ");");

        return LibGDXCollections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
    }

    private String resolveSize(FieldOutput sizeField) {
        String size;
        if (sizeField != null) {
            if (sizeField.getFieldType().getName().equals(ShaderFieldType.Float))
                size = "vec2(" + sizeField.getRepresentation() + ")";
            else
                size = sizeField.getRepresentation();
        } else {
            size = "vec2(100.0, 100.0)";
        }
        return size;
    }

    private String resolveAnchor(FieldOutput anchorField) {
        String anchor;
        if (anchorField != null) {
            if (anchorField.getFieldType().getName().equals(ShaderFieldType.Float))
                anchor = "vec2(" + anchorField.getRepresentation() + ")";
            else
                anchor = anchorField.getRepresentation();
        } else {
            anchor = "vec2(0.5, 0.5)";
        }
        return anchor;
    }
}
