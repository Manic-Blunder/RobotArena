package fuzzyMath.shapes

import java.awt.Graphics

abstract class Rectangle(a: Vector2, b: Vector2) : Shape {
    val segments = listOf(
        Segment(a, Vector2(b.x, a.y)), // top
        Segment(a, Vector2(a.x, b.y)), // left side
        Segment(Vector2(b.x, a.y), b), // right side
        Segment(Vector2(a.x, b.y), a), // bottom
    )

    fun draw(g: Graphics) {

    }

    fun fill(g: Graphics) {

    }
}