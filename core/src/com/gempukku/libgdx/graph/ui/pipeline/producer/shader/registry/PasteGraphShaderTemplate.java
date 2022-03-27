package com.gempukku.libgdx.graph.ui.pipeline.producer.shader.registry;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.ui.LibgdxGraphScreen;
import com.gempukku.libgdx.graph.ui.graph.GraphType;

public class PasteGraphShaderTemplate implements GraphShaderTemplate {
    private GraphType graphType;

    public PasteGraphShaderTemplate(GraphType graphType) {
        this.graphType = graphType;
    }

    @Override
    public String getTitle() {
        return "Paste";
    }

    @Override
    public void invokeTemplate(Stage stage, Callback callback) {
        if (LibgdxGraphScreen.graphInClipboard.graphType != null && graphType.getType().equals(LibgdxGraphScreen.graphInClipboard.graphType.getType())) {
            callback.addShader("", LibgdxGraphScreen.graphInClipboard.graph);
        }
    }
}
