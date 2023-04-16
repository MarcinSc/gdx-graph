package com.gempukku.libgdx.graph.ui.shader.producer.provided;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.shader.config.common.provided.SceneColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.TextureWrapDisplayText;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;

public class SceneColorShaderBoxProducer extends DefaultMenuGraphNodeEditorProducer {
    public SceneColorShaderBoxProducer() {
        super(new SceneColorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        Texture.TextureWrap[] wrapValues = new Texture.TextureWrap[]{Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat};

        EnumSelectEditorPart<Texture.TextureWrap> uWrapBox = new EnumSelectEditorPart<>("U Wrap ", "uWrap",
                new TextureWrapDisplayText(), wrapValues);
        EnumSelectEditorPart<Texture.TextureWrap> vWrapBox = new EnumSelectEditorPart<>("V Wrap ", "vWrap",
                new TextureWrapDisplayText(), wrapValues);
        graphNodeEditor.addGraphBoxPart(uWrapBox);
        graphNodeEditor.addGraphBoxPart(vWrapBox);
    }
}
