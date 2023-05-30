package com.gempukku.libgdx.graph.artemis.time;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;

public class TimeKeepingSystem extends BaseEntitySystem {
    private IntMap<DefaultTimeKeeper> timeKeepers = new IntMap<>();
    private ComponentMapper<TimeComponent> timeComponentMapper;

    public TimeKeepingSystem() {
        super(Aspect.all(TimeComponent.class));
    }

    protected void inserted(int entityId) {
        Gdx.app.debug("TimeKeepingSystem", "Located time keeper");
        TimeComponent time = timeComponentMapper.get(entityId);
        DefaultTimeKeeper timeKeeper = new DefaultTimeKeeper();
        timeKeeper.setTime(time.getTime());
        timeKeepers.put(entityId, timeKeeper);
    }

    @Override
    protected void removed(int entityId) {
        timeKeepers.remove(entityId);
    }

    @Override
    protected void processSystem() {
        float deltaTime = world.getDelta();
        for (IntMap.Entry<DefaultTimeKeeper> timeKeeper : timeKeepers) {
            timeKeeper.value.updateTime(deltaTime);
            timeComponentMapper.get(timeKeeper.key).setTime(timeKeeper.value.getTime());
        }
    }

    public DefaultTimeKeeper getTimeProvider(Entity entity) {
        return timeKeepers.get(entity.getId());
    }
}
