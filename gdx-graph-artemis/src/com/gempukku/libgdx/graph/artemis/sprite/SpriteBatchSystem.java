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
import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.WritablePropertyContainer;
import com.gempukku.libgdx.graph.plugin.models.GraphModels;
import com.gempukku.libgdx.graph.shader.property.MapWritablePropertyContainer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.DisposableProducer;
import com.gempukku.libgdx.graph.util.PreserveMinimumDisposableProducer;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.sprite.ObjectBatchModel;
import com.gempukku.libgdx.graph.util.sprite.ObjectReference;
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.manager.LimitedCapacityObjectRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.manager.MultiPageObjectBatchModel;
import com.gempukku.libgdx.graph.util.sprite.manager.TexturePagedObjectBatchModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.ContinuousSlotsObjectMeshStorage;
import com.gempukku.libgdx.graph.util.sprite.storage.DefaultSpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.ObjectMeshStorage;
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

    private SpriteModel defaultSpriteModel = new QuadSpriteModel();

    private ComponentMapper<SpriteBatchComponent> spriteBatchComponentMapper;

    private final ObjectMap<String, ObjectBatchModel<RenderableSprite, ObjectReference>> spriteBatchMap = new ObjectMap<>();

    private final Array<Entity> newSpriteBatchEntities = new Array<>();

    public SpriteBatchSystem() {
        super(Aspect.all(SpriteBatchComponent.class));
    }

    public ObjectBatchModel<RenderableSprite, ObjectReference> getSpriteBatchModel(String spriteBatchName) {
        return spriteBatchMap.get(spriteBatchName);
    }

    public void setDefaultSpriteModel(SpriteModel defaultSpriteModel) {
        this.defaultSpriteModel = defaultSpriteModel;
    }

    @Override
    protected void inserted(int entityId) {
        newSpriteBatchEntities.add(world.getEntity(entityId));
    }

    private ObjectBatchModel<RenderableSprite, ObjectReference> createSpriteBatchModel(final SpriteBatchComponent spriteBatch, final GraphModels graphModels, final String tag) {
        final SpriteModel spriteModel = getSpriteModel(spriteBatch);
        SpriteBatchComponent.SystemType spriteSystemType = spriteBatch.getType();

        final VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        if (spriteSystemType == SpriteBatchComponent.SystemType.TexturePaged) {
            return new TexturePagedObjectBatchModel<RenderableSprite, ObjectReference, ObjectBatchModel<RenderableSprite, ObjectReference>>(graphModels, tag,
                    new DisposableProducer<ObjectBatchModel<RenderableSprite, ObjectReference>>() {
                        @Override
                        public ObjectBatchModel<RenderableSprite, ObjectReference> create() {
                            MapWritablePropertyContainer propertyContainer = new MapWritablePropertyContainer();
                            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, vertexPropertySources, graphModels, tag, spriteModel, propertyContainer);
                        }

                        @Override
                        public void dispose(ObjectBatchModel<RenderableSprite, ObjectReference> disposable) {
                            disposable.dispose();
                        }
                    });
        } else if (spriteSystemType == SpriteBatchComponent.SystemType.MultiPaged) {
            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, vertexPropertySources, graphModels, tag, spriteModel, new MapWritablePropertyContainer());
        } else {
            throw new GdxRuntimeException("Unable to create SpriteBatchModel unknown type: " + spriteSystemType);
        }
    }

    private static MultiPageObjectBatchModel<RenderableSprite, ObjectReference, LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference>> createMultiPageSpriteBatchModel(
            final SpriteBatchComponent spriteBatch,
            final VertexAttributes vertexAttributes, final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            final GraphModels graphModels, final String tag, final SpriteModel spriteModel,
            final WritablePropertyContainer propertyContainer) {
        final PreserveMinimumDisposableProducer<LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference>> preserveMinimum = new PreserveMinimumDisposableProducer<>(spriteBatch.getMinimumPages(),
                new DisposableProducer<LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference>>() {
                    @Override
                    public LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference> create() {
                        ObjectMeshStorage<RenderableSprite, ObjectReference> objectMeshStorage = new ContinuousSlotsObjectMeshStorage<>(
                                spriteBatch.getSpritesPerPage(), vertexAttributes.vertexSize / 4,
                                spriteModel,
                                new DefaultSpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel));
                        LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference> model = new LimitedCapacityObjectRenderableModel<>(
                                spriteBatch.isStaticBatch(), objectMeshStorage,
                                vertexAttributes, propertyContainer, spriteModel);
                        graphModels.addModel(tag, model);
                        return model;
                    }

                    @Override
                    public void dispose(LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference> model) {
                        graphModels.removeModel(tag, model);
                        model.dispose();
                    }
                });
        return new MultiPageObjectBatchModel<RenderableSprite, ObjectReference, LimitedCapacityObjectRenderableModel<RenderableSprite, ObjectReference>>(preserveMinimum, propertyContainer) {
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
        ObjectBatchModel spritesModel = spriteBatchMap.remove(spriteSystemComponent.getName());
        spritesModel.dispose();
    }

    @Override
    protected void processSystem() {
        for (Entity newSpriteEntity : newSpriteBatchEntities) {
            SpriteBatchComponent spriteSystem = spriteBatchComponentMapper.get(newSpriteEntity);
            GraphModels graphModels = pipelineRendererSystem.getPluginData(GraphModels.class);

            String tag = spriteSystem.getRenderTag();
            ObjectBatchModel spriteModel = createSpriteBatchModel(spriteSystem, graphModels, tag);

            WritablePropertyContainer propertyContainer = spriteModel.getPropertyContainer();
            for (ObjectMap.Entry<String, Object> property : spriteSystem.getProperties()) {
                propertyContainer.setValue(property.key, evaluatePropertySystem.evaluateProperty(newSpriteEntity, property.value, Object.class));
            }

            spriteBatchMap.put(spriteSystem.getName(), spriteModel);
        }
        newSpriteBatchEntities.clear();
    }

    @Override
    public void dispose() {
        for (ObjectBatchModel spriteModel : spriteBatchMap.values()) {
            spriteModel.dispose();
        }
        spriteBatchMap.clear();
    }
}
