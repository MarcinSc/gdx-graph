#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sourceTexture;
uniform float u_minimalBrightness;

varying vec2 v_position;

void main() {
    vec4 color = texture2D(u_sourceTexture, v_position);
    float brightness = color.r * 0.2126729 + color.g * 0.7151522 + color.b * 0.0721750;
    if (brightness >= u_minimalBrightness) {
        gl_FragColor = color;
    } else {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
}