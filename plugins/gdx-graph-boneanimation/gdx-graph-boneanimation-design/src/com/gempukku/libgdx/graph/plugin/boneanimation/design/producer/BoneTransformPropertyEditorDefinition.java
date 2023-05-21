package com.gempukku.libgdx.graph.plugin.boneanimation.design.producer;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneTransformFieldType;
import com.gempukku.libgdx.graph.shader.boneanimation.property.BoneWeightFieldType;
import com.gempukku.libgdx.graph.shader.property.DefaultShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditor;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.IntegerEditorPart;
import com.kotcrab.vis.ui.util.Validators;

import java.util.List;

public class BoneTransformPropertyEditorDefinition implements ShaderPropertyEditorDefinition {
    private List<String> propertyFunctions;

    @Override
    public String getType() {
        return BoneTransformFieldType.type;
    }

    @Override
    public String getDefaultName() {
        return "New Bone Transform";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public void setPropertyFunctions(List<String> propertyFunctions) {
        this.propertyFunctions = propertyFunctions;
    }

    @Override
    public ShaderPropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultShaderPropertyEditor result = new DefaultShaderPropertyEditor(name, BoneWeightFieldType.type, propertyFunctions);
        result.addPropertyEditorPart(new IntegerEditorPart("Max bone count", "maxBoneCount", 12, new Validators.GreaterThanValidator(0),
                "gdx-graph-property-label", "gdx-graph-property"));
        result.initialize(jsonObject);

        return result;
    }
}
