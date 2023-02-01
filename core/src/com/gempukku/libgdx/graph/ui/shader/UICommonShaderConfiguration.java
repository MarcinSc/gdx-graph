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
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.DitherShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.effect.GradientShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.common.ConditionalShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapValueShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.math.value.RemapVectorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.property.*;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.SceneColorShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.provided.TimeShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.texture.UVFlipbookShaderBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.producer.value.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UICommonShaderConfiguration implements UIGraphConfiguration {
    private static Map<String, GraphBoxProducer> graphBoxProducers = new TreeMap<>();
    private static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    public static void register(GraphBoxProducer producer) {
        String menuLocation = producer.getMenuLocation();
        if (menuLocation == null)
            menuLocation = "Dummy";
        graphBoxProducers.put(menuLocation + "/" + producer.getName(), producer);
    }

    public static void registerPropertyType(PropertyBoxProducer propertyBoxProducer) {
        propertyProducers.put(propertyBoxProducer.getType(), propertyBoxProducer);
    }

    static {
        PropertyShaderGraphBoxProducer propertyProducer = new PropertyShaderGraphBoxProducer();
        propertyProducer.addPropertyGraphBoxCustomization(new TextureCustomization());
        register(propertyProducer);

        register(new GraphBoxProducerImpl(new Sampler2DShaderNodeConfiguration()));
        register(new UVFlipbookShaderBoxProducer());
        register(new GraphBoxProducerImpl(new UVTilingAndOffsetShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new BorderDetectionShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AddShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SubtractShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new OneMinusShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MultiplyShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DivideShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ReciprocalShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new PowerShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ExponentialShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ExponentialBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NaturalLogarithmShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LogarithmBase2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SquareRootShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new InverseSquareRootShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new SinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new TanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArcsinShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArccosShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ArctanShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new Arctan2ShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RadiansShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DegreesShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new AbsShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SignShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FloorShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CeilingShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FractionalPartShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ModuloShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MinimumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MaximumShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ClampShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SaturateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new LerpShaderNodeConfiguration()));
        register(new ConditionalShaderBoxProducer());
        register(new GraphBoxProducerImpl(new StepShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SmoothstepShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new LengthShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DistanceShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new DotProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CrossProductShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new NormalizeShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new DistanceFromPlaneShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new SplitShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new MergeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RemapShaderNodeConfiguration()));
        register(new RemapVectorShaderBoxProducer());
        register(new RemapValueShaderBoxProducer());

        register(new DitherShaderBoxProducer());
        register(new DitherColorShaderBoxProducer());
        register(new GraphBoxProducerImpl(new IntensityShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FresnelEffectShaderNodeConfiguration()));
        register(new GradientShaderBoxProducer());

        register(new GraphBoxProducerImpl(new SimplexNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SimplexNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PerlinNoise2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PerlinNoise3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiDistance2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiDistance3DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiBorder2DNodeConfiguration()));
        register(new GraphBoxProducerImpl(new VoronoiBorder3DNodeConfiguration()));

        register(new GraphBoxProducerImpl(new DotShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CheckerboardShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new EllipseShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new RectangleShapeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new StarShapeShaderNodeConfiguration()));

        register(new TimeShaderBoxProducer());
        register(new GraphBoxProducerImpl(new CameraPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CameraDirectionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new CameraViewportSizeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new FragmentCoordinateShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new SceneDepthShaderNodeConfiguration()));
        register(new SceneColorShaderBoxProducer());
        register(new GraphBoxProducerImpl(new ScreenPositionShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new PixelSizeShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ViewportSizeShaderNodeConfiguration()));

        register(new GraphBoxProducerImpl(new BillboardSpriteShaderNodeConfiguration()));
        register(new GraphBoxProducerImpl(new ScreenSpriteShaderNodeConfiguration()));

        register(new ValueColorBoxProducer(new ValueColorShaderNodeConfiguration()));
        register(new ValueFloatBoxProducer(new ValueFloatShaderNodeConfiguration()));
        register(new ValueVector2BoxProducer(new ValueVector2ShaderNodeConfiguration()));
        register(new ValueVector3BoxProducer(new ValueVector3ShaderNodeConfiguration()));
        register(new ValueBooleanBoxProducer(new ValueBooleanShaderNodeConfiguration()));

        registerPropertyType(new PropertyFloatBoxProducer());
        registerPropertyType(new PropertyVector2BoxProducer());
        registerPropertyType(new PropertyVector3BoxProducer());
        registerPropertyType(new PropertyColorBoxProducer());
        registerPropertyType(new PropertyMatrix4BoxProducer());
        registerPropertyType(new PropertyTextureBoxProducer());
    }

    @Override
    public Iterable<GraphBoxProducer> getGraphBoxProducers() {
        return graphBoxProducers.values();
    }

    @Override
    public Map<String, PropertyBoxProducer> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
