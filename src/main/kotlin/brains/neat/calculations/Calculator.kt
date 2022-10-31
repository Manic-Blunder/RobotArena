package brains.neat.calculations

import brains.neat.Genome
import brains.neat.Node

class Calculator(genome: Genome) {
    val inputNodes = arrayListOf<CalcNode>()
    val firstHiddenNodes = arrayListOf<CalcNode>()
    val secondHiddenNodes = arrayListOf<CalcNode>()
    val outputNodes = arrayListOf<CalcNode>()

    init {
        val nodeHash = hashMapOf<Node, CalcNode>()

        // TODO: 12/27/2020 replace this with better determination of input/output nodes
        genome.neat.inputNodes.forEach {
            val newNode = CalcNode(it.x)
            nodeHash[it] = newNode
            inputNodes.add(newNode)
        }

        genome.neat.firstHiddenLayer.forEach {
            val newNode = CalcNode(it.x)
            nodeHash[it] = newNode
            firstHiddenNodes.add(newNode)
        }

        genome.neat.secondHiddenNodeLayer.forEach {
            val newNode = CalcNode(it.x)
            nodeHash[it] = newNode
            secondHiddenNodes.add(newNode)
        }

        genome.neat.outputNodes.forEach {
            val newNode = CalcNode(it.x)
            nodeHash[it] = newNode
            outputNodes.add(newNode)
        }

        genome.weights.forEach {
            val nodeFrom = nodeHash[it.key.first]
            val nodeTo = nodeHash[it.key.second]
            if (nodeTo != null && nodeFrom != null) {
                val newConnection = Connection(nodeFrom, nodeTo) // TODO: 12/27/2020 Innovation Number
                newConnection.weight = it.value
                newConnection.inverted = false // it.inverted
                nodeTo.connectionsTo.add(newConnection)
            }
        }
    }

    fun calculate(inputs: List<Double>): List<Double> {
        (inputNodes zip inputs).forEach { it.first.output = it.second }
        firstHiddenNodes.forEach { it.recalculate() }
        secondHiddenNodes.forEach { it.recalculate() }
        return outputNodes.map(CalcNode::recalculate)
    }
}