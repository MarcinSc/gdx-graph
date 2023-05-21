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
import com.gempukku.libgdx.graph.shader.common.texture.BorderDetectionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.Sampler2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVFlipbookShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.texture.UVTilingAndOffsetShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.common.value.*;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.*;

public class CommonShaderConfiguration implements GraphConfiguration {
    private static final ObjectMap<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new ObjectMap<>();
    private static final Array<GraphShaderPropertyProducer> graphShaderPropertyProducers = new Array<>();
    private static TextureRegion defaultTextureRegion;

    public static void addNodeBuilder(GraphShaderNodeBuilder graphShaderNodeBuilder) {
        graphShaderNodeBuilders.put(graphShaderNodeBuilder.getType(), graphShaderNodeBuilder);
    }

    public static void addPropertyProducer(GraphShaderPropertyProducer graphShaderPropertyProducer) {
        graphShaderPropertyProducers.add(graphShaderPropertyProducer);
        ShaderFieldTypeRegistry.registerShaderFieldType(graphShaderPropertyProducer.getType());
    }

    public static void setDefaultTextureRegionProperty(TextureRegion textureRegion) {
        defaultTextureRegion = textureRegion;
    }

    static {
        // Math - Arithmetic
        addNodeBuilder(new AddShaderNodeBuilder());
        addNodeBuilder(new SubtractShaderNodeBuilder());
        addNodeBuilder(new OneMinusShaderNodeBuilder());
        addNodeBuilder(new MultiplyShaderNodeBuilder());
        addNodeBuilder(new DivideShaderNodeBuilder());
        addNodeBuilder(new ReciprocalShaderNodeBuilder());

        // Math - exponential
        addNodeBuilder(new PowerShaderNodeBuilder());
        addNodeBuilder(new ExponentialShaderNodeBuilder());
        addNodeBuilder(new ExponentialBase2ShaderNodeBuilder());
        addNodeBuilder(new NaturalLogarithmShaderNodeBuilder());
        addNodeBuilder(new LogarithmBase2ShaderNodeBuilder());
        addNodeBuilder(new SquareRootShaderNodeBuilder());
        addNodeBuilder(new InverseSquareRootShaderNodeBuilder());

        // Math - Common
        addNodeBuilder(new AbsShaderNodeBuilder());
        addNodeBuilder(new SignShaderNodeBuilder());
        addNodeBuilder(new FloorShaderNodeBuilder());
        addNodeBuilder(new CeilingShaderNodeBuilder());
        addNodeBuilder(new FractionalPartShaderNodeBuilder());
        addNodeBuilder(new ModuloShaderNodeBuilder());
        addNodeBuilder(new MinimumShaderNodeBuilder());
        addNodeBuilder(new MaximumShaderNodeBuilder());
        addNodeBuilder(new MaximumShaderNodeBuilder());
        addNodeBuilder(new ClampShaderNodeBuilder());
        addNodeBuilder(new SaturateShaderNodeBuilder());
        addNodeBuilder(new LerpShaderNodeBuilder());
        addNodeBuilder(new ConditionalShaderNodeBuilder());
        addNodeBuilder(new StepShaderNodeBuilder());
        addNodeBuilder(new SmoothstepShaderNodeBuilder());

        // Math - geometric
        addNodeBuilder(new LengthShaderNodeBuilder());
        addNodeBuilder(new DistanceShaderNodeBuilder());
        addNodeBuilder(new DotProductShaderNodeBuilder());
        addNodeBuilder(new CrossProductShaderNodeBuilder());
        addNodeBuilder(new NormalizeShaderNodeBuilder());

        // Math - advanced
        addNodeBuilder(new MergeShaderNodeBuilder());
        addNodeBuilder(new SplitShaderNodeBuilder());
        addNodeBuilder(new RemapShaderNodeBuilder());
        addNodeBuilder(new RemapVectorShaderNodeBuilder());
        addNodeBuilder(new RemapValueShaderNodeBuilder());

        // Math - trigonometry
        addNodeBuilder(new SinShaderNodeBuilder());
        addNodeBuilder(new CosShaderNodeBuilder());
        addNodeBuilder(new TanShaderNodeBuilder());
        addNodeBuilder(new ArcsinShaderNodeBuilder());
        addNodeBuilder(new ArccosShaderNodeBuilder());
        addNodeBuilder(new ArctanShaderNodeBuilder());
        addNodeBuilder(new Arctan2ShaderNodeBuilder());
        addNodeBuilder(new RadiansShaderNodeBuilder());
        addNodeBuilder(new DegreesShaderNodeBuilder());

        // Math - utilities
        addNodeBuilder(new DistanceFromPlaneShaderNodeBuilder());

        // Effect
        addNodeBuilder(new FresnelEffectShaderNodeBuilder());
        addNodeBuilder(new DitherShaderNodeBuilder());
        addNodeBuilder(new DitherColorShaderNodeBuilder());
        addNodeBuilder(new IntensityShaderNodeBuilder());
        addNodeBuilder(new GradientShaderNodeBuilder());

        // Texture
        addNodeBuilder(new Sampler2DShaderNodeBuilder());
        addNodeBuilder(new UVFlipbookShaderNodeBuilder());
        addNodeBuilder(new UVTilingAndOffsetShaderNodeBuilder());
        addNodeBuilder(new BorderDetectionShaderNodeBuilder());

        // Noise
        addNodeBuilder(new SimplexNoise2DShaderNodeBuilder());
        addNodeBuilder(new SimplexNoise3DShaderNodeBuilder());
        addNodeBuilder(new PerlinNoise2DShaderNodeBuilder());
        addNodeBuilder(new PerlinNoise3DShaderNodeBuilder());
        addNodeBuilder(new VoronoiDistance2DShaderNodeBuilder());
        addNodeBuilder(new VoronoiDistance3DShaderNodeBuilder());
        addNodeBuilder(new VoronoiBorder2DShaderNodeBuilder());
        addNodeBuilder(new VoronoiBorder3DShaderNodeBuilder());

        // Shape
        addNodeBuilder(new DotShapeShaderNodeBuilder());
        addNodeBuilder(new CheckerboardShapeShaderNodeBuilder());
        addNodeBuilder(new EllipseShapeShaderNodeBuilder());
        addNodeBuilder(new RectangleShapeShaderNodeBuilder());
        addNodeBuilder(new StarShapeShaderNodeBuilder());

        // Provided
        addNodeBuilder(new TimeGraphShaderNodeBuilder());
        addNodeBuilder(new CameraPositionShaderNodeBuilder());
        addNodeBuilder(new CameraDirectionShaderNodeBuilder());
        addNodeBuilder(new CameraViewportSizeShaderNodeBuilder());
        addNodeBuilder(new FragmentCoordinateShaderNodeBuilder());
        addNodeBuilder(new SceneDepthShaderNodeBuilder());
        addNodeBuilder(new SceneColorShaderNodeBuilder());
        addNodeBuilder(new ScreenPositionShaderNodeBuilder());
        addNodeBuilder(new PixelSizeShaderNodeBuilder());
        addNodeBuilder(new ViewportSizeShaderNodeBuilder());

        // Sprite
        addNodeBuilder(new BillboardSpriteShaderNodeBuilder());
        addNodeBuilder(new ScreenSpriteShaderNodeBuilder());

        // Values
        addNodeBuilder(new ValueBooleanShaderNodeBuilder());
        addNodeBuilder(new ValueColorShaderNodeBuilder());
        addNodeBuilder(new ValueFloatShaderNodeBuilder());
        addNodeBuilder(new ValueVector2ShaderNodeBuilder());
        addNodeBuilder(new ValueVector3ShaderNodeBuilder());

        addPropertyProducer(new FloatShaderPropertyProducer());
        addPropertyProducer(new ColorShaderPropertyProducer());
        addPropertyProducer(new Vector2ShaderPropertyProducer());
        addPropertyProducer(new Vector3ShaderPropertyProducer());
        addPropertyProducer(new Matrix4ShaderPropertyProducer());
        addPropertyProducer(new TextureShaderPropertyProducer() {
            @Override
            protected TextureRegion getDefaultTextureRegion() {
                return defaultTextureRegion;
            }
        });
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
