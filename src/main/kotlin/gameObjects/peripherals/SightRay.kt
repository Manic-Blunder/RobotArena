package gameObjects.peripherals

import fuzzyMath.plus
import fuzzyMath.shapes.Line
import fuzzyMath.shapes.Vector2
import gameObjects.core.Dood
import java.awt.Color
import java.awt.Graphics

class SightRay(private val dood: Dood, private val start: Vector2, val angle: Double, stepSize: Double, private val steps: Int) {

    private val step = Vector2.fromPolar(stepSize, angle)
    private val length = stepSize * steps
    private val line: Line = Line(start, Vector2.fromPolar(length, angle))

    fun cast(): Pair<Double, Double> {
        var currentStep = start
        if (dood.arena.structures.any { line.isColliding(it) } || dood.arena.doods.any { it != dood && line.isColliding(it) }) {
            repeat(steps) { i ->
                currentStep += step
                if (dood.arena.structures.any { it.contains(currentStep) }) {
                    return Pair((i.toDouble() / steps.toDouble()), -0.5)
                }
                dood.arena.doods.firstOrNull { it != dood && it.contains(currentStep) }?.let { otherDood ->
                    return Pair((i.toDouble() / steps.toDouble()), if (otherDood.team != dood.team) 1.0 else -1.0)
                }
            }
        }
        return Pair(0.0, 0.0)
    }

    fun draw(g: Graphics) {
        val raysult = cast()
        g.color = when (raysult.second) {
            1.0 -> Color.RED
            -0.5 -> Color.DARK_GRAY
            -1.0 -> Color.GREEN
            else -> Color.LIGHT_GRAY
        }
        val lengthCoefficient = if (raysult.first <= 0.0) 1.0 else raysult.first
        g.drawLine(
            line.position.x.toInt(),
            line.position.y.toInt(),
            line.position.x.toInt() + (line.p2Relative.x * lengthCoefficient).toInt(),
            line.position.y.toInt() + (line.p2Relative.y * lengthCoefficient).toInt(),
        )
    }
}