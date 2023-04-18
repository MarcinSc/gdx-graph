package com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.DepthOfFieldPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;
import com.kotcrab.vis.ui.util.Validators;

public class DepthOfFieldBoxProducer extends GdxGraphNodeEditorProducer {
    public DepthOfFieldBoxProducer() {
        super(new DepthOfFieldPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        FloatEditorPart maxBlurPart = new FloatEditorPart("Max blur", "maxBlur", 10, new Validators.GreaterThanValidator(0, false));
        maxBlurPart.setValue(10f);
        graphNodeEditor.addGraphBoxPart(maxBlurPart);

        CheckboxEditorPart blurBackground = new CheckboxEditorPart("Blur background", "blurBackground");
        blurBackground.setValue(false);
        graphNodeEditor.addGraphBoxPart(blurBackground);
    }
}
