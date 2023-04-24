package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.libgdx.common.Supplier;

public class RequestTabOpen extends Event {
    private String id;
    private String title;
    private Drawable icon;
    private Supplier<Table> contentSupplier;
    private Supplier<AssistantPluginTab> tabSupplier;

    public RequestTabOpen(String id, String title, Drawable icon, Supplier<Table> contentSupplier, Supplier<AssistantPluginTab> tabSupplier) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.contentSupplier = contentSupplier;
        this.tabSupplier = tabSupplier;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Supplier<AssistantPluginTab> getTabSupplier() {
        return tabSupplier;
    }

    public Supplier<Table> getContentSupplier() {
        return contentSupplier;
    }
}
