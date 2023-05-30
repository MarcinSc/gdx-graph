package com.gempukku.libgdx.graph.shader;

public interface ModelContainer<Model> {
    void addModel(Model model);
    void removeModel(Model model);

    Iterable<? extends Model> getModels();

    void removeAllModels();
}
