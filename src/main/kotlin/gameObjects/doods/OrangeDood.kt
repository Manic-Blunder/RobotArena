package gameObjects.doods

import brains.neat.Genome
import fuzzyMath.shapes.Vector2
import gameObjects.core.Arena
import gameObjects.core.Dood
import gameObjects.core.Team

class OrangeDood(arena: Arena, genome: Genome, position: Vector2) : Dood(arena, position, Team.ORANGE, genome) {
    override val neuralInputs: ArrayList<Double>
        get() = arrayListOf()

    override fun processNeuralOutputs(outputs: List<Double>) {

    }

    override fun recalculateGenomeScore() {

    }
}