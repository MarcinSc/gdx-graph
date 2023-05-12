package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.GraphResolver;
import com.gempukku.libgdx.graph.ui.GraphResolverHolder;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

import java.util.ArrayList;

public class ShaderGraphEditorPart extends VisTable implements GraphNodeEditorPart {
    private static final int TAG_WIDTH = 110;
    private static final int NAME_WIDTH = 110;
    private static final int BUTTONS_WIDTH = 100;
    private final VerticalGroup shaderGroup;
    private final UIGraphType graphType;
    private final ArrayList<ShaderInfo> shaders = new ArrayList<>();

    private int updateCounter = 0;

    public ShaderGraphEditorPart(UIGraphType graphType) {
        this.graphType = graphType;
        shaderGroup = new VerticalGroup();
        shaderGroup.top();
        shaderGroup.grow();

        VisTable table = new VisTable();
        table.setDebug(true);
        table.add(new VisLabel("Tag", "gdx-graph-property-label")).width(TAG_WIDTH);
        table.add(new VisLabel("Name", "gdx-graph-property-label")).width(NAME_WIDTH);
        table.add(new VisLabel("Actions", "gdx-graph-property-label")).width(BUTTONS_WIDTH);
        table.add(new VisLabel("", "gdx-graph-property-label")).growX();
        table.row();

        VisScrollPane scrollPane = new VisScrollPane(shaderGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        table.add(scrollPane).colspan(3).grow().row();

        final VisTextButton newShader = new VisTextButton("New Shader", "gdx-graph-property-label");
        newShader.addListener(new NewShaderListener());

        VisTable buttons = new VisTable();
        buttons.add(newShader);
        table.add(buttons).colspan(3).growX().row();

        add(table).grow().row();
    }

    @Override
    public void act(float delta) {
        if (++updateCounter == 10) {
            updateCounter = 0;
            updateGraphs();
        }

        super.act(delta);
    }

    @Override
    public float getPrefWidth() {
        return 350;
    }

    @Override
    public float getPrefHeight() {
        return 200;
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    private void addShaderGraph(String path, String tag) {
        ShaderInfo shaderInfo = new ShaderInfo(path, tag);
        shaders.add(shaderInfo);
        shaderGroup.addActor(shaderInfo.getActor());
    }

    private void moveShaderUp(ShaderInfo shaderInfo) {
        int shaderIndex = shaders.indexOf(shaderInfo);
        if (shaderIndex > 0) {
            ShaderInfo otherShader = shaders.get(shaderIndex - 1);
            Actor nodeActor = shaderGroup.getChild(shaderIndex);
            Actor otherActor = shaderGroup.getChild(shaderIndex - 1);

            shaders.remove(shaderInfo);
            shaders.remove(otherShader);

            shaderGroup.removeActor(nodeActor);
            shaderGroup.removeActor(otherActor);

            shaders.add(shaderIndex - 1, shaderInfo);
            shaders.add(shaderIndex, otherShader);

            shaderGroup.addActorAt(shaderIndex - 1, nodeActor);
            shaderGroup.addActorAt(shaderIndex, otherActor);

            updateGraphs();
        }
    }

    private void moveShaderDown(ShaderInfo shaderInfo) {
        int shaderIndex = shaders.indexOf(shaderInfo);
        if (shaderIndex < shaders.size() - 1) {
            moveShaderUp(shaders.get(shaderIndex + 1));
        }
    }

    private void removeShaderGraph(ShaderInfo shaderInfo) {
        shaders.remove(shaderInfo);
        shaderGroup.removeActor(shaderInfo.getActor());
    }

    private void updateGraphs() {
        int shaderSize = shaders.size();
        for (int i = 0; i < shaderSize; i++) {
            shaders.get(i).update(i == 0, i == shaderSize - 1);
        }
    }

    public void initialize(JsonValue data) {
        if (data != null) {
            JsonValue shaderArray = data.get("shaders");
            for (JsonValue shaderObject : shaderArray) {
                String path = shaderObject.getString("path");
                String tag = shaderObject.getString("tag");
                addShaderGraph(path, tag);
            }
            updateGraphs();
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        JsonValue shaderArray = new JsonValue(JsonValue.ValueType.array);
        for (ShaderInfo shader : shaders) {
            JsonValue shaderObject = new JsonValue(JsonValue.ValueType.object);
            shaderObject.addChild("path", new JsonValue(shader.getPath()));
            shaderObject.addChild("tag", new JsonValue(shader.getTag()));
            shaderArray.addChild(shaderObject);
        }

        object.addChild("shaders", shaderArray);
    }

    private class NewShaderListener extends ChangeListener {
        @Override
        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
            PopupMenu popupMenu = new PopupMenu();

            for (GraphResolver.GraphInformation graphInformation : GraphResolverHolder.graphResolver.getGraphsByType(graphType.getType())) {
                MenuItem menuItem = new MenuItem(graphInformation.getName());
                popupMenu.addItem(menuItem);
                menuItem.addListener(
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                addShaderGraph(graphInformation.getPath(), graphInformation.getName());
                                updateGraphs();
                            }
                        });
            }

            popupMenu.showMenu(getStage(), actor);
        }
    }

    private static void setButtonDisabled(Button button, boolean disabled) {
        button.setDisabled(disabled);
        button.setTouchable(disabled ? Touchable.disabled : Touchable.enabled);
    }

    private class ShaderInfo {
        private final String path;
        private final VisImageButton upButton;
        private final VisImageButton downButton;
        private final VisImageButton editButton;
        private VisTable table;
        private VisTextField textField;
        private VisLabel name;

        public ShaderInfo(final String path, String tag) {
            this.path = path;
            table = new VisTable();

            textField = new VisTextField(tag, "gdx-graph-property");
            textField.setMessageText("Shader Tag");
            table.add(textField).width(TAG_WIDTH);

            name = new VisLabel("", "gdx-graph-property-label");
            name.setEllipsis(true);
            table.add(name).width(NAME_WIDTH);

            Skin skin = VisUI.getSkin();

            upButton = new VisImageButton(skin.getDrawable("graph-icon-up"), "Move up");
            upButton.getStyle().imageDisabled = skin.getDrawable("graph-icon-up-disabled");
            upButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            moveShaderUp(ShaderInfo.this);
                        }
                    });
            table.add(upButton).width(BUTTONS_WIDTH/4f);
            downButton = new VisImageButton(skin.getDrawable("graph-icon-down"), "Move down");
            downButton.getStyle().imageDisabled = skin.getDrawable("graph-icon-down-disabled");
            downButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            moveShaderDown(ShaderInfo.this);
                        }
                    });
            table.add(downButton).width(BUTTONS_WIDTH/4f);

            editButton = new VisImageButton(skin.getDrawable("graph-icon-edit"), "Edit");
            editButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            editButton.fire(new RequestGraphOpen(path));
                        }
                    });
            table.add(editButton).width(BUTTONS_WIDTH/4f);
            final VisImageButton deleteButton = new VisImageButton(skin.getDrawable("graph-icon-delete"), "Delete");
            deleteButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            removeShaderGraph(ShaderGraphEditorPart.ShaderInfo.this);
                        }
                    });
            table.add(deleteButton).width(BUTTONS_WIDTH/4f);
            table.row();
        }

        public void update(boolean isFirst, boolean isLast) {
            setButtonDisabled(upButton, isFirst);
            setButtonDisabled(downButton, isLast);

            GraphResolver.GraphInformation graph = GraphResolverHolder.graphResolver.findGraphByPath(path);
            setButtonDisabled(editButton, graph == null);

            String graphName = (graph != null) ? graph.getName() : "Not found";
            name.setText(graphName);
            name.setColor(graph != null ? Color.WHITE : Color.RED);
        }


        public VisTable getActor() {
            return table;
        }

        public String getPath() {
            return path;
        }

        public String getTag() {
            return textField.getText();
        }
    }
}
