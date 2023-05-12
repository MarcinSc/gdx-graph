package com.gempukku.libgdx.graph.assistant.data;

import com.gempukku.libgdx.graph.ui.GraphResolver;

public class GdxGraphData implements GraphResolver.GraphInformation {
    private String name;
    private String graphType;
    private String path;

    public GdxGraphData() {
    }

    public GdxGraphData(String name, String graphType, String path) {
        this.name = name;
        this.graphType = graphType;
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGraphType() {
        return graphType;
    }

    @Override
    public String getPath() {
        return path;
    }
}
