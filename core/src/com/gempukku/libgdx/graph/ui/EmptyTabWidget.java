package com.gempukku.libgdx.graph.ui;

import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class EmptyTabWidget extends VisTable {
    public EmptyTabWidget() {
        VisLabel label = new VisLabel("Credits:\nCurveWidget and GradientWidget credits go to authors of Talos project:\nhttps://github.com/rockbite/talos\nLicensed under: Apache License 2.0");
        add(label);
    }
}
