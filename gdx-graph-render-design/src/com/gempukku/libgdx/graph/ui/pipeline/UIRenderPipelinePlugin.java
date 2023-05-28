package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.RenderPipelineRuntimeInitializer;
import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.*;
import com.gempukku.libgdx.graph.pipeline.config.math.common.*;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.*;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.*;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.*;
import com.gempukku.libgdx.graph.pipeline.config.math.value.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.value.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.*;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.FileGraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.*;
import com.gempukku.libgdx.graph.ui.pipeline.value.*;
import com.kotcrab.vis.ui.VisUI;

public class UIRenderPipelinePlugin implements UIGdxGraphPlugin {
    @Override
    public void initialize(FileHandleResolver assetResolver) {
        // Register graph type
        UIRenderPipelineGraphType graphType = new UIRenderPipelineGraphType(VisUI.getSkin().getDrawable("graph-render-pipeline-icon"));
        GraphTypeRegistry.registerType(graphType);

        // Register node editors
        GdxGraphNodeEditorProducer endProducer = new GdxGraphNodeEditorProducer(new EndPipelineNodeConfiguration());
        endProducer.setCloseable(false);
        UIRenderPipelineConfiguration.register(endProducer);

        UIRenderPipelineConfiguration.register(new ValueColorEditorProducer(new ValueColorPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new ValueFloatEditorProducer(new ValueFloatPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new ValueVector2EditorProducer(new ValueVector2PipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new ValueVector3EditorProducer(new ValueVector3PipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new ValueBooleanEditorProducer(new ValueBooleanPipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new TimePipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new RenderSizePipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new AddPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new DividePipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new MultiplyPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new OneMinusPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ReciprocalPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SubtractPipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new AbsPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new CeilingPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ClampPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new FloorPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new FractionalPartPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new LerpPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new MaximumPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new MinimumPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ModuloPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SaturatePipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SignPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SmoothstepPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new StepPipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ExponentialBase2PipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ExponentialPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new InverseSquareRootPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new LogarithmBase2PipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new NaturalLogarithmPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new PowerPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SquareRootPipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new CrossProductPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new DistancePipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new DotProductPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new LengthPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new NormalizePipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ArccosPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ArcsinPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new ArctanPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new CosPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new DegreesPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new RadiansPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SinPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new TanPipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new SplitPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new MergePipelineNodeConfiguration()));

        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new StartPipelineNodeConfiguration()));
        UIRenderPipelineConfiguration.register(new GdxGraphNodeEditorProducer(new PipelineRendererNodeConfiguration()));

        // Register property types
        UIRenderPipelineConfiguration.registerPropertyType(new PropertyFloatEditorDefinition());
        UIRenderPipelineConfiguration.registerPropertyType(new PropertyVector2EditorDefinition());
        UIRenderPipelineConfiguration.registerPropertyType(new PropertyVector3EditorDefinition());
        UIRenderPipelineConfiguration.registerPropertyType(new PropertyColorEditorDefinition());
        UIRenderPipelineConfiguration.registerPropertyType(new PropertyBooleanEditorDefinition());
        UIRenderPipelineConfiguration.registerPropertyType(
                new PipelinePropertyEditorDefinitionImpl("New Camera", "Camera"));

        RuntimePluginRegistry.register(RenderPipelineRuntimeInitializer.class);

        RenderPipelineTemplateRegistry.register(
                new FileGraphTemplate(graphType, "Empty pipeline",
                        assetResolver.resolve("template/empty-pipeline.json")));

    }
}
