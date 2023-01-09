package com.gempukku.libgdx.graph.test.component;

import com.artemis.Component;

public class PlayerInputControlledComponent extends Component {
    private String currentState;
    private boolean currentlyFacingRight;
    private float runVelocity;
    private float jumpVelocity;

    public float getRunVelocity() {
        return runVelocity;
    }

    public void setRunVelocity(float runVelocity) {
        this.runVelocity = runVelocity;
    }

    public float getJumpVelocity() {
        return jumpVelocity;
    }

    public void setJumpVelocity(float jumpVelocity) {
        this.jumpVelocity = jumpVelocity;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public boolean isCurrentlyFacingRight() {
        return currentlyFacingRight;
    }

    public void setCurrentlyFacingRight(boolean currentlyFacingRight) {
        this.currentlyFacingRight = currentlyFacingRight;
    }
}
