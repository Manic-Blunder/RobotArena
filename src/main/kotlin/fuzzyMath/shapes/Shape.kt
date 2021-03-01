package fuzzyMath.shapes

interface Shape {
    var position: Vector2
    val x
        get() = position.x
    val y
        get() = position.y
    fun distanceFromSurface(point: Vector2): Double
    fun closestEdgePoint(point: Vector2): Vector2
    fun isColliding(other: Circular): Boolean
    fun isColliding(other: Rectangular): Boolean
    fun contains(point: Vector2): Boolean
    fun expel(other: Circular)
    fun expel(other: Rectangular)
}
