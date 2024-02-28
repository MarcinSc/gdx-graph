package com.gempukku.libgdx.graph.shader.producer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.shader.config.EndModelShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.setting.Culling;
import com.gempukku.libgdx.graph.shader.setting.DepthTesting;
import com.gempukku.libgdx.graph.shader.ui.BlendingEditorPart;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.*;
import com.gempukku.libgdx.ui.undo.UndoableSelectBox;
import com.gempukku.libgdx.ui.undo.UndoableValidatableTextField;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class EndModelShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public EndModelShaderEditorProducer() {
        super(new EndModelShaderNodeConfiguration());
        setCloseable(false);
    }

    @Override
    protected boolean skipFieldId(String fieldId) {
        return fieldId.equals("discardValue");
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        VisTable table = new VisTable();
        table.add(new VisLabel("Discard if ", "gdx-graph-property-label"));
        UndoableSelectBox<String> comparisonType = new UndoableSelectBox<>("gdx-graph-property");
        comparisonType.setItems("<=", "<", "==", ">", ">=");
        table.add(comparisonType);
        UndoableValidatableTextField discardValue = new UndoableValidatableTextField("0", "gdx-graph-property");
        discardValue.addValidator(Validators.FLOATS);
        table.add(discardValue).growX();
        table.row();

        DefaultGraphNodeEditorPart discard = new DefaultGraphNodeEditorPart(table,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void initialize(JsonValue data) {
                        if (data != null) {
                            comparisonType.setSelected(data.getString("discardComparison", "<="));
                            discardValue.setText(SimpleNumberFormatter.format(data.getFloat("discardValue", 0f)));
                        }
                    }

                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("discardComparison", new JsonValue(comparisonType.getSelected()));
                        object.addChild("discardValue", new JsonValue(Float.parseFloat(discardValue.getText())));
                    }
                });
        GraphNodeInput input = configuration.getNodeInputs().get("discardValue");
        discard.setInputConnector(GraphNodeInputSide.Left, input,
                graphNodeEditor.getInputDrawable(input, true), graphNodeEditor.getInputDrawable(input, false));
        graphNodeEditor.addGraphEditorPart(discard);

        final ModelShaderPreviewEditorPart previewBoxPart = new ModelShaderPreviewEditorPart("preview.modelType");

        SelectEditorPart positionType = new SelectEditorPart("Position", "positionType",
                "gdx-graph-property-label", "gdx-graph-property",
                new String[] {"Object space", "World space", "Clip space"});
        graphNodeEditor.addGraphEditorPart(positionType);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Rendering config", "gdx-graph-section-label", "default"));

        EnumSelectEditorPart<Culling> cullingBox = new EnumSelectEditorPart<>("Culling", "culling", Culling.back,
                new ToStringEnum<>(),
                "gdx-graph-property-label", "gdx-graph-property",
                new Array<>(Culling.values()));
        graphNodeEditor.addGraphEditorPart(cullingBox);

        BlendingEditorPart blendingBox = new BlendingEditorPart();
        graphNodeEditor.addGraphEditorPart(blendingBox);

        EnumSelectEditorPart<DepthTesting> depthTestBox = new EnumSelectEditorPart<>("DepthTest", "depthTest", DepthTesting.less,
                new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(DepthTesting.values()));
        graphNodeEditor.addGraphEditorPart(depthTestBox);

        CheckboxEditorPart writeDepthBox = new CheckboxEditorPart("Write depth", "depthWrite", false, "gdx-graph-property-label");
        graphNodeEditor.addGraphEditorPart(writeDepthBox);

        graphNodeEditor.addGraphEditorPart(new SectionEditorPart("Preview", "gdx-graph-section-label", "default"));

        graphNodeEditor.addGraphEditorPart(previewBoxPart);
    }
}
