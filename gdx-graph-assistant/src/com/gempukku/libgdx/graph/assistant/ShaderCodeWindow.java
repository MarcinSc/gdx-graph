package com.gempukku.libgdx.graph.assistant;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.ui.tabbedpane.GDirtyTab;
import com.gempukku.libgdx.ui.tabbedpane.GDirtyTabIconLabel;
import com.gempukku.libgdx.ui.tabbedpane.GTabbedPane;
import com.kotcrab.vis.ui.widget.ScrollableTextArea;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;

public class ShaderCodeWindow extends VisWindow {
    public ShaderCodeWindow(GraphShader graphShader) {
        super("Generated shader", "resizable");
        setModal(true);

        GTabbedPane<SimpleTab> tabbedPane = new GTabbedPane<>();

        VisTable vertexShader = new VisTable();
        ScrollableTextArea vertexTextArea = new ScrollableTextArea(graphShader.getVertexShaderProgram());
        vertexShader.add(new VisScrollPane(vertexTextArea)).grow();

        VisTable fragmentShader = new VisTable();
        ScrollableTextArea fragmentTextArea = new ScrollableTextArea(graphShader.getFragmentShaderProgram());
        fragmentShader.add(new VisScrollPane(fragmentTextArea)).grow();

        tabbedPane.addTab(new SimpleTab(tabbedPane, "Vertex shader", vertexShader));
        tabbedPane.addTab(new SimpleTab(tabbedPane, "Fragment shader", fragmentShader));

        add(tabbedPane).grow();
        setSize(600, 400);
        setResizable(true);
        addCloseButton();
    }

    private class SimpleTab implements GDirtyTab {
        private GDirtyTabIconLabel<SimpleTab> tabActor;
        private Table content;

        public SimpleTab(GTabbedPane<SimpleTab> tabbedPane, String title, Table content) {
            this.content = content;
            tabActor = new GDirtyTabIconLabel<>(tabbedPane, this, title);
        }

        @Override
        public Actor getTabActor() {
            return tabActor;
        }

        @Override
        public Table getContentTable() {
            return content;
        }

        @Override
        public void tabAdded() {

        }

        @Override
        public void setActive(boolean b) {
            tabActor.setActive(b);
        }

        @Override
        public void tabClosed() {

        }

        @Override
        public boolean isDirty() {
            return false;
        }
    }
}
