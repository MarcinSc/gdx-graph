package com.gempukku.libgdx.graph.shader.producer.math.value;

import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapVectorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.ui.undo.UndoableSelectBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class RemapVectorShaderEditorProducer extends GdxGraphNodeEditorProducer {
    public RemapVectorShaderEditorProducer() {
        super(new RemapVectorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, NodeConfiguration configuration) {
        final VisSelectBox<String> xBox = createSelectBox("X");
        final VisSelectBox<String> yBox = createSelectBox("Y");
        final VisSelectBox<String> zBox = createSelectBox("Z");
        final VisSelectBox<String> wBox = createSelectBox("W");

        VisTable table = new VisTable();
        table.add(new VisLabel("X: ", "gdx-graph-property-label"));
        table.add(xBox);
        table.add(new VisLabel("Y: ", "gdx-graph-property-label"));
        table.add(yBox);
        table.row();
        table.add(new VisLabel("Z: ", "gdx-graph-property-label"));
        table.add(zBox);
        table.add(new VisLabel("W: ", "gdx-graph-property-label"));
        table.add(wBox);
        table.row();

        graphNodeEditor.addGraphEditorPart(
                new DefaultGraphNodeEditorPart(
                        table,
                        new DefaultGraphNodeEditorPart.Callback() {
                            @Override
                            public void initialize(JsonValue data) {
                                if (data != null) {
                                    xBox.setSelected(data.getString("x", "X"));
                                    yBox.setSelected(data.getString("y", "Y"));
                                    zBox.setSelected(data.getString("z", "Z"));
                                    wBox.setSelected(data.getString("w", "W"));
                                }
                            }

                            @Override
                            public void serialize(JsonValue object) {
                                object.addChild("x", new JsonValue(xBox.getSelected()));
                                object.addChild("y", new JsonValue(yBox.getSelected()));
                                object.addChild("z", new JsonValue(zBox.getSelected()));
                                object.addChild("w", new JsonValue(wBox.getSelected()));
                            }
                        }));
    }

    private VisSelectBox<String> createSelectBox(String defaultValue) {
        final VisSelectBox<String> result = new UndoableSelectBox<>("gdx-graph-property");
        result.setItems("0.0", "1.0", "X", "Y", "Z", "W");
        result.setSelected(defaultValue);
        result.setAlignment(Align.right);
        return result;
    }
}
