package com.gempukku.libgdx.graph.ui.shader;

import com.gempukku.libgdx.graph.shader.common.sprite.BillboardSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.common.sprite.ScreenSpriteShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.effect.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.arithmetic.*;
import com.gempukku.libgdx.graph.shader.config.common.math.common.*;
import com.gempukku.libgdx.graph.shader.config.common.math.exponential.*;
import com.gempukku.libgdx.graph.shader.config.common.math.geometric.*;
import com.gempukku.libgdx.graph.shader.config.common.math.trigonometry.*;
import com.gempukku.libgdx.graph.shader.config.common.math.utility.DistanceFromPlaneShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.math.value.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.noise.*;
import com.gempukku.libgdx.graph.shader.config.common.provided.*;
import com.gempukku.libgdx.graph.shader.config.common.shape.*;
import com.gempukku.libgdx.graph.shader.config.common.texture.BorderDetectionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.texture.UVTilingAndOffsetShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.common.value.*;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.ScreenPreviewShaderGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherColorShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.GradientShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.common.ConditionalShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapValueShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapVectorShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.*;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.SceneColorShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.TimeShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.texture.UVFlipbookShaderEditorProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UICommonShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphEditorProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new LinkedHashMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphEditorProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    static {
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new Sampler2DShaderNodeConfiguration(), "color", 150, 150));
        register(new UVFlipbookShaderEditorProducer());
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new UVTilingAndOffsetShaderNodeConfiguration(), "output", 150, 150));
        register(new GdxGraphNodeEditorProducer(new BorderDetectionShaderNodeConfiguration()));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new AddShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SubtractShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new OneMinusShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new MultiplyShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DivideShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ReciprocalShaderNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new PowerShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ExponentialShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ExponentialBase2ShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new NaturalLogarithmShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new LogarithmBase2ShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SquareRootShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new InverseSquareRootShaderNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SinShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new CosShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new TanShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArcsinShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArccosShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ArctanShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new Arctan2ShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new RadiansShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DegreesShaderNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new AbsShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SignShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new FloorShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new CeilingShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new FractionalPartShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ModuloShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new MinimumShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new MaximumShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new ClampShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SaturateShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new LerpShaderNodeConfiguration(), "output", 150, 150));
        register(new ConditionalShaderEditorProducer());
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new StepShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SmoothstepShaderNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new LengthShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DistanceShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DotProductShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new CrossProductShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new NormalizeShaderNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DistanceFromPlaneShaderNodeConfiguration(), "output", 150, 150));

        register(new GdxGraphNodeEditorProducer(new SplitShaderNodeConfiguration()));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new MergeShaderNodeConfiguration(), "color", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new RemapShaderNodeConfiguration(), "output", 150, 150));
        register(new RemapVectorShaderEditorProducer());
        register(new RemapValueShaderEditorProducer());

        register(new DitherShaderEditorProducer());
        register(new DitherColorShaderEditorProducer());
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new IntensityShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new FresnelEffectShaderNodeConfiguration(), "output", 150, 150));
        register(new GradientShaderEditorProducer());

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SimplexNoise2DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new SimplexNoise3DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new PerlinNoise2DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new PerlinNoise3DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiDistance2DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiDistance3DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiBorder2DNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new VoronoiBorder3DNodeConfiguration(), "output", 150, 150));

        register(new ScreenPreviewShaderGraphNodeEditorProducer(new DotShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new CheckerboardShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new EllipseShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new RectangleShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new ScreenPreviewShaderGraphNodeEditorProducer(new StarShapeShaderNodeConfiguration(), "output", 150, 150));

        register(new TimeShaderEditorProducer());
        register(new GdxGraphNodeEditorProducer(new CameraPositionShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CameraDirectionShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CameraViewportSizeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FragmentCoordinateShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SceneDepthShaderNodeConfiguration()));
        register(new SceneColorShaderEditorProducer());
        register(new GdxGraphNodeEditorProducer(new ScreenPositionShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new PixelSizeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ViewportSizeShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new BillboardSpriteShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ScreenSpriteShaderNodeConfiguration()));

        register(new ValueColorEditorProducer(new ValueColorShaderNodeConfiguration()));
        register(new ValueFloatEditorProducer(new ValueFloatShaderNodeConfiguration()));
        register(new ValueVector2EditorProducer(new ValueVector2ShaderNodeConfiguration()));
        register(new ValueVector3EditorProducer(new ValueVector3ShaderNodeConfiguration()));
        register(new ValueBooleanEditorProducer(new ValueBooleanShaderNodeConfiguration()));

        registerPropertyType(new PropertyFloatEditorDefinition());
        registerPropertyType(new PropertyVector2EditorDefinition());
        registerPropertyType(new PropertyVector3EditorDefinition());
        registerPropertyType(new PropertyColorEditorDefinition());
        registerPropertyType(new PropertyMatrix4EditorDefinition());
        registerPropertyType(new PropertyTextureEditorDefinition());
    }

    @Override
    public Iterable<MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphEditorProducers.values();
    }

    @Override
    public Map<String, PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}
