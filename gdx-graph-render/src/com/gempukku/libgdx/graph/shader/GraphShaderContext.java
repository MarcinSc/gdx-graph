package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.shader.property.ShaderPropertySource;

public interface GraphShaderContext {
    ShaderPropertySource getPropertySource(String name);

    void addManagedResource(Disposable disposable);
}
