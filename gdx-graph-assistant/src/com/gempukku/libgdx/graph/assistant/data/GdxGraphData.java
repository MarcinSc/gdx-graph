package com.gempukku.libgdx.graph.assistant.data;

public class GdxGraphData {
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

    public String getName() {
        return name;
    }

    public String getGraphType() {
        return graphType;
    }

    public String getPath() {
        return path;
    }
}
