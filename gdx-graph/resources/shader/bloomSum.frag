#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sourceTexture;
uniform sampler2D u_brightnessTexture;
uniform float u_bloomStrength;

varying vec2 v_position;

void main() {
    vec4 color = texture2D(u_sourceTexture, v_position);
    vec4 brightnessColor = texture2D(u_brightnessTexture, v_position);
    color.rgb += brightnessColor.rgb * u_bloomStrength;
    gl_FragColor = color;
}