package fuzzyMath

import fuzzyMath.shapes.Vector2
import kotlin.math.*

fun clamp(min: Double, value: Double, max: Double): Double = value.coerceAtLeast(min).coerceAtMost(max)
fun max(v: Vector2, d: Double): Vector2 = Vector2(max(v.x, d), max(v.y, d))
fun min(v: Vector2, d: Double): Vector2 = Vector2(min(v.x, d), min(v.y, d))
fun sigmoid(input: Double): Double = 1 / (1 + exp(-input))
fun sigmoid(input: Matrix): Matrix =
    Matrix(input.elements.map { row: List<Double> ->
        (row.map { element: Double ->
            sigmoid(element)
        })
    })
fun dotProduct(v1: Vector2, v2: Vector2) = v1.x * v2.x + v1.y * v2.y
fun sumZenos(n: Int) = if (n == 0) 0.0 else (1..n).map{1.0 / 2.0.pow(it)}.sum()