package com.gempukku.libgdx.graph.shader.common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.shader.common.effect.*;
import com.gempukku.libgdx.graph.shader.common.math.arithmetic.*;
import com.gempukku.libgdx.graph.shader.common.math.common.*;
import com.gempukku.libgdx.graph.shader.common.math.exponential.*;
import com.gempukku.libgdx.graph.shader.common.math.geometric.*;
import com.gempukku.libgdx.graph.shader.common.math.trigonometry.*;
import com.gempukku.libgdx.graph.shader.common.math.utility.DistanceFromPlaneShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.math.value.*;
import com.gempukku.libgdx.graph.shader.common.noise.*;
import com.gempukku.libgdx.graph.shader.common.provided.*;
import com.gempukku.libgdx.graph.shader.common.shape.*;
import com.gempukku.libgdx.graph.shader.common.sprite.BillboardSpriteShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.sprite.ScreenSpriteShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.*;
import com.gempukku.libgdx.graph.shader.common.value.*;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.*;

public class CommonShaderConfiguration implements GraphConfiguration {
    private static final ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    private static final Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();
    private static TextureRegion defaultTextureRegion;

    public static void register(GraphShaderNodeBuilder graphShaderNodeBuilder) {
        graphShaderNodeBuilders.put(graphShaderNodeBuilder.getType(), graphShaderNodeBuilder);
    }

    public static void setDefaultTextureRegionProperty(TextureRegion textureRegion) {
        defaultTextureRegion = textureRegion;
    }

    static {
        // Math - Arithmetic
        register(new AddShaderNodeBuilder());
        register(new SubtractShaderNodeBuilder());
        register(new OneMinusShaderNodeBuilder());
        register(new MultiplyShaderNodeBuilder());
        register(new DivideShaderNodeBuilder());
        register(new ReciprocalShaderNodeBuilder());

        // Math - exponential
        register(new PowerShaderNodeBuilder());
        register(new ExponentialShaderNodeBuilder());
        register(new ExponentialBase2ShaderNodeBuilder());
        register(new NaturalLogarithmShaderNodeBuilder());
        register(new LogarithmBase2ShaderNodeBuilder());
        register(new SquareRootShaderNodeBuilder());
        register(new InverseSquareRootShaderNodeBuilder());

        // Math - Common
        register(new AbsShaderNodeBuilder());
        register(new SignShaderNodeBuilder());
        register(new FloorShaderNodeBuilder());
        register(new CeilingShaderNodeBuilder());
        register(new FractionalPartShaderNodeBuilder());
        register(new ModuloShaderNodeBuilder());
        register(new MinimumShaderNodeBuilder());
        register(new MaximumShaderNodeBuilder());
        register(new MaximumShaderNodeBuilder());
        register(new ClampShaderNodeBuilder());
        register(new SaturateShaderNodeBuilder());
        register(new LerpShaderNodeBuilder());
        register(new ConditionalShaderNodeBuilder());
        register(new StepShaderNodeBuilder());
        register(new SmoothstepShaderNodeBuilder());

        // Math - geometric
        register(new LengthShaderNodeBuilder());
        register(new DistanceShaderNodeBuilder());
        register(new DotProductShaderNodeBuilder());
        register(new CrossProductShaderNodeBuilder());
        register(new NormalizeShaderNodeBuilder());

        // Math - advanced
        register(new MergeShaderNodeBuilder());
        register(new SplitShaderNodeBuilder());
        register(new RemapShaderNodeBuilder());
        register(new RemapVectorShaderNodeBuilder());
        register(new RemapValueShaderNodeBuilder());

        // Math - trigonometry
        register(new SinShaderNodeBuilder());
        register(new CosShaderNodeBuilder());
        register(new TanShaderNodeBuilder());
        register(new ArcsinShaderNodeBuilder());
        register(new ArccosShaderNodeBuilder());
        register(new ArctanShaderNodeBuilder());
        register(new Arctan2ShaderNodeBuilder());
        register(new RadiansShaderNodeBuilder());
        register(new DegreesShaderNodeBuilder());

        // Math - utilities
        register(new DistanceFromPlaneShaderNodeBuilder());

        // Effect
        register(new FresnelEffectShaderNodeBuilder());
        register(new DitherShaderNodeBuilder());
        register(new DitherColorShaderNodeBuilder());
        register(new IntensityShaderNodeBuilder());
        register(new GradientShaderNodeBuilder());

        // Texture
        register(new Sampler2DShaderNodeBuilder());
        register(new TextureSizeShaderNodeBuilder());
        register(new UVFlipbookShaderNodeBuilder());
        register(new UVTilingAndOffsetShaderNodeBuilder());
        register(new BorderDetectionShaderNodeBuilder());

        // Noise
        register(new SimplexNoise2DShaderNodeBuilder());
        register(new SimplexNoise3DShaderNodeBuilder());
        register(new PerlinNoise2DShaderNodeBuilder());
        register(new PerlinNoise3DShaderNodeBuilder());
        register(new VoronoiDistance2DShaderNodeBuilder());
        register(new VoronoiDistance3DShaderNodeBuilder());
        register(new VoronoiBorder2DShaderNodeBuilder());
        register(new VoronoiBorder3DShaderNodeBuilder());

        // Shape
        register(new DotShapeShaderNodeBuilder());
        register(new CheckerboardShapeShaderNodeBuilder());
        register(new EllipseShapeShaderNodeBuilder());
        register(new RectangleShapeShaderNodeBuilder());
        register(new StarShapeShaderNodeBuilder());

        // Provided
        register(new TimeGraphShaderNodeBuilder());
        register(new CameraPositionShaderNodeBuilder());
        register(new CameraDirectionShaderNodeBuilder());
        register(new CameraViewportSizeShaderNodeBuilder());
        register(new FragmentCoordinateShaderNodeBuilder());
        register(new SceneDepthShaderNodeBuilder());
        register(new SceneColorShaderNodeBuilder());
        register(new ScreenPositionShaderNodeBuilder());
        register(new PixelSizeShaderNodeBuilder());
        register(new ViewportSizeShaderNodeBuilder());

        // Sprite
        register(new BillboardSpriteShaderNodeBuilder());
        register(new ScreenSpriteShaderNodeBuilder());

        // Values
        register(new ValueBooleanShaderNodeBuilder());
        register(new ValueColorShaderNodeBuilder());
        register(new ValueFloatShaderNodeBuilder());
        register(new ValueVector2ShaderNodeBuilder());
        register(new ValueVector3ShaderNodeBuilder());

        registerPropertyProducer(new FloatShaderPropertyProducer());
        registerPropertyProducer(new ColorShaderPropertyProducer());
        registerPropertyProducer(new Vector2ShaderPropertyProducer());
        registerPropertyProducer(new Vector3ShaderPropertyProducer());
        registerPropertyProducer(new Matrix4ShaderPropertyProducer());
        registerPropertyProducer(new TextureShaderPropertyProducer() {
            @Override
            protected TextureRegion getDefaultTextureRegion() {
                return defaultTextureRegion;
            }
        });
    }

    public static void registerPropertyProducer(GraphShaderPropertyProducer graphShaderPropertyProducer) {
        graphShaderPropertyProducers.add(graphShaderPropertyProducer);
        ShaderFieldTypeRegistry.registerShaderFieldType(graphShaderPropertyProducer.getType());
    }

    @Override
    public Array<GraphShaderPropertyProducer> getPropertyProducers() {
        return graphShaderPropertyProducers;
    }

    @Override
    public GraphShaderNodeBuilder getGraphShaderNodeBuilder(String type) {
        return graphShaderNodeBuilders.get(type);
    }
}
