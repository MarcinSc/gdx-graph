package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.shader.property.PropertyLocation;

public abstract class GraphType {
    private String type;
    private boolean exportable;

    protected GraphType(String type, boolean exportable) {
        this.type = type;
        this.exportable = exportable;
    }

    public String getType() {
        return type;
    }

    public boolean isExportable() {
        return exportable;
    }

    public abstract PropertyLocation[] getPropertyLocations();
}
