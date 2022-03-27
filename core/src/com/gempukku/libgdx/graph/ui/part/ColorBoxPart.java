package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;


public class ColorBoxPart extends VisTable implements GraphBoxPart {
    private String property;
    private final VisImage image;
    private final ColorPicker picker;

    public ColorBoxPart(String label, String property) {
        this(label, property, Color.WHITE);
    }

    public ColorBoxPart(String label, String property, Color defaultColor) {
        this.property = property;

        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.sharedInstance.texture);
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinSize(20, 20);

        image = new VisImage(baseDrawable);
        image.setColor(defaultColor);

        picker = new ColorPicker(new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
                image.fire(new GraphChangedEvent(false, true));
            }
        });
        picker.setColor(defaultColor);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });

        add(new VisLabel(label)).growX();
        add(image);
        row();
    }


    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    public void initialize(JsonValue data) {
        if (data != null) {
            String value = data.getString(property);
            Color color = Color.valueOf(value);
            image.setColor(color);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(image.getColor().toString()));
    }

    @Override
    public void dispose() {
        picker.dispose();
    }
}
