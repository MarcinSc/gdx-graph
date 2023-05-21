package com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor;

import com.gempukku.libgdx.graph.pipeline.config.postprocessor.DepthOfFieldPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.FloatEditorPart;
import com.kotcrab.vis.ui.util.Validators;

public class DepthOfFieldEditorProducer extends GdxGraphNodeEditorProducer {
    public DepthOfFieldEditorProducer() {
        super(new DepthOfFieldPipelineNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        FloatEditorPart maxBlurPart = new FloatEditorPart("Max blur", "maxBlur", 10,
                new Validators.GreaterThanValidator(0, false),
                "gdx-graph-property-label", "gdx-graph-property");
        graphNodeEditor.addGraphEditorPart(maxBlurPart);

        CheckboxEditorPart blurBackground = new CheckboxEditorPart("Blur background", "blurBackground", false,
                "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(blurBackground);
    }
}
