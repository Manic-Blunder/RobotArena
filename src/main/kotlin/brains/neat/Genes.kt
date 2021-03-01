package brains.neat

open class Gene(val innovationNumber: Int) {
    fun equals(other: Gene) = innovationNumber == other.innovationNumber
}

class NodeGene(val x: Double, val y: Double, val type: Type, innovationNumber: Int) : Gene(innovationNumber) {
    enum class Type { INPUT, OUTPUT, HIDDEN }
}

class ConnectionGene(val from: NodeGene, val to: NodeGene, innovationNumber: Int) : Gene(innovationNumber)

class WeightGene(val connection: ConnectionGene, val weight: Double, val inverted: Boolean) : Gene(connection.innovationNumber) {
    val from
        get() = connection.from
    val to
        get() = connection.to
}