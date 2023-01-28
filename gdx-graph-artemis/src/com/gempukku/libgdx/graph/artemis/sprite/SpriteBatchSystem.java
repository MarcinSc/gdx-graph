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
import com.gempukku.libgdx.graph.util.sprite.RenderableSprite;
import com.gempukku.libgdx.graph.util.sprite.SpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.manager.LimitedCapacitySpriteRenderableModel;
import com.gempukku.libgdx.graph.util.sprite.manager.MultiPageSpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.manager.TexturePagedSpriteBatchModel;
import com.gempukku.libgdx.graph.util.sprite.model.QuadSpriteModel;
import com.gempukku.libgdx.graph.util.sprite.model.SpriteModel;
import com.gempukku.libgdx.graph.util.sprite.storage.ContinuousSlotsSpriteStorage;
import com.gempukku.libgdx.graph.util.sprite.storage.DefaultSpriteSerializer;
import com.gempukku.libgdx.graph.util.sprite.storage.SpriteStorage;
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

    private final ObjectMap<String, SpriteBatchModel> spriteBatchMap = new ObjectMap<>();

    private Array<Entity> newSpriteBatchEntities = new Array<>();

    public SpriteBatchSystem() {
        super(Aspect.all(SpriteBatchComponent.class));
    }

    public SpriteBatchModel getSpriteBatchModel(String spriteBatchName) {
        return spriteBatchMap.get(spriteBatchName);
    }

    public void setDefaultSpriteModel(SpriteModel defaultSpriteModel) {
        this.defaultSpriteModel = defaultSpriteModel;
    }

    @Override
    protected void inserted(int entityId) {
        newSpriteBatchEntities.add(world.getEntity(entityId));
    }

    private SpriteBatchModel createSpriteBatchModel(final SpriteBatchComponent spriteBatch, final GraphModels graphModels, final String tag) {
        final SpriteModel spriteModel = getSpriteModel(spriteBatch);
        SpriteBatchComponent.SystemType spriteSystemType = spriteBatch.getType();

        final VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        if (spriteSystemType == SpriteBatchComponent.SystemType.TexturePaged) {
            return new TexturePagedSpriteBatchModel<>(graphModels, tag,
                    new DisposableProducer<SpriteBatchModel>() {
                        @Override
                        public SpriteBatchModel create() {
                            MapWritablePropertyContainer propertyContainer = new MapWritablePropertyContainer();
                            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, vertexPropertySources, graphModels, tag, spriteModel, propertyContainer);
                        }

                        @Override
                        public void dispose(SpriteBatchModel disposable) {
                            disposable.dispose();
                        }
                    });
        } else if (spriteSystemType == SpriteBatchComponent.SystemType.MultiPaged) {
            return createMultiPageSpriteBatchModel(spriteBatch, vertexAttributes, vertexPropertySources, graphModels, tag, spriteModel, new MapWritablePropertyContainer());
        } else {
            throw new GdxRuntimeException("Unable to create SpriteBatchModel unknown type: " + spriteSystemType);
        }
    }

    private static MultiPageSpriteBatchModel<LimitedCapacitySpriteRenderableModel> createMultiPageSpriteBatchModel(
            final SpriteBatchComponent spriteBatch,
            final VertexAttributes vertexAttributes, final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources,
            final GraphModels graphModels, final String tag, final SpriteModel spriteModel,
            final WritablePropertyContainer propertyContainer) {
        final PreserveMinimumDisposableProducer<LimitedCapacitySpriteRenderableModel> preserveMinimum = new PreserveMinimumDisposableProducer<>(spriteBatch.getMinimumPages(),
                new DisposableProducer<LimitedCapacitySpriteRenderableModel>() {
                    @Override
                    public LimitedCapacitySpriteRenderableModel create() {
                        SpriteStorage<RenderableSprite> spriteStorage = new ContinuousSlotsSpriteStorage<>(
                                spriteBatch.getSpritesPerPage(),
                                new DefaultSpriteSerializer(vertexAttributes, vertexPropertySources, spriteModel));
                        LimitedCapacitySpriteRenderableModel model = new LimitedCapacitySpriteRenderableModel(
                                spriteBatch.isStaticBatch(), spriteStorage,
                                vertexAttributes, propertyContainer, spriteModel);
                        graphModels.addModel(tag, model);
                        return model;
                    }

                    @Override
                    public void dispose(LimitedCapacitySpriteRenderableModel model) {
                        graphModels.removeModel(tag, model);
                        model.dispose();
                    }
                });
        return new MultiPageSpriteBatchModel<LimitedCapacitySpriteRenderableModel>(preserveMinimum, propertyContainer) {
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
        SpriteBatchModel spritesModel = spriteBatchMap.remove(spriteSystemComponent.getName());
        spritesModel.dispose();
    }

    @Override
    protected void processSystem() {
        for (Entity newSpriteEntity : newSpriteBatchEntities) {
            SpriteBatchComponent spriteSystem = spriteBatchComponentMapper.get(newSpriteEntity);
            GraphModels graphModels = pipelineRendererSystem.getPluginData(GraphModels.class);

            String tag = spriteSystem.getRenderTag();
            SpriteBatchModel spriteModel = createSpriteBatchModel(spriteSystem, graphModels, tag);

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
        for (SpriteBatchModel spriteModel : spriteBatchMap.values()) {
            spriteModel.dispose();
        }
        spriteBatchMap.clear();
    }
}
