package com.gempukku.libgdx.graph.artemis.patchwork;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
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
import com.gempukku.libgdx.graph.shader.GraphModels;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;
import com.gempukku.libgdx.graph.util.model.GraphModelUtil;
import com.gempukku.libgdx.graph.util.patchwork.PatchReference;
import com.gempukku.libgdx.graph.util.patchwork.RenderablePatch;
import com.gempukku.libgdx.graph.util.patchwork.storage.PatchMemoryMesh;
import com.gempukku.libgdx.graph.util.patchwork.storage.PatchSerializer;
import com.gempukku.libgdx.graph.util.property.HierarchicalPropertyContainer;
import com.gempukku.libgdx.graph.util.storage.*;
import com.gempukku.libgdx.lib.artemis.evaluate.EvaluatePropertySystem;

public class PatchworkSystem extends BaseEntitySystem {
    private EvaluatePropertySystem evaluatePropertySystem;
    private PipelineRendererSystem pipelineRendererSystem;

    private ComponentMapper<PatchworkComponent> patchworkComponentMapper;

    private final ObjectMap<String, MultiPartBatchModel<RenderablePatch, PatchReference>> patchworkMap = new ObjectMap<>();

    private final Array<Entity> newPatchworkEntities = new Array<>();

    public PatchworkSystem() {
        super(Aspect.all(PatchworkComponent.class));
    }

    @Override
    protected void inserted(int entityId) {
        newPatchworkEntities.add(world.getEntity(entityId));
    }

    @Override
    protected void removed(int entityId) {
        PatchworkComponent spriteSystemComponent = patchworkComponentMapper.get(entityId);
        MultiPartBatchModel<RenderablePatch, PatchReference> spritesModel = patchworkMap.remove(spriteSystemComponent.getName());
        spritesModel.dispose();
    }

    public MultiPartBatchModel<RenderablePatch, PatchReference> getPatchworkModel(String patchworkName) {
        return patchworkMap.get(patchworkName);
    }

    @Override
    protected void processSystem() {
        for (Entity newPatchworkEntity : newPatchworkEntities) {
            PatchworkComponent patchwork = patchworkComponentMapper.get(newPatchworkEntity);
            GraphModels graphModels = pipelineRendererSystem.getPluginData(GraphModels.class);

            String tag = patchwork.getRenderTag();
            WritablePropertyContainer propertyContainer = new MapWritablePropertyContainer();
            MultiPartBatchModel<RenderablePatch, PatchReference> patchworkModel = createPatchworkBatchModel(patchwork, graphModels, tag, propertyContainer);

            for (ObjectMap.Entry<String, Object> property : patchwork.getProperties()) {
                propertyContainer.setValue(property.key, evaluatePropertySystem.evaluateProperty(newPatchworkEntity, property.value, Object.class));
            }

            patchworkMap.put(patchwork.getName(), patchworkModel);
        }
        newPatchworkEntities.clear();
    }

    private MultiPartBatchModel<RenderablePatch, PatchReference> createPatchworkBatchModel(
            final PatchworkComponent patchwork, final GraphModels graphModels, final String tag,
            final WritablePropertyContainer propertyContainer) {
        PatchworkComponent.SystemType patchworkSystemType = patchwork.getType();

        final VertexAttributes vertexAttributes = GraphModelUtil.getShaderVertexAttributes(graphModels, tag);
        final ObjectMap<VertexAttribute, ShaderPropertySource> vertexPropertySources = GraphModelUtil.getPropertySourceMap(graphModels, tag, vertexAttributes);

        final PatchSerializer patchSerializer = new PatchSerializer(vertexAttributes, vertexPropertySources);

        if (patchworkSystemType == PatchworkComponent.SystemType.TexturePaged) {
            return new TexturePagedMultiPartBatchModel<>(graphModels, tag,
                    new DisposableProducer<MultiPartBatchModel<RenderablePatch, PatchReference>>() {
                        @Override
                        public MultiPartBatchModel<RenderablePatch, PatchReference> create() {
                            HierarchicalPropertyContainer texturePropertyContainer = new HierarchicalPropertyContainer(propertyContainer);
                            return createMultiPagePatchworkModel(patchwork, vertexAttributes, patchSerializer, graphModels, tag,
                                    texturePropertyContainer);
                        }

                        @Override
                        public void dispose(MultiPartBatchModel<RenderablePatch, PatchReference> disposable) {
                            disposable.dispose();
                        }
                    });
        } else if (patchworkSystemType == PatchworkComponent.SystemType.MultiPaged) {
            return createMultiPagePatchworkModel(patchwork, vertexAttributes, patchSerializer, graphModels, tag, propertyContainer);
        } else {
            throw new GdxRuntimeException("Unable to create PatchworkModel, unknown type: " + patchworkSystemType);
        }
    }

    private PagedMultiPartBatchModel<RenderablePatch, PatchReference> createMultiPagePatchworkModel(
            final PatchworkComponent patchwork,
            final VertexAttributes vertexAttributes, final PatchSerializer patchSerializer,
            final GraphModels graphModels, final String tag,
            final WritablePropertyContainer propertyContainer) {
        DisposableProducer<MultiPartRenderableModel<RenderablePatch, PatchReference>> renderableProducer =
                new DisposableProducer<MultiPartRenderableModel<RenderablePatch, PatchReference>>() {
                    @Override
                    public MultiPartRenderableModel<RenderablePatch, PatchReference> create() {
                        PatchMemoryMesh<RenderablePatch> multiPartMemoryMesh =
                                new PatchMemoryMesh<>(
                                        patchwork.getIndexCapacityPerPage(), patchwork.getVertexCapacityPerPage(), patchSerializer);
                        GdxMeshRenderableModel meshModel =
                                new GdxMeshRenderableModel(
                                        patchwork.isStaticBatch(), multiPartMemoryMesh, vertexAttributes, propertyContainer, tag);
                        DefaultMultiPartRenderableModel<RenderablePatch, PatchReference> model =
                                new DefaultMultiPartRenderableModel<>(multiPartMemoryMesh, meshModel);
                        graphModels.addModel(model);
                        return model;
                    }

                    @Override
                    public void dispose(MultiPartRenderableModel<RenderablePatch, PatchReference> model) {
                        graphModels.removeModel(model);
                        model.dispose();
                    }
                };
        final PreserveMinimumDisposableProducer<MultiPartRenderableModel<RenderablePatch, PatchReference>> preserveMinimum =
                new PreserveMinimumDisposableProducer<>(patchwork.getMinimumPages(), renderableProducer);
        return new PagedMultiPartBatchModel<RenderablePatch, PatchReference>(preserveMinimum, propertyContainer) {
            @Override
            public void dispose() {
                super.dispose();
                preserveMinimum.dispose();
            }
        };
    }


    @Override
    public void dispose() {
        for (MultiPartBatchModel<RenderablePatch, PatchReference> patchworkModel : patchworkMap.values()) {
            patchworkModel.dispose();
        }
        patchworkMap.clear();
    }
}