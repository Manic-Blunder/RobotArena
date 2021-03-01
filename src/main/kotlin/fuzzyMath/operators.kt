package fuzzyMath

import fuzzyMath.shapes.Vector2

operator fun Vector2.plus(p2: Vector2) = Vector2(x + p2.x, y + p2.y)
operator fun Vector2.minus(p2: Vector2) = Vector2(x - p2.x, y - p2.y)
operator fun Vector2.div(d: Double) = Vector2(x / d, y / d)
operator fun Vector2.div(i: Int) = Vector2(x / i, y / i)
operator fun Vector2.times(d: Double) = Vector2(x*d, y*d)
operator fun Vector2.times(i: Int) = Vector2(x*i, y*i)
operator fun Vector2.times(v: Vector2) = Vector2(x*v.x - y*v.y, x*v.y + y*v.x)