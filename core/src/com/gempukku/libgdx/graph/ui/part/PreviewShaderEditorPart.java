package com.gempukku.libgdx.graph.ui.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.ColorFromOutputFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupOpenGLSettingsIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.AttributePositionVertexShaderIngredient;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.graph.ui.preview.ModelShaderPreview;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.VisTable;

public class PreviewShaderEditorPart extends VisTable implements GraphNodeEditorPart, GraphChangedAware {
    private final ModelShaderPreview modelShaderPreview;

    public PreviewShaderEditorPart(String nodeId, String output, int width, int height) {
        DefaultGraphShaderRecipe recipe = createRecipe(nodeId, output);

        modelShaderPreview = new ModelShaderPreview(recipe);
        modelShaderPreview.setModel(ModelShaderPreview.ShaderPreviewModel.Rectangle);

        add(modelShaderPreview).width(width).height(height);
    }

    private static DefaultGraphShaderRecipe createRecipe(String nodeId, String output) {
        DefaultGraphShaderRecipe recipe = new DefaultGraphShaderRecipe();
        recipe.addInitIngredient(new InitializePropertyMapIngredient());
        recipe.addInitIngredient(new SetupOpenGLSettingsIngredient("end"));
        recipe.addInitIngredient(new SetupFloatPrevisionIngredient());

        recipe.addVertexShaderIngredient(new AttributePositionVertexShaderIngredient("Position"));

        recipe.addFragmentShaderIngredient(new ColorFromOutputFragmentIngredient(nodeId, output));

        recipe.addFinalizeShaderIngredient(new SetShaderProgramIngredient());
        recipe.addFinalizeShaderIngredient(new InitializeShaderProgramIngredient());
        return recipe;
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

    @Override
    public void initialize(JsonValue data) {

    }

    @Override
    public void serializePart(JsonValue value) {

    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        if (event.isData() || event.isStructure()) {
            modelShaderPreview.graphChanged(hasErrors, graph);
        }
    }
}
