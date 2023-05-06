package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.common.Supplier;
import com.kotcrab.vis.ui.widget.color.ColorPicker;

public class ColorPickerSupplier implements Supplier<ColorPicker> {
    public static ColorPickerSupplier instance = null;
    private ColorPicker colorPicker;

    public static void initialize() {
        instance = new ColorPickerSupplier();
        instance.colorPicker = new ColorPicker();
    }

    public static void dispose() {
        instance.disposeColorPicker();
        instance = null;
    }

    @Override
    public ColorPicker get() {
        return colorPicker;
    }

    private void disposeColorPicker() {
        colorPicker.dispose();
    }
}
