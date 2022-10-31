package gameObjects.neats

import brains.neat.Neat

class GreenNeat(maxGenomes: Int, minGenomes: Int) : Neat(64, 32, 16, 4, maxGenomes, minGenomes) {
    //output nodes = length of output array
    //input how many inputs to the neat algorithm
    init {
        //        Differential weights
        disjointCoefficient = 1.0 // how much mismatched connections count against genome similarity
        excessCoefficient = 1.0 // how much excess connections count against genome similarity
        deltaWeightCoefficient = 1.0 // how much connection weight variance counts against genome similarity

        //        Mutation values (1.0 is 100% chance per evolution)
        mutateConnectionChance = 0.8 // chance to form a new connection between two nodes. The new connection will have a random weight
        mutateNodeChance = 0.8 // chance to mutate a new node. The new node wll have both halves of the original connection, plus one new connection
        mutateShiftWeightChance = 0.2 // chance to shift the weight of a connection
        mutateRandomWeightChance = 0.2 // chance to completely randomize the weight of a connection
        mutateToggleConnectionChance = 0.6 // chance to disable/enable a connection. Connections are initially enabled
        mutateDropNodeChance = 0.6  // chance to drop a node (and all associated connections)
        mutateDropConnectionChance = 0.2 // chance to drop a connection
        weightShiftPower = 0.3 // how much the weight can shift
        weightRandomPower = 1.0 // range of the new weight
        mutationRate = 5 // number of attempted mutations per genome per cycle

        //        Evolution values
        speciesFault = 10.0 // The acceptable distance a genome can be from a species' representative, to be categorized into that species
        cullRate = 80.0 // The percent of lowest-scoring genomes in each species that are killed and replaced each cycle
    }
}