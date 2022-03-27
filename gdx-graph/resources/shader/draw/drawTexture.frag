#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sourceTexture;
uniform vec2 u_sourcePosition;
uniform vec2 u_sourceSize;

varying vec2 v_position;

void main() {
    vec2 sourcePosition = u_sourcePosition + v_position * u_sourceSize;
    vec4 color = texture2D(u_sourceTexture, sourcePosition);
    gl_FragColor = color;
}