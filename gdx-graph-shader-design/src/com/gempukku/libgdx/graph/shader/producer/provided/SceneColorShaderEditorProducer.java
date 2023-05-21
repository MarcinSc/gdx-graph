package com.gempukku.libgdx.graph.shader.producer.provided;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.TextureWrapDisplayText;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class SceneColorShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public SceneColorShaderEditorProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        Texture.TextureWrap[] wrapValues = new Texture.TextureWrap[]{Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat};

        EnumSelectEditorPart<Texture.TextureWrap> uWrapBox = new EnumSelectEditorPart<>("U Wrap ", "uWrap", wrapValues[0],
                new TextureWrapDisplayText(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(wrapValues));
        EnumSelectEditorPart<Texture.TextureWrap> vWrapBox = new EnumSelectEditorPart<>("V Wrap ", "vWrap", wrapValues[0],
                new TextureWrapDisplayText(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(wrapValues));
        graphNodeEditor.addGraphEditorPart(uWrapBox);
        graphNodeEditor.addGraphEditorPart(vWrapBox);
    }
}
