package brains.neat

class Specimen(val genome: Genome) {
    val activations = mutableMapOf<Node, Double>()

    fun calculate(inputs: List<Double>) {
        (inputs zip genome.neat.inputNodes).forEach {
            activations[it.second] = it.first
        }
    }
}