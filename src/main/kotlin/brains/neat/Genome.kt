package brains.neat

import brains.neat.calculations.Calculator
import fuzzyMath.sigmoid
import java.awt.RadialGradientPaint
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class Genome(val neat: Neat) {
    var species: Species? = null
    var score = 0.0
    var weights = mutableMapOf<Pair<Node, Node>, Double>()

    fun calculate(inputs: List<Double>): List<Double> {
        return Calculator(this).calculate(inputs)
    }

    private fun addWeightGene(toFrom: Pair<Node, Node>, weight: Double) {
        weights[toFrom] = weight
    }

    fun similarityTo(other: Genome): Double {
        var similar = 0.0
        var shared = 0

        weights.forEach { ours ->
            other.weights[ours.key]?.let { theirs ->
                similar += (0.5 - abs(sigmoid(ours.value - sigmoid(theirs)))) * 5
                shared += 1
            }
        }

        val excess = (weights.size - shared) + (other.weights.size - shared)
        return max(similar - excess * neat.excessCoefficient, 1.0)
    }

    fun crossOver(other: Genome): Genome {
        val newGenome = Genome(neat)
        weights.forEach { weight ->
            val otherWeight = other.weights[weight.key]
            if(otherWeight != null || Random.nextBoolean()) {
                newGenome.weights[weight.key] = weight.value
            }
        }
        other.weights.forEach { weight ->
            if(Random.nextBoolean()) {
                newGenome.weights[weight.key] = weight.value
            }
        }
        return newGenome
    }

    fun mutate() {
        if (neat.mutateDropConnectionChance > Math.random()) mutateDropConnection()
        if (neat.mutateConnectionChance > Math.random()) mutateNewConnection()
        if (neat.mutateRandomWeightChance > Math.random()) mutateRandomizeConnectionWeight()
        if (neat.mutateShiftWeightChance > Math.random()) mutateShiftConnectionWeight()
    }

    private fun mutateNewConnection() {
        (neat.inputNodes + neat.firstHiddenLayer + neat.secondHiddenNodeLayer).randomOrNull()?.let { leftNode ->
            val rightNodes = when (leftNode.type) {
                Node.Type.INPUT -> neat.firstHiddenLayer + neat.secondHiddenNodeLayer + neat.outputNodes
                Node.Type.FIRST_HIDDEN -> neat.secondHiddenNodeLayer + neat.outputNodes
                Node.Type.SECOND_HIDDEN -> neat.outputNodes
                Node.Type.OUTPUT -> mutableSetOf()
            }
            rightNodes.randomOrNull()?.let { rightNode ->
                weights[Pair(leftNode, rightNode)] = (Math.random() * 2 - 1) * neat.weightRandomPower
            }
        }
    }

    private fun mutateShiftConnectionWeight() {
        weights.keys.randomOrNull()?.let { key ->
            weights[key]?.let { currentWeight ->
                weights[key] = currentWeight + (Math.random() * 2 - 1) * neat.weightShiftPower
            }
        }
    }

    private fun mutateRandomizeConnectionWeight() {
        weights.keys.randomOrNull()?.let { weights[it] = (Math.random() * 2 - 1) * neat.weightRandomPower }
    }

    private fun mutateDropConnection() {
        weights.keys.randomOrNull()?.let { weights.remove(it) }
    }
}