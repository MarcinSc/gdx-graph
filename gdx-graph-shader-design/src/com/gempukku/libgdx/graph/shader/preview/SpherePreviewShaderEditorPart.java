package com.gempukku.libgdx.graph.shader.preview;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.builder.recipe.DefaultGraphShaderRecipe;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.InitializeShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.finalize.SetShaderProgramIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.fragment.ColorFragmentIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.InitializePropertyMapIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.init.SetupFloatPrevisionIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.OutputSource;
import com.gempukku.libgdx.graph.shader.builder.recipe.vertex.CameraAttributePositionVertexShaderIngredient;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;

public class SpherePreviewShaderEditorPart extends DisposableTable implements GraphNodeEditorPart, GraphChangedAware {
    private final ShaderPreview modelShaderPreview;
    private MeshPreviewRenderableModel renderableModel;

    public SpherePreviewShaderEditorPart(String nodeId, String output, int width, int height) {
        DefaultGraphShaderRecipe recipe = createRecipe(nodeId, output);

        modelShaderPreview = new ShaderPreview(recipe);

        add(modelShaderPreview).width(width).height(height);
    }

    private static DefaultGraphShaderRecipe createRecipe(String nodeId, String output) {
        DefaultGraphShaderRecipe recipe = new DefaultGraphShaderRecipe(nodeId);
        recipe.addInitIngredient(new InitializePropertyMapIngredient());
        recipe.addInitIngredient(new SetupFloatPrevisionIngredient());

        recipe.addVertexShaderIngredient(new CameraAttributePositionVertexShaderIngredient("Position"));

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
        ModelBuilder modelBuilder = new ModelBuilder();
        Material material = new Material();
        float sphereDiameter = 0.8f;
        Model model = modelBuilder.createSphere(sphereDiameter, sphereDiameter, sphereDiameter, 50, 50,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.Tangent | VertexAttributes.Usage.TextureCoordinates);

        renderableModel = new MeshPreviewRenderableModel(model.meshes.get(0));
        model.dispose();
        modelShaderPreview.setRenderableModel(renderableModel);
    }

    @Override
    protected void disposeWidget() {
        renderableModel.dispose();
        renderableModel = null;
    }
}
