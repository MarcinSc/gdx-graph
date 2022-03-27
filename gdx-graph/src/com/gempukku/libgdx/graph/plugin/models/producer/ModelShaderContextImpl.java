package com.gempukku.libgdx.graph.plugin.models.producer;

import com.gempukku.libgdx.graph.pipeline.producer.rendering.producer.ShaderContextImpl;
import com.gempukku.libgdx.graph.plugin.PluginPrivateDataSource;
import com.gempukku.libgdx.graph.plugin.models.ModelShaderContext;
import com.gempukku.libgdx.graph.plugin.models.RenderableModel;

public class ModelShaderContextImpl extends ShaderContextImpl implements ModelShaderContext {
    private RenderableModel renderableModel;

    public ModelShaderContextImpl(PluginPrivateDataSource pluginPrivateDataSource) {
        super(pluginPrivateDataSource);
    }

    @Override
    public RenderableModel getRenderableModel() {
        return renderableModel;
    }

    public void setRenderableModel(RenderableModel renderableModel) {
        this.renderableModel = renderableModel;
    }
}
