package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.property.PropertySource;

public interface GraphShaderContext {
    PropertySource getPropertySource(String name);

    void addManagedResource(Disposable disposable);
}
