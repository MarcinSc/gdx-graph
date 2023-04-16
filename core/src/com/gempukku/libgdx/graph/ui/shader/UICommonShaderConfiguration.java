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
import com.gempukku.libgdx.graph.ui.DefaultMenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.MenuGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.GradientShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.common.ConditionalShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapValueShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapVectorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.*;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.SceneColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.TimeShaderBoxProducerDefault;
import com.gempukku.libgdx.graph.ui.shader.producer.texture.UVFlipbookShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UICommonShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, MenuGraphNodeEditorProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyEditorDefinition> propertyProducers = new LinkedHashMap<>();

    public static void register(MenuGraphNodeEditorProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyEditorDefinition propertyEditorDefinition) {
        propertyProducers.put(propertyEditorDefinition.getType(), propertyEditorDefinition);
    }

    static {
        register(new DefaultMenuGraphNodeEditorProducer(new Sampler2DShaderNodeConfiguration()));
        register(new UVFlipbookShaderBoxProducer());
        register(new DefaultMenuGraphNodeEditorProducer(new UVTilingAndOffsetShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new BorderDetectionShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new AddShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SubtractShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new OneMinusShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MultiplyShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DivideShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ReciprocalShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new PowerShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ExponentialShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ExponentialBase2ShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new NaturalLogarithmShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new LogarithmBase2ShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SquareRootShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new InverseSquareRootShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new SinShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CosShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new TanShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ArcsinShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ArccosShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ArctanShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new Arctan2ShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new RadiansShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DegreesShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new AbsShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SignShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FloorShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CeilingShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FractionalPartShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ModuloShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MinimumShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MaximumShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ClampShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SaturateShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new LerpShaderNodeConfiguration()));
        register(new ConditionalShaderBoxProducer());
        register(new DefaultMenuGraphNodeEditorProducer(new StepShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SmoothstepShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new LengthShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DistanceShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new DotProductShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CrossProductShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new NormalizeShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new DistanceFromPlaneShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new SplitShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new MergeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new RemapShaderNodeConfiguration()));
        register(new RemapVectorShaderBoxProducer());
        register(new RemapValueShaderBoxProducer());

        register(new DitherShaderBoxProducer());
        register(new DitherColorShaderBoxProducer());
        register(new DefaultMenuGraphNodeEditorProducer(new IntensityShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FresnelEffectShaderNodeConfiguration()));
        register(new GradientShaderBoxProducer());

        register(new DefaultMenuGraphNodeEditorProducer(new SimplexNoise2DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SimplexNoise3DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new PerlinNoise2DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new PerlinNoise3DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new VoronoiDistance2DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new VoronoiDistance3DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new VoronoiBorder2DNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new VoronoiBorder3DNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new DotShapeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CheckerboardShapeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new EllipseShapeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new RectangleShapeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new StarShapeShaderNodeConfiguration()));

        register(new TimeShaderBoxProducerDefault());
        register(new DefaultMenuGraphNodeEditorProducer(new CameraPositionShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CameraDirectionShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new CameraViewportSizeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new FragmentCoordinateShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new SceneDepthShaderNodeConfiguration()));
        register(new SceneColorShaderBoxProducer());
        register(new DefaultMenuGraphNodeEditorProducer(new ScreenPositionShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new PixelSizeShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ViewportSizeShaderNodeConfiguration()));

        register(new DefaultMenuGraphNodeEditorProducer(new BillboardSpriteShaderNodeConfiguration()));
        register(new DefaultMenuGraphNodeEditorProducer(new ScreenSpriteShaderNodeConfiguration()));

        register(new ValueColorBoxProducerDefault(new ValueColorShaderNodeConfiguration()));
        register(new ValueFloatBoxProducerDefault(new ValueFloatShaderNodeConfiguration()));
        register(new ValueVector2BoxProducerDefault(new ValueVector2ShaderNodeConfiguration()));
        register(new ValueVector3BoxProducerDefault(new ValueVector3ShaderNodeConfiguration()));
        register(new ValueBooleanBoxProducerDefault(new ValueBooleanShaderNodeConfiguration()));

        registerPropertyType(new PropertyFloatEditorDefinition());
        registerPropertyType(new PropertyVector2EditorDefinition());
        registerPropertyType(new PropertyVector3EditorDefinition());
        registerPropertyType(new PropertyColorEditorDefinition());
        registerPropertyType(new PropertyMatrix4EditorDefinition());
        registerPropertyType(new PropertyTextureEditorDefinition());
    }

    @Override
    public Iterable<MenuGraphNodeEditorProducer> getGraphNodeEditorProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyEditorDefinition> getPropertyEditorDefinitions() {
        return propertyProducers;
    }
}
