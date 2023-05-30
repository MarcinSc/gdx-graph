package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeProvider;
import com.gempukku.libgdx.graph.ui.AssetResolver;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class GraphShaderBuilder {
    public static GraphShader buildTextShader(GraphWithProperties graph, TimeProvider timeProvider) {
        AssetResolver assetResolver = AssetResolver.instance;

        PipelineRendererConfiguration configuration = new PipelineRendererConfiguration(timeProvider, assetResolver, null, null);

        ObjectMap<Class<? extends RendererConfiguration>, Producer<? extends RendererConfiguration>> previewConfigurationBuilders = UIRenderPipelineConfiguration.getPreviewConfigurationBuilders();
        for (ObjectMap.Entry<Class<? extends RendererConfiguration>, Producer<? extends RendererConfiguration>> configurationProducer : previewConfigurationBuilders) {
            Class<RendererConfiguration> configurationClass = (Class<RendererConfiguration>) configurationProducer.key;
            RendererConfiguration rendererConfiguration = configurationProducer.value.create();
            configuration.setConfig(configurationClass, rendererConfiguration);
        }

        GraphShader graphShader = GraphTypeRegistry.findGraphType(graph.getType(), ShaderGraphType.class).buildGraphShader("", configuration, graph, false);

        configuration.dispose();

        return graphShader;
    }
}
