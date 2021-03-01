package fuzzyMath.shapes

import fuzzyMath.*
import java.awt.Point
import kotlin.math.*

class Vector2(var x: Double, var y: Double) {

    val abs get() = sqrt(x * x + y * y)
    val angle get() = atan2(y, x)

    fun distance(pt: Vector2) = (this - pt).abs

    fun firstQuadrant() =
        Vector2(abs(x), abs(y))

    fun toPoint() = Point(x.toInt(), y.toInt())

    fun reflect(angle: Double) = this * fromPolar(1.0, (angle - this.angle) * 2)

    companion object {
        fun fromPoint(p: Point) = Vector2(p.x.toDouble(), p.y.toDouble())
        fun fromPolar(r: Double, theta: Double) = Vector2(r * cos(theta), r * sin(theta))
        fun zero() = Vector2(0.0, 0.0)
    }

    fun unitVector() = this/abs

}