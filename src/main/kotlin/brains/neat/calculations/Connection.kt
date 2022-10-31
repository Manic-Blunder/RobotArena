package brains.neat.calculations

class Connection(val from: CalcNode, val to: CalcNode) {
    var weight = 0.0
    var inverted = true
    val output
        get() = if (inverted) 1.0 - from.output else from.output
}