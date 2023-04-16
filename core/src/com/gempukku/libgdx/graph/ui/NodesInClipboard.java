package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.ui.graph.data.GraphConnection;
import com.gempukku.libgdx.ui.graph.data.GraphNode;

public class NodesInClipboard {
    public GraphType graphType;
    public Array<NodesData> nodesData;
    public Array<GraphConnection> graphConnections;

    static class NodesData {
        public GraphNode graphNode;
        public float x;
        public float y;

        public NodesData(GraphNode graphNode, float x, float y) {
            this.graphNode = graphNode;
            this.x = x;
            this.y = y;
        }
    }
}
