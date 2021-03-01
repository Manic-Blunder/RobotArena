package gameObjects.neats

import brains.neat.Neat

class ZoonerNeat(maxGenomes: Int, minGenomes: Int) : Neat(19, 4, maxGenomes, minGenomes) {
    init {
        breedingType = BreedType.ALPHA

        //        Differential weights
        disjointCoefficient = 1.0 // how much mismatched connections count against genome similarity
        excessCoefficient = 1.0 // how much excess connections count against genome similarity
        deltaWeightCoefficient = 1.0 // how much connection weight variance counts against genome similarity

        //        Mutation values (1.0 is 100% chance per evolution)
        mutateConnectionChance = 0.6 // chance to form a new connection between two nodes. The new connection will have a random weight
        mutateNodeChance = 0.6 // chance to mutate a new node. The new node wll have both halves of the original connection, plus one new connection
        mutateShiftWeightChance = 1.0 // chance to shift the weight of a connection
        mutateRandomWeightChance = 0.0 // chance to completely randomize the weight of a connection
        mutateToggleConnectionChance = 0.1 // chance to disable/enable a connection. Connections are initially enabled
        mutateDropNodeChance = 0.2  // chance to drop a node (and all associated connections)
        mutateDropConnectionChance = 0.6 // chance to drop a connection
        weightShiftPower = 0.1 // how much the weight can shift
        weightRandomPower = 0.0 // range of the new weight
        mutationRate = 1 // number of attempted mutations per genome per cycle

        //        Evolution values
        speciesFault = 5.0 // The acceptable distance a genome can be from a species' representative, to be categorized into that species
        cullRate = 50.0 // The percent of lowest-scoring genomes in each species that are killed and replaced each cycle
    }
}