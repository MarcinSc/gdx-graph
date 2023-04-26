package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.gdx.assistant.plugin.AssistantPluginTab;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.ui.TabControl;

public class RequestTabOpen extends Event {
    private String id;
    private String title;
    private Drawable icon;
    private Supplier<Table> contentSupplier;
    private Function<TabControl, AssistantPluginTab> tabCreator;

    public RequestTabOpen(String id, String title, Drawable icon, Supplier<Table> contentSupplier, Function<TabControl, AssistantPluginTab> tabCreator) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.contentSupplier = contentSupplier;
        this.tabCreator = tabCreator;
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

    public Function<TabControl, AssistantPluginTab> getTabCreator() {
        return tabCreator;
    }

    public Supplier<Table> getContentSupplier() {
        return contentSupplier;
    }
}
