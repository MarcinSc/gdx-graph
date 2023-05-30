package com.gempukku.libgdx.graph.artemis.sprite;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.data.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.data.WritablePropertyContainer;
import com.gempukku.libgdx.graph.pipeline.util.DisposableProducer;
import com.gempukku.libgdx.graph.pipeline.util.PreserveMinimumDisposableProducer;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.ShaderInformation;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteReference;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteSlotMemoryMesh;
import com.gempukku.libgdx.graph.util.storage.*;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;
import com.gempukku.libgdx.lib.artemis.shape.ShapeSystem;

// Should not be necessary, as we only want to make the ShapeSystem optional, but
// that is what I consider a bug in Artemis, where it doesn't work, if you just
// put it on field.
@Wire(failOnNull = false)
public class SpriteBatchSystem extends BaseEntitySystem {
    private EvaluatePropertySystem evaluatePropertySystem;
    private PipelineRendererSystem pipelineRendererSystem;
    @Wire(failOnNull = false)
    private ShapeSystem shapeSystem;

    private ComponentMapper<SpriteBatchComponent> spriteBatchComponentMapper;

    private SpriteModel defaultSpriteModel = new QuadSpriteModel();

    private final ObjectMap<String, MultiPartBatchModel<RenderableSprite, SpriteReference>> spriteBatchMap = new ObjectMap<>();

    private final Array<Entity> newSpriteBatchEntities = new Array<>();

    public SpriteBatchSystem() {
        super(Aspect.all(SpriteBatchComponent.class));
    }

    public MultiPartBatchModel<RenderableSprite, SpriteReference> getSpriteBatchModel(String spriteBatchName) {
        return spriteBatchMap.get(spriteBatchName);
    }

    public void setDefaultSpriteModel(SpriteModel defaultSpriteModel) {
        this.defaultSpriteModel = defaultSpriteModel;
    }

    @Override
    protected void inserted(int entityId) {
        newSpriteBatchEntities.add(world.getEntity(entityId));
    }

    private MultiPartBatchModel<RenderableSprite, SpriteReference> createSpriteBatchModel(
            final SpriteBatchComponent spriteBatch, final ShaderInformation shaderInformation, final ModelContainer<RenderableModel> modelContainer,
            final String tag, final WritablePropertyContainer propertyContainer) {
        final SpriteModel spriteModel = getSpriteModel(spriteBatch);
        SpriteBatchComponent.SystemType spriteSystemType = spriteBatch.getType();

        final VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(shaderInformation, tag);
        final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(shaderInformation, tag, vertexAttributes);

        final SpriteSerializer spriteSerializer = new SpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel);

        if (spriteSystemType == SpriteBatchComponent.SystemType.TexturePaged) {
            return new TexturePagedMultiPartBatchModel<>(shaderInformation, tag,
                    new DisposableProducer<MultiPartBatchModel<RenderableSprite, SpriteReference>>() {
                        @Override
                        public MultiPartBatchModel<RenderableSprite, SpriteReference> create() {
                            HierarchicalPropertyContainer texturePropertyContainer = new HierarchicalPropertyContainer(propertyContainer);
                            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, spriteSerializer, modelContainer, tag, spriteModel,
                                    texturePropertyContainer);
                        }

                        @Override
                        public void dispose(MultiPartBatchModel<RenderableSprite, SpriteReference> disposable) {
                            disposable.dispose();
                        }
                    });
        } else if (spriteSystemType == SpriteBatchComponent.SystemType.MultiPaged) {
            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, spriteSerializer, modelContainer, tag, spriteModel, propertyContainer);
        } else {
            throw new GdxRuntimeException("Unable to create SpriteBatchModel unknown type: " + spriteSystemType);
        }
    }

    private PagedMultiPartBatchModel<RenderableSprite, SpriteReference> createMultiPageSpriteBatchModel(
            final SpriteBatchComponent spriteBatch,
            final VertexAttributes vertexAttributes, final SpriteSerializer spriteSerializer,
            final ModelContainer<RenderableModel> modelContainer, final String tag, final SpriteModel spriteModel,
            final WritablePropertyContainer propertyContainer) {
        DisposableProducer<MultiPartRenderableModel<RenderableSprite, SpriteReference>> renderableProducer =
                new DisposableProducer<MultiPartRenderableModel<RenderableSprite, SpriteReference>>() {
                    @Override
                    public MultiPartRenderableModel<RenderableSprite, SpriteReference> create() {
                        SpriteSlotMemoryMesh<RenderableSprite> multiPartMemoryMesh =
                                new SpriteSlotMemoryMesh<>(
                                        spriteBatch.getSpritesPerPage(), spriteModel, spriteSerializer);
                        GdxMeshRenderableModel meshModel =
                                new GdxMeshRenderableModel(
                                        spriteBatch.isStaticBatch(), multiPartMemoryMesh, vertexAttributes, propertyContainer, tag);
                        DefaultMultiPartRenderableModel<RenderableSprite, SpriteReference> model =
                                new DefaultMultiPartRenderableModel<>(multiPartMemoryMesh, meshModel);
                        modelContainer.addModel(model);
                        return model;
                    }

                    @Override
                    public void dispose(MultiPartRenderableModel<RenderableSprite, SpriteReference> model) {
                        modelContainer.removeModel(model);
                        model.dispose();
                    }
                };
        final PreserveMinimumDisposableProducer<MultiPartRenderableModel<RenderableSprite, SpriteReference>> preserveMinimum =
                new PreserveMinimumDisposableProducer<>(spriteBatch.getMinimumPages(), renderableProducer);
        return new PagedMultiPartBatchModel<RenderableSprite, SpriteReference>(preserveMinimum, propertyContainer) {
            @Override
            public void dispose() {
                super.dispose();
                preserveMinimum.dispose();
            }
        };
    }

    private SpriteModel getSpriteModel(SpriteBatchComponent spriteBatch) {
        String shapeName = spriteBatch.getShapeName();
        if (shapeName != null) {
            if (shapeSystem == null)
                throw new GdxRuntimeException("Sprite batch has shape name defined, but no ShapeSystem is in context");

            short[] indices = shapeSystem.getIndices(shapeName);
            int vertexCount = shapeSystem.getVertexCount(shapeName);
            return new DefaultSpriteModel(vertexCount, indices);
        } else {
            return defaultSpriteModel;
        }
    }

    @Override
    protected void removed(int entityId) {
        SpriteBatchComponent spriteSystemComponent = spriteBatchComponentMapper.get(entityId);
        MultiPartBatchModel<RenderableSprite, SpriteReference> spritesModel = spriteBatchMap.remove(spriteSystemComponent.getName());
        spritesModel.dispose();
    }

    @Override
    protected void processSystem() {
        ShaderInformation shaderInformation = pipelineRendererSystem.getShaderInformation();
        ModelContainer<RenderableModel> modelContainer = pipelineRendererSystem.getModelContainer();

        for (Entity newSpriteEntity : newSpriteBatchEntities) {
            SpriteBatchComponent spriteSystem = spriteBatchComponentMapper.get(newSpriteEntity);

            String tag = spriteSystem.getRenderTag();
            WritablePropertyContainer propertyContainer = new MapWritablePropertyContainer();
            MultiPartBatchModel<RenderableSprite, SpriteReference> spriteModel = createSpriteBatchModel(spriteSystem, shaderInformation, modelContainer, tag, propertyContainer);

            for (ObjectMap.Entry<String, Object> property : spriteSystem.getProperties()) {
                propertyContainer.setValue(property.key, evaluatePropertySystem.evaluateProperty(newSpriteEntity, property.value, Object.class));
            }

            spriteBatchMap.put(spriteSystem.getName(), spriteModel);
        }
        newSpriteBatchEntities.clear();
    }

    @Override
    public void dispose() {
        for (MultiPartBatchModel<RenderableSprite, SpriteReference> spriteModel : spriteBatchMap.values()) {
            spriteModel.dispose();
        }
        spriteBatchMap.clear();
    }
}
