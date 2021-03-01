package brains.neat

fun main() {
    val neat = Neat(70, 7, 120, 20)
    val inputs = listOf(
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
        Math.random(),
    )

    repeat(500) {
//        val time = System.currentTimeMillis()
        neat.genomes.forEach {
            it.score = it.calculate(inputs).first()
        }
        neat.printSpecies()
        neat.evolve()
//        println("Looped in ${System.currentTimeMillis() - time}")
    }

    val clientGenomes = neat.genomes
    val liveNodeGenes = mutableSetOf<NodeGene>()
    val nodesGenes = mutableSetOf<NodeGene>()
    clientGenomes.forEach { genome ->
        genome.nodes.forEach { node ->
            if (genome.weights.any { connection -> connection.from == node }) liveNodeGenes.add(node)
            nodesGenes.add(node)
        }
    }
    println("${neat.connections.size} connections in environment")
    println("${neat.nodes.size} nodes in environment")
    println("${nodesGenes.size} nodes among genes")
    println("${liveNodeGenes.size} live nodes among genes")
}