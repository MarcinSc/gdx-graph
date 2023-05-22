package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.ColorFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.OutputSource;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.AttributePositionVertexShaderIngredient;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;

public class ScreenPreviewShaderEditorPart extends DisposableTable implements GraphNodeEditorPart, GraphChangedAware {
    private final ShaderPreview modelShaderPreview;
    private SimplePreviewRenderableModel renderableModel;

    public ScreenPreviewShaderEditorPart(String nodeId, String output, int width, int height) {
        DefaultGraphShaderRecipe recipe = createRecipe(nodeId, output);

        modelShaderPreview = new ShaderPreview(recipe);
        modelShaderPreview.setRenderableModelSupplier(UIModelShaderConfiguration.getPreviewModelSuppliers().get("Rectangle"));

        add(modelShaderPreview).width(width).height(height);
    }

    private static DefaultGraphShaderRecipe createRecipe(String nodeId, String output) {
        DefaultGraphShaderRecipe recipe = new DefaultGraphShaderRecipe(nodeId);
        recipe.addInitIngredient(new InitializePropertyMapIngredient());
        recipe.addInitIngredient(new SetupFloatPrevisionIngredient());

        recipe.addVertexShaderIngredient(new AttributePositionVertexShaderIngredient("Position"));

        recipe.addFragmentShaderIngredient(new ColorFragmentIngredient(new OutputSource(nodeId, output)));

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
    public void graphChanged(GraphChangedEvent event, GraphWithProperties graph) {
        if (event.isData() || event.isStructure()) {
            modelShaderPreview.graphChanged(graph);
        }
    }

    @Override
    protected void initializeWidget() {
        try {
//            renderableModel = new SimplePreviewRenderableModel(4, new short[]{0, 2, 1, 2, 0, 3});
//            renderableModel.addFunctionAttributeValues(AttributeFunctions.Position, new ArrayValuePerVertex<>(
//                    new Vector3(0, 0, 0),
//                    new Vector3(0, 1, 0),
//                    new Vector3(1, 1, 0),
//                    new Vector3(1, 0, 0)
//            ));
//            renderableModel.addFunctionAttributeValues(AttributeFunctions.TexCoord0, new ArrayValuePerVertex<>(
//                    new Vector2(0, 0),
//                    new Vector2(0, 1),
//                    new Vector2(1, 1),
//                    new Vector2(1, 0)
//            ));
//            modelShaderPreview.setRenderableModelSupplier(renderableModel);
        } catch (Exception exp) {
            // Ignore
        }
    }

    @Override
    protected void disposeWidget() {
        if (renderableModel != null) {
            renderableModel.dispose();
        }
        renderableModel = null;
    }
}
