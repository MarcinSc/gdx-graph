vec3 borderVoronoiDistanceRandom3(vec3 p) {
    return fract(sin(vec3(dot(p, vec3(127.1, 311.7, 415.3)), dot(p, vec3(269.5, 183.3, 116.9)), dot(p, vec3(175.3, 211.5, 386.9))))*43758.5453);
}

// The MIT License
// Copyright © 2013 Inigo Quilez
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
// to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above
// copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
// OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
float borderVoronoiDistance3d(vec3 x, float progress)
{
    vec3 n = floor(x);
    vec3 f = fract(x);

    //----------------------------------
    // first pass: regular voronoi
    //----------------------------------
    vec3 mg, mr;

    float md = 8.0;
    for (int k=-1; k<=1; k++) {
        for (int j=-1; j<=1; j++) {
            for (int i=-1; i<=1; i++)
            {
                vec3 g = vec3(float(i), float(j), float(k));
                vec3 o = borderVoronoiDistanceRandom3(n + g);
                o = 0.5 + 0.5*sin(progress + 6.2831*o);
                vec3 r = g + o - f;
                float d = dot(r, r);

                if (d<md)
                {
                    md = d;
                    mr = r;
                    mg = g;
                }
            }
        }
    }

    //----------------------------------
    // second pass: distance to borders
    //----------------------------------
    md = 8.0;
    for (int k=-2; k<=2; k++) {
        for (int j=-2; j<=2; j++) {
            for (int i=-2; i<=2; i++)
            {
                vec3 g = mg + vec3(float(i), float(j), float(k));
                vec3 o = borderVoronoiDistanceRandom3(n + g);
                o = 0.5 + 0.5*sin(progress + 6.2831*o);
                vec3 r = g + o - f;

                if (dot(mr-r, mr-r)>0.00001) {
                    md = min(md, dot(0.5*(mr+r), normalize(r-mr)));
                }
            }
        }
    }

    return md;
}

float voronoiBorder3d(vec3 x, float progress) {
    float d = borderVoronoiDistance3d(x, progress);

    return 1.0 - smoothstep(0.0, 0.05, d);
}
