package brains.neat

/*
* This is the NEAT environment. It represents the context that all the genomes are evolving in.
*
* */
open class Neat(inputNodeCount: Int, firstHiddenLayerCount: Int, secondHiddenLayerCount: Int, outputNodeCount: Int, private var maxGenomes: Int, private var minGenomes: Int) {
    var breedingType: BreedType = BreedType.RANDOM
    private var connectionInnovations = 0 // Unique countable ID for connection genes
    private var nodeInnovations = 0 // Unique countable ID for node genes
    val nodes
        get() = inputNodes + firstHiddenLayer + secondHiddenNodeLayer + outputNodes
    val genomes = mutableSetOf<Genome>() // All genomes within the NEAT environment. This should be equal to maxGenomes after each evolve cycle
    val species = mutableSetOf<Species>() // All species within the NEAT environment. This list changes with each evolve cycle as genomes are re-categorized
    val inputNodes = mutableSetOf<Node>()
    val firstHiddenLayer = mutableSetOf<Node>()
    val secondHiddenNodeLayer = mutableSetOf<Node>()
    val outputNodes = mutableSetOf<Node>()

    var generation = 0

    //        Distance values
    var disjointCoefficient = 1.0
        protected set
    var excessCoefficient = 0.5
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
    var speciesFault = 0.0 // The acceptable distance a genome can be from a species' representative, to be categorized into that species
        protected set
    var cullRate = 60.0 // The percent of lowest-scoring genomes in each species that are killed and replaced each cycle
        protected set

    init {
        repeat(inputNodeCount) { inputNodes.add(Node(0.0, it / inputNodeCount.toDouble(), Node.Type.INPUT)) }
        repeat(firstHiddenLayerCount) { firstHiddenLayer.add(Node(0.33, it / firstHiddenLayerCount.toDouble(), Node.Type.FIRST_HIDDEN))}
        repeat(secondHiddenLayerCount) { secondHiddenNodeLayer.add(Node(0.66, it / secondHiddenLayerCount.toDouble(), Node.Type.SECOND_HIDDEN))}
        repeat(outputNodeCount) { outputNodes.add(Node(1.0, it / outputNodeCount.toDouble(), Node.Type.OUTPUT)) }
        repeat(maxGenomes) { genomes.add(Genome(this)) }
    }

    fun evolve() {
        generation++
        println("evolving gen $generation")
//        if (generation % 10 == 0 && maxGenomes >= minGenomes) {
//            maxGenomes--
//        }
//        resetSpecies()
//        categorizeGenomes()
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
        val cullCount = (genomes.size * (cullRate / 100)).toInt()
        genomes.sortedBy { it.score }.take(cullCount).forEach {
            genomes.remove(it)
        }
    }

    private fun reproduce() {
        val selector = WeightedRandomSelector<Genome>()

        genomes.forEach { selector.add(it, it.score) }
        val currentGenomes = genomes.toSet()

        while (genomes.size < maxGenomes) {
            val male = selector.select()
            val breedingCandidates = currentGenomes.shuffled().take(60)
            val mateSelector = WeightedRandomSelector<Genome>()
            breedingCandidates.forEach { mateSelector.add(it, male.similarityTo(it)) }
            val female = mateSelector.select()
            genomes.add(male.crossOver(female))
        }
    }

    private fun mutate() = genomes.forEach { it.mutate() }

    fun printSpecies() {
//        println("NEAT Nodes: ${nodes.size}")
//        println("NEAT Connections: ${connections.size}")
//        genomes.maxByOrNull(Genome::score)?.let { println("${species.size} species present. Best: Score - ${it.score}") }
        genomes.maxByOrNull(Genome::score)?.let { println("${it.score}") }
    }

    enum class BreedType { RANDOM, ALPHA }
}

