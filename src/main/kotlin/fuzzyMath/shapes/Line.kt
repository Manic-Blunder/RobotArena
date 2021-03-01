package fuzzyMath.shapes

import fuzzyMath.*
import java.awt.Graphics
import kotlin.math.*
import java.awt.geom.Line2D

import java.awt.Rectangle


open class Line(override var position: Vector2, val p2Relative: Vector2) : Shape {
    val p2Absolute
        get() = position + p2Relative
    val length
        get() = (position + p2Relative).abs
    val angle
        get() = (position + p2Relative).angle

    override fun distanceFromSurface(point: Vector2) = (point - closestEdgePoint(point)).abs

    override fun closestEdgePoint(point: Vector2) =
        position + p2Relative * min(1.0, max(0.0, dotProduct(point - position, p2Relative) / (p2Relative.abs).pow(2)))

    override fun isColliding(other: Circular) = other.distanceFromSurface(closestEdgePoint(other.position)) < 0

    override fun isColliding(other: Rectangular): Boolean {
        val r1 = Rectangle(other.position.x.toInt(), other.position.y.toInt(), other.dimensions.x.toInt(), other.dimensions.y.toInt())
        val l1: Line2D = Line2D.Double(position.x, position.y, p2Absolute.x, p2Absolute.y)
        return l1.intersects(r1)
    }

    override fun contains(point: Vector2): Boolean {
        TODO("Not yet implemented")
    }

    override fun expel(other: Circular) {
        TODO("Not yet implemented")
    }

    override fun expel(other: Rectangular) {
        TODO("Not yet implemented")
    }

    fun draw(g: Graphics) {
        g.drawLine(
            position.x.toInt(),
            position.y.toInt(),
            position.x.toInt() + (p2Relative.x).toInt(),
            position.y.toInt() + (p2Relative.y).toInt(),
        )
    }
}