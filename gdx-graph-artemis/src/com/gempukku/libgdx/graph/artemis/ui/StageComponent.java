package com.gempukku.libgdx.graph.artemis.ui;

import com.artemis.Component;

public class StageComponent extends Component {
    private String stageName;
    private boolean inputProcessing = true;

    public String getStageName() {
        return stageName;
    }

    public boolean isInputProcessing() {
        return inputProcessing;
    }
}
