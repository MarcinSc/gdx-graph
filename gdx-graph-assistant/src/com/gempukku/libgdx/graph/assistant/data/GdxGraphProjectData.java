package com.gempukku.libgdx.graph.assistant.data;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.ui.GraphResolver;

public class GdxGraphProjectData implements GraphResolver {
    private final Array<GdxGraphData> graphs = new Array<>();

    public Array<GdxGraphData> getGraphs() {
        return graphs;
    }

    public GdxGraphData findGraphByName(String name) {
        for (GdxGraphData graph : graphs) {
            if (graph.getName().equals(name))
                return graph;
        }
        return null;
    }

    @Override
    public GdxGraphData findGraphByPath(String path) {
        for (GdxGraphData graph : graphs) {
            if (graph.getPath().equals(path))
                return graph;
        }
        return null;
    }

    @Override
    public Iterable<? extends GraphInformation> getGraphsByType(String graphType) {
        Array<GraphInformation> result = new Array<>();
        for (GdxGraphData graph : graphs) {
            if (graph.getGraphType().equals(graphType))
                result.add(graph);
        }

        return result;
    }
}
