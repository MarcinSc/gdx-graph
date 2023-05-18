package com.gempukku.libgdx.graph.ui.part;

import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedAware;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.editor.part.CollapsibleSectionEditorPart;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageTextButton;

public class GraphAwareCollapsibleSectionEditorPart extends CollapsibleSectionEditorPart implements GraphChangedAware {
    private final GraphNodeEditorPart graphNodeEditorPart;

    public GraphAwareCollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel) {
        super(property, graphNodeEditorPart, sectionLabel);
        this.graphNodeEditorPart = graphNodeEditorPart;
    }

    public GraphAwareCollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel, String imageTextButtonStyleName, String separatorStyleName) {
        super(property, graphNodeEditorPart, sectionLabel, imageTextButtonStyleName, separatorStyleName);
        this.graphNodeEditorPart = graphNodeEditorPart;
    }

    public GraphAwareCollapsibleSectionEditorPart(String property, GraphNodeEditorPart graphNodeEditorPart, String sectionLabel, VisImageTextButton.VisImageTextButtonStyle imageTextButtonStyle, Separator.SeparatorStyle separatorStyle) {
        super(property, graphNodeEditorPart, sectionLabel, imageTextButtonStyle, separatorStyle);
        this.graphNodeEditorPart = graphNodeEditorPart;
    }

    @Override
    public void graphChanged(GraphChangedEvent event, boolean hasErrors, GraphWithProperties graph) {
        if (graphNodeEditorPart instanceof GraphChangedAware) {
            ((GraphChangedAware) graphNodeEditorPart).graphChanged(event, hasErrors, graph);
        }
    }
}
