attribute vec3 a_position;

varying vec2 v_position;

void main() {
    v_position = a_position.xy;
    gl_Position = vec4((a_position.xy * 2.0 - 1.0), 1.0, 1.0);
}