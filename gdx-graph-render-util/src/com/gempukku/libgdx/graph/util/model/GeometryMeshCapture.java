package com.gempukku.libgdx.graph.util.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ShortArray;

public class GeometryMeshCapture implements MeshPartBuilder {
    private final ShortArray indicesArray = new ShortArray();
    private final FloatArray positionArray = new FloatArray();
    private final FloatArray normalArray = new FloatArray();
    private final FloatArray colorArray = new FloatArray();
    private final FloatArray uvArray = new FloatArray();

    private boolean vertexTransformationEnabled;
    private final Matrix4 positionTransform = new Matrix4();
    private final Matrix3 normalTransform = new Matrix3();

    private final Vector3 tmpVector3 = new Vector3();
    private short nextVertexIndex = 0;

    public void clear() {
        indicesArray.clear();
        positionArray.clear();
        normalArray.clear();
        colorArray.clear();
        uvArray.clear();

        vertexTransformationEnabled = false;
        positionTransform.idt();
        normalTransform.idt();

        nextVertexIndex = 0;
    }

    public int getVertexCount() {
        return nextVertexIndex;
    }

    public ShortArray getIndicesArray() {
        return indicesArray;
    }

    public FloatArray getPositionArray() {
        return positionArray;
    }

    public FloatArray getNormalArray() {
        return normalArray;
    }

    public FloatArray getColorArray() {
        return colorArray;
    }

    public FloatArray getUvArray() {
        return uvArray;
    }

    @Override
    public MeshPart getMeshPart() {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public int getPrimitiveType() {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public VertexAttributes getAttributes() {
        return new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
    }

    @Override
    public void setColor(Color color) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void setUVRange(float u1, float v1, float u2, float v2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void setUVRange(TextureRegion r) {
        throw new GdxRuntimeException("Not supported");
    }


    @Override
    public Matrix4 getVertexTransform(Matrix4 out) {
        return out.set(positionTransform);
    }

    @Override
    public void setVertexTransform(Matrix4 transform) {
        vertexTransformationEnabled = transform != null;
        if (vertexTransformationEnabled) {
            positionTransform.set(transform);
            normalTransform.set(transform).inv().transpose();
        } else {
            positionTransform.idt();
            normalTransform.idt();
        }
    }

    @Override
    public boolean isVertexTransformationEnabled() {
        return vertexTransformationEnabled;
    }

    @Override
    public void setVertexTransformationEnabled(boolean enabled) {
        vertexTransformationEnabled = enabled;
    }

    @Override
    public void ensureVertices(int numVertices) {
        positionArray.ensureCapacity(3 * numVertices);
        normalArray.ensureCapacity(3 * numVertices);
        colorArray.ensureCapacity(4 * numVertices);
        uvArray.ensureCapacity(2 * numVertices);
    }

    @Override
    public void ensureIndices(int numIndices) {
        indicesArray.ensureCapacity(numIndices);
    }

    @Override
    public void ensureCapacity(int numVertices, int numIndices) {
        ensureIndices(numIndices);
        ensureVertices(numVertices);
    }

    @Override
    public void ensureTriangleIndices(int numTriangles) {
        ensureCapacity(3, 3);
    }

    @Override
    public void ensureRectangleIndices(int numRectangles) {
        ensureCapacity(4, 6);
    }

    @Override
    public short vertex(float... values) {
        throw new GdxRuntimeException("Not supported");

    }

    @Override
    public short vertex(Vector3 pos, Vector3 nor, Color col, Vector2 uv) {
        short vertexIndex = nextVertexIndex++;
        if (pos != null) {
            Vector3 result = tmpVector3.set(pos);
            if (vertexTransformationEnabled)
                result.mul(positionTransform);
            positionArray.add(result.x, result.y, result.z);
        }
        if (nor != null) {
            Vector3 result = tmpVector3.set(nor);
            if (vertexTransformationEnabled)
                result.mul(normalTransform).nor();
            normalArray.add(result.x, result.y, result.z);
        }
        if (col != null)
            colorArray.add(col.r, col.g, col.b, col.a);
        if (uv != null)
            uvArray.add(uv.x, uv.y);
        return vertexIndex;
    }

    @Override
    public short vertex(VertexInfo info) {
        return vertex(info.position, info.normal, info.color, info.uv);
    }

    @Override
    public short lastIndex() {
        return (short) (nextVertexIndex - 1);
    }

    @Override
    public void index(short value) {
        indicesArray.add(value);
    }

    @Override
    public void index(short value1, short value2) {
        indicesArray.add(value1);
        indicesArray.add(value2);
    }

    @Override
    public void index(short value1, short value2, short value3) {
        indicesArray.add(value1);
        indicesArray.add(value2);
        indicesArray.add(value3);
    }

    @Override
    public void index(short value1, short value2, short value3, short value4) {
        indicesArray.add(value1);
        indicesArray.add(value2);
        indicesArray.add(value3);
        indicesArray.add(value4);

    }

    @Override
    public void index(short value1, short value2, short value3, short value4, short value5, short value6) {
        indicesArray.add(value1);
        indicesArray.add(value2);
        indicesArray.add(value3);
        indicesArray.add(value4);
        indicesArray.add(value5);
        indicesArray.add(value6);
    }

    @Override
    public void index(short value1, short value2, short value3, short value4, short value5, short value6, short value7, short value8) {
        indicesArray.add(value1);
        indicesArray.add(value2);
        indicesArray.add(value3);
        indicesArray.add(value4);
        indicesArray.add(value5);
        indicesArray.add(value6);
        indicesArray.add(value7);
        indicesArray.add(value8);
    }

    @Override
    public void line(short index1, short index2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void line(VertexInfo p1, VertexInfo p2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void line(Vector3 p1, Vector3 p2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void line(Vector3 p1, Color c1, Vector3 p2, Color c2) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void triangle(short index1, short index2, short index3) {
        index(index1, index2, index3);
    }

    @Override
    public void triangle(VertexInfo p1, VertexInfo p2, VertexInfo p3) {
        index(vertex(p1), vertex(p2), vertex(p3));
    }

    @Override
    public void triangle(Vector3 p1, Vector3 p2, Vector3 p3) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void triangle(Vector3 p1, Color c1, Vector3 p2, Color c2, Vector3 p3, Color c3) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void rect(short corner00, short corner10, short corner11, short corner01) {
        index(corner00, corner10, corner11, corner11, corner01, corner00);
    }

    @Override
    public void rect(VertexInfo corner00, VertexInfo corner10, VertexInfo corner11, VertexInfo corner01) {
        short index00 = vertex(corner00);
        short index10 = vertex(corner10);
        short index11 = vertex(corner11);
        short index01 = vertex(corner01);
        index(index00, index10, index11, index11, index01, index00);
    }

    @Override
    public void rect(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal) {
        short index00 = vertex(corner00, normal, null, null);
        short index10 = vertex(corner10, normal, null, null);
        short index11 = vertex(corner11, normal, null, null);
        short index01 = vertex(corner01, normal, null, null);
        index(index00, index10, index11, index11, index01, index00);
    }

    @Override
    public void rect(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void addMesh(Mesh mesh) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void addMesh(MeshPart meshpart) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void addMesh(Mesh mesh, int indexOffset, int numIndices) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void addMesh(float[] vertices, short[] indices) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void addMesh(float[] vertices, short[] indices, int indexOffset, int numIndices) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void patch(VertexInfo corner00, VertexInfo corner10, VertexInfo corner11, VertexInfo corner01, int divisionsU, int divisionsV) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void patch(Vector3 corner00, Vector3 corner10, Vector3 corner11, Vector3 corner01, Vector3 normal, int divisionsU, int divisionsV) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void patch(float x00, float y00, float z00, float x10, float y10, float z10, float x11, float y11, float z11, float x01, float y01, float z01, float normalX, float normalY, float normalZ, int divisionsU, int divisionsV) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void box(VertexInfo corner000, VertexInfo corner010, VertexInfo corner100, VertexInfo corner110, VertexInfo corner001, VertexInfo corner011, VertexInfo corner101, VertexInfo corner111) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void box(Vector3 corner000, Vector3 corner010, Vector3 corner100, Vector3 corner110, Vector3 corner001, Vector3 corner011, Vector3 corner101, Vector3 corner111) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void box(Matrix4 transform) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void box(float width, float height, float depth) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void box(float x, float y, float z, float width, float height, float depth) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void circle(float radius, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, Vector3 center, Vector3 normal, Vector3 tangent, Vector3 binormal, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float tangentX, float tangentY, float tangentZ, float binormalX, float binormalY, float binormalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, float centerX, float centerY, float centerZ, float normalX, float normalY, float normalZ) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void ellipse(float width, float height, float innerWidth, float innerHeight, int divisions, Vector3 center, Vector3 normal) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void cylinder(float width, float height, float depth, int divisions) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void cylinder(float width, float height, float depth, int divisions, float angleFrom, float angleTo, boolean close) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void cone(float width, float height, float depth, int divisions) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void cone(float width, float height, float depth, int divisions, float angleFrom, float angleTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void sphere(float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void sphere(Matrix4 transform, float width, float height, float depth, int divisionsU, int divisionsV, float angleUFrom, float angleUTo, float angleVFrom, float angleVTo) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void capsule(float radius, float height, int divisions) {
        throw new GdxRuntimeException("Not supported");
    }

    @Override
    public void arrow(float x1, float y1, float z1, float x2, float y2, float z2, float capLength, float stemThickness, int divisions) {
        throw new GdxRuntimeException("Not supported");
    }
}
