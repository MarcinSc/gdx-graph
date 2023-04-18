package com.gempukku.libgdx.graph.ui.shader.producer.math.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapVectorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditor;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class RemapVectorShaderBoxProducer extends GdxGraphNodeEditorProducer {
    public RemapVectorShaderBoxProducer() {
        super(new RemapVectorShaderNodeConfiguration());
    }

    @Override
    protected void buildNodeEditorAfterIO(GdxGraphNodeEditor graphNodeEditor, Skin skin, NodeConfiguration configuration) {
        final VisSelectBox<String> xBox = createSelectBox("X");
        final VisSelectBox<String> yBox = createSelectBox("Y");
        final VisSelectBox<String> zBox = createSelectBox("Z");
        final VisSelectBox<String> wBox = createSelectBox("W");

        VisTable table = new VisTable();
        table.add("X: ");
        table.add(xBox);
        table.add("Y: ");
        table.add(yBox);
        table.row();
        table.add("Z: ");
        table.add(zBox);
        table.add("W: ");
        table.add(wBox);
        table.row();

        graphNodeEditor.addGraphBoxPart(
                new DefaultGraphNodeEditorPart(
                        table,
                        new DefaultGraphNodeEditorPart.Callback() {
                            @Override
                            public void serialize(JsonValue object) {
                                object.addChild("x", new JsonValue(xBox.getSelected()));
                                object.addChild("y", new JsonValue(yBox.getSelected()));
                                object.addChild("z", new JsonValue(zBox.getSelected()));
                                object.addChild("w", new JsonValue(wBox.getSelected()));
                            }
                        }
                ) {
                    @Override
                    public void initialize(JsonValue data) {
                        if (data != null) {
                            xBox.setSelected(data.getString("x", "X"));
                            yBox.setSelected(data.getString("y", "Y"));
                            zBox.setSelected(data.getString("z", "Z"));
                            wBox.setSelected(data.getString("w", "W"));
                        }
                    }
                });
    }

    private VisSelectBox<String> createSelectBox(String defaultValue) {
        final VisSelectBox<String> result = new VisSelectBox<>();
        result.setItems("0.0", "1.0", "X", "Y", "Z", "W");
        result.setSelected(defaultValue);
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        result.fire(new GraphChangedEvent(false, true));
                    }
                });
        return result;
    }
}
