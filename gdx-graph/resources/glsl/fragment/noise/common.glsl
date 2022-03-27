float permute(float x)
{
    return mod(((x * 34.0) + 1.0) * x, 289.0);
}

vec3 permute(vec3 x) {
    return mod(((x * 34.0) + 1.0) * x, 289.0);
}

vec4 permute(vec4 x)
{
    return mod(((x * 34.0) + 1.0) * x, 289.0);
}

float taylorInvSqrt(float r)
{
    return 1.79284291400159 - 0.85373472095314 * r;
}

vec4 taylorInvSqrt(vec4 r)
{
    return 1.79284291400159 - 0.85373472095314 * r;
}

vec4 grad4(float j, vec4 ip)
{
    const vec4 ones = vec4(1.0, 1.0, 1.0, -1.0);
    vec4 p, s;

    p.xyz = floor(fract(vec3(j) * ip.xyz) * 7.0) * ip.z - 1.0;
    p.w = 1.5 - dot(abs(p.xyz), ones.xyz);
    s = vec4(lessThan(p, vec4(0.0)));
    p.xyz = p.xyz + (s.xyz * 2.0 - 1.0) * s.www;

    return p;
}

vec2 fade(vec2 t) {
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}

vec3 fade(vec3 t) {
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}

vec4 fade(vec4 t) {
    return t * t * t * (t * (t * 6.0 - 15.0) + 10.0);
}
