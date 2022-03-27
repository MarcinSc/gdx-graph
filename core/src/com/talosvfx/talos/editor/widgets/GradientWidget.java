/*******************************************************************************
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.talosvfx.talos.editor.widgets;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.talosvfx.talos.editor.widgets.ui.GradientImage;
import com.talosvfx.talos.runtime.values.ColorPoint;

import java.util.Comparator;

public class GradientWidget extends Group {
    private Array<ColorPoint> points = new Array<>();

    private Vector2 areaPos = new Vector2();
    private Vector2 areaSize = new Vector2();

    private GradientWidgetListener listener;

    private GradientImage gradientImage;

    private Color tmpColor = new Color();

    private final String TRIANGLE = "slider-knob";
    private Comparator<ColorPoint> comparator = new Comparator<ColorPoint>() {
        @Override
        public int compare(ColorPoint o1, ColorPoint o2) {
            if (o1.pos < o2.pos)
                return -1;
            if (o1.pos > o2.pos)
                return 1;

            return 0;
        }
    };

    public interface GradientWidgetListener {
        void colorPickerShow(ColorPoint point, Runnable onSuccess);
    }

    public GradientWidget() {
        gradientImage = new GradientImage();
        addActor(gradientImage);

        addListener(new ClickListener() {

            private int draggingPoint = -1;

            private long clickTime;

            private boolean justRemoved = false;

            public int hit(float x, float y) {
                for (int i = 0; i < points.size; i++) {
                    ColorPoint colorPoint = points.get(i);
                    float pos = areaPos.x + areaSize.x * colorPoint.pos;

                    if (Math.abs(x - pos) < 10) {
                        return i;
                    }
                }

                return -1;
            }

            public void setPosToMouse(float x, float y) {
                float pos = (x - areaPos.x) / areaSize.x;

                float leftBound = 0f;
                float rightBound = 1f;

                if (points.size - 1 >= draggingPoint + 1) {
                    rightBound = points.get(draggingPoint + 1).pos;
                }
                if (draggingPoint > 0) {
                    leftBound = points.get(draggingPoint - 1).pos;
                }

                if (pos < leftBound) {
                    pos = leftBound;
                }
                if (pos > rightBound) {
                    pos = rightBound;
                }

                points.get(draggingPoint).pos = pos;
                updateGradientData();
            }


            private void doubleClick(float x, float y) {
                int hitIndex = hit(x, y);
                if (hitIndex >= 0) {
                    if (points.size > 1) {
                        removePoint(hitIndex);
                        justRemoved = true;
                    }
                }
            }

            private void rightClick(InputEvent event, float x, float y) {
                int hit = hit(x, y);
                if (hit >= 0) {
                    if (listener != null) {
                        listener.colorPickerShow(points.get(hit),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        updateGradientData();
                                    }
                                });
                    }
                    event.handle();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == Input.Buttons.RIGHT) {
                    event.handle();
                    return true;
                }

                long now = TimeUtils.millis();

                justRemoved = false;

                if (now - clickTime < 200 && button == 0) {
                    // this is a doubleClick
                    doubleClick(x, y);
                }

                clickTime = now;

                draggingPoint = hit(x, y);

                if (!justRemoved) {
                    if (draggingPoint == -1) {
                        float pos = (x - areaPos.x) / areaSize.x;
                        ColorPoint point = createPoint(getPosColor(pos), pos);
                        draggingPoint = points.indexOf(point, true);
                    } else {
                        setPosToMouse(x, y);
                    }
                }

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);


                if (draggingPoint >= 0) {
                    setPosToMouse(x, y);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (button == Input.Buttons.RIGHT) {
                    rightClick(event, x, y);

                    return;
                }

                draggingPoint = -1;
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
    }

    public Color getPosColor(float pos) {
        if (pos <= points.get(0).pos) {
            tmpColor.set(points.get(0).color);
        }

        if (pos >= points.get(points.size - 1).pos) {
            tmpColor.set(points.get(points.size - 1).color);
        }

        for (int i = 0; i < points.size - 1; i++) {
            if (points.get(i).pos < pos && points.get(i + 1).pos > pos) {
                // found it

                if (points.get(i + 1).pos == points.get(i).pos) {
                    tmpColor.set(points.get(i).color);
                } else {
                    float localAlpha = (pos - points.get(i).pos) / (points.get(i + 1).pos - points.get(i).pos);
                    tmpColor.r = Interpolation.linear.apply(points.get(i).color.r, points.get(i + 1).color.r, localAlpha);
                    tmpColor.g = Interpolation.linear.apply(points.get(i).color.g, points.get(i + 1).color.g, localAlpha);
                    tmpColor.b = Interpolation.linear.apply(points.get(i).color.b, points.get(i + 1).color.b, localAlpha);
                }
                break;
            }
        }

        return tmpColor;
    }

    public void setListener(GradientWidgetListener listener) {
        this.listener = listener;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        areaPos.set(7, 8);
        areaSize.set(getWidth() - 12, getHeight() - 8);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        gradientImage.setPosition(getX() + areaPos.x, getY() + areaPos.y);
        gradientImage.setSize(areaSize.x, areaSize.y);
        gradientImage.draw(batch, parentAlpha);

        drawPoints(batch, parentAlpha);
    }

    public ColorPoint createPoint(Color color, float pos) {
        ColorPoint colorPoint = new ColorPoint();
        colorPoint.pos = pos;
        colorPoint.color.set(color);
        points.add(colorPoint);

        updateGradientData();

        return colorPoint;
    }

    public void removePoint(int index) {
        if (points.size <= 1)
            return;
        points.removeIndex(index);
        updateGradientData();
    }

    public Array<ColorPoint> getPoints() {
        return points;
    }

    private void updateGradientData() {
        points.sort(comparator);
        gradientImage.setPoints(points);
        fire(new ChangeListener.ChangeEvent());
    }

    private void drawPoints(Batch batch, float parentAlpha) {
        for (int i = 0; i < points.size; i++) {
            batch.setColor(points.get(i).color);
            batch.draw(WhitePixel.sharedInstance.texture, getX() + areaPos.x + areaSize.x * points.get(i).pos - 6f, getY() + areaPos.y - 8f, 12f, 8f);
        }
    }
}