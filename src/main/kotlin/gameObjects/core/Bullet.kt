package gameObjects.core

import fuzzyMath.*
import fuzzyMath.shapes.Line
import fuzzyMath.shapes.Vector2
import java.awt.Color
import java.awt.Graphics

class Bullet(private val source: Dood, override var position: Vector2, private val velocity: Vector2) : Line(position, velocity), GameObject {
    var isDead = false
        private set
    val isOutOfBounds
        get() = position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720

    override fun update() {
        position += velocity
        source.arena.doods.forEach {
            if (!it.isDead && isColliding(it)) {
                isDead = true
                source.arena.onDoodKilled(source, it)
            }
        }
        source.arena.structures.forEach {
            if (isColliding(it)) isDead = true
        }
    }

    override fun render(g: Graphics) {
        g.color = Color.darkGray
        g.fillOval(position.x.toInt(), position.y.toInt(), 4, 4)
//        g.drawLine(position.x.toInt(), position.y.toInt(), (position.x + velocity.x).toInt(), (position.y + velocity.y).toInt())
    }
}