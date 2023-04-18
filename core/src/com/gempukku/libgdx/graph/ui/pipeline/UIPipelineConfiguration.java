package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.common.Supplier;
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
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyEditorDefinitionImpl;
import com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor.DepthOfFieldBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyColorEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyFloatEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector2EditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector3EditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.CheckboxEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;

import java.util.Map;
import java.util.TreeMap;

public class UIPipelineConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new TreeMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    static {
        GdxGraphNodeEditorProducer endProducer = new GdxGraphNodeEditorProducer(new EndPipelineNodeConfiguration()) {
            @Override
            protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {

            }

            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        register(endProducer);

        register(new ValueColorBoxProducerDefault(new ValueColorPipelineNodeConfiguration()));
        register(new ValueFloatBoxProducerDefault(new ValueFloatPipelineNodeConfiguration()));
        register(new ValueVector2BoxProducerDefault(new ValueVector2PipelineNodeConfiguration()));
        register(new ValueVector3BoxProducerDefault(new ValueVector3PipelineNodeConfiguration()));
        register(new ValueBooleanBoxProducerDefault(new ValueBooleanPipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new TimePipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new RenderSizePipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new AddPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DividePipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MultiplyPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new OneMinusPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ReciprocalPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SubtractPipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new AbsPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CeilingPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ClampPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FloorPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FractionalPartPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new LerpPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MaximumPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MinimumPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ModuloPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SaturatePipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SignPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SmoothstepPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new StepPipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new ExponentialBase2PipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ExponentialPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new InverseSquareRootPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new LogarithmBase2PipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new NaturalLogarithmPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new PowerPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SquareRootPipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new CrossProductPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DistancePipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DotProductPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new LengthPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new NormalizePipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new ArccosPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ArcsinPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ArctanPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CosPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DegreesPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new RadiansPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SinPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new TanPipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new SplitPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MergePipelineNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new StartPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new PipelineRendererNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new BloomPipelineNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new GaussianBlurPipelineNodeConfiguration()));
        register(new DepthOfFieldBoxProducer());
        register(new GdxGraphNodeEditorProducer(new GammaCorrectionPipelineNodeConfiguration()));

        registerPropertyType(new PropertyFloatEditorDefinition());
        registerPropertyType(new PropertyVector2EditorDefinition());
        registerPropertyType(new PropertyVector3EditorDefinition());
        registerPropertyType(new PropertyColorEditorDefinition());
        PipelinePropertyEditorDefinitionImpl booleanProperty = new PipelinePropertyEditorDefinitionImpl("New Boolean", "Boolean");
        booleanProperty.addPropertyBoxPart(
                new Supplier<GraphNodeEditorPart>() {
                    @Override
                    public GraphNodeEditorPart get() {
                        return new CheckboxEditorPart("Value", "value", false);
                    }
                });
        registerPropertyType(booleanProperty);
        registerPropertyType(new PipelinePropertyEditorDefinitionImpl("New Camera", "Camera"));
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, ? extends PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}
