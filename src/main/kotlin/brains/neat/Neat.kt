package brains.neat

import fuzzyMath.max

/*
* This is the NEAT environment. It represents the context that all the genomes are evolving in.
*
* */
open class Neat(inputNodeCount: Int, outputNodeCount: Int, private var maxGenomes: Int, private var minGenomes: Int) {
    var breedingType: BreedType = BreedType.RANDOM
    val maxNodes = inputNodeCount * outputNodeCount
    private var connectionInnovations = 0 // Unique countable ID for connection genes
    private var nodeInnovations = 0 // Unique countable ID for node genes
    val connections // arrayListOf<ConnectionGene>() // All connections genes within this NEAT environment, so multiple Genomes don't duplicate connections
        get() = genomes.flatMap { it.weights.map { weight -> weight.connection } }.toSet()
    val nodes //  listOf<NodeGene>() // All node genes within this NEAT environment, so multiple Genomes don't duplicate nodes
        get() = connections.flatMap { listOf(it.to, it.from) }.toSet()
    val genomes = mutableSetOf<Genome>() // All genomes within the NEAT environment. This should be equal to maxGenomes after each evolve cycle
    val species = mutableSetOf<Species>() // All species within the NEAT environment. This list changes with each evolve cycle as genomes are re-categorized
    val inputNodes = mutableSetOf<NodeGene>()
    val outputNodes = mutableSetOf<NodeGene>()

    var generation = 0

    //        Distance values
    var disjointCoefficient = 1.0
        protected set
    var excessCoefficient = 1.0
        protected set
    var deltaWeightCoefficient = 1.0
        protected set

    //        Mutation values
    var weightShiftPower = 0.3
        protected set
    var weightRandomPower = 1.0
        protected set
    var biasShiftPower = 0.3
        protected set
    var biasRandomPower = 1.0
        protected set
    var mutateConnectionChance = 0.4
        protected set
    var mutateNodeChance = 0.4
        protected set
    var mutateShiftWeightChance = 0.4
        protected set
    var mutateRandomWeightChance = 0.4
        protected set
    var mutateShiftBiasChance = 0.4
        protected set
    var mutateRandomBiasChance = 0.4
        protected set
    var mutateToggleConnectionChance = 0.4
        protected set
    var mutateDropNodeChance = 0.4
        protected set
    var mutateDropConnectionChance = 0.4
        protected set
    var mutationRate = 1
        protected set

    //        Evolution values
    var speciesFault = 5.0 // The acceptable distance a genome can be from a species' representative, to be categorized into that species
        protected set
    var cullRate = 60.0 // The percent of lowest-scoring genomes in each species that are killed and replaced each cycle
        protected set

    init {
        repeat(inputNodeCount) { inputNodes.add(newNode(0.0, it / inputNodeCount.toDouble(), NodeGene.Type.INPUT)) }
        repeat(outputNodeCount) { outputNodes.add(newNode(1.0, it / outputNodeCount.toDouble(), NodeGene.Type.OUTPUT)) }
        repeat(maxGenomes) { genomes.add(Genome(this)) }
    }

    fun evolve() {
        generation++
//        if (generation % 10 == 0 && maxGenomes >= minGenomes) {
//            maxGenomes--
//        }
        resetSpecies()
        categorizeGenomes()
        cull()
        reproduce()
        mutate()
    }

    private fun resetSpecies() = species.forEach(Species::reset)

    private fun categorizeGenomes() = genomes.filter { it.species == null }.forEach { categorizeGenome(it) }

    private fun categorizeGenome(genome: Genome) {
        if (species.none { s -> s.put(genome) }) species.add(Species(this, genome))
    }

    private fun cull() {
        species.forEach { it.cull(cullRate) }
        species.removeAll { it.size <= 1 }
        genomes.removeAll { it.species == null }
    }

    private fun reproduce() {
        val selector = WeightedRandomSelector<Species>()
        species.forEach { selector.add(it, it.score) }

        while (genomes.size < maxGenomes) {
            val randomSpecies = selector.select()
            randomSpecies?.let {
                it.breed()?.let { child ->
                    genomes.add(child)
                    it.forcePut(child)
                }
            }
        }
    }

    private fun mutate() = genomes.forEach { genome -> repeat(mutationRate) { genome.mutate() } }

    //    this is come gross shit
    fun getConnection(nodeA: NodeGene, nodeB: NodeGene): ConnectionGene {
        val existingConnection = connections.firstOrNull { it.from == nodeA && it.to == nodeB }
        val innovationNumber = existingConnection?.innovationNumber ?: connectionInnovations++
        val connectionGene = ConnectionGene(nodeA, nodeB, innovationNumber)
//        if (existingConnection == null) connections.add(connectionGene)
        return connectionGene
    }

    fun newNode(x: Double, y: Double, type: NodeGene.Type): NodeGene {
        val node = NodeGene(x, y, type, nodeInnovations++)
//        nodes.add(node)
        return node
    }

    fun printSpecies() {
//        println("NEAT Nodes: ${nodes.size}")
//        println("NEAT Connections: ${connections.size}")
//        species.maxByOrNull(Species::score)?.let { println("${species.size} species present. Best: Score - ${it.score} Size - ${it.size}") }
        species.maxByOrNull(Species::score)?.let { println("${it.score}") }
    }

    enum class BreedType { RANDOM, ALPHA }
}

