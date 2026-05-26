#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoords;

out vec4 fColor;
out vec2 fTexCoords;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform vec2 atlasSize;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords / atlasSize;

    gl_Position = projectionMatrix * modelViewMatrix * vec4(aPos, 1.0);
}