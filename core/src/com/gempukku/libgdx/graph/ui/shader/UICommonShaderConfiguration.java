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
        register(new GdxGraphNodeEditorProducer(new Sampler2DShaderNodeConfiguration()));
        register(new UVFlipbookShaderEditorProducer());
        register(new GdxGraphNodeEditorProducer(new UVTilingAndOffsetShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new BorderDetectionShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new AddShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SubtractShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new OneMinusShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MultiplyShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DivideShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ReciprocalShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new PowerShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ExponentialShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ExponentialBase2ShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new NaturalLogarithmShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new LogarithmBase2ShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SquareRootShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new InverseSquareRootShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new SinShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CosShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new TanShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ArcsinShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ArccosShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ArctanShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new Arctan2ShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new RadiansShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DegreesShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new AbsShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SignShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FloorShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CeilingShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FractionalPartShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ModuloShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MinimumShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MaximumShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new ClampShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SaturateShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new LerpShaderNodeConfiguration()));
        register(new ConditionalShaderEditorProducer());
        register(new GdxGraphNodeEditorProducer(new StepShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new SmoothstepShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new LengthShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DistanceShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new DotProductShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CrossProductShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new NormalizeShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new DistanceFromPlaneShaderNodeConfiguration()));

        register(new GdxGraphNodeEditorProducer(new SplitShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new MergeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new RemapShaderNodeConfiguration()));
        register(new RemapVectorShaderEditorProducer());
        register(new RemapValueShaderEditorProducer());

        register(new DitherShaderEditorProducer());
        register(new DitherColorShaderEditorProducer());
        register(new GdxGraphNodeEditorProducer(new IntensityShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new FresnelEffectShaderNodeConfiguration()));
        register(new GradientShaderEditorProducer());

        register(new PreviewShaderGraphNodeEditorProducer(new SimplexNoise2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new SimplexNoise3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new PerlinNoise2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new PerlinNoise3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiDistance2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiDistance3DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiBorder2DNodeConfiguration(), "output", 150, 150));
        register(new PreviewShaderGraphNodeEditorProducer(new VoronoiBorder3DNodeConfiguration(), "output", 150, 150));

        register(new GdxGraphNodeEditorProducer(new DotShapeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new CheckerboardShapeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new EllipseShapeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new RectangleShapeShaderNodeConfiguration()));
        register(new GdxGraphNodeEditorProducer(new StarShapeShaderNodeConfiguration()));

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
