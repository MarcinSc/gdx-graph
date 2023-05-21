package com.gempukku.libgdx.graph.ui.pipeline.value;

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
import com.gempukku.libgdx.graph.pipeline.util.WhitePixel;
import com.gempukku.libgdx.graph.ui.ColorPickerSupplier;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutput;
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
    public ValueColorEditorProducer(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        Color color = Color.valueOf("FFFFFFFF");
        Color oldColor = new Color(color);

        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.texture);
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinWidth(20);

        VisImage image = new VisImage(baseDrawable);
        image.setColor(color);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ColorPicker colorPicker = ColorPickerSupplier.instance.get();
                        colorPicker.setColor(oldColor);
                        colorPicker.setListener(
                                new ColorPickerAdapter() {
                                    @Override
                                    public void finished(Color newColor) {
                                        if (!oldColor.equals(newColor)) {
                                            setPickedColor(image, oldColor, newColor);
                                        }
                                    }
                                });
                        //displaying picker with fade in animation
                        image.getStage().addActor(colorPicker.fadeIn());
                    }
                });


        VisTable table = new VisTable();
        table.add(new VisLabel("Color", "gdx-graph-property-label")).growX();
        table.add(image).fillY();
        table.row();

        DefaultGraphNodeEditorPart colorPart = new DefaultGraphNodeEditorPart(table,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void initialize(JsonValue data) {
                        if (data != null)
                            setPickedColor(image, oldColor, Color.valueOf(data.getString("color", "FFFFFFFF")));
                    }

                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("color", new JsonValue(image.getColor().toString()));
                    }
                });
        GraphNodeOutput output = configuration.getNodeOutputs().get("value");
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, output, getOutputDrawable(output, true), getOutputDrawable(output, false));
        return colorPart;
    }

    private void setPickedColor(VisImage image, Color oldColor, Color color) {
        image.setColor(color);
        UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
        event.setUndoableAction(new SetColorAction(image, new Color(oldColor), new Color(color)));
        image.fire(event);
        Pools.free(event);
        oldColor.set(color);
    }

    private class SetColorAction extends DefaultUndoableAction {
        private final VisImage image;
        private final Color oldColor;
        private final Color newColor;

        public SetColorAction(VisImage image, Color oldColor, Color newColor) {
            this.image = image;
            this.oldColor = oldColor;
            this.newColor = newColor;
        }

        @Override
        public void undoAction() {
            setPickedColor(image, newColor, oldColor);
        }

        @Override
        public void redoAction() {
            setPickedColor(image, oldColor, newColor);
        }
    }
}
