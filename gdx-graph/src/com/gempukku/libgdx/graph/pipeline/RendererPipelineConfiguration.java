package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.field.BooleanFieldType;
import com.gempukku.libgdx.graph.field.FloatFieldType;
import com.gempukku.libgdx.graph.field.Vector2FieldType;
import com.gempukku.libgdx.graph.field.Vector3FieldType;
import com.gempukku.libgdx.graph.pipeline.field.CameraPipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.ColorPipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldTypeRegistry;
import com.gempukku.libgdx.graph.pipeline.producer.math.arithmetic.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.common.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.exponential.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.geometric.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.trigonometry.*;
import com.gempukku.libgdx.graph.pipeline.producer.math.value.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.math.value.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.producer.node.PipelineNodeProducer;
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
import com.gempukku.libgdx.graph.plugin.callback.producer.RenderCallbackPipelineNodeProducer;

public class RendererPipelineConfiguration {
    private static final ObjectMap<String, PipelineNodeProducer> pipelineNodeProducers = new ObjectMap<>();
    private static final Array<PipelinePropertyProducer> pipelinePropertyProducers = new Array<>();

    public static void register(PipelineNodeProducer pipelineNodeProducer) {
        pipelineNodeProducers.put(pipelineNodeProducer.getType(), pipelineNodeProducer);
    }

    static {
        register(new StartPipelineNodeProducer());
        register(new EndPipelineNodeProducer());
        register(new RenderCallbackPipelineNodeProducer());
        register(new PipelineRendererNodeProducer());

        register(new ValueFloatPipelineNodeProducer());
        register(new ValueVector2PipelineNodeProducer());
        register(new ValueVector3PipelineNodeProducer());
        register(new ValueColorPipelineNodeProducer());
        register(new ValueBooleanPipelineNodeProducer());

        register(new RenderSizePipelineNodeProducer());
        register(new TimePipelineNodeProducer());

        register(new AddPipelineNodeProducer());
        register(new DividePipelineNodeProducer());
        register(new MultiplyPipelineNodeProducer());
        register(new OneMinusPipelineNodeProducer());
        register(new ReciprocalPipelineNodeProducer());
        register(new SubtractPipelineNodeProducer());

        register(new AbsPipelineNodeProducer());
        register(new CeilingPipelineNodeProducer());
        register(new ClampPipelineNodeProducer());
        register(new FloorPipelineNodeProducer());
        register(new FractionalPartPipelineNodeProducer());
        register(new LerpPipelineNodeProducer());
        register(new MaximumPipelineNodeProducer());
        register(new MinimumPipelineNodeProducer());
        register(new ModuloPipelineNodeProducer());
        register(new SaturatePipelineNodeProducer());
        register(new SignPipelineNodeProducer());
        register(new SmoothstepPipelineNodeProducer());
        register(new StepPipelineNodeProducer());

        register(new ExponentialBase2PipelineNodeProducer());
        register(new ExponentialPipelineNodeProducer());
        register(new InverseSquareRootPipelineNodeProducer());
        register(new LogarithmBase2PipelineNodeProducer());
        register(new NaturalLogarithmPipelineNodeProducer());
        register(new PowerPipelineNodeProducer());
        register(new SquareRootPipelineNodeProducer());

        register(new CrossProductPipelineNodeProducer());
        register(new DistancePipelineNodeProducer());
        register(new DotProductPipelineNodeProducer());
        register(new LengthPipelineNodeProducer());
        register(new NormalizePipelineNodeProducer());

        register(new ArccosPipelineNodeProducer());
        register(new ArcsinPipelineNodeProducer());
        register(new ArctanPipelineNodeProducer());
        register(new CosPipelineNodeProducer());
        register(new DegreesPipelineNodeProducer());
        register(new RadiansPipelineNodeProducer());
        register(new SinPipelineNodeProducer());
        register(new TanPipelineNodeProducer());

        register(new MergePipelineNodeProducer());
        register(new SplitPipelineNodeProducer());

        register(new PropertyPipelineNodeProducer());

        register(new BloomPipelineNodeProducer());
        register(new GaussianBlurPipelineNodeProducer());
        register(new DepthOfFieldPipelineNodeProducer());
        register(new GammaCorrectionPipelineNodeProducer());

        registerPropertyProducer(new FloatPipelinePropertyProducer(), new FloatFieldType());
        registerPropertyProducer(new Vector2PipelinePropertyProducer(), new Vector2FieldType());
        registerPropertyProducer(new Vector3PipelinePropertyProducer(), new Vector3FieldType());
        registerPropertyProducer(new ColorPipelinePropertyProducer(), new ColorPipelineFieldType());
        registerPropertyProducer(new BooleanPipelinePropertyProducer(), new BooleanFieldType());
        registerPropertyProducer(new CameraPipelinePropertyProducer(), new CameraPipelineFieldType());
    }

    public static void registerPropertyProducer(PipelinePropertyProducer pipelinePropertyProducer, PipelineFieldType pipelineFieldType) {
        pipelinePropertyProducers.add(pipelinePropertyProducer);
        PipelineFieldTypeRegistry.registerPipelineFieldType(pipelineFieldType);
    }

    public static PipelineNodeProducer findProducer(String type) {
        return pipelineNodeProducers.get(type);
    }

    public static PipelineFieldType findFieldType(String type) {
        return PipelineFieldTypeRegistry.findPipelineFieldType(type);
    }

    public static PipelinePropertyProducer findPropertyProducer(String type) {
        for (PipelinePropertyProducer pipelinePropertyProducer : pipelinePropertyProducers) {
            if (pipelinePropertyProducer.getType().equals(type))
                return pipelinePropertyProducer;
        }
        return null;
    }

    private RendererPipelineConfiguration() {

    }
}
