#version 300 es

uniform mat4 uni_ModelViewProjection;

in vec2 in_Position;
in vec2 in_TexCoords;

out vec2 ex_TexCoords;

void main() {
	gl_Position = uni_ModelViewProjection * vec4(in_Position.x, in_Position.y, 0.0f, 1.0f);
	ex_TexCoords = in_TexCoords;
}
