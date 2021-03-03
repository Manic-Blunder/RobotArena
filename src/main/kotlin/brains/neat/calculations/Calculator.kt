package brains.neat.calculations

import brains.neat.Genome
import fuzzyMath.sigmoid

class Calculator(genome: Genome) {
    val inputNodes = arrayListOf<Node>()
    val hiddenNodes = arrayListOf<Node>()
    val outputNodes = arrayListOf<Node>()

    init {
        val nodeHash = hashMapOf<Int, Node>()

        // TODO: 12/27/2020 replace this with better determination of input/output nodes
        genome.nodes.forEach {
            val newNode = Node(it.x)
            nodeHash[it.innovationNumber] = newNode
            when {
                newNode.x <= 0.1 -> inputNodes.add(newNode)
                newNode.x >= 0.9 -> outputNodes.add(newNode)
                else -> hiddenNodes.add(newNode)
            }
        }

        hiddenNodes.sortBy { it.x }

        genome.weights.forEach {
            val nodeFrom = nodeHash[it.from.innovationNumber]
            val nodeTo = nodeHash[it.to.innovationNumber]
            if (nodeTo != null && nodeFrom != null) {
                val newConnection = Connection(nodeFrom, nodeTo, 0) // TODO: 12/27/2020 Innovation Number
                newConnection.weight = it.weight
                newConnection.inverted = it.inverted
                nodeTo.connections.add(newConnection)
            }
        }
    }

    fun calculate(inputs: List<Double>): List<Double> {
        (inputNodes zip inputs).forEach { it.first.output = it.second }
        hiddenNodes.forEach { it.recalculate() }
        return outputNodes.map(Node::recalculate)
    }
}