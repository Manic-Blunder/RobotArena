package brains.neat.calculations

import kotlin.math.exp

class Node(val x: Double) {
    var output: Double = 0.0
    var connections = arrayListOf<Connection>()
    val weightedOutput
        get() = connections.sumByDouble { it.weight * (it.output) }

    fun recalculate(): Double {
        output = sigmoid(weightedOutput)
        return output
    }

    private fun sigmoid(n: Double): Double = 1.0 / (1 + exp(-n))
}