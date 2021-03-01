package brains.neat.calculations

import brains.neat.Gene

class Connection(val from: Node, val to: Node, innovationNumber: Int) : Gene(innovationNumber) {
    var weight = 0.0
    var inverted = true
    val output
        get() = if (inverted) 1.0 - from.output else from.output
}