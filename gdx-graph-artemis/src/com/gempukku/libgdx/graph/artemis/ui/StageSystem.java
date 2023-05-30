package com.gempukku.libgdx.graph.artemis.ui;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.artemis.renderer.PipelineRendererSystem;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.lib.artemis.camera.ScreenResized;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorProvider;

public class StageSystem extends EntitySystem implements InputProcessorProvider, Disposable {
    private PipelineRendererSystem pipelineRendererSystem;

    private int processorPriority;
    private SimpleUIRendererConfiguration simpleUIRendererConfiguration;

    private Array<Entity> addedEntities = new Array<>();
    private String inputProcessingStageId;
    private InputMultiplexer multiplexer = new InputMultiplexer();

    public StageSystem(int processorPriority) {
        super(Aspect.all(StageComponent.class));

        this.processorPriority = processorPriority;
    }

    @Override
    protected void initialize() {
        simpleUIRendererConfiguration = new SimpleUIRendererConfiguration();
        pipelineRendererSystem.addRendererConfiguration(UIRendererConfiguration.class, simpleUIRendererConfiguration);
    }

    @Override
    public void inserted(Entity e) {
        addedEntities.add(e);
    }

    @Override
    public void removed(Entity e) {
        addedEntities.removeValue(e, true);
        StageComponent stageComponent = e.getComponent(StageComponent.class);
        Stage stage = removeStage(stageComponent.getStageName());
        if (stage != null) {
            stage.dispose();
        }
    }

    public Stage getStage(String stageId) {
        return simpleUIRendererConfiguration.getStage(stageId);
    }

    @EventListener
    public void screenResized(ScreenResized screenResized, Entity entity) {
        for (Stage stage : simpleUIRendererConfiguration.getStageMap().values()) {
            stage.getViewport().update(screenResized.getWidth(), screenResized.getHeight(), true);
        }
    }

    @Override
    public int getInputPriority() {
        return processorPriority;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return multiplexer;
    }

    public void addStage(String stageId, Stage stage, boolean inputProcessing) {
        simpleUIRendererConfiguration.setStage(stageId, stage);
        if (inputProcessing)
            setInputProcessingStage(stageId);
    }

    public Stage removeStage(String stageId) {
        return simpleUIRendererConfiguration.removeStage(stageId);
    }

    public void setInputProcessingStage(String stageId) {
        multiplexer.clear();
        multiplexer.addProcessor(getStage(stageId));
    }

    @Override
    protected void processSystem() {
        if (addedEntities.size > 0) {
            for (Entity stageEntity : addedEntities) {
                StageComponent stageComponent = stageEntity.getComponent(StageComponent.class);

                Stage stage = new Stage(new ScreenViewport());
                addStage(stageComponent.getStageName(), stage, stageComponent.isInputProcessing());
            }
            addedEntities.clear();
        }

        for (Stage value : simpleUIRendererConfiguration.getStageMap().values()) {
            value.act(world.getDelta());
        }
    }

    @Override
    public void dispose() {
        for (Stage stage : simpleUIRendererConfiguration.getStageMap().values()) {
            stage.dispose();
        }
        simpleUIRendererConfiguration.removeAllStages();
    }
}
