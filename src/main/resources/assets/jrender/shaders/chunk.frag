#version 330 core
in vec4 fColor;
in vec2 fTexCoords;

out vec4 FragColor;

uniform sampler2D textureAtlas;

void main()
{
    FragColor = fColor * texture(textureAtlas, fTexCoords);
}