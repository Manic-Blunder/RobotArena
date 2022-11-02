package brains.neat

import brains.neat.calculations.Calculator
import fuzzyMath.sigmoid
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class Genome(val neat: Neat) {
    var species: Species? = null
    var score = 0.0
    var connections = mutableMapOf<Pair<Node, Node>, Connection>()
    var calculator = Calculator(this)

    fun rebuildCalculations() {
        calculator = Calculator(this)
    }

    fun calculate(inputs: List<Double>): List<Double> {
        return calculator.calculate(inputs)
    }

    private fun addWeightGene(toFrom: Pair<Node, Node>, connection: Connection) {
        connections[toFrom] = connection
    }

    fun similarityTo(other: Genome): Double {
        var similar = 0.0
        var shared = 0

        connections.forEach { ours ->
            other.connections[ours.key]?.let { theirs ->
                similar += (0.5 - abs(sigmoid(ours.value.weight - sigmoid(theirs.weight)))) * 5
                shared += 1
            }
        }

        val excess = (connections.size - shared) + (other.connections.size - shared)
        return max(similar - excess * neat.excessCoefficient, 1.0)
    }

    fun crossOver(other: Genome): Genome {
        val newGenome = Genome(neat)
        connections.forEach { weight ->
            val otherWeight = other.connections[weight.key]
            if(otherWeight != null || Random.nextBoolean()) {
                newGenome.connections[weight.key] = weight.value
            }
        }
        other.connections.forEach { weight ->
            if(Random.nextBoolean()) {
                newGenome.connections[weight.key] = weight.value
            }
        }
        return newGenome
    }

    fun mutate() {
        if (neat.mutateDropConnectionChance > Math.random()) mutateDropConnection()
        if (neat.mutateConnectionChance > Math.random()) mutateNewConnection()
        if (neat.mutateRandomWeightChance > Math.random()) mutateRandomizeConnectionWeight()
        if (neat.mutateShiftWeightChance > Math.random()) mutateShiftConnectionWeight()
        if (neat.mutateToggleConnectionChance > Math.random()) mutateInvertConnection()
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
                connections[Pair(leftNode, rightNode)] = Connection((Math.random() * 2 - 1) * neat.weightRandomPower, Random.nextBoolean())
            }
        }
    }

    private fun mutateShiftConnectionWeight() {
        connections.keys.randomOrNull()?.let { key ->
            connections[key]?.let { currentWeight ->
                connections[key] = Connection(currentWeight.weight + (Math.random() * 2 - 1) * neat.weightShiftPower, currentWeight.inverted)
            }
        }
    }

    private fun mutateInvertConnection() {
        connections.keys.randomOrNull()?.let { key ->
            connections[key]?.let { currentWeight ->
                connections[key] = Connection(currentWeight.weight * -1, !currentWeight.inverted)
            }
        }
    }

    private fun mutateRandomizeConnectionWeight() {
        connections.keys.randomOrNull()?.let { key ->
            connections[key]?.let { currentWeight ->
                connections[key] = Connection((Math.random() * 2 - 1) * neat.weightRandomPower, currentWeight.inverted)
            }
        }
    }

    private fun mutateDropConnection() {
        connections.keys.randomOrNull()?.let { connections.remove(it) }
    }
}