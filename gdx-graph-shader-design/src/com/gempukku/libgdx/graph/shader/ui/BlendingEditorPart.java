package com.gempukku.libgdx.graph.shader.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.setting.BlendingFactor;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisTable;

public class BlendingEditorPart extends VisTable implements GraphNodeEditorPart {
    public enum Blending {
        disabled("Disabled", false, false, BlendingFactor.zero, BlendingFactor.one),
        alpha("Alpha", false, true, BlendingFactor.source_alpha, BlendingFactor.one_minus_source_alpha),
        additive("Additive", false, true, BlendingFactor.source_alpha, BlendingFactor.one),
        multiplicative("Multiplicative", false, true, BlendingFactor.destination_color, BlendingFactor.zero),
        custom("Custom", true, true, null, null);

        private final String text;
        private final boolean customBlending;
        private final boolean enabled;
        private final BlendingFactor sourceFactor;
        private final BlendingFactor destinationFactor;

        Blending(String text, boolean customBlending, boolean enabled, BlendingFactor sourceFactor, BlendingFactor destinationFactor) {
            this.text = text;
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

        public BlendingFactor getSourceFactor() {
            return sourceFactor;
        }

        public BlendingFactor getDestinationFactor() {
            return destinationFactor;
        }

        public boolean isBlending(boolean enabled, BlendingFactor sourceFactor, BlendingFactor destinationFactor) {
            if (this.customBlending)
                return false;
            if (!enabled && !this.enabled)
                return true;
            return this.sourceFactor == sourceFactor && this.destinationFactor == destinationFactor;
        }

        public static Blending findBlending(boolean enabled, BlendingFactor sourceFactor, BlendingFactor destinationFactor) {
            for (Blending value : Blending.values()) {
                if (value.isBlending(enabled, sourceFactor, destinationFactor))
                    return value;
            }
            return null;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private EnumSelectEditorPart<Blending> blendingSelect;
    private EnumSelectEditorPart<BlendingFactor> sourceFactorSelect;
    private EnumSelectEditorPart<BlendingFactor> destinationFactorSelect;

    public BlendingEditorPart() {
        this.blendingSelect = new EnumSelectEditorPart<>("Blending: ", null, Blending.disabled, new ToStringEnum<>(),
                "gdx-graph-property-label", "gdx-graph-property",
                new Array<>(Blending.values()));

        this.sourceFactorSelect = new EnumSelectEditorPart<>("Blend Source: ", "blendingSourceFactor", BlendingFactor.zero, new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(BlendingFactor.values()));
        this.destinationFactorSelect = new EnumSelectEditorPart<>("Blend Destination: ", "blendingDestinationFactor", BlendingFactor.zero, new ToStringEnum<>(), "gdx-graph-property-label", "gdx-graph-property", new Array<>(BlendingFactor.values()));

        this.blendingSelect.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        Blending blending = Blending.valueOf(BlendingEditorPart.this.blendingSelect.getSelected());
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
            BlendingFactor sourceFactor = blendingSourceFactor != null ?
                    BlendingFactor.valueOf(blendingSourceFactor.replace(' ', '_')) : null;
            BlendingFactor destinationFactor = blendingDestinationFactor != null ?
                    BlendingFactor.valueOf(blendingDestinationFactor.replace(' ', '_')) : null;

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
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    @Override
    public Actor getActor() {
        return this;
    }
}
