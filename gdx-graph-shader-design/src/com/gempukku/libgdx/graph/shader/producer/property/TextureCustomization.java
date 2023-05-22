package com.gempukku.libgdx.graph.shader.producer.property;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.graph.ui.pipeline.property.TextureWrapDisplayText;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultGraphNodeOutput;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class TextureCustomization implements PropertyGraphEditorCustomization {
    @Override
    public void process(DefaultNodeConfiguration configuration, GdxGraphNodeEditor result, JsonValue data) {
        ShaderFieldType shaderFieldType = ShaderFieldTypeRegistry.findShaderFieldType(configuration.getType());
        if (shaderFieldType != null && shaderFieldType.isTexture()) {
            Texture.TextureWrap[] wrapValues = new Texture.TextureWrap[]{Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat};
            configuration.addNodeOutput(new DefaultGraphNodeOutput("textureSize", "Texture Size", ShaderFieldType.Vector2));
            result.addOutputGraphPart(new DefaultGraphNodeOutput("textureSize", "Texture Size", ShaderFieldType.Vector2));
            EnumSelectEditorPart<Texture.TextureWrap> uWrap = new EnumSelectEditorPart<>("U wrap ", "uWrap", wrapValues[0],
                    new TextureWrapDisplayText(), new Array<>(wrapValues));
            EnumSelectEditorPart<Texture.TextureWrap> vWrap = new EnumSelectEditorPart<>("V wrap ", "vWrap", wrapValues[0],
                    new TextureWrapDisplayText(), new Array<>(wrapValues));
            result.addGraphEditorPart(uWrap);
            result.addGraphEditorPart(vWrap);

            if (data != null) {
                uWrap.initialize(data);
                vWrap.initialize(data);
            }
        }
    }
}
