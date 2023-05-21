package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.part.ToStringEnum;
import com.gempukku.libgdx.ui.graph.editor.part.CollapsibleEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.EnumSelectEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.SelectEditorPart;
import com.gempukku.libgdx.ui.undo.UndoableTextField;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.List;

public class DefaultShaderPropertyEditor extends VisTable implements ShaderPropertyEditor {
    private final EnumSelectEditorPart<PropertyLocation> locationPath;
    private String propertyType;
    private Array<GraphNodeEditorPart> propertyEditorParts = new Array<>();
    private VisTextField nameField;
    private CollapsibleEditorPart attributeFunctionPart;

    public DefaultShaderPropertyEditor(String name, String propertyType, List<String> functionTypes) {
        this.propertyType = propertyType;

        nameField = new UndoableTextField(name, "gdx-graph-property");
        VisTable headerTable = new VisTable();
        headerTable.add(new VisLabel("Name: ", "gdx-graph-property-label"));
        headerTable.add(nameField).growX();
        headerTable.row();
        add(headerTable).growX().row();

        if (functionTypes.size() > 0) {
            String[] functions = new String[functionTypes.size()+1];
            functions[0] = "- none -";
            int index = 1;
            for (String functionType : functionTypes) {
                functions[index++] = functionType;
            }

            SelectEditorPart selectEditorPart = new SelectEditorPart("Function:", "function",
                    "gdx-graph-property-label", "gdx-graph-property",
                    functions);
            attributeFunctionPart = new CollapsibleEditorPart("function.expanded", selectEditorPart);
        }

        locationPath = new EnumSelectEditorPart<>("Location", "location", PropertyLocation.Uniform, new ToStringEnum<>(),
                "gdx-graph-property-label", "gdx-graph-property",
                new Array<>(new PropertyLocation[]{PropertyLocation.Attribute, PropertyLocation.Uniform, PropertyLocation.Global_Uniform}));
        locationPath.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (locationPath.getSelected().equals(PropertyLocation.Attribute.toString())) {
                            if (attributeFunctionPart != null)
                                attributeFunctionPart.setExpanded(true);
                        } else {
                            if (attributeFunctionPart != null)
                                attributeFunctionPart.setExpanded(false);
                        }
                    }
                });

        addPropertyEditorPart(locationPath);
        if (attributeFunctionPart != null) {
            addPropertyEditorPart(attributeFunctionPart);
        }
    }

    @Override
    public String getType() {
        return propertyType;
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    @Override
    public JsonValue getData() {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);

        for (GraphNodeEditorPart graphEditorPart : propertyEditorParts)
            graphEditorPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }

    public void addPropertyEditorPart(GraphNodeEditorPart graphEditorPart) {
        propertyEditorParts.add(graphEditorPart);
        final Actor actor = graphEditorPart.getActor();
        add(actor).growX().row();
    }

    public void initialize(JsonValue value) {
        for (GraphNodeEditorPart graphEditorPart : propertyEditorParts) {
            graphEditorPart.initialize(value);
        }
    }

    @Override
    public PropertyLocation getPropertyLocation() {
        return PropertyLocation.valueOf(locationPath.getSelected());
    }

    @Override
    public Actor getActor() {
        return this;
    }
}
