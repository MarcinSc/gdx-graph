package com.gempukku.libgdx.graph.ui.shader.producer.value;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.*;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;


public class ValueColorBoxProducer extends ValueGraphBoxProducer {
    public ValueColorBoxProducer(NodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JsonValue data) {
        String value = data.getString("color");

        return createGraphBox(id, value);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(id, "FFFFFFFF");
    }

    private GraphBox createGraphBox(String id, String value) {
        GraphBoxImpl end = new GraphBoxImpl(id, configuration);
        end.addGraphBoxPart(createValuePart(value));

        return end;
    }

    private GraphBoxPartImpl createValuePart(String value) {
        Color color = Color.valueOf(value);

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
        table.add(new VisLabel("Color")).growX();
        table.add(image);
        table.row();

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(table,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JsonValue object) {
                        object.addChild("color", new JsonValue(image.getColor().toString()));
                    }
                }) {
            @Override
            public void dispose() {
                picker.dispose();
            }
        };
        colorPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, configuration.getNodeOutputs().get("value"));
        return colorPart;
    }
}
