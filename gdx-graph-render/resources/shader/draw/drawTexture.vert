attribute vec3 a_position;

uniform vec2 u_targetPosition;
uniform vec2 u_targetSize;

varying vec2 v_position;

void main() {
    v_position = a_position.xy;
    vec2 result = u_targetPosition + a_position.xy * u_targetSize;
    gl_Position = vec4((result * 2.0 - 1.0), 1.0, 1.0);
}