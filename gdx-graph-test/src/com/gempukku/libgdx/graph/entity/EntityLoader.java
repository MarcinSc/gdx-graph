package com.gempukku.libgdx.graph.entity;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class EntityLoader {
    public static Entity readEntity(Engine engine, Json json, String path) {
        EntityDef spriteDef = json.fromJson(EntityDef.class, Gdx.files.internal(path));

        return createEntity(engine, spriteDef);
    }

    private static Entity createEntity(Engine engine, EntityDef entityDef) {
        Entity entity = engine.createEntity();

        for (Component component : entityDef.getComponents()) {
            entity.add(component);
        }
        engine.addEntity(entity);

        return entity;
    }
}
