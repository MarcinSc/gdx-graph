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
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.pipeline.producer.PipelinePropertyEditorDefinitionImpl;
import com.gempukku.libgdx.graph.ui.pipeline.producer.postprocessor.DepthOfFieldBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyColorEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyFloatEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector2EditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.property.PropertyVector3EditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.DefaultGraphNodeEditor;
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
        DefaultMenuGraphNodeEditorProducer endProducer = new DefaultMenuGraphNodeEditorProducer(new EndPipelineNodeConfiguration()) {
            @Override
            protected void buildNodeEditor(DefaultGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {

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

        register(new DefaultMenuGraphNodeEditorProducer(new TimePipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new RenderSizePipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new AddPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DividePipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MultiplyPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new OneMinusPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ReciprocalPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SubtractPipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new AbsPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CeilingPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ClampPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FloorPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FractionalPartPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new LerpPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MaximumPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MinimumPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ModuloPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SaturatePipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SignPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SmoothstepPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new StepPipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new ExponentialBase2PipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ExponentialPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new InverseSquareRootPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new LogarithmBase2PipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new NaturalLogarithmPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new PowerPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SquareRootPipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new CrossProductPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DistancePipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DotProductPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new LengthPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new NormalizePipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new ArccosPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ArcsinPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ArctanPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CosPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DegreesPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new RadiansPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SinPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new TanPipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new SplitPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MergePipelineNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new StartPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new PipelineRendererNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new BloomPipelineNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new GaussianBlurPipelineNodeConfiguration()));
        register(new DepthOfFieldBoxProducer());
        register(new DefaultMenuGraphNodeEditorProducer(new GammaCorrectionPipelineNodeConfiguration()));

        propertyProducers.put(PipelineFieldType.Float, new PropertyFloatEditorDefinition());
        propertyProducers.put(PipelineFieldType.Vector2, new PropertyVector2EditorDefinition());
        propertyProducers.put(PipelineFieldType.Vector3, new PropertyVector3EditorDefinition());
        propertyProducers.put(PipelineFieldType.Color, new PropertyColorEditorDefinition());
        PipelinePropertyEditorDefinitionImpl booleanProperty = new PipelinePropertyEditorDefinitionImpl("New Boolean", "Boolean");
        booleanProperty.addPropertyBoxPart(
                new Supplier<GraphNodeEditorPart>() {
                    @Override
                    public GraphNodeEditorPart get() {
                        return new CheckboxEditorPart("Value", "value", false);
                    }
                });
        propertyProducers.put(PipelineFieldType.Boolean, booleanProperty);
        propertyProducers.put(PipelineFieldType.Camera, new PipelinePropertyEditorDefinitionImpl("New Camera", "Camera"));
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
