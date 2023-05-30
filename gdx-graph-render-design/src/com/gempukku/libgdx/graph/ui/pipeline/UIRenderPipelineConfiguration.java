package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.pipeline.RendererConfiguration;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;

import java.util.Map;
import java.util.TreeMap;

public class UIRenderPipelineConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphEditorProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new TreeMap<>();
    private static ObjectMap<Class<? extends RendererConfiguration>,
            Producer<? extends RendererConfiguration>> previewConfigurationBuilders = new ObjectMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphEditorProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    public static <T extends RendererConfiguration> void registerPreviewConfigurationBuilder(Class<T> clazz, Producer<T> configurationProducer) {
        previewConfigurationBuilders.put(clazz, configurationProducer);
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphEditorProducers.values();
    }

    @Override
    public Map<String, ? extends PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }

    public static ObjectMap<Class<? extends RendererConfiguration>, Producer<? extends RendererConfiguration>> getPreviewConfigurationBuilders() {
        return previewConfigurationBuilders;
    }
}
