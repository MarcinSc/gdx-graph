package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class ValueColorEditorProducer extends ValueGraphEditorProducer {
    private VisImage image;
    private ColorPicker picker;
    private Color oldColor;

    public ValueColorEditorProducer(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        Color color = Color.valueOf("FFFFFFFF");
        oldColor = color;

        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.texture);
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinWidth(20);

        image = new VisImage(baseDrawable);
        image.setColor(color);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        picker.setColor(oldColor);
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });


        VisTable table = new DisposableTable() {
            @Override
            protected void initializeWidget() {
                picker = new ColorPicker(new ColorPickerAdapter() {
                    @Override
                    public void finished(Color newColor) {
                        if (!oldColor.equals(newColor)) {
                            setPickedColor(newColor);
                        }
                    }
                });
            }

            @Override
            protected void disposeWidget() {
                picker.dispose();
                picker = null;
            }
        };
        table.add(new VisLabel("Color", "gdx-graph-property-label")).growX();
        table.add(image).fillY();
        table.row();

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(table,
                new DefaultGraphNodeEditorPart.Callback() {
                                @Override
                    public void serialize(JsonValue object) {
                        object.addChild("color", new JsonValue(image.getColor().toString()));
                    }
                }) {
            @Override
            public void initialize(JsonValue data) {
                if (data != null)
                    image.setColor(Color.valueOf(data.getString("color", "FFFFFFFF")));
            }
        };
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }

    private void setPickedColor(Color color) {
        image.setColor(color);
        UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
        event.setUndoableAction(new SetColorAction(oldColor, color));
        image.fire(event);
        Pools.free(event);
        oldColor = color;
    }

    private class SetColorAction extends DefaultUndoableAction {
        private final Color oldColor;
        private final Color newColor;

        public SetColorAction(Color oldColor, Color newColor) {
            this.oldColor = oldColor;
            this.newColor = newColor;
        }

        @Override
        public void undoAction() {
            setPickedColor(oldColor);
        }

        @Override
        public void redoAction() {
            setPickedColor(newColor);
        }
    }
}
