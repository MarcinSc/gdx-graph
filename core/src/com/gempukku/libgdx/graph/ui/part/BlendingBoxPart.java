package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.VisTable;

public class BlendingBoxPart extends VisTable implements GraphBoxPart {

    public enum Blending {
        disabled(false, false, BasicShader.BlendingFactor.zero, BasicShader.BlendingFactor.one),
        alpha(false, true, BasicShader.BlendingFactor.source_alpha, BasicShader.BlendingFactor.one_minus_source_alpha),
        additive(false, true, BasicShader.BlendingFactor.source_alpha, BasicShader.BlendingFactor.one),
        multiplicative(false, true, BasicShader.BlendingFactor.destination_color, BasicShader.BlendingFactor.zero),
        custom(true, true, null, null);

        private final boolean customBlending;
        private final boolean enabled;
        private final BasicShader.BlendingFactor sourceFactor;
        private final BasicShader.BlendingFactor destinationFactor;

        Blending(boolean customBlending, boolean enabled, BasicShader.BlendingFactor sourceFactor, BasicShader.BlendingFactor destinationFactor) {
            this.customBlending = customBlending;
            this.enabled = enabled;
            this.sourceFactor = sourceFactor;
            this.destinationFactor = destinationFactor;
        }

        public boolean isCustom() {
            return customBlending;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public BasicShader.BlendingFactor getSourceFactor() {
            return sourceFactor;
        }

        public BasicShader.BlendingFactor getDestinationFactor() {
            return destinationFactor;
        }

        public boolean isBlending(boolean enabled, BasicShader.BlendingFactor sourceFactor, BasicShader.BlendingFactor destinationFactor) {
            if (this.customBlending)
                return false;
            if (!enabled && !this.enabled)
                return true;
            return this.sourceFactor == sourceFactor && this.destinationFactor == destinationFactor;
        }

        public static Blending findBlending(boolean enabled, BasicShader.BlendingFactor sourceFactor, BasicShader.BlendingFactor destinationFactor) {
            for (Blending value : Blending.values()) {
                if (value.isBlending(enabled, sourceFactor, destinationFactor))
                    return value;
            }
            return null;
        }
    }

    private EnumSelectBoxPart<Blending> blendingSelect;
    private EnumSelectBoxPart<BasicShader.BlendingFactor> sourceFactorSelect;
    private EnumSelectBoxPart<BasicShader.BlendingFactor> destinationFactorSelect;

    public BlendingBoxPart() {
        this.blendingSelect = new EnumSelectBoxPart<>("Blending: ", null, new StringifyEnum<Blending>(), Blending.values());

        this.sourceFactorSelect = new EnumSelectBoxPart<>("Blend Source: ", "blendingSourceFactor", new ToStringEnum<BasicShader.BlendingFactor>(), BasicShader.BlendingFactor.values());
        this.destinationFactorSelect = new EnumSelectBoxPart<>("Blend Destination: ", "blendingDestinationFactor", new ToStringEnum<BasicShader.BlendingFactor>(), BasicShader.BlendingFactor.values());

        this.blendingSelect.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Blending blending = Blending.valueOf(BlendingBoxPart.this.blendingSelect.getSelected());
                        if (blending.isCustom()) {
                            sourceFactorSelect.setEnabled(true);
                            destinationFactorSelect.setEnabled(true);
                        } else {
                            sourceFactorSelect.setEnabled(false);
                            sourceFactorSelect.setSelected(blending.getSourceFactor());
                            destinationFactorSelect.setEnabled(false);
                            destinationFactorSelect.setSelected(blending.getDestinationFactor());
                        }
                    }
                });

        add(this.blendingSelect).growX().row();
        add(this.sourceFactorSelect).growX().row();
        add(this.destinationFactorSelect).growX().row();
    }

    @Override
    public void initialize(JsonValue object) {
        if (object != null) {
            boolean blending = object.getBoolean("blending", false);
            String blendingSourceFactor = object.getString("blendingSourceFactor", null);
            String blendingDestinationFactor = object.getString("blendingDestinationFactor", null);
            BasicShader.BlendingFactor sourceFactor = blendingSourceFactor != null ?
                    BasicShader.BlendingFactor.valueOf(blendingSourceFactor.replace(' ', '_')) : null;
            BasicShader.BlendingFactor destinationFactor = blendingDestinationFactor != null ?
                    BasicShader.BlendingFactor.valueOf(blendingDestinationFactor.replace(' ', '_')) : null;

            Blending resultBlending = Blending.findBlending(blending, sourceFactor, destinationFactor);
            if (resultBlending != null) {
                blendingSelect.setSelected(resultBlending);
                sourceFactorSelect.setSelected(resultBlending.getSourceFactor());
                sourceFactorSelect.setEnabled(false);
                destinationFactorSelect.setSelected(resultBlending.getDestinationFactor());
                destinationFactorSelect.setEnabled(false);
            } else {
                blendingSelect.setSelected(Blending.custom);
                sourceFactorSelect.setSelected(sourceFactor);
                destinationFactorSelect.setSelected(destinationFactor);
            }
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        Blending selectedBlending = Blending.valueOf(blendingSelect.getSelected());
        if (selectedBlending.isCustom()) {
            object.addChild("blending", new JsonValue(true));
            object.addChild("blendingSourceFactor", new JsonValue(sourceFactorSelect.getSelected()));
            object.addChild("blendingDestinationFactor", new JsonValue(destinationFactorSelect.getSelected()));
        } else {
            boolean enabled = selectedBlending.isEnabled();
            object.addChild("blending", new JsonValue(enabled));
            if (enabled) {
                object.addChild("blendingSourceFactor", new JsonValue(selectedBlending.getSourceFactor().name()));
                object.addChild("blendingDestinationFactor", new JsonValue(selectedBlending.getDestinationFactor().name()));
            }
        }
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return null;
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public void dispose() {

    }
}
