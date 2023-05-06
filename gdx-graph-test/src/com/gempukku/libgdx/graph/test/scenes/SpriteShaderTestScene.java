package com.gempukku.libgdx.graph.test.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.time.TimeKeeper;
import com.gempukku.libgdx.graph.util.ArrayValuePerVertex;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.DefaultRenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.SpriteUtil;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMemoryMesh;
import com.gempukku.libgdx.graph.util.storage.DefaultMultiPartRenderableModel;
import com.gempukku.libgdx.graph.util.storage.GdxMeshRenderableModel;
import com.gempukku.libgdx.graph.util.storage.MeshSerializer;
import com.gempukku.libgdx.graph.util.storage.MultiPartRenderableModel;

public class SpriteShaderTestScene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private Camera camera;
    private MultiPartRenderableModel<RenderableSprite, SpriteReference> spriteBatch;

    @Override
    public String getName() {
        return "Sprites Test";
    }

    @Override
    public void initializeScene() {
        camera = new OrthographicCamera();
        pipelineRenderer = loadPipelineRenderer();

        GraphModels graphModels = pipelineRenderer.getPluginData(GraphModels.class);

        String tag = "Test";
        VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        SpriteModel spriteModel = new QuadSpriteModel();

        MeshSerializer<RenderableSprite> spriteSerializer = new SpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel);

        SpriteSlotMemoryMesh<RenderableSprite> sprites = new SpriteSlotMemoryMesh<>(
                2, spriteModel, spriteSerializer);

        GdxMeshRenderableModel gdxMesh = new GdxMeshRenderableModel(true, sprites, vertexAttributes, new MapWritablePropertyContainer());

        spriteBatch = new DefaultMultiPartRenderableModel<>(sprites, gdxMesh);

        graphModels.addModel(tag, spriteBatch);

        DefaultRenderableSprite sprite1 = new DefaultRenderableSprite();
        sprite1.setValue("Position", new Vector3(0, 0, -10));
        sprite1.setValue("UV", SpriteUtil.QUAD_UVS);
        ArrayValuePerVertex<Vector2> colorPerVertex = new ArrayValuePerVertex<>(
                new Vector2(0, 1), new Vector2(1, 0), new Vector2(0, 0), new Vector2(1, 1));
        sprite1.setValue("Vertex Color", colorPerVertex);

        DefaultRenderableSprite sprite2 = new DefaultRenderableSprite();
        sprite2.setValue("Position", new Vector3(150, 0, -10));
        sprite2.setValue("UV", SpriteUtil.QUAD_UVS);

        spriteBatch.addPart(sprite1);
        spriteBatch.addPart(sprite2);

        graphModels.setGlobalProperty("Test", "Color", new Vector2(1f, 1f));
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void resizeScene(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void disposeScene() {
        spriteBatch.dispose();
        pipelineRenderer.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        PipelineRenderer pipelineRenderer = PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/sprite-shader-test.json"), timeKeeper);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        return pipelineRenderer;
    }
}