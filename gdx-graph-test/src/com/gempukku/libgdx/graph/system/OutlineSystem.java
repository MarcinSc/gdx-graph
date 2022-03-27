package com.gempukku.libgdx.graph.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.gempukku.libgdx.graph.component.OutlineComponent;
import com.gempukku.libgdx.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprites;

public class OutlineSystem extends EntitySystem {
    private ImmutableArray<Entity> outlineEntities;
    private final PipelineRenderer pipelineRenderer;

    public OutlineSystem(int priority, PipelineRenderer pipelineRenderer) {
        super(priority);
        this.pipelineRenderer = pipelineRenderer;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(OutlineComponent.class).get();
        outlineEntities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float delta) {
        GraphSprites graphSprites = pipelineRenderer.getPluginData(GraphSprites.class);
        for (Entity outlineEntity : outlineEntities) {
            OutlineComponent outlineComponent = outlineEntity.getComponent(OutlineComponent.class);
            SpriteComponent spriteComponent = outlineEntity.getComponent(SpriteComponent.class);
            if (outlineComponent.isDirty()) {
                String type = outlineComponent.getType();
                boolean outline = outlineComponent.hasOutline();
                if (type.equals("asTag")) {
                    if (outline) {
                        GraphSprite outlineSprite = graphSprites.addSprite("Outline", spriteComponent.getSprite());
                        spriteComponent.getGraphSprites().put("Outline", outlineSprite);
                    } else {
                        GraphSprite outlineSprite = spriteComponent.getGraphSprites().get("Outline");
                        if (outlineSprite != null)
                            graphSprites.removeSprite(outlineSprite);
                    }
                } else if (type.equals("asProperty")) {
                    spriteComponent.getSprite().setOutline(outline ? 1 : 0);
//
//                    graphSprites.setProperty(spriteComponent.getGraphSprite(), "Outline", outline ? 1 : 0);
                }

                outlineComponent.clean();
            }
        }
    }
}
