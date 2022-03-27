#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sourceTexture;
uniform vec2 u_pixelSize;
uniform int u_blurRadius;
uniform float u_kernel[65];
uniform int u_vertical;

varying vec2 v_position;

void main() {
    vec4 sampleAccum = vec4(0.0, 0.0, 0.0, 0.0);

    for (int i = 0; i <= 64; i++) {
        if (i > u_blurRadius) {
            break;
        }
        float kernel = u_kernel[i];
        if (u_vertical == 1) {
            sampleAccum += texture2D(u_sourceTexture, v_position + u_pixelSize * vec2(0, i)) * kernel;
            if (i > 0) {
                sampleAccum += texture2D(u_sourceTexture, v_position - u_pixelSize * vec2(0, i)) * kernel;
            }
        } else {
            sampleAccum += texture2D(u_sourceTexture, v_position + u_pixelSize * vec2(i, 0)) * kernel;
            if (i > 0) {
                sampleAccum += texture2D(u_sourceTexture, v_position - u_pixelSize * vec2(i, 0)) * kernel;
            }
        }
    }

    gl_FragColor = sampleAccum;
}