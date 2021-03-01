package gameObjects.core

import fuzzyMath.shapes.Shape
import fuzzyMath.shapes.Vector2
import java.awt.Graphics

interface GameObject : Shape {
    fun update() {}
    fun render(g: Graphics)
}