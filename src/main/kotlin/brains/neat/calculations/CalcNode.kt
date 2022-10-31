package brains.neat.calculations

import kotlin.math.exp

class CalcNode(val x: Double) {
    var output: Double = 0.0
    var connectionsTo = arrayListOf<Connection>()
    val weightedOutput
        get() = connectionsTo.sumByDouble { it.weight * (it.output) }

    fun recalculate(): Double {
        output = sigmoid(weightedOutput)
        return output
    }

    private fun sigmoid(n: Double): Double = 1.0 / (1 + exp(-n))
}