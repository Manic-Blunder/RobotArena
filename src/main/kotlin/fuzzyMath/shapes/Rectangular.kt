package fuzzyMath.shapes

import fuzzyMath.*
import kotlin.math.max
import kotlin.math.min

interface Rectangular : Shape {
    val dimensions: Vector2
    val centerPoint get() = dimensions / 2 + position
    val extents get() = (dimensions / 2).firstQuadrant()

    override fun distanceFromSurface(point: Vector2) : Double {
        val d = (point - centerPoint).firstQuadrant() - extents
        return max(d, 0.0).abs + min(max(d.x,d.y), 0.0)
    }

    override fun closestEdgePoint(point: Vector2): Vector2 {
        if (contains(point)) {
            val leftDiff = point.x - position.x
            val rightDiff = position.x + dimensions.x - point.x
            val upDiff = point.y - position.y
            val downDiff = position.y + dimensions.y - point.y
            val shortest = listOf(leftDiff, rightDiff, upDiff, downDiff).minOrNull()
            return when (shortest) {
                leftDiff -> Vector2(point.x - leftDiff, point.y)
                rightDiff -> Vector2(point.x + rightDiff, point.y)
                upDiff -> Vector2(point.x, point.y - upDiff)
                downDiff -> Vector2(point.x, point.y + downDiff)
                else -> point
            }
        } else {
            val x = clamp(position.x, point.x, position.x + dimensions.x)
            val y = clamp(position.y, point.y, position.y + dimensions.y)
            return Vector2(x, y)
        }
    }

    override fun isColliding(other: Circular): Boolean {
        TODO("Not yet implemented")
    }

    override fun isColliding(other: Rectangular): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(point: Vector2) =
        point.x >= position.x && point.x <= position.x + dimensions.x && point.y >= position.y && point.y <= position.y + dimensions.y

    override fun expel(other: Circular) {
        if (contains(other.position)) {
            val leftDiff = other.position.x - position.x
            val rightDiff = position.x + dimensions.x - other.position.x
            val upDiff = other.position.y - position.y
            val downDiff = position.y + dimensions.y - other.position.y
            val shortest = listOf(leftDiff, rightDiff, upDiff, downDiff).minOrNull()
            other.position = when (shortest) {
                leftDiff -> Vector2(other.position.x - leftDiff, other.position.y)
                rightDiff -> Vector2(other.position.x + rightDiff, other.position.y)
                upDiff -> Vector2(other.position.x, other.position.y - upDiff)
                downDiff -> Vector2(other.position.x, other.position.y + downDiff)
                else -> other.position
            }
        }
    }

    override fun expel(other: Rectangular) {
        TODO("Not yet implemented")
    }
}