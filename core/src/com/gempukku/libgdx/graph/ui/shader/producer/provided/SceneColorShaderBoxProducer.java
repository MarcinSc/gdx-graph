package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.graphics.Texture;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.TextureWrapDisplayText;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class SceneColorShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public SceneColorShaderBoxProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        Texture.TextureWrap[] wrapValues = new Texture.TextureWrap[]{Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat};

        EnumSelectEditorPart<Texture.TextureWrap> uWrapBox = new EnumSelectEditorPart<>("U Wrap ", "uWrap",
                new TextureWrapDisplayText(), "gdx-graph-property-label", "gdx-graph-property", wrapValues);
        EnumSelectEditorPart<Texture.TextureWrap> vWrapBox = new EnumSelectEditorPart<>("V Wrap ", "vWrap",
                new TextureWrapDisplayText(), "gdx-graph-property-label", "gdx-graph-property", wrapValues);
        graphNodeEditor.addGraphBoxPart(uWrapBox);
        graphNodeEditor.addGraphBoxPart(vWrapBox);
    }
}
