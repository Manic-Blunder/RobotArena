package fuzzyMath.shapes

import fuzzyMath.minus
import java.awt.Graphics
import java.awt.Point

class Segment(val a: Vector2, val b: Vector2) {
    constructor(ax: Double, ay: Double, bx: Double, by: Double) : this(Vector2(ax, ay), Vector2(bx, by))
    constructor(ax: Int, ay: Int, bx: Int, by: Int) : this(Vector2.fromPoint(Point(ax, ay)), Vector2.fromPoint(Point(bx, by)))

    private val relative = b - a
    val slopeX
        get() = b.x - a.x
    val slopeY
        get() = b.y - a.y
    val slope
        get() = slopeY / slopeX
    val distance
        get() = relative.abs
    val radians
        get() = relative.angle
    val crossProduct
        get() = a.x * b.y - a.y * b.x

    fun intersection(other: Segment): Vector2? {
        val sDenominator = (-other.slopeX * slopeY + slopeX * other.slopeY)
        if (sDenominator != 0.0) { // If this is false, the lines are co-linear.
            val s = (-slopeY * (a.x - other.a.x) + slopeX * (a.y - other.a.y)) / sDenominator

            val tDenominator = (-other.slopeX * slopeY + slopeX * other.slopeY)
            if (tDenominator != 0.0) {
                val t = (other.slopeX * (a.y - other.a.y) - other.slopeY * (a.x - other.a.x)) / tDenominator

                if (s in 0.0..1.0 && t in 0.0..1.0) return Vector2(a.x + t * slopeX, a.y + t * slopeY)
            }
        }
        return null
    }

    fun draw(g: Graphics) {
        g.drawLine(a.x.toInt(), a.y.toInt(), b.x.toInt(), b.y.toInt())
    }
}