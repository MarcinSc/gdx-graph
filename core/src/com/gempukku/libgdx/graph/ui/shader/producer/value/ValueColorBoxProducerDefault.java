package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.config.MenuNodeConfiguration;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.ui.graph.editor.part.DefaultGraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;


public class ValueColorBoxProducerDefault extends ValueGraphBoxProducerDefault {
    public ValueColorBoxProducerDefault(MenuNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    protected DefaultGraphNodeEditorPart createValuePart() {
        Color color = Color.valueOf("FFFFFFFF");

        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.texture);
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinSize(20, 20);

        final VisImage image = new VisImage(baseDrawable);
        image.setColor(color);

        final ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
                image.fire(new GraphChangedEvent(false, true));
            }
        });
        picker.setColor(color);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });


        VisTable table = new VisTable();
        table.add(new VisLabel("Color", "gdx-graph-property-label")).growX();
        table.add(image);
        table.row();

        DefaultGraphNodeEditorPart colorPart = new ColorEditorPart(table,
                new DefaultGraphNodeEditorPart.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("color", new JsonValue(image.getColor().toString()));
                    }
                }, picker, image);
        colorPart.setOutputConnector(GraphNodeOutputSide.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }

    private class ColorEditorPart extends DefaultGraphNodeEditorPart implements Disposable {
        private ColorPicker picker;
        private VisImage image;

        public ColorEditorPart(Actor actor, Callback callback, ColorPicker picker, VisImage image) {
            super(actor, callback);
            this.picker = picker;
            this.image = image;
        }

        @Override
        public void initialize(JsonValue data) {
            if (data != null)
                image.setColor(Color.valueOf(data.getString("color", "FFFFFFFF")));
        }

        @Override
        public void dispose() {
            picker.dispose();
        }
    }
}
