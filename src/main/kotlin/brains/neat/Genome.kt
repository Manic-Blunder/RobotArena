package brains.neat

import brains.neat.calculations.Calculator
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

class Genome(val neat: Neat) : Comparable<Genome> {
    var species: Species? = null
    var score = 0.0
    val weights = mutableSetOf<WeightGene>()
    val nodes
        get() = (weights.flatMap { listOf(it.from, it.to) } + neat.inputNodes + neat.outputNodes).toSet()
    private val innovationNumber
        get() = weights.lastOrNull()?.innovationNumber ?: 0

    fun calculate(inputs: List<Double>): List<Double> = Calculator(this).calculate(inputs)

    private fun addWeightGene(weightGene: WeightGene) {
        if (weights.none { it.connection == weightGene.connection }) {
            weights.add(weightGene)
        }
    }

    fun distanceFrom(other: Genome): Double {
        if (innovationNumber < other.innovationNumber) return other.distanceFrom(this)
        var i = 0
        var j = 0
        var disjoint = 0
        var similar = 0
        var deltaWeight = 0.0

        val ourWeights = weights.sortedBy { it.innovationNumber }
        val theirWeights = other.weights.sortedBy { it.innovationNumber }

        while (i < ourWeights.size && j < theirWeights.size) {
            val ourWeight = ourWeights[i]
            val theirWeight = theirWeights[j]
            when {
                ourWeight.innovationNumber == theirWeight.innovationNumber -> {
                    similar++
                    deltaWeight += abs(ourWeight.weight - theirWeight.weight)
                    i++
                    j++
                }
                ourWeight.innovationNumber > theirWeight.innovationNumber -> {
                    disjoint++
                    j++
                }
                else -> {
                    disjoint++
                    i++
                }
            }
        }

        val excess = ourWeights.size - i
        val highestConnections = max(weights.size, other.weights.size)
        val n = if (highestConnections > 20) 1 else 20
        return neat.disjointCoefficient * disjoint / n + neat.excessCoefficient * excess / n + neat.deltaWeightCoefficient * deltaWeight
    }

    fun crossOver(other: Genome): Genome {
        val newGenome = Genome(neat)
        var i = 0
        var j = 0

        val ourWeights = weights.sortedBy { it.innovationNumber }
        val theirWeights = other.weights.sortedBy { it.innovationNumber }

        while (i < ourWeights.size && j < theirWeights.size) {
            val ourWeightGene = ourWeights[i]
            val theirWeightGene = theirWeights[j]
            when {
                ourWeightGene.innovationNumber == theirWeightGene.innovationNumber -> {
                    if (Math.random() > 0.5) {
                        newGenome.addWeightGene(WeightGene(ourWeightGene.connection, ourWeightGene.weight, ourWeightGene.inverted))
                    } else {
                        newGenome.addWeightGene(WeightGene(theirWeightGene.connection, theirWeightGene.weight, theirWeightGene.inverted))
                    }
                    i++
                    j++
                }
                ourWeightGene.innovationNumber > theirWeightGene.innovationNumber -> {
                    if (Math.random() > 0.5) {
                        newGenome.addWeightGene(WeightGene(theirWeightGene.connection, theirWeightGene.weight, theirWeightGene.inverted))
                    }
                    j++
                }
                else -> {
                    if (Math.random() > 0.5) {
                        newGenome.addWeightGene(WeightGene(ourWeightGene.connection, ourWeightGene.weight, ourWeightGene.inverted))
                    }
                    i++
                }
            }
        }

        while (i < weights.size) {
            ourWeights[i].let { weightGene ->
                newGenome.addWeightGene(WeightGene(weightGene.connection, weightGene.weight, weightGene.inverted))
            }
            i++
        }
//
        while (j < theirWeights.size) {
            theirWeights[j].let { weightGene ->
                newGenome.addWeightGene(WeightGene(weightGene.connection, weightGene.weight, weightGene.inverted))
            }
            j++
        }

        return newGenome
    }

    fun mutate() {
        if (neat.mutateDropNodeChance > Math.random()) mutateDropNode()
        if (neat.mutateDropConnectionChance > Math.random()) mutateDropConnection()
        if (neat.mutateConnectionChance > Math.random()) mutateNewConnection(null)
        if (neat.mutateNodeChance > Math.random()) mutateNewNode()
        if (neat.mutateRandomWeightChance > Math.random()) mutateRandomizeConnectionWeight()
        if (neat.mutateShiftWeightChance > Math.random()) mutateShiftConnectionWeight()
        if (neat.mutateToggleConnectionChance > Math.random()) mutateToggleConnection()
    }

    private fun mutateNewConnection(nodeGene: NodeGene?) {
        for (i in (0 until 100)) {
            val nodeA = nodes.randomOrNull()
//            val nodeB = nodeGene ?: if (Math.random() > 0.4) nodes.randomOrNull() else nodes.filter { it.type == NodeGene.Type.OUTPUT }.randomOrNull()
            val nodeB = nodeGene ?: nodes.randomOrNull()
            if (nodeA != null && nodeB != null) {
                if (nodeA.x == nodeB.x) {
                    continue
                } else {
                    val from = if (nodeA.x < nodeB.x) nodeA else nodeB
                    val to = if (nodeA.x < nodeB.x) nodeB else nodeA
                    val existingConnection = neat.connections.firstOrNull { it.from == from && it.to == to }
                    if (existingConnection == null) {
                        addWeightGene(WeightGene(neat.getConnection(from, to), (Math.random() * 2 - 1), Random.nextBoolean()))
                        return
                    } else {
                        if (weights.none { it.connection == existingConnection }) {
                            addWeightGene(WeightGene(existingConnection, (Math.random() * 2 - 1) * neat.weightRandomPower, Random.nextBoolean()))
                            return
                        } else {
                            continue
                        }
                    }
                }
            }
        }
    }

    private fun mutateNewNode() {
        val nodeMod = nodes.size / neat.maxNodes.toFloat()
        if (nodeMod < Math.random()) {
            weights.randomOrNull()?.let { weightGene ->
                val connection = weightGene.connection

                val potentialExistingMiddleNodes = neat.connections.filter { it.from == weightGene.from }.map { it.to }
                val existingMiddleConnection = neat.connections.firstOrNull { it.to == weightGene.to && it.from in potentialExistingMiddleNodes }
                val middle = existingMiddleConnection?.from ?: neat.newNode((connection.from.x + connection.to.x) / 2.0, (connection.from.y + connection.to.y) / 2.0, NodeGene.Type.HIDDEN)

                addWeightGene(WeightGene(neat.getConnection(connection.from, middle), 1.0, true))
                addWeightGene(WeightGene(neat.getConnection(middle, connection.to), weightGene.weight, weightGene.inverted))
                weights.remove(weightGene)
                mutateNewConnection(middle)
            }
        }
    }

    private fun mutateShiftConnectionWeight() {
        weights.randomOrNull()?.let {
            weights.remove(it)
            addWeightGene(WeightGene(it.connection, it.weight + (Math.random() * 2 - 1) * neat.weightShiftPower, it.inverted))
        }
    }

    private fun mutateRandomizeConnectionWeight() {
        weights.randomOrNull()?.let {
            weights.remove(it)
            addWeightGene(WeightGene(it.connection, (Math.random() * 2 - 1) * neat.weightRandomPower, it.inverted))
        }
    }

    private fun mutateToggleConnection() {
        weights.randomOrNull()?.let {
            weights.remove(it)
            addWeightGene(WeightGene(it.connection, it.weight, !it.inverted))
        }
    }

    private fun mutateDropConnection() {
        weights.randomOrNull()?.let {
            weights.remove(it)
        }
    }

    private fun mutateDropNode() {
//        nodes.filter { it.type == NodeGene.Type.HIDDEN }.randomOrNull()?.let { node -> weights.removeIf { weight -> weight.from == node || weight.to == node } }
    }

    override fun compareTo(other: Genome): Int {
        return when {
            other.innovationNumber > innovationNumber -> -1
            other.innovationNumber < innovationNumber -> 1
            else -> 0
        }
    }
}