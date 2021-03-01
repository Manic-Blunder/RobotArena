package fuzzyMath.shapes

import fuzzyMath.minus
import fuzzyMath.plus
import fuzzyMath.times

interface Circular : Shape {
    val radius: Double
    val diameter
        get() = radius * 2
    override fun distanceFromSurface(point: Vector2) = position.distance(point) - radius
    override fun closestEdgePoint(point: Vector2) = position + ((point - position) * (radius / (point - position).abs))
    override fun isColliding(other: Circular) = position.distance(other.position) < radius + other.radius
    override fun isColliding(other: Rectangular) = other.distanceFromSurface(position) < radius
    override fun contains(point: Vector2): Boolean {
        return (point-position).abs <= radius
    }

    override fun expel(other: Circular) {
        TODO("Not yet implemented")
    }

    override fun expel(other: Rectangular) {
        TODO("Not yet implemented")
    }
}