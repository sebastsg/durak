#version 300 es

uniform sampler2D uni_Texture;
uniform mediump vec4 uni_Color;

in mediump vec2 ex_TexCoords;

out mediump vec4 out_Color;

void main() {
	out_Color = texture(uni_Texture, ex_TexCoords).rgba * uni_Color;
}
