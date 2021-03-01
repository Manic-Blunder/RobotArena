package gameObjects.core

import fuzzyMath.*
import fuzzyMath.shapes.Rectangular
import fuzzyMath.shapes.Vector2
import java.awt.Color
import java.awt.Graphics

class Structure(override var position: Vector2, override val dimensions: Vector2) : GameObject, Rectangular {
    val center
        get() = position + (dimensions / 2.0)

    override fun render(g: Graphics) {
        g.color = Color.BLACK
        g.fillRect(position.x.toInt(), position.y.toInt(), dimensions.x.toInt(), dimensions.y.toInt())
    }
}