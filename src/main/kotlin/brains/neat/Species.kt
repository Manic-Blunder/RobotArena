package brains.neat

class Species(val neat: Neat, var representative: Genome) {
    private val genomes = mutableSetOf(representative)
    val score
        get() = genomes.sumByDouble { it.score } / genomes.size.toDouble()
    val size
        get() = genomes.size

    init {
        representative.species = this
    }

    fun put(genome: Genome): Boolean {
        if (genome.distanceFrom(representative) < neat.speciesFault + representative.neat.species.size) {
            genome.species = this
            genomes.add(genome)
            return true
        }
        return false
    }

    fun forcePut(genome: Genome) {
        genome.species = this
        genomes.add(genome)
    }

    private fun goExtinct() = genomes.forEach { it.species = null }

    fun reset() {
        genomes.random().let {
            goExtinct()
            genomes.clear()
            forcePut(it)
        }
    }

    fun cull(percentage: Double) {
        val cullCount = (genomes.size * (percentage / 100)).toInt()
        genomes.sortedBy { it.score }.take(cullCount).forEach {
            it.species = null
            genomes.remove(it)
        }
        if (size <= 1) goExtinct()
    }

    fun breed(): Genome {
        return when (neat.breedingType) {
            Neat.BreedType.RANDOM -> randomBreed()
            Neat.BreedType.ALPHA -> alphaBreed() ?: randomBreed()
        }
    }

    private fun randomBreed(): Genome {
        val first = genomes.random()
        val second = genomes.random()
        return if (first.score > second.score) first.crossOver(second) else second.crossOver(first)
    }

    private fun alphaBreed(): Genome? {
        val randomGenome = genomes.filter { it.score > 0 }.randomOrNull() ?: genomes.random()
        return genomes.maxByOrNull { it.score }?.let { bestGenome ->
            bestGenome.crossOver(randomGenome)
        }
    }
}