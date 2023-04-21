package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.ui.graph.editor.part.GraphNodeEditorPart;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

public abstract class ShaderGraphBoxPart extends VisTable implements GraphNodeEditorPart {
    private static final int EDIT_WIDTH = 40;
    private static final int REMOVE_WIDTH = 60;
    private final VerticalGroup shaderGroup;
    private ArrayList<ShaderInfo> shaders = new ArrayList<>();

    public ShaderGraphBoxPart() {
        shaderGroup = new VerticalGroup();
        shaderGroup.top();
        shaderGroup.grow();

        VisTable table = new VisTable();
        table.add(new VisLabel("Tag", "gdx-graph-property-label")).colspan(5).growX();
        table.row();

        VisScrollPane scrollPane = new VisScrollPane(shaderGroup);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        table.add(scrollPane).grow().row();

        final VisTextButton newShader = new VisTextButton("New Shader", "gdx-graph-property-label");
        newShader.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        UIGraphType graphType = getGraphType();
                        PopupMenu popupMenu = new PopupMenu();
                        for (final GraphTemplate graphTemplate : graphType.getGraphTemplates()) {
                            MenuItem menuItem = new MenuItem(graphTemplate.getTitle());
                            popupMenu.addItem(menuItem);
                            menuItem.addListener(
                                    new ChangeListener() {
                                        @Override
                                        public void changed(ChangeEvent event, Actor actor) {
                                            String id = UUID.randomUUID().toString().replace("-", "");
                                            addShaderGraph(id, "", graphTemplate.getGraph());
                                        }
                                    });
                        }
                        popupMenu.addSeparator();

                        MenuItem loadFromFile = new MenuItem("From file...");
                        popupMenu.addItem(loadFromFile);
                        loadFromFile.addListener(
                                new ChangeListener() {
                                    @Override
                                    public void changed(ChangeEvent event, Actor actor) {
                                        FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
                                        fileChooser.setModal(true);
                                        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                                        fileChooser.setListener(new FileChooserAdapter() {
                                            @Override
                                            public void selected(Array<FileHandle> file) {
                                                FileHandle selectedFile = file.get(0);
                                                JsonReader parser = new JsonReader();
                                                try {
                                                    InputStream is = selectedFile.read();
                                                    try {
                                                        JsonValue shader = parser.parse(new InputStreamReader(is));
                                                        String id = UUID.randomUUID().toString().replace("-", "");
                                                        addShaderGraph(id, selectedFile.nameWithoutExtension(), shader);
                                                    } finally {
                                                        is.close();
                                                    }
                                                } catch (IOException exp) {
                                                    // Ignored
                                                }
                                            }
                                        });
                                        getStage().addActor(fileChooser.fadeIn());
                                    }
                                });

                        popupMenu.showMenu(getStage(), newShader);
                    }
                });

        VisTable buttons = new VisTable();
        buttons.add(newShader);
        table.add(buttons).growX().row();

        add(table).grow().row();
    }

    protected abstract UIGraphType getGraphType();

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

    private void addShaderGraph(String id, String tag, JsonValue shader) {
        ShaderInfo shaderInfo = new ShaderInfo(id, tag, shader);
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

    public void initialize(JsonValue data) {
        if (data != null) {
            JsonValue shaderArray = data.get("shaders");
            for (JsonValue shaderObject : shaderArray) {
                String id = shaderObject.getString("id");
                String tag = shaderObject.getString("tag");
                JsonValue shader = shaderObject.get("shader");
                addShaderGraph(id, tag, shader);
            }
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        JsonValue shaderArray = new JsonValue(JsonValue.ValueType.array);
        for (ShaderInfo shader : shaders) {
            JsonValue shaderObject = new JsonValue(JsonValue.ValueType.object);
            shaderObject.addChild("id", new JsonValue(shader.getId()));
            shaderObject.addChild("tag", new JsonValue(shader.getTag()));
            GetSerializedGraph event = new GetSerializedGraph(shader.getId());
            fire(event);
            JsonValue shaderGraph = event.getGraph();
            if (shaderGraph == null)
                shaderGraph = shader.getInitialShaderJson();
            shaderObject.addChild("shader", shaderGraph);
            shaderArray.addChild(shaderObject);
        }

        object.addChild("shaders", shaderArray);
    }

    private class ShaderInfo {
        private String id;
        private JsonValue initialShaderJson;
        private VisTable table;
        private VisTextField textField;

        public ShaderInfo(final String id, String tag, final JsonValue initialShaderJson) {
            this.id = id;
            this.initialShaderJson = initialShaderJson;
            table = new VisTable();
            textField = new VisTextField(tag, "gdx-graph-property");
            textField.setMessageText("Shader Tag");
            table.add(textField).growX();

            VisTextButton upButton = new VisTextButton("Up", "gdx-graph-property-label");
            upButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            moveShaderUp(ShaderInfo.this);
                        }
                    });
            table.add(upButton).width(30);
            VisTextButton downButton = new VisTextButton("Dn", "gdx-graph-property-label");
            downButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            moveShaderDown(ShaderInfo.this);
                        }
                    });
            table.add(downButton).width(30);

            final VisTextButton editButton = new VisTextButton("Edit", "gdx-graph-property-label");
            editButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            editButton.fire(new RequestGraphOpen(id, "Shader - " + textField.getText(), initialShaderJson, getGraphType()));
                        }
                    });
            table.add(editButton).width(EDIT_WIDTH);
            final VisTextButton deleteButton = new VisTextButton("Remove", "gdx-graph-property-label");
            deleteButton.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            Dialogs.showOptionDialog(getStage(), "Confirm", "Would you like to remove the shader?",
                                    Dialogs.OptionDialogType.YES_CANCEL, new OptionDialogListener() {
                                        @Override
                                        public void yes() {
                                            fire(new GraphRemoved(id));
                                            removeShaderGraph(ShaderGraphBoxPart.ShaderInfo.this);
                                        }

                                        @Override
                                        public void no() {

                                        }

                                        @Override
                                        public void cancel() {

                                        }
                                    });
                        }
                    });
            table.add(deleteButton).width(REMOVE_WIDTH);
            table.row();
        }

        public String getId() {
            return id;
        }

        public VisTable getActor() {
            return table;
        }

        public String getTag() {
            return textField.getText();
        }

        public JsonValue getInitialShaderJson() {
            return initialShaderJson;
        }
    }
}
