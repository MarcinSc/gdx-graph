package com.gempukku.libgdx.graph.plugin.particles.design.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.plugin.particles.config.EndParticlesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.BlendingBoxPart;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SectionEditorPart;

public class EndParticlesShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public EndParticlesShaderBoxProducer() {
        super(new EndParticlesShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final ParticlesShaderPreviewBoxPart previewBoxPart = new ParticlesShaderPreviewBoxPart();

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Rendering config"));

        EnumSelectEditorPart<BasicShader.Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", new ToStringEnum<>(), BasicShader.Culling.values());
        graphNodeEditor.addGraphBoxPart(cullingBox);

        BlendingBoxPart blendingBox = new BlendingBoxPart();
        graphNodeEditor.addGraphBoxPart(blendingBox);

        EnumSelectEditorPart<BasicShader.DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", new ToStringEnum<>(), BasicShader.DepthTesting.values());
        graphNodeEditor.addGraphBoxPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite");
        graphNodeEditor.addGraphBoxPart(writeDepthBox);

        graphNodeEditor.addGraphBoxPart(new SectionEditorPart("Preview"));

        graphNodeEditor.addGraphBoxPart(previewBoxPart);
    }
}
