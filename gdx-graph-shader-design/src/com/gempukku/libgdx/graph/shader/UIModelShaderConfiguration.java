package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModelProducer;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;

import java.util.*;

public class UIModelShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, ShaderPropertyEditorDefinition> propertyProducers = new LinkedHashMap<>();
    private static Map<String, List<String>> propertyTypeToFunctions = new HashMap<>();
    private static Map<String, Producer<? extends PreviewRenderableModelProducer>> previewModels = new LinkedHashMap<>();
    private static Producer<? extends PreviewRenderableModelProducer> screenPreviewModel;
    private static Producer<? extends PreviewRenderableModelProducer> spherePreviewModel;

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(ShaderPropertyEditorDefinition propertyEditorDefinition) {
        String propertyType = propertyEditorDefinition.getType();
        if (!propertyTypeToFunctions.containsKey(propertyType)) {
            propertyTypeToFunctions.put(propertyType, new ArrayList<>());
        }
        propertyEditorDefinition.setPropertyFunctions(propertyTypeToFunctions.get(propertyType));
        propertyProducers.put(propertyType, propertyEditorDefinition);
    }

    public static void registerPropertyFunction(String propertyType, String function) {
        if (!propertyTypeToFunctions.containsKey(propertyType)) {
            propertyTypeToFunctions.put(propertyType, new ArrayList<>());
        }
        List<String> propertyTypeFunctions = propertyTypeToFunctions.get(propertyType);
        propertyTypeFunctions.add(function);
        propertyTypeFunctions.sort(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
        ShaderPropertyEditorDefinition propertyEditorDefinition = propertyProducers.get(propertyType);
        if (propertyEditorDefinition != null)
            propertyEditorDefinition.setPropertyFunctions(propertyTypeFunctions);
    }

    public static void registerPreviewModel(String displayName, Producer<? extends PreviewRenderableModelProducer> previewRenderableModelSupplier) {
        previewModels.put(displayName, previewRenderableModelSupplier);
    }

    public static void setScreenPreviewModel(Producer<? extends PreviewRenderableModelProducer> screenPreviewModel) {
        UIModelShaderConfiguration.screenPreviewModel = screenPreviewModel;
    }

    public static void setSpherePreviewModel(Producer<? extends PreviewRenderableModelProducer> screenPreviewModel) {
        UIModelShaderConfiguration.spherePreviewModel = screenPreviewModel;
    }

    public static Producer<? extends PreviewRenderableModelProducer> getScreenPreviewModel() {
        return screenPreviewModel;
    }

    public static Producer<? extends PreviewRenderableModelProducer> getSpherePreviewModel() {
        return spherePreviewModel;
    }

    public static Map<String, Producer<? extends PreviewRenderableModelProducer>> getPreviewModelSuppliers() {
        return previewModels;
    }

    @Override
    public Iterable<? extends MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, ShaderPropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}
