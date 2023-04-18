package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.field.BooleanFieldType;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.field.Vector2FieldType;
import com.gempukku.libgdx.graph.field.Vector3FieldType;
import com.gempukku.libgdx.graph.pipeline.field.CameraPipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.ColorPipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.exponential.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.geometric.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.value.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.value.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.DepthOfFieldPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.postprocessor.GaussianBlurPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.provided.RenderSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.provided.TimePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.PipelineRendererNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.value.producer.*;
import com.gempukku.libgdx.graph.pipeline.property.*;
import com.gempukku.libgdx.graph.plugin.PluginRegistry;
import com.gempukku.libgdx.graph.plugin.PluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.producer.RenderCallbackPipelineNodeProducer;

public class RenderPipelineRuntimeInitializer implements PluginRuntimeInitializer {
    @Override
    public void initialize(PluginRegistry pluginRegistry) {
        GraphTypeRegistry.registerType(new RenderPipelineGraphType());

        RendererPipelineConfiguration.register(new StartPipelineNodeProducer());
        RendererPipelineConfiguration.register(new EndPipelineNodeProducer());
        RendererPipelineConfiguration.register(new RenderCallbackPipelineNodeProducer());
        RendererPipelineConfiguration.register(new PipelineRendererNodeProducer());

        RendererPipelineConfiguration.register(new ValueFloatPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ValueVector2PipelineNodeProducer());
        RendererPipelineConfiguration.register(new ValueVector3PipelineNodeProducer());
        RendererPipelineConfiguration.register(new ValueColorPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ValueBooleanPipelineNodeProducer());

        RendererPipelineConfiguration.register(new RenderSizePipelineNodeProducer());
        RendererPipelineConfiguration.register(new TimePipelineNodeProducer());

        RendererPipelineConfiguration.register(new AddPipelineNodeProducer());
        RendererPipelineConfiguration.register(new DividePipelineNodeProducer());
        RendererPipelineConfiguration.register(new MultiplyPipelineNodeProducer());
        RendererPipelineConfiguration.register(new OneMinusPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ReciprocalPipelineNodeProducer());
        RendererPipelineConfiguration.register(new SubtractPipelineNodeProducer());

        RendererPipelineConfiguration.register(new AbsPipelineNodeProducer());
        RendererPipelineConfiguration.register(new CeilingPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ClampPipelineNodeProducer());
        RendererPipelineConfiguration.register(new FloorPipelineNodeProducer());
        RendererPipelineConfiguration.register(new FractionalPartPipelineNodeProducer());
        RendererPipelineConfiguration.register(new LerpPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MaximumPipelineNodeProducer());
        RendererPipelineConfiguration.register(new MinimumPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ModuloPipelineNodeProducer());
        RendererPipelineConfiguration.register(new SaturatePipelineNodeProducer());
        RendererPipelineConfiguration.register(new SignPipelineNodeProducer());
        RendererPipelineConfiguration.register(new SmoothstepPipelineNodeProducer());
        RendererPipelineConfiguration.register(new StepPipelineNodeProducer());

        RendererPipelineConfiguration.register(new ExponentialBase2PipelineNodeProducer());
        RendererPipelineConfiguration.register(new ExponentialPipelineNodeProducer());
        RendererPipelineConfiguration.register(new InverseSquareRootPipelineNodeProducer());
        RendererPipelineConfiguration.register(new LogarithmBase2PipelineNodeProducer());
        RendererPipelineConfiguration.register(new NaturalLogarithmPipelineNodeProducer());
        RendererPipelineConfiguration.register(new PowerPipelineNodeProducer());
        RendererPipelineConfiguration.register(new SquareRootPipelineNodeProducer());

        RendererPipelineConfiguration.register(new CrossProductPipelineNodeProducer());
        RendererPipelineConfiguration.register(new DistancePipelineNodeProducer());
        RendererPipelineConfiguration.register(new DotProductPipelineNodeProducer());
        RendererPipelineConfiguration.register(new LengthPipelineNodeProducer());
        RendererPipelineConfiguration.register(new NormalizePipelineNodeProducer());

        RendererPipelineConfiguration.register(new ArccosPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ArcsinPipelineNodeProducer());
        RendererPipelineConfiguration.register(new ArctanPipelineNodeProducer());
        RendererPipelineConfiguration.register(new CosPipelineNodeProducer());
        RendererPipelineConfiguration.register(new DegreesPipelineNodeProducer());
        RendererPipelineConfiguration.register(new RadiansPipelineNodeProducer());
        RendererPipelineConfiguration.register(new SinPipelineNodeProducer());
        RendererPipelineConfiguration.register(new TanPipelineNodeProducer());

        RendererPipelineConfiguration.register(new MergePipelineNodeProducer());
        RendererPipelineConfiguration.register(new SplitPipelineNodeProducer());

        RendererPipelineConfiguration.register(new PropertyPipelineNodeProducer());

        RendererPipelineConfiguration.register(new BloomPipelineNodeProducer());
        RendererPipelineConfiguration.register(new GaussianBlurPipelineNodeProducer());
        RendererPipelineConfiguration.register(new DepthOfFieldPipelineNodeProducer());
        RendererPipelineConfiguration.register(new GammaCorrectionPipelineNodeProducer());

        RendererPipelineConfiguration.registerPropertyProducer(new FloatPipelinePropertyProducer(), new FloatFieldType());
        RendererPipelineConfiguration.registerPropertyProducer(new Vector2PipelinePropertyProducer(), new Vector2FieldType());
        RendererPipelineConfiguration.registerPropertyProducer(new Vector3PipelinePropertyProducer(), new Vector3FieldType());
        RendererPipelineConfiguration.registerPropertyProducer(new ColorPipelinePropertyProducer(), new ColorPipelineFieldType());
        RendererPipelineConfiguration.registerPropertyProducer(new BooleanPipelinePropertyProducer(), new BooleanFieldType());
        RendererPipelineConfiguration.registerPropertyProducer(new CameraPipelinePropertyProducer(), new CameraPipelineFieldType());
    }

    @Override
    public void dispose() {

    }
}
