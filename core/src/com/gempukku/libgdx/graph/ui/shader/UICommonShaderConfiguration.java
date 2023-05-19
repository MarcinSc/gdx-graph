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
import com.gempukku.libgdx.graph.ui.graph.PreviewShaderGraphNodeEditorProducer;
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
        register(new PreviewShaderGraphNodeEditorProducer(new Sampler2DShaderNodeConfiguration(), "color", 150, 150));
        register(new UVFlipbookShaderEditorProducer());
        register(new PreviewShaderGraphNodeEditorProducer(new UVTilingAndOffsetShaderNodeConfiguration(), "output", 150, 150));
        register(new GdxGraphNodeEditorProducer(new BorderDetectionShaderNodeConfiguration()));

        register(new PreviewShaderGraphNodeEditorProducer(new AddShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SubtractShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new OneMinusShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new MultiplyShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new DivideShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ReciprocalShaderNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new PowerShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ExponentialShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ExponentialBase2ShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new NaturalLogarithmShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new LogarithmBase2ShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SquareRootShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new InverseSquareRootShaderNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new SinShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new CosShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new TanShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ArcsinShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ArccosShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ArctanShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new Arctan2ShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new RadiansShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new DegreesShaderNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new AbsShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SignShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new FloorShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new CeilingShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new FractionalPartShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ModuloShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new MinimumShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new MaximumShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new ClampShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SaturateShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new LerpShaderNodeConfiguration(), "output", 150, 150));
        register(new ConditionalShaderEditorProducer());
        register(new PreviewShaderGraphNodeEditorProducer(new StepShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SmoothstepShaderNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new LengthShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new DistanceShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new DotProductShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new CrossProductShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new NormalizeShaderNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new DistanceFromPlaneShaderNodeConfiguration(), "output", 150, 150));

        register(new GdxGraphNodeEditorProducer(new SplitShaderNodeConfiguration()));
        register(new PreviewShaderGraphNodeEditorProducer(new MergeShaderNodeConfiguration(), "color", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new RemapShaderNodeConfiguration(), "output", 150, 150));
        register(new RemapVectorShaderEditorProducer());
        register(new RemapValueShaderEditorProducer());

        register(new DitherShaderEditorProducer());
        register(new DitherColorShaderEditorProducer());
        register(new PreviewShaderGraphNodeEditorProducer(new IntensityShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new FresnelEffectShaderNodeConfiguration(), "output", 150, 150));
        register(new GradientShaderEditorProducer());

        register(new PreviewShaderGraphNodeEditorProducer(new SimplexNoise2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SimplexNoise3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new PerlinNoise2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new PerlinNoise3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiDistance2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiDistance3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiBorder2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiBorder3DNodeConfiguration(), "output", 150, 150));

        register(new PreviewShaderGraphNodeEditorProducer(new DotShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new CheckerboardShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new EllipseShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new RectangleShapeShaderNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new StarShapeShaderNodeConfiguration(), "output", 150, 150));

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
