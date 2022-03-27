package com.gempukku.libgdx.graph.ui.pipeline;

import com.gempukku.libgdx.graph.pipeline.config.math.arithmetic.*;
import com.gempukku.libgdx.graph.pipeline.config.math.common.*;
import com.gempukku.libgdx.graph.pipeline.config.math.exponential.*;
import com.gempukku.libgdx.graph.pipeline.config.math.geometric.*;
import com.gempukku.libgdx.graph.pipeline.config.math.trigonometry.*;
import com.gempukku.libgdx.graph.pipeline.config.math.value.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.value.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.*;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxPart;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.part.CheckboxBoxPart;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PropertyPipelineGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor.DepthOfFieldBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class UIPipelineConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer> propertyProducers = new TreeMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyBoxProducer propertyBoxProducer) {
        propertyProducers.put(propertyBoxProducer.getType(), propertyBoxProducer);
    }

    static {
        GraphBoxProducer endProducer = new GraphBoxProducerImpl(new EndPipelineNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        register(endProducer);

        register(new PropertyPipelineGraphBoxProducer());

        register(new ValueColorBoxProducer(new ValueColorPipelineNodeConfiguration()));
        register(new ValueFloatBoxProducer(new ValueFloatPipelineNodeConfiguration()));
        register(new ValueVector2BoxProducer(new ValueVector2PipelineNodeConfiguration()));
        register(new ValueVector3BoxProducer(new ValueVector3PipelineNodeConfiguration()));
        register(new ValueBooleanBoxProducer(new ValueBooleanPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new TimePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RenderSizePipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AddPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DividePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MultiplyPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new OneMinusPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ReciprocalPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SubtractPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AbsPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CeilingPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ClampPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FloorPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FractionalPartPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LerpPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MaximumPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MinimumPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ModuloPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SaturatePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SignPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SmoothstepPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new StepPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new ExponentialBase2PipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ExponentialPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new InverseSquareRootPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LogarithmBase2PipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NaturalLogarithmPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PowerPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SquareRootPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new CrossProductPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DistancePipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DotProductPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LengthPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NormalizePipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new ArccosPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArcsinPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArctanPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CosPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DegreesPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RadiansPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SinPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new TanPipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new SplitPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MergePipelineNodeConfiguration()));

        register(new GraphBoxProducerImpl(new StartPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PipelineRendererNodeConfiguration()));

        register(new GraphBoxProducerImpl(new BloomPipelineNodeConfiguration()));
        register(new GraphBoxProducerImpl(new GaussianBlurPipelineNodeConfiguration()));
        register(new DepthOfFieldBoxProducer());
        register(new GraphBoxProducerImpl(new GammaCorrectionPipelineNodeConfiguration()));

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        PipelinePropertyBoxProducerImpl booleanProperty = new PipelinePropertyBoxProducerImpl("New Boolean", "Boolean");
        booleanProperty.addPropertyBoxPart(
                new Supplier<PropertyBoxPart>() {
                    @Override
                    public PropertyBoxPart get() {
                        return new CheckboxBoxPart("Value", "value", false);
                    }
                });
        propertyProducers.put("Boolean", booleanProperty);
        propertyProducers.put("Camera", new PipelinePropertyBoxProducerImpl("New Camera", "Camera"));
        propertyProducers.put("Callback", new PipelinePropertyBoxProducerImpl("New Callback", "Callback"));
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
