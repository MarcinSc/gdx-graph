package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.plugin.particles.config.EndParticlesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.setting.Culling;
import com.gempukku.libgdx.graph.shader.setting.DepthTesting;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingEditorPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndParticlesShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public EndParticlesShaderEditorProducer() {
        super(new EndParticlesShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final ParticlesShaderPreviewEditorPart previewBoxPart = new ParticlesShaderPreviewEditorPart(
                "preview.cameraDistance", "preview.lifetime", "preview.initialCount",
                "preview.perSecond", "preview.modelType");

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Rendering config", "gdx-graph-section-label", "default"));

        EnumSelectEditorPart<Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", Culling.back,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(Culling.values()));
        graphNodeEditor.addGraphEditorPart(cullingBox);

        BlendingEditorPart blendingBox = new BlendingEditorPart();
        graphNodeEditor.addGraphEditorPart(blendingBox);

        EnumSelectEditorPart<DepthTesting> depthTestBox = new EnumSelectEditorPart<>("Depth Test", "depthTest", DepthTesting.less,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(DepthTesting.values()));
        graphNodeEditor.addGraphEditorPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(writeDepthBox);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Preview", "gdx-graph-section-label", "default"));

        graphNodeEditor.addGraphEditorPart(previewBoxPart);
    }
}
